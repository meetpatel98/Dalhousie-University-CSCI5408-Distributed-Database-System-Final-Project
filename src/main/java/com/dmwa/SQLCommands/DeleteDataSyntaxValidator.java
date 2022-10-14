package com.dmwa.SQLCommands;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class DeleteDataSyntaxValidator implements IQueryParser {

    String deleteRegex = "DELETE FROM\\s+(\\S+)\\s*(WHERE\\s(.*?)\\s*)?;";
    List<String> valuesData = new ArrayList<>();
    String nameOfTable, values = "";


    @Override
    public Boolean checkSyntax(String statement) {
        Pattern pattern = Pattern.compile(deleteRegex);
        Matcher matcher = pattern.matcher(statement);
        while (matcher.find()) {
            nameOfTable = matcher.group(1);
            if(matcher.group(2)!=null) {
                values = matcher.group(3);
            }

        }
        return matcher.matches();
    }

    @Override
    public Map<String, List<String>> getMetadata() {
        Map<String, List<String>> metadata = new HashMap<>();

        List<String> tableNames = Arrays.asList(nameOfTable.split("\\."));
        List<String> tempTableNames = new ArrayList<>();
        List<String> tempDB = new ArrayList<>();
        List<String> pathList = new ArrayList<>();
        tempTableNames.add(tableNames.get(2));
        metadata.put("table", tempTableNames);
        tempDB.add(tableNames.get(1));
        metadata.put("database", tempDB);
        pathList.add(tableNames.get(0));
        metadata.put("path", pathList);

        if(!values.equals("")) {
            valuesData.add(values);
            metadata.put("values", valuesData);
        }
        return metadata;
    }

}
