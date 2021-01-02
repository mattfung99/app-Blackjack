package com.example.app_blackjack.model;

public class User {
    // Create instance fields
    private String username;
    private String password;
    private String filepath;
    private int numDecks;
    private String chosenCardDesign;
    private boolean isThisUserGameStarted;
    private double balance;
    private double userMostMoneyWon;
    private double userMostMoneyLost;
    private int gamesWon;
    private int gamesLost;
    private int gamesForfeited;
    private int numTimesBankrupted;

    public User(String username, String password, String filepath) {
        this.username = username;
        this.password = password;
        this.filepath = filepath;
        this.numDecks = 1;
        this.chosenCardDesign = "blue";
        this.isThisUserGameStarted = false;
        this.balance = 5000.00;
        this.userMostMoneyWon = 0;
        this.userMostMoneyLost = 0;
        this.gamesWon = 0;
        this.gamesLost = 0;
        this.gamesForfeited = 0;
        this.numTimesBankrupted = 0;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getFilepath() {
        return filepath;
    }

    public void setFilepath(String filepath) {
        this.filepath = filepath;
    }

    public int getNumDecks() {
        return numDecks;
    }

    public void setNumDecks(int numDecks) {
        this.numDecks = numDecks;
    }

    public String getChosenCardDesign() {
        return chosenCardDesign;
    }

    public void setChosenCardDesign(String chosenCardDesign) {
        this.chosenCardDesign = chosenCardDesign;
    }

    public boolean isThisUserGameStarted() {
        return isThisUserGameStarted;
    }

    public void setThisUserGameStarted(boolean thisUserGameStarted) {
        isThisUserGameStarted = thisUserGameStarted;
    }

    public double getBalance() {
        return balance;
    }

    public void setBalance(double balance) {
        this.balance = balance;
    }

    public double getUserMostMoneyWon() {
        return userMostMoneyWon;
    }

    public void setUserMostMoneyWon(double userMostMoneyWon) {
        this.userMostMoneyWon = userMostMoneyWon;
    }

    public double getUserMostMoneyLost() {
        return userMostMoneyLost;
    }

    public void setUserMostMoneyLost(double userMostMoneyLost) {
        this.userMostMoneyLost = userMostMoneyLost;
    }

    public int getGamesWon() {
        return gamesWon;
    }

    public void setGamesWon(int gamesWon) {
        this.gamesWon = gamesWon;
    }

    public int getGamesLost() {
        return gamesLost;
    }

    public void setGamesLost(int gamesLost) {
        this.gamesLost = gamesLost;
    }

    public int getGamesForfeited() {
        return gamesForfeited;
    }

    public void setGamesForfeited(int gamesForfeited) {
        this.gamesForfeited = gamesForfeited;
    }

    public int getNumTimesBankrupted() {
        return numTimesBankrupted;
    }

    public void setNumTimesBankrupted(int numTimesBankrupted) {
        this.numTimesBankrupted = numTimesBankrupted;
    }
}
