package com.dmwa.Logs;

import com.dmwa.Logs.constants.Constants;
import com.dmwa.Logs.model.ModelGeneralLogs;
import com.dmwa.Logs.utils.UserSessionUtils;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class GeneralLogImpl {

    private static GeneralLogImpl generalLogsImplInstance = null;

    public static GeneralLogImpl getGeneralLogsImplInstance() {
        if (generalLogsImplInstance == null) {
            generalLogsImplInstance = getLogsImplInstance();
        }
        return generalLogsImplInstance;
    }

    private static GeneralLogImpl getLogsImplInstance() {
        return new GeneralLogImpl();
    }

    public void generalLogsEntry(ModelGeneralLogs modelGeneralLogs) {
        logsDirectorySetup logDirSetup = logsDirectorySetup.getLogsDirectorySetupInstane();
        File logFilesDirectory = logDirSetup.generateDirectories();

        try (BufferedWriter writeGeneralLog = new BufferedWriter(new FileWriter("src/main/java/com/dmwa/Logs/log_files/generalLogs.txt", true))) {
            writeGeneralLog.append("Log Timestamp: ").append(String.valueOf(modelGeneralLogs.getLogsEntryTs())).append("\n");
            writeGeneralLog.append("Username: ").append(UserSessionUtils.getUsername()).append("\n");
            writeGeneralLog.append("Database: ").append(UserSessionUtils.getDatabaseName()).append("\n");
            writeGeneralLog.append("Message: ").append(modelGeneralLogs.getMessage()).append("\n");
            writeGeneralLog.append("Query Execution Time: ").append(String.valueOf(modelGeneralLogs.getExecutionTime())).append("ms").append("\n");
            writeGeneralLog.append("Number of Tables: ").append(String.valueOf(modelGeneralLogs.getCountTable())).append("\n");
            writeGeneralLog.append("Number of Records: ").append(String.valueOf(modelGeneralLogs.getCountRecord())).append("\n");
            writeGeneralLog.append("Timestamp: ").append(String.valueOf(modelGeneralLogs.getGeneralLogsTimestamp())).append("\n");
            writeGeneralLog.append("\n\n");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
