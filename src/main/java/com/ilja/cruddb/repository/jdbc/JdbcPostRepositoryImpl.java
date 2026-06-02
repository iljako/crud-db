package com.ilja.cruddb.repository.jdbc;

import com.ilja.cruddb.model.Label;
import com.ilja.cruddb.model.Post;
import com.ilja.cruddb.model.PostStatus;
import com.ilja.cruddb.repository.PostRepository;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.*;

import static com.ilja.cruddb.utils.JdbcUtils.getPreparedStatement;

public class JdbcPostRepositoryImpl implements PostRepository {

    public JdbcPostRepositoryImpl() {
    }

    @Override
    public Post save(Post post) {
        String sql = post.getId() == null ? "INSERT INTO posts (content, created, updated, status, writer_id) VALUES (?, ?, ?, ?::post_status, ?)" : "UPDATE posts SET content = ?, updated = ?, status = ?::post_status WHERE id = ?";

        try (PreparedStatement stmt = getPreparedStatement(sql)) {

            stmt.setString(1, post.getContent());
            if (post.getId() == null) {
                stmt.setObject(2, LocalDateTime.now());
                stmt.setObject(3, LocalDateTime.now());
                stmt.setString(4, post.getStatus().name());
                stmt.setLong(5, 1L);
            } else {
                stmt.setObject(2, LocalDateTime.now());
                stmt.setString(3, post.getStatus().name());
                stmt.setLong(4, post.getId());
            }
            stmt.executeUpdate();

            if (post.getId() == null) {
                try (ResultSet rs = stmt.getGeneratedKeys()) {
                    if (rs.next()) {
                        post.setId(rs.getLong(1));
                    }
                }
            }

            saveLabelsAndLinks(post);

            return post;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Optional<Post> findById(Long id) {
        String sql = "SELECT p.id AS post_id, p.content, p.status, p.created, p.updated, " + "l.id AS label_id, l.name AS label_name " + "FROM posts p " + "LEFT JOIN post_labels pl ON p.id = pl.post_id " + "LEFT JOIN labels l ON pl.label_id = l.id " + "WHERE p.id = ?";
        try (PreparedStatement stmt = getPreparedStatement(sql)) {

            stmt.setLong(1, id);
            Post post = null;
            try (ResultSet rs = stmt.executeQuery()) {
                List<Post> posts = mapResultSetToPosts(rs);
                return posts.isEmpty() ? Optional.empty() : Optional.of(posts.get(0));
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<Post> findAll() {
        String sql = "SELECT p.id AS post_id, p.content, p.status, p.created, p.updated, " + "l.id AS label_id, l.name AS label_name " + "FROM posts p " + "LEFT JOIN post_labels pl ON p.id = pl.post_id " + "LEFT JOIN labels l ON pl.label_id = l.id " + "ORDER BY p.created DESC";

        try (PreparedStatement stmt = getPreparedStatement(sql)) {
            Map<Long, Post> postMap = new LinkedHashMap<>();

            try (ResultSet rs = stmt.executeQuery()) {
                return mapResultSetToPosts(rs);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void deleteById(Long id) {
        String sql = "DELETE FROM posts WHERE id = ?";
        try (PreparedStatement stmt = getPreparedStatement(sql)) {

            stmt.setLong(1, id);
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<Post> findByWriterId(Long writerId) {
        String sql = "SELECT p.id AS post_id, p.content, p.status, p.created, p.updated, " + "l.id AS label_id, l.name AS label_name " + "FROM posts p " + "LEFT JOIN post_labels pl ON p.id = pl.post_id " + "LEFT JOIN labels l ON pl.label_id = l.id " + "WHERE p.writer_id = ? " + "ORDER BY p.created DESC";

        try (PreparedStatement stmt = getPreparedStatement(sql)) {
            stmt.setLong(1, writerId);

            Map<Long, Post> postMap = new LinkedHashMap<>();

            try (ResultSet rs = stmt.executeQuery()) {
                return mapResultSetToPosts(rs);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private void saveLabelsAndLinks(Post post) throws SQLException {
        Long postId = post.getId();

        String deleteLinksSql = "DELETE FROM post_labels WHERE post_id = ?";
        try (PreparedStatement delStmt = getPreparedStatement(deleteLinksSql)) {
            delStmt.setLong(1, postId);
            delStmt.executeUpdate();
        }

        for (Label label : post.getLabels()) {
            if (label.getId() == null) {
                Long existingId = getLabelIdByName(label.getName());
                if (existingId != null) {
                    label.setId(existingId);
                } else {
                    String insertLabelSql = "INSERT INTO labels (name) VALUES (?)";
                    try (PreparedStatement labelStmt = getPreparedStatement(insertLabelSql)) {
                        labelStmt.setString(1, label.getName());
                        labelStmt.executeUpdate();
                        try (ResultSet rs = labelStmt.getGeneratedKeys()) {
                            if (rs.next()) {
                                label.setId(rs.getLong(1));
                            }
                        }
                    }
                }
            } else {
                String updateLabelSql = "UPDATE labels SET name = ? WHERE id = ?";
                try (PreparedStatement labelStmt = getPreparedStatement(updateLabelSql)) {
                    labelStmt.setString(1, label.getName());
                    labelStmt.setLong(2, label.getId());
                    labelStmt.executeUpdate();
                }
            }

            String insertLinkSql = "INSERT INTO post_labels (post_id, label_id) VALUES (?, ?)";
            try (PreparedStatement linkStmt = getPreparedStatement(insertLinkSql)) {
                linkStmt.setLong(1, postId);
                linkStmt.setLong(2, label.getId());
                linkStmt.executeUpdate();
            }
        }
    }

    private Long getLabelIdByName(String name) throws SQLException {
        String sql = "SELECT id FROM labels WHERE name = ?";
        try (PreparedStatement stmt = getPreparedStatement(sql)) {
            stmt.setString(1, name);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getLong("id");
                }
            }
        }
        return null;
    }

    private List<Post> mapResultSetToPosts(ResultSet rs) throws SQLException {
        Map<Long, Post> postMap = new LinkedHashMap<>();

        while (rs.next()) {
            Long postId = rs.getLong("post_id");

            Post post = postMap.get(postId);
            if (post == null) {
                post = new Post(postId, rs.getString("content"), PostStatus.valueOf(rs.getString("status")));
                post.setCreated(rs.getObject("created", LocalDateTime.class));
                post.setUpdated(rs.getObject("updated", LocalDateTime.class));
                postMap.put(postId, post);
            }

            Long labelId = rs.getObject("label_id", Long.class);
            if (labelId != null) {
                Label label = new Label(labelId, rs.getString("label_name"));
                post.getLabels().add(label);
            }
        }

        return new ArrayList<>(postMap.values());
    }
}