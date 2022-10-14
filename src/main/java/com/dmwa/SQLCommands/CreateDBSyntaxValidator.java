package com.dmwa.SQLCommands;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CreateDBSyntaxValidator implements IQueryParser {
    String createDBRegex = "CREATE DATABASE (\\S+)\\s*\\;";
    String db;

    // Validating Syntax of Create DB Query
    @Override
    public Boolean checkSyntax(String statement) {
        Pattern re = Pattern.compile(createDBRegex);
        Matcher matcher = re.matcher(statement);
        while (matcher.find()) {
            db = matcher.group(1);
        }
        return matcher.matches();
    }

    // Fetching Metadata
    @Override
    public Map<String, List<String>> getMetadata() {
        Map<String, List<String>> metadata = new HashMap<>();

        List<String> db_list = new ArrayList<>();
        db_list.add(db);
        metadata.put("database", db_list);

        return metadata;
    }

}
