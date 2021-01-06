package com.example.app_blackjack.model;

import org.jetbrains.annotations.NotNull;
import java.util.ArrayList;

public class Game {
    private Deck deck;
    private int userScore;
    private int dealerScore;
    private final int gameNumDecks;
    private final String gameChosenCardDesign;
    private double userBetAmount;
    private int numCardsRemaining;
    private int numCardsUsed;
    private String currState;
    private ArrayList<Card> userDeck;
    private ArrayList<Card> dealerDeck;

    public Game(int numDecksIn, String cardDesignIn) {
        this.deck = new Deck();
        this.userScore = 0;
        this.dealerScore = 0;
        this.gameNumDecks = numDecksIn;
        this.gameChosenCardDesign = cardDesignIn;
        this.userBetAmount = 0.0;
        this.numCardsRemaining = 52;
        this.numCardsUsed = 0;
        this.currState = "SELECT_BET";
        this.userDeck = new ArrayList<>();
        this.dealerDeck = new ArrayList<>();
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

    public String getCurrState() {
        return currState;
    }

    public void setCurrState(String currState) {
        this.currState = currState;
    }

    public ArrayList<Card> getUserDeck() {
        return userDeck;
    }

    public void setUserDeck(ArrayList<Card> userDeck) {
        this.userDeck = userDeck;
    }

    public int getUserDeckSize() {
        return userDeck.size();
    }

    public void addCardUserDeck(Card cardDrawn) {
        userDeck.add(cardDrawn);
    }

    public void clearUserDeck() {
        userDeck.clear();
    }

    public ArrayList<Card> getDealerDeck() {
        return dealerDeck;
    }

    public void setDealerDeck(ArrayList<Card> dealerDeck) {
        this.dealerDeck = dealerDeck;
    }

    public int getDealerDeckSize() {
        return dealerDeck.size();
    }

    public void addCardDealerDeck(Card cardDrawn) {
        dealerDeck.add(cardDrawn);
    }

    public void clearDealerDeck() {
        dealerDeck.clear();
    }

    @NotNull
    @Override
    public String toString() {
        return "User Deck\n" + createDeckStringOutput(true) + "\nDealer Deck\n" + createDeckStringOutput(false);
    }

    private StringBuilder createDeckStringOutput(boolean deckIndicator) {
        StringBuilder output = new StringBuilder();
        for (Card c : (deckIndicator ? userDeck : dealerDeck)) {
            output.append(c.getCardID()).append(" ").append(c.getCardValue()).append("\n");
        }
        return output;
    }
}
