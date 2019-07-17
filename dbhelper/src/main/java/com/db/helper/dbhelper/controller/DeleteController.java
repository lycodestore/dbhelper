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
@RequestMapping("/delete")
public class DeleteController {
    // 删除数据库
    @GetMapping("/database/{dbname}")
    public Response deleteDatabase(@RequestHeader LinkedHashMap header,
                                   @PathVariable("dbname") String dbname) throws Exception {
        Response response = new Response();

        List<String> forbidList = new ArrayList<String>();
        forbidList.add("information_schema");
        forbidList.add("performance_schema");
        forbidList.add("mysql");

        if (forbidList.contains(dbname)) {
            response.setStatusCode(20002);
            response.setStatusMsg("删除系统数据库会导致数据库崩溃！");
            return response;
        }

        Connection connection = JDBCUtil.getConnection(header.get("cookie").toString(),"");
        String sql = "drop database " + dbname;
        PreparedStatement preparedStatement = connection.prepareStatement(sql);

        try {
            preparedStatement.executeUpdate();
            response.setStatusCode(20000);
            response.setStatusMsg("成功");
        } catch (Exception e) {
            e.printStackTrace();
            response.setStatusCode(20001);
            response.setStatusMsg("数据库操作异常！");
        }

        JDBCUtil.release(null, preparedStatement, connection);
        return response;
    }

    // 删除数据表
    @GetMapping("/table/{dbname}/{tablename}")
    public Response deleteTable(@RequestHeader LinkedHashMap header,
                                @PathVariable("dbname") String dbname,
                                @PathVariable("tablename") String tablename) throws Exception {
        Connection connection = JDBCUtil.getConnection(header.get("cookie").toString(),"/" + dbname);
        String sql = "drop table " + tablename;
        PreparedStatement preparedStatement = connection.prepareStatement(sql);
        Response response = new Response();
        try {
            preparedStatement.executeUpdate();
            response.setStatusCode(20000);
            response.setStatusMsg("成功");
        } catch (Exception e) {
            response.setStatusCode(20004);
            response.setStatusMsg(e.getMessage());
        }

        return response;
    }

    // 删除记录
    @PostMapping("/record")
    public Response deleteRecord(@RequestHeader LinkedHashMap header,
                                 @RequestBody JSONObject jsonObject) throws Exception {
        String dbname = jsonObject.getString("dbname");
        String tablename = jsonObject.getString("tablename");
        JSONObject record = jsonObject.getJSONObject("record");

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
        List<String> textFields = Tools.getCharField(table.getInfos());

        // 执行删除
        String deleteSql = "delete from " + tablename + " where ";
        Set<String> keys = record.keySet();
        for (String key: keys) {
            if (record.get(key) != null) {
                if (textFields.contains(key)) {
                    deleteSql += key + "=" + "'" + record.getString(key) + "' and ";
                } else {
                    deleteSql += key + "=" + record.getString(key) + " and ";
                }
            }
        }

        deleteSql = deleteSql.substring(0, deleteSql.length() - 5);
        PreparedStatement deleteSatatement = connection.prepareStatement(deleteSql);
        Response response = new Response();
        try {
            deleteSatatement.executeUpdate();
            response.setStatusCode(20000);
            response.setStatusMsg("成功");
        } catch (Exception e) {
            response.setStatusCode(20006);
            response.setStatusMsg(e.getMessage());
        }
        System.out.println(deleteSql);
        return response;
    }
}
