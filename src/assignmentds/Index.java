package assignmentds;

import java.util.Scanner;

public class Index {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        String reset = "\u001B[0m";
        String green = "\u001B[32m";
        
        System.out.println(green+" __    __     _                            _                           _    _               _____ _              ___      _                  ");                 
        System.out.println(green+"/ / /\\ \\ \\___| | ___ ___  _ __ ___   ___  | |_ ___     /\\  /\\__ _  ___| | _(_)_ __   __ _  /__   \\ |__   ___    / __\\   _| |_ _   _ _ __ ___ ");  
        System.out.println(green+"\\ \\/  \\/ / _ \\ |/ __/ _ \\| '_ ` _ \\ / _ \\ | __/ _ \\   / /_/ / _` |/ __| |/ / | '_ \\ / _` |   / /\\/ '_ \\ / _ \\  / _\\| | | | __| | | | '__/ _ \\"); 
        System.out.println(green+ " \\  /\\  /  __/ | (_| (_) | | | | | |  __/ | || (_) | / __  / (_| | (__|   <| | | | | (_| |  / /  | | | |  __/ / /  | |_| | |_| |_| | | |  __/");
        System.out.println(green+"  \\/  \\/ \\___|_|\\___\\___/|_| |_| |_|\\___|  \\__\\___/  \\/ /_/ \\__,_|\\___|_|\\_\\_|_| |_|\\__, |  \\/   |_| |_|\\___| \\/    \\__,_|\\__|\\__,_|_|  \\___|"); 
        System.out.println(green+"                                                                                    |___/                                                    ");

        
        String response;
        boolean isValidResponse = false;
        do{  
            String cyan = "\u001B[36m";
            System.out.print(cyan + "Have you registered? (yes/no): " + reset); // Ask the user whether they have registered
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
