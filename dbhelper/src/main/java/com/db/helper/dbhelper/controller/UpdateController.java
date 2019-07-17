package com.db.helper.dbhelper.controller;

import com.alibaba.fastjson.JSONObject;
import com.db.helper.dbhelper.domain.InfoRow;
import com.db.helper.dbhelper.domain.Response;
import com.db.helper.dbhelper.domain.Table;
import com.db.helper.dbhelper.util.JDBCUtil;
import com.db.helper.dbhelper.util.Tools;
import org.springframework.web.bind.annotation.*;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.*;

@RestController
@RequestMapping("/update")
public class UpdateController {
    // 更新记录
    @PostMapping("/record")
    public Response updateRecord(@RequestHeader LinkedHashMap header,
                                 @RequestBody JSONObject jsonObject) throws Exception {
        String dbname = jsonObject.getString("dbname");
        String tablename = jsonObject.getString("tablename");
        JSONObject oldRecord = jsonObject.getJSONObject("oldRecord");
        JSONObject newRecord = jsonObject.getJSONObject("newRecord");

        // 查询对应表格的数据结构，以确定哪些字段是字符串
        Connection connection = JDBCUtil.getConnection(header.get("cookie").toString(), "/" + dbname);
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
        List<String> textFields = Tools.getCharField(table.getInfos());


        String updateSql = "update " + tablename + " set ";
        Set<String> keys = oldRecord.keySet();
        Map<String, String> conditions = new HashMap<>();
        for (String key: keys) {
            updateSql += key + "=";
            if (newRecord.get(key) != null) {
                if (!newRecord.getString(key).equals("")) {
                    if (textFields.contains(key)) {
                        updateSql += "'" + newRecord.getString(key) + "',";
                        if (oldRecord.get(key) != null) {
                            conditions.put(key, "'" + oldRecord.getString(key) + "'");
                        }
                    } else {
                        updateSql += newRecord.getString(key) + ",";
                        if (oldRecord.get(key) != null) {
                            conditions.put(key, oldRecord.getString(key));
                        }
                    }
                } else {
                    updateSql += "null,";
                    if (oldRecord.get(key) != null) {
                        conditions.put(key, oldRecord.getString(key));
                    }
                }
            } else {
                updateSql += "null,";
                if (textFields.contains(key)) {
                    if (oldRecord.get(key) != null) {
                        conditions.put(key, "'" + oldRecord.getString(key) + "'");
                    }
                } else {
                    if (oldRecord.get(key) != null) {
                        conditions.put(key, oldRecord.getString(key));
                    }
                }
            }
        }
        updateSql = updateSql.substring(0, updateSql.length() - 1);
        updateSql += " where ";
        for (String conditionKey: conditions.keySet()) {
            if (conditions.get(conditionKey) != null) {
                updateSql += conditionKey + "=" +conditions.get(conditionKey) + " and ";
            }
        }
        updateSql = updateSql.substring(0, updateSql.length() - 5);
        PreparedStatement updateStatement = connection.prepareStatement(updateSql);
        Response response = new Response();
        try {
            updateStatement.executeUpdate();
            response.setStatusCode(20000);
            response.setStatusMsg("成功");
        } catch (Exception e) {
            response.setStatusCode(20007);
            response.setStatusMsg(e.getMessage());
        }
        System.out.println(updateSql);
        return response;
    }
}
