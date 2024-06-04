package assignmentds;

import java.util.LinkedList;
import java.util.List;
import java.util.Scanner;

public class FriendRequest {
    
    static String reset = "\u001B[0m";
    static String purple_background ="\u001B[45m";
    static String magenta = "\u001B[35m";

    public static void main(User user) {
        User currentUser = user; 
        Scanner scanner = new Scanner(System.in);
        int option = -1;
        
        

        while (option != 0) {
            displayMenu();
            option = scanner.nextInt();
            switch (option) {
                case 1:
                    viewOtherStudents(currentUser, scanner);
                    break;
                case 2:
                    manageFriendRequests(currentUser, scanner);
                    break;
                case 0:
                    System.out.println("Exiting...");
                    Home.main(user);
                    break;
                default:
                    System.out.println("Invalid option. Please try again.");
            }
        }
        scanner.close();
    }

    private static void displayMenu() {
        System.out.println("");
        String reset = "\u001B[0m";
        String blue = "\u001B[34m";
        String cyan = "\u001B[36m";
        System.out.println(blue + "   ___     _                _                                                                _   \n" +
        blue + "  / __\\ __(_) ___ _ __   __| |___    /\\/\\   __ _ _ __   __ _  __ _  ___ _ __ ___   ___ _ __ | |_ \n" +
        blue + " / _\\| '__| |/ _ \\ '_ \\ / _` / __|  /    \\ / _` | '_ \\ / _` |/ _` |/ _ \\ '_ ` _ \\ / _ \\ '_ \\| __|\n" +
        blue + "/ /  | |  | |  __/ | | | (_| \\__ \\ / /\\/\\ \\ (_| | | | | (_| | (_| |  __/ | | | | |  __/ | | | |_ \n" +
        blue + "\\/   |_|  |_|\\___|_| |_|\\__,_|___/ \\/    \\/\\__,_|_| |_|\\__,_|\\__, |\\___|_| |_| |_|\\___|_| |_|\\__|\n" +
        blue + "                                                             |___/                               ");
        System.out.println("\nWelcome to Friends Management System!");
        System.out.println("1. View Other Students");
        System.out.println("2. Manage Friend Requests");
        System.out.println("0. Exit");
        System.out.print(cyan + "Enter your option: " + reset);
    }

    public static void viewOtherStudents(User currentUser, Scanner scanner) {
        List<User> students = DBOperations.fetchAllStudentsExcept(currentUser.getUsername());
        LinkedList<User> friends = DBOperations.fetchFriendsByUsername(currentUser.getUsername());
    
        System.out.println(purple_background+"\nStudent List:"+reset);
        int index = 1;
        for (User student : students) {
            if (!friends.contains(student)) { // Check if the student is not already a friend
                System.out.println(index + ". " + student.getUsername());
                index++;
            }
        }
        
        String reset = "\u001B[0m";
        String cyan = "\u001B[36m";
        System.out.print(cyan + "\nChoose a student to view profile (0 to go back): " + reset);
        int choice = scanner.nextInt();
        scanner.nextLine(); // Consume the newline
    
        if (choice > 0 && choice <= index - 1) { // Validate user's choice
            User selectedStudent = null;
            int selectedIndex = 1;
            for (User student : students) {
                if (!friends.contains(student)) {
                    if (selectedIndex == choice) {
                        selectedStudent = student;
                        break;
                    }
                    selectedIndex++;
                }
            }
            if (selectedStudent != null) {
                viewStudentProfile(selectedStudent, currentUser, scanner); // Pass the selected student to the profile view method
            } else {
                System.out.println("Invalid choice.");
            }
        } else if (choice != 0) {
            System.out.println("Invalid choice.");
        }
    }


    private static void viewStudentProfile(User student, User currentUser, Scanner scanner) {
        System.out.println("\nViewing Profile of " + magenta + student.getUsername()+reset);

        boolean value = true;

        do {
            ViewProfile.displayOtherStudentProfile(student);
            System.out.println("\nSend Friend Request to " + magenta +student.getUsername() + reset);
            System.out.println("1. Yes");
            System.out.println("2. No");
            int response = scanner.nextInt();

            switch (response) {
                case 1:
                    sendFriendRequest(student, currentUser);
                    value = false;
                    break;
                case 2:
                    value = false;
                    break;
                default:
                    System.out.println("Invalid input.");
                    value = true;
            }

        } while (value);
    }

    private static void manageFriendRequests(User currentUser, Scanner scanner) {
        // Check if the student has any friend requests
        LinkedList<User> friendRequests = DBOperations.fetchFriendRequests(currentUser.getUsername());
        if (!friendRequests.isEmpty()) {
            System.out.println("\nYou have received friend requests from:");
            int count = 1;
            for (User sender : friendRequests) {
                System.out.println(count + ". " + sender.getUsername());
                count++;
            }
            // Prompt the user to select a friend request
            System.out.print("Choose the number of the friend request to manage (0 to go back): ");
            int requestNumber = scanner.nextInt();
            scanner.nextLine(); // Consume newline left-over
            
            String reset = "\u001B[0m";
            String cyan = "\u001B[36m";
            if (requestNumber > 0 && requestNumber <= friendRequests.size()) {
                User sender = friendRequests.get(requestNumber - 1);
                System.out.println("\nSelected friend request from: " + sender.getUsername());
                System.out.println("1. Accept");
                System.out.println("2. Reject");
                System.out.print(cyan + "Choose an action: " + reset);
                int choice = scanner.nextInt();
                scanner.nextLine(); // Consume newline left-over

                switch (choice) {
                    case 1:
                        // Accept friend request
                        accept(currentUser, sender);
                        break;
                    case 2:
                        // Reject friend request
                        reject(currentUser, sender);
                        break;
                    default:
                        System.out.println("Invalid action.");
                }
            } else if (requestNumber == 0) {
                // Return to main menu
            } else {
                System.out.println("Invalid friend request number.");
            }
        } else {
            System.out.println("\nNo friend requests.");
        }
    }

    private static void accept(User currentUser, User sender) {
        // Accept the friend request by updating the database
        DBOperations.acceptFriendRequest(sender, currentUser);

        // Print a success message
        System.out.println(magenta + sender.getUsername() + reset + " is now your friend.");
    }

    private static void reject(User currentUser, User sender) {
        // Reject the friend request by updating the database
        DBOperations.rejectFriendRequest(sender, currentUser);

        // Print a success message
        System.out.println(magenta + sender.getUsername() +reset + "'s request removed.");
    }

    private static void sendFriendRequest(User student, User currentUser) {
        if (currentUser != null) {
            if (DBOperations.isFriend(currentUser.getUsername(), student.getUsername())) {
                System.out.println("You are already friends with " + magenta + student.getUsername()+reset);
            } else {
                DBOperations.sendFriendRequest(currentUser, student);
                System.out.println("Friend request sent to " + magenta + student.getUsername()+reset);
            }
        } else {
            System.out.println("Please log in first.");
        }
    }
}
