package com.tsypk.sniper3.utils;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

/**
 * @author tsypk on 16.11.2021 20:26
 * @project sniper-3
 */
public class PropertyManager {
    private final static String configFolder = "jboss.server.config.dir";
    private final static String propertyFile = "app.properties";
    private final static String localPropertyFile = "/Users/tsypk/IdeaProjects/sniper-3/src/main/resources/app.properties";

    public static String getProperty(String propertyName) {
        Properties properties = new Properties();
        FileInputStream fis;
        String absPath = System.getProperty(configFolder);
        try {
            fis = new FileInputStream(absPath.concat("/").concat(propertyFile));
            properties.load(fis);
            return properties.getProperty(propertyName);
        } catch (IOException unexpected) {
            unexpected.printStackTrace();
            return null;
        }
    }
}
