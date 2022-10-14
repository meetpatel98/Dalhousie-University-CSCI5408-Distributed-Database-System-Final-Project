package com.dmwa.SQLCommands;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CreateCountUpdateSyntaxValidator implements IQueryParser {
    String createRegex = "COUNT UPDATE (\\S+)\\s*\\;";
    String db;

    @Override
    public Boolean checkSyntax(String statement) {
        Pattern re = Pattern.compile(createRegex);
        Matcher matcher = re.matcher(statement);
        while (matcher.find()) {
            db = matcher.group(1);
        }
        return matcher.matches();
    }

    @Override
    public Map<String, List<String>> getMetadata() {

        Map<String, List<String>> metadata = new HashMap<>();
        List<String> db_list = new ArrayList<>();
        db_list.add(db);
        metadata.put("database", db_list);
        return metadata;
    }

}
