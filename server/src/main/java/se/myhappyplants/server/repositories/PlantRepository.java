package se.myhappyplants.server.repositories;

import se.myhappyplants.shared.Plant;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class PlantRepository extends Repository {

    // TODO: adjust to new implementation
    public List<Plant> getResult(String plantSearch) {
        List<Plant> plantList = new ArrayList<>();
        String query = """
                SELECT * FROM plants WHERE to_tsvector(scientific_name || ' ' || common_name || ' ' || family) @@ to_tsquery(?);
                """;
        try (java.sql.Connection connection = startConnection()) {
            try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
                preparedStatement.setString(1, plantSearch + ":*");
                ResultSet resultSet = preparedStatement.executeQuery();
                while (resultSet.next()) {
                    int plantId = resultSet.getInt("id");
                    String commonName = resultSet.getString("common_name");
                    String scientificName = resultSet.getString("scientific_name");
                    String familyName = resultSet.getString("family");
                    String imageURL = resultSet.getString("image_url");
                    String maintenance = resultSet.getString("maintenance");
                    long wateringFrequency = resultSet.getLong("watering_frequency");
                    boolean poisonousToPets = resultSet.getBoolean("poisonous_to_pets");
                    String light = resultSet.getString("light");
                    plantList.add(new Plant(plantId, scientificName, familyName, commonName, imageURL,
                            light, maintenance, poisonousToPets, wateringFrequency));
                }
            }
        } catch (SQLException sqlException) {
            System.out.println(sqlException.getMessage());
            return null;
        }

        return plantList;
    }

    public Plant getPlantDetails(int plantID) {
        Plant plantDetails = null;
        String query = """
                SELECT scientific_name, family, common_name, image_url, light, maintenance, poisonous_to_pets, watering_frequency FROM plants WHERE id = ?; 
                """;
        try (java.sql.Connection connection = startConnection()) {
            try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
                preparedStatement.setInt(1, plantID);
                ResultSet resultSet = preparedStatement.executeQuery();
                while (resultSet.next()) {
                    String scientificName = resultSet.getString("scientific_name");
                    String family = resultSet.getString("family");
                    String common_name = resultSet.getString("common_name");
                    String image_url = resultSet.getString("image_url");
                    String light = resultSet.getString("light");
                    String maintenance = resultSet.getString("maintenance");
                    boolean poisonous_to_pets = resultSet.getBoolean("poisonous_to_pets");
                    long watering_frequency = resultSet.getLong("watering_frequency");

                    plantDetails = new Plant(plantID, scientificName, family, common_name, image_url, light,
                                             maintenance, poisonous_to_pets, watering_frequency);
                }
            }
        } catch (SQLException sqlException) {
            System.out.println(sqlException.getMessage());
        }
        return plantDetails;
    }

    public static boolean isNumeric(String str) {
        try {
            Double.parseDouble(str);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }
}
