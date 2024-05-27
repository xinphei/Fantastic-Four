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
        System.out.println("Global Leaderboard:");
        System.out.printf("%-20s %s\n", "Username", "Current Points");
        for (User user : studentUsers) {
            System.out.printf("%-20s %d\n", user.getUsername(), user.getCurrentPoints());
        }
    }
}
