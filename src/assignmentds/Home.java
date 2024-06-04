package assignmentds;

import java.util.Scanner;

public class Home {

    public static void main(User user) {
        
        displayWelcomeMessage();

        boolean isValidResponse = false;
        char choice;
        Scanner scanner = new Scanner(System.in);

        do {
            String reset = "\u001B[0m";
            String cyan = "\u001B[36m";
            displayMenu(user);
            System.out.print(cyan + "Choice: " + reset);
            choice = scanner.next().charAt(0);

            switch (choice) {
                case '1': // view profile
                    isValidResponse = true;
                    ViewProfile.main(user);
                    break;
                    
                case '2': // view event
                    isValidResponse = true;
                    ViewEvent.main(user);
                    break;
                    
                case '3': // forum
                    isValidResponse = true;
                    Forum.main(user);
                    break;
                    
                case '4': // attempt quiz
                    if (user.getRole() == 1) {
                        isValidResponse = true;
                        AttemptQuiz.main(user);
                    } else {
                        System.out.println("Access denied. This feature is only accessible to young students.");
                    }
                    break;
                    
                case '5': // global leaderboard
                    if (user.getRole() == 1) {
                        isValidResponse = true;
                        GlobalLeaderboard.main(user);
                    } else {
                        System.out.println("Access denied. This feature is only accessible to young students.");
                    }
                    break;
                    
                case '6': // friend request
                    if (user.getRole() == 1) {
                        isValidResponse = true;
                        FriendRequest.main(user);
                    } else {
                        System.out.println("Access denied. This feature is only accessible to young students.");
                    }
                    break;
                    
                case '7' : //book a tour
                    if (user.getRole() == 2) {
                        isValidResponse = true;
                        BookingSystem.main(user);
                    } else {
                        System.out.println("Access denied. This feature is only accessible for parents.");
                    }
                    break;
                
                case '8': // create event
                    if (user.getRole() == 3) {
                        isValidResponse = true;
                        CreateEvent.main(user);
                    } else {
                        System.out.println("Access denied. This feature is only accessible to educators.");
                    }
                    break;
                    
                case '9': // create quiz
                    if (user.getRole() == 3) {
                        isValidResponse = true;
                        CreateQuiz.main(user);
                    } else {
                        System.out.println("Access denied. This feature is only accessible to educators.");
                    }
                    break;
   
                case '0': // logout
                    isValidResponse = true;
                    System.out.println("Logging out...");
                    String magenta = "\u001B[35m";
                    System.out.println(magenta + "  _                          _    ___       _       ___           __   __            _             _      _ \n" +
                           magenta + " | |   ___  __ _ __ _ ___ __| |  / _ \\ _  _| |_    / __| ___ ___  \\ \\ / /__ _  _    /_\\  __ _ __ _(_)_ _ | |\n" +
                           magenta + " | |__/ _ \\/ _` / _` / -_) _` | | (_) | || |  _|_  \\__ \\/ -_) -_)  \\ V / _ \\ || |  / _ \\/ _` / _` | | ' \\|_|\n" +
                           magenta + " |____\\___/\\__, \\__, \\___\\__,_|  \\___/ \\_,_|\\__(_) |___/\\___\\___|   |_|\\___/\\_,_| /_/ \\_\\__, \\__,_|_|_||_(_)\n" +
                           magenta + "           |___/|___/                                                                   |___/               ");
                    break;
                    
                default:
                    System.out.println("Invalid input. Please try again.");
            }
        } while (!isValidResponse);
    }

    private static void displayWelcomeMessage() {
        String reset = "\u001B[0m";
        String green = "\u001B[32m";
        System.out.println("");
        System.out.println(green + "  _    _  ____  __  __ ______      _____        _____ ______ \n" +
                green + " | |  | |/ __ \\|  \\/  |  ____|    |  __ \\ /\\   / ____|  ____|\n" +
                green + " | |__| | |  | | \\  / | |__       | |__) /  \\ | |  __| |__   \n" +
                green + " |  __  | |  | | |\\/| |  __|      |  ___/ /\\ \\| | |_ |  __|  \n" +
                green + " | |  | | |__| | |  | | |____     | |  / ____ \\ |__| | |____ \n" +
                green + " |_|  |_|\\____/|_|  |_|______|    |_| /_/    \\_\\_____|______|\n" +
                green + "                                                             ");
    }

    private static void displayMenu(User user) {
        String reset = "\u001B[0m";
        String blue = "\u001B[34m";
        System.out.println(blue + "    _                   _ _    _       _           _   _ _ \n" +
                blue + "   /_\\  __ __ ___ _____(_) |__| |___  | |_ ___    /_\\ | | |\n" +
                blue + "  / _ \\/ _/ _/ -_|_-<_-< | '_ \\ / -_) |  _/ _ \\  / _ \\| | |\n" +
                blue + " /_/ \\_\\__\\__\\___/__/__/_|_.__/_\\___|  \\__\\___/ /_/ \\_\\_|_|\n" +
                blue + "                                                           ");
        System.out.println("1. View Profile");
        System.out.println("2. View Event");
        System.out.println("3. Forum");

        if (user.getRole() == 1) {
            System.out.println(blue + "   ___       _          _                   _ _    _       _        __   __                    ___ _           _         _      \n" +
                blue + "  / _ \\ _ _ | |_  _    /_\\  __ __ ___ _____(_) |__| |___  | |_ ___  \\ \\ / /__ _  _ _ _  __ _  / __| |_ _  _ __| |___ _ _| |_ ___\n" +
                blue + " | (_) | ' \\| | || |  / _ \\/ _/ _/ -_|_-<_-< | '_ \\ / -_) |  _/ _ \\  \\ V / _ \\ || | ' \\/ _` | \\__ \\  _| || / _` / -_) ' \\  _(_-<\n" +
                blue + "  \\___/|_||_|_|\\_, | /_/ \\_\\__\\__\\___/__/__/_|_.__/_\\___|  \\__\\___/   |_|\\___/\\_,_|_||_\\__, | |___/\\__|\\_,_\\__,_\\___|_||_\\__/__/\n" +
                blue + "               |__/                                                                    |___/                                    ");
            System.out.println("4. Attempt Quiz");
            System.out.println("5. Global Leaderboard");
            System.out.println("6. Friend Requests");
        }

        if (user.getRole() == 2) {
            System.out.println(blue + "   ___       _          _                   _ _    _       _         ___                  _      \n" +
                blue + "  / _ \\ _ _ | |_  _    /_\\  __ __ ___ _____(_) |__| |___  | |_ ___  | _ \\__ _ _ _ ___ _ _| |_ ___\n" +
                blue + " | (_) | ' \\| | || |  / _ \\/ _/ _/ -_|_-<_-< | '_ \\ / -_) |  _/ _ \\ |  _/ _` | '_/ -_) ' \\  _(_-<\n" +
                blue + "  \\___/|_||_|_|\\_, | /_/ \\_\\__\\__\\___/__/__/_|_.__/_\\___|  \\__\\___/ |_| \\__,_|_| \\___|_||_\\__/__/\n" +
                blue + "               |__/                                                                              ");
            System.out.println("7. Booking A Tour"); 
        }

        if (user.getRole() == 3) {
            System.out.println(blue + "   ___       _          _                   _ _    _       _         ___    _              _              \n" +
                blue + "  / _ \\ _ _ | |_  _    /_\\  __ __ ___ _____(_) |__| |___  | |_ ___  | __|__| |_  _ __ __ _| |_ ___ _ _ ___\n" +
                blue + " | (_) | ' \\| | || |  / _ \\/ _/ _/ -_|_-<_-< | '_ \\ / -_) |  _/ _ \\ | _|/ _` | || / _/ _` |  _/ _ \\ '_(_-<\n" +
                blue + "  \\___/|_||_|_|\\_, | /_/ \\_\\__\\__\\___/__/__/_|_.__/_\\___|  \\__\\___/ |___\\__,_|\\_,_\\__\\__,_|\\__\\___/_| /__/\n" +
                blue + "               |__/                                                                                       ");
            System.out.println("8. Create an Event");
            System.out.println("9. Create a Quiz");
        }
        
        System.out.println();
        System.out.println("0. Log Out");
        System.out.println();
    }
}
