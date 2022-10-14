package com.dmwa.Logs.model;

import java.time.Instant;
import java.util.List;
import java.util.Map;

public class ModelGeneralLogs {

    private String message;
    private long ExecutionTime;
    private long countRecord;
    private List<Map<String, Long>> listOfTable;
    private int countTable;

    private static ModelGeneralLogs modelGeneralLogsInstance;

    public static ModelGeneralLogs getModelGeneralLogsInstance(){
        if (modelGeneralLogsInstance == null){
            modelGeneralLogsInstance = new ModelGeneralLogs();
        }
        return modelGeneralLogsInstance;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public List<Map<String, Long>> getListOfTable() {
        return listOfTable;
    }

    public long getExecutionTime() {
        return ExecutionTime;
    }

    public void setExecutionTime(long executionTime) {
        ExecutionTime = executionTime;
    }

    public Instant getGeneralLogsTimestamp(){
        return Instant.now();
    }

    public long getLogsEntryTs() {
        return Instant.now().toEpochMilli();
    }

    public void setListOfTable(List<Map<String, Long>> listOfTable) {
        this.listOfTable = listOfTable;
    }

    public int getCountTable() {
        return countTable;
    }

    public void setCountTable(int countTable) {
        this.countTable = countTable;
    }

    public long getCountRecord() {
        return countRecord;
    }

    public void setCountRecord(long countRecord) {
        this.countRecord = countRecord;
    }

    public static void setModelGeneralLogsInstance(ModelGeneralLogs modelGeneralLogsInstance) {
        ModelGeneralLogs.modelGeneralLogsInstance = modelGeneralLogsInstance;
    }



}
