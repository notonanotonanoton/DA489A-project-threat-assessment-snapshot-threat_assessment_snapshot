package shared;

import org.junit.jupiter.api.Test;
import se.myhappyplants.shared.UserPlant;
import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDate;


/**
 * Test class for the UserPlant class.
 * @see se.myhappyplants.shared.UserPlant
 * @author Douglas Almö Thorsell
 */

public class UserPlantTest {

    private final long currentTime = 1738756800000L;

    private UserPlant testPlant = new UserPlant(
            527,
            "Actaea racemosa",
            "Ranunculaceae",
            "black cohosh",
            "https://perenual.com/storage/species_image/527_actaea_racemosa/og/28204469216_e9680ed0a4_b.jpg",
            "full sun",
            "Low",
            false,
            734400000,
            "testPlant",
            1738411200000L
    );

    @Test
    void getWaterProgressCorrectInput(){
        long expectedValue = 345600000;

        long result = testPlant.getProgress(currentTime);

        assertEquals(expectedValue, result, "Wrong calculation. The expected return value is: " + expectedValue);
    }

    @Test
    void getWaterProgressIncorrectInput(){
        long expectedValue = -1;
        long illegalTime = 1000;

        long result = testPlant.getProgress(illegalTime);

        assertEquals(expectedValue, result, "The returned value should be -1. Because an illegal time was inputted as currentTime.");
    }

    @Test
    void getProgressFormattedCorrectInputDayOnly(){
        String expectedValue = "4d";

        String result = testPlant.getProgressFormatted(currentTime);

        assertEquals(expectedValue, result, "The format is ether incorrect or the calculation is incorrect. Correct format and calculation is '4d'");
    }

    @Test
    void getProgressFormattedCorrectInputDayAndHours(){
        String expectedValue = "4d 6h";
        long testTime = 1738778400000L;

        String result = testPlant.getProgressFormatted(testTime);

        assertEquals(expectedValue, result, "The format is ether incorrect or the calculation is incorrect. Correct format and calculation is '4d 6h'");
    }
}
