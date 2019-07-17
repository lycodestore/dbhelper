package com.db.helper.dbhelper.domain;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

public class TableData {
    private BigDecimal total;

    private List<String> header;

    private List<Map<String, String>> data;

    public BigDecimal getTotal() {
        return total;
    }

    public void setTotal(BigDecimal total) {
        this.total = total;
    }

    public List<String> getHeader() {
        return header;
    }

    public void setHeader(List<String> header) {
        this.header = header;
    }

    public List<Map<String, String>> getData() {
        return data;
    }

    public void setData(List<Map<String, String>> data) {
        this.data = data;
    }
}
