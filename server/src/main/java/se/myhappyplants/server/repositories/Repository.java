package se.myhappyplants.server.repositories;

import se.myhappyplants.server.PasswordsAndKeys;

import java.sql.DriverManager;
import java.sql.SQLException;

public abstract class Repository {

    // use try-with-resources when starting connection
    public java.sql.Connection startConnection() {
        java.sql.Connection connection = null;
        try {
            connection = DriverManager.getConnection(PasswordsAndKeys.dbServerAddress, PasswordsAndKeys.dbUsername, PasswordsAndKeys.dbPassword);
        } catch (SQLException sqlException) {
            System.out.println(sqlException.getMessage());
        }
        return connection;
    }
}
