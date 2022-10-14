package com.dmwa.SQLCommands;

import com.dmwa.GCPConfiguration.GCPConfig;
import com.dmwa.GCPConfiguration.GCPWriter;

import java.io.*;
import java.time.Instant;
import java.util.List;

public class CreateTable {

    public String createTable(String tablename, String dbName, String path, List<String> columnNames,
            List<String> columnType, List<String> foreignKey) throws Exception {
        FileOperation fileOperation = new FileOperation();
        String result = null;
        int temp = 1;
        String gcpLocation1=null;
        String gcpLocation2=null;
        File file = new File("src/main/java/com/dmwa/Database/" + dbName + ".txt");
        ;
        if (path.equalsIgnoreCase("VM1")) {

            if (file.exists()) { // check if database file exists
                file = new File("src/main/java/com/dmwa/Tables/VM1/" + tablename + ".txt");
                gcpLocation1="src/main/java/com/dmwa/Tables/VM1/" + tablename + ".txt";
                gcpLocation2="/home/"+ GCPConfig.VM2_User +"/Project/csci-5408-w2022-project-dpg15/src/main/java/com/dmwa/Tables/VM1/"+ tablename + ".txt";
            } else {
                result = "Invalid Database!!";
                fileOperation.reportToEventErrorLog(result);
            }
        } else {
            if (file.exists()) { // check if database file exists
                file = new File("src/main/java/com/dmwa/Tables/VM2/" + tablename + ".txt");
                gcpLocation1="src/main/java/com/dmwa/Tables/VM2/" + tablename + ".txt";
                gcpLocation2="/home/"+ GCPConfig.VM2_User +"/Project/csci-5408-w2022-project-dpg15/src/main/java/com/dmwa/Tables/VM2/"+ tablename + ".txt";
            } else {
                result = "Invalid Database!!";
                fileOperation.reportToEventErrorLog(result);
            }
        }
        try {

            if (!file.exists()) {
                FileWriter fileWriter = new FileWriter("src/main/java/com/dmwa/Database/" + dbName + ".txt", true);
                if (fileWriter != null) {
                    fileWriter.append("\n");
                    fileWriter.append(tablename);
                    fileWriter.flush();
                    fileWriter.close();
                    fileWriter = new FileWriter(file);
                    if (fileWriter != null) {
                        fileWriter.append("#TABLE\n@database\n" + dbName + "\n@table\n" + tablename + "\n@column\n");
                        for (String column : columnNames) {
                            if (temp == (columnNames.size())) {
                                fileWriter.append(column);
                            } else {
                                fileWriter.append(column + "~");
                            }

                            temp++;
                        }
                        temp = 1;
                        fileWriter.append("\n" + "@meta\n");
                        for (String column : columnType) {
                            if (temp == (columnType.size())) {
                                fileWriter.append(column);
                            } else {
                                fileWriter.append(column + "~");
                            }

                            temp++;
                        }
                        fileWriter.append("\n" + "@value");
                        result = "Table Created Successfully";
                        fileOperation.reportToLog(result, 1);
                        QueryExecutionTime queryExecutionTime = new QueryExecutionTime();
                        queryExecutionTime.setT2(Instant.now().toEpochMilli());
                    } else {
                        result = "Error in table";
                        fileOperation.reportToEventErrorLog(result);
                    }
                } else {
                    result = "Error in database table";
                    fileOperation.reportToEventErrorLog(result);
                }
                fileWriter.flush();
                fileWriter.close();

                GCPWriter gw=new GCPWriter();
                gw.writeFile(gcpLocation1,gcpLocation2);

                enterForeignKeyDetails(foreignKey);
                GlobalData masterRecord = new GlobalData();
                masterRecord.writeToGlobalData(tablename, path);
            } else {
                result = "Table already exists";
                fileOperation.reportToGeneralLog(result);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return result;
    }

    public void enterForeignKeyDetails(List<String> foreignKey) throws Exception {
        FileWriter fileWriter = new FileWriter("src/main/java/com/dmwa/GlobalData/ForeignKeyReferences.txt", true);
        String merge = "";
        for (int i = 0; i < foreignKey.size(); i++) {
            merge += foreignKey.get(i) + "\t";
        }
        fileWriter.append(merge + "\n");
        fileWriter.flush();
        fileWriter.close();
        GCPWriter gw=new GCPWriter();
        gw.writeFile("src/main/java/com/dmwa/GlobalData/ForeignKeyReferences.txt","/home/"+ GCPConfig.VM2_User +"/Project/csci-5408-w2022-project-dpg15/src/main/java/com/dmwa/GlobalData/ForeignKeyReferences.txt");

    }

}
