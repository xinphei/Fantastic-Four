package assignmentds;

import java.sql.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Scanner;
import java.util.TimeZone;

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
        }
        while (!loginSuccessful && askToLoginAgain(scanner));
        scanner.close();
    }

    private static User authenticateUser(String identifier, String password) {
        // Validate identifier again before proceeding
        if ("".equals(identifier)) {
            System.out.println("-------------------------------------------");
            System.out.println("Email/Username is required");
            System.out.println("-------------------------------------------");
            return null;
        }
        
        else if (!AuthenticationSystem.isValidEmail(identifier)) {
            return null;
        }
        
        if ("".equals(password)) {
            System.out.println("-------------------------------------------");
            System.out.println("Password is required");
            System.out.println("-------------------------------------------");
            return null;
        }

        try (ResultSet resultSet = DBOperations.getUserDetailsSet(identifier)) { //changed the parameter "email" to "identifier"
            if (resultSet.next()) {
                // validate password
                String hashedPassword = resultSet.getString("password");
                byte[] retrievedSalt = resultSet.getBytes("salt");
                String inputHash = SecureEncryptor.hashPassword(password, retrievedSalt);
                
                // if password valid, retrieve user details
                if (inputHash.equals(hashedPassword)) {

                    //MODIFIED BY DY**********************************************
                    String email = resultSet.getString("email");
                    String username = resultSet.getString("username");
                    int role = resultSet.getInt("role");
                    double coordinateX = resultSet.getDouble("locationCoordinate_X");
                    double coordinateY = resultSet.getDouble("locationCoordinate_Y");
                    int current_points = resultSet.getInt("current_points");

                    Coordinate coordinate = new Coordinate(coordinateX, coordinateY);

                    User authenticatedUser = new User(email, username, hashedPassword, retrievedSalt, role, coordinate, current_points);
                    //MODIFIED BY DY**********************************************
                    return authenticatedUser;
                }
                else {
                    // User authentication failed 
                    System.out.println("-------------------------------------------");
                    System.out.println("Login failed. Incorrect email/username or password.");
                    System.out.println("-------------------------------------------");
                    return null;
                }
            }
            else {
                // User authentication failed 
                System.out.println("-------------------------------------------");
                System.out.println("Login failed. Incorrect email/username or password.");
                System.out.println("-------------------------------------------");
                return null;
            }
        }
        catch (SQLException e) {
            e.printStackTrace();
            return null;            
        }
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
