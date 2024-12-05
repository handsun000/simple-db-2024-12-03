package com.ll.util;

import com.ll.SimpleDb;
import lombok.RequiredArgsConstructor;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.*;

public class Sql {
    private final SimpleDb simpleDb;
    private final StringBuilder query;
    private final List<Object> params;

    public Sql(SimpleDb simpleDb) {
        this.simpleDb = simpleDb;
        this.query = new StringBuilder();
        this.params = new ArrayList<>();
    }

    public Sql append(String query, Object... params) {
        this.query.append(" ").append(query);
        if (params != null) {
            this.params.addAll(Arrays.asList(params));
        }
        return this;
    }

    public long insert() {
        return (long) commonSql("INSERT");
    }

    public int update() {
        return (int) commonSql("UPDATE");
    }

    public int delete() {
        return (int) commonSql("DELETE");
    }
    
    public List<Map<String, Object>> selectRows() {
        List<Map<String, Object>> list = new ArrayList<>();
        
        ResultSet rs = (ResultSet) commonSql("SELECT");
        try {
            ResultSetMetaData metaData = rs.getMetaData();
            while(rs.next()) {
                Map<String, Object> map = new HashMap<>();
                mappingData(metaData, map, rs);
                list.add(map);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return list;
    }

    public Map<String, Object> selectRow() {
        Map<String, Object> map = new HashMap<>();
        ResultSet rs = (ResultSet) commonSql("SELECT");
        try {
            ResultSetMetaData metaData = rs.getMetaData();
            mappingData(metaData, map, rs);
        }catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return map;
    }

    public LocalDateTime selectDatetime() {
        return (LocalDateTime) commonSql("SELECT");
    }

    private static void mappingData(ResultSetMetaData metaData, Map<String, Object> map, ResultSet rs) throws SQLException {
        for (int i = 1; i<= metaData.getColumnCount(); i++) {
            map.put(metaData.getColumnName(i), rs.getObject(i));
        }
    }

    private Object commonSql(String command) {
        return simpleDb.dbCommand(command, query.toString(), params.toArray());
    }
}
