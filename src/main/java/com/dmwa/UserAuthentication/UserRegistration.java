package com.dmwa.UserAuthentication;


import java.util.Scanner;

public class UserRegistration {

   public static void register(Scanner scanner) {
        String usernameFromFile;
       System.out.println("Please set your username:");
       final String username = scanner.nextLine();
       if (!ValidateData.validate(username)) {
            System.out.println("Invalid username");
            return;
       }
       final String hashedUsername= Hashing.HashUsername(username);

       usernameFromFile= ValidateData.getHashedUsername(hashedUsername);
       if(hashedUsername.equals(usernameFromFile)){
           System.out.println("User already exists");
           return;
       }

       System.out.println("Please set your password:");
       final String password = scanner.nextLine();
       if (!ValidateData.validatePassword(password)) {
            System.out.println("Invalid password..");
            return;
       }
       final String hashedPassword = Hashing.HashPassword(password);


       System.out.println("Please enter your security question");
       final String question1 = scanner.nextLine();

       if (!ValidateData.validate(question1)) {
            System.out.println("Invalid");
            return;
       }
       System.out.println("Please enter answer to your security question:");
       final String answer1 = scanner.nextLine();
       ValidateData.WriteToFile(hashedUsername, hashedPassword, question1, answer1);

   }


}





