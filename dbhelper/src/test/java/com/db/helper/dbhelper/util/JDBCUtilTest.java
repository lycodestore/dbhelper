package com.db.helper.dbhelper.util;

import org.junit.Test;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class JDBCUtilTest {
    @Test
    public void sqltest() throws Exception {
        String cookie = "language=zh; host=127.0.0.1:3306; username=root; password=123456; role=admin; sidebarStatus=0; Admin-Token=admin-token";

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

        System.out.println(hostMatcher.group(1));
        System.out.println(usernameMatcher.group(1));
        System.out.println(passwordMatcher.group(1));
    }
}
