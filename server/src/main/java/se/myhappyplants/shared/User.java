package se.myhappyplants.shared;

import java.io.*;

/**
 * Container class that defines a User
 * Created by: Linn Borgström
 * Updated by: Linn Borgström, 2021-05-17
 */
public class User {

    private int uniqueId;
    private String email;
    private String password;
    private String avatarURL;
    private String accessToken;
    private String securityQuestion;
    private String securityAnswer;
    private boolean isNotificationsActivated = true;
    private boolean funFactsActivated = true;

    /**
     * Constructor used when registering a new user account
     */
    public User(String email, String password, boolean isNotificationsActivated) {
        this.email = email;
        this.password = password;
        this.isNotificationsActivated = isNotificationsActivated;
    }

    public User(int uniqueId, String accessToken){
        this.uniqueId = uniqueId;
        this.accessToken = accessToken;
    }

    /**
     * Constructor used for testing purposes.
     * @author Douglas Almö Thorsell
     */
    public User(String email, String password, boolean isNotificationsActivated, boolean funFactsActivated) {
        this.email = email;
        this.password = password;
        this.isNotificationsActivated = isNotificationsActivated;
        this.funFactsActivated = funFactsActivated;
    }

    /**
     * Simple constructor for login requests
     */
    public User(String email, String password) {
        this.email = email;
        this.password = password;
    }

    public User(int uniqueID, String email, boolean notificationsActivated) {
        this.uniqueId = uniqueID;
        this.email = email;
        this.isNotificationsActivated = notificationsActivated;

    }
    /**
     * Constructor used to return a users details from the database
     */
    public User(int uniqueId, String email, boolean isNotificationsActivated, boolean funFactsActivated) {
        this.uniqueId = uniqueId;
        this.email = email;
        this.isNotificationsActivated = isNotificationsActivated;
        this.funFactsActivated = funFactsActivated;
    }

    /**
     * Constructor used for testing purposes.
     * @author Douglas Almö Thorsell
     */
    public User(int uniqueId, String email, String password, String accessToken, boolean isNotificationsActivated, boolean funFactsActivated) {
        this.uniqueId = uniqueId;
        this.email = email;
        this.password = password;
        this.accessToken = accessToken;
        this.isNotificationsActivated = isNotificationsActivated;
        this.funFactsActivated = funFactsActivated;
    }

    public int getUniqueId() {
        return uniqueId;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public boolean areNotificationsActivated() {
        return isNotificationsActivated;
    }

    public String getAccessToken(){
        return this.accessToken;
    }

    public void setAccessToken(String accessToken){
        this.accessToken = accessToken;
    }

    public void setIsNotificationsActivated(boolean notificationsActivated) {
        this.isNotificationsActivated = notificationsActivated;
    }

    public void setUniqueId(int uniqueId){
        this.uniqueId = uniqueId;
    }

    public String getAvatarURL() {
        return avatarURL;
    }

    public void setAvatar(String pathToImg) {
        this.avatarURL = new File(pathToImg).toURI().toString();
    }

    public boolean areFunFactsActivated() {
        return funFactsActivated;
    }

    public void setFunFactsActivated(boolean funFactsActivated) {
        this.funFactsActivated = funFactsActivated;
    }
    public String getSecurityQuestion() {
        return securityQuestion;
    }

    public String getSecurityAnswer() {
        return securityAnswer;
    }
}
