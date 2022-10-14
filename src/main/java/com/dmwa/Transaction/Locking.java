package com.dmwa.Transaction;

import java.io.*;
import java.util.UUID;

public class Locking {
    public void setLock(String tableName, String queryType) throws Exception{
        FileWriter fileWriter=null;
        FileWriter fileWriterTransaction = new FileWriter("src/main/java/com/dmwa/Transaction/transaction.txt",true);
        BufferedWriter buffer = new BufferedWriter(fileWriterTransaction);

        String line = "";

        if(line.contains(tableName)){
            System.out.println("The table is currently being accessed by another user, please wait!");
            Thread.sleep(10000);
            fileWriter = new FileWriter(new File("src/main/java/com/dmwa/Tables/lock.txt"));
            if (fileWriter != null) {
                fileWriter.append(tableName+"\n");
                buffer.append("Transaction ID: "+ UUID.randomUUID()+"------->   Table Name: "+tableName+"------->   Query Type: "+queryType+"\n");
            }
        }
        else{
            fileWriter = new FileWriter(new File("src/main/java/com/dmwa/Tables/lock.txt"));
            if (fileWriter != null) {
                fileWriter.append(tableName+"\n");
                buffer.append("Transaction ID: "+ UUID.randomUUID()+"------->   Table Name: "+tableName+"------->   Query Type: "+queryType+"\n");
            }
        }
        fileWriter.close();
        buffer.close();
    }

    public void removeLock(String tableName) throws Exception{
        FileWriter fileWriter = new FileWriter(new File("src/main/java/com/dmwa/Tables/lock.txt"));
        fileWriter.append("");
        fileWriter.flush();
        fileWriter.close();
    }
}
