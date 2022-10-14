package com.dmwa.SQLCommands;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class InsertDataSyntaxValidator implements IQueryParser {
    public String insertRegex = "INSERT INTO (\\S+).*\\s+\\((.*?)\\).*\\s+VALUES.*\\s+\\((.*?)\\).*\\;";
    boolean isEqual =false;
    boolean isSizeEqual =true;
    String tableName, tableColumns, tableValues;
    List<String> columnRecord = new ArrayList<>();
    List<String> valuesRecord = new ArrayList<>();


    @Override
    public Boolean checkSyntax(String statement) {
        Pattern re = Pattern.compile(insertRegex);
        Matcher matcher = re.matcher(statement);
        while (matcher.find()) {
            tableName = matcher.group(1);
            tableColumns = matcher.group(2).trim();
            columnRecord = Arrays.asList(tableColumns.split(","));
            tableValues = matcher.group(3).trim();
            valuesRecord = Arrays.asList(tableValues.split(","));

            if (columnRecord.size() != valuesRecord.size()) {
                isSizeEqual = false;
            }
        }
        isEqual = matcher.matches();

        if(isEqual && isSizeEqual){
            return true;
        }else{
            return false;
        }
    }

    @Override
    public Map<String, List<String>> getMetadata() {
        Map<String, List<String>> metadata = new HashMap<>();

        List<String> tableNames = Arrays.asList(tableName.split("\\."));
        List<String> tempTableNames = new ArrayList<>();
        tempTableNames.add(tableNames.get(2));
        metadata.put("table", tempTableNames);
        List<String> tempDB = new ArrayList<>();
        tempDB.add(tableNames.get(1));
        metadata.put("database", tempDB);
        List<String> pathList = new ArrayList<>();
        pathList.add(tableNames.get(0));
        metadata.put("path", pathList);


        metadata.put("columns", columnRecord);
        metadata.put("tableValues", valuesRecord);
        return metadata;
    }

}
