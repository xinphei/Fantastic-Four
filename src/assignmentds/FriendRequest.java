package assignmentds;

import assignmentds.User;
import assignmentds.ViewProfile;

import java.util.LinkedList;
import java.util.Scanner;

public class FriendRequest {

    static class UserNode {
        User user;
        LinkedList<UserNode> friends;

        public UserNode(User user) {
            this.user = user;
            this.friends = new LinkedList<>();
        }
    }

    public static void main(User user) {
        // Assume users are already created and stored in a List<User>
        LinkedList<User> allUsers = new LinkedList<>();
        // Create a graph of users
        LinkedList<UserNode> graph = createGraph(allUsers);

        // Simulate user interaction
        Scanner scanner = new Scanner(System.in);
        User currentUser = user; // Assign the passed user to currentUser
        int option = -1;

        while (option != 0) {
            System.out.println("\nWelcome to Friend Request System!");
            System.out.println("1. View Other Students");
            System.out.println("2. Manage Friend Requests");
            System.out.println("0. Exit");
            System.out.print("Enter your option: ");
            option = scanner.nextInt();

            switch (option) {
                case 1:
                    viewOtherStudents(graph, currentUser, scanner);
                    break;
                case 2:
                    if (currentUser != null) {
                        manageFriendRequests(graph, currentUser, scanner);
                    } else {
                        System.out.println("Please log in first.");
                    }
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
        Home.main(user);
    }

    private static LinkedList<UserNode> createGraph(LinkedList<User> allUsers) {
        LinkedList<UserNode> graph = new LinkedList<>();
        for (User user : allUsers) {
            graph.add(new UserNode(user));
        }
        return graph;
    }

    private static void viewOtherStudents(LinkedList<UserNode> graph, User currentUser, Scanner scanner) {
        int count = 1;
        LinkedList<UserNode> otherStudents = new LinkedList<>();
        for (UserNode node : graph) {
            if (currentUser == null || (!currentUser.getFriends().contains(node.user) && !currentUser.equals(node.user))) {
                otherStudents.add(node);
                System.out.println(count + ". " + node.user.getUsername());
                count++;
            }
        }

        int choice = 10000000;

        do {
            System.out.print("Choose a student to view profile (0 to go back): ");
            choice = scanner.nextInt();

            if (choice == 0) {
                break; // Exit the loop if the choice is 0
            }

            if (choice > 0 && choice <= count - 1) {
                UserNode selectedStudentNode = otherStudents.get(choice - 1);
                sendFriendRequest(selectedStudentNode.user, currentUser, scanner);

            } else {
                System.out.println("Invalid choice. Please try again.");
            }

        } while (true); // Keep looping until the user chooses to go back
    }

    private static void sendFriendRequest(User student, User currentUser, Scanner scanner) {
        System.out.println("Viewing Profile of " + student.getUsername());
        ViewProfile.main(student);
        System.out.println("1. Send Friend Request");
        System.out.println("0. Back to Other Students");
        System.out.print("Choose an action: ");
        int choice = scanner.nextInt();
        switch (choice) {
            case 1:
                if (currentUser != null) {
                    currentUser.sendFriendRequest(student);
                    System.out.println("Friend request sent to " + student.getUsername());
                } else {
                    System.out.println("Please log in first.");
                }
                break;
            case 0:
                // Return to Other Students list
                break;
            default:
                System.out.println("Invalid option. Please try again.");
        }
    }

    private static void manageFriendRequests(LinkedList<UserNode> graph, User currentUser, Scanner scanner) {
        LinkedList<User> friendRequests = currentUser.getFriendRequests();
        if (friendRequests.isEmpty()) {
            System.out.println("You have no friend requests.");
        } else {
            System.out.println("Friend Requests:");
            int count = 1;
            for (User friend : friendRequests) {
                System.out.println(count + ". " + friend.getUsername());
                count++;
            }
            System.out.println("0. Back");
            System.out.print("Choose a friend request to manage (0 to go back): ");
            int choice = scanner.nextInt();
            if (choice != 0 && choice <= count - 1) {
                User selectedFriend = friendRequests.get(choice - 1);
                System.out.println("1. Accept");
                System.out.println("2. Remove");
                System.out.print("Choose an action: ");
                int action = scanner.nextInt();
                switch (action) {
                    case 1:
                        currentUser.acceptFriendRequest(selectedFriend);
                        System.out.println(selectedFriend.getUsername() + " is now your friend.");
                        break;
                    case 2:
                        currentUser.removeFriendRequest(selectedFriend);
                        System.out.println(selectedFriend.getUsername() + "'s request removed.");
                        break;
                    default:
                        System.out.println("Invalid action. Please try again.");
                }
            }
        }
    }
}
