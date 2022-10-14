package com.dmwa.SQLCommands;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SelectDataSyntaxValidator implements IQueryParser {

    String selectQueryRegex = "SELECT\\s+(.+?)\\s*\\s+FROM\\s+(.*?)\\s*(WHERE\\s+(.*?)\\s*)?;";
    String columns, tableName, constraint = "";


    @Override
    public Boolean checkSyntax(String statement) {
        Pattern pattern = Pattern.compile(selectQueryRegex);
        Matcher matcher = pattern.matcher(statement);
        while (matcher.find()) {
            columns = matcher.group(1).trim();
            tableName = matcher.group(2).trim();
            if (matcher.group(3) != null) {
                constraint = matcher.group(4);
            }
        }
        return matcher.matches();
    }

    @Override
    public Map<String, List<String>> getMetadata() {
        Map<String, List<String>> metadata = new HashMap<>();

        List<String> tableNames = Arrays.asList(tableName.split("\\."));
        List<String> tempTableNames = new ArrayList<>();
        List<String> tempDB = new ArrayList<>();
        List<String> paths = new ArrayList<>();
        tempTableNames.add(tableNames.get(2));
        metadata.put("table", tempTableNames);
        tempDB.add(tableNames.get(1));
        metadata.put("database", tempDB);
        paths.add(tableNames.get(0));
        metadata.put("path", paths);
        List<String> columnSet = Arrays.asList(columns.split(","));
        metadata.put("columns", columnSet);

        if (!constraint.equals("")) {
            List<String> constraintColSet = new ArrayList<>();
            constraintColSet.add(constraint);
            metadata.put("constraint", constraintColSet);
        }
        return metadata;
    }

}
