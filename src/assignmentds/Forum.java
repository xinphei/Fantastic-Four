package assignmentds;

import java.io.*;
import java.sql.Timestamp;
import java.util.Scanner;

public class Forum {

    private static final String FILE_PATH = "forum_post.txt";
    private static final int MAX_FORUM_SIZE = 200;

    public static void main(String [] args) {
        
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
                        forumSize = addPost(forumPosts, forumSize, scanner/*, user*/);
                        break;
                    case 3:
                        System.out.println("Exiting the forum. Goodbye!");
                        saveForumData(forumPosts, forumSize);
                        //Home.main(user);
                        System.exit(0);
                    default:
                        System.out.println("Invalid choice. Please enter a valid option.");
                }
            }
            else {
                System.out.println("Invalid choice. Please enter a valid option.");
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
                forumPosts[forumSize++] = new DiscussionPost(id, /*username,*/ timestamp, message);
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
        String red = "\u001B[31m";
        String headTitle="___    ____ _____   __  __ __  _____ _____ ____  ___   ____       _____   ___   ____  __ __  ___ ___ \n" +
        "|   \\  |    / ___/  /  ]|  |  |/ ___// ___/|    |/   \\ |    \\     |     | /   \\ |    \\|  |  ||   |   |\n" +
        "|    \\  |  (   \\_  /  / |  |  (   \\_(   \\_  |  ||     ||  _  |    |   __||     ||  D  )  |  || _   _ |\n" +
        "|  D  | |  |\\__  |/  /  |  |  |\\__  |\\__  | |  ||  O  ||  |  |    |  |_  |  O  ||    /|  |  ||  \\_/  |\n" +
        "|     | |  |/  \\ /   \\_ |  :  |/  \\ |/  \\ | |  ||     ||  |  |    |   _] |     ||    \\|  :  ||   |   |\n" +
        "|     | |  |\\    \\     ||     |\\    |\\    | |  ||     ||  |  |    |  |   |     ||  .  \\     ||   |   |\n" +
        "|_____||____|\\___|\\____| \\__,_| \\___| \\___||____|\\___/ |__|__|    |__|    \\___/ |__|\\_|\\__,_||___|___|\n" +
        "                                                                                                      ";
        System.out.println("\n******************************************************************************************************************************");
        System.out.println(red + headTitle + reset);
        System.out.println("******************************************************************************************************************************");
        System.out.println("1. View Discussion");
        System.out.println("2. Add Post");
        System.out.println("3. Exit");
        System.out.println("-------------------------------------------");
        System.out.print("Enter your choice: ");
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

    private static int addPost(DiscussionPost[] forumPosts, int forumSize, Scanner scanner/*, User user*/) {
        //String username = user.getUsername();
        //System.out.print("Username: " + username);

        System.out.print("\nEnter your message: ");
        String message = scanner.nextLine(); // Consume the newline character
        message = scanner.nextLine(); // Read the actual message

        forumPosts[forumSize++] = new DiscussionPost(forumSize + 1/*, username*/, new Timestamp(System.currentTimeMillis()), message);

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
        //private String username;
        private Timestamp timestamp;
        private String message;

        public DiscussionPost(int id/*, String username*/, Timestamp timestamp, String message) {
            this.id = id;
            //this.username = username;
            this.timestamp = timestamp;
            this.message = message;
        }

        @Override
        public String toString() {
            return "ID: " + id + "\nUsername: " + /*username + */"\nTimestamp: " + timestamp + "\nMessage: " + message;
        }

        public String toFileString() {
            return id + "," + /*username + */"," + timestamp + "," + message;
        }
    }
}
