package com.dmwa.Logs.model;

import java.time.Instant;

public class ModelEventLogs {

    private String Id;
    private String columnName;
    private String tableName;
    private String updatedValue;
    private String oldValue;
    private String eventType;
    private String messags;
    private String Username;
    private String dBname;

    private static ModelEventLogs modelEventLogsInstance = null;

    public static ModelEventLogs getModelEventLogsInstances(){
        if (modelEventLogsInstance == null){
            modelEventLogsInstance = new ModelEventLogs();
        }
        return modelEventLogsInstance;
    }

    public String getId() {
        return Id;
    }

    public void setId(String id) {
        Id = id;
    }

    public String getUsername() {
        return Username;
    }

    public void setUsername(String username) {
        Username = username;
    }

    public String getMessags() {
        return messags;
    }

    public void setMessags(String messags) {
        this.messags = messags;
    }

    public String getdBname() {
        return dBname;
    }

    public void setdBname(String dBname) {
        this.dBname = dBname;
    }

    public String getColumnName() {
        return columnName;
    }

    public void setColumnName(String columnName) {
        this.columnName = columnName;
    }

    public String getTableName() {
        return tableName;
    }

    public void setTableName(String tableName) {
        this.tableName = tableName;
    }

    public String getUpdatedValue() {
        return updatedValue;
    }

    public void setUpdatedValue(String updatedValue) {
        this.updatedValue = updatedValue;
    }

    public String getEventType() {
        return eventType;
    }

    public void setEventType(String eventType) {
        this.eventType = eventType;
    }

    public long getEventLogsEntryTs() {
        return Instant.now().toEpochMilli();
    }

    public String getOldValue() {
        return oldValue;
    }

    public void setOldValue(String oldValue) {
        this.oldValue = oldValue;
    }


    public Instant getEventLogsTs() {
        return Instant.now();
    }
}
