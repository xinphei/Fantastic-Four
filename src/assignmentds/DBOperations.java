package assignmentds;

import java.sql.*;
import java.util.LinkedList;
import java.util.Properties;

public class DBOperations {

    private static String url = "jdbc:mysql://127.0.0.1:3306/userdb"; //url format is jdbc:mysql://<database number>/<database name>
    private static String DBuser = "root"; //user usually is "root"
    private static String pw = "2416"; //your password

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
