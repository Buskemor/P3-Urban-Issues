package database;

import Iter1.*;
import java.sql.*;
import java.util.Date;


public class DbInserter {
    private Connection connection;
    private Category category;

    private String description;
    private String email;
    private int citizenId;

    private String road;
    private int houseNumber;
    public int locationId;

    public DbInserter(String road, int houseNumber, String description, Category category, Citizen citizen) {
        String url = "jdbc:mysql://localhost:3306/issuesdb";
        String username = "root";
        String password = "KENDATABASE123";

        try {
            this.connection = DriverManager.getConnection(url, username, password);
        } catch (SQLException e) {
            System.out.println(e);
        }
        this.road = road;
        this.houseNumber = houseNumber;
        this.description = description;
        this.category = category;
        this.email = citizen.getEmail(); // maybe a bit strange, but it works
    }
    public DbInserter(String road, int houseNumber, String description, Category category) {
        String url = "jdbc:mysql://localhost:3306/issuesdb";
        String username = "root";
        String password = "KENDATABASE123";

        try {
            this.connection = DriverManager.getConnection(url, username, password);
        } catch (SQLException e) {
            System.out.println(e);
        }
        this.road = road;
        this.houseNumber = houseNumber;
        this.description = description;
        this.category = category;
        this.email = null;
    }




    public void addIssueToDatabase() {
        if(!areCategoriesValid())
            return;

        System.out.println("Checking if attribute id's already exist in the db:");
        citizenId = checkIfAttributeExists("citizen");
        locationId = checkIfAttributeExists("location");

        System.out.println("Adding unique attributes to tables..");
        if(citizenId == 0 && email != null)
            citizenId = insertUniqueSQLAttribute("citizen");
        if(locationId == 0)
            locationId = insertUniqueSQLAttribute("location");

        System.out.println("Inserting into issues table..");
        insertIssueIntoTable(new DbManager().convertEnumToSQLId(category));
    }



    private boolean areCategoriesValid() { //could argue that this should be moved to DbManager
        try {
            PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM categories");
            ResultSet resultSet = preparedStatement.executeQuery();
            int index = 0;
            while (resultSet.next()) {
//                resultSet.getInt("category_id");
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



    private int checkIfAttributeExists(String attribute) {
//        String table = attribute + "s";
        try {
            ResultSet resultSet = null;
            PreparedStatement preparedStatement = null;
            if(attribute.equals("citizen")) {
                preparedStatement = connection.prepareStatement(
                "SELECT * FROM citizens WHERE ? = email");
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
                System.out.println("    "+attribute +" already exists as "+attribute+"_id: " + resultSet.getInt(attribute+"_id"));
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



    private void insertIssueIntoTable(int categoryId) {
        try {
            PreparedStatement insertIssueQuery = connection.prepareStatement(
            "INSERT INTO issues (date, description, category_id, status_id, location_id, citizen_id)"
                +"VALUES(?, ?, ?, ?, ?, ?)");
            insertIssueQuery.setDate(1, new java.sql.Date(new Date().getTime())); //date
            insertIssueQuery.setString(2,description); //description
            insertIssueQuery.setInt(3,categoryId); //category
            insertIssueQuery.setInt(4, 1); //status (CONSTANT)
            insertIssueQuery.setInt(5,locationId); //location
            if (citizenId == 0) {
                insertIssueQuery.setNull(6, Types.NULL); //citizenId
            } else {
                insertIssueQuery.setInt(6, citizenId);
            }

            insertIssueQuery.execute();

            System.out.println("Issue inserted into issues table successfully.");
            System.out.println();
            System.out.println();
        } catch (SQLException e) {
            System.out.println(e);
        }
    }
}