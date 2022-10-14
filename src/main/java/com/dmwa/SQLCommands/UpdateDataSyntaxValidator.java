package com.dmwa.SQLCommands;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class UpdateDataSyntaxValidator implements IQueryParser {

    String updateDataRegex = "UPDATE\\s+(\\S+)\\s*SET\\s+(.*?)\\s*(WHERE\\s+(.*?))?;";
    String tableName, value, constraint = "";


    @Override
    public Boolean checkSyntax(String statement) {
        Pattern pattern = Pattern.compile(updateDataRegex);
        Matcher matcher = pattern.matcher(statement);
        while (matcher.find()) {
            tableName = matcher.group(1);
            value = matcher.group(2);
            if(matcher.group(3)!=null) {
                constraint = matcher.group(4);
            }
        }
        return matcher.matches();
    }

    @Override
    public Map<String, List<String>> getMetadata() {
        Map<String, List<String>> metadata = new HashMap<>();

        List<String> tableNames = Arrays.asList(tableName.split("\\."));
        List<String> colSet = new ArrayList<>();
        List<String> valueSet = new ArrayList<>();
        List<String> tempTableName = new ArrayList<>();
        List<String> tempDB = new ArrayList<>();
        List<String> paths = new ArrayList<>();
        tempTableName.add(tableNames.get(2));
        metadata.put("table", tempTableName);
        tempDB.add(tableNames.get(1));
        metadata.put("database", tempDB);
        paths.add(tableNames.get(0));
        metadata.put("path", paths);

        List<String> tempData = Arrays.asList(value.split(","));
        int i=0;
        for(String data : tempData){
            List<String> separator = Arrays.asList(data.split("="));
            colSet.add(separator.get(0).trim());
            valueSet.add(separator.get(1).trim());
            i++;
        }

        metadata.put("columns", colSet);
        metadata.put("values", valueSet);

        if(!constraint.equals("")) {
            List<String> constraintSet = new ArrayList<>();
            constraintSet.add(constraint);
            metadata.put("constraint", constraintSet);
        }
        return metadata;
    }

}
