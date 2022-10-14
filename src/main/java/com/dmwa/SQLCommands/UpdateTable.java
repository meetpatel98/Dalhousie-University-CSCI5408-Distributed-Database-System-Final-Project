package com.dmwa.SQLCommands;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.time.Instant;
import java.util.*;

public class UpdateTable {
    FileOperation fileOperation = new FileOperation();
    public void updateOperation(String table, String database, String path, List<String> queryColName, List<String> queryValue, List<String> whereCondition) throws Exception {

        Map<String,List<String>> readTable = new HashMap<>();
        readTable = readFromTable(table,database, path);

        List<String> colNames = readTable.get("column");
        int columns = colNames.size();
        List<String> rows = readTable.get("value");
        List<List<String>> rowData = getRows(rows, columns);
        List<List<String>> resultSet = new ArrayList<>();
        List<String> meta = readTable.get("meta");

        int colNumToModify = getColNo(colNames, queryColName.get(0));

        if(whereCondition.size()==0){
            String nameOfCol = queryColName.get(0);
            int colNumber = getColNo(colNames, nameOfCol);
            List<String> tempResultSet = new ArrayList<>();
            for(List<String> row : rowData){
                for(int i=0;i<row.size();i++){
                    if(i== colNumber){
                        tempResultSet.add(queryValue.get(0));
                    }else{
                        tempResultSet.add(row.get(i));
                    }
                }
            }
            readTable.replace("value", tempResultSet);


        }else{
            String constraint = whereCondition.get(0);

            if (constraint.contains(">")) {

                List<String> constraintSplit = Arrays.asList(constraint.split(">"));
                String colName = constraintSplit.get(0);
                int colNum = getColNo(colNames, colName);
                String typeOfColumn = meta.get(colNum);
                if (typeOfColumn.contains("int") || typeOfColumn.contains("float")) {

                    for (int i = 0; i < rowData.size(); i++) {
                        List<String> row = rowData.get(i);
                        int intValue = Integer.parseInt(row.get(colNum));
                        if (intValue > Integer.parseInt(constraintSplit.get(1))) {
                            List<List<String>> finalResultSet = new ArrayList<>();
                            finalResultSet.add(row);
                            finalResultSet = getUpdatedList(finalResultSet, colNumToModify, queryValue);
                            resultSet.add(finalResultSet.get(0));
                        } else {
                            resultSet.add(row);
                        }
                    }

                    List<String> resultData = new ArrayList<>();
                    resultData = getRowValuesToWriteInTable(resultSet);
                    readTable.replace("value", resultData);
                } else {
                    System.out.println("Can not use > with string value");
                }

            } else if (constraint.contains("<")) {
                List<String> constraintSplit = Arrays.asList(constraint.split("<"));
                String colName = constraintSplit.get(0);
                int colNum = getColNo(colNames, colName);
                String typeOfCol = meta.get(colNum);
                if (typeOfCol.contains("int") || typeOfCol.contains("float")) {
                    for (int i = 0; i < rowData.size(); i++) {
                        List<String> row = rowData.get(i);
                        int intValue = Integer.parseInt(row.get(colNum));
                        if (intValue < Integer.parseInt(constraintSplit.get(1))) {
                            List<List<String>> finalResultSet = new ArrayList<>();
                            finalResultSet.add(row);
                            finalResultSet = getUpdatedList(finalResultSet, colNumToModify, queryValue);
                            resultSet.add(finalResultSet.get(0));
                        }else{
                            resultSet.add(row);
                        }
                    }
                    List<String> resultData = new ArrayList<>();
                    resultData = getRowValuesToWriteInTable(resultSet);
                    readTable.replace("value", resultData);
                } else {
                    System.out.println("Can not use < with string value");
                }

            } else if (constraint.contains("=")) {
                List<String> constraintSplit = Arrays.asList(constraint.split("="));
                String colName = constraintSplit.get(0);
                int colNum = getColNo(colNames, colName);
                String typeOfCol = meta.get(colNum);
                if (typeOfCol.toLowerCase().contains("int") || typeOfCol.toLowerCase().contains("float")) {
                    for (int i = 0; i < rowData.size(); i++) {
                        List<String> row = rowData.get(i);
                        String intValue = row.get(colNum);
                        if (intValue.equals(constraintSplit.get(1))) {
                            List<List<String>> finalResultList = new ArrayList<>();
                            finalResultList.add(row);
                            finalResultList = getUpdatedList(finalResultList, colNumToModify, queryValue);
                            resultSet.add(finalResultList.get(0));
                        }else{
                            resultSet.add(row);
                        }
                    }

                    List<String> result_values = new ArrayList<>();
                    result_values = getRowValuesToWriteInTable(resultSet);
                    readTable.replace("value", result_values);
                } else {
                    System.out.println("Can not use = with string value");
                }
            }
        }

        UpdateData updateData = new UpdateData();
        String result = updateData.updateOrDelete(readTable.get("table").get(0), readTable.get("database").get(0), path, readTable);
        System.out.println(result);
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

    private List<List<String>> getUpdatedList(List<List<String>> result_list, int columnNumberForUpdate, List<String> valueInQuery) {
        List<List<String>> temp_list = new ArrayList<>();
        for(List<String> row : result_list){
            row.set(columnNumberForUpdate,valueInQuery.get(0));
            temp_list.add(row);
        }
        return temp_list;
    }

    public List<List<String>> getRows(List<String> rows, int totalCols) {
        List<List<String>> valueOfRows = new ArrayList<>();
        List<String> tempData = new ArrayList<>();
        for (int i = 1; i <= rows.size(); i++) {
            if (i % totalCols == 0) {
                tempData.add(rows.get(i - 1));
                valueOfRows.add(tempData);
                tempData = new ArrayList<>();
            } else {
                tempData.add(rows.get(i - 1));
            }
        }
        return valueOfRows;
    }

    public int getColNo(List<String> columns, String column_name) {
        int i = 0;
        for (String name : columns) {
            if(name.contains("$")){
                name=name.replace("$","");
            }
            if (name.equals(column_name)) {
                return i;
            }
            i++;
        }
        System.out.println("Column does not exist!");
        fileOperation.reportToEventErrorLog("Column does not exist!");
        return 0;
    }


    private List<String> getRowValuesToWriteInTable(List<List<String>> result_list) {
        List<String> temp_list = new ArrayList<>();
        for (List<String> row : result_list) {
            temp_list.addAll(row);
        }
        return temp_list;
    }
}
