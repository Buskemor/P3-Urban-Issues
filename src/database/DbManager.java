package database;

import Iter1.*;

import java.sql.*;

public class DbManager {
    public static void main(String[] args) {
        DbManager ta = new DbManager();
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

    public void iterateStatus(int issueId) {
        try {
            PreparedStatement selectStatusQuery = connection.prepareStatement("SELECT status_id FROM issues WHERE issue_id = ?");
            selectStatusQuery.setInt(1, issueId);
            ResultSet selectStatusResult = selectStatusQuery.executeQuery();

            int statusId = 0;
            while(selectStatusResult.next()) {
                statusId = selectStatusResult.getInt(1);
            }
            Status status = null;

            switch (statusId) {
                case 0:
                    throw new RuntimeException("status id is 0");
                case 1:
                    status = Status.IN_PROGRESS;
                    break;
                case 2:
                    status = Status.RESOLVED;
                    break;
                case 3:
                    throw new RuntimeException("status is already resolved, cannot change it");
            }

            PreparedStatement updateIssueQuery = connection.prepareStatement("UPDATE issues SET status_id = ? WHERE issue_id = ?");
            updateIssueQuery.setInt(1, convertEnumToSQLId(status));
            updateIssueQuery.setInt(2, issueId);
            updateIssueQuery.execute();

            SendFeedback.sendFeedbackToCitizen(status, new Citizen("PLACEHOLDER@GMAIL.COM",true));
        } catch (SQLException e) {
            System.out.println(e);
        }
    }


    public int[] selectIssuesByCategory(Category category) {
        int amountOfIssues = countIssuesInCategory(category);

        try {
            PreparedStatement selectIssueIdsQuery = connection.prepareStatement(
                    "SELECT issue_id FROM issues WHERE category_id = ?");
            selectIssueIdsQuery.setInt(1, convertEnumToSQLId(category));
            ResultSet issueIdsResult = selectIssueIdsQuery.executeQuery();

            int[] issueIds = new int[amountOfIssues];
            int index = 0;
            System.out.print("issues ids of "+category+": ");
            while(issueIdsResult.next()) {
                issueIds[index] = issueIdsResult.getInt("issue_id");
                System.out.print(issueIds[index]+", ");
                index++;
            }
            System.out.println();
            return issueIds;
        } catch (SQLException e) {
            System.out.println(e);
        }
        return new int[0];
    }



    private int countIssuesInCategory(Category category) {
        try {
            PreparedStatement selectIssueIdCountQuery = connection.prepareStatement(
                    "SELECT COUNT(issue_id) FROM issues WHERE category_id = ?");
            selectIssueIdCountQuery.setInt(1, convertEnumToSQLId(category));
            ResultSet issueIdCountResult = selectIssueIdCountQuery.executeQuery();

            int amountOfIssues = 0;
            while(issueIdCountResult.next()) {
                amountOfIssues = issueIdCountResult.getInt(1);
            }
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

            while(selectRoadResult.next()) {
                return selectRoadResult.getString(1);
            }
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

            while(selectHouseNrResult.next()) {
                return selectHouseNrResult.getInt(1);
            }
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

            while(selectStatusResult.next()) {
                return selectStatusResult.getString(1);
            }
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
            while(selectStatusResult.next()) {
                statusId = selectStatusResult.getInt(1);
            }
            switch (statusId) {
                case 1:
                    return Status.PENDING;
                case 2:
                    return Status.IN_PROGRESS;
                case 3:
                    return Status.RESOLVED;
            }
        } catch (SQLException e) {
            System.out.println(e);
        }
        throw new RuntimeException("status couldn't be determined");
    }

    public String fetchIssueEmail(int issueId) {
        try {
            PreparedStatement selectEmailQuery = connection.prepareStatement(
                    "SELECT email FROM issues " +
                        "INNER JOIN citizens ON citizens.citizen_id = issues.citizen_id " +
                        "WHERE issue_id = ?");
            selectEmailQuery.setInt(1, issueId);
            ResultSet selectEmailResult = selectEmailQuery.executeQuery();

            while(selectEmailResult.next()) {
                return selectEmailResult.getString(1);
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
            while(selectEmailResult.next()) {
                return true;
            }
            return false;
        } catch(SQLException e) {
            System.out.println(e);
        }
        throw new RuntimeException("failed to check if issue has an email");
    }
}