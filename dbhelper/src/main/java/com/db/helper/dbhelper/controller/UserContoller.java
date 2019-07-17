package com.db.helper.dbhelper.controller;

import com.alibaba.fastjson.JSONObject;
import com.db.helper.dbhelper.domain.Response;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.sql.Connection;
import java.sql.DriverManager;

@RestController
@RequestMapping("/user")
public class UserContoller {
    // 登录
    @PostMapping("/login")
    public Response login(@RequestBody JSONObject jsonObject) throws Exception {
        String host = jsonObject.getString("host");
        String url = "jdbc:mysql://" + host;
        String user = jsonObject.getString("username");
        String password = jsonObject.getString("password");
        String driverClass = "com.mysql.jdbc.Driver";

        Response response = new Response();
        try {
            Class.forName(driverClass);
            Connection connection = DriverManager.getConnection(url, user, password);
            response.setStatusCode(20000);
            response.setStatusMsg("成功");
        } catch (Exception e) {
            response.setStatusCode(20008);
            response.setStatusMsg("登录失败");
        }

        return response;
    }
}
