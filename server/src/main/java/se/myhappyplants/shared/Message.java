package se.myhappyplants.shared;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

// TODO: streamline this class
public class Message {

    private MessageType messageType;
    private boolean notifications;
    private String messageText;
    private User user;
    private boolean success;
    private int plantID;
    private int userID;
    private LocalDate date;
    private List<Plant> plantArray;
    private List<UserPlant> userPlantList;
    private Plant plant;
    private String newNickname;
    private TokenStatus tokenStatus;


    public Message(boolean success) {
        this.success = success;
    }

    public Message(MessageType messageType, User user) {

        this.messageType = messageType;
        this.user = user;
    }

    public Message(MessageType messageType, TokenStatus tokenStatus){
        this.messageType = messageType;
        this.tokenStatus = tokenStatus;
    }

    public Message(MessageType messageType, int userID) {

        this.messageType = messageType;
        this.userID = userID;
    }

    public Message(MessageType messageType, User user, Plant plant) {
        this(messageType, user);
        this.plant = plant;
    }

    public Message(MessageType messageType, Plant plant) {
        this.messageType = messageType;
        this.plant = plant;
    }

    public Message(MessageType messageType, boolean notifications, User user) {
        this(messageType, user);
        this.notifications = notifications;
    }

    public Message(MessageType messageType, User user, Plant plant, LocalDate date) {
        this(messageType, user, plant);
        this.date = date;
    }

    public Message(MessageType messageType, User user, Plant plant, String newNickname) {
        this(messageType, user, plant);
        this.newNickname = newNickname;
    }

    public Message(List<Plant> plantArray, boolean success) {
        this.plantArray = plantArray;
        this.success = success;
    }

    public Message(ArrayList<UserPlant> plantArray, boolean success) {
        this.userPlantList = plantArray;
        this.success = success;
    }

    public Message(Plant plant, boolean success){
        this.plant = plant;
        this.success = success;
    }

    public Message(MessageType messageType, String messageText) {
        this.messageType = messageType;
        this.messageText = messageText;
    }

    public Message(User user, boolean success) {
        this.user = user;
        this.success = success;
    }

    public String getNewNickname() {
        return newNickname;
    }

    public MessageType getMessageType() {
        return messageType;
    }

    public String getMessageText() {
        return messageText;
    }

    public User getUser() {
        return user;
    }

    public boolean isSuccess() {
        return success;
    }

    public List<Plant> getPlantArray() {
        return plantArray;
    }

    public Plant getPlant() {
        return plant;
    }

    public int getPlantID() {
        return plantID;
    }

    public LocalDate getDate() {
        return date;
    }

    public boolean getNotifications() {
        return notifications;
    }

}
