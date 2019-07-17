package com.db.helper.dbhelper.domain;

import java.util.HashMap;
import java.util.Map;

public class InfoRow {
    private Map<String, String> rows = new HashMap<>();

    public void add(String title, String content) {
        rows.put(title, content);
    }

    public Map<String, String> getRows() {
        return rows;
    }

    public void setRows(Map<String, String> rows) {
        this.rows = rows;
    }
}
