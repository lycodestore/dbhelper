package com.db.helper.dbhelper.util;

import java.sql.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class JDBCUtil {
    public static Connection getConnection(String cookie, String database) throws Exception{
        String hostRegx = ".*host=(.*?);";
        String usernameRegx = ".*username=(.*?);";
        String passwordRegx = ".*password=(.*?);";

        Pattern hostPattern = Pattern.compile(hostRegx);
        Pattern usernamePattern = Pattern.compile(usernameRegx);
        Pattern passwordPattern = Pattern.compile(passwordRegx);

        Matcher hostMatcher = hostPattern.matcher(cookie);
        Matcher usernameMatcher = usernamePattern.matcher(cookie);
        Matcher passwordMatcher = passwordPattern.matcher(cookie);

        hostMatcher.find();
        usernameMatcher.find();
        passwordMatcher.find();

        String host = hostMatcher.group(1);
        String username = usernameMatcher.group(1);
        String password = passwordMatcher.group(1);

        String url = "jdbc:mysql://" + host + database;
        String driverClass = "com.mysql.jdbc.Driver";

        Class.forName(driverClass);
        Connection connection = DriverManager.getConnection(url, username, password);

        return connection;
    }

    public static void release(ResultSet resultSet,
                               Statement statement,
                               Connection connection) {
        if (resultSet != null) {
            try {
                resultSet.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        if (statement != null) {
            try {
                statement.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        if (connection != null) {
            try {
                connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
}
