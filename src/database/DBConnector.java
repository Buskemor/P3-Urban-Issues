package database;

import java.sql.*;
import java.util.Date;


public class DBConnector {
    public static void main(String[] args) {
        String url = "jdbc:mysql://localhost:3306/issuesdb";
        String username = "root";
        String password = "KENDATABASE123";

        String description = "jeg elsker molopods";
        String email = "runebaton@gmail.com";
        int citizenId = 0;

        String road = "myggegade";
        int houseNumber = 19;
        int locationId = 0;

        java.sql.Date date = new java.sql.Date(new Date().getTime());
        Category category = Category.WATER; // doesn't do anything yet

        try {
            Connection connection = DriverManager.getConnection(url, username, password);

//          insert into locations table
            PreparedStatement preparedStatement = connection.prepareStatement(
            "SELECT * FROM locations WHERE road = ? AND housenumber = ?");
            preparedStatement.setString(1, road);
            preparedStatement.setInt(2, houseNumber);
            ResultSet resultSet = preparedStatement.executeQuery();


            while(resultSet.next()) {
                System.out.println("location exists as location_id: " + resultSet.getInt("location_id"));
                locationId = resultSet.getInt("location_id");
            }

//            if no location id matches, run this
            if(locationId == 0) {
                System.out.println("location id: " + locationId + ", is 0");
                preparedStatement = connection.prepareStatement(
                "SELECT MAX(location_id) FROM locations");
                resultSet = preparedStatement.executeQuery();

                while(resultSet.next()) {
                    locationId = resultSet.getInt("MAX(location_id)") + 1;
                }
                preparedStatement = connection.prepareStatement(
                "INSERT INTO locations (road, housenumber) VALUES(?,?)");
                preparedStatement.setString(1, road);
                preparedStatement.setInt(2, houseNumber);
                preparedStatement.execute();
            }


//          insert into citizens table
            preparedStatement = connection.prepareStatement(
            "SELECT * FROM citizens WHERE ? = EMAIL");
            preparedStatement.setString(1, email);
            resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                System.out.println("location exists as citizen_id: " + resultSet.getInt("citizen_id"));
                citizenId = resultSet.getInt("citizen_id");
            }

//            if no citizen id matches, run this
            if(citizenId == 0) {
                System.out.println("citizen id: " + citizenId + ", is 0");
                preparedStatement = connection.prepareStatement(
                "SELECT MAX(citizen_id) FROM citizens");
                resultSet = preparedStatement.executeQuery();
                while(resultSet.next()) {
                    citizenId = resultSet.getInt("MAX(citizen_id)") + 1;
                }
                preparedStatement = connection.prepareStatement(
                "INSERT INTO citizens (email) VALUES(?)");
                preparedStatement.setString(1, email);
                preparedStatement.execute();
            }


//          insert into issues table
            preparedStatement = connection.prepareStatement(
            "INSERT INTO issues (date, description, category_id, status_id, location_id, citizen_id)"
               +"VALUES(?, ?, ?, ?, ?, ?)");
            preparedStatement.setDate(1, date); //date
            preparedStatement.setString(2,description); //description
            preparedStatement.setInt(3,5); //category
            preparedStatement.setInt(4, 1); //status (CONSTANT)
            preparedStatement.setInt(5,locationId); //location
            preparedStatement.setInt(6, citizenId); //citizenId

            preparedStatement.execute();
            System.out.println("Issue inserted into issues table successfully.");

        } catch (SQLException e) {
            System.out.println(e);
        }
    }
}
