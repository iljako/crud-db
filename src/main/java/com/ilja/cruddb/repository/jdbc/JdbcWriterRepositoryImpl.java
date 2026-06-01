package com.ilja.cruddb.repository.jdbc;

import com.ilja.cruddb.model.Writer;
import com.ilja.cruddb.repository.WriterRepository;

import javax.sql.DataSource;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static com.ilja.cruddb.utils.JdbcUtils.getPreparedStatement;

public class JdbcWriterRepositoryImpl implements WriterRepository {

    private final DataSource dataSource;

    public JdbcWriterRepositoryImpl(DataSource dataSource) {
        this.dataSource = dataSource;
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
                if (rs.next()) {
                    return Optional.of(new Writer(
                            rs.getLong("id"),
                            rs.getString("first_name"),
                            rs.getString("last_name")
                    ));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return Optional.empty();
    }

    @Override
    public List<Writer> findAll() {
        String sql = "SELECT * FROM writers ORDER BY last_name, first_name";
        List<Writer> writers = new ArrayList<>();
        try (Connection conn = dataSource.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                writers.add(new Writer(
                        rs.getLong("id"),
                        rs.getString("first_name"),
                        rs.getString("last_name")
                ));
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return writers;
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
}