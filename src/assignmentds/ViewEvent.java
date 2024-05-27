package assignmentds;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class ViewEvent {
    private static List<Event> events = Event.getEvents();
    private static List<Event> registeredEvents = new ArrayList<>(); // New list to store registered events
    private static int points = 0;
    private static Scanner scanner = new Scanner(System.in);
    private static LocalDate currentDate = LocalDate.of(2024, 5, 18); // Current date

    public static void main(User user) {
    // Initialize events
    Event.initializeEvents();

    // Display live events and closest upcoming events regardless of user role
    if (currentDate.equals(LocalDate.of(2024,12,31))) {
        displayLiveEvents();
    } else {
        displayLiveEvents();
        displayClosestUpcomingEvents();
    }

    // Only parents (role 2) can register for events
    if (user.getRole() == 2) {
        registerForEvents(user); // Only parents can access register for event page
        // View registered events should only be accessible to parents who have registered events
        viewRegisteredEvents(user);
    } else {
        System.out.println();
    }

    // Proceed to home page
    Home.main(user);
}




    private static void displayLiveEvents() {
        //Live Event
        System.out.println("                                    _               _____          _               _ \n" +
                "  /\\  /\\__ _ _ __  _ __   ___ _ __ (_)_ __   __ _  /__   \\___   __| | __ _ _   _  / \\\n" +
                " / /_/ / _` | '_ \\| '_ \\ / _ \\ '_ \\| | '_ \\ / _` |   / /\\/ _ \\ / _` |/ _` | | | |/  /\n" +
                "/ __  / (_| | |_) | |_) |  __/ | | | | | | | (_| |  / / | (_) | (_| | (_| | |_| /\\_/ \n" +
                "\\/ /_/ \\__,_| .__/| .__/ \\___|_| |_|_|_| |_|\\__, |  \\/   \\___/ \\__,_|\\__,_|\\__, \\/   \n" +
                "            |_|   |_|                       |___/                          |___/   \n");

        boolean eventFound = false;
        for (Event event : events) {
             if (event.getDate().isEqual(currentDate)) {
                System.out.println(event);
                eventFound = true;
            }
        }

        if (!eventFound) {
            System.out.println("\nOopsie, there are no events today.");
        }
    }

    private static void displayClosestUpcomingEvents() {
        int count = 0;
        System.out.println("                                 _                 __                 _         \n" +
                " /\\ /\\ _ __   ___ ___  _ __ ___ (_)_ __   __ _    /__\\_   _____ _ __ | |_ ___ _ \n" +
                "/ / \\ \\ '_ \\ / __/ _ \\| '_ ` _ \\| | '_ \\ / _` |  /_\\ \\ \\ / / _ \\ '_ \\| __/ __(_)\n" +
                "\\ \\_/ / |_) | (_| (_) | | | | | | | | | | (_| | //__  \\ V /  __/ | | | |_\\__ \\_ \n" +
                " \\___/| .__/ \\___\\___/|_| |_| |_|_|_| |_|\\__, | \\__/   \\_/ \\___|_| |_|\\__|___(_)\n" +
                "      |_|                                |___/                                  \n");
        for (Event event : events) {
            if (event.getDate().isAfter(currentDate)) {
                if (count == 0) {
                    System.out.println("   ___                             _        _    \n" +
                            "  | __|   __ __    ___    _ _     | |_     / |   \n" +
                            "  | _|    \\ V /   / -_)  | ' \\    |  _|    | |   \n" +
                            "  |___|   _\\_/_   \\___|  |_||_|   _\\__|   _|_|_  \n" + "_|\"\"\"\"\"|_|\"\"\"\"\"|_|\"\"\"\"\"|_|\"\"\"\"\"|_|\"\"\"\"\"|_|\"\"\"\"\"| \n" +
                            "\"`-0-0-'\"`-0-0-'\"`-0-0-'\"`-0-0-'\"`-0-0-'\"`-0-0-' \n");

                } else if (count == 1) {
                    System.out.println("   ___                             _       ___   \n" +
                            "  | __|   __ __    ___    _ _     | |_    |_  )  \n" +
                            "  | _|    \\ V /   / -_)  | ' \\    |  _|    / /   \n" +
                            "  |___|   _\\_/_   \\___|  |_||_|   _\\__|   /___|  \n" +
                            "_|\"\"\"\"\"|_|\"\"\"\"\"|_|\"\"\"\"\"|_|\"\"\"\"\"|_|\"\"\"\"\"|_|\"\"\"\"\"| \n" +
                            "\"`-0-0-'\"`-0-0-'\"`-0-0-'\"`-0-0-'\"`-0-0-'\"`-0-0-' \n");

                } else if (count == 2) {
                    System.out.println("   ___                             _       ____  \n" +
                            "  | __|   __ __    ___    _ _     | |_    |__ /  \n" +
                            "  | _|    \\ V /   / -_)  | ' \\    |  _|    |_ \\  \n" +
                            "  |___|   _\\_/_   \\___|  |_||_|   _\\__|   |___/  \n" +
                            "_|\"\"\"\"\"|_|\"\"\"\"\"|_|\"\"\"\"\"|_|\"\"\"\"\"|_|\"\"\"\"\"|_|\"\"\"\"\"| \n" +
                            "\"`-0-0-'\"`-0-0-'\"`-0-0-'\"`-0-0-'\"`-0-0-'\"`-0-0-' \n");

                } if (event.getDate().equals(LocalDate.of(2024,12,31))) {
                    System.out.println(event);
                    System.out.println("                                       _                                \n" +
                            " |  _  __ _|_   _ \\  / _   _ _|_   _ _|_  _|_ |_   _  \\  / _   _   _  | \n" +
                            " | (_| _>  |_  (/_ \\/ (/_ | | |_  (_) |    |_ | | (/_  \\/ (/_ (_| |   o \n" +
                            "                                                       /                ");
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
    // Only parents (role 2) can register for events
    if (user.getRole() != 2) {
        System.out.println("Access denied. Only parents can register for events.");
        return;
    }

    boolean continueRegistration = true;

    while (continueRegistration) {
        System.out.println("\nWould you like to register for an event? (Y/N)");
        String choice = scanner.nextLine().toUpperCase();

        if (choice.equals("Y")) {
            registerForEvent();
        } else if (choice.equals("N")) {
            continueRegistration = false;
        } else {
            System.out.println("Invalid choice. Please enter 'Y' or 'N'.");
        }
    }
}


    private static void registerForEvent() {
        System.out.println("Enter the event title you want to register for:");
        String eventTitle = scanner.nextLine();

        Event selectedEvent = findEventByTitle(eventTitle);

        if (selectedEvent != null) {
            if (hasConflict(selectedEvent)) {
                System.out.println("Error: You have another event registered on the same day.");
            } else {
                points += 5;
                registeredEvents.add(selectedEvent); // Add the event to the list of registered events
                System.out.println("Successfully registered: " + selectedEvent.getTitle());
            }
        } else {
            System.out.println("Event not found.");
        }
    }

    private static Event findEventByTitle(String eventTitle) {
        for (Event event : events) {
            if (event.getTitle().equalsIgnoreCase(eventTitle)) {
                return event;
            }
        }
        return null;
    }

    private static boolean hasConflict(Event selectedEvent) {
        for (Event event : registeredEvents) {
            if (event.getDate().isEqual(selectedEvent.getDate())) {
                return true;
            }
        }
        return false;
    }

    private static void viewRegisteredEvents(User user) {
    // Check if the user is a parent (role 2) and if they have registered events
    if (user.getRole() == 2 && !registeredEvents.isEmpty()) {
        System.out.println(" __                              __       _ _           __            _     _                    _   _ \n" +
                "/ _\\_   _  ___ ___ ___  ___ ___ / _|_   _| | |_   _    /__\\ ___  __ _(_)___| |_ ___ _ __ ___  __| | / \\\n" +
                "\\ \\| | | |/ __/ __/ _ \\/ __/ __| |_| | | | | | | | |  / \\/// _ \\/ _` | / __| __/ _ \\ '__/ _ \\/ _` |/  /\n" +
                "_\\ \\ |_| | (_| (_|  __/\\__ \\__ \\  _| |_| | | | |_| | / _  \\  __/ (_| | \\__ \\ ||  __/ | |  __/ (_| /\\_/ \n" +
                "\\__/\\__,_|\\___\\___\\___||___/___/_|  \\__,_|_|_|\\__, | \\/ \\_/\\___|\\__, |_|___/\\__\\___|_|  \\___|\\__,_\\/   \n" +
                "                                              |___/             |___/                                  ");

        System.out.println("Total points gained: " + points);
        for (Event event : registeredEvents) {
            System.out.println(event);
        }
    } else {
        System.out.println("No events registered or access denied. Only parents who successfully registered an event can view registered events.");
    }
}

}