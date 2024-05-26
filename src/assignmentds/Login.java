package assignmentds;

import java.sql.*;
import java.util.Scanner;

public class Login {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        boolean loginSuccessful = true;
        do {
            String reset = "\u001B[0m";
            String cyan ="\u001B[36m";
            System.out.println("\n\n___________________________________________");
            System.out.println(cyan +"             ^^^ LOGIN PAGE ^^^            "+ reset);
            System.out.println("___________________________________________");
            System.out.print("Email/Username: ");
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
            
            System.out.print("Password: ");
            String password = scanner.nextLine();

            // Perform authentication using MySQL
            User user = authenticateUser(identifier, password);
            if (user != null) {
                System.out.println("\n<><><><><><><><><><><><><><><><><><><><>");
                System.out.println("Congratulations! Login Successful.");
                System.out.println("<><><><><><><><><><><><><><><><><><><><>");
 
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
