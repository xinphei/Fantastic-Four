package assignmentds;

import java.sql.*;

public class DBTest {
    private static String url = "jdbc:mysql://localhost:3306/userdb"; //write your own url, user, and password
    private static String DBuser = "root";
    private static String pw = "password";

    public static void main(String[] args) {
        try (Connection connection = DriverManager.getConnection(url, DBuser, pw)) {
            PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM userdb.users");
            ResultSet resultSet = preparedStatement.executeQuery();

            while(resultSet.next()){
                System.out.println(resultSet.getString("username"));
                System.out.println(resultSet.getString("password"));
                System.out.println(resultSet.getString("email"));
                System.out.println();
            }

            if (connection != null) {
                System.out.println("Connection successful!");
            }
        } catch (SQLException e) {
            System.out.println(e.getErrorCode());
        }
    }
}
