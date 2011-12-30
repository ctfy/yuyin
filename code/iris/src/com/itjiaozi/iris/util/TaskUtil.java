package com.itjiaozi.iris.util;

import jregex.Pattern;

public class TaskUtil {
    public static String searchContactNameForCall(String str) {
        if (null == str) {
            str = "";
        }
        jregex.Pattern pattern = new Pattern("打电话给({name}.*)");
        jregex.Matcher matcher = pattern.matcher(str);
        if (matcher.find()) {
            String name = matcher.group("name");
            return name;
        }
        return str;
    }

    public static String searchContactNameForMessage(String str) {
        if (null == str) {
            str = "";
        }
        jregex.Pattern pattern = new Pattern("发短信给({name}.*)");
        jregex.Matcher matcher = pattern.matcher(str);
        if (matcher.find()) {
            String name = matcher.group("name");
            return name;
        }
        return str;
    }

    public static String searchAppNameForOpenApp(String str) {
        if (null == str) {
            str = "";
        }
        jregex.Pattern pattern = new Pattern("打开({name}.*)");
        jregex.Matcher matcher = pattern.matcher(str);
        if (matcher.find()) {
            String name = matcher.group("name");
            return name;
        }
        return str;
    }
}
