package assignmentds;

import java.util.List;

public class ViewProfile {
    
    public static void main(User user) {

        // Call displayProfile method with the provided user
        displayProfile(user);
        Home.main(user);
    }

    public static void displayProfile(User user) {
        System.out.println("Profile Information:");
        System.out.println("Email: " + user.getEmail());
        System.out.println("Username: " + user.getUsername());
        System.out.println("Role: " + getRoleName(user.getRole()));
        System.out.println("Location Coordinate: " + user.getLocationCoordinate());
        
        
        // Additional information based on role
        switch (user.getRole()) {
            case 1: // Young Students
                System.out.println("Points: " + user.getCurrentPoints());
                System.out.println("Friends: ");
                displayFriends(user.getFriends());
                System.out.println("Parents: ");
                displayParents(user.getParents());
                break;
            case 2: // Parents
                System.out.println("Past Bookings: ");
                displayPastBookings(user.getPastBookings());
                System.out.println("Children: ");
                displayChildren(user.getChildren());
                break;
            case 3: // Educators
                System.out.println("Number of Quizzes Created: " + user.getNumEventsCreated());
                System.out.println("Number of Events Created: " + user.getNumQuizzesCreated());
                break;
            default:
                System.out.println("Invalid role.");
        }
        
    }

    // Method to get role name based on role number
    private static String getRoleName(int role) {
        switch (role) {
            case 1:
                return "Young Students";
            case 2:
                return "Parents";
            case 3:
                return "Educators";
            default:
                return "Unknown";
        }
    }

    // Method to display friends list
    private static void displayFriends(List<User> friends) {
        for (User friend : friends) {
            System.out.println("- " + friend.getUsername());
        }
    }

    // Method to display parents list
    private static void displayParents(List<User> parents) {
        for (User parent : parents) {
            System.out.println("- " + parent.getUsername());
        }
    }

    // Method to display children list
    private static void displayChildren(List<User> children) {
        for (User child : children) {
            System.out.println("- " + child.getUsername());
        }
    }
    
    // Method to display past bookings
    private static void displayPastBookings(List<BookingSystem> pastBookings) {
        for (BookingSystem booking : pastBookings) {
            System.out.println("- Destination: " + booking.getDestination().getName());
            System.out.println("  Date: " + booking.getDate());
            System.out.println("");
        }
    }
}





