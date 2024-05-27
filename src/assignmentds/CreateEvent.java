package assignmentds;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeParseException;
import java.util.Scanner;

public class CreateEvent {
    
    private static int numEventsCreated = 0;

    public static void main(User user) {
        System.out.println("   ___               _           __                 _       ___                   \n" +
                "  / __\\ __ ___  __ _| |_ ___    /__\\_   _____ _ __ | |_    / _ \\__ _  __ _  ___ _ \n" +
                " / / | '__/ _ \\/ _` | __/ _ \\  /_\\ \\ \\ / / _ \\ '_ \\| __|  / /_)/ _` |/ _` |/ _ (_)\n" +
                "/ /__| | |  __/ (_| | ||  __/ //__  \\ V /  __/ | | | |_  / ___/ (_| | (_| |  __/_ \n" +
                "\\____/_|  \\___|\\__,_|\\__\\___| \\__/   \\_/ \\___|_| |_|\\__| \\/    \\__,_|\\__, |\\___(_)\n" +
                "                                                                     |___/        ");

        Event.initializeEvents();
        Scanner sc = new Scanner(System.in);
        boolean createAnotherEvent = true;

        while (createAnotherEvent) {
            String title = getTitle(sc);
            String description = getDescription(sc);
            String venue = getVenue(sc);
            LocalDate date = getDate(sc);
            LocalTime[] times = getTimes(sc);

            Event newEvent = new Event(title, description, venue, date, times[0], times[1]);
            Event.addEvent(newEvent);
            
            numEventsCreated++;

            System.out.println(" __                              __       _ _                             _           _   _ \n" +
                    "/ _\\_   _  ___ ___ ___  ___ ___ / _|_   _| | |_   _    ___ _ __ ___  __ _| |_ ___  __| | / \\\n" +
                    "\\ \\| | | |/ __/ __/ _ \\/ __/ __| |_| | | | | | | | |  / __| '__/ _ \\/ _` | __/ _ \\/ _` |/  /\n" +
                    "_\\ \\ |_| | (_| (_|  __/\\__ \\__ \\  _| |_| | | | |_| | | (__| | |  __/ (_| | ||  __/ (_| /\\_/ \n" +
                    "\\__/\\__,_|\\___\\___\\___||___/___/_|  \\__,_|_|_|\\__, |  \\___|_|  \\___|\\__,_|\\__\\___|\\__,_\\/   \n" +
                    "                                              |___/                                         \n");
            // Display the last added event
            System.out.println(Event.getEvents().get(Event.getEvents().size() - 1).toString());

            // Ask if the user wants to create another event
            System.out.println("Do you want to create another event? (yes/no): ");
            String input = sc.nextLine();
            createAnotherEvent = input.equalsIgnoreCase("yes");
        }
        // Write events to file after all events have been entered
        Event.writeEventsToFile("events.txt");
        sc.close();
    }
    
    public static int getNumEventsCreated() {
        return numEventsCreated;
    }

    private static String getTitle(Scanner sc) {
        System.out.println("What's the title of the event? ");
        return sc.nextLine();
    }

    private static String getDescription(Scanner sc) {
        System.out.println("What's the event about? ");
        return sc.nextLine();
    }

    private static String getVenue(Scanner sc) {
        System.out.println("Where will it be held? ");
        return sc.nextLine();
    }

    private static LocalDate getDate(Scanner sc) {
        LocalDate date = null;
        boolean validDate = false;
        while (!validDate) {
            try {
                System.out.println("When will it be held? (e.g. 2024-12-04) ");
                date = LocalDate.parse(sc.nextLine());
                validDate = true;
            } catch (DateTimeParseException e) {
                System.out.println("Invalid date format. Please enter the date in YYYY-MM-DD format.");
            }
        }
        return date;
    }

    private static LocalTime[] getTimes(Scanner sc) {
        LocalTime[] times = new LocalTime[2];
        boolean validTimes = false;
        while (!validTimes) {
            try {
                System.out.println("What time will it start and end? (e.g. 11:05-13:30) ");
                String[] timeParts = sc.nextLine().split("-");
                if (timeParts.length != 2) {
                    throw new IllegalArgumentException("Invalid time format. Please enter the time range in HH:MM-HH:MM format.");
                }
                times[0] = LocalTime.parse(timeParts[0]);
                times[1] = LocalTime.parse(timeParts[1]);
                validTimes = true;
            } catch (DateTimeParseException | IllegalArgumentException e) {
                System.out.println(e.getMessage());
            }
        }
        return times;
    }
}
