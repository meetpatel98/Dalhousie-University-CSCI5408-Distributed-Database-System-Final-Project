package com.dmwa.SQLCommands;

import java.io.File;
import java.io.FileWriter;
import java.time.Instant;

import com.dmwa.GCPConfiguration.GCPConfig;
import com.dmwa.GCPConfiguration.GCPWriter;

public class CreateDatabase {

    // Generating file for each database in Database folder
    public String createDatabase(String dbName) {
        FileOperation fileOperation = new FileOperation();
        String result = null;
        try {
            File file = new File("src/main/java/com/dmwa/Database/" + dbName + ".txt");
            if (!file.exists()) {
                FileWriter fileWriter = new FileWriter("src/main/java/com/dmwa/Database/" + dbName + ".txt");
                if (fileWriter != null) {
                    fileWriter.append("#DATABASE\n@name\n" + dbName + "\n@table");
                    result = "Database Created Successfully";
                    fileOperation.reportToLog(result, 0);
                    QueryExecutionTime queryExecutionTime = new QueryExecutionTime();
                    queryExecutionTime.setT2(Instant.now().toEpochMilli());
                } else {
                    result = "Database not created";
                    fileOperation.reportToEventErrorLog(result);
                }
                fileWriter.flush();
                fileWriter.close();
            } else {
                result = "Database already exists";
                fileOperation.reportToGeneralLog(result);
            }
            GCPWriter gw=new GCPWriter();
            gw.writeFile("src/main/java/com/dmwa/Database/" + dbName + ".txt","/home/"+ GCPConfig.VM2_User +"/Project/csci-5408-w2022-project-dpg15/src/main/java/com/dmwa/Database/"+ dbName + ".txt");


        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

}
