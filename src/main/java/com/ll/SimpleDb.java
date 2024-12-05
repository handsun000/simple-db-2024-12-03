package com.ll;

import com.ll.util.Sql;
import lombok.RequiredArgsConstructor;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

    public Sql genSql() {

        return new Sql(this);
    }

    public void run(String query, Object... params) {
        dbCommand("", query, params);
    }

    public Object dbCommand(String command, String query, Object... params) {
        connect();
        try (PreparedStatement ps = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)){
            if (params != null) setPreparedStatementParameters(ps, params);

            switch (command) {
                case "SELECT" -> {
                    List<Map<String,Object>> list = new ArrayList<>();
                    ps.executeQuery();
                    ResultSet rs = ps.executeQuery();
                    ResultSetMetaData metaData = rs.getMetaData();
                    Map<String, Object> map = null;
                    while(rs.next()) {
                        map = new HashMap<>();
                        for (int i = 1; i<= metaData.getColumnCount(); i++) {
                            map.put(metaData.getColumnName(i), rs.getObject(i));
                        }
                        list.add(map);
                    }
                    if (list.size() == 1) return map;
                    else return list;
                }
                case "INSERT" -> {
                    ps.executeUpdate();
                    ResultSet rs = ps.getGeneratedKeys();

                    if (rs.next()) return rs.getLong(1);
                    else throw new RuntimeException("No generated key returned.");
                }
                case "UPDATE" -> {
                    return ps.executeUpdate();
                }
                case "DELETE" -> {
                    return ps.executeUpdate();
                }
                default -> {
                    return ps.executeUpdate();
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
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
}
