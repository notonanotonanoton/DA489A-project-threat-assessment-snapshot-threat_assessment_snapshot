package se.myhappyplants.shared;

import java.sql.Date;
import java.time.LocalDate;

public class UserPlant extends Plant {
    private String nickname;
    private long last_watered;

    public UserPlant(int id, String scientific_name, String family, String common_name, String image_url, String light,
                     String maintenance, boolean poisonous_to_pets, long water_frequency, String nickname, long last_watered) {
        super(id, common_name, scientific_name, family, image_url, maintenance, light, poisonous_to_pets, water_frequency);
        this.nickname = nickname;
        this.last_watered = last_watered;
    }

    public long getProgress(long currentTime) {
        if(currentTime < last_watered || currentTime < 1735732800000.0){
            return -1;
        }
        return currentTime - last_watered;
    }

    public String getProgressFormatted(long currentTime) {
        long progress = getProgress(currentTime);
        long hours = ((progress/1000)/60)/60;
        int roundedHours = (int) Math.ceil(hours);

        if(roundedHours < 24){
            return String.format("%dh", roundedHours);
        } else if (roundedHours == 24) {
            return String.format("%dd", 1);
        }else {
            int totalDays = roundedHours/24;
            int totalHours = roundedHours - (totalDays*24);

            if(totalHours == 0){
                return String.format("%dd", totalDays);
            }

            return String.format("%dd %hh", totalDays, totalHours);
        }
    }



    public long getLastWatered() {
        return last_watered;
    }

    /**
     * This method is only used for testing purposes.
     */
    public void setLastWatered(long last_watered) {
        this.last_watered = last_watered;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }
}
