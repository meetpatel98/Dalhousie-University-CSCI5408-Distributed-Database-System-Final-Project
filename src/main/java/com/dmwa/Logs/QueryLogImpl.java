package com.dmwa.Logs;

import com.dmwa.Logs.constants.Constants;
import com.dmwa.Logs.model.ModelQueryLogs;
import com.dmwa.Logs.utils.UserSessionUtils;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class QueryLogImpl {

    private static QueryLogImpl queryLogsImplInstance = null;

    public static QueryLogImpl getQueryLogsImplInstance() {
        if (queryLogsImplInstance == null) {
            queryLogsImplInstance = getLogsImplInstance();
        }
        return queryLogsImplInstance;
    }

    private static QueryLogImpl getLogsImplInstance() {
        return new QueryLogImpl();
    }

    public void generateQueryLogEntry(ModelQueryLogs modelQueryLogs) {
        logsDirectorySetup logDirSetup = logsDirectorySetup.getLogsDirectorySetupInstane();
        File logFilesDirectory = logDirSetup.generateDirectories();

        try (BufferedWriter writeQueryLog = new BufferedWriter(new FileWriter("src/main/java/com/dmwa/Logs/log_files/queryLogs.txt", true))) {
            writeQueryLog.append("Log Timestamp: ").append(String.valueOf(modelQueryLogs.getLogsEntryTs())).append("\n");
            writeQueryLog.append("Username: ").append(UserSessionUtils.getUsername()).append("\n");
            writeQueryLog.append("Database: ").append(UserSessionUtils.getDatabaseName()).append("\n");
            writeQueryLog.append("Query: ").append(modelQueryLogs.getQueryEntered()).append("\n");
            writeQueryLog.append("Query Submission Timestamp: ").append(String.valueOf(modelQueryLogs.getQuerySubmissionTs())).append("\n\n");
        } catch (IOException e) {
            e.printStackTrace();
        }

    }


}
