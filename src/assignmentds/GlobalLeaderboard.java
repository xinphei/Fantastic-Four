package assignmentds;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class GlobalLeaderboard {
    
    private List<User> users;

    public GlobalLeaderboard() {
        this.users = DBOperations.fetchAllUsers();
    }
    
    public List<User> getUsers() {
        return users;
    }

    public static void main(User user) {
        // Create an instance of GlobalLeaderboard
        GlobalLeaderboard leaderboard = new GlobalLeaderboard();
    
        // Display leaderboard
        leaderboard.displayLeaderboard();
        Home.main(user);
    }

    public void displayLeaderboard() {
        // Ensure users list is not null
        if (users == null || users.isEmpty()) {
            System.out.println("No users found.");
            return;
        }

        // Filter users based on role equal to 1 (student)
        List<User> studentUsers = users.stream()
                .filter(user -> user.getRole() == 1)
                .collect(Collectors.toList());

        // Sort student users based on current points (descending) and timestamp of last point update (ascending)
        studentUsers.sort(
            Comparator.comparing(User::getCurrentPoints, Comparator.nullsLast(Comparator.reverseOrder()))
                      .thenComparing(User::getPointLastUpdated, Comparator.nullsLast(Comparator.naturalOrder()))
        );

        // Display leaderboard
        String blue = "\u001B[34m";
        System.out.println(blue + "   ___ _       _           _     __                _           _                         _ \n" +
        blue + "  / _ \\ | ___ | |__   __ _| |   / /  ___  __ _  __| | ___ _ __| |__   ___   __ _ _ __ __| |\n" +
        blue + " / /_\\/ |/ _ \\| '_ \\ / _` | |  / /  / _ \\/ _` |/ _` |/ _ \\ '__| '_ \\ / _ \\ / _` | '__/ _` |\n" +
        blue + "/ /_\\\\| | (_) | |_) | (_| | | / /__|  __/ (_| | (_| |  __/ |  | |_) | (_) | (_| | | | (_| |\n" +
        blue + "\\____/|_|\\___/|_.__/ \\__,_|_| \\____/\\___|\\__,_|\\__,_|\\___|_|  |_.__/ \\___/ \\__,_|_|  \\__,_|\n" +
        blue + "                                                                                           ");
        System.out.println("");
        System.out.printf("%-20s %s\n", "Username", "Current Points");
        System.out.println("");
        for (User user : studentUsers) {
            System.out.printf("%-20s %d\n", user.getUsername(), user.getCurrentPoints());
        }
    }
}
