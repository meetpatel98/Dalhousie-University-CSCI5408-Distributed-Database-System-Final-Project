package com.dmwa.ERD;
/**author name: Khushboo Patel
 * Developed since: 01-04-2022
 * Version: 1.0
 * Description: This class will generate the ER Diagram of the given table
 * */

import java.io.*;
import java.util.ArrayList;
import java.util.Formatter;
import java.util.List;

public class GenerateERD {

    private static String ROOT_PATH = "src/main/java/com/dmwa/";
    private String ERDfileName = "";
    //Reads the file
    //Creating a File object for directory
    public void generateERD(String databaseName ) throws IOException {
        String instance=databaseName.split("\\.")[0].trim();
        //Reading the directory
        File directoryPath = new File(ROOT_PATH+"Database");

        //List of all files and directories
        File filesList[] = directoryPath.listFiles();
        int storeLineNumber = 0;
        int lineNumber = 0;

        //Initialize array list
        List<String> tableName = new ArrayList<>();
        String filePath="";

        for (File file : filesList) {

           // if(!file.getName().contains(databaseName.split("\\.")[1])){
             //   System.out.println("Database for the user does not exists!");
            //}

            //if the file contains the database then read the file
            if(file.getName().equals(databaseName.split("\\.")[1]+".txt")){
                file.getAbsolutePath();

                //Create file for ERD
                File myObj = new File(ROOT_PATH+ "ERDDiagram/ERD_"+file.getName());

                if(myObj.exists()){
                    ERDfileName = myObj.getName();
                }else{
                    myObj.createNewFile();
                }
                //read the database file and retrirve the table name from the file
                BufferedReader br = new BufferedReader(new FileReader(file));
                String line;


                while ((line = br.readLine()) != null) {

                    //if the line contain table annotation then read its next line that contain all the table name
                    if (line.equals("@table")) {
                        storeLineNumber = lineNumber;
                    }

                    //storing all the value having
                    if((storeLineNumber>0) && (storeLineNumber < lineNumber)){
                        String tables = line;
                        tableName.add(tables);
                    }
                    lineNumber = ++lineNumber;
                }
            }
        }
        constructERD(tableName,instance);
    }

    //this method will read the metadata
    private void constructERD(List<String> tableName,String instance) throws IOException {

        //Reading the directory
        File directoryPath = new File(ROOT_PATH+"/Tables/"+instance);

        //List of all files and directories
        File filesList[] = directoryPath.listFiles();
        String fileName="";
        String[] columnName = null;
        String[] metaName = null;
        //construct ERD
        for(File files : filesList) {

            if (!files.getName().equals(".DS_Store") && !files.getName().equals("README.md")) {


                fileName = files.getName().replaceAll(".txt", "");

                if (tableName.contains(fileName)) {
                    //read the file and construct the table
                    BufferedReader br = new BufferedReader(new FileReader(files));
                    String line = "";
                    while ((line = br.readLine()) != null) {

                        //reading the column names
                        if (line.equalsIgnoreCase("@column")) {
                            String newline = br.readLine();
                            columnName = newline.split("~");
                        }

                        //getting the meta data of the column
                        if (line.equalsIgnoreCase("@meta")) {
                            String newline = br.readLine();
                            metaName = newline.split("~");
                        }
                    }
                }
                //create the ERD in the File
                createERDFile(fileName, columnName, metaName);
            }
        }
    }

    //create a ERD diagram in a file
    private void createERDFile(String fileName, String[] columnName, String[] metaName) throws IOException {

        //read the forign key value from the foreign key file
        String foreignValue = readforignKey(fileName);
        String[] table_FK_Value=null;
        if(foreignValue!=null) {
            table_FK_Value = foreignValue.split(" ");
        }

        //FileWriter fw = new FileWriter( new File(ROOT_PATH+ "ERDDiagram/"+ERDfileName));
        Formatter  formatFile = new Formatter(new FileOutputStream(ROOT_PATH+ "ERDDiagram/"+ERDfileName));

        formatFile.format("----------------------------------------------------------------------------------------------------------------------------------------------------\n");
        formatFile.format("\t\t\t\t\t\t\t\t\t\t\t\t\t\tTable:"+fileName+ "\n");
        formatFile.format("----------------------------------------------------------------------------------------------------------------------------------------------------\n");
        formatFile.format("%20s%20s%20s%20s%20s%20s%20s\n","Column Name |"," Data Type |","Primary Key |","Foreign Key |","Foreign Column | ","Foreign Table | ","Cardinality");
        formatFile.format("----------------------------------------------------------------------------------------------------------------------------------------------------\n");

        System.out.println("----------------------------------------------------------------------------------------------------------------------------------------------------\n");
        System.out.println("Table:"+fileName);
        System.out.format("%20s%20s%20s%20s%20s%20s%20s\n", "Column Name |"," Data Type |","Primary Key |","Foreign Key |","Foreign Column | ","Foreign Table | ","Cardinality");
        System.out.println("----------------------------------------------------------------------------------------------------------------------------------------------------\n");

        for(int i=0;i<columnName.length;i++){

            if(columnName[i].contains("$")){
                formatFile.format(	"%20s%20s%20s%20s%20s%20s%20s\n", columnName[i]+"|", metaName[i].replaceAll("$", "")+"|", "PK |", " |" ," |" ,"|", "");
                System.out.format(	"%20s%20s%20s%20s%20s%20s%20s\n", columnName[i]+"|" ,metaName[i].replaceAll("$", "")+"|", "PK |" , " |" ,"|", "|", "");

            }
            else if(table_FK_Value!=null && columnName[i].equals(table_FK_Value[1])){
                formatFile.format("%20s%20s%20s%20s%20s%20s%20s\n", columnName[i]+"|",metaName[i].replaceAll("$", "")+"|", "|","FK |" , columnName[i] +"|", table_FK_Value[0] +"|", "1:M ");
                System.out.format("%20s%20s%20s%20s%20s%20s%20s\n", columnName[i]+"|",metaName[i].replaceAll("$", "")+"|", "|","FK |" , columnName[i] +"|", table_FK_Value[0] +"|", "1:M");
            }
            else{
                formatFile.format("%20s%20s%20s%20s%20s%20s%20s\n", columnName[i]+"|" ,metaName[i].replaceAll("$", "")+"|",  "|", "|" ,"|", "|" ,"" );
                System.out.format( "%20s%20s%20s%20s%20s%20s%20s\n", columnName[i]+"|" ,metaName[i].replaceAll("$", ""),"|",  "|", "|" ,"|", "|","" );
            }

        }
        formatFile.format("-------------------------------------------------------------------------------------------------------------------------------------------------------------------------\n");

        formatFile.close();
    }

    //this method will get the foreign key of the table
    private String readforignKey(String fileName) throws IOException {

        String fkValue = null;
        FileReader fw = new FileReader( new File(ROOT_PATH+ "GlobalData/ForeignKeyReferences.txt"));
        BufferedReader br = new BufferedReader(fw);
        String line  = "";

        while((line = br.readLine())!=null){
            String[] metaValue = line.split("\t");
            if(metaValue[0].equals(fileName)){
                fkValue = metaValue[1]+" "+metaValue[2];
            }
        }
        return fkValue;
    }
}
