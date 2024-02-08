package com.zherikhov.listshop.utils;

public class TextFormat {
    public static String userNameFormat(String userName) {
        userName = userName.replace("@", "");
        userName = userName.replace("https://t.me/", "");
        return userName;
    }
}
