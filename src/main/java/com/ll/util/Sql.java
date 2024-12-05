package com.ll.util;

import com.ll.SimpleDb;
import lombok.RequiredArgsConstructor;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

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
        return (long) simpleDb.dbCommand("INSERT", query.toString(), params.toArray());
    }

    public int update() {
        return (int) simpleDb.dbCommand("UPDATE", query.toString(), params.toArray());
    }

    public int delete() {
        return (int) simpleDb.dbCommand("DELETE", query.toString(), params.toArray());
    }
}
