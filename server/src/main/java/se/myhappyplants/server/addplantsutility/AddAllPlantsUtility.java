package se.myhappyplants.server.addplantsutility;

import com.google.gson.Gson;
import com.google.gson.stream.JsonReader;
import se.myhappyplants.server.PasswordsAndKeys;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

// uses a new object, PlantToAdd, that accurately matches the parameters of
// the Json objects -- unlike Plant
public class AddAllPlantsUtility {
    PlantToAdd[] plantsToAdd;

    AddAllPlantsUtility() {
        Gson gson = new Gson();
        JsonReader jsonReader;
        try {
            jsonReader = new JsonReader(new FileReader("src/main/java/se/myhappyplants/server/addplantsutility/plants.json"));
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
        plantsToAdd = gson.fromJson(jsonReader, PlantToAdd[].class);
    }

    public void testFirst10() {
        for (int i = 0; i < 10; i++) {
            System.out.println(plantsToAdd[i].toString());
        }
    }

    public void replaceNull() {
        for (PlantToAdd plantToAdd : plantsToAdd) {
            if (plantToAdd != null) {
                if (plantToAdd.scientific_name == null) {
                    plantToAdd.scientific_name = "Unknown";
                } else {
                    // fix for entries having mixed use of uppercase and lowercase
                    char[] chars = plantToAdd.light.toCharArray();
                    chars[0] = Character.toUpperCase(chars[0]);
                    plantToAdd.light = new String(chars);
                }
                if (plantToAdd.family == null) {
                    plantToAdd.family = "Unknown";
                } else {
                    // fix for entries having mixed use of uppercase and lowercase
                    char[] chars = plantToAdd.light.toCharArray();
                    chars[0] = Character.toUpperCase(chars[0]);
                    plantToAdd.light = new String(chars);
                }
                if (plantToAdd.common_name == null) {
                    plantToAdd.common_name = "Unknown";
                } else {
                    // fix for entries having mixed use of uppercase and lowercase
                    char[] chars = plantToAdd.light.toCharArray();
                    chars[0] = Character.toUpperCase(chars[0]);
                    plantToAdd.light = new String(chars);
                }
                if (plantToAdd.light == null) {
                    plantToAdd.light = "Unknown";
                } else {
                    // fix for entries having mixed use of uppercase and lowercase
                    char[] chars = plantToAdd.light.toCharArray();
                    chars[0] = Character.toUpperCase(chars[0]);
                    plantToAdd.light = new String(chars);
                }
                if (plantToAdd.maintenance == null) {
                    plantToAdd.maintenance = "Unknown";
                } else {
                    // fix for entries having mixed use of uppercase and lowercase
                    char[] chars = plantToAdd.maintenance.toCharArray();
                    chars[0] = Character.toUpperCase(chars[0]);
                    plantToAdd.maintenance = new String(chars);
                }
            }
        }
    }

    public void addAllPlants() {
        for (PlantToAdd plant : plantsToAdd) {
            java.sql.Connection connection;
            try {
                connection = DriverManager.getConnection(PasswordsAndKeys.dbServerAddress, PasswordsAndKeys.dbUsername, PasswordsAndKeys.dbPassword);
            } catch (SQLException sqlException) {
                System.out.println(sqlException.getMessage());
                return;
            }
            String query = """
                    INSERT INTO plants (id, common_name, family, scientific_name, image_url, light, maintenance, poisonous_to_pets, watering_frequency) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?);
                    """;
            try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
                preparedStatement.setInt(1, plant.id);
                preparedStatement.setString(2, plant.common_name);
                preparedStatement.setString(3, plant.family);
                preparedStatement.setString(4, plant.scientific_name);
                preparedStatement.setString(5, plant.image_url);
                preparedStatement.setString(6, plant.light);
                preparedStatement.setString(7, plant.maintenance);
                preparedStatement.setBoolean(8, plant.poisonous_to_pets);
                preparedStatement.setLong(9, plant.watering_frequency);
                preparedStatement.executeUpdate();
            } catch (SQLException sqlException) {
                System.out.println(sqlException.getMessage());
            } finally {
                try {
                    connection.close();
                } catch (SQLException sqlException) {
                    System.out.println(sqlException.getMessage());
                }
            }
        }
    }

    public static void main(String[] args) {
        try {
            DriverManager.registerDriver(new org.postgresql.Driver());
        } catch (SQLException sqlException) {
            System.out.println(sqlException.getMessage());
        }
        AddAllPlantsUtility test = new AddAllPlantsUtility();
        test.replaceNull();
        test.testFirst10();
        test.addAllPlants();
    }
}
