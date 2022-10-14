package com.dmwa.Dump;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

/**@author Khushboo Patel
 * This class will generate the Dump for the sql.
 * Version: 01
 * */
public class GenerateDumpforUser implements GenerateDump{

    private static String ROOT_PATH = "src/main/java/com/dmwa/";

    private String databaseName;
    private String SQLDumpFile=null;

    public void GenerateDumpforUser(String databaseName){
        this.databaseName = databaseName;
    }

    @Override
    //read the file where table is stored
    public void generateSQLDump(String databaseName) throws IOException {

        String instance = databaseName.split("\\.")[0].trim();
        //Read the database file from the computer
        //Reading the directory
        File directoryPath = new File(ROOT_PATH+"Database");

        //List of all files and directories
        File databaseList[] = directoryPath.listFiles();
        System.out.println(databaseList);
        int storeLineNumber = 0;
        int lineNumber = 0;

        //Initialize array list
        List<String> tableName = new ArrayList<>();
        String filePath="";
        String dbName = databaseName.split("\\.")[1].trim();
        for (File database : databaseList) {

//            if(!database.getName().contains(databaseName)){
//                System.out.println("Database for the user does not exists!");
//             }

            //if the file contains the database then read the file
            if(database.getName().equals(dbName+".txt")){
                database.getAbsolutePath();

                //Create file for ERD
                File myObj = new File(ROOT_PATH+ "DumpFile/Dump_"+database.getName().replace(".txt","")+".sql");

                if(myObj.exists()){
                    SQLDumpFile = myObj.getName();
                    System.out.println("Dump file created"+SQLDumpFile);
                }else{
                    myObj.createNewFile();
                }
                //read the database file and retrirve the table name from the file
                BufferedReader br = new BufferedReader(new FileReader(database));
                String line;

                while ((line = br.readLine()) != null) {

                    lineNumber = ++lineNumber;
                    //if the line contain table annotation then read its next line that contain all the table name
                    if (line.equals("@table")) {
                        //read the table name
                        storeLineNumber = lineNumber;
                    }

                    //storing all the value having
                    if((storeLineNumber>0) && (storeLineNumber < lineNumber)){
                        String tables = line;
                        tableName.add(tables);
                    }
                }
            }
        }
        System.out.println(tableName);
        exportSQLDump(tableName, instance);
    }

    private void exportSQLDump(List<String> tableName, String instance) throws IOException {

        String fileName;
        String[] columnName = null;
        String[] metaName = null;

        //Reading the directory
        File directoryPath = new File(ROOT_PATH + "/Tables/"+instance);

        //List of all files and directories
        File filesList[] = directoryPath.listFiles();

        //create arraylist to insert the tableValue
        List<String> tableData = new ArrayList<>();

        //construct ERD
        for(File files : filesList){

            int storeLine = 0;
            int lineNumber = 0;

            fileName = files.getName().replaceAll(".txt", "");

            if(tableName.contains(fileName)){

                //read the file and construct the table
                BufferedReader br = new BufferedReader(new FileReader(files));
                String line="";
                while((line = br.readLine())!=null){

                    lineNumber = ++lineNumber;
                    //reading the database names
                    if(line.equalsIgnoreCase("@database")){
                        String databasename = br.readLine();
                        createDatabase(databasename);
                    }
                    //reading the table  names
                    if(line.equalsIgnoreCase("@table")){
                        String table = br.readLine();
                    }

                    //reading the column names
                    if(line.equalsIgnoreCase("@column")){
                        String newline = br.readLine();
                        columnName = newline.split("~");
                    }

                    //getting the meta data of the column
                    if(line.equalsIgnoreCase("@meta")){
                        String newline = br.readLine();
                        metaName = newline.split("~");
                    }

                    if(line.equalsIgnoreCase("@value")){
                        storeLine = lineNumber;
                    }
                    if(storeLine!=0 && (lineNumber>storeLine)){
                        String tablevalue="";
                        line  = line.replace("~", ",");
                        String[] value = line.split(",");
                        tablevalue=tablevalue+"(";
                        for(int i=0;i< value.length;i++){
                            tablevalue = tablevalue+"'"+value[i]+"',";
                        }
                        tablevalue = tablevalue.substring(0, tablevalue.length() - 1);
                        tablevalue=tablevalue+")";
                        tableData.add(tablevalue);
                    }
                }
                System.out.println(tableData);
                //create the table in the File
                createTable(fileName, columnName, metaName);
                //create insert query
                createInsertQuery(fileName, columnName, tableData);
            }

        }
    }

    //this method will create the insert Query in file
    private void createInsertQuery(String tableName, String[] columnName,  List<String> tableData) throws IOException {

        if(tableData != null){
            FileWriter fw = new FileWriter(new File(ROOT_PATH+"DumpFile/"+SQLDumpFile), true);
            BufferedWriter bw = new BufferedWriter(fw);
            StringBuilder bs = new StringBuilder();
            String tableRecords = "";

            bs.append("INSERT INTO '"+tableName+"'(");
            for(int i=0;i<columnName.length; i++){
                bs.append(columnName[i]+",");
            }
            bs.deleteCharAt(bs.length()-1);
            bs.append(")\n");
            bs.append("VALUES(\n");
            System.out.println("table data"+tableData);
            System.out.println(tableData.size());
            for(int i = 0;i<tableData.size();i++){
                tableRecords+=tableData.get(i)+",";
            }
            bs.append(tableRecords);
            bs.deleteCharAt(bs.length()-1);
            bs.append("\n");
            bs.append(");");
            bs.append("\n\n");
            //append the bs in the file
            bw.append(bs.toString());

            bw.close();
            fw.close();
        }
    }

    private String createInsertValue(String[] tabledatarecords) {

        String tableRecords = "";
        for (int k = 0; k < tabledatarecords.length; k++) {
            tableRecords += tableRecords + " '" + tabledatarecords[k] + "' , ";
        }
        return tableRecords;
    }

    //this method will write a create table query in the dump file
    private void createTable(String table, String[] columnName, String[] metaName) throws IOException {

        FileWriter fw = new FileWriter(new File(ROOT_PATH+"DumpFile/"+SQLDumpFile), true);
        BufferedWriter bw = new BufferedWriter(fw);
        StringBuilder bs = new StringBuilder();
        if(table != null && table != ""){

            String columnVal = "";
            String metaVal = "";
            bs.append("CREATE TABLE '"+table+"'(");
            for(int i=0;i<columnName.length ; i++) {
                if(columnName[i].contains("$") && metaName[i].contains("$")) {
                    columnVal = columnName[i].replace("$", "");
                    metaVal = metaName[i].replace("$", "");
                }
                else if(columnName[i].contains("@") && metaName[i].contains("@")){
                    columnVal = columnName[i].replace("@", "");
                    metaVal = metaName[i].replace("@", "");
                }
                else{
                    columnVal = columnName[i];
                    metaVal = metaName[i];
                }
                bs.append(columnVal+ " " + metaVal + ",");
            }
            bs.deleteCharAt(bs.length() - 1);
            bs.append(" );");
            bs.append("\n\n");
            bw.append(bs.toString());

        }
        bw.close();
        fw.close();
    }

    //This method will create the database query in the dump file
    private void createDatabase(String databasename) throws IOException {
        FileWriter fw = new FileWriter(new File(ROOT_PATH+"DumpFile/"+SQLDumpFile), true);
        BufferedWriter bw = new BufferedWriter(fw);
        StringBuilder bs = new StringBuilder();

        //Drop the database if exist
        bw.append("DROP DATABASE IF EXISTS "+databasename+";\n");
        if(databasename != null && databasename != ""){
            bs.append("CREATE DATABASE "+databasename+";");
            bw.append(bs.toString());
            bw.append("\n\n");
        }
        bw.close();
        fw.close();
    }
}
