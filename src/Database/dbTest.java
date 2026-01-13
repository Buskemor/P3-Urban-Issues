package Database;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class dbTest extends DbInserter {
    private String description;
    private String email;
    private int citizenId;

    private String road;
    private int houseNumber;
    public int locationId;

    public dbTest() {
        super(road, houseNumber, description, category, username, password, ip, port, schema, null);
    }

    /**
     * Fetches the id of the row that contains the email, road / houseNumber
     * @param attributeSQL
     * @param tableName
     * @return An int, the id of the row that coReturns -1 if there is no existing id of the corresponding attribute.
     */
//    private int fetchExistingIdOfAttribute(String attributeSQL, int secondAttributeSQL) {
//        try {
//            PreparedStatement preparedStatement = connection.prepareStatement(
//            "SELECT * FROM locations WHERE road = ? AND housenumber = ?");
//            preparedStatement.setString(1, road);
//            preparedStatement.setInt(2, houseNumber);
//            ResultSet resultSet = preparedStatement.executeQuery();
//
//            while(resultSet.next())
//                return resultSet.getInt(attributeSQL);
//
//        } catch (SQLException e) {
//            System.out.println(e);
//        }
//        return -1;
//    }


    private int fetchExistingId(String primaryKeyColumn) {
        try {
            ResultSet resultSet = null;
            PreparedStatement preparedStatement = null;
            if(primaryKeyColumn.equals("citizen_id")) {
                preparedStatement = connection.prepareStatement(
                        "SELECT * FROM citizens WHERE ? = email");
                preparedStatement.setString(1, email);
                resultSet = preparedStatement.executeQuery();
            }
            if(primaryKeyColumn.equals("location_id")) {
                preparedStatement = connection.prepareStatement(
                "SELECT * FROM locations WHERE road = ? AND housenumber = ?");
                preparedStatement.setString(1, road);
                preparedStatement.setInt(2, houseNumber);
                resultSet = preparedStatement.executeQuery();
            }

            while(resultSet.next())
                return resultSet.getInt(primaryKeyColumn);

        } catch (SQLException e) {
            System.out.println(e);
        }
        return 0;
    }

//    private int fetchExistingIdOfAttribute(String attributeSQL) {
//        try {
//            PreparedStatement preparedStatement = connection.prepareStatement(
//            "SELECT * FROM citizens WHERE ? = email");
//            preparedStatement.setString(1, email);
//            ResultSet resultSet = preparedStatement.executeQuery();
//
//            while(resultSet.next())
//                return resultSet.getInt(attributeSQL);
//
//        } catch (SQLException e) {
//            System.out.println(e);
//        }
//        return -1;
//    }

}
