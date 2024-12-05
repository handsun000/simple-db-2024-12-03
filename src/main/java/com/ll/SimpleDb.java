package com.ll;

import com.ll.util.Sql;
import lombok.RequiredArgsConstructor;

import java.sql.*;
import java.util.List;

@RequiredArgsConstructor
public class SimpleDb {
    private final String host;
    private final String id;
    private final String pw;
    private final String db;

    private Connection connection;

    private String createDatabaseUrl() {
        return String.format("jdbc:mysql://%s:3306/%s", host, db);
    }

    private void connect() {
        if (connection == null) {
            try {
                connection = DriverManager.getConnection(createDatabaseUrl(), id, pw);
            } catch (SQLException e) {
                throw new RuntimeException("Failed to connect to database", e);
            }
        }
    }

    public void run(String query, Object... params) {
        connect();
        try (PreparedStatement ps = connection.prepareStatement(query)) {
            setPreparedStatementParameters(ps, params);
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Failed to connect to database" + query, e);
        }
    }

    private static void setPreparedStatementParameters(PreparedStatement ps, Object[] params) throws SQLException {
        for (int i = 0; i < params.length; i++) {
            ps.setObject(i + 1, params[i]);
        }
    }

    public void close() {
        if (connection != null) {
            try {
                connection.close();
            } catch (SQLException e) {
                throw new RuntimeException("Failed to close database connection", e);
            } finally {
                connection = null;
            }
        }
    }

    public Sql genSql() {

        return new Sql(this);
    }

    public long insert(String query, List<Object> params) {
        connect();
        try (PreparedStatement ps = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {
            setPreparedStatementParameters(ps, params.toArray());
            ps.executeUpdate();

            ResultSet rs = ps.getGeneratedKeys();

            if (rs.next()) {
                return rs.getLong(1);
            } else {
                throw new RuntimeException("No generated key returned.");
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
