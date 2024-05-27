package assignmentds;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class AuthenticationSystem {

    public static boolean isValidEmail(String email) {
        String emailRegex = "^[A-Za-z0-9+_.-]+@(.+)$";
        Pattern pattern = Pattern.compile(emailRegex);
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }
    
    public static boolean isValidUsername(String username) {
        if (username.contains("@")) {
            return false; // Username contains '@', invalid
        }
        
        String usernameRegex = "^[A-Za-z0-9_]{3,20}$";
        return username.matches(usernameRegex);
    }

    public static boolean isValidPassword(String pass) {
        // remove whitespace
        pass = pass.trim();

        final int NUM_UPPER_LETTERS = 1;
        final int NUM_LOWER_LETTERS = 1;
        final int NUM_SPECIAL_CHAR = 1;
        final int NUM_DIGITS = 3;
        int upperCase = 0;
        int lowerCase = 0;
        int specialChar = 0;
        int digit = 0;

        char c;
        for (int i = 0; i < pass.length(); i++) {
            c = pass.charAt(i);
            if (Character.isDigit(c)) {
                digit++;
            }
            else if (Character.isUpperCase(c)) {
                upperCase++;
            }
            else if (Character.isLowerCase(c)) {
                lowerCase++;
            }
            else if ("!@#$%^&*()-+".indexOf(c) >= 0) {
                specialChar++;
            }
        }
        return (upperCase >= NUM_UPPER_LETTERS && lowerCase >= NUM_LOWER_LETTERS && digit >= NUM_DIGITS && specialChar >= NUM_SPECIAL_CHAR && pass.length() > 7);
    }

    public static boolean isIdentifierRegistered(Connection connection, String identifier) throws SQLException {
        // Check if the provided identifier (email or username) is registered
        String query = "SELECT COUNT(*) FROM userdb.users WHERE email = ? OR username = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setString(1, identifier);
            preparedStatement.setString(2, identifier);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                resultSet.next();
                int count = resultSet.getInt(1);
                return count > 0;
            }
        }
    }
    
}
