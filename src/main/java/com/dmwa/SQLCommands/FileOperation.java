package com.dmwa.SQLCommands;

import com.dmwa.Logs.EventLogImpl;
import com.dmwa.Logs.GeneralLogImpl;
import com.dmwa.Logs.constants.Constants;
import com.dmwa.Logs.model.ModelEventLogs;
import com.dmwa.Logs.model.ModelGeneralLogs;
import com.dmwa.Logs.utils.UserSessionUtils;

import java.io.*;
import java.util.HashMap;
import java.util.Map;

public class FileOperation {

    QueryExecutionTime queryExecutionTime = new QueryExecutionTime();
    long executionTime;

    public long getExecutionTime(){
        executionTime = queryExecutionTime.getT1() - queryExecutionTime.getT2();
        return executionTime;
    }

    public void reportToLog (String message, int x) {

        EventLogImpl eventLog = EventLogImpl.getEventLogsImplInstance();
        ModelEventLogs modelEventLogs = ModelEventLogs.getModelEventLogsInstances();
        modelEventLogs.setMessags(message);
        modelEventLogs.setEventType(Constants.CHANGE_IN_DB);

        GeneralLogImpl generalLog = GeneralLogImpl.getGeneralLogsImplInstance();
        ModelGeneralLogs modelGeneralLogs = ModelGeneralLogs.getModelGeneralLogsInstance();
        modelGeneralLogs.setMessage(message);
        modelGeneralLogs.setExecutionTime(getExecutionTime());
        try {
            if (x==1){
                modelGeneralLogs.setCountTable(getTableCount());
                modelGeneralLogs.setCountRecord(sendFileName());
            }
            else {
                modelGeneralLogs.setCountTable(0);
                modelGeneralLogs.setCountRecord(0);
            }
        }catch (Exception e){
            e.printStackTrace();
        }

        eventLog.generateEventLogEntry(modelEventLogs);
        generalLog.generalLogsEntry(modelGeneralLogs);
    }


    public void reportToEventMessageLog (String message) {

        EventLogImpl eventLog = EventLogImpl.getEventLogsImplInstance();
        ModelEventLogs modelEventLogs = ModelEventLogs.getModelEventLogsInstances();
        modelEventLogs.setMessags(message);
        modelEventLogs.setEventType(Constants.CHANGE_IN_DB);
        eventLog.generateEventLogEntry(modelEventLogs);

    }

    public void reportToEventErrorLog (String message) {

        EventLogImpl eventLog = EventLogImpl.getEventLogsImplInstance();
        ModelEventLogs modelEventLogs = ModelEventLogs.getModelEventLogsInstances();
        modelEventLogs.setMessags(message);
        modelEventLogs.setEventType(Constants.CRASH_REPORT);
        eventLog.generateEventLogEntry(modelEventLogs);
    }


    public void reportToEventUpdateLog (String message, String updatedTabelName) {
        EventLogImpl eventLog = EventLogImpl.getEventLogsImplInstance();
        ModelEventLogs modelEventLogs = ModelEventLogs.getModelEventLogsInstances();
        modelEventLogs.setMessags(message);
        modelEventLogs.setEventType(Constants.UPDATE_OPERATION);
        modelEventLogs.setUpdatedValue(updatedTabelName);
        eventLog.generateEventLogEntry(modelEventLogs);
    }



    public void reportToGeneralLog (String message) {

        GeneralLogImpl generalLog = GeneralLogImpl.getGeneralLogsImplInstance();
        ModelGeneralLogs modelGeneralLogs = ModelGeneralLogs.getModelGeneralLogsInstance();
        modelGeneralLogs.setMessage(message);

        generalLog.generalLogsEntry(modelGeneralLogs);

    }



    public static int getTableCount () throws IOException {
        int count = 0;
        String path = "src/main/java/com/dmwa/Database/"+UserSessionUtils.getDatabaseName()+".txt";
        BufferedReader br = new BufferedReader(new FileReader(path));
        String line;
        while ((line = br.readLine()) != null) {
            if (line.contains("@table"))
            {
                break;
            }
        }
        while (br.readLine() != null) {
            count ++;
        }
        return count;
    }


    public static int sendFileName() throws IOException {
        String path = "src/main/java/com/dmwa/Database/"+UserSessionUtils.getDatabaseName()+".txt";
        BufferedReader br = new BufferedReader(new FileReader(path));
        String line;
        while ((line = br.readLine()) != null) {
            if (line.contains("@table"))
            {
                break;
            }
        }

        int totalRecord= 0;

        for (int i=0; i<getTableCount(); i++){
            totalRecord = totalRecord + gettableRecored(br.readLine());
        }

        return totalRecord;
    }


    public static int gettableRecored(String fileName) throws IOException {

        String instance1="VM1";
        String instance2="VM2";
        String instance3="local";

        int count = 0;

        boolean check1 = new File("src/main/java/com/dmwa/Tables/"+instance1, fileName+".txt").exists();
        boolean check2 = new File("src/main/java/com/dmwa/Tables/"+instance2, fileName+".txt").exists();
        boolean check3 = new File("src/main/java/com/dmwa/Tables/"+instance3, fileName+".txt").exists();

        if (check1){
            String path = "src/main/java/com/dmwa/Tables/"+instance1+"/"+fileName+".txt";
            BufferedReader br = new BufferedReader(new FileReader(path));
            String line;
            while ((line = br.readLine()) != null) {
                if (line.contains("@value"))
                {
                    break;
                }
            }
            while (br.readLine() != null) {
                count ++;
            }
        } else if (check2){
            String path = "src/main/java/com/dmwa/Tables/"+instance2+"/"+fileName+".txt";
            BufferedReader br = new BufferedReader(new FileReader(path));
            String line;
            while ((line = br.readLine()) != null) {
                if (line.contains("@value"))
                {
                    break;
                }
            }
            while (br.readLine() != null) {
                count ++;
            }
        } else if (check3){
            String path = "src/main/java/com/dmwa/Tables/"+instance3+"/"+fileName+".txt";
            BufferedReader br = new BufferedReader(new FileReader(path));
            String line;
            while ((line = br.readLine()) != null) {
                if (line.contains("@value"))
                {
                    break;
                }
            }
            while (br.readLine() != null) {
                count ++;
            }
        }
        return count;
    }


}
