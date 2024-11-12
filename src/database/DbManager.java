package database;

import Iter1.*;

import java.sql.*;
import java.util.List;
import java.util.ArrayList;

public class DbManager {
    public static void main(String[] args) {

//        issueInserter = new DbInserter(location, finalhousenumber, description, finalCategory, new Citizen(email));

        DbManager ta = new DbManager();

//        DbInserter = new DbInserter(new Issue())

        ArrayList<Issue> gg = ta.fetchAllIssues();

        for (Issue element : gg) {
            System.out.println("Date: " + element.getDate());
            System.out.println("Status: " + element.getStatus());
            System.out.println("Road: " + element.getRoad());
            System.out.println("House Number: " + element.getHouseNumber());
            System.out.println("Description: " + element.getDescription());
            System.out.println("Category: " + element.getCategory());
            System.out.println("Reported By: " + element.getReportedBy());
            System.out.println("Issue ID: " + element.getIssueId());
            System.out.println(); // Blank line for readability between elements
        }

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
    private ArrayList<Issue> issues;

    public DbManager() {
        String url = "jdbc:mysql://localhost:3306/issuesdb";
        String username = "root";
        String password = "KENDATABASE123";
        try {
            this.connection = DriverManager.getConnection(url, username, password);
        } catch (SQLException e) {
            System.out.println(e);
        }

        this.issues = fetchAllIssues();
    }

    public int convertEnumToSQLId(Enum<?> enumeration) {
        return enumeration.ordinal() + 1;
    }

    public int[] sortIssues(int[] issueIds, String issuesSQLAttribute, boolean descending) {
        try {
            String issueIdsPlaceholder = "?";
            // length -1 because we already have the first "?"*/
            for (int i = 0; i < issueIds.length-1; ++i) {
                issueIdsPlaceholder += ",?";
            }

            String descendingSQL = "";
            if(descending)
                descendingSQL = " DESC";

            PreparedStatement sortIssueIdsQuery = connection.prepareStatement(
            "SELECT issue_id FROM issues WHERE issue_id IN ("+issueIdsPlaceholder+")" +
                "ORDER BY "+issuesSQLAttribute + descendingSQL);

            for (int i = 0; i < issueIds.length; ++i) {
                sortIssueIdsQuery.setInt(i+1, issueIds[i]);
            }

            ResultSet sortedIssuesResult = sortIssueIdsQuery.executeQuery();

            int[] sortedIssueIds = new int[issueIds.length];

            int index = 0;
            while(sortedIssuesResult.next()) {
                sortedIssueIds[index] = sortedIssuesResult.getInt(1);
                index++;
            }
            if(issueIds.length != index)
                throw new RuntimeException("different amount of issues selected by SQL than given in as input, check the issueIds parameter");
            return sortedIssueIds;
        } catch (SQLException e) {
            System.out.println(e);
        }
        throw new RuntimeException("could not sort, read the SQL exception");
    }
//    change this an all others issueId parameters to take in an Issue instance instead
    public void iterateStatus(int issueId) {
        try {
            PreparedStatement selectStatusQuery = connection.prepareStatement("SELECT status_id FROM issues WHERE issue_id = ?");
            selectStatusQuery.setInt(1, issueId);
            ResultSet selectStatusResult = selectStatusQuery.executeQuery();
            int statusId = 0;
            if(selectStatusResult.next())
                statusId = selectStatusResult.getInt(1);

            Status status = switch (statusId) {
                case 1 -> Status.IN_PROGRESS;
                case 2 -> Status.RESOLVED;
                case 3 -> throw new RuntimeException("status is already resolved, cannot change it");
                default -> throw new IllegalArgumentException(statusId + " is invalid");
            };

            PreparedStatement updateIssueQuery = connection.prepareStatement("UPDATE issues SET status_id = ? WHERE issue_id = ?");
            updateIssueQuery.setInt(1, convertEnumToSQLId(status));
            updateIssueQuery.setInt(2, issueId);
            updateIssueQuery.execute();

            SendFeedback.sendFeedbackToCitizen(status, new Citizen("PLACEHOLDER@GMAIL.COM"));
        } catch (SQLException e) {
            System.out.println(e);
        }
    }


    public ArrayList<Issue> fetchAllIssues() {
//        int amountOfIssues = countIssuesInCategory(category);
        ArrayList<Issue> issues = new ArrayList<Issue>();
        try {
            PreparedStatement selectAllQuery = connection.prepareStatement(
                "SELECT issues.issue_id, date, locations.road, locations.housenumber, locations.location_id, categories.category, `description`, statuses.status_id, citizens.email " +
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
            String categoryString = null;
            int statusId = 0;
            String email = null;


            while(selectAllResult.next()) {
                issueId = selectAllResult.getInt("issue_id");
                date = selectAllResult.getDate("date");
                road = selectAllResult.getString("road");
                houseNumber = selectAllResult.getInt("housenumber");
                description = selectAllResult.getString("description");
                categoryString = selectAllResult.getString("category");
                statusId = selectAllResult.getInt("status_id");
                email = selectAllResult.getString("email");

                categoryString = categoryString.toUpperCase();
                Category category = null;



                for(int i = 0; i < Category.values().length; ++i) {
                    if(Category.values()[i] == Category.valueOf(categoryString)) {
                        category=Category.values()[i];
                        break;
                    }
                }

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

    public ArrayList<Issue> fetchIssuesByCategories(ArrayList<Category> selectedCategories) {
        ArrayList<Issue> issues = new ArrayList<>();

        for(Issue issue : this.issues) {
            for(Category category : selectedCategories) {
                if(issue.getCategory() == category)
                    issues.add(issue);
            }
        }
        return issues;
    }


    // Helper method to convert status ID to Status Enum
    private Status getStatusFromId(int statusId) {
        switch (statusId) {
            case 1:
                return Status.PENDING;
            case 2:
                return Status.IN_PROGRESS;
            case 3:
                return Status.RESOLVED;
            default:
                return null;
        }
    }
}