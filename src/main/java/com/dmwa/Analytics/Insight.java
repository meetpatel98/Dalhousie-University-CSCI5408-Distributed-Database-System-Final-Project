package com.dmwa.Analytics;

import java.io.*;
import java.util.HashMap;
import java.util.Map;

//this class will print the insight for the query
public class Insight {

    private String ROOT_FOLDER = "src/main/java/com/dmwa/AnalyticFile/Analytic.txt";




    public void getQueryCount(String username) throws IOException {

        //read the file
        File file = new File(ROOT_FOLDER);
        //Read the file and search for the username
        FileReader fr = new FileReader(file);
        BufferedReader br = new BufferedReader(fr);
        String line="";
        String databasename = "";
        int queryCount=0;
        Map<String, Integer> db_with_queryCount = new HashMap<>();

        while((line = br.readLine())!=null){
            //read the line and get th query count
            String[] recordlines = line.split(",");

            //check if username matches or not
            if(recordlines[0].split(":")[1].trim().equals(username)) {
                databasename = recordlines[1].split(":")[1].trim();
                queryCount = Integer.parseInt(recordlines[2].split(":")[1].trim());
                db_with_queryCount.containsKey(databasename);
                db_with_queryCount.put(databasename, queryCount);
            }
        }
        for(String key : db_with_queryCount.keySet()) {
            System.out.println("user " + username + " submitted " + db_with_queryCount.get(key) + " queries for " + key + " running on Virtual Machine 1");
        }
    }

    //print the update count for the user database
    public void getUpdateCount(String username, String databasename) throws IOException {
        //read the file
        File file = new File(ROOT_FOLDER);
        //Read the file and search for the username
        FileReader fr = new FileReader(file);
        BufferedReader br = new BufferedReader(fr);
        String line="";

        int updateCount=0;
        Map<String, Integer> db_with_updateCount = new HashMap<>();

        while((line = br.readLine())!=null){
            //read the line and get th query count
            String[] recordlines = line.split(",");

            //check if username matches or not
            if(recordlines[0].split(":")[1].trim().equals(username)) {
                databasename = recordlines[1].split(":")[1].trim();
                updateCount = Integer.parseInt(recordlines[3].split(":")[1].trim());
                db_with_updateCount.put(databasename, updateCount);
            }
        }
        for(String key : db_with_updateCount.keySet()) {
            if(key.equals(databasename)) {
                System.out.println("user " + username + " performed " + db_with_updateCount.get(key) + " update on " + key );

            }
        }
    }
}
