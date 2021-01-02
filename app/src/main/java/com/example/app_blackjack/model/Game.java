package com.example.app_blackjack.model;

public class Game {
    private Deck deck;
    private int userScore;
    private int dealerScore;
    private final int gameNumDecks;
    private final String gameChosenCardDesign;
    private double userBetAmount;
    private int numCardsRemaining;
    private int numCardsUsed;

    public Game(int numDecksIn, String cardDesignIn) {
        this.deck = new Deck();
        this.userScore = 0;
        this.dealerScore = 0;
        this.gameNumDecks = numDecksIn;
        this.gameChosenCardDesign = cardDesignIn;
        this.userBetAmount = 0.0;
        this.numCardsRemaining = 52;
        this.numCardsUsed = 0;
    }

    public Deck getDeck() {
        return deck;
    }

    public void setDeck(Deck deck) {
        this.deck = deck;
    }

    public void createMultiDeck(int numDecks) {
        Deck tempDeck;
        for (int i = 0; i < numDecks; i++) {
            tempDeck = new Deck();
            tempDeck.createDeck();
            deck.getPlayingDeck().addAll(tempDeck.getPlayingDeck());
        }
    }

    public int getUserScore() {
        return userScore;
    }

    public void setUserScore(int userScore) {
        this.userScore = userScore;
    }

    public int getDealerScore() {
        return dealerScore;
    }

    public void setDealerScore(int dealerScore) {
        this.dealerScore = dealerScore;
    }

    public int getGameNumDecks() {
        return gameNumDecks;
    }

    public String getGameChosenCardDesign() {
        return gameChosenCardDesign;
    }

    public double getUserBetAmount() {
        return userBetAmount;
    }

    public void setUserBetAmount(double userBetAmount) {
        this.userBetAmount = userBetAmount;
    }

    public int getNumCardsRemaining() {
        return numCardsRemaining;
    }

    public void setNumCardsRemaining(int numCardsRemaining) {
        this.numCardsRemaining = numCardsRemaining;
    }

    public int getNumCardsUsed() {
        return numCardsUsed;
    }

    public void setNumCardsUsed(int numCardsUsed) {
        this.numCardsUsed = numCardsUsed;
    }
}
