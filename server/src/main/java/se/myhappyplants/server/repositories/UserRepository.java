package se.myhappyplants.server.repositories;

import org.mindrot.jbcrypt.BCrypt;
import se.myhappyplants.shared.TokenStatus;
import se.myhappyplants.shared.User;

import java.security.SecureRandom;
import java.sql.*;
import java.util.Base64;
import java.util.function.BinaryOperator;

public class UserRepository extends Repository {

    public boolean saveUser(User user) {
        if(!checkEmailAndPasswordLegal(user.getEmail(), user.getPassword())){
            return false;
        }
        if(user.getSecurityAnswer() == null || user.getSecurityQuestion() == null){
            return false;
        }
        if(!checkSecurityQuestionLegal(user.getSecurityQuestion(), user.getSecurityAnswer())){
            return false;
        }

        boolean success = false;
        String hashedPassword = BCrypt.hashpw(user.getPassword(), BCrypt.gensalt());
        String hashedAnswer = BCrypt.hashpw(user.getSecurityAnswer(), BCrypt.gensalt());

        String query = """
                INSERT INTO registered_users (email, password, security_question, security_answer) VALUES (?, ?, ?, ?);
                """;
        try (java.sql.Connection connection = startConnection()) {
            try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
                preparedStatement.setString(1, user.getEmail());
                preparedStatement.setString(2, hashedPassword);
                preparedStatement.setString(3, user.getSecurityQuestion());
                preparedStatement.setString(4, hashedAnswer);
                preparedStatement.executeUpdate();
                success = true;
            }
        } catch (SQLException sqlException) {
            System.out.println(sqlException.getMessage());
        }
        return success;
    }

    public boolean checkLogin(String email, String password) {
        boolean isVerified = false;
        String query = """
                SELECT password FROM registered_users WHERE email = ?;
                """;
        try (java.sql.Connection connection = startConnection()) {
            try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
                preparedStatement.setString(1, email);
                ResultSet resultSet = preparedStatement.executeQuery();
                if (resultSet.next()) {
                    String hashedPassword = resultSet.getString(1);
                    isVerified = BCrypt.checkpw(password, hashedPassword);
                }
            }
        } catch (SQLException sqlException) {
            System.out.println(sqlException.getMessage());
        }
        return isVerified;
    }

    public String getSecurityQuestion(String email) {
        String question = "";

        String query = """
                SELECT security_question FROM registered_users WHERE email = ?;
                """;
        try (java.sql.Connection connection = startConnection()) {
            try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
                preparedStatement.setString(1, email);
                ResultSet resultSet = preparedStatement.executeQuery();
                if (resultSet.next()) {
                    question = resultSet.getString(1);
                }
            }
        } catch (SQLException sqlException) {
            System.out.println(sqlException.getMessage());
        }
        return question;
    }

    public boolean verifySecurityQuestion(String email, String userAnswer) {
        boolean verified = false;

        String query = """
                SELECT security_answer FROM registered_users WHERE email = ?;
                """;
        try (java.sql.Connection connection = startConnection()) {
            try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
                preparedStatement.setString(1, email);
                ResultSet resultSet = preparedStatement.executeQuery();
                if (resultSet.next()) {
                    String actualAnswer = resultSet.getString(1);
                    if(BCrypt.checkpw(userAnswer, actualAnswer)){
                        verified = true;
                    }
                }
            }
        } catch (SQLException sqlException) {
            System.out.println(sqlException.getMessage());
        }
        return verified;
    }

    public boolean updatePasswordWithSecurityQuestion(String email, String userAnswer, String newPassword) {
        boolean updated = false;
        if(!verifySecurityQuestion(email, userAnswer)){
            return false;
        }

        String hashedPassword = BCrypt.hashpw(newPassword, BCrypt.gensalt());


        String query = """
                UPDATE registered_users SET password = ? WHERE email = ?;
                """;
        try (java.sql.Connection connection = startConnection()) {
            try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
                preparedStatement.setString(1, hashedPassword);
                preparedStatement.setString(2, email);
                preparedStatement.executeUpdate();
                updated = true;
            }
        } catch (SQLException sqlException) {
            System.out.println(sqlException.getMessage());
        }
        return updated;
    }



    public User getUserDetails(String email) {
        User user = null;
        String query = """
                SELECT id, notification_activated, fun_facts_activated FROM registered_users WHERE email = ?;
                """;
        try (java.sql.Connection connection = startConnection()) {
            try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
                preparedStatement.setString(1, email);
                ResultSet resultSet = preparedStatement.executeQuery();
                while (resultSet.next()) {
                    int uniqueID = resultSet.getInt(1);
                    boolean notificationActivated = resultSet.getBoolean(2);
                    boolean funFactsActivated = resultSet.getBoolean(3);
                    user = new User(uniqueID, email, notificationActivated, funFactsActivated);
                }
            }
        } catch (SQLException sqlException) {
            System.out.println(sqlException.getMessage());
        }
        return user;
    }

    public boolean deleteAccount(String email, String password) {
        boolean accountDeleted = false;
        if (checkLogin(email, password)) {
            String query = """
                    SELECT id FROM registered_users WHERE email = ?;
                    """;
            try (Connection connection = startConnection()) {
                connection.setAutoCommit(false);
                try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
                    preparedStatement.setString(1, email);
                    ResultSet resultSet = preparedStatement.executeQuery();
                    if (!resultSet.next()) {
                        throw new SQLException();
                    }
                    int id = resultSet.getInt(1);
                    String queryDeletePlants = """
                            DELETE FROM user_plants WHERE user_id = ?;
                            """;
                    try (PreparedStatement deletePlantsStatement = connection.prepareStatement(queryDeletePlants)) {
                        deletePlantsStatement.setInt(1, id);
                        deletePlantsStatement.executeUpdate();
                    }
                    String queryDeleteUser = """
                            DELETE FROM registered_users WHERE id = ?;
                            """;
                    try (PreparedStatement deleteUserStatement = connection.prepareStatement(queryDeleteUser)) {
                        deleteUserStatement.setInt(1, id);
                        deleteUserStatement.executeUpdate();
                    }
                    accountDeleted = true;
                } catch (SQLException sqlException) {
                    connection.rollback();
                    System.out.println("Transaction rolled back");
                    System.out.println("Account was not deleted");
                    System.out.println(sqlException.getMessage());
                } finally {
                    connection.setAutoCommit(true);
                }
            } catch (SQLException sqlException) {
                System.out.println(sqlException.getMessage());
            }
        }
        return accountDeleted;
    }

    public boolean changeNotifications(String email, boolean notifications) {
        boolean notificationsChanged = false;
        String query = """
                UPDATE registered_users SET notification_activated = ? WHERE email = ?;
                """;
        try (java.sql.Connection connection = startConnection()) {
            try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
                preparedStatement.setBoolean(1, notifications);
                preparedStatement.setString(2, email);
                notificationsChanged = true;
            }
        } catch (SQLException sqlException) {
            System.out.println(sqlException.getMessage());
        }
        return notificationsChanged;
    }

    public boolean changeFunFacts(String email, boolean funFactsActivated) {
        boolean funFactsChanged = false;
        String query = """
                UPDATE registered_users SET fun_facts_activated = ? WHERE email = ?;
                """;
        try (java.sql.Connection connection = startConnection()) {
            try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
                preparedStatement.setBoolean(1, funFactsActivated);
                preparedStatement.setString(2, email);
                funFactsChanged = true;
            }
        } catch (SQLException sqlException) {
            System.out.println(sqlException.getMessage());
        }
        return funFactsChanged;
    }

    public String getNewAccessToken(String email, String password){
        if(!checkLogin(email, password)){
            return null;
        }

        deleteAccessToken(email, password);

        String token = generateToken();
        long creation_time = System.currentTimeMillis();

        String query = """
                INSERT INTO access_token (token, user_email, creation_time)
                VALUES (?, ?, ?);
                """;
        try (java.sql.Connection connection = startConnection()) {
            try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
                preparedStatement.setString(1, token);
                preparedStatement.setString(2, email);
                preparedStatement.setLong(3, creation_time);
                preparedStatement.executeUpdate();
            }
        } catch (SQLException sqlException) {
            System.out.println(sqlException.getMessage());
        }

        return token;
    }

    public boolean deleteAccessToken(String email, String password){
        if(!checkLogin(email, password)){
            return false;
        }
        boolean success = false;
        String query = """
                DELETE from access_token WHERE user_email = ?;
                """;
        try (java.sql.Connection connection = startConnection()) {
            try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
                preparedStatement.setString(1, email);
                preparedStatement.executeUpdate();
                success = true;
            }
        } catch (SQLException sqlException) {
            System.out.println(sqlException.getMessage());
        }
        return success;
    }

    public TokenStatus verifyAccessToken(int userID, String accessToken){
        String returnedToken;
        long tokenCreated;
        TokenStatus tokenStatus = TokenStatus.NO_MATCH;

        String query = """
                select at.token, at.creation_time from registered_users as ru
                join access_token as at on ru.email = at.user_email
                where ru.id = ?;
                """;

        try (java.sql.Connection connection = startConnection()) {
            try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
                preparedStatement.setInt(1, userID);
                ResultSet resultSet = preparedStatement.executeQuery();
                while (resultSet.next()) {
                    returnedToken = resultSet.getString(1);
                    tokenCreated = resultSet.getLong(2);

                    if(returnedToken.equals(accessToken)){
                        tokenStatus = TokenStatus.EXPIRED;

                        if((System.currentTimeMillis() - tokenCreated) < 3600000){
                            tokenStatus = TokenStatus.VALID;
                        }
                    }
                }
            }
        } catch (SQLException sqlException) {
            System.out.println(sqlException.getMessage());
        }

        return tokenStatus;
    }


    /**
     * Checks if the email and password of a user is a legal input.
     * @author Douglas Almö Thorsell
     */
    public boolean checkEmailAndPasswordLegal(String email, String password){
        if(password == null || password.length() > 72 || password.length() < 6){
            return false;
        }

        if(email == null || email.isEmpty()){
            return false;
        }

        if(!email.contains("@") || !email.contains(".")){
            return false;
        }

        return true;
    }

    public boolean checkSecurityQuestionLegal(String question, String answer){
        if(question.isEmpty() || question.length() > 100){
            return false;
        }

        if (answer.isEmpty() || answer.length() > 50){
            return false;
        }

        return true;
    }

    /**
     * Generates an access token for a user.
     * @author Douglas Almö Thorsell
     */
    private String generateToken(){
        SecureRandom secureRandom = new SecureRandom();
        Base64.Encoder base64Encoder = Base64.getUrlEncoder();

        byte[] randomBytes = new byte[24];
        secureRandom.nextBytes(randomBytes);

        return base64Encoder.encodeToString(randomBytes);
    }



    //This will be deleted later. It's currently used for manual testing of the authentication system.

    public static void main(String[] args) {
        UserRepository ur = new UserRepository();
/*        User testUser = new User(
                "test@testmail.com",
                "test123",
                true,
                true
        );


        ur.saveUser(testUser);
        String token = ur.getNewAccessToken(testUser.getEmail(), testUser.getPassword());
        System.out.println(token);
        ur.deleteAccessToken(testUser.getEmail(), testUser.getPassword());*/

        System.out.println(ur.verifyAccessToken(90, "SqAum_5KmAgJVo7_6gHWxfmgcMGcefVl"));

    }


}

