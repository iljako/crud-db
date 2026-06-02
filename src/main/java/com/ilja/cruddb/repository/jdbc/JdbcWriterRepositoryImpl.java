package com.ilja.cruddb.repository.jdbc;

import com.ilja.cruddb.model.Label;
import com.ilja.cruddb.model.Post;
import com.ilja.cruddb.model.PostStatus;
import com.ilja.cruddb.model.Writer;
import com.ilja.cruddb.repository.WriterRepository;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDateTime;
import java.util.*;

import static com.ilja.cruddb.utils.JdbcUtils.getPreparedStatement;
import static com.ilja.cruddb.utils.JdbcUtils.getStatement;

public class JdbcWriterRepositoryImpl implements WriterRepository {

    public JdbcWriterRepositoryImpl() {

    }

    @Override
    public Writer save(Writer writer) {
        String sql = writer.getId() == null ?
                "INSERT INTO writers (first_name, last_name) VALUES (?, ?)" :
                "UPDATE writers SET first_name = ?, last_name = ? WHERE id = ?";

        try (PreparedStatement stmt = getPreparedStatement(sql)) {

            stmt.setString(1, writer.getFirstName());
            stmt.setString(2, writer.getLastName());
            if (writer.getId() != null) {
                stmt.setLong(3, writer.getId());
            }
            stmt.executeUpdate();

            if (writer.getId() == null) {
                try (ResultSet rs = stmt.getGeneratedKeys()) {
                    if (rs.next()) {
                        writer.setId(rs.getLong(1));
                    }
                }
            }
            return writer;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Optional<Writer> findById(Long id) {
        String sql = "SELECT * FROM writers WHERE id = ?";
        try (PreparedStatement stmt = getPreparedStatement(sql)) {

            stmt.setLong(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                List<Writer> writers = mapResultSetToWriters(rs);
                return writers.isEmpty() ? Optional.empty() : Optional.of(writers.get(0));
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<Writer> findAll() {
        String sql = "SELECT * FROM writers ORDER BY last_name, first_name";
        List<Writer> writers = new ArrayList<>();
        try (Statement stmt = getStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            return mapResultSetToWriters(rs);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void deleteById(Long id) {
        String sql = "DELETE FROM writers WHERE id = ?";
        try (PreparedStatement stmt = getPreparedStatement(sql)) {

            stmt.setLong(1, id);
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private List<Writer> mapResultSetToWriters(ResultSet rs) throws SQLException {
        Map<Long, Writer> writerMap = new LinkedHashMap<>();
        Map<Long, Map<Long, Post>> writerPostsMap = new HashMap<>();

        while (rs.next()) {
            Long writerId = rs.getLong("writer_id");

            Writer writer = writerMap.get(writerId);
            if (writer == null) {
                writer = new Writer(
                        writerId,
                        rs.getString("first_name"),
                        rs.getString("last_name")
                );
                writerMap.put(writerId, writer);
                writerPostsMap.put(writerId, new HashMap<>());
            }

            Long postId = rs.getObject("post_id", Long.class);
            if (postId != null) {
                Map<Long, Post> postsMap = writerPostsMap.get(writerId);
                Post post = postsMap.get(postId);
                if (post == null) {
                    post = new Post(
                            postId,
                            rs.getString("post_content"),
                            PostStatus.valueOf(rs.getString("post_status"))
                    );
                    post.setCreated(rs.getObject("post_created", LocalDateTime.class));
                    post.setUpdated(rs.getObject("post_updated", LocalDateTime.class));
                    postsMap.put(postId, post);
                    writer.getPosts().add(post);
                }

                Long labelId = rs.getObject("label_id", Long.class);
                if (labelId != null) {
                    Label label = new Label(labelId, rs.getString("label_name"));
                    post.getLabels().add(label);
                }
            }
        }

        return new ArrayList<>(writerMap.values());
    }

    private String getWriterWithRelationsSql() {
        return "SELECT w.id AS writer_id, w.first_name, w.last_name, " +
                "p.id AS post_id, p.content AS post_content, p.status AS post_status, " +
                "p.created AS post_created, p.updated AS post_updated, " +
                "l.id AS label_id, l.name AS label_name " +
                "FROM writers w " +
                "LEFT JOIN posts p ON w.id = p.writer_id " +
                "LEFT JOIN post_labels pl ON p.id = pl.post_id " +
                "LEFT JOIN labels l ON pl.label_id = l.id";
    }

    private void saveWriter(Writer writer) throws SQLException {
        String sql = writer.getId() == null ?
                "INSERT INTO writers (first_name, last_name) VALUES (?, ?)" :
                "UPDATE writers SET first_name = ?, last_name = ? WHERE id = ?";

        try (PreparedStatement stmt = getPreparedStatement(sql)) {
            stmt.setString(1, writer.getFirstName());
            stmt.setString(2, writer.getLastName());
            if (writer.getId() != null) {
                stmt.setLong(3, writer.getId());
            }
            stmt.executeUpdate();

            if (writer.getId() == null) {
                try (ResultSet rs = stmt.getGeneratedKeys()) {
                    if (rs.next()) {
                        writer.setId(rs.getLong(1));
                    }
                }
            }
        }
    }

    private void savePostWithLabels(Post post, Long writerId) throws SQLException {
        String sql = post.getId() == null ?
                "INSERT INTO posts (content, created, updated, status, writer_id) VALUES (?, ?, ?, ?::post_status, ?)" :
                "UPDATE posts SET content = ?, updated = ?, status = ?::post_status WHERE id = ?";

        try (PreparedStatement stmt = getPreparedStatement(sql)) {
            stmt.setString(1, post.getContent());
            if (post.getId() == null) {
                stmt.setObject(2, LocalDateTime.now());
                stmt.setObject(3, LocalDateTime.now());
                stmt.setString(4, post.getStatus().name());
                stmt.setLong(5, writerId);
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
        }

        String deleteLinksSql = "DELETE FROM post_labels WHERE post_id = ?";
        try (PreparedStatement delStmt = getPreparedStatement(deleteLinksSql)) {
            delStmt.setLong(1, post.getId());
            delStmt.executeUpdate();
        }

        for (Label label : post.getLabels()) {
            saveLabelAndGetId(label);

            String insertLinkSql = "INSERT INTO post_labels (post_id, label_id) VALUES (?, ?)";
            try (PreparedStatement linkStmt = getPreparedStatement(insertLinkSql)) {
                linkStmt.setLong(1, post.getId());
                linkStmt.setLong(2, label.getId());
                linkStmt.executeUpdate();
            }
        }
    }

    private void saveLabelAndGetId(Label label) throws SQLException {
        if (label.getId() == null) {
            String findSql = "SELECT id FROM labels WHERE name = ?";
            try (PreparedStatement findStmt = getPreparedStatement(findSql)) {
                findStmt.setString(1, label.getName());
                try (ResultSet rs = findStmt.executeQuery()) {
                    if (rs.next()) {
                        label.setId(rs.getLong(1));
                        return;
                    }
                }
            }
            String insertSql = "INSERT INTO labels (name) VALUES (?)";
            try (PreparedStatement insStmt = getPreparedStatement(insertSql)) {
                insStmt.setString(1, label.getName());
                insStmt.executeUpdate();
                try (ResultSet rs = insStmt.getGeneratedKeys()) {
                    if (rs.next()) {
                        label.setId(rs.getLong(1));
                    }
                }
            }
        } else {
            String updateSql = "UPDATE labels SET name = ? WHERE id = ?";
            try (PreparedStatement updStmt = getPreparedStatement(updateSql)) {
                updStmt.setString(1, label.getName());
                updStmt.setLong(2, label.getId());
                updStmt.executeUpdate();
            }
        }
    }
}