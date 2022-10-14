package com.dmwa.Logs;

import com.dmwa.Logs.constants.Constants;
import com.dmwa.Logs.model.ModelEventLogs;
import com.dmwa.Logs.utils.UserSessionUtils;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class LoginLogImpl {

    private static LoginLogImpl loginLogInstance = null;

    public static LoginLogImpl getLoginLogInstance() {
        if (loginLogInstance == null) {
            loginLogInstance = new LoginLogImpl();
        }
        return loginLogInstance;
    }

    public void generateLoginlogsLogEntry() {
        logsDirectorySetup logDirSetup = logsDirectorySetup.getLogsDirectorySetupInstane();
        File logFilesDirectory = logDirSetup.generateDirectories();
        try (BufferedWriter writeLoginLogs = new BufferedWriter(new FileWriter("src/main/java/com/dmwa/Logs/log_files/loginLogs.txt", true))) {
            writeLoginLogs.append("Username: ").append(UserSessionUtils.getUsername()).append("\n");
            writeLoginLogs.append("Login TimeStamp: ").append(UserSessionUtils.getLoginTimestamp()).append("\n\n");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


}
