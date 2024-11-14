package Controller;

import Model.*;
import javafx.util.Pair;

import java.sql.*;
import java.util.ArrayList;

public class DbManager {
    public static void main(String[] args) {

//        issueInserter = new DbInserter(location, finalhousenumber, description, finalCategory, new Citizen(email));

        DbManager ta = new DbManager();
        System.out.println(ta.fetchCategories());
//        DbInserter = new DbInserter(new Issue())

//        ArrayList<Issue> gg = ta.fetchAllIssues();

//        for (Issue element : gg) {
//            System.out.println("Date: " + element.getDate());
//            System.out.println("Status: " + element.getStatus());
//            System.out.println("Road: " + element.getRoad());
//            System.out.println("House Number: " + element.getHouseNumber());
//            System.out.println("Description: " + element.getDescription());
//            System.out.println("Category: " + element.getCategory().getValue());
//            System.out.println("Reported By: " + element.getReportedBy());
//            System.out.println("Issue ID: " + element.getIssueId());
//            System.out.println(); // Blank line for readability between elements
//        }

//        for(Pair<Integer, String> cat : ta.categories) {
//            System.out.println(cat);
//        }

//        ArrayList<Issue> tata = new ArrayList<>();
//        ArrayList<Pair<Integer, String>> ta2 = new ArrayList<>();
//        ta2.add(new Pair<>(1, "Other"));
//
//        tata = ta.fetchIssuesByCategories(ta2);
//
//        for(Issue issue : tata)
//            System.out.println(issue.getIssueId() + " " + issue.getCategory());

//        ArrayList<Category> cats = new ArrayList<Category>();
//        cats.add(Category.ELECTRICAL);
//        cats.add(Category.OBSTRUCTION);
//
//        ArrayList<Issue> issuesOfCat = ta.fetchIssuesByCategories(cats);
//
//        for(Issue element : issuesOfCat) {
//            System.out.println(element.getCategory());
//        }


    }

    private Connection connection;

    public DbManager() {
        String url = "jdbc:mysql://localhost:3306/issuesdb";
        String username = "root";
        String password = "KENDATABASE123";
        try {
            this.connection = DriverManager.getConnection(url, username, password);
        } catch (SQLException e) {
            System.out.println(e);
        }
    }

    public int convertEnumToSQLId(Enum<?> enumeration) {
        return enumeration.ordinal() + 1;
    }


    public void updateIssueStatus(Issue issue, Status newStatus) {
        int issueId = issue.getIssueId();
        try (PreparedStatement updateStatusQuery = connection.prepareStatement(
                "UPDATE issues SET status_id = ? WHERE issue_id = ?")) {
            updateStatusQuery.setInt(1, convertEnumToSQLId(newStatus));
            updateStatusQuery.setInt(2, issueId);
            updateStatusQuery.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e);
        }
    }

    private ArrayList<Issue> fetchAllIssues() {
        ArrayList<Issue> issues = new ArrayList<>();
        try {
            PreparedStatement selectAllQuery = connection.prepareStatement(
                "SELECT issues.issue_id, date, locations.road, locations.housenumber, locations.location_id, categories.category_id, categories.category, `description`, statuses.status_id, citizens.email " +
                    "FROM issues " +
                    "INNER JOIN locations ON locations.location_id = issues.location_id " +
                    "INNER JOIN statuses ON statuses.status_id = issues.status_id " +
                    "INNER JOIN categories ON categories.category_id = issues.category_id " +
                    "LEFT OUTER JOIN citizens ON citizens.citizen_id = issues.citizen_id " +
                    "ORDER BY date");

            ResultSet selectAllResult = selectAllQuery.executeQuery();

            int issueId = 0;
            Date date = null;
            String road = null;
            int houseNumber = 0;
            String description = null;
            int categoryId = 0;
            String categoryString = null;
            int statusId = 0;
            String email = null;

            ArrayList<Pair<Integer, String>> categories = new ArrayList<>();

            while(selectAllResult.next()) {
                issueId = selectAllResult.getInt("issue_id");
                date = selectAllResult.getDate("date");
                road = selectAllResult.getString("road");
                houseNumber = selectAllResult.getInt("housenumber");
                description = selectAllResult.getString("description");
                categoryId = selectAllResult.getInt("category_id");
                categoryString = selectAllResult.getString("category");
                statusId = selectAllResult.getInt("status_id");
                email = selectAllResult.getString("email");

                Pair<Integer, String> category = new Pair<>(categoryId, categoryString.substring(0,1).toUpperCase() + categoryString.substring(1));

                Status status = switch (statusId) {
                    case 1 -> Status.PENDING;
                    case 2 -> Status.IN_PROGRESS;
                    case 3 -> Status.RESOLVED;
                    default -> null;
                };

                if(email == null)
                    issues.add(new Issue(issueId, date, road, houseNumber, description, category, status));
                else {
                    issues.add(new Issue(issueId, date, road, houseNumber, description, category, status, new Citizen(email)));
                }
            }
            return issues;

        } catch (SQLException e) {
            System.out.println(e);
        }
        return issues;
    }

    public ArrayList<Pair<Integer, String>> fetchCategories() {
        try {
            PreparedStatement query = connection.prepareStatement("SELECT category, category_id FROM categories");
            ResultSet result = query.executeQuery();

            ArrayList<Pair<Integer, String>> categories = new ArrayList<>();
            while(result.next()) {
                categories.add(new Pair<>(
                    result.getInt("category_id"),
//                    Make the first letter in the category upper case
                    result.getString("category").substring(0,1).toUpperCase()+result.getString("category").substring(1))
                );
            }
            return categories;

        } catch (SQLException e) {
            System.out.println(e);
        }
        throw new RuntimeException("No categories");
    }

    public boolean addNewCategory(String newCategory) {
        String query = "INSERT INTO categories(category) VALUES(?)";
        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setString(1, newCategory);
            int rowsAffected = preparedStatement.executeUpdate();
            return rowsAffected > 0; // Returns true if the insertion was successful
        } catch (SQLException e) {
            System.out.println("Error adding new category: " + e.getMessage());
            return false; // If there was an error
        }
    }

    public ArrayList<Issue> fetchIssuesByCategories(ArrayList<Pair<Integer, String>> selectedCategories) {
        ArrayList<Issue> issues = new ArrayList<>();
        for(Issue issue : this.fetchAllIssues())
            for(Pair<Integer, String> category : selectedCategories) {
                if(issue.getCategory().equals(category))
                    issues.add(issue);
            }

        return issues;
    }
}