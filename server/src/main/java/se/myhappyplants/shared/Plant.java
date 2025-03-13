package se.myhappyplants.shared;


/**
 * Class defining a plant
 * Created by: Frida Jacobsson
 * Updated by: Linn Borgström, Eric Simonson, Susanne Vikström
 */
// TODO: cleanup class
public class Plant {

    // naming convention consistent with database
    private int id;
    private String common_name;
    private String scientific_name;
    private String family;
    private String image_url;
    private String maintenance;
    private String light;
    // time in millisecond form
    private long watering_frequency;
    private boolean poisonous_to_pets;
    public Plant() {
    };

    public Plant(int id, String scientific_name, String family, String common_name,
                 String image_url, String light, String maintenance, boolean poisonous_to_pets, long watering_frequency) {
        this.id = id;
        this.scientific_name = scientific_name;
        this.family = family;
        this.common_name = common_name;
        this.image_url = image_url;
        this.light = light;
        this.maintenance = maintenance;
        this.poisonous_to_pets = poisonous_to_pets;
        this.watering_frequency = watering_frequency;
    }

    // TODO: fix better Json conversion
    @Override
    public String toString() {
        return String.format("{\nid: %d,\nscientific_name: %s,\nfamily: %s,\ncommon_name: %s,\nimage_url: %s,\nlight: %s,\nmaintenance: %s,\npoisonous_to_pets: %b,\nwatering_frequency: %d\n},\n",
                id, scientific_name, family, common_name, image_url, light, maintenance, poisonous_to_pets, watering_frequency);
    }

    public String getCommon_name() {
        return common_name;
    }

    public String getScientific_name() {
        return scientific_name;
    }

    public int getId() {
        return id;
    }

    public void setImage_url(String image_url) {
        this.image_url = image_url;
    }

    public String getImage_url() {
        if(image_url == null) {
            // TODO: replace with non-random default picture
            image_url = "";
        }
        return image_url.replace("https", "http");
    }

    public boolean getIsPoisonoutToPets() {
        return poisonous_to_pets;
    }

    public String getFamily() {
        return family;
    }

    public String getLight() {
        return light;
    }

    public String getMaintenance() {
        return maintenance;
    }

    public long getWaterFrequency() {
        return watering_frequency;
    }
}