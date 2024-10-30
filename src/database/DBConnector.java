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
        Category category = Category.WATER;
//        int categoryId = 0;

        try {
            Connection connection = DriverManager.getConnection(url, username, password);

//          insert into description table
//            PreparedStatement preparedStatement = connection.prepareStatement(
//            "INSERT INTO description (description) VALUES(?)");
//            preparedStatement.setString(1, description);
//            preparedStatement.execute();

//          insert into locations table
//            preparedStatement = connection.prepareStatement(
//            "INSERT INTO locations (road, housenumber) VALUES(?, ?)");
//            preparedStatement.setString(1, road);
//            preparedStatement.setInt(2, houseNumber);
//            preparedStatement.execute();

//          insert into locations table
            PreparedStatement preparedStatement = connection.prepareStatement(
                    "SELECT * FROM locations WHERE road = ? AND housenumber = ?");
            preparedStatement.setString(1, road);
            preparedStatement.setInt(2, houseNumber);
            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                if(resultSet.getInt("location_id") == 0) {
                    System.out.println(email + " does not match any emails in the citizens table.");
                    preparedStatement = connection.prepareStatement(
                            "INSERT INTO locations (road, housenumber) VALUES(?,?)");
                    preparedStatement.setString(1, road);
                    preparedStatement.setInt(2, houseNumber);
                    preparedStatement.execute();
                }
                locationId = resultSet.getInt("location_id");
            }
            if(locationId == 0) {
                System.out.println("location id: " + locationId + ", is 0");
                // insert into citizens table
                preparedStatement = connection.prepareStatement(
                        "SELECT MAX(location_id) FROM locations");
//                preparedStatement.setString(1, email);
                ResultSet resultSet2 = preparedStatement.executeQuery();

                while(resultSet2.next()) {
                    citizenId = resultSet2.getInt("MAX(location_id)") + 1;
                }
                System.out.println(locationId);

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
                if(resultSet.getInt("citizen_id") == 0) {
                    System.out.println(email + " does not match any emails in the citizens table.");
                    preparedStatement = connection.prepareStatement(
                            "INSERT INTO citizens (email) VALUES(?)");
                    preparedStatement.setString(1, email);
                    preparedStatement.execute();
                }
                citizenId = resultSet.getInt("citizen_id");
            }
            if(citizenId == 0) {
                System.out.println("citizen id: " + citizenId + ", is 0");
                // insert into citizens table
                preparedStatement = connection.prepareStatement(
                        "SELECT MAX(citizen_id) FROM citizens");
//                preparedStatement.setString(1, email);
                ResultSet resultSet2 = preparedStatement.executeQuery();

                while(resultSet2.next()) {
                    citizenId = resultSet2.getInt("MAX(citizen_id)") + 1;
                }
//                System.out.println(citizenId);

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

            System.out.println(citizenId);
            preparedStatement.execute();
            System.out.println("everything executed");

        } catch (SQLException e) {
            System.out.println(e);
        }

    }
}
