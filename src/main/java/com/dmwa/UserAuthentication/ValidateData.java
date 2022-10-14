package com.dmwa.UserAuthentication;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.List;
import java.util.Scanner;

public class ValidateData {

    public static boolean validate(String inputValue) {
        try {
            if (inputValue.isEmpty()) {
                return false;
            } else {
                return true;
            }
        } catch (Exception e) {
            return false;
        }
    }

    public static boolean validatePassword(String inputValue) {
        try {
            if (inputValue.isEmpty()) {
                return false;
            } else if (inputValue.matches("[A-Za-z0-9]+")) {
                return true;
            } else {
                return false;
            }
        } catch (Exception e) {
            return false;
        }
    }

    public static void WriteToFile(String username, String hashedPassword, String question1, String answer1) {
        Path p = Paths.get("src/main/java/com/dmwa/UserAuthentication/User_Profile.txt");

        String s = System.lineSeparator() + username + "|" + hashedPassword + "|" + question1 + "|" + answer1;

        try (BufferedWriter writer = Files.newBufferedWriter(p, StandardOpenOption.APPEND)) {
            writer.write(s);
        } catch (IOException ioe) {
            System.err.format("IOException: %s%n", ioe);
        }

    }

    public static boolean matchUsername(String hashedUsername) {
        try {
            File myFile = new File("src/main/java/com/dmwa/UserAuthentication/User_Profile.txt");
            BufferedReader readerObj = new BufferedReader(new FileReader(myFile));
            String line = "";
            while ((line = readerObj.readLine()) != null) {
                String[] s = line.split("\\|");
                for (String a : s) {
                    if (a.equals(hashedUsername)) {
                        return true;
                    }
                }
            }

        } catch (Exception e) {
            return true;
        }
        return true;
    }

    public static void getSecurityQuestion(String username) {

        try {
            File myFile = new File("src/main/java/com/dmwa/UserAuthentication/User_Profile.txt");
            BufferedReader readerObj = new BufferedReader(new FileReader(myFile));
            String line = "";
            while ((line = readerObj.readLine()) != null) {
                String[] s = line.split("\\|");
                for (String a : s) {
                    if (a.equals(username)) {
                        System.out.println(s[2]);
                    }
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public static boolean validateAnswer(String username, String answer1) {

//        try {
//            File myFile = new File("src/main/java/com/dmwa/UserAuthentication/User_Profile.txt");
//            BufferedReader readerObj = new BufferedReader(new FileReader(myFile));
//            String line = "";
//            while ((line = readerObj.readLine()) != null) {
//                String[] s = line.split("\\|");
//                for (String a : s) {
//                    if (a.equals(username)){
//                     if(a.equals(s[3])) {
//                        return true;
//                    }
//                }
//            }
//
//        }
//        }catch (Exception e) {
//            return false;
//        }
//        return true;
//    }
        try {
            List<String> allLines = Files.readAllLines(Paths.get("src/main/java/com/dmwa/UserAuthentication/User_Profile.txt"));
            for (String line : allLines) {
                String[] s = line.split("\\|");
                if (s[0].equals(username) && s[3].equals(answer1)) {
                    return true;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }

    public static String getHashedPassword(String username){
        String hashPassword = null;
        try {

            File myFile = new File("src/main/java/com/dmwa/UserAuthentication/User_Profile.txt");
            BufferedReader readerObj = new BufferedReader(new FileReader(myFile));
            String line = "";
            while((line = readerObj.readLine()) != null)
            {
                String[] s = line.split("\\|");
                for (String a : s) {
                    if (a.equals(username)) {
                        hashPassword=s[1];
                    }
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return hashPassword;

    }

    public static String getHashedUsername(String username){
        String hashUsername = null;
        try {

            File myFile = new File("src/main/java/com/dmwa/UserAuthentication/User_Profile.txt");
            BufferedReader readerObj = new BufferedReader(new FileReader(myFile));
            String line = "";
            while((line = readerObj.readLine()) != null)
            {
                String[] s = line.split("\\|");
                for (String a : s) {
                    if (a.equals(username)) {
                        hashUsername=s[0];
                    }
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return hashUsername;

    }
}

