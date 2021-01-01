package com.example.app_blackjack.model;
import org.jetbrains.annotations.NotNull;
import java.util.ArrayList;
import java.util.Collections;

public class Deck {
    public enum Suit {
        SPADE,
        HEART,
        CLUB,
        DIAMOND
    }

    public enum Rank {
        TWO,
        THREE,
        FOUR,
        FIVE,
        SIX,
        SEVEN,
        EIGHT,
        NINE,
        TEN,
        JACK,
        QUEEN,
        KING,
        ACE,
    }

    private final ArrayList<Card> deck;
    private final ArrayList<Card> usedDeck;

    public Deck() {
        this.deck = new ArrayList<>();
        this.usedDeck = new ArrayList<>();
    }

    public void createDeck() {
        for (Suit suit : Suit.values()) {
            for (Rank rank : Rank.values()) {
                Card card = new Card(suit, rank, fetchID(suit, rank), fetchValue(rank));
                deck.add(card);
            }
        }
    }

    public void shuffleDeck() {
        Collections.shuffle(this.deck);
    }

    public Card drawCard() {
        Card cardDrawn = deck.get(0);
        usedDeck.add(cardDrawn);
        deck.remove(0);
        return cardDrawn;
    }

    public boolean isDeckEmpty() {
        return deck.isEmpty();
    }

    public ArrayList<Card> getDeck() {
        return deck;
    }

    public int getNumCardsRemaining() {
        return deck.size();
    }

    public ArrayList<Card> getUsedDeck() {
        return usedDeck;
    }

    public int getNumCardsUsed() {
        return usedDeck.size();
    }

    private String fetchID(Suit suit, Rank rank) {
        return (rank == Rank.JACK || rank == Rank.QUEEN || rank == Rank.KING || rank == Rank.ACE) ?
                fetchSuitValue(suit).concat(fetchAlternateValue(rank)) :
                fetchSuitValue(suit).concat(Integer.toString(fetchValue(rank)));
    }

    @NotNull
    @Override
    public String toString()
    {
        StringBuilder deckOutput = new StringBuilder();
        for (Card c : deck) {
            deckOutput.append(c.getCardID()).append(" ").append(c.getCardValue()).append("\n");
        }
        return deckOutput.toString();
    }

    private String fetchSuitValue(Suit suit) {
        switch (suit) {
            case SPADE:
                return "s";
            case HEART:
                return "h";
            case CLUB:
                return "c";
            case DIAMOND:
                return "d";
            default:
                return "error";
        }
    }

    private int fetchValue(Rank rank) {
        switch (rank) {
            case JACK: case QUEEN: case KING:
                return 10;
            case ACE:
                return 11;
            default:
                return rank.ordinal() + 2;
        }
    }

    private String fetchAlternateValue(Rank rank) {
        switch (rank) {
            case TEN:
                return "10";
            case JACK:
                return "j";
            case QUEEN:
                return "q";
            case KING:
                return "k";
            case ACE:
                return "a";
            default:
                return "error";
        }
    }
}