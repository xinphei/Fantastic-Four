package assignmentds;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Scanner;

public class ViewProfile {
    
    public static void main(User user) {

        // Call displayProfile method with the provided user
        displayProfile(user);
        Home.main(user);
    }

    public static void displayProfile(User user) {
        boolean validChoice = false;
        Scanner sc = new Scanner(System.in);

        do {
            displaySelection();
            System.out.print("Choice: ");
            char choice = sc.next().charAt(0);
            sc.nextLine();
            switch (choice) {
                case '1' :
                    validChoice = true;
                    System.out.println("Profile Information:");
                    System.out.println("Email: " + user.getEmail());
                    System.out.println("Username: " + user.getUsername());
                    System.out.println("Role: " + getRoleName(user.getRole()));
                    System.out.println("Location Coordinate: " + user.getLocationCoordinate());

                    // Additional information based on role
                    switch (user.getRole()) {
                        case 1: // Young Students
                            System.out.println("Points: " + user.getCurrentPoints());
                            System.out.println("Friends: ");
                            displayFriends(user.getFriends());
                            System.out.println("Parents: ");
                            displayParents(ParentChildRelationship.loadParent(user.getUsername()));
                            break;
                        case 2: // Parents
                            System.out.println("Past Bookings: ");
                            displayPastBookings(user.getPastBookings());
                            System.out.println("Children: ");
                            displayChildren(ParentChildRelationship.loadChildren(user.getUsername()));
                            break;
                        case 3: // Educators
                            System.out.println("Number of Quizzes Created: " + CreateEvent.getNumEventsCreated(user.getUsername(), user.getRole()));
                            System.out.println("Number of Events Created: " + CreateQuiz.getNumQuizzesCreated(user.getUsername(), user.getRole()));
                            //MODIFIED BY DY
                            break;
                        default:
                            System.out.println("Invalid role.");
                    }
                break;
                case '2' :
                    validChoice = true;

                    if (user.getRole() == 1) {
                        if (checkParentMax(user.getUsername())){
                            System.out.print("Enter username of parent : ");
                            String parentUsername = sc.next();
                            String parentEmail = verifyUser(parentUsername, 2);
                            if (parentEmail != null) {
                                System.out.println("Parent found with email : " + parentEmail);
                                System.out.print("Is this the correct parent? Type 'Yes' to confirm: ");
                                String confirm = sc.next();
                                sc.nextLine();
                                if ("yes".equalsIgnoreCase(confirm)) {
                                    if (!relationshipExists(parentUsername, user.getUsername())) {
                                        addRelationship(parentUsername, user.getUsername());
                                        displayProfile(user);
                                    } else {
                                        System.out.println("\nRelationship already existed\n");
                                        displayProfile(user);
                                    }
                                } else {
                                    System.out.println("\nAdding parent operation failed.\n");
                                    validChoice = false;
                                }
                            } else {
                                System.out.println("\nParent with the username entered not found.\n");
                                validChoice = false;
                            }
                        } else {
                            System.out.println("\nCurrent account already register with 2 parents.\n");
                            validChoice = false;
                        }
                    } else if (user.getRole() == 2) {
                        System.out.print("Enter username of the child : ");
                        String childUsername = sc.next();
                        String childEmail = verifyUser(childUsername, 1);
                        if (childEmail!=null) {
                            System.out.println("Child found with email : " + childEmail);
                            System.out.print("Is this the correct child? Type 'Yes' to confirm: ");
                            String confirm = sc.next();
                            sc.nextLine();
                            if ("yes".equalsIgnoreCase(confirm)) {
                                if (!relationshipExists(user.getUsername(), childUsername)) {
                                    addRelationship(user.getUsername(), childUsername);
                                    displayProfile(user);
                                } else {
                                    System.out.println("\nRelationship already existed\n");
                                    displayProfile(user);
                                }
                            } else {
                                System.out.println("\nAdding child operation failed\n");
                                validChoice = false;
                            }
                        } else {
                            System.out.println("\nChild with the username entered not found.\n");
                            validChoice = false;
                        }
                    } else if (user.getRole() == 3) {
                        System.out.println("\nEducator does not require adding parents or children.\n");
                        validChoice = false;
                    }
                break;
                default:
                    System.out.println("Invalid selection");
            }
        } while (!validChoice);
    }

    private static void displaySelection( ) {
        System.out.println("\n<<<<<<<<<<<<<<<<<>>>>>>>>>>>>>>>>");
        System.out.println("1. View Profile");
        System.out.println("2. Add Parent or Child");
        System.out.println("<<<<<<<<<<<<<<<<<>>>>>>>>>>>>>>>>");
    }

    private static boolean checkParentMax(String username) {
        String query = "SELECT COUNT(parent_username) AS parent_count FROM userdb.parentchildrelationship WHERE child_username = ?";
        try {
            Connection conn = DBOperations.getConnection();
            PreparedStatement pstmt = conn.prepareStatement(query);
            pstmt.setString(1, username);
            ResultSet resultSet = pstmt.executeQuery();
            if (resultSet.next()) {
                int count = resultSet.getInt("parent_count");
                return count < 2; // Returns true if less than 2 parents are registered
            }
        } catch (SQLException e) {
            System.out.println("Checking parent count error : " + e.getMessage());
        }
        return false;
    }

    public static boolean relationshipExists(String parentUsername, String childUsername) {
        String sql = "SELECT 1 FROM userdb.parentchildrelationship WHERE parent_username = ? AND child_username = ?";
        try (Connection conn = DBOperations.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, parentUsername);
            pstmt.setString(2, childUsername);
            ResultSet rs = pstmt.executeQuery();
            boolean exists = rs.next();
            System.out.println("Checking existence for " + parentUsername + " and " + childUsername + ": " + exists);
            return exists;
        } catch (SQLException e) {
            System.out.println("Query failed: " + e.getMessage());
        }
        return false;
    }

    private static String verifyUser(String username, int role) {
        String query = "SELECT email FROM userdb.users WHERE username = ? AND role = ?";

        try (Connection conn = DBOperations.getConnection();
             PreparedStatement preparedStatement = conn.prepareStatement(query)){
            preparedStatement.setString(1, username);
            preparedStatement.setInt(2, role);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                return resultSet.getString("email");
            }
        } catch (SQLException e) {
            System.out.println("Query failed : " + e.getMessage());
        }
        return null;
    }

    private static void addRelationship(String parentUsername, String childUsername) {
        String query = "INSERT INTO userdb.parentchildrelationship (parent_username, child_username) VALUES (?, ?)";

        try (Connection conn = DBOperations.getConnection();
             PreparedStatement preparedStatement = conn.prepareStatement(query)) {
            preparedStatement.setString(1, parentUsername);
            preparedStatement.setString(2, childUsername);
            preparedStatement.executeUpdate();
            System.out.println("\n" +
                    "  _____      _       _   _                 _     _                   _     _          _                                    __       _ _       \n" +
                    " |  __ \\    | |     | | (_)               | |   (_)                 | |   | |        | |                                  / _|     | | |      \n" +
                    " | |__) |___| | __ _| |_ _  ___  _ __  ___| |__  _ _ __     __ _  __| | __| | ___  __| |  ___ _   _  ___ ___ ___  ___ ___| |_ _   _| | |_   _ \n" +
                    " |  _  // _ \\ |/ _` | __| |/ _ \\| '_ \\/ __| '_ \\| | '_ \\   / _` |/ _` |/ _` |/ _ \\/ _` | / __| | | |/ __/ __/ _ \\/ __/ __|  _| | | | | | | | |\n" +
                    " | | \\ \\  __/ | (_| | |_| | (_) | | | \\__ \\ | | | | |_) | | (_| | (_| | (_| |  __/ (_| | \\__ \\ |_| | (_| (_|  __/\\__ \\__ \\ | | |_| | | | |_| |\n" +
                    " |_|  \\_\\___|_|\\__,_|\\__|_|\\___/|_| |_|___/_| |_|_| .__/   \\__,_|\\__,_|\\__,_|\\___|\\__,_| |___/\\__,_|\\___\\___\\___||___/___/_|  \\__,_|_|_|\\__, |\n" +
                    "                                                  | |                                                                                    __/ |\n" +
                    "                                                  |_|                                                                                   |___/ \n");
        } catch (SQLException e) {
            System.out.println("Insertion failed : " + e.getMessage());
        }
    }

    // Method to get role name based on role number
    private static String getRoleName(int role) {
        switch (role) {
            case 1:
                return "Young Students";
            case 2:
                return "Parents";
            case 3:
                return "Educators";
            default:
                return "Unknown";
        }
    }

    // Method to display friends list
    private static void displayFriends(List<User> friends) {
        for (User friend : friends) {
            System.out.println("- " + friend.getUsername());
        }
    }

    // Method to display parents list
    private static void displayParents(List<String> parents) {
        for (String parent : parents) {
            System.out.println("- " + parent);
        }
    }

    // Method to display children list
    private static void displayChildren(List<String> children) {
        for (String child : children) {
            System.out.println("- " + child);
        }
    }
    
    
    // Method to display past bookings
    private static void displayPastBookings(List<BookingSystem> pastBookings) {
    if (pastBookings == null || pastBookings.isEmpty()) {
        System.out.println("No past bookings available.");
        return;
    }

    for (BookingSystem booking : pastBookings) {
        if (booking != null) {
            System.out.println("- Destination: " + booking.getDestination().getName());
            System.out.println("  Date: " + booking.getDate());
            System.out.println("");
        }
    }
}
}





