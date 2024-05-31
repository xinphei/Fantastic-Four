package assignmentds;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Random;
import java.util.ArrayList;
import java.time.LocalDateTime;
import java.util.List;
import java.util.LinkedList;
import java.util.Scanner;
import java.sql.Timestamp;
import java.util.Objects;

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
    private List<BookingSystem> pastBookings;
    private Coordinate locationCoordinate; //The Coordinate class is at above
    private int currentPoints;
    //add pointLastUpdated for GlobalLeaderboard (changes)
    private Timestamp pointLastUpdated;
    //for FriendRequest purpose (changes) use linkedList for edges (graph thoery)
    private LinkedList<User> friends;
    private LinkedList<User> friendRequests;

    private byte [] salt; //ADDED BY DY

    public User(String email, String username, String password, byte[] salt, int role, Coordinate locationCoordinate, int currentPoints, Timestamp pointLastUpdated){
        this.email = email;
        this.username = username;
        this.password = password;
        this.role = role;
        this.locationCoordinate = locationCoordinate;
        this.currentPoints = currentPoints;
        this.pointLastUpdated = pointLastUpdated;
        this.friends = new LinkedList<>();
        this.friendRequests = new LinkedList<>();
        this.salt = salt;
        this.pastBookings = new ArrayList<>(); // Initialize pastBookings list
    }

    // Getters and setters
    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getUsername() {
        return this.username;
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
    public Timestamp getPointLastUpdated(){
        return pointLastUpdated;
    }

    public void setPointLastUpdated(Timestamp pointLastUpdated) {
        this.pointLastUpdated = pointLastUpdated;
    }
 

    public LinkedList<User> getFriends() {
        // Fetch friends from the database based on the user's ID
        LinkedList<User> friends = DBOperations.fetchFriendsByUsername(this.username);
        return friends;
    }

    public void setFriends(LinkedList<User> friends) {
        this.friends = friends;
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


    public static User createUser(String email, String username, String hashedPassword, byte[] salt, int role, Coordinate locationCoordinate, int currentPoint, Timestamp pointLastUpdated) {
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
                currentPoint, 
                pointLastUpdated
                
        );

        // add user to database, return appropriately
        if (DBOperations.addUserToDB(newUser)) {
            return newUser;
        }
        else {
            return null;
        }
    }

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
            int role;
            // Set user role
            boolean isValidRole = false;
            do{
                System.out.println("Roles:");
                System.out.println("1- Young Students");
                System.out.println("2- Parents");
                System.out.println("3- Educators");
                System.out.print("Enter Role Number: ");
                role = scanner.nextInt();
                if(role ==1 || role ==2||role ==3){
                    isValidRole = false;
                }
                else{
                    System.out.println("Enter number 1 to 3 only");
                    isValidRole = true;
                }   
            }while(isValidRole);
            
            scanner.nextLine(); // Consume newline left-over
            // Hash the password before storing it
            byte[] salt = SecureEncryptor.generateSalt();
            String hashedPassword = SecureEncryptor.hashPassword(password, salt);
            Timestamp timestamp = new Timestamp(System.currentTimeMillis());

            User newUser = User.createUser(email, username, hashedPassword, salt, role, coordinate, 0, timestamp);

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
    
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return email.equals(user.email); // Assuming email is unique for each user
    }

    @Override
    public int hashCode() {
        return Objects.hash(email);
    }

}
    



