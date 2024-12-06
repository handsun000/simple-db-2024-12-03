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

    @SuppressWarnings("unchecked")
    public List<Map<String, Object>> selectRows() {
        return (List<Map<String, Object>>)commonSql("SELECT");
    }

    @SuppressWarnings("unchecked")
    public Map<String, Object> selectRow() {
        List<Map<String, Object>> list = (List<Map<String, Object>>)commonSql("SELECT");
        return list.get(0);
    }

    public LocalDateTime selectDatetime() {
        Map<String, Object> map = selectRow();

        return (LocalDateTime) map.get("NOW()");
    }

    private Object commonSql(String command) {
        return simpleDb.dbCommand(command, query.toString(), params.toArray());
    }

    public Long selectLong() {
        Map<String, Object> map = selectRow();
        return (Long) map.get("id");
    }

    public String selectString() {
        Map<String, Object> map = selectRow();
        return (String) map.get("title");
    }

    public Boolean selectBoolean() {
        Map<String, Object> map = selectRow();

        String key = map.keySet().iterator().next();

        Object value = map.get(key);

        if (value instanceof Boolean) return (Boolean) value;
        else if (value instanceof Number) return ((Number) value).intValue() == 1;

        return (Boolean) map.get(key);
    }
}
