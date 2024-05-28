package assignmentds;

import java.sql.*;
import java.util.*;
public class ParentChildRelationship {
    public static List<String> loadParent(String username){
        List<String> parents = new ArrayList<>();
        try {
            Connection connection = DBOperations.getConnection();
            String query = "SELECT parent_username FROM userdb.parentchildrelationship WHERE child_username = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, username);
            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                String parent = resultSet.getString("parent_username");
                parents.add(parent);
            }

        } catch (SQLException e) {
            System.out.println("Error with loading parents : " + e.getMessage());
        }
        return parents;
    }

    public static List<String> loadChildren(String username){
        List<String> children = new ArrayList<>();
        try {
            Connection connection = DBOperations.getConnection();
            String query = "SELECT child_username FROM userdb.parentchildrelationship WHERE parent_username = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, username);
            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                String child = resultSet.getString("child_username");
                children.add(child);
            }

        } catch (SQLException e) {
            System.out.println("Error in loading children : " + e.getMessage());
        }
        return children;
    }
}
