package assignmentds;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Random;
import java.util.ArrayList;
import java.time.LocalDateTime;
import java.util.List;
import java.util.LinkedList;
import java.util.Scanner;
import assignmentds.Home;

//Coordinate class
class Coordinate {
    private double latitude;
    private double longitude;

    //Coordinate class
    public Coordinate(double latitude, double longitude) {
        this.latitude = latitude;
        this.longitude = longitude;
    }

    // Getters and setters
    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    @Override
    public String toString() {
        return "(" + latitude + ", " + longitude + ")";
    }
}

//User class
public class User{
    private String email, username, password;
    private int role;
    /*
    Roles:
    1- Young Students
    2- Parents
    3- Educators
     */
    private ArrayList<User> parents;
    private ArrayList<User> children;
    private List<BookingSystem> pastBookings;
    private Coordinate locationCoordinate; //The Coordinate class is at above
    private int currentPoints;
    //add pointLastUpdated for GlobalLeaderboard (changes)
    private LocalDateTime pointLastUpdated;
    //for FriendRequest purpose (changes) use linkedList for edges (graph thoery)
    private LinkedList<User> friends;
    private LinkedList<User> friendRequests;

    private byte [] salt; //ADDED BY DY

    public User(String email, String username, String password, byte[] salt, int role, Coordinate locationCoordinate, int currentPoints){
        this.email = email;
        this.username = username;
        this.password = password;
        this.role = role;
        this.locationCoordinate = locationCoordinate;
        this.currentPoints = currentPoints; //CHANGED BY DY
        // changes
        this.pointLastUpdated = LocalDateTime.now();
        this.parents = new ArrayList<>();
        this.children = new ArrayList<>();
        // changes
        this.friends = new LinkedList<>();
        this.friendRequests = new LinkedList<>();
        this.salt = salt;
    }

    // Getters and setters
    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public byte[] getSalt(){ return salt; }  //ADDED BY DY

    public void setPassword(String password) {
        this.password = password;
    }

    public int getRole() {
        return role;
    }

    public void setRole(int role) {
        this.role = role;
    }

    public ArrayList<User> getParents() {
        return parents;
    }

    public void setParents(ArrayList<User> parents) {
        this.parents = parents;
    }

    public ArrayList<User> getChildren() {
        return children;
    }

    public void setChildren(ArrayList<User> children) {
        this.children = children;
    }

    public Coordinate getLocationCoordinate() {
        return locationCoordinate;
    }

    public void setLocationCoordinate(Coordinate locationCoordinate) {
        this.locationCoordinate = locationCoordinate;
    }

    public int getCurrentPoints() {
        return currentPoints;
    }

    public void setCurrentPoints(int currentPoints) {
        this.currentPoints = currentPoints;
    }
    
    //add pointLastUpdated for GlobalLeaderboard (changes)
    public LocalDateTime getPointLastUpdated(){
        return pointLastUpdated;
    }

    public void setPointLastUpdated(LocalDateTime pointLastUpdated) {
        this.pointLastUpdated = pointLastUpdated;
    }
    // changes
    public void sendFriendRequest(User friend) {
        friendRequests.add(friend);
    }

    public void acceptFriendRequest(User friend) {
        friends.add(friend);
        friendRequests.remove(friend);
        friend.addFriend(this);
    }

    public void removeFriendRequest(User friend) {
        friendRequests.remove(friend);
    }

    public List<User> getFriends() {
        return friends;
    }

    public LinkedList<User> getFriendRequests() {
        return friendRequests;
    }

    private void addFriend(User friend) {
        friends.add(friend);
    }
    
    // Method to add a parent to the user
    public void addParent(User parent) {
        parents.add(parent);
    }

    // Method to add a child to the user
    public void addChild(User child) {
        children.add(child);
    }

    // Method to display user details
    public void displayUserDetails() {
        System.out.println("Email: " + email);
        System.out.println("Username: " + username);
        System.out.println("Role: " + role);
        if (!parents.isEmpty()) {
            System.out.println("Parents: ");
            for (User parent : parents) {
                System.out.println("- " + parent.toString());
            }
        }
        if (!children.isEmpty()) {
            System.out.println("Children: ");
            for (User child : children) {
                System.out.println("- " + child.toString());
            }
        }
        System.out.println("Location Coordinate: " + locationCoordinate);
        System.out.println("Current Points: " + currentPoints);
    }

    public int getNumQuizzesCreated() {
       return CreateQuiz.getNumQuizzesCreated();
    }

    public int getNumEventsCreated() {
       return CreateEvent.getNumEventsCreated();
    }
    
    public List<BookingSystem> getPastBookings() {
        return pastBookings;
    }

    public void setPastBookings(List<BookingSystem> pastBookings) {
        this.pastBookings = pastBookings;
    }

    public void addPastBooking(BookingSystem booking) {
        pastBookings.add(booking);
    }


    public static User createUser(String email, String username, String hashedPassword, byte[] salt, int role, Coordinate locationCoordinate, int currentPoint) {
        // validate user details
        if (email == null || username == null || hashedPassword == null) {
            System.out.println("-------------------------------------------");
            System.out.println("Invalid user information.");
            System.out.println("-------------------------------------------");
            System.out.println();
            return null;
        }

        // create user instance to be returned
        User newUser = new User(
                email,
                username,
                hashedPassword,
                salt,
                role,
                locationCoordinate,
                currentPoint
        );

        // add user to database, return appropriately
        if (DBOperations.addUserToDB(newUser)) {
            return newUser;
        }
        else {
            return null;
        }
    }

//    public static User getUser(String email, String hashedPassword) {
//        try (ResultSet resultSet = DBOperations.getUserDetailsSet(email, hashedPassword)) {
//            if (resultSet.next()) {
//                String username = resultSet.getString("username");
//                byte[] retrievedSalt = resultSet.getBytes("salt");
//                int role =  resultSet.getInt("role");
//                double coordinateX = resultSet.getDouble("locationCoordinate_X");
//                double coordinateY = resultSet.getDouble("locationCoordinate_Y");
//                int currentPoints = resultSet.getInt("currentPoints");
//
//                Coordinate coordinate = new Coordinate(coordinateX, coordinateY);
//
//                return new User(username, email, hashedPassword, retrievedSalt, role, coordinate, currentPoints);
//            }
//        }
//        catch (SQLException e) {
//            e.printStackTrace();
//        }
//
//        return null;
//    }

    public static User registerUser(Scanner scanner) {
        do {
            String reset = "\u001B[0m";
            String cyan = "\u001B[36m";
            System.out.println("\n\n___________________________________________");
            System.out.println(cyan +"         ^^^ REGISTRATION PAGE ^^^         "+ reset);
            System.out.println("___________________________________________");
            System.out.print("Email: ");
            String email = scanner.nextLine();

            if (!AuthenticationSystem.isValidEmail(email)) {
                System.out.println("-------------------------------------------");
                System.out.println("Invalid email address. Please try again.");
                System.out.println("-------------------------------------------");
                continue;
            }

            System.out.print("Username: ");
            String username = scanner.nextLine();
        
            if (!AuthenticationSystem.isValidUsername(username)) {
                System.out.println("-------------------------------------------");
                System.out.println("Invalid username. Please try again.");
                System.out.println("-------------------------------------------");
                continue;
            }   

            System.out.println("\n//Password rules//");
            System.out.println("At least: ");
            System.out.println("1) 8 characters");
            System.out.println("2) 1 capital letter");
            System.out.println("3) 1 small letter");
            System.out.println("4) 1 special character");
            System.out.println("5) 3 number digits");
            System.out.print("\nPassword: ");
            String password = scanner.nextLine();

            if (!AuthenticationSystem.isValidPassword(password)) {
                System.out.println("\n-------------------------------------------");
                System.out.println("Invalid password. Please try again.");
                System.out.println("-------------------------------------------");
                continue;
            }

            // Generate random coordinates
            Random rand = new Random();
            double x = -500.0 + (500.0 - (-500.0)) * rand.nextDouble();
            double y = -500.0 + (500.0 - (-500.0)) * rand.nextDouble();
            String coordinates = "(" + x + ", " + y + ")";
            Coordinate coordinate = new Coordinate(x, y); //ADDED BY DY

            // Set user role
            System.out.println("Roles:");
            System.out.println("1- Young Students");
            System.out.println("2- Parents");
            System.out.println("3- Educators");
            System.out.print("Enter Role Number: ");
            int role = scanner.nextInt();
            scanner.nextLine(); // Consume newline left-over

            // Hash the password before storing it
            byte[] salt = SecureEncryptor.generateSalt();
            String hashedPassword = SecureEncryptor.hashPassword(password, salt);

            User newUser = User.createUser(email, username, hashedPassword, salt, role, coordinate, 0);

            if (newUser != null) {
                System.out.println("-------------------------------------------");
                System.out.println("Registration successful!");
                System.out.println("-------------------------------------------");
                Home.main(newUser);
                return newUser;
            }
            else {
                System.out.println("-------------------------------------------");
                System.out.println("Registration failed. Please try again.");
                System.out.println("-------------------------------------------");
            }   
        }
        while (true);
    }

}
    



