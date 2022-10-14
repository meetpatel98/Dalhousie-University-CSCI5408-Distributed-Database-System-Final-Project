package com.dmwa.Logs.utils;
import com.dmwa.Logs.constants.Constants;

import java.io.*;
import java.nio.file.Files;
import java.time.Instant;
import java.util.Properties;

public class UserSessionUtils {
    private static String username;
    private static String userLoginTimestamp;
    private static String databaseName;

    private static final Properties properties = new Properties();

    public static Properties getUserSessionProperties() {
        try {
            File resourcesDirectory = new File(Constants.DIRECTORY_RESOURCES);
            File userSessionPropertiesFile = new File("src/resources/UserSession.properties");
            FileInputStream fileInputStream = new FileInputStream(userSessionPropertiesFile.getCanonicalPath());
            properties.load(fileInputStream);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return properties;
    }

    public static String getUsername() {
        Properties properties = getUserSessionProperties();
        username = getProperty(properties);
        return username;
    }

    private static String getProperty(Properties properties) {
        return properties.getProperty("distributedDatabase.username");
    }


    public static String getLoginTimestamp() {
        Properties properties = getUserSessionProperties();
        userLoginTimestamp = getPropertiesProperty(properties);
        return userLoginTimestamp;
    }

    private static String getPropertiesProperty(Properties properties) {
        return properties.getProperty("distributedDatabase.loginTimestamp");
    }

    public static String getDatabaseName() {
        Properties properties = getUserSessionProperties();
        databaseName = properties.getProperty("distributedDatabase.databaseName");
        return databaseName;
    }

    public static void setUserSession(String username, Instant loginTimestamp) {
        Properties properties = getUserSessionProperties();
        try {
            File resourcesDirectory = new File(Constants.DIRECTORY_RESOURCES);
            File userSessionPropertiesFile = new File("src/resources/UserSession.properties");
            FileOutputStream outputStream = new FileOutputStream(userSessionPropertiesFile.getCanonicalPath());
            getSetProperty(username, properties);
            getObject(loginTimestamp, properties);
            properties.store(outputStream, null);
            System.out.println("User Session Stored Successfully!");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static Object getObject(Instant loginTimestamp, Properties properties) {
        return properties.setProperty("distributedDatabase.loginTimestamp", String.valueOf(loginTimestamp));
    }

    private static Object getSetProperty(String username, Properties properties) {
        return properties.setProperty("distributedDatabase.username", username);
    }


    public static void setDatabaseName(String databaseName) {
        Properties properties = getUserSessionProperties();
        try {
            File resourcesDirectory = new File(Constants.DIRECTORY_RESOURCES);
            File userSessionPropertiesFile = new File("src/resources/UserSession.properties");
            FileOutputStream outputStream = new FileOutputStream(userSessionPropertiesFile.getCanonicalPath());
            getObject(databaseName, properties);
            properties.store(outputStream, null);
            System.out.println("Information About Database Stored Successfully!");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static Object getObject(String databaseName, Properties properties) {
        return properties.setProperty("distributedDatabase.databaseName", databaseName);
    }

}
