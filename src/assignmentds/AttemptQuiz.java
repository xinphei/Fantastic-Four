package assignmentds;

import assignmentds.Quiz;

import java.awt.Desktop;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.net.URI;
import java.net.URISyntaxException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.sql.Timestamp;
import java.io.FileWriter;
import java.io.IOException;


public class AttemptQuiz {
    private static List<Quiz> availableQuizzes = Quiz.getQuizzes();
    private static Scanner scanner = new Scanner(System.in);
    private static final String COMPLETED_QUIZZES_FILE = "completed_quizzes.txt";
    private static List<String> completedQuizzes;
    static String red = "\u001B[31m";
    static String green = "\u001B[32m";
    static String reset = "\u001B[0m";
    static String blue = "\u001B[34m";
    static String cyan = "\u001B[36m";

    public static void main(User user) {
        System.out.println(blue + "   _   _   _                       _       ____       _     ");     
        System.out.println(blue + "  /_\\ | |_| |_ ___ _ __ ___  _ __ | |_    /___ \\_   _(_)____");
        System.out.println(blue + " //_\\\\| __| __/ _ \\ '_ ` _ \\| '_ \\| __|  //  / / | | | |_  /");
        System.out.println(blue + "/  _  \\ |_| ||  __/ | | | | | |_) | |_  / \\_/ /| |_| | |/ / "); 
        System.out.println(blue + "\\_/ \\_/\\__|\\__\\___|_| |_| |_| .__/ \\__| \\___,_\\ \\__,_|_/___|");
        System.out.println(blue + "                            |_|");                            

        System.out.println("Welcome to the Quiz Attempter!");
        
        // Load complete quizzes from file
        completedQuizzes = loadCompletedQuizzes(user.getUsername());
        
        // Initialize quizzes
        Quiz.initializeQuiz();
        
        // Display theme options and select themes
        List<String> selectedThemes = selectThemes();

        // Allow student to select and attempt quizzes
        selectAndAttemptQuizzes(selectedThemes, user);

        Home.main(user);
    }
    
    private static List<String> loadCompletedQuizzes(String username) {
        List<String> completedQuizzes = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(COMPLETED_QUIZZES_FILE))) {
            String line;
            while ((line = reader.readLine()) != null) {
                if (line.startsWith(username + ": ")) {
                    completedQuizzes.add(line.substring(username.length() + 2)); // Remove username prefix
                }
            }
        } catch (IOException e) {
            System.out.println("Error loading completed quizzes: " + e.getMessage());
        }
        return completedQuizzes;
    }
    
    private static void saveCompletedQuiz(String username, String completedQuiz) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(COMPLETED_QUIZZES_FILE, true))) {
            writer.write(username + ": " + completedQuiz + System.lineSeparator());
        } catch (IOException e) {
            System.out.println("Error saving completed quizzes: " + e.getMessage());
        }
    }


    private static List<String> selectThemes() {
        List<String> selectedThemes = new ArrayList<>();
        boolean validInput = false;

        while (!validInput) {
            System.out.println("\nSelect themes to filter quizzes by entering their numbers separated by spaces (e.g. 1 2):");
            System.out.println("0. Default (all selected)");
            System.out.println("1. Science");
            System.out.println("2. Technology");
            System.out.println("3. Engineering");
            System.out.println("4. Mathematics");
            System.out.print(cyan + "Enter theme numbers: " + reset);
            String themeInput = scanner.nextLine();
            String[] themeNumbers = themeInput.split(" ");

            for (String number : themeNumbers) {
                switch (number) {
                    case "0":
                        selectedThemes.add("Science");
                        selectedThemes.add("Technology");
                        selectedThemes.add("Engineering");
                        selectedThemes.add("Mathematics");
                        validInput = true; // Exit loop
                        break;
                    case "1":
                        selectedThemes.add("Science");
                        break;
                    case "2":
                        selectedThemes.add("Technology");
                        break;
                    case "3":
                        selectedThemes.add("Engineering");
                        break;
                    case "4":
                        selectedThemes.add("Mathematics");
                        break;
                    default:
                        System.out.println("Invalid theme number: " + number);
                        selectedThemes.clear(); // Clear previously selected themes
                        break;
                }
            }

            if (!selectedThemes.isEmpty()) {
                validInput = true;
            }
        }
        return selectedThemes;
    }

    private static void displayQuizzes(List<String> selectedThemes, String[][] quizInfo) {
        System.out.println("\nAvailable Quizzes:");
        int counter = 1;
        int index = 0;
        for (String theme : selectedThemes) {
            System.out.println("\n" + theme.toUpperCase()); // Display the theme in uppercase
            System.out.println("-------------"); // Underline the theme
            for (Quiz quiz : availableQuizzes) {
                if (quiz.getTheme().equalsIgnoreCase(theme)) {
                    quizInfo[index][0] = quiz.getTitle(); // Quiz name
                    quizInfo[index][1] = completedQuizzes.contains(quiz.getTitle()) ? green+"Complete"+reset : red+"Incomplete"+reset; // Completion status
                    System.out.println(counter + ". " + quiz.getTitle() + " [" + quizInfo[index][1] + "] ");
                    counter++;
                    index++;
                }
            }
        }

        // If no quizzes found for the selected themes
        if (counter == 1) {
            System.out.println("No quizzes available for the selected themes.");
        }
    }
    
    
    private static void selectAndAttemptQuizzes(List<String> selectedThemes, User user) {
        boolean continueAttempting = true;
        int update = user.getCurrentPoints();
        String[][] quizInfo = new String[selectedThemes.size() * availableQuizzes.size()][2];

        while (continueAttempting) {
            displayQuizzes(selectedThemes, quizInfo);
            System.out.println(cyan + "\nSelect a quiz to attempt (or type 'done' to finish):" + reset);
            String selectedQuizNumber = scanner.nextLine();
    
            if (selectedQuizNumber.equalsIgnoreCase("done")) {
                continueAttempting = false;
            } else {
                try {
                    int quizNumber = Integer.parseInt(selectedQuizNumber);
                    int count = 0;
                    Quiz selectedQuiz = null;
    
                    for (Quiz quiz : availableQuizzes) {
                        if (selectedThemes.contains(quiz.getTheme())) {
                            count++;
                            if (count == quizNumber) {
                                selectedQuiz = quiz;
                                break;
                            }
                        }
                    }
    
                    if (selectedQuiz != null) {
                        System.out.println("Attempting quiz: " + selectedQuiz.getTitle());
                        openQuizUrl(selectedQuiz.getQuizizzLink());
    
                        if (!completedQuizzes.contains(selectedQuiz.getTitle())) {
                            completedQuizzes.add(selectedQuiz.getTitle());
                            saveCompletedQuiz(user.getUsername(), selectedQuiz.getTitle());
                            System.out.println("Congratulations! You have completed the quiz. You have been awarded 2 marks.");
                            update += 2;
                            Timestamp now = Timestamp.valueOf(LocalDateTime.now());
    
                            DBOperations.updateCurrentPoints(user.getEmail(), update, now);
                            System.out.println("Your existing points: " + green + update + reset);
                        } else {
                            System.out.println("You have already completed this quiz.");
                        }
                    } else {
                        System.out.println("Quiz not found. Please select a valid quiz.");
                    }
                } catch (NumberFormatException e) {
                   System.out.println("Invalid input. Please enter a valid quiz number or 'done'.");
                } catch (IOException | URISyntaxException e) {
                    System.out.println("Error opening quiz URL: " + e.getMessage());
                }
            }
        }
    }


    private static void openQuizUrl(String url) throws IOException, URISyntaxException {
        if (Desktop.isDesktopSupported()) {
            Desktop desktop = Desktop.getDesktop();
            desktop.browse(new URI(url));
        } else {
            System.out.println("Desktop API is not supported. Please open the following URL manually: " + url);
        }
    }
}
