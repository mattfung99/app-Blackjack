package com.example.app_blackjack.model;

public class DataHandler {
    // Create instance fields
    private static DataHandler instance;
    private User user;
    private Game game;
    private double mostMoneyWon;
    private double mostMoneyLost;
    private boolean userLoggedIn;
    private boolean dataLoadedFromSharedPref;

    public static DataHandler getInstance() {
        if (instance == null)
            instance = new DataHandler();
        return instance;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Game getGame() {
        return game;
    }

    public boolean isUserLoggedIn() {
        return userLoggedIn;
    }

    public void setUserLoggedIn(boolean userLoggedIn) {
        this.userLoggedIn = userLoggedIn;
    }

    public boolean isDataLoadedFromSharedPref() {
        return dataLoadedFromSharedPref;
    }

    public void setDataLoadedFromSharedPref(boolean dataLoadedFromSharedPref) {
        this.dataLoadedFromSharedPref = dataLoadedFromSharedPref;
    }
}
