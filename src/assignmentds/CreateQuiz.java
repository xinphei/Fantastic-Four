package assignmentds;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CreateQuiz {
    
    private static int numQuizCreated = 0;
    
    private static final String[] VALID_THEMES = {"Science", "Technology", "Engineering", "Mathematics"};

    public static int getNumQuizzesCreated(String username, int role) {
        return getQuizNum(username, role);
    }
    
    public static void main(User user) {
        String reset = "\u001B[0m";
        String blue = "\u001B[34m";
        String cyan = "\u001B[36m";
        String magenta = "\u001B[35m";
        System.out.println(blue + "   ___               _           ____       _         ___                   \n" +
                "  / __\\ __ ___  __ _| |_ ___    /___ \\_   _(_)____   / _ \\__ _  __ _  ___ _ \n" +
                " / / | '__/ _ \\/ _` | __/ _ \\  //  / / | | | |_  /  / /_)/ _` |/ _` |/ _ (_)\n" +
                "/ /__| | |  __/ (_| | ||  __/ / \\_/ /| |_| | |/ /  / ___/ (_| | (_| |  __/_ \n" +
                "\\____/_|  \\___|\\__,_|\\__\\___| \\___,_\\ \\__,_|_/___| \\/    \\__,_|\\__, |\\___(_)\n" +
                "                                                               |___/        ");

        Quiz.initializeQuiz();
        Scanner sc = new Scanner(System.in);
        boolean createAnotherQuiz = true;

        while (createAnotherQuiz) {
            // Prompt user for quiz information
            System.out.println(cyan + "What's the title of the quiz? ");
            String title = sc.nextLine();

            System.out.println(cyan + "What's the description of the quiz? ");
            String description = sc.nextLine();

            String theme = null;
            boolean validTheme = false;
            while (!validTheme) {
                System.out.println(cyan + "What's the theme of the quiz? (Science, Technology, Engineering, Mathematics)");
                theme = sc.nextLine();
                validTheme = isValidTheme(theme);
                if (!validTheme) {
                    System.out.println("Invalid theme. Please enter one of the following: Science, Technology, Engineering, Mathematics");
                }
            }

            String quizizzLink = null;
            boolean validLink = false;
            while (!validLink) {
                System.out.println(cyan + "What's the Quizizz link? ");
                quizizzLink = sc.nextLine();
                validLink = isValidURL(quizizzLink);
                if (!validLink) {
                    System.out.println("Invalid URL format for Quizizz link. Please enter a valid URL.");
                }
            }
            Quiz newQuiz = new Quiz(title, description, theme, quizizzLink);
            Quiz.addQuiz(newQuiz);
            
            numQuizCreated++;

            System.out.println(magenta + " __                              __       _ _                             _           _   _ \n" +
                    "/ _\\_   _  ___ ___ ___  ___ ___ / _|_   _| | |_   _    ___ _ __ ___  __ _| |_ ___  __| | / \\\n" +
                    "\\ \\| | | |/ __/ __/ _ \\/ __/ __| |_| | | | | | | | |  / __| '__/ _ \\/ _` | __/ _ \\/ _` |/  /\n" +
                    "_\\ \\ |_| | (_| (_|  __/\\__ \\__ \\  _| |_| | | | |_| | | (__| | |  __/ (_| | ||  __/ (_| /\\_/ \n" +
                    "\\__/\\__,_|\\___\\___\\___||___/___/_|  \\__,_|_|_|\\__, |  \\___|_|  \\___|\\__,_|\\__\\___|\\__,_\\/   \n" +
                    "                                              |___/                                         \n");
            System.out.println(Quiz.getQuizzes().get(Quiz.getQuizzes().size() - 1).toString());

            // Ask if the user wants to create another quiz
            System.out.println("Do you want to create another quiz? (yes/no): ");
            String input = sc.nextLine();
            createAnotherQuiz = input.equalsIgnoreCase("yes");
        }
        Quiz.writeQuizzesToFile("quizzes.txt");
        updateQuizNum(user.getUsername(), numQuizCreated, user.getRole());
        Home.main(user);
        sc.close();
    }

    private static boolean isValidTheme(String theme) {
        for (String validTheme : VALID_THEMES) {
            if (validTheme.equalsIgnoreCase(theme)) {
                return true;
            }
        }
        return false;
    }

    private static boolean isValidURL(String url) {
        // Regular expression to match common URL patterns
        String regex = "^((https?|ftp|smtp):\\/\\/)?(www.)?[a-zA-Z0-9-]+(.[a-zA-Z]{2,3})(:\\d{1,5})?([\\/a-zA-Z0-9_.\\-\\+%]*)*(\\?[a-zA-Z0-9_]+=[a-zA-Z0-9_.\\-\\+%]*)?(#[a-zA-Z0-9_]*)?$";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(url);
        return matcher.matches();
    }

    private static int getQuizNum(String username, int role) {
        String sql = "SELECT quizNum FROM userdb.users WHERE username = ? AND role = ?";
        try (Connection conn = DBOperations.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, username);
            pstmt.setInt(2, role);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                return rs.getInt("quizNum");
            }
        } catch (SQLException e) {
            System.out.println("Error retrieving quiz number: " + e.getMessage());
        }
        return 0;
    }

    public static void updateQuizNum(String username, int newQuizzesCount, int role) {
        int currentQuizNum = getQuizNum(username, role);
        int updatedQuizNum = currentQuizNum + newQuizzesCount;

        String sql = "UPDATE userdb.users SET quizNum = ? WHERE username = ? AND role = ?";
        try (Connection conn = DBOperations.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, updatedQuizNum);
            pstmt.setString(2, username);
            pstmt.setInt(3, role);
            int affectedRows = pstmt.executeUpdate();
            if (affectedRows > 0) {
                System.out.println("\nQuiz number updated successfully.\n");
            } else {
                System.out.println("No records updated, check educator ID.");
            }
        } catch (SQLException e) {
            System.out.println("Error updating quiz number: " + e.getMessage());
        }
    }

}
