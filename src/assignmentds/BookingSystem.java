package assignmentds;

import java.time.LocalDate;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class BookingSystem extends ViewEvent {

    private static List<Destination> destinations = new ArrayList<>();
    private static LocalDate currentDate = LocalDate.of(2024, 4, 28);

    private Destination destination;
    private LocalDate date;

    public BookingSystem(Destination destination, LocalDate date) {
        this.destination = destination;
        this.date = date;
    }

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        readFile();

        // Assume user coordinates
        double userX = 0.00;
        double userY = 0.00;

        // Current date
        // Calculate distances and filter destinations
        List <Destination> filteredDestinations = filterDestinations(destinations, userX, userY);

        // Display booking page
        displayBookingPage(filteredDestinations, currentDate);

        // Display booking page
        System.out.print("Enter destination ID for booking: ");
        int selectedDestinationId = sc.nextInt();
        if (selectedDestinationId < 1 || selectedDestinationId > filteredDestinations.size()) {
            System.out.println("Invalid destination ID. Please enter a valid ID.");
            return; // Exit the program if the destination ID is invalid
        }

        Destination selectedDestination = filteredDestinations.get(selectedDestinationId - 1);
        System.out.println("=========================================================================");
        System.out.println("\nSelected booking for: " + selectedDestination.getName());

        // Calculate available time slots
        List<LocalDate> availableTimeSlots = calculateAvailableTimeSlots(currentDate, null);

        // Display available time slots
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        System.out.println("\nAvailable Time Slots:");
        for (int i = 0; i < availableTimeSlots.size(); i++) {
            System.out.println("[" + (i + 1) + "] " + formatter.format(availableTimeSlots.get(i)));
        }

        System.out.print("\nEnter a time slot: ");
        int selectedTimeSlot = sc.nextInt();
        if (selectedTimeSlot < 1 || selectedTimeSlot > availableTimeSlots.size()) {
            System.out.println("Invalid time slot. Please enter a valid time slot.");
            return; // Exit the program if the time slot is invalid
        }

        LocalDate selectedDate = availableTimeSlots.get(selectedTimeSlot - 1);
        System.out.println("\nBooking confirmed for " + selectedDestination.getName() + " on " + formatter.format(selectedDate));

        System.out.println("                                  __       _ _         _                 _            _   _ \n" +
                " ___ _   _  ___ ___ ___  ___ ___ / _|_   _| | |_   _  | |__   ___   ___ | | _____  __| | / \\\n" +
                "/ __| | | |/ __/ __/ _ \\/ __/ __| |_| | | | | | | | | | '_ \\ / _ \\ / _ \\| |/ / _ \\/ _` |/  /\n" +
                "\\__ \\ |_| | (_| (_|  __/\\__ \\__ \\  _| |_| | | | |_| | | |_) | (_) | (_) |   <  __/ (_| /\\_/ \n" +
                "|___/\\__,_|\\___\\___\\___||___/___/_|  \\__,_|_|_|\\__, | |_.__/ \\___/ \\___/|_|\\_\\___|\\__,_\\/   \n" +
                "                                               |___/                                        ");

        // Close the scanner
        sc.close();
    }

    private static void readFile() {
        try (Scanner input = new Scanner(new FileInputStream("BookingDestination.txt"))) {
            while (input.hasNextLine()) {
                String name = input.nextLine().trim();
                // Skip blank line
                if (name.isEmpty()) {
                    continue;
                }
                String[] coordinates = input.nextLine().trim().split(", ");
                double x = Double.parseDouble(coordinates[0]);
                double y = Double.parseDouble(coordinates[1]);
                destinations.add(new Destination(name, x, y));
            }
        } catch (FileNotFoundException | NumberFormatException e) {
            e.printStackTrace();
        }
    }

    private static List<Destination> filterDestinations(List<Destination> destinations, double userX, double userY) {
        List <Destination> filtered = new ArrayList<>();
        for (Destination destination : destinations) {
            double distance = calculateDistance(userX, userY, destination.getX(), destination.getY());
            destination.setDistance(distance);
            filtered.add(destination);
        }
        filtered.sort((d1, d2) -> Double.compare(d1.getDistance(), d2.getDistance()));
        return filtered;
    }

    private static double calculateDistance(double x1, double y1, double x2, double y2) {
        return Math.sqrt(Math.pow(x2 - x1, 2) + Math.pow(y2 - y1, 2));
    }

    private static void displayBookingPage(List<Destination> destinations, LocalDate currentDate) {
        System.out.println("   ___             _    _                 ___                   \n" +
                "  / __\\ ___   ___ | | _(_)_ __   __ _    / _ \\__ _  __ _  ___ _ \n" +
                " /__\\/// _ \\ / _ \\| |/ / | '_ \\ / _` |  / /_)/ _` |/ _` |/ _ (_)\n" +
                "/ \\/  \\ (_) | (_) |   <| | | | | (_| | / ___/ (_| | (_| |  __/_ \n" +
                "\\_____/\\___/ \\___/|_|\\_\\_|_| |_|\\__, | \\/    \\__,_|\\__, |\\___(_)\n" +
                "                                |___/              |___/ ");
        for (int i = 0; i < 5; i++) {
            Destination destination = destinations.get(i);
            System.out.println("[" + (i + 1) + "] " + destination.getName());
            System.out.printf("%.2f km away\n", destination.getDistance());
            System.out.println("");
        }
    }

    // Calculate available time slots
    private static List<LocalDate> calculateAvailableTimeSlots(LocalDate currentDate, List<Event> registeredEvents) {
        List<LocalDate> availableTimeSlots = new ArrayList<>();

        LocalDate nextDate = currentDate.plusDays(1); // Start checking from the next day
        while (availableTimeSlots.size() < 7) { // Limit to one week
            boolean isAvailable = true;
            if (registeredEvents != null) {
                for (Event event : registeredEvents) {
                    if (nextDate.equals(event.getDate())) {
                        isAvailable = false;
                        break;
                    }
                }
            } if (isAvailable) {
                availableTimeSlots.add(nextDate);
            }
            nextDate = nextDate.plusDays(1); // Move to the next day
        }
        return availableTimeSlots;
    }
    
    public Destination getDestination() {
        return destination;
    }

    public LocalDate getDate() {
        return date;
    }
    
    // Helper method to check if two dates are the same day
    private static boolean isSameDay(LocalDate date1, LocalDate date2) {
        return date1.equals(date2);
    }
}
