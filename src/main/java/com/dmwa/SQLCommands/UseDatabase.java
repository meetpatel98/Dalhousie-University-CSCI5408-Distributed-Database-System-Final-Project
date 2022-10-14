package com.dmwa.SQLCommands;

import java.io.File;
import java.io.FileWriter;

public class UseDatabase {
    // Generating file for each database in Database folder
    public String useDatabase(String dbName) {
        String result = null;
        try {
            File file = new File("src/main/java/com/dmwa/Database/" + dbName + ".txt");
            if (file.exists()) {
                result=dbName;
            } else {
                result = "Database does not exists";
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println(result);
        return result;
    }
}
