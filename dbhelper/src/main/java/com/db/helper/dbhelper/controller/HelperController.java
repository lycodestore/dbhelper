package com.db.helper.dbhelper.controller;

import com.db.helper.dbhelper.util.JDBCUtil;
import org.springframework.web.bind.annotation.*;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

@RestController
@RequestMapping("/helper")
public class HelperController {
    // 查询数据库支持的字符集
    @GetMapping("/characterset")
    public List<String> getCharactersets(@RequestHeader LinkedHashMap header) throws Exception {
        Connection connection = JDBCUtil.getConnection(header.get("cookie").toString(),"");
        String sql = "show character set";
        PreparedStatement preparedStatement = connection.prepareStatement(sql);
        ResultSet resultSet = preparedStatement.executeQuery();
        List<String> charactersets = new ArrayList<>();

        while (resultSet.next()) {
            String characterset = resultSet.getString(1);
            charactersets.add(characterset);
        }

        JDBCUtil.release(resultSet, preparedStatement, connection);

        return charactersets;
    }

    // 查询字符集对应的校验集
    @GetMapping("/collation/{characterset}")
    public List<String> getCollations(@RequestHeader LinkedHashMap header,
                                      @PathVariable("characterset") String characterset) throws Exception {
        Connection connection = JDBCUtil.getConnection(header.get("cookie").toString(),"");
        String sql = "show collation like '" + characterset + "\\_%'";
        PreparedStatement preparedStatement = connection.prepareStatement(sql);
        ResultSet resultSet = preparedStatement.executeQuery();

        List<String> collations = new ArrayList<>();
        while (resultSet.next()) {
            String collation = resultSet.getString(1);
            collations.add(collation);
        }

        JDBCUtil.release(resultSet, preparedStatement, connection);

        return collations;
    }
}
