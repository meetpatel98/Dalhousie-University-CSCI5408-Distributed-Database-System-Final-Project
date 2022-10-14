package com.dmwa.Dump;

import java.io.IOException;

public interface GenerateDump {

    public void generateSQLDump(String databaseName) throws IOException;
}
