package com.db.helper.dbhelper.domain;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Table {
    private String name;

    private List<Map<String, String>> infos;

    public List<Map<String, String>> getInfos() {
        return infos;
    }

    public void setInfos(List<Map<String, String>> infos) {
        this.infos = infos;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
