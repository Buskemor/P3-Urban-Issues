package database;

import Iter1.Status;

import java.sql.*;
import java.util.Date;


public class DbConnector {
    public static void main(String[] args) {
        String description = "jeg elsker molopods";
        String email = "runebaton@gmail.com";
        String road = "fortældem";
        int houseNumber = 19;
        Category category = Category.WATER;

        DbConnector vind = new DbConnector(description,email,road,houseNumber,category);

        vind.addIssueToDatabase();

//        vind.iterateStatus(2);
//        vind.iterateStatus(5);
//        vind.iterateStatus(5);

        System.out.println();
        System.out.println(vind.convertEnumToSQLId(Status.IN_PROGRESS));


//        vind.selectIssuesByCategory(Category.OBSTRUCTION);
    }

    private Connection connection;
    private Status status;
    private final Category category;

    private final String description;
    private final String email;
    private int citizenId;

    private final String road;
    private final int houseNumber;
    public int locationId;

    public DbConnector(String description, String email, String road, int houseNumber, Category category) {
        String url = "jdbc:mysql://localhost:3306/issuesdb";
        String username = "root";
        String password = "KENDATABASE123";

        try {
            this.connection = DriverManager.getConnection(url, username, password);
        } catch (SQLException e) {
            System.out.println(e);
        }
        this.description = description;
        this.email = email;
        this.road = road;
        this.houseNumber = houseNumber;
        this.category = category;
    }



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
        int categoryId = convertEnumToSQLId(category);
        int amountOfIssues = getAmountOfIssuesInCategory(category, categoryId);

        try {
            PreparedStatement selectIssueIdsQuery = connection.prepareStatement(
                    "SELECT issue_id FROM issues WHERE category_id = ?");
            selectIssueIdsQuery.setInt(1, categoryId);
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



    private int getAmountOfIssuesInCategory(Category category, int categoryId) {
        try {
            PreparedStatement selectIssueIdCountQuery = connection.prepareStatement(
                    "SELECT COUNT(issue_id) FROM issues WHERE category_id = ?");
            selectIssueIdCountQuery.setInt(1, categoryId);
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



    public void addIssueToDatabase() {
        if(!areCategoriesValid())
            return;

        citizenId = checkIfAttributeExists("citizen");
        locationId = checkIfAttributeExists("location");
        System.out.println("checked for duplicates");

        if(citizenId == 0)
            citizenId = insertUniqueSQLAttribute("citizen");
        if(locationId == 0)
            locationId = insertUniqueSQLAttribute("location");
        System.out.println("added unique");

        insertIssueIntoTable(convertEnumToSQLId(category));
        System.out.println("inserted issues");
    }



    public boolean areCategoriesValid() {
        try {
            PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM categories");
            ResultSet resultSet = preparedStatement.executeQuery();
            int index = 0;
            while (resultSet.next()) {
                resultSet.getInt("category_id");
                if (resultSet.getString("category").toUpperCase().equals(Category.values()[index].name())) {
                    System.out.println("Database category (" + resultSet.getString("category") + ") " +
                                        "equals java enum (" + Category.values()[index].name() + ") ✅");
                } else {
                    System.out.println("Database category (" + resultSet.getString("category") + ") " +
                                        "does NOT equal java enum (" + Category.values()[index].name() + ") ❌");
                    return false;
                }
                index++;
            }
        } catch (SQLException e) {
            System.out.println(e);
        }
        System.out.println("All categories are valid ✅");
        System.out.println();
        return true;
    }



    public int checkIfAttributeExists(String attribute) {
//        String table = attribute + "s";
        try {
            ResultSet resultSet = null;
            PreparedStatement preparedStatement = null;
            if(attribute.equals("citizen")) {
                preparedStatement = connection.prepareStatement(
                "SELECT * FROM citizens WHERE ? = EMAIL");
                preparedStatement.setString(1, email);
                resultSet = preparedStatement.executeQuery();
            }
            if(attribute.equals("location")) {
                preparedStatement = connection.prepareStatement(
                "SELECT * FROM locations WHERE road = ? AND housenumber = ?");
                preparedStatement.setString(1, road);
                preparedStatement.setInt(2, houseNumber);
                resultSet = preparedStatement.executeQuery();
            }

            while(resultSet.next()) {
                System.out.println(attribute +" exists as "+attribute+"_id: " + resultSet.getInt(attribute+"_id"));
                return resultSet.getInt(attribute+"_id");
            }
        } catch (SQLException e) {
            System.out.println(e);
        }
        return 0;
    }



    private int insertUniqueSQLAttribute(String attribute) {
        int id = 0;
        try{
            String table = attribute + "s";
            PreparedStatement selectQuery = connection.prepareStatement(
            "SELECT MAX("+attribute+"_id) FROM "+table);
            ResultSet selectResult = selectQuery.executeQuery();
            while(selectResult.next()) {
                id = selectResult.getInt("MAX("+attribute+"_id)") + 1;
            }
            System.out.println("ran select in unique");

            if(attribute.equals("citizen")) {
                PreparedStatement insertQuery = connection.prepareStatement(
                "INSERT INTO citizens (email) VALUES(?)");
                insertQuery.setString(1, email);
                insertQuery.execute();
            }
            if(attribute.equals("location")) {
                PreparedStatement insertQuery = connection.prepareStatement(
                "INSERT INTO locations (road, housenumber) VALUES(?,?)");
                insertQuery.setString(1, road);
                insertQuery.setInt(2, houseNumber);
                insertQuery.execute();
            }

            return id;
        } catch (SQLException e) {
            System.out.println(e);
        }
        return id;
    }



    public int convertEnumToSQLId(Enum<?> enumeration) {
        return enumeration.ordinal() + 1;
    }



    private void insertIssueIntoTable(int categoryId) {
        try {
//            insert into issues table
            PreparedStatement insertIssueQuery = connection.prepareStatement(
            "INSERT INTO issues (date, description, category_id, status_id, location_id, citizen_id)"
                +"VALUES(?, ?, ?, ?, ?, ?)");
            insertIssueQuery.setDate(1, new java.sql.Date(new Date().getTime())); //date
            insertIssueQuery.setString(2,description); //description
            insertIssueQuery.setInt(3,categoryId); //category
            insertIssueQuery.setInt(4, 1); //status (CONSTANT)
            insertIssueQuery.setInt(5,locationId); //location
            insertIssueQuery.setInt(6, citizenId); //citizenId

            insertIssueQuery.execute();
            System.out.println("Issue inserted into issues table successfully.");

        } catch (SQLException e) {
            System.out.println(e);
        }
    }
}