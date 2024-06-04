package assignmentds;

import java.io.*;
import java.sql.Timestamp;
import java.util.Scanner;
import assignmentds.User;

public class Forum {

    private static final String FILE_PATH = "forum_post.txt";
    private static final int MAX_FORUM_SIZE = 200;

    public static void main(User user) {
        
        DiscussionPost[] forumPosts = new DiscussionPost[MAX_FORUM_SIZE];
        int forumSize = loadForumData(forumPosts);

        Scanner scanner = new Scanner(System.in);

        while (true) {
            printForumMenu();
            int choice;

            // Ensure the input is an integer
            while (!scanner.hasNextInt()) {
                System.out.println("Invalid input. Please enter a valid option.");
                System.out.print("Choice: ");
                scanner.next(); // Consume the invalid input
            }

            choice = scanner.nextInt();

            if (choice >= 1 && choice <= 3) {
                switch (choice) {
                    case 1:
                        viewForum(forumPosts, forumSize);
                        break;
                    case 2:
                        forumSize = addPost(forumPosts, forumSize, scanner, user);
                        break;
                    case 3:
                        System.out.println("Exiting the forum. Goodbye!");
                        saveForumData(forumPosts, forumSize);
                        Home.main(user);
                        System.exit(0);
                    default:
                        System.out.println("Invalid choice. Please enter a valid option.");
                }
            }
        }
    }

    private static int loadForumData(DiscussionPost[] forumPosts) {
        int forumSize = 0;
        try (BufferedReader reader = new BufferedReader(new FileReader(FILE_PATH))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                int id = Integer.parseInt(parts[0]);
                String username = parts[1];
                Timestamp timestamp = Timestamp.valueOf(parts[2]);
                String message = parts[3];
                forumPosts[forumSize++] = new DiscussionPost(id, username, timestamp, message);
            }
        }
        catch (IOException | NumberFormatException e) {
            e.printStackTrace();
        }
        return forumSize;
    }

    private static void saveForumData(DiscussionPost[] forumPosts, int forumSize) {
        try (PrintWriter writer = new PrintWriter(new FileWriter(FILE_PATH))) {
            for (int i = 0; i < forumSize; i++) {
                writer.println(forumPosts[i].toFileString());
            }
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void printForumMenu() {
        String reset = "\u001B[0m";
        String blue = "\u001B[34m";
        String cyan = "\u001B[36m";
        System.out.println(blue + "    ___ _                        _                 ___                          \n" +
                           blue + "   /   (_)___  ___ _   _ ___ ___(_) ___  _ __     / __\\__  _ __ _   _ _ __ ___  \n" +
                           blue + "  / /\\ / / __|/ __| | | / __/ __| |/ _ \\| '_ \\   / _\\/ _ \\| '__| | | | '_ ` _ \\ \n" +
                           blue + " / /_//| \\__ \\ (__| |_| \\__ \\__ \\ | (_) | | | | / / | (_) | |  | |_| | | | | | |\n" +
                           blue + "/___,' |_|___/\\___|\\__,_|___/___/_|\\___/|_| |_| \\/   \\___/|_|   \\__,_|_| |_| |_|\n" +
                           blue + "                                                                                ");
        System.out.println("1. View Discussion");
        System.out.println("2. Add Post");
        System.out.println("3. Exit");
        System.out.println("-------------------------------------------");
        System.out.print(cyan + "Enter your choice: " + reset);
    }

    private static void viewForum(DiscussionPost[] forumPosts, int forumSize) {
        System.out.println("Discussion Forum\n");

        if (forumSize == 0) {
            System.out.println("No posts yet. Be the first to contribute!");
        }
        else {
            for (int i = 0; i < forumSize; i++) {
                System.out.println(forumPosts[i]);
                System.out.println("-------------------------------------------");
            }
        }

        System.out.println();
    }

    private static int addPost(DiscussionPost[] forumPosts, int forumSize, Scanner scanner, User user) {
        String reset = "\u001B[0m";
        String blue = "\u001B[34m";
        String cyan = "\u001B[36m";
        String username = user.getUsername();
        System.out.print("Username: " + username);

        System.out.print(cyan + "\nEnter your message: " + reset);
        String message = scanner.nextLine(); // Consume the newline character
        message = scanner.nextLine(); // Read the actual message

        forumPosts[forumSize++] = new DiscussionPost(forumSize + 1, username, new Timestamp(System.currentTimeMillis()), message);

        if (forumSize == forumPosts.length) {
            System.out.println("Forum is full. Your post has been added, but consider removing old posts.");
        }
        else {
            System.out.println("Your post has been added to the forum.\n");
        }

        return forumSize;
    }

    static class DiscussionPost {

        private int id;
        private String username;
        private Timestamp timestamp;
        private String message;

        public DiscussionPost(int id, String username, Timestamp timestamp, String message) {
            this.id = id;
            this.username = username;
            this.timestamp = timestamp;
            this.message = message;
        }

        @Override
        public String toString() {
            return "ID: " + id + "\nUsername: " + username + "\nTimestamp: " + timestamp + "\nMessage: " + message;
        }

        public String toFileString() {
            return id + "," + username + "," + timestamp + "," + message;
        }
    }
}
