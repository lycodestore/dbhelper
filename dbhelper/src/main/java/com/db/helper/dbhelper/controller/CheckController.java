package com.db.helper.dbhelper.controller;

import com.db.helper.dbhelper.domain.Database;
import com.db.helper.dbhelper.domain.InfoRow;
import com.db.helper.dbhelper.domain.Table;
import com.db.helper.dbhelper.domain.TableData;
import com.db.helper.dbhelper.util.JDBCUtil;
import org.springframework.web.bind.annotation.*;
import com.alibaba.fastjson.JSONObject;

import java.math.BigDecimal;
import java.sql.*;
import java.util.*;

@RestController
@RequestMapping("/check")
public class CheckController {
    @GetMapping("/dblist")
    public List<Database> dblist(@RequestHeader LinkedHashMap header) throws Exception {
        List<Database> databases = new ArrayList<Database>();
        Connection connection = JDBCUtil.getConnection(header.get("cookie").toString(), "");
        String sql = "show databases";

        PreparedStatement preparedStatement = connection.prepareStatement(sql);
        ResultSet resultSet = preparedStatement.executeQuery();
        Database database = null;
        while (resultSet.next()) {
            String dbname = resultSet.getString(1);
            database = new Database();
            database.setName(dbname);
            databases.add(database);
        }
        JDBCUtil.release(resultSet, preparedStatement, connection);
        return databases;
    }

    @GetMapping("/tables/{dbname}")
    public List<Table> getTables(@RequestHeader LinkedHashMap header,
                                 @PathVariable("dbname") String dbname) throws Exception {
        List<Table> tables = new ArrayList<Table>();

        Connection connection = JDBCUtil.getConnection(header.get("cookie").toString(), "/" + dbname);
        String sql = "show tables";

        PreparedStatement preparedStatement = connection.prepareStatement(sql);
        ResultSet resultSet = preparedStatement.executeQuery();

        Table table = null;
        while (resultSet.next()) {
            String tableName = resultSet.getString(1);
            table = new Table();
            table.setName(tableName);
            tables.add(table);
        }
        JDBCUtil.release(resultSet, preparedStatement, connection);
        return tables;
    }

    // 查看表格结构
    @GetMapping("/{db}/{table}")
    public Table tableDetail(@RequestHeader LinkedHashMap header,
                             @PathVariable("db") String db,
                             @PathVariable("table") String tableName) throws Exception {
        Connection connection = JDBCUtil.getConnection(header.get("cookie").toString(),"/" + db);
        String sql = "show full columns from " + tableName;
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

        Table table = new Table();
        table.setName(tableName);
        table.setInfos(infos);
        JDBCUtil.release(resultSet, preparedStatement, connection);
        return table;
    }

    // 分页查询表里的数据
    @PostMapping("/data")
    public TableData getData(@RequestHeader LinkedHashMap head,@RequestBody JSONObject body) throws Exception {
        String dbname = body.getString("dbname");
        String tablename = body.getString("tablename");
        int offset = Integer.parseInt(body.getString("offset"));
        Connection connection = JDBCUtil.getConnection(head.get("cookie").toString(),"/" + dbname);


        String queryTotal = "select count(*) from " + tablename;
        PreparedStatement queryTotalStatement = connection.prepareStatement(queryTotal);
        ResultSet queryResult = queryTotalStatement.executeQuery();
        BigDecimal total = null;
        while (queryResult.next()) {
            total = new BigDecimal(queryResult.getInt(1));
        }

        JDBCUtil.release(queryResult, queryTotalStatement, null);


        String sql = "select * from " + tablename + " limit " + offset + ",10";
        PreparedStatement preparedStatement = connection.prepareStatement(sql);
        System.out.println(sql);
        ResultSet resultSet = preparedStatement.executeQuery();


        List<String> header = new ArrayList<>();
        List<Map<String, String>> data = new ArrayList<>();
        int columnCount = resultSet.getMetaData().getColumnCount();
        for (int i = 1; i <= columnCount; i++) {
            header.add(resultSet.getMetaData().getColumnLabel(i));
        }
        while (resultSet.next()) {
            Map<String, String> row = new HashMap<>();
            for (int i = 1; i <= columnCount; i++) {
                row.put(resultSet.getMetaData().getColumnLabel(i), resultSet.getString(i));
            }
            data.add(row);
        }

        JDBCUtil.release(resultSet, preparedStatement, connection);


        TableData tableData = new TableData();
        tableData.setTotal(total);
        tableData.setHeader(header);
        tableData.setData(data);
        return tableData;
    }

    // 一次行获取表中所有数据
    @PostMapping("/alldata")
    public TableData getAllData(@RequestHeader LinkedHashMap head,
                                @RequestBody JSONObject body) throws Exception {
        String dbname = body.getString("dbname");
        String tablename = body.getString("tablename");
        Connection connection = JDBCUtil.getConnection(head.get("cookie").toString(),"/" + dbname);


        String queryTotal = "select count(*) from " + tablename;
        PreparedStatement queryTotalStatement = connection.prepareStatement(queryTotal);
        ResultSet queryResult = queryTotalStatement.executeQuery();
        BigDecimal total = null;
        while (queryResult.next()) {
            total = new BigDecimal(queryResult.getInt(1));
        }

        JDBCUtil.release(queryResult, queryTotalStatement, null);


        String sql = "select * from " + tablename;
        PreparedStatement preparedStatement = connection.prepareStatement(sql);
        System.out.println(sql);
        ResultSet resultSet = preparedStatement.executeQuery();


        List<String> header = new ArrayList<>();
        List<Map<String, String>> data = new ArrayList<>();
        int columnCount = resultSet.getMetaData().getColumnCount();
        for (int i = 1; i <= columnCount; i++) {
            header.add(resultSet.getMetaData().getColumnLabel(i));
        }
        while (resultSet.next()) {
            Map<String, String> row = new HashMap<>();
            for (int i = 1; i <= columnCount; i++) {
                row.put(resultSet.getMetaData().getColumnLabel(i), resultSet.getString(i));
            }
            data.add(row);
        }

        JDBCUtil.release(resultSet, preparedStatement, connection);


        TableData tableData = new TableData();
        tableData.setTotal(total);
        tableData.setHeader(header);
        tableData.setData(data);
        return tableData;
    }

    // 查看建表语句
    @GetMapping("/showsql/{dbname}/{tablename}")
    public String showSql(@RequestHeader LinkedHashMap header,
                          @PathVariable("dbname") String dbname,
                          @PathVariable("tablename") String tablename) throws Exception {
        Connection connection = JDBCUtil.getConnection(header.get("cookie").toString(),"/" + dbname);
        String sql = "show create table " + tablename;
        System.out.println(sql);
        PreparedStatement preparedStatement = connection.prepareStatement(sql);
        ResultSet resultSet = preparedStatement.executeQuery();
        String createTableSql = "";
        while (resultSet.next()) {
            createTableSql = resultSet.getString(2);
        }
        JDBCUtil.release(resultSet, preparedStatement, connection);

        return createTableSql;
    }

    // 查看建库语句
    @GetMapping("/showcreatedb/{dbname}")
    public String showCreateDb(@RequestHeader LinkedHashMap header,
                               @PathVariable("dbname") String dbname) throws Exception {
        Connection connection = JDBCUtil.getConnection(header.get("cookie").toString(),"");
        String sql = "show create database " + dbname;
        System.out.println(sql);
        PreparedStatement preparedStatement = connection.prepareStatement(sql);
        ResultSet resultSet = preparedStatement.executeQuery();
        String createDbSql = "";
        while (resultSet.next()) {
            createDbSql = resultSet.getString(2);
        }
        JDBCUtil.release(resultSet, preparedStatement, connection);

        return createDbSql;
    }
}
