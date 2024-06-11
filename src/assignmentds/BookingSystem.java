package assignmentds;

import java.time.LocalDate;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.InputMismatchException;
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

    public static void main(User user) {
        String reset = "\u001B[0m";
        String blue = "\u001B[34m";
        String magenta = "\u001B[35m";
        String green = "\u001B[32m";
        String cyan = "\u001B[36m";
        
        Scanner sc = new Scanner(System.in);
        readFile();

        double userX = user.getLatitude();
        double userY = user.getLongitude();

        // Current date
        // Calculate distances and filter destinations
        List<Destination> filteredDestinations = filterDestinations(destinations, userX, userY);
        
        // Display booking page
        displayBookingPage(filteredDestinations, currentDate);

        // Get and display children for parent
        String parentUsername = user.getUsername();
        List<String> children = ParentChildRelationship.loadChildren(parentUsername);

        if (children.isEmpty()) {
            System.out.println("No children found for the entered username.");
            
            return;
        }

        System.out.println(cyan + "List of children(s): " + reset);
        for (int i = 0; i < children.size(); i++) {
            System.out.println("[" + (i + 1) + "] " + children.get(i));
        }
        
        int selectedChildIndex;
        boolean isValidC = false;
        do{
            System.out.print(cyan + "\nEnter a child's index number for booking: " + reset);
            selectedChildIndex = sc.nextInt();
            if (selectedChildIndex < 1 || selectedChildIndex > children.size()) {
                System.out.println("Invalid selection. Please enter a valid index.");
            }
            else{
                isValidC = true;
            }
        }while(!isValidC);
        
       
        String selectedChild = children.get(selectedChildIndex - 1);

        // Get available dates for booking
        List<LocalDate> availableDates = DBOperations.getAvailableDates(parentUsername);
        // Display available dates
        System.out.println(cyan + "Available Dates for Booking:" + reset);
        for (int i = 0; i < availableDates.size(); i++) {
            System.out.println("[" + (i + 1) + "] " + availableDates.get(i));
        }
        
        // Prompt user to select a date for booking
        int selectedDateIndex = 0;
        boolean isValidD = false;
        do {
            try {
                System.out.print(cyan+"\nEnter a date for booking (choose from 1 to " + availableDates.size() + "): "  + reset);
                selectedDateIndex = sc.nextInt();
                
                if (selectedDateIndex < 1 || selectedDateIndex > availableDates.size()) {
                    System.out.println("Invalid date selection. Please choose a valid date.");
                } else {
                    isValidD = true;
                }
            } catch (InputMismatchException e) {
                System.out.println("Invalid input. Please enter a number.");
                sc.next(); // Clear the invalid input
            }
        } while (!isValidD);

        
        // Get the selected date
        LocalDate selectedDate = availableDates.get(selectedDateIndex - 1);

        // Prompt user to select a destination
        int selectedDestinationId = 0;
        boolean isValidID = false;
        do {
            try {
                System.out.print(cyan + "Enter destination ID for booking: " + reset);
                selectedDestinationId = sc.nextInt();
                if (selectedDestinationId < 1 || selectedDestinationId > 5) {
                    System.out.println("Invalid destination ID. Please enter a valid ID.");
                } else {
                    isValidID = true;
                }
            } catch (InputMismatchException e) {
                System.out.println("Invalid input. Please enter a number.");
                sc.next(); // Clear the invalid input
            }
        } while (!isValidID);
        

        Destination selectedDestination = filteredDestinations.get(selectedDestinationId - 1);
        System.out.println("=========================================================================");
        System.out.println("\nSelected booking for: " + green + selectedDestination.getName() + reset + " for child " + green + selectedChild + reset);

        // Book the tour
        boolean bookingSuccess = DBOperations.bookATour(selectedChild, selectedDestination.getName(), selectedDate.toString(), user);
        if (bookingSuccess) {
            System.out.println("\nBooking confirmed for " + green + selectedDestination.getName() + reset + " on " 
                    + green + selectedDate + reset + " for child " + green + selectedChild + reset);

        System.out.println(magenta + "                                  __       _ _         _                 _            _   _ \n" +
                magenta + " ___ _   _  ___ ___ ___  ___ ___ / _|_   _| | |_   _  | |__   ___   ___ | | _____  __| | / \\\n" +
                magenta + "/ __| | | |/ __/ __/ _ \\/ __/ __| |_| | | | | | | | | | '_ \\ / _ \\ / _ \\| |/ / _ \\/ _` |/  /\n" +
                magenta + "\\__ \\ |_| | (_| (_|  __/\\__ \\__ \\  _| |_| | | | |_| | | |_) | (_) | (_) |   <  __/ (_| /\\_/ \n" +
                magenta + "|___/\\__,_|\\___\\___\\___||___/___/_|  \\__,_|_|_|\\__, | |_.__/ \\___/ \\___/|_|\\_\\___|\\__,_\\/   \n" +
                magenta + "                                               |___/                                        ");

        } else {
            System.out.println("Failed to book the tour. Please try again.");
        }
        Home.main(user);
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
        String reset = "\u001B[0m";
        String blue = "\u001B[34m";
        System.out.println(blue + "   ___             _    _                 ___                   \n" +
                blue + "  / __\\ ___   ___ | | _(_)_ __   __ _    / _ \\__ _  __ _  ___ _ \n" +
                blue + " /__\\/// _ \\ / _ \\| |/ / | '_ \\ / _` |  / /_)/ _` |/ _` |/ _ (_)\n" +
                blue + "/ \\/  \\ (_) | (_) |   <| | | | | (_| | / ___/ (_| | (_| |  __/_ \n" +
                blue + "\\_____/\\___/ \\___/|_|\\_\\_|_| |_|\\__, | \\/    \\__,_|\\__, |\\___(_)\n" +
                blue + "                                |___/              |___/ ");
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
