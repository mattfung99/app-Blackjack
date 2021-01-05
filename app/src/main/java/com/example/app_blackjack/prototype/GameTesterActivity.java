package com.example.app_blackjack.prototype;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import com.example.app_blackjack.R;
import com.example.app_blackjack.model.Card;
import com.example.app_blackjack.model.DataHandler;
import com.example.app_blackjack.model.Game;
import com.google.gson.Gson;
import java.util.Objects;

public class GameTesterActivity extends AppCompatActivity {
    private final DataHandler dHandler = DataHandler.getInstance();
    private Game game;
    private Card cardDrawn;

    private TextView displayCardName;
    private ImageView displayCard;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_tester);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        setupWidgets();

        if (dHandler.isUserLoggedIn())
        {
            if (dHandler.isUserGameStarted()) {
                game = dHandler.getGame();
                displayWidgets(true);
            } else {
                displayDefaultWidgets(true);
                createGame(true);
                dHandler.setUserGameStarted(true);
                dHandler.getUser().setThisUserGameStarted(true);
                saveUserIntoSharedPref();
            }
        } else {
            if (dHandler.isRandomGameStarted()) {
                game = dHandler.getGame();
                displayWidgets(false);
            } else {
                displayDefaultWidgets(false);
                createGame(false);
                dHandler.setRandomGameStarted(true);
            }
        }
        setupDrawBtn();
    }

    private void createGame(boolean isUserLoggedIn) {
        dHandler.setGame(isUserLoggedIn ?
                new Game(dHandler.getUser().getNumDecks(), dHandler.getUser().getChosenCardDesign()) :
                new Game(dHandler.getDefaultNumDecks(), dHandler.getDefaultChosenCardDesign())
        );
        game = dHandler.getGame();
        game.getDeck().createDeck();
        if (dHandler.getDefaultNumDecks() > 1) {
            game.createMultiDeck(dHandler.getDefaultNumDecks() - 1);
        }
        game.getDeck().shuffleDeck();
    }

    private void setupWidgets() {
        displayCardName = (TextView)findViewById(R.id.textGameDisplayCardName);
        displayCard = (ImageView)findViewById(R.id.imageGameDisplayCard);
    }

    private void displayWidgets(boolean gameFlag) {
        if (game.getDeck().getNumCardsUsed() == 0) {
            displayDefaultWidgets(gameFlag);
        } else {
            displayCardName.setText(getString(R.string.test_display_card_name, game.getDeck().getUsedDeck().get(game.getDeck().getNumCardsUsed() - 1).getCardID()));
            int resID = getResources().getIdentifier(game.getDeck().getUsedDeck().get(game.getDeck().getNumCardsUsed() - 1).getCardID() , "drawable", getPackageName());
            displayCard.setImageResource(resID);
        }
    }

    private void displayDefaultWidgets(boolean gameFlag) {
        displayCardName.setText(getString(R.string.test_display_card_name_default));
        int resID;
        if (gameFlag) {
            resID = getResources().getIdentifier(dHandler.getUser().getChosenCardDesign(), "drawable", getPackageName());
        } else {
            resID = getResources().getIdentifier(dHandler.getDefaultChosenCardDesign(), "drawable", getPackageName());
        }
        displayCard.setImageResource(resID);
    }

    private void setupDrawBtn() {
        Button btnDrawCard = (Button)findViewById(R.id.btnGameDrawCard);
        btnDrawCard.setOnClickListener(v -> {
            cardDrawn = game.getDeck().drawCard();
            displayCardName.setText(getString(R.string.test_display_card_name, cardDrawn.getCardID()));
            int resID = getResources().getIdentifier(cardDrawn.getCardID() , "drawable", getPackageName());
            displayCard.setImageResource(resID);
            saveGameInSharedPref();
        });
    }

    private void saveUserIntoSharedPref() {
        SharedPreferences userPref = getSharedPreferences("PREF_USERS", MODE_PRIVATE);
        SharedPreferences.Editor userEditor = userPref.edit();
        Gson gson = new Gson();
        String json = gson.toJson(dHandler.getUser());
        userEditor.putString(dHandler.getUser().getUsername(), json);
        userEditor.apply();
    }

    private void saveGameInSharedPref() {
        SharedPreferences sessionPref = getSharedPreferences("PREF_USER_SESSION", MODE_PRIVATE);
        SharedPreferences.Editor sessionEditor = sessionPref.edit();
        SharedPreferences gamePref = getSharedPreferences("PREF_GAMES", MODE_PRIVATE);
        SharedPreferences.Editor gameEditor = gamePref.edit();
        Gson gson = new Gson();
        String json = gson.toJson(game);
        if (dHandler.isUserLoggedIn()) {
            gameEditor.putString(dHandler.getUser().getUsername() + "Game", json);
            sessionEditor.putBoolean("userGameStarted", dHandler.isUserGameStarted());
        } else {
            gameEditor.putString("randomGame", json);
            sessionEditor.putBoolean("randomGameStarted", dHandler.isRandomGameStarted());
        }
        gameEditor.apply();
        sessionEditor.apply();
    }

    public static Intent makeIntent(Context context) {
        return new Intent(context, GameTesterActivity.class);
    }
}