package com.zherikhov.listshop.utils;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Objects;
import java.util.Properties;

public class ResourcesUtil {

    public static String getProperties(String path, String variable) {
        String configPath = Objects.requireNonNull(Thread.currentThread().getContextClassLoader().getResource(path)).getPath();

        Properties properties = new Properties();
        try {
            properties.load(new FileInputStream(configPath));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        return properties.getProperty(variable);
    }
}
