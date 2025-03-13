package se.myhappyplants.server.repositories;

import org.mindrot.jbcrypt.BCrypt;
import se.myhappyplants.shared.Plant;
import se.myhappyplants.shared.User;
import se.myhappyplants.shared.UserPlant;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;

/**
 * Class responsible for calling the database about a users library.
 * Created by: Linn Borgström
 * Updated by: Frida Jacobsson 2021-05-21
 */
// TODO: fix this whole thing...
public class UserPlantRepository extends Repository {

    //TODO Update this class to work on the new implementation.

    public boolean savePlant(User user, UserPlant userPlant) {
        boolean success = false;

        String query = """
                INSERT INTO user_plants (user_id, nickname,last_watered, plant_id, image_url) VALUES (?, ?, ?, ?, ?);
                """;
        if (userPlant == null) {
            return success;
        }
        try (java.sql.Connection connection = startConnection()) {
            try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
                preparedStatement.setInt(1, user.getUniqueId());
                preparedStatement.setString(2, userPlant.getNickname());
                preparedStatement.setLong(3, userPlant.getLastWatered());
                preparedStatement.setInt(4, userPlant.getId());
                preparedStatement.setString(5, userPlant.getImage_url());
                preparedStatement.executeUpdate();
                success = true;
            }
        } catch (SQLException sqlException) {
            System.out.println(sqlException.getMessage());
        }
        return success;
    }


    //TODO Update this method to work on the new implementation


    public ArrayList<UserPlant> getUserLibrary(int userId) {
        ArrayList<UserPlant> plantList = new ArrayList<UserPlant>();
        String query = """
                SELECT p.*, up.nickname, up.last_watered FROM plants p JOIN user_plants up ON p.id = up.plant_id WHERE up.user_id = ?
                """;
        try (java.sql.Connection connection = startConnection()) {
            try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
                preparedStatement.setInt(1, userId);
                ResultSet resultSet = preparedStatement.executeQuery();
                while (resultSet.next()) {
                    int plantId = resultSet.getInt("id");
                    String scientificName = resultSet.getString("scientific_name");
                    String family = resultSet.getString("family");
                    String commonName = resultSet.getString("common_name");
                    String imageURL = resultSet.getString("image_url");
                    String light = resultSet.getString("light");
                    String maintenance = resultSet.getString("maintenance");
                    boolean poisonousToPets = resultSet.getBoolean("poisonous_to_pets");
                    long waterFrequency = resultSet.getLong("watering_frequency");
                    String nickname = resultSet.getString("nickname");
                    long lastWatered = resultSet.getLong("last_watered");
                    plantList.add(new UserPlant(plantId, scientificName, family, commonName, imageURL, light, maintenance, poisonousToPets, waterFrequency, nickname, lastWatered));
                }
            }
        } catch (SQLException sqlException) {
            System.out.println(sqlException.getMessage());
        }
        return plantList;
    }

    //TODO Update this method to work on the new implementation

    public UserPlant getUserPlant(int userId, int UniquePlantId) {
        UserPlant userPlant = null;
        String query = """
                        SELECT p.*, up.nickname, up.last_watered FROM plants p JOIN user_plants up ON p.id = up.plant_id WHERE up.id = ? AND up.user_id = ?;
                """;
        try (java.sql.Connection connection = startConnection()) {
            try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
                preparedStatement.setInt(1, UniquePlantId);
                preparedStatement.setInt(2, userId);
                ResultSet resultSet = preparedStatement.executeQuery();
                while (resultSet.next()) {
                    int plantId = resultSet.getInt("id");
                    String scientificName = resultSet.getString("scientific_name");
                    String family = resultSet.getString("family");
                    String commonName = resultSet.getString("common_name");
                    String imageURL = resultSet.getString("image_url");
                    String light = resultSet.getString("light");
                    String maintenance = resultSet.getString("maintenance");
                    boolean poisonousToPets = resultSet.getBoolean("poisonous_to_pets");
                    long waterFrequency = resultSet.getLong("water_frequency");
                    String nickname = resultSet.getString("nickname");
                    long lastWatered = resultSet.getLong("last_watered");
                    userPlant = new UserPlant(plantId, scientificName, family, commonName, imageURL, light, maintenance, poisonousToPets, waterFrequency, nickname, lastWatered);
                }
            }
        } catch (SQLException sqlException) {
            System.out.println(sqlException.getMessage());
        }
        return userPlant;
    }

    //TODO Update this method to work on the new implementation


    public boolean deletePlant(int userId, int plantId) {
        boolean plantDeleted = false;

        String query = """
                        DELETE FROM user_plants WHERE plant_id = ? AND user_id = ?;
                """;
        if (plantId <= 0) {
            return plantDeleted;
        }
        try (java.sql.Connection connection = startConnection()) {
            try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
                preparedStatement.setInt(1, plantId);
                preparedStatement.setInt(2, userId);
                preparedStatement.executeUpdate();
                plantDeleted = true;
            }
        } catch (SQLException sqlException) {
            System.out.println(sqlException.getMessage());
        }
        return plantDeleted;
    }

    //TODO Update this method to work on the new implementation



    public boolean changeLastWatered(int userId, int plant_id) {
        boolean dateChanged = false;
        String query = """ 
                    UPDATE user_plants SET last_watered = ? WHERE plant_id = ? AND user_id = ?;
                    """;
        if (userId <= 0 || plant_id <= 0) {
            return dateChanged;
        }
        try (java.sql.Connection connection = startConnection()) {
            try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
                preparedStatement.setLong(1, System.currentTimeMillis());
                preparedStatement.setInt(2, plant_id);
                preparedStatement.setInt(3, userId);
                preparedStatement.executeUpdate();
                dateChanged = true;
            }
        } catch (SQLException sqlException) {
            System.out.println(sqlException.getMessage());
        }
        return dateChanged;
    }

    //TODO Update this method to work on the new implementation

    public boolean changeNickname(int userId, int plantId, String newNickname) {
        boolean nicknameChanged = false;
        String query = """
                    UPDATE user_plants 
                    SET nickname = ? 
                    WHERE plant_id = ? AND user_id = ?;
                   """;
        if (newNickname == null || newNickname.isEmpty()) {
            return nicknameChanged;
        }
        if (userId <= 0 || plantId <= 0) {
            return nicknameChanged;
        }
        try (Connection connection = startConnection()) {
            try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
                preparedStatement.setString(1, newNickname);
                preparedStatement.setInt(2, plantId);
                preparedStatement.setInt(3, userId);
                preparedStatement.executeUpdate();
                nicknameChanged = true;
            }
        } catch (SQLException sqlException) {
            System.out.println(sqlException.getMessage());
        }
        return nicknameChanged;
    }

        // TODO: adjust to new implementation


    public boolean changeAllToWatered(int userId) {
        boolean dateChanged = false;
        String query = """
                    UPDATE user_plants 
                    SET last_watered = ? 
                    WHERE user_id = ?;
                   """;
        if (userId <= 0) {
            return dateChanged;
        }
        try (Connection connection = startConnection()) {
            try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
                preparedStatement.setLong(1, System.currentTimeMillis());
                preparedStatement.setInt(2, userId);
                dateChanged = true;
            }
        } catch (SQLException sqlException) {
            System.out.println(sqlException.getMessage());
        }
        return dateChanged;
    }


    //TODO The remaining methods under here will probably get deleted but let the code stay at the moment.

    /*

        public long getWaterFrequency(int plantId) {
        long waterFrequency = -1;
        String query = """
        SELECT water_frequency FROM species WHERE id = ?;
        """;
        try (PreparedStatement preparedStatement = connection.getConnection().prepareStatement(query)) {
            preparedStatement.setInt(1, plantId);
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                long waterLong = resultSet.getLong("water_frequency");
                int water = (int) waterLong;
                waterFrequency = WaterCalculator.calculateWaterFrequencyForWatering(water);
            }
        }
        catch (SQLException sqlException) {
            System.out.println(sqlException.getMessage());
        } finally {
            connection.closeConnection();
        }
        return waterFrequency;

    }
     */

}