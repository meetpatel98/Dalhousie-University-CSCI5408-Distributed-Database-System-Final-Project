package com.dmwa;

import com.dmwa.Analytics.DBAnalytic;
import com.dmwa.Analytics.Insight;
import com.dmwa.Dump.GenerateDumpforUser;
import com.dmwa.ERD.GenerateERD;
import com.dmwa.GCPConfiguration.GCPConnection;
import com.dmwa.Logs.QueryLogImpl;
import com.dmwa.Logs.model.ModelQueryLogs;
import com.dmwa.Logs.utils.UserSessionUtils;
import com.dmwa.SQLCommands.*;
import com.dmwa.UserAuthentication.UserLogin;
import com.dmwa.UserAuthentication.UserRegistration;

import java.time.Instant;
import java.util.*;

public final class Main {

    public static void UserMenu() {
        System.out.println("Select an option:");
        Scanner sc = new Scanner(System.in);
        System.out.println("1.REGISTER");
        System.out.println("2. LOGIN");
        System.out.println("3. LOGOUT");

        String s = sc.nextLine();
        switch (s) {
            case "1":
                UserRegistration.register(sc);
                UserMenu();
                break;
            case "2":
                UserLogin.Login(sc);
                UserMenu();
                break;
            case "3":
                System.exit(0);

            default:
                break;
        }
    }

    public static void AfterLoginMenu(String username){

        boolean flag = true;
        System.out.println("===========Perform any sql operations===================");
        System.out.println("WRITE QUERIES");
        System.out.println("   : Create Database- CREATE DATABASE <database name>;");
        System.out.println("   : Create Table- CREATE TABLE <VM Name>.<DB Name>.<Table Name> (<Column Name> <Data Type> *<PRIMARY KEY/FOREIGN Key REFERENCES TableName(ColumnName)>*);");
        System.out.println("   : Insert Data- INSERT INTO <VM Name>.<DB Name>.<Table Name> (<Column Name separated by ','>) VALUES(<Values to be inserted>);");
        System.out.println("   : Update Table- UPDATE <VM Name>.<DB Name>.<Table Name> SET <column to be updated><operator>'><New Value> WHERE <Column Name with condition><operator><Column Value with condition>");
        System.out.println("   : Select Data- SELECT * FROM <VM Name>.<DB Name>.<Table Name> WHERE <Column Name with condition><operator><Column Value with condition>;");
        System.out.println("   : Delete Data- DELETE FROM <VM Name>.<DB Name>.<Table Name> WHERE <Column Name with condition><operator><Column Value with condition>;");
        System.out.println("   : Drop Table- DROP TABLE <VM Name>.<DB Name>.<Table Name>;");
        System.out.println("EXPORT SQL DUMP");
        System.out.println("   : DUMP");
        System.out.println("Generate ERD");
        System.out.println("   : ERD");
        System.out.println("Generate Analytics");
        System.out.println("   : COUNT QUERIES;");
        System.out.println("   : COUNT UPDATE <Database Name>;");
        System.out.println("SHOW LOGS");
        System.out.println("========================================================");
        while (flag) {
            System.out.println("Enter Input! Enter exit to exit!");
            String input = "";
            Scanner scanner = new Scanner(System.in);
            try {
                while (scanner.hasNext()) {
                    input = scanner.nextLine();
                    if (input.equalsIgnoreCase("exit")) {
                        flag = false;
                        System.exit(0);
                        //break;
                    }
                    queryExecution(input);
                    System.out.println("Enter Input! Enter exit to exit!");
                }

            } catch (Exception ex) {
                System.out.println(ex.getMessage());
            } finally {
                GCPConnection.closeSession();
                scanner.close();
            }
        }


    }
    public static void main(String[] args) {
        UserMenu();
    }

    public static void queryExecution(String query) throws Exception {

        ModelQueryLogs modelQueryLogs = ModelQueryLogs.getModelQueryLogsinstances();
        modelQueryLogs.setQueryEntered(query);
        QueryLogImpl queryLog =QueryLogImpl.getQueryLogsImplInstance();
        queryLog.generateQueryLogEntry(modelQueryLogs);

        QueryExecutionTime queryExecutionTime = new QueryExecutionTime();
        queryExecutionTime.setT1(Instant.now().toEpochMilli());

        QueryParser queryParser = new QueryParser();
        if (queryParser.getQueryDetails(query)) {
            Map<String, List<String>> metadata = new HashMap<>();
            metadata = getStringListMap(queryParser, metadata);
            switch (queryParser.type) {
                case USE:
                    UseDatabase usedb = new UseDatabase();
                    usedb.useDatabase(metadata.get("database").get(0));
                    UserSessionUtils.setDatabaseName(metadata.get("database").get(0));
                    DBAnalytic analyticsUse=new DBAnalytic();
                    analyticsUse.analyzeUserOperation(UserSessionUtils.getUsername(),metadata.get("database").get(0),false);
                    break;
                case CREATEDB:
                    CreateDatabase database = new CreateDatabase();
                    database.createDatabase(metadata.get("database").get(0));
                    //calling analytics
                    DBAnalytic analyticsDB=new DBAnalytic();
                    analyticsDB.analyzeUserOperation(UserSessionUtils.getUsername(),metadata.get("database").get(0),false);
                    break;
                case CREATE:
                    CreateTable createTable = new CreateTable();

                    if (metadata.containsKey("foreign_key")) {
                        createTable.createTable(metadata.get("table").get(0), metadata.get("database").get(0),
                                metadata.get("path").get(0), metadata.get("column_name"),
                                metadata.get("column_type"), metadata.get("foreign_key"));
                    } else {
                        List<String> blank_list = new ArrayList<>();
                        createTable.createTable(metadata.get("table").get(0), metadata.get("database").get(0),
                                metadata.get("path").get(0), metadata.get("column_name"),
                                metadata.get("column_type"), blank_list);
                    }
                    //calling analytics
                    DBAnalytic analytics=new DBAnalytic();
                    analytics.analyzeUserOperation(UserSessionUtils.getUsername(),metadata.get("database").get(0),false);
                    break;
                case INSERT:
                    InsertData writeTable = new InsertData();
                    writeTable.InsertData(metadata.get("table").get(0), metadata.get("database").get(0), metadata.get("path").get(0), metadata.get("tableValues"));

                    //calling analytics
                    DBAnalytic analyticsInsert=new DBAnalytic();
                    analyticsInsert.analyzeUserOperation("username",metadata.get("database").get(0),false);
                    break;
                case DELETE:
                    DeleteData deleteOperation = new DeleteData();
                    if (metadata.containsKey("values")) {
                        deleteOperation.deleteData(metadata.get("table").get(0), metadata.get("database").get(0), metadata.get("path").get(0), metadata.get("values"));
                    } else {
                        List<String> emptySet = new ArrayList<>();
                        deleteOperation.deleteData(metadata.get("table").get(0), metadata.get("database").get(0), metadata.get("path").get(0), emptySet);
                    }
                    DBAnalytic analyticsDel=new DBAnalytic();
                    analyticsDel.analyzeUserOperation("username",metadata.get("database").get(0),false);
                    break;
                case DROP:
                    DropTable drop = new DropTable();
                    drop.dropTable(metadata.get("table").get(0), metadata.get("database").get(0), metadata.get("path").get(0));
                    DBAnalytic analyticsDrop=new DBAnalytic();
                    analyticsDrop.analyzeUserOperation("username",metadata.get("database").get(0),false);
                    break;
                case UPDATE:
                    UpdateTable updateOperation = new UpdateTable();
                    if (metadata.containsKey("constraint")) {
                        updateOperation.updateOperation(metadata.get("table").get(0), metadata.get("database").get(0), metadata.get("path").get(0), metadata.get("columns"), metadata.get("values"), metadata.get("constraint"));
                    } else {
                        List<String> emptySet = new ArrayList<>();
                        updateOperation.updateOperation(metadata.get("table").get(0), metadata.get("database").get(0), metadata.get("path").get(0), metadata.get("columns"), metadata.get("values"), emptySet);
                    }
                    DBAnalytic analyticsUpdate=new DBAnalytic();
                    analyticsUpdate.analyzeUserOperation("username",metadata.get("database").get(0),true);
                    break;
                case ERD:
                    System.out.println("ERD");
                    System.out.println("Select the database you want to generate ERD of: ");
                    Scanner sc = new Scanner(System.in);
                    String dbName = sc.nextLine();
                    if(dbName != null){
                        GenerateERD erd = new GenerateERD();
                        erd.generateERD(dbName);
                        GCPWriter gw=new GCPWriter();
        gw.writeFile("src/main/java/com/dmwa/ERDDiagram/ERD_employee_database.txt","/home/"+ GCPConfig.VM2_User +"/Project/ csci-5408-w2022-project-dpg15/src/main/java/com/dmwa/ERDDiagram/ERD_employee_database.txt");

                    }else{
                        System.out.println("Please select valid database");
                    }
                    break;
                case DUMP:
                    System.out.println("DUMP");
                    System.out.println("Select the database you want to generate DUMP of: ");
                    Scanner s = new Scanner(System.in);
                    String databaseName = s.nextLine();

                    if(databaseName != null){
                        if(databaseName.contains(".")){
                            //call the method that will generate the SQL Dump
                            GenerateDumpforUser dump = new GenerateDumpforUser();
                            dump.generateSQLDump(databaseName);
                        } else{
                            System.out.println("Please Provide Database and Instance in a proper Format!!");
                        }
                    } else{
                        System.out.println("Please Select Proper Database!!");
                    }
                    break;
                case SELECT:
                    SelectData selectOperation = new SelectData();
                    if (metadata.containsKey("constraint")) {
                        selectOperation.selectData(metadata.get("table").get(0), metadata.get("database").get(0), metadata.get("path").get(0), metadata.get("columns"), metadata.get("constraint"));
                    } else {
                        List<String> emptySet = new ArrayList<>();
                        selectOperation.selectData(metadata.get("table").get(0), metadata.get("database").get(0), metadata.get("path").get(0), metadata.get("columns"), emptySet);
                    }
                    break;
                case COUNTQUERY:
                    System.out.println("ANALYTIC");
                    Insight insight = new Insight();
                    String username = UserSessionUtils.getUsername();
                    insight.getQueryCount(username);
                    break;

                case COUNT:
                    System.out.println("ANALYTIC");
                    String user = UserSessionUtils.getUsername();
                    Insight insights = new Insight();
                    insights.getUpdateCount(user,metadata.get("database").get(0) );
                    break;
            }
        }
    }

    private static Map<String, List<String>> getStringListMap(QueryParser queryParser,
            Map<String, List<String>> metadata) {
        if (!queryParser.type.toString().equals("ERD") && !queryParser.type.toString().equals("DUMP")
                && !queryParser.type.toString().equals("TRANSACTION") && !queryParser.type.toString().contains("COUNTQUERY")) {
            metadata = queryParser.get_metadata();
        }
        return metadata;
    }
}

