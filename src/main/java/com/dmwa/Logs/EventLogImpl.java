package com.dmwa.Logs;

import com.dmwa.Logs.constants.Constants;
import com.dmwa.Logs.model.ModelEventLogs;
import com.dmwa.Logs.utils.UserSessionUtils;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class EventLogImpl {

    private static EventLogImpl eventLogsImplInstance = null;

    public static EventLogImpl getEventLogsImplInstance() {
        if (eventLogsImplInstance == null) {
            eventLogsImplInstance = new EventLogImpl();
        }
        return eventLogsImplInstance;
    }

    public void generateEventLogEntry(ModelEventLogs modelEventLogs){
        logsDirectorySetup logDirSetup = logsDirectorySetup.getLogsDirectorySetupInstane();
        File logFilesDirectory = logDirSetup.generateDirectories();
        try (BufferedWriter writeEventLog = new BufferedWriter(new FileWriter("src/main/java/com/dmwa/Logs/log_files/eventLogs.txt", true))) {
            if (modelEventLogs.getEventType().equals(Constants.CHANGE_IN_DB)) {
                writeEventLog.append("Log Timestamp: ").append(String.valueOf(modelEventLogs.getEventLogsEntryTs())).append("\n");
                writeEventLog.append("Username: ").append(UserSessionUtils.getUsername()).append("\n");
                writeEventLog.append("Database: ").append(UserSessionUtils.getDatabaseName()).append("\n");
                writeEventLog.append("Message: ").append(modelEventLogs.getMessags()).append("\n");
                writeEventLog.append("Timestamp: ").append(String.valueOf(modelEventLogs.getEventLogsTs())).append("\n\n");
            } else if (modelEventLogs.getEventType().equals(Constants.CRASH_REPORT)) {
                writeEventLog.append("Log Timestamp: ").append(String.valueOf(modelEventLogs.getEventLogsEntryTs())).append("\n");
                writeEventLog.append("Username: ").append(UserSessionUtils.getUsername()).append("\n");
                writeEventLog.append("Database: ").append(UserSessionUtils.getDatabaseName()).append("\n");
                writeEventLog.append("Error: ").append(modelEventLogs.getMessags()).append("\n");
                writeEventLog.append("Timestamp: ").append(String.valueOf(modelEventLogs.getEventLogsTs())).append("\n\n");
            } else if (modelEventLogs.getEventType().equals(Constants.UPDATE_OPERATION)) {
                writeEventLog.append("Log Timestamp: ").append(String.valueOf(modelEventLogs.getEventLogsTs())).append("\n");
                writeEventLog.append("Username: ").append(UserSessionUtils.getUsername()).append("\n");
                writeEventLog.append("Database: ").append(modelEventLogs.getdBname()).append("\n");
                writeEventLog.append("Message: ").append(modelEventLogs.getMessags()).append("\n");
                writeEventLog.append("Updated Tables: ").append(modelEventLogs.getTableName()).append("\n");
//                writeEventLog.append("Record ID: ").append(String.valueOf(modelEventLogs.getId())).append("\n");
                writeEventLog.append("Timestamp: ").append(String.valueOf(modelEventLogs.getEventLogsTs())).append("\n\n");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
