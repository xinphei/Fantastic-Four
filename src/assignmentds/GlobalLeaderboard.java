package assignmentds;

import assignmentds.User;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class GlobalLeaderboard {
    
    private List<User> users;

    public GlobalLeaderboard() {
        this.users = new ArrayList<>();
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
        // Sort users based on current points and timestamp of last point update
        users.sort(Comparator.comparing((User user) -> user.getCurrentPoints()).reversed()
                        .thenComparing((User user) -> user.getPointLastUpdated()).reversed());
    
        // Display leaderboard
        System.out.println("Global Leaderboard:");
        System.out.println("Username\tCurrent Points");
        for (User user : users) {
            System.out.println(user.getUsername() + "\t\t" + user.getCurrentPoints());
        }
    }
}