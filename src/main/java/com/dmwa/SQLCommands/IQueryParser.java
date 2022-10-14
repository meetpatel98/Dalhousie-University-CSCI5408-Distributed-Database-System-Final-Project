package com.dmwa.SQLCommands;

import java.util.List;
import java.util.Map;

public interface IQueryParser {
    public Boolean checkSyntax(String statement);

    Map<String, List<String>> getMetadata();

}
