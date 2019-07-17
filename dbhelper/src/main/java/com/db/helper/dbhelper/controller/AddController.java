package com.db.helper.dbhelper.controller;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.db.helper.dbhelper.domain.InfoRow;
import com.db.helper.dbhelper.domain.Response;
import com.db.helper.dbhelper.domain.Table;
import com.db.helper.dbhelper.util.JDBCUtil;
import com.db.helper.dbhelper.util.Tools;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.*;

@RestController
@RequestMapping("/new")
public class AddController {
    // 新建数据库
    @PostMapping("/database")
    public Response createDatabase(@RequestHeader LinkedHashMap header,
                                   @RequestBody JSONObject jsonObject) throws Exception{
        String dbname = jsonObject.getString("dbname");
        String characterset = jsonObject.getString("characterset");
        String collation = jsonObject.getString("collation");

        Connection connection = JDBCUtil.getConnection(header.get("cookie").toString(),"");
        String sql = "create database " + dbname + " default character set " + characterset + " collate " + collation;
        PreparedStatement preparedStatement = connection.prepareStatement(sql);
        Response response = new Response();
        try {
            preparedStatement.executeUpdate();
            response.setStatusCode(20000);
            response.setStatusMsg("成功");
        } catch (Exception e) {
            e.printStackTrace();
            response.setStatusCode(20001);
            response.setStatusMsg("数据库操作异常");
        }

        JDBCUtil.release(null, preparedStatement, connection);
        return response;
    }


    // 新建数据表
    @PostMapping("/table")
    public Response createTable(@RequestHeader LinkedHashMap header,
                                @RequestBody JSONObject jsonObject) throws Exception {
        String dbname = jsonObject.getString("dbname");
        String tablename = jsonObject.getString("tablename");
        JSONArray columns = jsonObject.getJSONArray("tableData");
        String sql = "create table " + tablename + "(";
        int cloumnCount = columns.size();
        List<String> keys = new ArrayList<>();

        for (int i = 0; i < cloumnCount; i++) {
            JSONObject column = columns.getJSONObject(i);

            String columnName = column.getString("column");
            String type = column.getString("type");
            String comment = column.getString("comment");
            Boolean isKey = column.getBoolean("isKey");

            List<String> numberTypeList = Arrays.asList(
                    "BIGINT", "DEC", "DECIMAL", "DOUBLE", "DOUBLE PRECISION",
                    "FLOAT", "INT", "INTEGER", "TINYINT"
            );
            List<String> textTypeList = Arrays.asList(
                    "BLOB", "BLOB DATA TYPE", "CHAR", "CHAR BYTE", "LONGTEXT",
                    "MEDIUMBLOB", "MEDIUMTEXT", "TEXT", "TINYBLOB", "TINYTEXT", "VARCHAR"
            );

            sql += columnName + " " + type;

            if (numberTypeList.contains(type)) {
                BigDecimal defaultValue = null;
                try {
                    defaultValue = column.getBigDecimal("default");
                } catch (Exception e) {
                    e.printStackTrace();
                }

                if (defaultValue != null) {
                    sql += " default " + defaultValue;
                }
            }

            Integer length = null;
            try {
                length = column.getInteger("length");
                if (length != null) {
                    sql += "(" + length + ")";
                }
            } catch (Exception e) {
            }

            String defaultValue = "";
            try {
                defaultValue = column.getString("default");
            } catch (Exception e) {
                e.printStackTrace();
            }

            if (!defaultValue.equals("")) {
                sql += " default " + defaultValue;
            }
            if (!comment.equals("")) {
                sql += " comment " + "'" + comment + "'";
            }
            if (i != cloumnCount - 1) {
                sql += ",";
            }
            if (isKey) {
                keys.add(columnName);
            }
        }

        if (keys.size() != 0) {
            sql += ", primary key(";
            for (int i = 0; i < keys.size(); i++) {
                sql += keys.get(i);
                if (i != keys.size() - 1) {
                    sql += ", ";
                }
            }
            sql += ")";
        }
        sql += ")";
        System.out.println(sql);

        Connection connection = JDBCUtil.getConnection(header.get("cookie").toString(),"/" + dbname);
        PreparedStatement preparedStatement = connection.prepareStatement(sql);
        Response response = new Response();
        try {
            preparedStatement.executeUpdate();
            response.setStatusCode(20000);
            response.setStatusMsg("成功");
        } catch (Exception e) {
            e.printStackTrace();
            response.setStatusCode(20003);
            response.setStatusMsg(e.getMessage());
        }

        JDBCUtil.release(null, preparedStatement, connection);
        return response;
    }

    // 新增记录
    @PostMapping("/record")
    public Response insertRecord(@RequestHeader LinkedHashMap header,
                                 @RequestBody JSONObject jsonObject) throws Exception {
        // 从请求体中获取参数
        String dbname = jsonObject.getString("dbname");
        String tablename = jsonObject.getString("tablename");
        JSONArray records = jsonObject.getJSONArray("records");

        // 查询对应表格的数据结构，以确定哪些字段是字符串
        Connection connection = JDBCUtil.getConnection(header.get("cookie").toString(),"/" + dbname);
        String sql = "show full columns from " + tablename;
        PreparedStatement preparedStatement = connection.prepareStatement(sql);
        ResultSet resultSet = preparedStatement.executeQuery();
        List<Map<String, String>> infos = new ArrayList<>();
        while (resultSet.next()) {
            int columnCount = resultSet.getMetaData().getColumnCount();
            InfoRow infoRow = new InfoRow();
            for (int i = 1; i <= columnCount; i++) {
                infoRow.add(resultSet.getMetaData().getColumnLabel(i), resultSet.getString(i));
            }
            infos.add(infoRow.getRows());
        }
        JDBCUtil.release(resultSet, preparedStatement, null);
        Table table = new Table();
        table.setInfos(infos);
        List<String> textField = Tools.getCharField(table.getInfos());


        Response response = new Response();
        // 执行插入
        int rowCount = records.size();
        for (int i = 0; i < rowCount; i++) {
            String insertSql = "insert into " + tablename + "(";

            JSONObject row = records.getJSONObject(i);
            List<String> values = new ArrayList<>();
            List<String> keyList = new ArrayList<>();
            Set<String> keys = row.keySet();
            for (String key: keys) {
                if (row.get(key) != null) {
                    String value = row.getString(key);
                    values.add(value);
                    keyList.add(key);
                    insertSql += key + ",";
                }
            }
            insertSql = insertSql.substring(0, insertSql.length() - 1);
            insertSql += ") values (";
            for (int k = 0; k < values.size(); k++) {
                if (textField.contains(keyList.get(k))) {
                    insertSql += "'" + values.get(k) + "'";
                } else {
                    insertSql += values.get(k);
                }
                if (k != values.size() - 1) {
                    insertSql += ",";
                } else {
                    insertSql += ")";
                }
            }
            System.out.println(insertSql);
            PreparedStatement insertStatement = connection.prepareStatement(insertSql);
            try {
                insertStatement.executeUpdate();
                response.setStatusCode(20000);
                response.setStatusMsg("成功");
            } catch (Exception e) {
                response.setStatusCode(2005);
                response.setStatusMsg(e.getMessage());
                return response;
            }
            JDBCUtil.release(null, insertStatement, null);
        }

        JDBCUtil.release(null, null, connection);

        return response;
    }
}
