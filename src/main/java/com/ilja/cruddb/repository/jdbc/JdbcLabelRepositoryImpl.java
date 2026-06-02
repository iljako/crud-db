package com.ilja.cruddb.repository.jdbc;

import com.ilja.cruddb.model.Label;
import com.ilja.cruddb.repository.LabelRepository;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static com.ilja.cruddb.utils.JdbcUtils.getPreparedStatement;
import static com.ilja.cruddb.utils.JdbcUtils.getStatement;

public class JdbcLabelRepositoryImpl implements LabelRepository {

    public JdbcLabelRepositoryImpl() {
    }

    @Override
    public Label save(Label label) {
        String sql = label.getId() == null ?
                "INSERT INTO labels (name) VALUES (?)" :
                "UPDATE labels SET name = ? WHERE id = ?";

        try (PreparedStatement stmt = getPreparedStatement(sql)) {
            stmt.setString(1, label.getName());
            if (label.getId() != null) {
                stmt.setLong(2, label.getId());
            }
            stmt.executeUpdate();
            if (label.getId() == null) {
                try (ResultSet rs = stmt.getGeneratedKeys()) {
                    if (rs.next()) {
                        label.setId(rs.getLong(1));
                    }
                }
            }
            return label;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Optional<Label> findById(Long id) {
        String sql = "SELECT * FROM labels WHERE id = ?";
        try (PreparedStatement stmt = getPreparedStatement(sql)) {
            stmt.setLong(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return Optional.of(mapRowToLabel(rs));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return Optional.empty();
    }

    @Override
    public List<Label> findAll() {
        String sql = "SELECT * FROM labels";
        List<Label> labels = new ArrayList<>();
        try (Statement stmt = getStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                labels.add(mapRowToLabel(rs));
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return labels;
    }

    @Override
    public void deleteById(Long id) {
        String sql = "DELETE FROM labels WHERE id = ?";
        try (PreparedStatement stmt = getPreparedStatement(sql)) {
            stmt.setLong(1, id);
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Optional<Label> findByName(String name) {
        String sql = "SELECT * FROM labels WHERE name = ?";
        try (PreparedStatement stmt = getPreparedStatement(sql)) {
            stmt.setString(1, name);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return Optional.of(mapRowToLabel(rs));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return Optional.empty();
    }

    private Label mapRowToLabel(ResultSet rs) throws SQLException {
        return new Label(
                rs.getLong("id"),
                rs.getString("name")
        );
    }
}