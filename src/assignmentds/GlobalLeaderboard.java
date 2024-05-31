package assignmentds;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class GlobalLeaderboard {
    
    private List<User> users;
    static String reset = "\u001B[0m";
    static String blue_background = "\u001B[44m";

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
        System.out.println(" _______   ___        ______    _______       __      ___           ___       _______       __       ________    _______   _______   _______     ______      __        _______   ________   \n" +
        " /\" _   \"| |\"  |      /    \" \\  |   _  \"\\     /\"\"\\    |\"  |         |\"  |     /\"     \"|     /\"\"\\     |\"      \"\\  /\"     \"| /\"      \\ |   _  \"\\   /    \" \\    /\"\"\\      /\"      \\ |\"      \"\\  \n" +
        "(: ( \\___) ||  |     // ____  \\ (. |_)  :)   /    \\   ||  |         ||  |    (: ______)    /    \\    (.  ___  :)(: ______)|:        |(. |_)  :) // ____  \\  /    \\    |:        |(.  ___  :) \n" +
        " \\/ \\      |:  |    /  /    ) :)|:     \\/   /' /\\  \\  |:  |         |:  |     \\/    |     /' /\\  \\   |: \\   ) || \\/    |  |_____/   )|:     \\/ /  /    ) :)/' /\\  \\   |_____/   )|: \\   ) || \n" +
        " //  \\ ___  \\  |___(: (____/ // (|  _  \\\\  //  __'  \\  \\  |___       \\  |___  // ___)_   //  __'  \\  (| (___\\ || // ___)_  //      / (|  _  \\\\(: (____/ ////  __'  \\   //      / (| (___\\ || \n" +
        "(:   _(  _|( \\_|:  \\\\        /  |: |_)  :)/   /  \\\\  \\( \\_|:  \\     ( \\_|:  \\(:      \"| /   /  \\\\  \\ |:       :)(:      \"||:  __   \\ |: |_)  :)\\        //   /  \\\\  \\ |:  __   \\ |:       :) \n" +
        " \\_______)  \\_______)\\\"_____/   (_______/(___/    \\___)\\_______)     \\_______)\\_______)(___/    \\___)(________/  \\_______)|__|  \\___)(_______/  \\\"_____/(___/    \\___)|__|  \\___)(________/  \n" +
        "                                                                                                                                                                                             ");
        System.out.println("");
        System.out.printf("%-20s %s\n", blue_background+"Username", "Current Points"+reset);
        System.out.println("");
        for (User user : studentUsers) {
            System.out.printf("%-20s %d\n", user.getUsername(), user.getCurrentPoints());
        }
    }
}
