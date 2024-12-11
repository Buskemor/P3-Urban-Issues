package Database;

import IssueStructure.*;
import javafx.util.Pair;

import java.sql.*;
import java.util.Date;


public class DbInserter extends DbManager {
    private Pair<Integer, String> category;

    private String description;
    private String email;
    private int citizenId;

    private String road;
    private int houseNumber;
    public int locationId;

    public DbInserter(String road, int houseNumber, String description, Pair<Integer, String> category,
                      String username, String password, String ip, String port, String schema,
                      Citizen citizen) {
        super(username, password, ip, port, schema);

        this.road = road;
        this.houseNumber = houseNumber;
        this.description = description;
        this.category = category;
        if(citizen != null)
            this.email = citizen.getEmail();
    }

    public DbInserter(String road, int houseNumber, String description, Pair<Integer, String> category,
                      String username, String password, String ip, String port, String schema) {
        this(road, houseNumber, description, category, username, password, ip, port, schema, null);
    }



    public void addIssueToDatabase() {
        System.out.println("Checking if attribute id's already exist in the db:");
        citizenId = checkIfAttributeExists("citizen");
        locationId = checkIfAttributeExists("location");

        System.out.println("Adding unique attributes to tables..");
        if(citizenId == 0 && email != null)
            citizenId = insertUniqueSQLAttribute("citizen");
        if(locationId == 0)
            locationId = insertUniqueSQLAttribute("location");

        System.out.println("Inserting into issues table..");
        insertIssueIntoTable(category.getKey());
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