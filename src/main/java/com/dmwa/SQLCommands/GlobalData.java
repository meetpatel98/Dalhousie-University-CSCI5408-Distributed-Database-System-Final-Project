package com.dmwa.SQLCommands;

import com.dmwa.GCPConfiguration.GCPConfig;
import com.dmwa.GCPConfiguration.GCPWriter;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.List;

public class GlobalData {

    // Writing the global data of which table belongs to which instance
    public void writeToGlobalData(String tableName, String path) throws Exception {
        String line = "";
        String splitBy = "\n";
        BufferedReader reader = new BufferedReader(new FileReader("src/main/java/com/dmwa/GlobalData/GlobalData.txt"));
        List<String> addTable = new ArrayList<>();
        while ((line = reader.readLine()) != null) {
            String[] globalData = line.split(splitBy);
            addTable.add(globalData[0]);
        }

        FileWriter fileWriter = new FileWriter("src/main/java/com/dmwa/GlobalData/GlobalData.txt");
        for (int i = 0; i < addTable.size(); i++) {
            if (path.contains("VM1") && i == 1) {
                fileWriter.append(addTable.get(i) + "~" + tableName + "\n");
            } else if (i == 2 && path.contains("VM2")) {
                fileWriter.append(addTable.get(i) + "~" + tableName + "\n");
            } else {
                fileWriter.append(addTable.get(i) + "\n");
            }

        }
        fileWriter.flush();
        fileWriter.close();
        GCPWriter gw=new GCPWriter();
        gw.writeFile("src/main/java/com/dmwa/GlobalData/GlobalData.txt","/home/"+ GCPConfig.VM2_User +"/Project/csci-5408-w2022-project-dpg15/src/main/java/com/dmwa/GlobalData/GlobalData.txt");

    }

    // Deleting the global data of tables from respective instance
    public void deleteFromGlobalData(String tableName, String path) throws Exception {
        String line = "";
        String splitBy = "\n";
        BufferedReader reader = new BufferedReader(new FileReader("src/main/java/com/dmwa/GlobalData/GlobalData.txt"));
        List<String> removeTable = new ArrayList<>();
        while ((line = reader.readLine()) != null) {
            String[] users = line.split(splitBy);
            removeTable.add(users[0]);
        }

        FileWriter fileWriter = new FileWriter("src/main/java/com/dmwa/GlobalData/GlobalData.txt");
        for (int i = 0; i < removeTable.size(); i++) {
            if (path.contains("VM1") && i == 2) {
                fileWriter.append((removeTable.get(i).replace(tableName, "")));
            } else if (i == 4 && path.contains("VM2")) {
                fileWriter.append((removeTable.get(i).replace(tableName, "")));
            } else {
                fileWriter.append(removeTable.get(i) + "\n");
            }

        }
        fileWriter.flush();
        fileWriter.close();
    }

}
