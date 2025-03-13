package se.myhappyplants.server.addplantsutility;

public class PlantToAdd {
    public int id;
    public String scientific_name;
    public String family;
    public String common_name;
    public String image_url;
    public String light;
    public String maintenance;
    public boolean poisonous_to_pets;
    public long watering_frequency;

    @Override
    public String toString() {
        return String.format("%d, %s, %s, %s, %s, %s, %s, %b, %d",
                id, scientific_name, family, common_name, image_url, light, maintenance, poisonous_to_pets, watering_frequency);
    }
}
