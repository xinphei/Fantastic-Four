package assignmentds;

import assignmentds.Quiz;

import java.awt.Desktop;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.sql.Timestamp;

public class AttemptQuiz {
    private static List<Quiz> availableQuizzes = Quiz.getQuizzes();
    private static List<String> completedQuizzes = new ArrayList<>();
    private static Scanner scanner = new Scanner(System.in);

    public static void main(User user) {
        System.out.println("   _   _   _                       _       ____       _     ");     
        System.out.println("  /_\\ | |_| |_ ___ _ __ ___  _ __ | |_    /___ \\_   _(_)____");
        System.out.println(" //_\\\\| __| __/ _ \\ '_ ` _ \\| '_ \\| __|  //  / / | | | |_  /");
        System.out.println("/  _  \\ |_| ||  __/ | | | | | |_) | |_  / \\_/ /| |_| | |/ / "); 
        System.out.println("\\_/ \\_/\\__|\\__\\___|_| |_| |_| .__/ \\__| \\___,_\\ \\__,_|_/___|");
        System.out.println("                            |_|");                            

        System.out.println("Welcome to the Quiz Attempter!");
        
        // Initialize quizzes
        Quiz.initializeQuiz();
        
        // Display theme options and select themes
        List<String> selectedThemes = selectThemes();

        // Allow student to select and attempt quizzes
        selectAndAttemptQuizzes(selectedThemes, user);

        Home.main(user);
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
            System.out.print("Enter theme numbers: ");
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
                    quizInfo[index][1] = completedQuizzes.contains(quiz.getTitle()) ? "Complete" : "Incomplete"; // Completion status
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
            System.out.println("\nSelect a quiz to attempt (or type 'done' to finish):");
            String selectedQuizNumber = scanner.nextLine();

            if (selectedQuizNumber.equalsIgnoreCase("done")) {
                continueAttempting = false;
            } else {
                // Attempt to parse the input as an integer
                try {
                    int quizNumber = Integer.parseInt(selectedQuizNumber);
                    // Find the selected quiz by number
                    if (quizNumber >= 1 && quizNumber <= availableQuizzes.size()) {
                        Quiz selectedQuiz = null;
                        int count = 0;
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
                            // Simulate attempting the quiz
                            System.out.println("Attempting quiz: " + selectedQuiz.getTitle());
                            // Open the quiz URL in a web browser
                            openQuizUrl(selectedQuiz.getQuizizzLink());
                            
                            // Mark the quiz as completed
                            completedQuizzes.add(selectedQuiz.getTitle());
                            // Award 2 marks to the student
                            System.out.println("Congratulations! You have completed the quiz. You have been awarded 2 marks.");
                            update += 2;
                            Timestamp now = Timestamp.valueOf(LocalDateTime.now());

                            // Update points in the database
                            DBOperations.updateCurrentPoints(user.getEmail(), update, now);
                            System.out.println("Your existing points: " + update);
                        } else {
                            System.out.println("Quiz not found. Please select a valid quiz.");
                        }
                    } else {
                        System.out.println("Invalid quiz number. Please select a valid quiz number.");
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
