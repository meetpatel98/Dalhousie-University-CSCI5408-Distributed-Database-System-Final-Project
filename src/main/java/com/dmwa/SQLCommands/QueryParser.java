package com.dmwa.SQLCommands;

import java.util.List;
import java.util.Map;

public class QueryParser {

    IQueryParser IQueryParser = null;
    String[] splitQuery;
    String inputQuery;
    public queryTypes type;
    public String dumpText = "";

    // Storing all the possible operations
    public enum queryTypes {
        SELECT,
        UPDATE,
        DELETE,
        DROP,
        INSERT,
        CREATE,
        ERD,
        DUMP,
        CREATEDB,
        TRANSACTION,
        USE,
        COUNTQUERY,
        COUNT
    }

    // Fetching query details
    public boolean getQueryDetails(String inputQuery) {
        this.inputQuery = inputQuery;
        if (inputQuery.equals("ERD")) {
            type = queryTypes.ERD;
            IQueryParser = null;
            return true;
        } else if (inputQuery.contains("DUMP")) {
            type = queryTypes.DUMP;
            dumpText = inputQuery;
            IQueryParser = null;
            return true;
        } else if (inputQuery.contains("TRANSACTION")) {
            type = queryTypes.TRANSACTION;
            dumpText = inputQuery;
            IQueryParser = null;
            return true;
        } else if(inputQuery.contains("COUNT QUERIES")){
            type = queryTypes.COUNTQUERY;
            dumpText = inputQuery;
            IQueryParser = null;
            return true;
        }
        if (checkQueryType(inputQuery)) {
            type = getQueryType(inputQuery);
            return syntaxCheck(type);
        } else {
            System.out.println("Please enter correct IQueryParser!");
            return false;
        }
    }

    // Calling class for syntax validator
    private boolean syntaxCheck(queryTypes type) {
        switch (type) {
            case USE:
                IQueryParser = new UseDBSyntaxValidator();
                break;
            case CREATEDB:
                IQueryParser = new CreateDBSyntaxValidator();
                break;
            case CREATE:
                IQueryParser = new CreateTableSyntaxValidator();
                break;
            case INSERT:
                IQueryParser = new InsertDataSyntaxValidator();
                break;
            case DELETE:
                IQueryParser = new DeleteDataSyntaxValidator();
                break;
            case DROP:
                IQueryParser = new DropTableSyntaxValidator();
                break;
             case SELECT:
                 IQueryParser = new SelectDataSyntaxValidator();
                 break;
             case UPDATE:
                 IQueryParser = new UpdateDataSyntaxValidator();
                 break;
            case COUNT:
                IQueryParser = new CreateCountUpdateSyntaxValidator();
                break;
        }
        if (IQueryParser.checkSyntax(inputQuery)) {
            System.out.println("Syntax is correct!");
            return true;
        } else {
            System.out.println("Syntax is not correct!");
            return false;
        }
    }

    public Map<String, List<String>> get_metadata() {
        return IQueryParser.getMetadata();
    }

    // Checking the type of query
    private boolean checkQueryType(String input) {
        int splited_word_counter = 0;
        splitQuery = input.trim().split("\\s+");
        for (queryTypes _queryTypes : queryTypes.values()) {
            if (splitQuery[splited_word_counter].equals(_queryTypes.toString())) {
                return true;
            }
        }
        return false;
    }

    // Fetching the type of query
    public queryTypes getQueryType(String keyword) {
        int splited_word_counter = 0;
        for (queryTypes _queryTypes : queryTypes.values()) {
            if (splitQuery[splited_word_counter].equals(_queryTypes.toString())) {
                if (_queryTypes.toString().equals("CREATE") && splitQuery[1].equals("DATABASE")) {
                    return queryTypes.CREATEDB;
                }
                return _queryTypes;
            }
        }
        return null;
    }

}
