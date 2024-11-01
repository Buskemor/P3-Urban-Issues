package database;

import java.sql.*;
import java.util.Date;


public class DBConnector {
    public static void main(String[] args) {
        String description = "jeg elsker molopods";
        String email = "runebaton@gmail.com";
        String road = "myggegade";
        int houseNumber = 19;
        Category category = Category.WATER;

        DBConnector vind = new DBConnector(description,email,road,houseNumber,category);
        vind.addIssueToDatabase();
    }

    private String url = "jdbc:mysql://localhost:3306/issuesdb";
    private String username = "root";
    private String password = "KENDATABASE123";
    private Connection connection;

    private String description;
    private String email;
    private int citizenId = 0;

    private String road;
    private int houseNumber;
    private int locationId = 0;

    private java.sql.Date date = new java.sql.Date(new Date().getTime());
    private Category category;
    private int categoryId = 0;



    public DBConnector(String description, String email, String road, int houseNumber, Category category) {
        try {
            this.connection = DriverManager.getConnection(this.url, this.username, this.password);
        } catch (SQLException e) {
            System.out.println(e);
        }
        this.description = description;
        this.email = email;
        this.road = road;
        this.houseNumber = houseNumber;
        this.category = category;
    }

    public void addIssueToDatabase() {
        if(!areCategoriesValid())
            return;

        citizenId = checkForSQLAttributeDuplicates("citizen");
        locationId = checkForSQLAttributeDuplicates("location");
        System.out.println("checked for duplicates");

        if(citizenId == 0)
            citizenId = insertUniqueSQLAttribute("citizen");
        if(locationId == 0)
            locationId = insertUniqueSQLAttribute("location");
        System.out.println("added unique");

        categoryId = convertCategoryEnumToSQLId(category);

        insertIssueIntoTable();
        System.out.println("inserted issues");
    }


    public boolean areCategoriesValid() {
        try{
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



    public int checkForSQLAttributeDuplicates(String attribute) {
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



    public int convertCategoryEnumToSQLId(Category category) {
        int index = 1;
        for(Category categoryElem : Category.values()) {
            if(categoryElem.equals(category)) {
                return index;
            }
            index++;
        }
        return 0;
    }



    private void insertIssueIntoTable() {
        try {
//            insert into issues table
            PreparedStatement insertIssueQuery = connection.prepareStatement(
            "INSERT INTO issues (date, description, category_id, status_id, location_id, citizen_id)"
                +"VALUES(?, ?, ?, ?, ?, ?)");
            insertIssueQuery.setDate(1, date); //date
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