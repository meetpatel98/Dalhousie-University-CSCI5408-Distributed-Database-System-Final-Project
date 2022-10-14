package com.dmwa.SQLCommands;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.time.Instant;
import java.util.*;

public class SelectData {
    FileOperation fileOperation = new FileOperation();
    public void selectData(String tableName, String database, String path, List<String> selectedColumns, List<String> constraint) throws IOException {

        Map<String,List<String>> readTable = new HashMap<>();
        readTable = readFromTable(tableName,database, path);

        List<String> ColName = readTable.get("column");
        int colLength = ColName.size();

        List<String> rows = readTable.get("value");
        List<List<String>> rowData = getRows(rows, colLength);
        List<List<String>> resultSet = new ArrayList<>();
        List<String> meta = readTable.get("meta");

        if(constraint.size()==0){
            for (List<String> row : rowData) {
                for (String value : row) {
                    System.out.print(value + "  ");
                }
                System.out.println();
            }
        }else {
            String constraints = constraint.get(0);
            if (constraints.contains(">")) {
                List<String> splitConstraint = Arrays.asList(constraints.split(">"));
                String colName = splitConstraint.get(0);
                int colNumber = getColumns(ColName, colName);
                String typeOfColumn = meta.get(colNumber);
                if (typeOfColumn.contains("int") || typeOfColumn.contains("float")) {
                    for (int i = 0; i < rowData.size(); i++) {
                        List<String> row = rowData.get(i);
                        int intValue = Integer.parseInt(row.get(colNumber));
                        if (intValue > Integer.parseInt(splitConstraint.get(1))) {
                            resultSet.add(row);
                        }
                    }
                    printData(resultSet, selectedColumns, ColName);
                } else {
                    System.out.println("Can not use > with string value");
                }

            } else if (constraints.contains("<")) {
                List<String> splitConstraint = Arrays.asList(constraints.split("<"));
                String column_name = splitConstraint.get(0);
                int column_number = getColumns(ColName, column_name);
                String column_type = meta.get(column_number);
                if (column_type.contains("int") || column_type.contains("float")) {
                    //for (List<String> row : row_values) {
                    for (int i = 0; i < rowData.size(); i++) {
                        List<String> row = rowData.get(i);
                        int columnValueInt = Integer.parseInt(row.get(column_number));
                        if (columnValueInt < Integer.parseInt(splitConstraint.get(1))) {
                            resultSet.add(row);
                        }
                    }
                    printData(resultSet, selectedColumns, ColName);
                } else {
                    System.out.println("Can not use < with string value");
                }

            } else if (constraints.contains("=")) {
                List<String> condition_divided = Arrays.asList(constraints.split("="));
                String column_name = condition_divided.get(0);
                int column_number = getColumns(ColName, column_name);
                String column_type = meta.get(column_number);
                if(column_type.contains("$")){
                    column_type=column_type.replace("$","");
                }
                if (column_type.toLowerCase(Locale.ROOT).contains("int") || column_type.toLowerCase(Locale.ROOT).contains("float") || column_type.toLowerCase(Locale.ROOT).contains("varchar")) {
                    //for (List<String> row : row_values) {
                    for (int i = 0; i < rowData.size(); i++) {
                        List<String> row = rowData.get(i);
                        String columnValueInt = row.get(column_number);
                        if (columnValueInt.equals(condition_divided.get(1))) {
                            resultSet.add(row);
                        }
                    }
                    printData(resultSet, selectedColumns, ColName);
                } else {
                    System.out.println("Can not use = with string value");
                }
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
            QueryExecutionTime queryExecutionTime = new QueryExecutionTime();
            queryExecutionTime.setT2(Instant.now().toEpochMilli());
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

        String response;
        if (path.equalsIgnoreCase("VM1")) {

            if (file.exists()) {
                filePath = "src/main/java/com/dmwa/Tables/VM1/" + tableName + ".txt";
            } else {
                response = "Invalid Database!!";
                fileOperation.reportToLog(response, 0);
            }
        } else if(path.equalsIgnoreCase("VM2")) {
            if (file.exists()) {
                filePath = "src/main/java/com/dmwa/Tables/VM2/" + tableName + ".txt";
            } else {
                response = "Invalid Database!!";
                fileOperation.reportToLog(response, 0);
            }
        }
        return filePath;
    }

    private void printData(List<List<String>> resultSet, List<String> columnValues, List<String> columnNames) {
        if (columnValues.get(0).equals("*")) {
            for (List<String> row : resultSet) {
                for (String value : row) {
                    System.out.print(value + "  ");
                }
                System.out.println();
            }
        } else {
            for (List<String> row : resultSet) {
                int i=0;
                for (String value : row) {
                    if(columnValues.contains(columnNames.get(i))) {
                        System.out.print(value + "  ");
                    }
                    i++;
                }
                System.out.println();
            }
        }
    }

    public int getColumns(List<String> columns, String colName) {
        int i = 0;
        for (String name : columns) {
            if(name.contains("$")){
                name=name.replace("$","");
            }
            if (name.equals(colName)) {
                return i;
            }
            i++;
        }
        System.out.println("Column does not exist!");
        return 0;
    }

    public List<List<String>> getRows(List<String> rows, int totalNoCols) {
        List<List<String>> rowData = new ArrayList<>();
        List<String> dataSet = new ArrayList<>();
        for (int i = 1; i <= rows.size(); i++) {
            if (i % totalNoCols == 0) {
                dataSet.add(rows.get(i - 1));
                rowData.add(dataSet);
                dataSet = new ArrayList<>();
            } else {
                dataSet.add(rows.get(i - 1));
            }
        }
        return rowData;
    }
}
