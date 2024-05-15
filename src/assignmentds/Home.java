package assignmentds;

import assignmentds.User;
import java.util.Scanner;

public class Home {

    public static void main(User user) {
        String reset = "\u001B[0m";
        String green = "\u001B[32m";
        System.out.println(green+"  _   _     U  ___ u  __  __  U _____ u  ____       _       ____  U _____ u \n" +
        " |'| |'|     \\/\"_ \\/U|' \\/ '|u\\| ___\"|/U|  _\"\\ uU  /\"\\  uU /\"___|u\\| ___\"|/ \n" +
        "/| |_| |\\    | | | |\\| |\\/| |/ |  _|\"  \\| |_) |/ \\/ _ \\/ \\| |  _ / |  _|\"   \n" +
        "U|  _  |u.-,_| |_| | | |  | |  | |___   |  __/   / ___ \\  | |_| |  | |___   \n" +
        " |_| |_|  \\_)-\\___/  |_|  |_|  |_____|  |_|     /_/   \\_\\  \\____|  |_____|  \n" +
        " //   \\\\       \\\\   <<,-,,-.   <<   >>  ||>>_    \\\\    >>  _)(|_   <<   >>  \n" +
        "(_\") (\"_)     (__)   (./  \\.) (__) (__)(__)__)  (__)  (__)(__)__) (__) (__) "+reset);


        boolean isValidResponse = false;
        char choice;
        Scanner scanner = new Scanner(System.in);

        do {
            System.out.println("\n^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^");
            System.out.println("***ACCESSIBLE TO ALL***");
            System.out.println("1. View Profile");
            System.out.println("2. View Event");
            System.out.println("3. Forum");
            
            
            if (user.getRole() == 1) {
                System.out.println("***ONLY ACCESSIBLE TO YOUNG STUDENTS***");
                System.out.println("4. Attempt Quiz");
                System.out.println("5. Global Leaderboard");
                System.out.println("6. Friend Requests");
            }

            if (user.getRole() == 2) {
                System.out.println("***ONLY ACCESSIBLE TO PARENTS***");
                System.out.println("7. Booking An Event");
            }

            if (user.getRole() == 3) {
                System.out.println("***ONLY ACCESSIBLE TO EDUCATORS***");
                System.out.println("8. Create an Event");
                System.out.println("9. Create a Quiz");
            }

            
            // others
            System.out.println("0. Log Out");
            System.out.println("^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^");

            System.out.print("Choice: ");
            choice = scanner.next().charAt(0);

            
            switch (choice) {
                case '1': //view profile
                    isValidResponse = true;
                    ViewProfile.main(user);
                    break;
                    
                case '2': //view event
                    isValidResponse = true;
                    ViewEvent.main(new String[0]);
                    break;
                    
                case '3': //forum
                    isValidResponse = true;
                    Forum.main(new String[0]);
                    break;
                    
                case '4': //attempt quiz
                    if (user.getRole() == 1) {
                        isValidResponse = true;
                        AttemptQuiz.main(user);
                    } else {
                        System.out.println("Access denied. This feature is only accessible to young students.");
                    }
                    break;
                    
                case '5': //global leaderboard
                    if (user.getRole() == 1) {
                        isValidResponse = true;
                        GlobalLeaderboard.main(new String[0]);
                    } else {
                        System.out.println("Access denied. This feature is only accessible to young students.");
                    }
                    break;
                    
                case '6': //friend request
                    if (user.getRole() == 1) {
                        isValidResponse = true;
                        FriendRequest.main(user);
                    } else {
                        System.out.println("Access denied. This feature is only accessible to young students.");
                    }
                    break;
                    
                case '7': //book event
                    if (user.getRole() == 2) {
                        isValidResponse = true;
                        BookingSystem.main(new String[0]);
                    } else {
                        System.out.println("Access denied. This feature is only accessible to parents.");
                    }
                    break;
                    
                case '8': //create event
                    if (user.getRole() == 3) {
                        isValidResponse = true;
                        CreateEvent.main(new String[0]);
                    } else {
                        System.out.println("Access denied. This feature is only accessible to educators.");
                    }
                    break;
                    
                case '9': //create quiz
                    if (user.getRole() == 3) {
                        isValidResponse = true;
                        CreateQuiz.main(new String[0]);
                    } else {
                        System.out.println("Access denied. This feature is only accessible to educators.");
                    }
                    break;
                    
                //others (logout)
                case '0':
                    isValidResponse = true;
                    System.out.println("\n /$$$$$$$$ /$$   /$$  /$$$$$$  /$$   /$$ /$$   /$$       /$$     /$$ /$$$$$$  /$$   /$$               ");
                    System.out.println("|__  $$__/| $$  | $$ /$$__  $$| $$$ | $$| $$  /$$/     |  $$   /$$//$$__  $$| $$  | $$               ");
                    System.out.println("   | $$   | $$  | $$| $$  \\ $$| $$$$| $$| $$ /$$/      \\  $$ /$$/| $$    \\ $$| $$  | $$      ");
                    System.out.println("   | $$   | $$$$$$$$| $$$$$$$$| $$ $$ $$| $$$$$/         \\  $$$$/ | $$  | $$| $$  | $$            ");
                    System.out.println("   | $$   | $$__  $$| $$__  $$| $$  $$$$| $$  $$           \\  $$/  |$$  | $$| $$  | $$            ");
                    System.out.println("   | $$   | $$  | $$| $$  | $$| $$ \\ $$$| $$ \\ $$           | $$   |$$  | $$| $$  | $$         ");
                    System.out.println("   | $$   | $$  | $$| $$  | $$| $$  \\ $$| $$  \\ $$          | $$   |$$$$$$/|  $$$$$$/         ");
                    System.out.println("   |__/   |__/  |__/|__/  |__/|__/   \\__/|__/  \\__/          |__/   \\______/    \\______/    ");
                    System.out.println("                                      Wish you have a nice day                                                         ");
                    System.out.println("                                                                                                      ");
                    System.out.println("<<<<<<<<<<<<<<<<<<<<<<<<<<<<Logged out... See you again!>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>");
                    
                    break;
                    
                default:
                    System.out.println("Invalid input. Please try again.");
            }
        }
        while (!isValidResponse);
    }
}
