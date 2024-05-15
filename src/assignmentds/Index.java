package assignmentds;

import java.util.Scanner;

public class Index {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        String reset = "\u001B[0m";
        String green = "\u001B[32m";
        
        System.out.println(green+" __      __       .__                                  __ ");                 
        System.out.println(green+"/  \\    /  \\ ____ |  |   ____  ____   _____   ____   _/ ");  
        System.out.println(green+"\\   \\/\\/   // __ \\|  | _/ ___\\/  _ \\ /     \\_/ __ \\"); 
        System.out.println(green+ "\\        /\\  ___/|  |_\\  \\__(  <_> )   Y Y  \\  ___/");
        System.out.println(green+"  \\__/\\  /  \\___  >____/\\___  >____/|__|_|  /\\___  >"); 
        System.out.println(green+"     \\/       \\/          \\/            \\/     \\/"+ reset);

        
        System.out.println("\n................................................................................................................");
        System.out.println("\t\t\t\t*** Welcome to Hacking the Future ***");
        System.out.println("................................................................................................................\n");
        
        
        String response;
        boolean isValidResponse = false;
        do{
            // Ask the user whether they have registered
            System.out.print("Have you registered? (yes/no): ");
            response = scanner.nextLine().toLowerCase(); // Convert to lowercase for case-insensitivity
        
            switch (response) {
                case "yes":
                    // If already registered, link to Login.java
                    Login.main(null);
                    isValidResponse = true;
                    break;
                case "no":
                    // If not registered, link to User.java for registration
                    User newUser = User.registerUser(scanner);
                    isValidResponse = true;
                    break;
                default:
                    System.out.println("Invalid response. Please enter 'yes' or 'no'.");
                    break;
            }
        } while (!isValidResponse);

        scanner.close();
       
    }
}
