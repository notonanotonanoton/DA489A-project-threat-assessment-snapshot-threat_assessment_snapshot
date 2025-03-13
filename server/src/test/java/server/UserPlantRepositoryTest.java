package server;

import org.junit.jupiter.api.*;
import se.myhappyplants.server.repositories.UserPlantRepository;
import se.myhappyplants.server.repositories.UserRepository;
import se.myhappyplants.shared.Plant;
import se.myhappyplants.shared.User;
import se.myhappyplants.shared.UserPlant;

import java.sql.Date;
import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class UserPlantRepositoryTest {

    private UserPlantRepository userPlantRepository;
    private static UserRepository userRepository = new UserRepository();
    private UserPlant testUserPlantWorking;
    private UserPlant testUserPlantWorking2;
    private final User testUser = new User(
            "test@testmail.com",
            "test123",
            true,
            true
    );
    @BeforeEach
    void setUp() {
        userPlantRepository = new UserPlantRepository();

        userRepository.deleteAccount("test@testmail.com","test123");
        userRepository.saveUser(testUser);
        testUser.setUniqueId(userRepository.getUserDetails(testUser.getEmail()).getUniqueId());

        testUserPlantWorking = new UserPlant(2862, "Euphorbia amygdaloides subsp. robbiae", "spurge", "Euphorbiaceae",
                "http://perenual.com/storage/species_image/2862_euphorbia_amygdaloides_subsp_robbiae/og/2048px-Bloeiende_Euphorbia_amygdaloides_var._Robbiae._31-03-2021._28d.j.b29.jpg", "Full sun", "Low",
                false, 734400000,"peeedeereeeeeeeeeeeeeeeeeeeeeeeeeee",734400000);

        testUserPlantWorking2 = new UserPlant(2862, "Euphorbia amygdaloides subsp. robbiae", "spurge", "Euphorbiaceae",
                "http://perenual.com/storage/species_image/2862_euphorbia_amygdaloides_subsp_robbiae/og/2048px-Bloeiende_Euphorbia_amygdaloides_var._Robbiae._31-03-2021._28d.j.b29.jpg", "Full sun", "Low",
                false, 734400000,"peeedeer",734400000);
    }

    @AfterAll
    static void tearDown() {
        userRepository.deleteAccount("test@testmail.com","test123");
    }

    @Test
    @Order(1)
    void testSavePlantSuccess() {
        boolean result = userPlantRepository.savePlant(testUser, testUserPlantWorking);
        assertEquals(true,result,
                "The savePlant method failed and didn't save the plant: testSavePlantSuccess failed");
    }

    @Test
    @Order(2)
    void testGetUserLibrarySuccess() {
        userPlantRepository.savePlant(testUser, testUserPlantWorking);
        userPlantRepository.savePlant(testUser, testUserPlantWorking2);
        List<UserPlant> plants = userPlantRepository.getUserLibrary(testUser.getUniqueId());
        String commonNameTest = plants.get(0).getCommon_name();
        int idTest = plants.get(1).getId();
        assertAll(
                ()  -> assertEquals("Euphorbiaceae",commonNameTest, "It should be Euphorbiaceae :testGetUserLibrarySuccess failed"),
                ()  -> assertEquals(2862,idTest, "It should be 2862 :testGetUserLibrarySuccess failed")
        );
    }

    @Test
    @Order(3)
    void testGetUserLibraryEmpty() {
        List<UserPlant> plants = userPlantRepository.getUserLibrary(testUser.getUniqueId());
        assertTrue(plants.isEmpty(),
                "it should be empty (this failed for an empty array) :testGetUserLibraryEmpty failed");
    }

    @Test
    @Order(4)
    void testGetUserLibraryNull() {
        userPlantRepository.savePlant(testUser, null);
        List<UserPlant> plants = userPlantRepository.getUserLibrary(testUser.getUniqueId());
        assertFalse(false,
                "it should be empty (this failed for the value null: testGetUserLibraryNull failed");
    }

    @Test
    @Order(5)
    void testDeletePlantSuccess() {
        userPlantRepository.savePlant(testUser, testUserPlantWorking);
        boolean result = userPlantRepository.deletePlant(testUser.getUniqueId(), testUserPlantWorking.getId());
        assertEquals(true,result,
                "The plant was not deleted :testDeletePlantSuccess failed");
    }

    @Test
    @Order(6)
    void testDeletePlantIllegalPlantID() {
        userPlantRepository.savePlant(testUser, testUserPlantWorking);
        boolean result = userPlantRepository.deletePlant(testUser.getUniqueId(), -1);
        assertEquals(false,result,
                "A non existing plant was deleted :testDeletePlantIllegalPlantID failed");
    }

    @Test
    @Order(7)
    void testChangeLastWateredSuccess() {
        userPlantRepository.savePlant(testUser, testUserPlantWorking);
        boolean result = userPlantRepository.changeLastWatered(testUser.getUniqueId(), testUserPlantWorking.getId());
        assertEquals(true, result,
                "The water was successfully changed :testChangeLastWateredSuccess failed");

    }

    @Test
    @Order(8)
    void testChangeLastWateredFailureIllegalUser() {
        userPlantRepository.savePlant(testUser, testUserPlantWorking);
        boolean result = userPlantRepository.changeLastWatered(-1, testUserPlantWorking.getId());
        assertEquals(false, result,
                "The water was successfully changed which it should not :testChangeLastWateredFailureIllegalUser failed");
    }

    @Test
    @Order(9)
    void testChangeNicknameSuccess() {
        userPlantRepository.savePlant(testUser, testUserPlantWorking);
        boolean result = userPlantRepository.changeNickname(testUser.getUniqueId(), testUserPlantWorking.getId(), "Adrian is a g" );
        assertEquals(true,result,
                "The nickname was not changed :testChangeNicknameSuccess failed");
    }

    @Test
    @Order(10)
    void testChangeNicknameFailureIllegalID() {
        userPlantRepository.savePlant(testUser, testUserPlantWorking);
        boolean result = userPlantRepository.changeNickname(-1, testUserPlantWorking.getId(), "Adrian is a g" );
        assertEquals(false,result,
                "The nickname was changed :testChangeNicknameFailureIllegalID failed");
    }


    @Test
    @Order(11)
    void testChangeNicknameFailureIllegalPlantID() {
        userPlantRepository.savePlant(testUser, testUserPlantWorking);
        boolean result = userPlantRepository.changeNickname(testUser.getUniqueId(), -1, "Adrian is a g" );
        assertEquals(false,result,
                "The nickname was changed :testChangeNicknameFailureIllegalPlantID failed");
    }


    @Test
    @Order(12)
    void testChangeNicknameFailureNullNickname() {
        userPlantRepository.savePlant(testUser, testUserPlantWorking);
        boolean result = userPlantRepository.changeNickname(testUser.getUniqueId(), testUserPlantWorking.getId(), null );
        assertEquals(false,result,
                "The nickname was changed :testChangeNicknameFailureNullNickname failed");
    }

    @Test
    @Order(13)
    void testChangeNicknameFailureNoNickname() {
        userPlantRepository.savePlant(testUser, testUserPlantWorking);
        boolean result = userPlantRepository.changeNickname(testUser.getUniqueId(), testUserPlantWorking.getId(), "" );
        assertEquals(false,result,
                "The nickname was changed :testChangeNicknameFailureNoNickname failed");
    }

    @Test
    @Order(14)
    void testWaterAllPlantsSuccess() {
        boolean result = userPlantRepository.changeAllToWatered(testUser.getUniqueId());
        assertEquals(true,result,
                "All plants weren't watered :testWaterAllPlantsSuccess failed");
    }

    @Test
    @Order(15)
    void testWaterAllPlantFailureIllegalUserID() {
        boolean result = userPlantRepository.changeAllToWatered(-1);
        assertEquals(false,result,
                "All plants were watered which they should not be :testWaterAllPlantFailureIllegalUserID failed");
    }
}
