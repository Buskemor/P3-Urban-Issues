package Controller;

import Model.*;
import javafx.util.Pair;

import java.sql.*;
import java.util.ArrayList;

public class DbAdmin extends DbManager {
    public DbAdmin(String username, String password, String ip, String port, String schema) {
        super(username, password, ip, port, schema);
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

    public ArrayList<Issue> fetchAllIssues() {
        ArrayList<Issue> issues = new ArrayList<>();
        try {
            PreparedStatement selectAllQuery = connection.prepareStatement(
                "SELECT issues.issue_id, date, locations.road, locations.housenumber, locations.location_id, " +
                    "categories.category_id, categories.category, `description`, statuses.status_id, citizens.email " +
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
                    case 4 -> Status.CANCELLED;
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

    public ArrayList<Issue> fetchIssuesByCategories(ArrayList<Pair<Integer, String>> selectedCategories, ArrayList<Issue> issues) {
        ArrayList<Issue> issuestemp = new ArrayList<>();
        for(Issue issue : issues)
            for(Pair<Integer, String> category : selectedCategories)
                if (issue.getCategory().equals(category))
                    issuestemp.add(issue);

        return issuestemp;
    }

    public boolean checkIfCategoryHasIssues(String categoryName) {
        String query = "SELECT COUNT(*) AS issueCount FROM issues " +
                "INNER JOIN categories ON issues.category_id = categories.category_id " +
                "WHERE categories.category = ?";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, categoryName);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                int issueCount = resultSet.getInt("issueCount");
                return issueCount > 0;
            }
        } catch (SQLException e) {
            System.out.println("Error checking category issues: " + e.getMessage());
        }
        return false;
    }

    public boolean deleteCategory(String categoryName) {
        try {
            connection.setAutoCommit(false); // Begin transaction

            PreparedStatement categoryCheckStmt = connection.prepareStatement(
                    "SELECT category_id FROM categories WHERE category = ?");
            categoryCheckStmt.setString(1, categoryName);
            ResultSet result = categoryCheckStmt.executeQuery();

            if (!result.next()) {
                System.out.println("Category not found.");
                return false;
            }

            int categoryId = result.getInt("category_id");

            PreparedStatement reassignStmt = connection.prepareStatement(
                    "UPDATE issues SET category_id = " +
                            "(SELECT category_id FROM categories WHERE category = 'deletedcategory') " +
                            "WHERE category_id = ?");
            reassignStmt.setInt(1, categoryId);
            reassignStmt.executeUpdate();

            System.out.println(categoryId);
            PreparedStatement deleteStmt = connection.prepareStatement(
                    "DELETE FROM categories WHERE category_id = ?");
            deleteStmt.setInt(1, categoryId);
            int rowsAffected = deleteStmt.executeUpdate();
            System.out.println(categoryId);

            connection.commit(); // Commit transaction
            return rowsAffected > 0;

        } catch (SQLException e) {
            try {
                connection.rollback();
            } catch (SQLException rollbackEx) {
                System.out.println("Rollback failed: " + rollbackEx.getMessage());
            }
            System.out.println("Error deleting category: " + e.getMessage());
            } finally {
            try {
                connection.setAutoCommit(true);
            } catch (SQLException e) {
                System.out.println("Failed to reset auto-commit: " + e.getMessage());
            }
        }
        return false;
    }



}