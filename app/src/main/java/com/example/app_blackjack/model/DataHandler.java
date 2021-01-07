package com.example.app_blackjack.model;

public class DataHandler {
    // Create instance fields
    private static DataHandler instance;
    private User user;
    private Game game;
    private int defaultNumDecks;
    private String defaultChosenCardDesign;
    private double defaultBalance;
    private double mostMoneyWon;
    private double mostMoneyLost;
    private String userMostMoneyWon;
    private String userMostMoneyLost;
    private boolean userLoggedIn;
    private boolean userGameStarted;
    private boolean randomGameStarted;
    private boolean userSessionLoadedFromSharedPref;
    private boolean userLoadedFromSharedPref;
    private boolean nonUserLoadedFromSharedPref;
    private boolean dataCleared;

    public static DataHandler getInstance() {
        if (instance == null)
            instance = new DataHandler();
        return instance;
    }

    public void clearData() {
        if (userLoggedIn) {
            user.resetUser();

            // Create a fragment indicating that user's stats will still be on all time record

//            mostMoneyWon = 0.0;
//            mostMoneyLost = 0.0;
//            userMostMoneyWon = "error";
//            userMostMoneyLost = "error";
            userGameStarted = false;
        } else {
            defaultNumDecks = 1;
            defaultChosenCardDesign = "blue";
            defaultBalance = 2000.0;
            randomGameStarted = false;
        }
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

    public void setGame(Game game) {
        this.game = game;
    }

    public int getDefaultNumDecks() {
        return defaultNumDecks;
    }

    public void setDefaultNumDecks(int defaultNumDecks) {
        this.defaultNumDecks = defaultNumDecks;
    }

    public String getDefaultChosenCardDesign() {
        return defaultChosenCardDesign;
    }

    public void setDefaultChosenCardDesign(String defaultChosenCardDesign) {
        this.defaultChosenCardDesign = defaultChosenCardDesign;
    }

    public double getDefaultBalance() {
        return defaultBalance;
    }

    public void setDefaultBalance(double defaultBalance) {
        this.defaultBalance = defaultBalance;
    }

    public double getMostMoneyWon() {
        return mostMoneyWon;
    }

    public void setMostMoneyWon(double mostMoneyWon) {
        this.mostMoneyWon = mostMoneyWon;
    }

    public double getMostMoneyLost() {
        return mostMoneyLost;
    }

    public void setMostMoneyLost(double mostMoneyLost) {
        this.mostMoneyLost = mostMoneyLost;
    }

    public String getUserMostMoneyWon() {
        return userMostMoneyWon;
    }

    public void setUserMostMoneyWon(String userMostMoneyWon) {
        this.userMostMoneyWon = userMostMoneyWon;
    }

    public String getUserMostMoneyLost() {
        return userMostMoneyLost;
    }

    public void setUserMostMoneyLost(String userMostMoneyLost) {
        this.userMostMoneyLost = userMostMoneyLost;
    }

    public boolean isUserLoggedIn() {
        return userLoggedIn;
    }

    public void setUserLoggedIn(boolean userLoggedIn) {
        this.userLoggedIn = userLoggedIn;
    }

    public boolean isUserGameStarted() {
        return userGameStarted;
    }

    public void setUserGameStarted(boolean userGameStarted) {
        this.userGameStarted = userGameStarted;
    }

    public boolean isRandomGameStarted() {
        return randomGameStarted;
    }

    public void setRandomGameStarted(boolean randomGameStarted) {
        this.randomGameStarted = randomGameStarted;
    }

    public boolean isUserSessionLoadedFromSharedPref() {
        return userSessionLoadedFromSharedPref;
    }

    public void setUserSessionLoadedFromSharedPref(boolean userSessionLoadedFromSharedPref) {
        this.userSessionLoadedFromSharedPref = userSessionLoadedFromSharedPref;
    }

    public boolean isUserLoadedFromSharedPref() {
        return userLoadedFromSharedPref;
    }

    public void setUserLoadedFromSharedPref(boolean userLoadedFromSharedPref) {
        this.userLoadedFromSharedPref = userLoadedFromSharedPref;
    }

    public boolean isNonUserLoadedFromSharedPref() {
        return nonUserLoadedFromSharedPref;
    }

    public void setNonUserLoadedFromSharedPref(boolean nonUserLoadedFromSharedPref) {
        this.nonUserLoadedFromSharedPref = nonUserLoadedFromSharedPref;
    }

    public boolean isDataCleared() {
        return dataCleared;
    }

    public void setDataCleared(boolean dataCleared) {
        this.dataCleared = dataCleared;
    }
}
