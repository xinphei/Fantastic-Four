package assignmentds;

import java.sql.*;
import java.util.*;
class Graph {
    private Map<String, List<String>> adjacencyList;

    public Graph() {
        adjacencyList = new HashMap<>();
    }

    public void addEdge(String parent, String child) {
        if (!adjacencyList.containsKey(parent)) {
            adjacencyList.put(parent, new ArrayList<>());
        }
        adjacencyList.get(parent).add(child);
    }

    public List<String> getChildren(String parent) {
        return adjacencyList.getOrDefault(parent, new ArrayList<>());
    }

    public void display() {
        for (Map.Entry<String, List<String>> entry : adjacencyList.entrySet()) {
            String parent = entry.getKey();
            List<String> children = entry.getValue();
            System.out.println("Parent: " + parent);
            System.out.println("Children: " + children);
            System.out.println();
        }
    }
}

public class ParentChildRelationship {
    public static void main(String[] args) {
        Graph graph = new Graph();

        try{
            Connection connection = DriverManager.getConnection(
                    "jdbc:mysql://127.0.0.1:3306/login_schema", //url format is jdbc:mysql://<database number>/<database name>
                    "root", //user usually is "root"
                    "password@" //put your password
            );

            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery("SELECT * FROM login_schema.parentchildrelationship");

            while(resultSet.next()){
                String parent = resultSet.getString("parent_username");
                String child = resultSet.getString("child_username");
                graph.addEdge(parent, child); //adding parent-child relationship from the database table
            }
        }catch (SQLException e){
            e.printStackTrace();;
        }
//
//        // Adding parent-child relationships (hard code)
//        graph.addEdge("TanChinPeng", "Adamtan09");
//        graph.addEdge("TanChinPeng", "Laura_tan");
//        graph.addEdge("firdaus_an", "ahmadfirdaus07");
//        graph.addEdge("Santya24", "reelansantya");
//        graph.addEdge("noobmaster68", "loremipsum96");
//        graph.addEdge("johndoe3698", "yinjiadoe20");
//        graph.addEdge("TanChinPeng", "jason0319");
//        graph.addEdge("johndoe3698", "katyln_doe");
//        graph.addEdge("aliabdul10", "Samadabdul");
//        graph.addEdge("Nevergonna19", "giveYouup3");

        // Displaying parent-child relationships
        graph.display();
    }
}
