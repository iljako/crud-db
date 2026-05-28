package com.ilja.cruddb.repository;

import com.ilja.cruddb.model.Post;
import com.ilja.cruddb.model.PostStatus;

import javax.sql.DataSource;
import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class JdbcPostRepositoryImpl implements PostRepository {

    private final DataSource dataSource;

    public JdbcPostRepositoryImpl(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Override
    public Post save(Post post) {
        String sql = post.getId() == null ?
                "INSERT INTO posts (content, created, updated, status, writer_id) VALUES (?, ?, ?, ?::post_status, ?)" :
                "UPDATE posts SET content = ?, updated = ?, status = ?::post_status WHERE id = ?";

        try (Connection conn = dataSource.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

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
            return post;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Optional<Post> findById(Long id) {
        String sql = "SELECT * FROM posts WHERE id = ?";
        try (Connection conn = dataSource.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setLong(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    Post post = new Post(
                            rs.getLong("id"),
                            rs.getString("content"),
                            PostStatus.valueOf(rs.getString("status"))
                    );
                    post.setCreated(rs.getObject("created", LocalDateTime.class));
                    post.setUpdated(rs.getObject("updated", LocalDateTime.class));
                    return Optional.of(post);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return Optional.empty();
    }

    @Override
    public List<Post> findAll() {
        String sql = "SELECT * FROM posts ORDER BY created DESC";
        List<Post> posts = new ArrayList<>();
        try (Connection conn = dataSource.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                Post post = new Post(
                        rs.getLong("id"),
                        rs.getString("content"),
                        PostStatus.valueOf(rs.getString("status"))
                );
                post.setCreated(rs.getObject("created", LocalDateTime.class));
                post.setUpdated(rs.getObject("updated", LocalDateTime.class));
                posts.add(post);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return posts;
    }

    @Override
    public void deleteById(Long id) {
        String sql = "DELETE FROM posts WHERE id = ?";
        try (Connection conn = dataSource.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setLong(1, id);
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<Post> findByWriterId(Long writerId) {
        String sql = "SELECT * FROM posts WHERE writer_id = ? ORDER BY created DESC";
        List<Post> posts = new ArrayList<>();
        try (Connection conn = dataSource.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setLong(1, writerId);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    Post post = new Post(
                            rs.getLong("id"),
                            rs.getString("content"),
                            PostStatus.valueOf(rs.getString("status"))
                    );
                    post.setCreated(rs.getObject("created", LocalDateTime.class));
                    post.setUpdated(rs.getObject("updated", LocalDateTime.class));
                    posts.add(post);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return posts;
    }

    @Override
    public void addLabelToPost(Long postId, Long labelId) {
        String sql = "INSERT INTO post_labels (post_id, label_id) VALUES (?, ?)";
        try (Connection conn = dataSource.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setLong(1, postId);
            stmt.setLong(2, labelId);
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void removeLabelFromPost(Long postId, Long labelId) {
        String sql = "DELETE FROM post_labels WHERE post_id = ? AND label_id = ?";
        try (Connection conn = dataSource.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setLong(1, postId);
            stmt.setLong(2, labelId);
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}