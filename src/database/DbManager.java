package database;

import Iter1.*;

import java.sql.*;
import java.util.List;
import java.util.ArrayList;

public class DbManager {
    public static void main(String[] args) {
        DbManager ta = new DbManager();

//        DbInserter = new DbInserter(new Issue())

//        ArrayList<Issue> gg = ta.fetchAllIssues();

//        for (Issue element : gg) {
//            System.out.println("Date: " + element.getDate());
//            System.out.println("Status: " + element.getStatus());
//            System.out.println("Road: " + element.getRoad());
//            System.out.println("House Number: " + element.getHouseNumber());
//            System.out.println("Description: " + element.getDescription());
//            System.out.println("Category: " + element.getCategory());
//            System.out.println("Reported By: " + element.getReportedBy());
//            System.out.println("Issue ID: " + element.getIssueId());
//            System.out.println(); // Blank line for readability between elements
//        }

        ArrayList<Category> cats = new ArrayList<Category>();
        cats.add(Category.ELECTRICAL);
        cats.add(Category.OBSTRUCTION);

        ArrayList<Issue> issuesOfCat = ta.fetchIssuesByCategories(cats);

        for(Issue element : issuesOfCat) {
            System.out.println(element.getCategory());
        }


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


//    public int[] selectIssuesInCategory(Category category) {
//        int amountOfIssues = countIssuesInCategory(category);
//
//        try {
//            PreparedStatement selectIssueIdsQuery = connection.prepareStatement(
//                    "SELECT issue_id FROM issues WHERE category_id = ?");
//            selectIssueIdsQuery.setInt(1, convertEnumToSQLId(category));
//            ResultSet issueIdsResult = selectIssueIdsQuery.executeQuery();
//
//            int[] issueIds = new int[amountOfIssues];
//            int index = 0;
//            System.out.print("issues ids of "+category+": ");
//            while(issueIdsResult.next()) {
//                issueIds[index] = issueIdsResult.getInt("issue_id");
//                System.out.print(issueIds[index]+", ");
//                index++;
//            }
//            System.out.println();
//            return issueIds;
//        } catch (SQLException e) {
//            System.out.println(e);
//        }
//        return new int[0];
//    }



    private int countIssuesInCategory(Category category) {
        try {
            PreparedStatement selectIssueIdCountQuery = connection.prepareStatement(
                    "SELECT COUNT(issue_id) FROM issues WHERE category_id = ?");
            selectIssueIdCountQuery.setInt(1, convertEnumToSQLId(category));
            ResultSet issueIdCountResult = selectIssueIdCountQuery.executeQuery();

            int amountOfIssues = 0;
            if(issueIdCountResult.next())
                amountOfIssues = issueIdCountResult.getInt(1);

            System.out.println("There are " + amountOfIssues+" issues of category: " + category +",");
            return amountOfIssues;
        } catch (SQLException e) {
            System.out.println(e);
        }
        return 0;
    }



    public String fetchIssueRoad(int issueId) {
        try {
            PreparedStatement selectRoadQuery = connection.prepareStatement(
                    "SELECT road FROM issues " +
                        "INNER JOIN locations ON locations.location_id = issues.location_id " +
                        "WHERE issue_id = ?");
            selectRoadQuery.setInt(1, issueId);
            ResultSet selectRoadResult = selectRoadQuery.executeQuery();
            if(selectRoadResult.next())
                return selectRoadResult.getString(1);

        } catch (SQLException e) {
            System.out.println(e);
        }
        throw new RuntimeException("road couldn't be determined");
    }

    public int fetchIssueHouseNumber(int issueId) {
        try {
            PreparedStatement selectHouseNrQuery = connection.prepareStatement(
                    "SELECT housenumber FROM issues " +
                        "INNER JOIN locations ON locations.location_id = issues.location_id " +
                        "WHERE issue_id = ?");
            selectHouseNrQuery.setInt(1, issueId);
            ResultSet selectHouseNrResult = selectHouseNrQuery.executeQuery();
            if(selectHouseNrResult.next())
                return selectHouseNrResult.getInt(1);

        } catch (SQLException e) {
            System.out.println(e);
        }
        throw new RuntimeException("housenumber couldn't be determined");
    }

    public String fetchIssueDescription(int issueId) {
        try {
            PreparedStatement selectDescriptionQuery = connection.prepareStatement(
                    "SELECT description FROM issues WHERE issue_id = ?");
            selectDescriptionQuery.setInt(1, issueId);
            ResultSet selectStatusResult = selectDescriptionQuery.executeQuery();

            if(selectStatusResult.next())
                return selectStatusResult.getString(1);

        } catch (SQLException e) {
            System.out.println(e);
        }
        throw new RuntimeException("description couldn't be determined");
    }

    public Status fetchIssueStatus(int issueId) {
        try {
            PreparedStatement selectStatusQuery = connection.prepareStatement(
                    "SELECT status_id FROM issues WHERE issue_id = ?");
            selectStatusQuery.setInt(1, issueId);
            ResultSet selectStatusResult = selectStatusQuery.executeQuery();

            int statusId = 0;
            if(selectStatusResult.next())
                statusId = selectStatusResult.getInt(1);

            switch (statusId) {
                case 1:
                    return Status.PENDING;
                case 2:
                    return Status.IN_PROGRESS;
                case 3:
                    return Status.RESOLVED;
            }
            System.out.println(statusId);
        } catch (SQLException e) {
            System.out.println(e);
        }
        throw new RuntimeException("status couldn't be determined");
    }

    public String fetchIssueEmail(int issueId) {
        try {
            PreparedStatement selectEmailQuery = connection.prepareStatement(
                    "SELECT citizens.email " +
                            "FROM issues " +
                            "INNER JOIN citizens ON issues.citizen_id = citizens.citizen_id " +
                            "WHERE issues.issue_id = ?");
            selectEmailQuery.setInt(1, issueId);
            ResultSet selectEmailResult = selectEmailQuery.executeQuery();

            if(selectEmailResult.next()) {
                return selectEmailResult.getString("email");
            }
        } catch (SQLException e) {
            System.out.println(e);
        }
        throw new RuntimeException("issue doesn't have an email");
    }

    public boolean doesIssueHaveEmail(int issueId) {
        try {
            PreparedStatement selectEmailQuery = connection.prepareStatement(
                    "SELECT email FROM issues " +
                        "INNER JOIN citizens ON citizens.citizen_id = issues.citizen_id " +
                        "WHERE issue_id = ?");
            selectEmailQuery.setInt(1, issueId);
            ResultSet selectEmailResult = selectEmailQuery.executeQuery();
            return selectEmailResult.next();
        } catch(SQLException e) {
            System.out.println(e);
        }
        throw new RuntimeException("failed to check if issue has an email");
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

                Status status = null;

                switch (statusId) {
                    case 1:
                        status = Status.PENDING;
                    case 2:
                        status = Status.IN_PROGRESS;
                    case 3:
                        status = Status.RESOLVED;
                }

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