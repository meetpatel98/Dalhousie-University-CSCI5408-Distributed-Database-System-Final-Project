package com.dmwa.GCPConfiguration;

import com.jcraft.jsch.ChannelSftp;
import com.jcraft.jsch.SftpException;

import java.io.IOException;

public class GCPWriter {
    public static void writeFile(String src,String dst) throws IOException, SftpException {
        try {

            ChannelSftp sftpChannel = GCPConnection.setupJsch();
            sftpChannel.connect();
            sftpChannel.put(src, dst);
            sftpChannel.disconnect();
        } catch (Exception exception) {
            exception.printStackTrace();
        }
    }
}
