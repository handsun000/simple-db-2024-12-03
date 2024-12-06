package com.ll.model;

import lombok.Data;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.Map;

@Getter
public class Article {
    private Long id;
    private String title;
    private String body;
    private LocalDateTime createdDate;
    private LocalDateTime modifiedDate;
    private boolean isBlind;

    public Article(Map<String, Object> row) {
        this.id = (Long) row.get("id");
        this.title = (String) row.get("title");
        this.body = (String) row.get("body");
        this.createdDate = (LocalDateTime) row.get("createdDate");
        this.modifiedDate = (LocalDateTime) row.get("modifiedDate");
        this.isBlind = (boolean) row.get("isBlind");
    }
}
