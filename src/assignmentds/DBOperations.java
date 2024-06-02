package assignmentds;

import java.sql.*;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

public class DBOperations {

    private static String url = "jdbc:mysql://localhost:3306/userdb"; //write your own url, user, and password
    private static String DBuser = "root";
    private static String pw = "2416";

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(url, DBuser, pw);
    }

    public static boolean addUserToDB(User newUser) {
        String sql = "INSERT INTO userdb.users (email, username, password, salt, role, locationCoordinate_X, locationCoordinate_Y) VALUES (?, ?, ?, ?, ?, ?, ?)";

        try (Connection connection = DriverManager.getConnection(url, DBuser, pw);
             PreparedStatement statement = connection.prepareStatement(sql);){

            if (AuthenticationSystem.isIdentifierRegistered(connection, newUser.getEmail())) {
                System.out.println("---------------------------------------------");
                System.out.println("Email has already been registered");
                System.out.println("---------------------------------------------");
                return false;
            } else {
                statement.setString(1, newUser.getEmail());
                statement.setString(2, newUser.getUsername());
                statement.setString(3, newUser.getPassword());
                statement.setBytes(4, newUser.getSalt());
                statement.setInt(5, newUser.getRole());
                statement.setObject(6, newUser.getLocationCoordinate().getLatitude());
                statement.setObject(7, newUser.getLocationCoordinate().getLongitude());

                // Execute the statement
                int rowsInserted = statement.executeUpdate(); // returns the number of rows affected by the execution, should be 1 if the insertion was successful
                return (rowsInserted > 0);
            }

        }catch (SQLException e){
            e.printStackTrace();
            return false;
        }
    }


    public static User getLoginUser(String identifier, String password) throws SQLException {
        String query;

        Connection connection = getConnection();
        //check if the identifier is email
        if (identifier.contains("@")) {
            query = "SELECT * FROM userdb.users WHERE email = ?";
        } else {
            query = "SELECT * FROM userdb.users WHERE username = ?";
        }

        PreparedStatement loginUser = connection.prepareStatement(query);
        loginUser.setString(1, identifier);
        ResultSet resultSet = loginUser.executeQuery();
        if (resultSet.next()) {
            String hashedPassword = resultSet.getString("password");
            byte[] retrievedSalt = resultSet.getBytes("salt");
            String inputHash = SecureEncryptor.hashPassword(password, retrievedSalt);
            if (hashedPassword.equals(inputHash)) {
                return new User(resultSet.getString("email"), resultSet.getString("username"), hashedPassword, retrievedSalt, resultSet.getInt("role"), new Coordinate(resultSet.getDouble("locationCoordinate_X"), resultSet.getDouble("locationCoordinate_Y")), resultSet.getInt("currentPoints"), resultSet.getTimestamp("pointLastUpdated"));
            } else {
                System.out.println("Incorrect Password");
                return null;
            }
        } else {
            System.out.println("No user found with the provided identifier");
            return null;
        }
    }

    // ATTEMPT QUIZZES
    public static boolean updateCurrentPoints(String email, int newPoints, Timestamp pointLastUpdated) {
        String sql = "UPDATE users SET currentPoints = ?, pointLastUpdated = ? WHERE email = ?";

        try (Connection connection = getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setInt(1, newPoints);
            statement.setTimestamp(2, pointLastUpdated);
            statement.setString(3, email);

            int rowsUpdated = statement.executeUpdate();
            return rowsUpdated > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    
    public static LinkedList<User> fetchAllUsers() {
        LinkedList<User> allUsers = new LinkedList<>();
        String query = "SELECT email, username, password, salt, role, locationCoordinate_X, locationCoordinate_Y, currentPoints, pointLastUpdated FROM users";

        try (Connection connection = getConnection();
             Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(query)) {

            while (resultSet.next()) {
                String email = resultSet.getString("email");
                String username = resultSet.getString("username");
                String password = resultSet.getString("password");
                byte[] salt = resultSet.getBytes("salt");
                int role = resultSet.getInt("role");
                double locationCoordinate_X = resultSet.getDouble("locationCoordinate_X");
                double locationCoordinate_Y = resultSet.getDouble("locationCoordinate_Y");
                int currentPoints = resultSet.getInt("currentPoints");
                Timestamp pointLastUpdated = resultSet.getTimestamp("pointLastUpdated");

                User user = new User(email, username, password, salt, role, new Coordinate(locationCoordinate_X, locationCoordinate_Y), currentPoints, pointLastUpdated);
                allUsers.add(user);
            }

        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }

        return allUsers;
    }
    
    

    //REGISTER FOR EVENT
    public static boolean registerForEvent(String username, String eventTitle, String eventDate, String startTime, String endTime) {
        DateTimeFormatter dateFormatter = DateTimeFormatter.ISO_LOCAL_DATE;
        DateTimeFormatter timeFormatter = DateTimeFormatter.ISO_LOCAL_TIME;

        LocalDate date = LocalDate.parse(eventDate, dateFormatter);
        LocalTime castedStartTime = LocalTime.parse(startTime, timeFormatter);
        LocalTime castedEndTime = LocalTime.parse(endTime, timeFormatter);

        // First, check if the user has already registered for this event
        if (userAlreadyRegistered(username, eventTitle)) {
            System.out.println("User has already registered for this event.");
            return false;
        }

        // Check for clashes in both event registrations and tour bookings
        String clashChecking = "SELECT 1 FROM ("
                + "SELECT event_date, start_time, end_time FROM (SELECT event_date, start_time, end_time FROM userdb.eventregistrations WHERE username = ? "
                + "UNION "
                + "SELECT tour_date AS event_date, '00:00:00' AS start_time, '23:59:59' AS end_time FROM userdb.tourbookings WHERE username = ?) AS combined "
                + "AS subquery"
                + "WHERE event_date = ? AND NOT (end_time <= ? OR start_time >= ?)";

        try (Connection conn = DBOperations.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(clashChecking)) {

            pstmt.setString(1, username);
            pstmt.setString(2, username);
            pstmt.setDate(3, java.sql.Date.valueOf(date));
            pstmt.setTime(4, java.sql.Time.valueOf(castedEndTime));
            pstmt.setTime(5, java.sql.Time.valueOf(castedStartTime));

            ResultSet resultSet = pstmt.executeQuery();
            if (resultSet.next() && resultSet.getInt(1) > 0) {
                System.out.println("Registration failed: Event time conflicts with an existing registration.");
                return false;
            }

            // register event registration if no clash
            String insertQuery = "INSERT INTO userdb.eventregistrations (username, event_title, event_date, start_time, end_time) VALUES (?, ?, ?, ?, ?)";
            try (PreparedStatement pstmtInsert = conn.prepareStatement(insertQuery)) {
                pstmtInsert.setString(1, username);
                pstmtInsert.setString(2, eventTitle);
                pstmtInsert.setDate(3, java.sql.Date.valueOf(date));
                pstmtInsert.setTime(4, java.sql.Time.valueOf(castedStartTime));
                pstmtInsert.setTime(5, java.sql.Time.valueOf(castedEndTime));
                pstmtInsert.executeUpdate();
                System.out.println("Event successfully registered.");
                return true;
            }

        } catch (SQLException e) {
            System.out.println("Database error: " + e.getMessage());
        }
        return false;
    }

    private static boolean userAlreadyRegistered(String username, String eventTitle) {
        String query = "SELECT COUNT(*) FROM userdb.eventregistrations WHERE username = ? AND event_title = ?";
        try (Connection conn = DBOperations.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {

            pstmt.setString(1, username);
            pstmt.setString(2, eventTitle);

            ResultSet rs = pstmt.executeQuery();
            if (rs.next() && rs.getInt(1) > 0) {
                return true; // User is already registered for this event
            }
        } catch (SQLException e) {
            System.out.println("Database error checking event registration: " + e.getMessage());
        }
        return false;
    }
    
    

    
    // GET THE LIST OF TOP 3 ONGOING EVENT(TODAY)
    public static List<Event> getOngoingEvents() {
        LocalDate today = LocalDate.now();

        List<Event> list = new ArrayList<>();
        String ongoingEventQuery = "SELECT * FROM userdb.events WHERE event_date = ? ";

        try (Connection conn = DBOperations.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(ongoingEventQuery)) {

            pstmt.setDate(1, java.sql.Date.valueOf(today));

            ResultSet results = pstmt.executeQuery();
            while (results.next()) {
                Event event = new Event(
                        results.getString("event_title"),
                        results.getString("description"),
                        results.getString("venue"),
                        results.getDate("event_date").toLocalDate(),
                        results.getTime("start_time").toLocalTime(),
                        results.getTime("end_time").toLocalTime()
                );
                list.add(event);
            }
        } catch (SQLException e) {
            System.out.println("Database error: " + e.getMessage());
        }
        return list;
    }

    // GET THE LIST OF UPCOMING EVENT(CLOSEST TO THE INSTANT TIME)
    public static List<Event> getUpcomingEvents() {
        LocalDate today = LocalDate.now();

        List<Event> list = new ArrayList<>();
        String upcomingEventsQuery = "SELECT * FROM userdb.events WHERE event_date > ? ORDER BY event_date, start_time LIMIT 3";

        try (Connection conn = DBOperations.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(upcomingEventsQuery)) {

            pstmt.setDate(1, java.sql.Date.valueOf(today));

            ResultSet results = pstmt.executeQuery();
            while (results.next()) {
                Event event = new Event(
                        results.getString("event_title"),
                        results.getString("description"),
                        results.getString("venue"),
                        results.getDate("event_date").toLocalDate(),
                        results.getTime("start_time").toLocalTime(),
                        results.getTime("end_time").toLocalTime()
                );
                list.add(event);
            }
        } catch (SQLException e) {
            System.out.println("Database error: " + e.getMessage());
        }
        return list;
    }


    // INSERT NEW EVENT (EDUCATOR)
    public static boolean insertNewEvent(String eventTitle, String description, String venue, LocalDate eventDate, LocalTime startTime, LocalTime endTime) {
        // Convert LocalDate and LocalTime to java.sql.Date and java.sql.Time
        java.sql.Date sqlDate = java.sql.Date.valueOf(eventDate);
        java.sql.Time sqlStartTime = java.sql.Time.valueOf(startTime);
        java.sql.Time sqlEndTime = java.sql.Time.valueOf(endTime);

        // Check if the event already exists
        if (eventExists(eventTitle)) {
            System.out.println("Event already exists with the same title and date.");
            return false; // Event already exists
        }

        // SQL query to insert a new event
        String insertSql = "INSERT INTO userdb.events (event_title, description, venue, event_date, start_time, end_time) VALUES (?, ?, ?, ?, ?, ?)";
        try (Connection conn = DBOperations.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(insertSql)) {

            pstmt.setString(1, eventTitle);
            pstmt.setString(2, description);
            pstmt.setString(3, venue);
            pstmt.setDate(4, sqlDate);
            pstmt.setTime(5, sqlStartTime);
            pstmt.setTime(6, sqlEndTime);

            int affectedRows = pstmt.executeUpdate();
            if (affectedRows > 0) {
                System.out.println("Event successfully added.");
                return true;
            } else {
                System.out.println("No event was added.");
                return false;
            }
        } catch (SQLException e) {
            System.out.println("Database error during event insertion: " + e.getMessage());
            return false;
        }
    }

    // CHECK IF THE EVENT WITH SAME TITLE HAS EXISTED BEFORE ADDING
    private static boolean eventExists(String eventTitle) {
        String checkSql = "SELECT COUNT(*) FROM userdb.events WHERE event_title = ?";
        try (Connection conn = DBOperations.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(checkSql)) {

            pstmt.setString(1, eventTitle);

            ResultSet rs = pstmt.executeQuery();
            if (rs.next() && rs.getInt(1) > 0) {
                return true; // Event already exists
            }
        } catch (SQLException e) {
            System.out.println("Database error during event existence check: " + e.getMessage());
        }
        return false;
    }

    // GET AVAILABLE DATE FOR PARENT TO BOOK SLOT FOR A TOUR (CHECK DB ID CLASH WITH ANY EVENT OR OTHER TOUR)
    public static List<LocalDate> getAvailableDates(String username) {
        LocalDate today = LocalDate.now();
        List<LocalDate> availableDates = new ArrayList<>();

        // Prepare SQL query to check for clashes
        String clashQuery = "SELECT 1 FROM (" +
                "SELECT event_date FROM userdb.eventregistrations WHERE username = ? " +
                "UNION " +
                "SELECT tour_date AS event_date FROM userdb.tourbookings WHERE username = ?" +
                ") AS combined WHERE event_date = ?";

        try (Connection conn = DBOperations.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(clashQuery)) {

            // Check each of the next seven days
            for (int i = 0; i < 7; i++) {
                LocalDate checkDate = today.plusDays(i);
                pstmt.setString(1, username);
                pstmt.setString(2, username);
                pstmt.setDate(3, java.sql.Date.valueOf(checkDate));

                ResultSet resultSet = pstmt.executeQuery();

                // If no results, no clashes for this day
                if (!resultSet.next()) {
                    availableDates.add(checkDate);
                }
            }
        } catch (SQLException e) {
            System.out.println("Database error: " + e.getMessage());
        }
        return availableDates;
    }

    // BOOK TOUR METHOD (BASED ON THE DATE SELECTION FROM THE METHOD getAvailableDates)
    public static boolean bookATour(String childUsername, String destination, String date) {
        LocalDate date1 = LocalDate.parse(date);
        java.sql.Date sqlDate = java.sql.Date.valueOf(date1);
        try {
            String query = "INSERT INTO userdb.tourbookings (username, destination, tour_date) VALUES (?, ?, ?)";
            Connection conn = getConnection();
            PreparedStatement psptm = conn.prepareStatement(query);
            psptm.setString(1, childUsername);
            psptm.setString(2, destination);
            psptm.setDate(3, sqlDate);
            return true;
        } catch (SQLException e) {
            System.out.println("SQL Exception : " + e.getMessage());
        }
        return false;
    }
    
    // FRIEND MANAGEMENT
    public static LinkedList<User> fetchAllStudentsExcept(String currentUsername) {
        LinkedList<User> students = new LinkedList<>();
        String sql = "SELECT * FROM users WHERE username != ? AND role = 1"; // Assuming role 1 is for students
        try (Connection connection = getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, currentUsername);
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                User student = extractUserFromResultSet(resultSet);
                students.add(student);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return students;
    }
    
    public static User fetchUserByUsername(String username) {
        String sql = "SELECT * FROM users WHERE username = ?";
        try (Connection connection = getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, username);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                String email = resultSet.getString("email");
                // Fetch other user properties from the result set
                // For simplicity, I'll assume you have a constructor in your User class
                // that accepts all necessary properties as arguments
                String password = resultSet.getString("password");
                byte[] salt = resultSet.getBytes("salt");
                int role = resultSet.getInt("role");
                // Fetch other properties as needed
                // Create and return a new User object
                return new User(email, username, password, salt, role, null, 0, null);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null; // Return null if no user found with the given username
    }
    
    private static User extractUserFromResultSet(ResultSet resultSet) throws SQLException {
        String email = resultSet.getString("email");
        String username = resultSet.getString("username");
        String password = resultSet.getString("password");
        byte[] salt = resultSet.getBytes("salt");
        int role = resultSet.getInt("role");
        double locationCoordinate_X = resultSet.getDouble("locationCoordinate_X");
        double locationCoordinate_Y = resultSet.getDouble("locationCoordinate_Y");
        int currentPoints = resultSet.getInt("currentPoints");
        Timestamp pointLastUpdated = resultSet.getTimestamp("pointLastUpdated");

        return new User(email, username, password, salt, role, new Coordinate(locationCoordinate_X, locationCoordinate_Y), currentPoints, pointLastUpdated);
    }


    public static boolean isFriend(String username1, String username2) {
        String query = "SELECT COUNT(*) AS count FROM friendships WHERE (user1 = ? AND user2 = ?) OR (user1 = ? AND user2 = ?)";
        try (Connection connection = getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, username1);
            statement.setString(2, username2);
            statement.setString(3, username2);
            statement.setString(4, username1);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    int count = resultSet.getInt("count");
                    return count > 0;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
    
    public static LinkedList<User> fetchFriendsByUsername(String username) {
        LinkedList<User> friends = new LinkedList<>();
        String sql = "SELECT u.* FROM users u JOIN friendships f ON u.username = f.user2 WHERE f.user1 = ? UNION SELECT u.* FROM users u JOIN friendships f ON u.username = f.user1 WHERE f.user2 = ?";

        try (Connection connection = getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, username);
            statement.setString(2, username);
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                User friend = extractUserFromResultSet(resultSet);
                friends.add(friend);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return friends;
    }

    public static LinkedList<User> fetchFriendRequests(String username) {
        LinkedList<User> friendRequests = new LinkedList<>();
        String sql = "SELECT sender_username FROM friend_requests WHERE recipient_username = ?";
        try (Connection connection = getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, username);
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                String senderUsername = resultSet.getString("sender_username");
                // Fetch the user object corresponding to the sender username
                User sender = fetchUserByUsername(senderUsername);
                friendRequests.add(sender);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return friendRequests;
    }

    public static void sendFriendRequest(User sender, User recipient) {
        // Insert a record into the friend_requests table
        String sql = "INSERT INTO userdb.friend_requests (sender_username, recipient_username) VALUES (?, ?)";
        try (Connection connection = DBOperations.getConnection();
            PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, sender.getUsername());
            statement.setString(2, recipient.getUsername());
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void acceptFriendRequest(User sender, User recipient) {
        // SQL statements for inserting into friendships and deleting from friend_requests
        String insertSql = "INSERT INTO userdb.friendships (user1, user2) VALUES (?, ?)";
        String deleteSql = "DELETE FROM userdb.friend_requests WHERE sender_username = ? AND recipient_username = ?";
    
        try (Connection connection = DBOperations.getConnection()) {
            // Disable auto-commit mode to handle transactions manually
            connection.setAutoCommit(false);

            try (PreparedStatement insertStatement = connection.prepareStatement(insertSql);
                 PreparedStatement deleteStatement = connection.prepareStatement(deleteSql)) {
            
                // Set parameters for the insert statement
                insertStatement.setString(1, sender.getUsername());
                insertStatement.setString(2, recipient.getUsername());
                insertStatement.executeUpdate();

                // Set parameters for the delete statement
                deleteStatement.setString(1, sender.getUsername());
                deleteStatement.setString(2, recipient.getUsername());
                deleteStatement.executeUpdate();

                // Commit the transaction
                connection.commit();
            } catch (SQLException ex) {
                // Rollback the transaction in case of an error
                connection.rollback();
                ex.printStackTrace();
            } finally {
                // Re-enable auto-commit mode
                connection.setAutoCommit(true);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    public static void rejectFriendRequest(User sender, User recipient) {
        // Remove the friend request from the friend_requests table
        String sql = "DELETE FROM userdb.friend_requests WHERE sender_username = ? AND recipient_username = ?";
        try (Connection connection = DBOperations.getConnection();
            PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, sender.getUsername());
            statement.setString(2, recipient.getUsername());
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void addFriendRequest(User sender, User recipient) {
        // Insert a record into the friend_requests table
        String sql = "INSERT INTO userdb.friend_requests (sender_username, recipient_username) VALUES (?, ?)";
        try (Connection connection = DBOperations.getConnection();
            PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, sender.getUsername());
            statement.setString(2, recipient.getUsername());
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
       }
    }
}
