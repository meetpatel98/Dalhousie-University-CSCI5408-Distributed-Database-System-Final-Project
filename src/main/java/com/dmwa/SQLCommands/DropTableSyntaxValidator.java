package com.dmwa.SQLCommands;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class DropTableSyntaxValidator implements IQueryParser {

    String dropTableRegex = "DROP TABLE\\s+(\\S+);";
    String tableName;


    @Override
    public Boolean checkSyntax(String statement) {
        Pattern pattern = Pattern.compile(dropTableRegex);
        Matcher matcher = pattern.matcher(statement);
        while (matcher.find()) {
            tableName = matcher.group(1);
        }
        return matcher.matches();
    }

    @Override
    public Map<String, List<String>> getMetadata() {
        Map<String, List<String>> metadata = new HashMap<>();

        List<String> tableNames = Arrays.asList(tableName.split("\\."));
        List<String> tempTableNames = new ArrayList<>();
        List<String> tempDBNames = new ArrayList<>();
        List<String> paths = new ArrayList<>();
        tempTableNames.add(tableNames.get(2));
        metadata.put("table", tempTableNames);
        tempDBNames.add(tableNames.get(1));
        metadata.put("database", tempDBNames);
        paths.add(tableNames.get(0));
        metadata.put("path", paths);

        return metadata;
    }

}
