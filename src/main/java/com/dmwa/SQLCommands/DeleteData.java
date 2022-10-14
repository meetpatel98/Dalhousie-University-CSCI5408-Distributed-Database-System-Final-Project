package com.dmwa.SQLCommands;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.time.Instant;
import java.util.*;

public class DeleteData {
    public void deleteData(String table, String database, String path, List<String> constraints) throws Exception {

        Map<String,List<String>> readData = new HashMap<>();
        readData = readFromTable(table,database, path);
        FileOperation fileOperation = new FileOperation();

        List<String> nameOfColumns = readData.get("column");
        int totalColumns = nameOfColumns.size();
        List<String> rows = readData.get("value");
        List<List<String>> resultSet = new ArrayList<>();
        List<String> meta = readData.get("meta");
        List<List<String>> valueOfRows = getRowValues(rows, totalColumns);

        if (constraints.size() == 0) {
            List<String> data = new ArrayList<>();
            readData.replace("value", data);
        } else {
            String constraint = constraints.get(0);
            if (constraint.contains(">")) {
                List<String> splitConstraint = Arrays.asList(constraint.split(">"));
                String colName = splitConstraint.get(0);
                int colNumber = getColumnNo(nameOfColumns, colName);
                String typeOfCol = meta.get(colNumber);
                if (typeOfCol.contains("int") || typeOfCol.contains("float")) {
                    for (int i = 0; i < valueOfRows.size(); i++) {
                        List<String> row = valueOfRows.get(i);
                        int IntColValue = Integer.parseInt(row.get(colNumber));
                        if (IntColValue > Integer.parseInt(splitConstraint.get(1))) {
                            continue;
                        } else {
                            resultSet.add(row);
                        }
                    }
                    print_list(resultSet);
                    List<String> result = new ArrayList<>();
                    result = getRowsToWrite(resultSet);
                    readData.replace("value", result);
                } else {
                    System.out.println("Can not use > with string value");
                }

            } else if (constraint.contains("<")) {
                List<String> splitConstraint = Arrays.asList(constraint.split("<"));
                String nameOfCol = splitConstraint.get(0);
                int noOfCol = getColumnNo(nameOfColumns, nameOfCol);
                String typeOfColumn = meta.get(noOfCol);
                if (typeOfColumn.contains("int") || typeOfColumn.contains("float")) {
                    for (int i = 0; i < valueOfRows.size(); i++) {
                        List<String> row = valueOfRows.get(i);
                        int IntColValue = Integer.parseInt(row.get(noOfCol));
                        if (IntColValue < Integer.parseInt(splitConstraint.get(1))) {
                            continue;
                        } else {
                            resultSet.add(row);
                        }
                    }
                    print_list(resultSet);
                    List<String> result = new ArrayList<>();
                    result = getRowsToWrite(resultSet);
                    readData.replace("value", result);
                } else {
                    System.out.println("Can not use < with string value");
                }

            } else if (constraint.contains("=")) {
                List<String> splitConstraint = Arrays.asList(constraint.split("="));
                String colName = splitConstraint.get(0);
                int colNumber = getColumnNo(nameOfColumns, colName);
                String typeOfCol = meta.get(colNumber);
                if (typeOfCol.toLowerCase().contains("int") || typeOfCol.toLowerCase().contains("float")) {
                    for (int i = 0; i < valueOfRows.size(); i++) {
                        List<String> row = valueOfRows.get(i);
                        String columnValueInt = row.get(colNumber);
                        if (columnValueInt.equals(splitConstraint.get(1))) {
                            continue;
                        } else {
                            resultSet.add(row);
                        }
                    }
                    print_list(resultSet);
                    List<String> result_values = new ArrayList<>();
                    result_values = getRowsToWrite(resultSet);
                    readData.replace("value", result_values);
                } else {
                    System.out.println("Can not use = with string value");
                }
            }
        }

        //write to table
        UpdateData updateTable = new UpdateData();
        String result = updateTable.updateOrDelete(readData.get("table").get(0), readData.get("database").get(0), path, readData);
        System.out.println(result);
        QueryExecutionTime queryExecutionTime = new QueryExecutionTime();
        queryExecutionTime.setT2(Instant.now().toEpochMilli());
        fileOperation.reportToLog(result, 1);
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
    private String getTablePath(String tableName, String databaseName, String path, String filePath) {
        File file = new File("src/main/java/com/dmwa/Database/" + databaseName + ".txt");


        if (path.equalsIgnoreCase("VM1")) {
            if (file.exists()) {
                filePath = "src/main/java/com/dmwa/Tables/VM1/" + tableName + ".txt";
            } else {
                //response = "Invalid Database!!";
            }
        } else if(path.equalsIgnoreCase("VM2")) {
            if (file.exists()) {
                filePath = "src/main/java/com/dmwa/Tables/VM2/" + tableName + ".txt";
            } else {
                //response = "Invalid Database!!";
            }
        }
        return filePath;
    }
    private List<String> getRowsToWrite(List<List<String>> result) {
        List<String> tempData = new ArrayList<>();
        for (List<String> row : result) {
            tempData.addAll(row);
        }
        return tempData;
    }
    public List<List<String>> getRowValues(List<String> rows, int columns) {
        List<String> tempRowData = new ArrayList<>();
        List<List<String>> rowData = new ArrayList<>();
        for (int i = 1; i <= rows.size(); i++) {
            if (i % columns == 0) {
                tempRowData.add(rows.get(i - 1));
                rowData.add(tempRowData);
                tempRowData = new ArrayList<>();
            } else {
                tempRowData.add(rows.get(i - 1));
            }
        }
        return rowData;
    }
    public int getColumnNo(List<String> columns, String columnName) {
        int i = 0;
        for (String name : columns) {
            if (name.equals(columnName)) {
                return i;
            }
            i++;
        }
        return 0;
    }
    private void print_list(List<List<String>> result_list) {

        for (List<String> row : result_list) {
            for (String value : row) {
                System.out.print(value + "  ");
            }
            System.out.println();
        }

    }
}
