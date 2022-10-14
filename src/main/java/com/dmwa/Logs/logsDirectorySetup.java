package com.dmwa.Logs;

import com.dmwa.Logs.constants.Constants;

import java.io.File;
import java.io.IOException;

public class logsDirectorySetup {

    private static logsDirectorySetup logsDirectorySetupInstance = null;
    File logFiles = null;

    public static logsDirectorySetup getLogsDirectorySetupInstane() {
        if (isaBoolean()) {
            logsDirectorySetupInstance = getLogsDirectorySetupInstance();
        }
        return logsDirectorySetupInstance;
    }

    private static logsDirectorySetup getLogsDirectorySetupInstance() {
        return new logsDirectorySetup();
    }

    private static boolean isaBoolean() {
        return logsDirectorySetupInstance == null;
    }

    public File generateDirectories(){
        logFiles = new File("src/main/java/com/dmwa/Logs/log_files");
        if (!logFiles.exists()) {
            if (logFiles.mkdirs()) {
                System.out.println(logFiles.getName() + " Directory Created!");
            }
        }
        return logFiles;

    }

}
