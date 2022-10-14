package com.dmwa.SQLCommands;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CreateTableSyntaxValidator implements IQueryParser {
    String createRegex = "CREATE TABLE (\\S+)\\s*\\((.*?)\\)\\;";
    String table_name, columns, only_table;

    // Validating Syntax for Create Table query
    @Override
    public Boolean checkSyntax(String statement) {
        Pattern re = Pattern.compile(createRegex);
        Matcher matcher = re.matcher(statement);
        while (matcher.find()) {
            table_name = matcher.group(1);
            columns = matcher.group(2);
        }
        return matcher.matches();
    }

    // Fetching the Metadata for creating tables
    @Override
    public Map<String, List<String>> getMetadata() {
        Map<String, List<String>> metadata = new HashMap<>();
        List<String> tableNames = Arrays.asList(table_name.split("\\."));
        if (tableNames.size() > 1) {
            List<String> tempTableNames = new ArrayList<>();
            tempTableNames.add(tableNames.get(2));
            only_table = tableNames.get(2);
            metadata.put("table", tempTableNames);
            List<String> tempDB = new ArrayList<>();
            tempDB.add(tableNames.get(1));
            metadata.put("database", tempDB);
            List<String> paths = new ArrayList<>();
            paths.add(tableNames.get(0));
            metadata.put("path", paths);

        } else {
            metadata.put("table", tableNames);
        }

        List<String> separator = Arrays.asList(columns.split(","));
        List<String> columnSet = new ArrayList<>();
        List<String> columnsTypeSet = new ArrayList<>();

        int i = 0;
        for (String columns : separator) {
            List<String> columns_parts = Arrays.asList(columns.trim().split(" "));

            // Checking if the table creation contains FOREIGN KEY Reference
            if (columns_parts.size() > 2) {
                if (columns.contains("FOREIGN")) {
                    List<String> foreignKey = new ArrayList<>();
                    foreignKey.add(only_table);
                    String foreignTable = columns_parts.get(5);
                    List<String> tableFK = Arrays.asList(foreignTable.split("\\("));
                    foreignKey.add(tableFK.get(0));
                    foreignKey.add(columns_parts.get(0));
                    metadata.put("foreign_key", foreignKey);

                    columnSet.add(columns_parts.get(0).trim());
                    columnsTypeSet.add(columns_parts.get(1).trim());
                } else {
                    columnSet.add(columns_parts.get(0).trim() + "$");
                    columnsTypeSet.add(columns_parts.get(1).trim() + "$");
                }
            } else {
                columnSet.add(columns_parts.get(0).trim());
                columnsTypeSet.add(columns_parts.get(1).trim());
            }
        }
        metadata.put("column_name", columnSet);
        metadata.put("column_type", columnsTypeSet);
        return metadata;
    }

}
