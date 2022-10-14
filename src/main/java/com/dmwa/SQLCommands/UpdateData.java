package com.dmwa.SQLCommands;

import com.dmwa.GCPConfiguration.GCPConfig;
import com.dmwa.GCPConfiguration.GCPWriter;
import com.dmwa.Transaction.Locking;

import java.io.File;
import java.io.FileWriter;
import java.time.Instant;
import java.util.List;
import java.util.Map;

public class UpdateData {
    FileOperation fileOperation = new FileOperation();
    public String updateOrDelete(String tableName, String databaseName , String location, Map<String, List<String>> tableValues) throws Exception {
        String response = null;
        Locking tableLock = new Locking();
        tableLock.setLock(tableName,"Update Query");
        File file = new File("src/main/java/com/dmwa/Database/" + databaseName + ".txt");

        if (location.equalsIgnoreCase("VM1")) {
            if (file.exists()) {
                file = new File("src/main/java/com/dmwa/Tables/VM1/" + tableName + ".txt");
            } else {
                response = "Invalid Database!!";
                fileOperation.reportToLog(response, 1);
            }
        } else if(location.equalsIgnoreCase("VM2")){
            if (file.exists()) {
                file = new File("src/main/java/com/dmwa/Tables/VM2/" + tableName + ".txt");
            } else {
                response = "Invalid Database!!";
                fileOperation.reportToLog(response, 1);
            }
        }
        try {
            if (file.exists()) {
                FileWriter fileWriter = new FileWriter(file);
                if (fileWriter != null) {

                    fileWriter.append("#TABLE\n@database\n");
                    fileWriter.append(tableValues.get("database").get(0));
                    fileWriter.append("\n@table\n");
                    fileWriter.append(tableValues.get("table").get(0));
                    fileWriter.append("\n@column\n");
                    fileWriter.append(getColumns(tableValues.get("column")));
                    fileWriter.append("\n@meta\n");
                    fileWriter.append(getColumns(tableValues.get("meta")));
                    fileWriter.append("\n@value\n");
                    fileWriter.append(getColValue(tableValues.get("value"), tableValues.get("column")));
                    response = "Data Modified Successfully";
                    fileOperation.reportToLog(response, 1);
                    QueryExecutionTime queryExecutionTime = new QueryExecutionTime();
                    queryExecutionTime.setT2(Instant.now().toEpochMilli());
                } else {
                    response = "Data Insertion Failed!";
                    fileOperation.reportToEventErrorLog(response);
                }
                fileWriter.flush();
                fileWriter.close();
            } else {
                response = "Table doesn't exist";
                fileOperation.reportToGeneralLog(response);
            }
            tableLock.removeLock(tableName);
            GCPWriter gw=new GCPWriter();
            gw.writeFile("src/main/java/com/dmwa/Tables/"+location+"/" + tableName + ".txt","/home/"+ GCPConfig.VM2_User +"/Project/csci-5408-w2022-project-dpg15/src/main/java/com/dmwa/Tables/"+location+"/" + tableName + ".txt");


        } catch (Exception e) {
            e.printStackTrace();
        }

        return response;
    }

    public String getColumns(List<String> column) {
        StringBuilder colString = new StringBuilder();
        for (int i = 0; i < column.size(); i++) {
            if (i == (column.size() - 1)) {
                colString.append(column.get(i));
            } else {
                colString.append(column.get(i) + "~");
            }
        }
        return colString.toString();
    }

    public String getColValue(List<String> value, List<String> column) {
        int colLength = column.size();
        StringBuilder colString = new StringBuilder();
        for (int i = 1; i <= value.size(); i++) {

            if (i % colLength == 0) {
                colString.append(value.get(i - 1) + "\n");
            } else {
                colString.append(value.get(i - 1) + "~");
            }
        }
        return colString.toString();
    }
}
