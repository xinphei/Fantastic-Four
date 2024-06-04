package assignmentds;

import java.sql.*;
import java.util.Scanner;

public class Login {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        boolean loginSuccessful = true;
        do {
            String reset = "\u001B[0m";
            String blue = "\u001B[34m";
            String cyan = "\u001B[36m";
            System.out.println(blue + "   __             _           ___                 \n" +
                           blue + "  / /  ___   __ _(_)_ __     / _ \\__ _  __ _  ___ \n" +
                           blue + " / /  / _ \\ / _` | | '_ \\   / /_)/ _` |/ _` |/ _ \\\n" +
                           blue + "/ /__| (_) | (_| | | | | | / ___/ (_| | (_| |  __/\n" +
                           blue + "\\____/\\___/ \\__, |_|_| |_| \\/    \\__,_|\\__, |\\___|\n" +
                           blue + "            |___/                      |___/      ");
            
            System.out.println();
            System.out.print(cyan + "Email/Username: " + reset);
            String identifier = scanner.nextLine();
            
            if ("".equals(identifier)) {
                System.out.println("-------------------------------------------");
                System.out.println("Email/Username is required");
                System.out.println("-------------------------------------------");
                loginSuccessful = false;
                continue; // Ask user to log in again
            }
            
            // Validate email or username
            else if (!AuthenticationSystem.isValidEmail(identifier) && !AuthenticationSystem.isValidUsername(identifier)) {
                System.out.println("-------------------------------------------");
                System.out.println("Login failed. Invalid email/username.");
                System.out.println("-------------------------------------------");
                loginSuccessful = false;
                continue; // Ask user to log in again
            }
            
            System.out.print(cyan + "Password: " + reset);
            String password = scanner.nextLine();

            // Perform authentication using MySQL
            User user = authenticateUser(identifier, password);
            if (user != null) {
                String magenta = "\u001B[35m";
                System.out.println(magenta + "   ___                        _      _   _              _        ___                       __      _ \n" +
                           magenta + "  / __|___ _ _  __ _ _ _ __ _| |_ __| | | |   ___  __ _(_)_ _   / __|_  _ __ __ ___ ______/ _|_  _| |\n" +
                           magenta + " | (__/ _ \\ ' \\/ _` | '_/ _` |  _(_-<_| | |__/ _ \\/ _` | | ' \\  \\__ \\ || / _/ _/ -_|_-<_-<  _| || | |\n" +
                           magenta + "  \\___\\___/_||_\\__, |_| \\__,_|\\__/__(_) |____\\___/\\__, |_|_||_| |___/\\_,_\\__\\__\\___/__/__/_|  \\_,_|_|\n" +
                           magenta + "               |___/                              |___/                                              ");
 
                Home.main(user); //lead to Home to choose what to do
                break;
            }
            else {
                loginSuccessful = false;
                continue;
            }
        } while (!loginSuccessful && askToLoginAgain(scanner));
        scanner.close();
    }

    private static User authenticateUser(String identifier, String password) {
        // Validate identifier again before proceeding
        if ("".equals(identifier)) {
            System.out.println("-------------------------------------------");
            System.out.println("Email/Username is required");
            System.out.println("-------------------------------------------");
            return null;
        } else if (!AuthenticationSystem.isValidEmail(identifier) && !AuthenticationSystem.isValidUsername(identifier)) {
            return null;
        }
        
        if ("".equals(password)) {
            System.out.println("-------------------------------------------");
            System.out.println("Password is required");
            System.out.println("-------------------------------------------");
            return null;
        }

        try {
            return DBOperations.getLoginUser(identifier, password);
        } catch (SQLException e) {
            System.out.println("SQL error " + e.getSQLState());
        }
        return null;
    }

    private static boolean askToLoginAgain(Scanner scanner) {
        System.out.print("\nDo you want to log in again? (yes/no): ");
        String response = scanner.nextLine().toLowerCase();
        if (response.equals("yes")) {
            return true;
        }
        else if (response.equals("no")) {
            System.out.println("\nReturning to the index page...\n");
            Index.main(null); //lead to index page
            return false;
        }
        else {
            System.out.println("\nInvalid response. Please enter 'yes' or 'no'.");
            return askToLoginAgain(scanner); // Ask again for a valid response
        }
    }
}
