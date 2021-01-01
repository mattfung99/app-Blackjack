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

    private final ArrayList<Card> playingDeck;
    private final ArrayList<Card> usedDeck;

    public Deck() {
        this.playingDeck = new ArrayList<>();
        this.usedDeck = new ArrayList<>();
    }

    public void createDeck() {
        for (Suit suit : Suit.values()) {
            for (Rank rank : Rank.values()) {
                Card card = new Card(suit, rank, fetchID(suit, rank), fetchValue(rank));
                playingDeck.add(card);
            }
        }
    }

    public void shuffleDeck() {
        Collections.shuffle(this.playingDeck);
    }

    public Card drawCard() {
        Card cardDrawn = playingDeck.get(0);
        usedDeck.add(cardDrawn);
        playingDeck.remove(0);
        return cardDrawn;
    }

    public boolean isDeckEmpty() {
        return playingDeck.isEmpty();
    }

    public ArrayList<Card> getPlayingDeck() {
        return playingDeck;
    }

    public int getNumCardsRemaining() {
        return playingDeck.size();
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
    public String toString() {
        return "Playing Deck\n" + createDeckStringOutput(true) + "\nUsed Deck" + createDeckStringOutput(false);
    }

    public void createDeckStringOutput() {
        System.out.println(createDeckStringOutput(false));
    }

    private StringBuilder createDeckStringOutput(boolean deckIndicator) {
        StringBuilder output = new StringBuilder();
        for (Card c : (deckIndicator ? playingDeck : usedDeck)) {
            output.append(c.getCardID()).append(" ").append(c.getCardValue()).append("\n");
        }
        return output;
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