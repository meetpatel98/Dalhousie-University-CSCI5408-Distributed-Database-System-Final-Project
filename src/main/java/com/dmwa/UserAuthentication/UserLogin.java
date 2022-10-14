package com.dmwa.UserAuthentication;

import com.dmwa.Logs.LoginLogImpl;
import com.dmwa.Logs.utils.UserSessionUtils;
import com.dmwa.Main;

import java.time.Instant;
import java.util.Scanner;

public class UserLogin {

    public static void Login(Scanner scanner){
        String hashedUsername;
        String hashedPassword;
        String usernameFromFile;
        System.out.println("Enter your username:");
        String username= scanner.nextLine();
        hashedUsername=Hashing.HashUsername(username);

        usernameFromFile= ValidateData.getHashedUsername(hashedUsername);
        if(!hashedUsername.equals(usernameFromFile)){
            return;
        }

        System.out.println("Enter your password:");
        String password= scanner.nextLine();
        password= Hashing.HashPassword(password);

        hashedPassword=ValidateData.getHashedPassword(hashedUsername);

        if(password.equals(hashedPassword)){
            System.out.println("Valid password");
        }
        else{
            System.out.println("Invalid password!!!");
            return;
        }

        System.out.println("Enter answer to the security question:");
        ValidateData.getSecurityQuestion(hashedUsername);

        System.out.println("Enter answer:");
        final String answer1=scanner.nextLine();
        if(!ValidateData.validateAnswer(hashedUsername,answer1))
        {
            System.out.println("Invalid answer.. Try logging in again");
            return;
        }
        UserSessionUtils.setUserSession(username, Instant.now());
        LoginLogImpl loginLog = LoginLogImpl.getLoginLogInstance();
        loginLog.generateLoginlogsLogEntry();
        System.out.println("You are logged in!!");
        Main.AfterLoginMenu(hashedUsername);
    }

}
