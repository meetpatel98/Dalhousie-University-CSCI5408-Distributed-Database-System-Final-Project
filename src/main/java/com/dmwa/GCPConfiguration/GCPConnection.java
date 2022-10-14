package com.dmwa.GCPConfiguration;

import com.jcraft.jsch.ChannelSftp;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;

public class GCPConnection {

    private static Session jschSession = null;
    private static ChannelSftp sftpChannel = null;

    static ChannelSftp setupJsch() throws JSchException {
        JSch jsch = new JSch();
        jsch.setKnownHosts("/Users/manasvisharma/.ssh/known_hosts");
        java.util.Properties config = new java.util.Properties();
        config.put("StrictHostKeyChecking", "no");

        jschSession = jsch.getSession(GCPConfig.REMOTE_DB_USER, GCPConfig.REMOTE_DB_HOST);
        jschSession.setConfig(config);

        jschSession.setPassword(GCPConfig.PASSWORD);
        jschSession.connect();

        return (ChannelSftp) jschSession.openChannel("sftp");
    }

    public static void closeSession() {
        if (sftpChannel != null) {
            sftpChannel.disconnect();
        }

        if (jschSession != null) {
            jschSession.disconnect();
        }
    }
}

