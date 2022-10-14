package com.dmwa.Analytics;

import com.dmwa.GCPConfiguration.GCPConfig;
import com.dmwa.GCPConfiguration.GCPWriter;
import com.jcraft.jsch.SftpException;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

/**This class will analyze the opration peerformed in database*
 * @author Khushboo Patel
 * Version 01
 */

public class DBAnalytic {

    private String ROOT_FOLDER = "src/main/java/com/dmwa/AnalyticFile/";
    private String analyticFile = "Analytic.txt";


    public void createFile() throws IOException {
        //create a file if not exist
        File myfileObj = new File(ROOT_FOLDER+analyticFile);
        System.out.println(ROOT_FOLDER+analyticFile);
        if(myfileObj.exists()){
            System.out.println("Analytic file created"+analyticFile);
        }else{
            myfileObj.createNewFile();
        }
    }

    public void analyzeUserOperation(String username, String database, boolean isUpdate) throws IOException, SftpException {

        //create analytic file first
        createFile();

        File file = new File(ROOT_FOLDER+analyticFile);
        //Read the file and search for the username
        FileReader fr = new FileReader(file);
        BufferedReader br = new BufferedReader(fr);

        //crete file writer object
        FileWriter fw = new FileWriter(file, true);
        BufferedWriter bw = new BufferedWriter(fw);

        String line;
        //write into the file
        String userName = username;
        int updatecount = 0;
        int querycount = 0;
        boolean userExist=false;
        //if file is empty the insert the line
        if(file.length() == 0){
            if(isUpdate==true){
                //update the count by 1
                updatecount = updatecount+1;
            }
            //increment the query count
            querycount=querycount+1;

            //write into the Analytic file
            bw.append("Username: "+username+", Database: " +database+", Query Count: "+querycount+ ", Update Count: "+updatecount+"\n");

        }else{
              int linesize = 0;
              while((line = br.readLine())!=null) {
                  linesize = linesize+1;
                  String storeline = line;
                  String[] lineRead = line.split(",");
                  String uname = lineRead[0].split(":")[1].trim();

                  //if the username already exist then read the line
                  if (uname.equals(username)) {

                      //enter the username
                      Path path = Paths.get(ROOT_FOLDER+analyticFile);
                      List<String> lines = Files.readAllLines(path, StandardCharsets.UTF_8);

                      int position = linesize;

                      //set user exist to true
                      userExist = true;

                      //check if it is update operation
                      if (isUpdate) {
                          updatecount = Integer.parseInt(lineRead[3].split(":")[1].trim()) + 1;
                          System.out.println("Update count: " + updatecount);
                      }

                      //increment the count of the query
                      querycount = Integer.parseInt(lineRead[2].split(":")[1].trim()) + 1;
                      String updatedLine = "Username: " + username + ", Database: " + database + ", Query Count: " + querycount + ", Update Count: " + updatecount;

                      //update the line in the file
                      lines.add(position, updatedLine);
                      Files.write(path, lines, StandardCharsets.UTF_8);
                      break;

                  }
              }

              //if user does not exist
              if(!userExist){

                  if(isUpdate==true){
                      //update the count by 1
                      updatecount = updatecount+1;
                  }
                  //increment the query count
                  querycount=querycount+1;

                  //write into the Analytic file
                  bw.append("Username: "+username+", Database: " +database+", Query Count: "+querycount+ ", Update Count: "+updatecount+"\n");

              }
        }
        bw.close();
        fw.close();
        br.close();
        fr.close();
        GCPWriter gw=new GCPWriter();
        gw.writeFile("src/main/java/com/dmwa/AnalyticFile/Analytic.txt","/home/"+ GCPConfig.VM2_User +"/Project/csci-5408-w2022-project-dpg15/src/main/java/com/dmwa/AnalyticFile/Analytic.txt");

    }

}
