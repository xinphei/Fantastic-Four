package assignmentds;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class ViewEvent {
    //private static List<Event> registeredEvents = new ArrayList<>(); // New list to store registered events
    private static Scanner scanner = new Scanner(System.in);

    public static void main(User user) {
        // Initialize events
        Event.initializeEvents();
        displayLiveEvents();
        displayClosestUpcomingEvents();
        
        // Only student (role 1) can register for events
        if (user.getRole() == 1) {
            registerForEvents(user); 
            viewRegisteredEvents(user);
        } else {
            System.out.println();
        }

        Home.main(user);
    }
    

    private static void displayLiveEvents() {
        // ANSI escape codes for color
        String reset = "\u001B[0m";
        String blue = "\u001B[34m";

        // Blue color ASCII art for Live Event
        System.out.println(blue + "                                    _               _____          _               _ \n" +
                blue + "  /\\  /\\__ _ _ __  _ __   ___ _ __ (_)_ __   __ _  /__   \\___   __| | __ _ _   _  / \\\n" +
                blue + " / /_/ / _` | '_ \\| '_ \\ / _ \\ '_ \\| | '_ \\ / _` |   / /\\/ _ \\ / _` |/ _` | | | |/  /\n" +
                blue + "/ __  / (_| | |_) | |_) |  __/ | | | | | | | (_| |  / / | (_) | (_| | (_| | |_| /\\_/ \n" +
                blue + "\\/ /_/ \\__,_| .__/| .__/ \\___|_| |_|_|_| |_|\\__, |  \\/   \\___/ \\__,_|\\__,_|\\__, \\/   \n" +
                blue + "            |_|   |_|                       |___/                          |___/   " + reset);

        List<Event> ongoingEvents = DBOperations.getOngoingEvents();
        if (ongoingEvents.isEmpty()) {
            System.out.println("\nOopsie, there are no events today.");
        } else {
            for (Event event : ongoingEvents) {
                System.out.println(event);
            }
        }
    }


    private static void displayClosestUpcomingEvents() {
        // ANSI escape codes for color
        String reset = "\u001B[0m";
        String blue = "\u001B[34m";
        String cyan = "\u001B[36m";
        int count = 0;
        System.out.println(blue + "                                 _                 __                 _         \n" +
                blue + " /\\ /\\ _ __   ___ ___  _ __ ___ (_)_ __   __ _    /__\\_   _____ _ __ | |_ ___ _ \n" +
                blue + "/ / \\ \\ '_ \\ / __/ _ \\| '_ ` _ \\| | '_ \\ / _` |  /_\\ \\ \\ / / _ \\ '_ \\| __/ __(_)\n" +
                blue + "\\ \\_/ / |_) | (_| (_) | | | | | | | | | | (_| | //__  \\ V /  __/ | | | |_\\__ \\_ \n" +
                blue + " \\___/| .__/ \\___\\___/|_| |_| |_|_|_| |_|\\__, | \\__/   \\_/ \\___|_| |_|\\__|___(_)\n" +
                blue + "      |_|                                |___/                                  \n" + reset);
        List<Event> upcomingEvents = DBOperations.getUpcomingEvents();
        if (upcomingEvents.isEmpty()) {
            System.out.println("No upcoming events found.");
        } else {
            for (Event event : upcomingEvents){
                if (count == 0) {
                    System.out.println(cyan + "   ___                             _        _    \n" +
                            cyan +  "  | __|   __ __    ___    _ _     | |_     / |   \n" +
                            cyan + "  | _|    \\ V /   / -_)  | ' \\    |  _|    | |   \n" +
                            cyan + "  |___|   _\\_/_   \\___|  |_||_|   _\\__|   _|_|_  \n" + 
                            cyan + "_|\"\"\"\"\"|_|\"\"\"\"\"|_|\"\"\"\"\"|_|\"\"\"\"\"|_|\"\"\"\"\"|_|\"\"\"\"\"| \n" +
                            cyan + "\"`-0-0-'\"`-0-0-'\"`-0-0-'\"`-0-0-'\"`-0-0-'\"`-0-0-' \n" + reset);

                } else if (count == 1) {
                    System.out.println(cyan + "   ___                             _       ___   \n" +
                            cyan + "  | __|   __ __    ___    _ _     | |_    |_  )  \n" +
                            cyan + "  | _|    \\ V /   / -_)  | ' \\    |  _|    / /   \n" +
                            cyan + "  |___|   _\\_/_   \\___|  |_||_|   _\\__|   /___|  \n" +
                            cyan + "_|\"\"\"\"\"|_|\"\"\"\"\"|_|\"\"\"\"\"|_|\"\"\"\"\"|_|\"\"\"\"\"|_|\"\"\"\"\"| \n" +
                            cyan + "\"`-0-0-'\"`-0-0-'\"`-0-0-'\"`-0-0-'\"`-0-0-'\"`-0-0-' \n"  + reset);

                } else if (count == 2) {
                    System.out.println(cyan + "   ___                             _       ____  \n" +
                            cyan + "  | __|   __ __    ___    _ _     | |_    |__ /  \n" +
                            cyan + "  | _|    \\ V /   / -_)  | ' \\    |  _|    |_ \\  \n" +
                            cyan + "  |___|   _\\_/_   \\___|  |_||_|   _\\__|   |___/  \n" +
                            cyan + "_|\"\"\"\"\"|_|\"\"\"\"\"|_|\"\"\"\"\"|_|\"\"\"\"\"|_|\"\"\"\"\"|_|\"\"\"\"\"| \n" +
                            cyan + "\"`-0-0-'\"`-0-0-'\"`-0-0-'\"`-0-0-'\"`-0-0-'\"`-0-0-' \n"  + reset);

                } if (event.getDate().equals(LocalDate.of(2024,12,31))) {
                    System.out.println(event);
                    System.out.println(blue + "                                       _                                \n" +
                            blue + " |  _  __ _|_   _ \\  / _   _ _|_   _ _|_  _|_ |_   _  \\  / _   _   _  | \n" +
                            blue + " | (_| _>  |_  (/_ \\/ (/_ | | |_  (_) |    |_ | | (/_  \\/ (/_ (_| |   o \n" +
                            blue + "                                                       /                " + reset);
                    break;
                }
                System.out.println(event);
                count++;
                if (count > 2) {
                    break;
                }
            }
        }
    }

    private static void registerForEvents(User user) {
        String reset = "\u001B[0m";
        String cyan = "\u001B[36m";
        
        if (user.getRole() != 1) {
            System.out.println("Access denied. Only student can register for events.");
            return;
        }

        boolean continueRegistration = true;

        while (continueRegistration) {
            System.out.println(cyan + "\nWould you like to register for an event? (Y/N)" + reset);
            String choice = scanner.nextLine().toUpperCase();

            if (choice.equals("Y")) {
                registerForEvent(user);
            } else if (choice.equals("N")) {
                continueRegistration = false;
            } else {
                System.out.println("Invalid choice. Please enter 'Y' or 'N'.");
            }
        }
    }


    private static void registerForEvent(User user) {
        String green = "\u001B[32m";
        String reset = "\u001B[0m";
        String cyan = "\u001B[36m";
        
        int update = user.getCurrentPoints();
        System.out.println(cyan + "Enter the event title you want to register for:");
        String eventTitle = scanner.nextLine();

        Event selectedEvent = findEventByTitle(eventTitle);

        if (selectedEvent != null) {
            if (hasConflict(user, selectedEvent)) {
                System.out.println("Error: You have another event registered on the same day.");
            } else {
                boolean registrationSuccess = DBOperations.registerForEvent(user.getUsername(), 
                        selectedEvent.getTitle(),
                        selectedEvent.getDate().toString(), 
                        selectedEvent.getStartTime().toString(), 
                        selectedEvent.getEndTime().toString());
                if (registrationSuccess) {
                    //**user.setCurrentPoints(user.getCurrentPoints() + 5);
                    System.out.println("You have been awarded 5 marks.");
                    update += 5;
                    Timestamp now = Timestamp.valueOf(LocalDateTime.now());

                    // Update points in the database
                    DBOperations.updateCurrentPoints(user.getEmail(), update, now);
                    user.setCurrentPoints(update);
                    System.out.println("Your existing points: " + green + update + reset);
                    System.out.println("Successfully registered: " + selectedEvent.getTitle());
                }
            }
        } else {
            System.out.println("Event not found.");
        }
    }

    private static Event findEventByTitle(String eventTitle) {
        List<Event> allEvents = new ArrayList<>();
        allEvents.addAll(DBOperations.getOngoingEvents());
        allEvents.addAll(DBOperations.getUpcomingEvents());
        for (Event event : allEvents) {
            if (event.getTitle().equalsIgnoreCase(eventTitle)) {
                return event;
            }
        }
        return null;
    }

    private static boolean hasConflict(User user, Event selectedEvent) {
        // Check the registeredEvents list for conflicts
        List<Event> registeredEvents = DBOperations.getRegisteredEvents(user.getUsername());
        for (Event event : registeredEvents) {
            if (event.getDate().isEqual(selectedEvent.getDate())) {
                return true;
            }
        }
        return false;
    }

    private static void viewRegisteredEvents(User user) {
        String reset = "\u001B[0m";
        String magenta = "\u001B[35m";
        System.out.println(magenta + " __                              __       _ _           __            _     _                    _   _ \n" +
                magenta + "/ _\\_   _  ___ ___ ___  ___ ___ / _|_   _| | |_   _    /__\\ ___  __ _(_)___| |_ ___ _ __ ___  __| | / \\\n" +
                magenta + "\\ \\| | | |/ __/ __/ _ \\/ __/ __| |_| | | | | | | | |  / \\/// _ \\/ _` | / __| __/ _ \\ '__/ _ \\/ _` |/  /\n" +
                magenta + "_\\ \\ |_| | (_| (_|  __/\\__ \\__ \\  _| |_| | | | |_| | / _  \\  __/ (_| | \\__ \\ ||  __/ | |  __/ (_| /\\_/ \n" +
                magenta + "\\__/\\__,_|\\___\\___\\___||___/___/_|  \\__,_|_|_|\\__, | \\/ \\_/\\___|\\__, |_|___/\\__\\___|_|  \\___|\\__,_\\/   \n" +
                magenta + "                                              |___/             |___/                                  " + reset);
        List<Event> registeredEvents = DBOperations.getRegisteredEvents(user.getUsername());
        if (registeredEvents.isEmpty()) {
            System.out.println("No events registered by parents.");         
        } else {
            for (Event event : registeredEvents) {
                System.out.println(event);
            }
        }
    }
}