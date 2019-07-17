package com.db.helper.dbhelper.util;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Tools {
    public static List<String> getCharField(List<Map<String, String>> fileds) {
        List<String> charFields = new ArrayList<>();
        List<String> textTypeList = Arrays.asList(
                "BLOB", "BLOB DATA TYPE", "CHAR", "CHAR BYTE", "LONGTEXT",
                "MEDIUMBLOB", "MEDIUMTEXT", "TEXT", "TINYBLOB", "TINYTEXT", "VARCHAR"
        );

        String regx = "\\(.*\\)";
        Pattern p = Pattern.compile(regx);
        for (Map<String, String> field: fileds) {
            String name = field.get("Field");
            String type = field.get("Type");
            Matcher m = p.matcher(type);
            type = m.replaceAll("");

            if (textTypeList.contains(type.toUpperCase())) {
                charFields.add(name);
            }
        }
        return charFields;
    }
}
