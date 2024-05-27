package assignmentds;

import java.sql.*;
import java.util.Properties;

public class DBOperations {

    private static String url = "jdbc:mysql://127.0.0.1:3306/userdb"; //url format is jdbc:mysql://<database number>/<database name>
    private static String DBuser = "root"; //user usually is "root"
    private static String pw = "0123"; //your password

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
                return new User(resultSet.getString("email"), resultSet.getString("username"), hashedPassword, retrievedSalt, resultSet.getInt("role"), new Coordinate(resultSet.getDouble("locationCoordinate_X"), resultSet.getDouble("locationCoordinate_Y")), resultSet.getInt("currentPoints"));
            } else {
                System.out.println("Incorrect Password");
                return null;
            }
        } else {
            System.out.println("No user found with the provided identifier");
            return null;
        }
    }

    public static boolean updateCurrentPoints(String email, int newPoints){
        String sql = "UPDATE users SET currentPoints = ? WHERE email = ?";

        try (Connection connection = getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setInt(1, newPoints);
            statement.setString(2, email);

            int rowsUpdated = statement.executeUpdate();
            return rowsUpdated > 0; // Return true if the update was successful

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
//        Usage Example:
//        boolean success = updateUserPoints("user@example.com", 100);
//        if (success) {
//            System.out.println("Points updated successfully.");
//        } else {
//            System.out.println("Failed to update points.");
//        }

    }


}
