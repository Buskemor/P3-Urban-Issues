package database;

import Iter1.Category;
import Iter1.Citizen;
import Iter1.Issue;
import Iter1.Status;

import java.sql.*;

public class DbManager {
    public static void main(String[] args) {
//        new DbManager().selectIssuesByCategory(Category.ELECTRICAL);
//        new DbManager().convertSqlIdToEnum(10,Status.PENDING);

        DbManager ta = new DbManager();

        System.out.println(ta.getStatusOfIssue(1));

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

//    public void convertSqlIdToEnum(int id, Enum<?> enumeration) {
////        System.out.println(enumeration.getClass());
////        System.out.println(enumeration.name());
//        enumeration.
//    }

    public void iterateStatus(int issueId) {
//        if(this.status.ordinal()+1 > Status.values().length)
//            throw new RuntimeException("Attempted to iterate enum out of bounds");
//        this.status = Status.values()[this.status.ordinal()+1];
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

        } catch (SQLException e) {
            System.out.println(e);
        }
    }



    public int[] selectIssuesByCategory(Category category) {
        int amountOfIssues = getAmountOfIssuesInCategory(category);

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



    private int getAmountOfIssuesInCategory(Category category) {
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



    private String getRoadOfIssue(int issueId) {
        return "";
    }

    private int getHouseNumberOfIssue(int issueId) {
        return 0;
    }

    private String getDescriptionOfIssue(int issueId) {
        return "";
    }

    private Category getCategoryOfIssue(int issueId) {
        return Category.OTHER;
    }

    private Status getStatusOfIssue(int issueId) {
        try {
            PreparedStatement selectStatusQuery = connection.prepareStatement(
                    "SELECT status_id FROM issues WHERE issue_id = ?");
            selectStatusQuery.setInt(1, issueId);
            ResultSet selectStatusResult = selectStatusQuery.executeQuery();

            int statusId = 0;
            while(selectStatusResult.next()) {
                statusId = selectStatusResult.getInt(1);
            }
            switch (statusId) { // scuffed, might fix later
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

    private Citizen getCitizenOfIssue(int issueId) {
        return new Citizen("yes@no", true);
    }
}
