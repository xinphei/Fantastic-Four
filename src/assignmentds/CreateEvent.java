package assignmentds;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeParseException;
import java.util.Scanner;

public class CreateEvent {
    
    private static int numEventsCreated = 0;

    public static void main(User user) {
        String reset = "\u001B[0m";
        String blue = "\u001B[34m";
        String magenta = "\u001B[35m";
        System.out.println(blue + "   ___               _           __                 _       ___                   \n" +
                           blue + "  / __\\ __ ___  __ _| |_ ___    /__\\_   _____ _ __ | |_    / _ \\__ _  __ _  ___ _ \n" +
                           blue + " / / | '__/ _ \\/ _` | __/ _ \\  /_\\ \\ \\ / / _ \\ '_ \\| __|  / /_)/ _` |/ _` |/ _ (_)\n" +
                           blue + "/ /__| | |  __/ (_| | ||  __/ //__  \\ V /  __/ | | | |_  / ___/ (_| | (_| |  __/_ \n" +
                           blue + "\\____/_|  \\___|\\__,_|\\__\\___| \\__/   \\_/ \\___|_| |_|\\__| \\/    \\__,_|\\__, |\\___(_)\n" +
                           blue + "                                                                     |___/        ");

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
            boolean eventAdded = DBOperations.insertNewEvent(title, description, venue, date, times[0], times[1]);
            Event.addEvent(newEvent);
            
            numEventsCreated++;

            System.out.println(magenta + " __                              __       _ _                             _           _   _ \n" +
                    magenta + "/ _\\_   _  ___ ___ ___  ___ ___ / _|_   _| | |_   _    ___ _ __ ___  __ _| |_ ___  __| | / \\\n" +
                    magenta + "\\ \\| | | |/ __/ __/ _ \\/ __/ __| |_| | | | | | | | |  / __| '__/ _ \\/ _` | __/ _ \\/ _` |/  /\n" +
                    magenta + "_\\ \\ |_| | (_| (_|  __/\\__ \\__ \\  _| |_| | | | |_| | | (__| | |  __/ (_| | ||  __/ (_| /\\_/ \n" +
                    magenta + "\\__/\\__,_|\\___\\___\\___||___/___/_|  \\__,_|_|_|\\__, |  \\___|_|  \\___|\\__,_|\\__\\___|\\__,_\\/   \n" +
                    magenta + "                                              |___/                                         \n");
            // Display the last added event
            System.out.println(Event.getEvents().get(Event.getEvents().size() - 1).toString());

            // Ask if the user wants to create another event
            System.out.println(reset + "Do you want to create another event? (yes/no): " + reset);
            String input = sc.nextLine();
            createAnotherEvent = input.equalsIgnoreCase("yes");
        }

        updateEventNum(user.getUsername(), numEventsCreated, user.getRole());
        Home.main(user);
        sc.close();
    }
    
    public static int getNumEventsCreated(String username, int role) {
        return getEventNum(username, role);
    }

    private static String getTitle(Scanner sc) {
        String reset = "\u001B[0m";
        String cyan = "\u001B[36m";
        System.out.println(cyan + "What's the title of the event? " + reset);
        return sc.nextLine();
    }

    private static String getDescription(Scanner sc) {
        String reset = "\u001B[0m";
        String cyan = "\u001B[36m";
        System.out.println(cyan + "What's the event about? " + reset);
        return sc.nextLine();
    }

    private static String getVenue(Scanner sc) {
        String reset = "\u001B[0m";
        String cyan = "\u001B[36m";
        System.out.println(cyan + "Where will it be held? " + reset);
        return sc.nextLine();
    }

    private static LocalDate getDate(Scanner sc) {
        String reset = "\u001B[0m";
        String cyan = "\u001B[36m";
        LocalDate date = null;
        boolean validDate = false;
        while (!validDate) {
            try {
                System.out.println(cyan + "When will it be held? (e.g. 2024-12-04) " + reset);
                date = LocalDate.parse(sc.nextLine());
                validDate = true;
            } catch (DateTimeParseException e) {
                System.out.println("Invalid date format. Please enter the date in YYYY-MM-DD format.");
            }
        }
        return date;
    }

    private static LocalTime[] getTimes(Scanner sc) {
        String reset = "\u001B[0m";
        String cyan = "\u001B[36m";
        LocalTime[] times = new LocalTime[2];
        boolean validTimes = false;
        while (!validTimes) {
            try {
                System.out.println(cyan + "What time will it start and end? (e.g. 11:05-13:30) " + reset);
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


    private static int getEventNum(String username, int role) {
        String sql = "SELECT eventNum FROM userdb.users WHERE username = ? AND role = ?";
        try (Connection conn = DBOperations.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, username);
            pstmt.setInt(2, role);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                return rs.getInt("eventNum");
            }
        } catch (SQLException e) {
            System.out.println("Error retrieving event number: " + e.getMessage());
        }
        return 0;
    }

    public static void updateEventNum(String username, int newEventCount, int role) {
        int currentEventNum = getEventNum(username, role);
        int updatedEventNum = currentEventNum + newEventCount;

        String sql = "UPDATE userdb.users SET eventNum = ? WHERE username = ? AND role = ?";
        try (Connection conn = DBOperations.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, updatedEventNum);
            pstmt.setString(2, username);
            pstmt.setInt(3, role);
            int affectedRows = pstmt.executeUpdate();
            if (affectedRows > 0) {
                System.out.println("\nEvent number updated successfully.\n");
            } else {
                System.out.println("No records updated, check educator ID.");
            }
        } catch (SQLException e) {
            System.out.println("Error updating event number: " + e.getMessage());
        }
    }
}