package com.example.app_blackjack.model;

public class Card {
    private final Deck.Suit cardSuit;
    private final Deck.Rank cardRank;
    private final String cardID;
    private final int cardValue;

    public Card(Deck.Suit suit, Deck.Rank rank, String id, int value) {
        this.cardSuit = suit;
        this.cardRank = rank;
        this.cardID = id;
        this.cardValue = value;
    }

    public Deck.Suit getSuit() {
        return cardSuit;
    }

    public Deck.Rank getRank() {
        return cardRank;
    }

    public String getCardID() {
        return cardID;
    }

    public int getCardValue() {
        return cardValue;
    }

    @Override
    public boolean equals(Object o) {
        return (o instanceof Card && ((Card) o).cardRank == cardRank && ((Card) o).cardSuit == cardSuit);
    }
}
