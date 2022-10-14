package com.dmwa.SQLCommands;

import com.dmwa.GCPConfiguration.GCPConfig;
import com.dmwa.GCPConfiguration.GCPWriter;
import com.dmwa.Transaction.Locking;

import java.io.*;
import java.time.Instant;
import java.util.*;

public class InsertData {

    FileOperation fileOperation = new FileOperation();
    public void InsertData(String table, String database, String path, List<String> values) throws Exception {

        Map<String,List<String>> readTable = new HashMap<>();
        readTable = readFromTable(table,database,path);

        int primaryKey = getPrimaryKey(readTable.get("meta"));
        String valueToCompare = values.get(primaryKey);

        List<String> columnNames = readTable.get("column");
        int totalColumns = columnNames.size();

        List<String> rows = readTable.get("value");
        if(rows.size()==0){
            writeIntoTable(table, database, path, values);
        }else {
            List<List<String>> row_values = get_row_data(rows, totalColumns);

            List<String> primayKeyColumnValues = new ArrayList<>();
            for (int i = 0; i < row_values.size(); i++) {
                List<String> row = row_values.get(i);
                primayKeyColumnValues.add(row.get(primaryKey));
            }

            boolean isError = false;
            for (String s : primayKeyColumnValues) {
                if (s.equals(valueToCompare)) {
                    System.out.println("Duplicate values in Primary key! Insertion failed.");
                    fileOperation.reportToEventErrorLog("Duplicate values in Primary key! Insertion failed.");
                    isError = true;
                    break;
                }
            }
            if (!isError) {
                writeIntoTable(table, database, path, values);
            }
        }
    }
    public Map<String, List<String>> readFromTable(String tableName, String databaseName, String path) throws IOException, IOException {
        String line = "";
        String splitBy = "\n";
        int counter = 0;
        String filePath = "";

        ArrayList<String> tableValues = new ArrayList<>();
        Map<String, List<String>> readTable = new HashMap<>();
        List<List<String>> rowTracker = new ArrayList<>();
        List<String> data = new ArrayList<>();
        filePath = getTablePath(tableName, databaseName, path, filePath);
        BufferedReader br = new BufferedReader(new FileReader(filePath));
        while ((line = br.readLine()) != null) {
            String[] users = line.split(splitBy);
            tableValues.add(users[0]);
        }


        for (String i : tableValues) {

            if (counter == 2) {

                List<String> db = new ArrayList<>();
                db.add(i);
                readTable.put("database", db);
            } else if (counter == 4) {

                List<String> tabel = new ArrayList<>();
                tabel.add(i);
                System.out.println(i);
                readTable.put("table", tabel);
            } else if (counter == 6) {

                List<String> columns = Arrays.asList(i.split("~"));
                readTable.put("column", columns);
            } else if (counter == 8) {

                List<String> metas = Arrays.asList(i.split("~"));
                readTable.put("meta", metas);
            } else if (counter > 9) {

                List<String> rows = Arrays.asList(i.split("~"));
                rowTracker.add(rows);
            }
            counter++;
        }


        for (List<String> row : rowTracker) {
            for (String value : row) {
                data.add(value);
            }
        }
        readTable.put("value", data);


        return readTable;
    }

    public String writeIntoTable(String tableName,String databaseName, String location , List<String> columnValues) throws Exception {
        int temp = 1;
        String response = null;
        Locking tableLock = new Locking();
        tableLock.setLock(tableName,"Insert Query");
        File file = getFileWrite(tableName, databaseName, location);


        if (file.exists()) {
            FileWriter fileWriter = new FileWriter(file, true);
            if (fileWriter != null) {
                fileWriter.append("\n");
                for (String column : columnValues) {
                    if (temp == (columnValues.size())) {
                        fileWriter.append(column);
                    } else {
                        fileWriter.append(column + "~");
                    }

                    temp++;
                }
                response = "Data inserted successfully";
                fileOperation.reportToLog(response, 1);
                QueryExecutionTime queryExecutionTime = new QueryExecutionTime();
                queryExecutionTime.setT2(Instant.now().toEpochMilli());
            } else {
                response = "Error accessing the table";
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

        return response;
    }

    private int getPrimaryKey(List<String> meta) {
        int i=0;
        for(String column : meta){
            if(column.contains("$")){
                return i;
            }
            i++;
        }
        return 0;
    }

    public List<List<String>> get_row_data(List<String> rows, int total_columns){
        List<List<String>> rows_value = new ArrayList<>();
        List<String> temp_list = new ArrayList<>();
        for(int i=1;i<=rows.size();i++){
            if(i % total_columns == 0){
                temp_list.add(rows.get(i-1));
                rows_value.add(temp_list);
                temp_list = new ArrayList<>();
            }else{
                temp_list.add(rows.get(i-1));
            }
        }
        return rows_value;
    }

    private File getFileWrite(String tableName, String databaseName, String location) {
        String response;
        File file = new File("src/main/java/com/dmwa/Database/" + databaseName + ".txt");

        if (location.equalsIgnoreCase("VM1")) {
            if (file.exists()) {
                file = new File("src/main/java/com/dmwa/Tables/VM1/" + tableName + ".txt");
            } else {
                response = "Invalid Database!!";
                fileOperation.reportToEventErrorLog(response);
            }
        } else if (location.equalsIgnoreCase("VM2")) {
            if (file.exists()) {
                file = new File("src/main/java/com/dmwa/Tables/VM2/" + tableName + ".txt");
            } else {
                response = "Invalid Database!!";
                fileOperation.reportToEventErrorLog(response);
            }
        }
        return file;
    }

    private String getTablePath(String tableName, String databaseName, String path, String filePath) {
        File file = new File("src/main/java/com/dmwa/Database/" + databaseName + ".txt");
        String response;

        if (path.equalsIgnoreCase("VM1")) {
            if (file.exists()) {
                filePath = "src/main/java/com/dmwa/Tables/VM1/" + tableName + ".txt";
            } else {
                response = "Invalid Database!!";
                fileOperation.reportToEventErrorLog(response);
            }
        } else if(path.equalsIgnoreCase("VM2")) {
            if (file.exists()) {
                filePath = "src/main/java/com/dmwa/Tables/VM2/" + tableName + ".txt";
            } else {
                response = "Invalid Database!!";
                fileOperation.reportToEventErrorLog(response);
            }
        }
        return filePath;
    }
}
