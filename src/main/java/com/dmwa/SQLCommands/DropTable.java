package com.dmwa.SQLCommands;

import com.dmwa.GCPConfiguration.GCPConfig;
import com.dmwa.GCPConfiguration.GCPWriter;
import com.dmwa.Transaction.Locking;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.List;

public class DropTable {
    String result = null;

    public String dropTable(String tableName, String database, String path) throws Exception {

        Locking tableLock = new Locking();
        tableLock.setLock(tableName,"Drop Query");
        File file = new File("src/main/java/com/dmwa/Database/" + database + ".txt");

        if (path.equalsIgnoreCase("VM1")) {
            if (file.exists()) {
                file = new File("src/main/java/com/dmwa/Tables/VM1/" + tableName + ".txt");
            } else {
                result = "Invalid Database";
            }
        } else if(path.equalsIgnoreCase("VM2")){
            if (file.exists()) {
                file = new File("src/main/java/com/dmwa/Tables/VM2/" + tableName + ".txt");
            } else {
                result = "Invalid Database";
            }
        }
        try {
            if (file.exists()) {
                tableDelFromDB(database, tableName);
                deleteFK(tableName);
                GlobalData masterRecord = new GlobalData();
                masterRecord.deleteFromGlobalData(tableName, path);
                boolean deleteTable = (file).delete();

                if (deleteTable) {
                    result = "Table deleted successfully";
                } else {
                    result = "Table doesn't exist";
                }
            }


        } catch (Exception e) {
            e.printStackTrace();
        }
        tableLock.removeLock(tableName);
        return result;

    }

    public void tableDelFromDB(String dbName, String tableName) throws Exception {
        String line = "";
        String splitBy = "\n";
        BufferedReader reader = new BufferedReader(new FileReader("src/main/java/com/dmwa/Database/" + dbName + ".txt"));
        List<String> delTable = new ArrayList<>();
        while ((line = reader.readLine()) != null) {
            String[] users = line.split(splitBy);
            delTable.add(users[0]);
        }
        for (int i = 0; i < delTable.size(); i++) {
            if (delTable.get(i).contains(tableName)) {
                delTable.remove(i);
            }
        }
        FileWriter fileWriter = new FileWriter("src/main/java/com/dmwa/Database/" + dbName + ".txt");
        for (int i = 0; i < delTable.size(); i++) {
            fileWriter.append(delTable.get(i) + "\n");
        }
        fileWriter.flush();
        fileWriter.close();
        GCPWriter gw=new GCPWriter();
        gw.writeFile("src/main/java/com/dmwa/Database/" + dbName + ".txt","/home/"+ GCPConfig.VM2_User +"/Project/csci-5408-w2022-project-dpg15/src/main/java/com/dmwa/Database/"+ dbName + ".txt");

    }

    public void deleteFK(String tableName) throws Exception {
        String line = "";
        String splitBy = "\n";
        BufferedReader reader = new BufferedReader(new FileReader("src/main/java/com/dmwa/GlobalData/ForeignKeyReferences.txt"));
        List<String> removeFK = new ArrayList<>();
        while ((line = reader.readLine()) != null) {
            String[] users = line.split(splitBy);
            removeFK.add(users[0]);
        }
        for (int i = 0; i < removeFK.size(); i++) {


            if (removeFK.get(i).contains(tableName)) {
                removeFK.remove(i);
            }
        }
        FileWriter fileWriter = new FileWriter("src/main/java/com/dmwa/GlobalData/ForeignKeyReferences.txt");
        for (int i = 0; i < removeFK.size(); i++) {
            fileWriter.append(removeFK.get(i) + "\n");
        }
        fileWriter.flush();
        fileWriter.close();
    }


}
