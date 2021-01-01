package com.example.app_blackjack.prototype;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import com.example.app_blackjack.R;
import com.example.app_blackjack.model.Card;
import com.example.app_blackjack.model.DataHandler;
import com.example.app_blackjack.model.Game;
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
//        setupWidgets();
//        game = dHandler.getGame();
//
//        if (dHandler.isUserGameStarted()) {
//            displayWidgets();
//        } else {
//            displayDefaultWidgets();
//            game.getDeck().createDeck();
//            if (dHandler.getNumDecks() > 1) {
//                game.createMultiDeck(dHandler.getNumDecks() - 1);
//            }
//            game.getDeck().shuffleDeck();
//            dHandler.setUserGameStarted(true);
//        }
//        System.out.println(game.getDeck());
//        setupDrawBtn();
        System.out.println("gt create hit");
    }

    @Override
    protected void onResume() {
        super.onResume();
        System.out.println("gt resume hit");
    }

    private void setupWidgets() {
        displayCardName = (TextView)findViewById(R.id.textGameDisplayCardName);
        displayCard = (ImageView)findViewById(R.id.imageGameDisplayCard);
    }



    private void displayWidgets() {
        if (game.getDeck().getNumCardsUsed() == 0) {
            displayDefaultWidgets();
        } else {
            displayCardName.setText(getString(R.string.test_display_card_name, game.getDeck().getUsedDeck().get(game.getDeck().getNumCardsUsed() - 1).getCardID()));
            int resID = getResources().getIdentifier(game.getDeck().getUsedDeck().get(game.getDeck().getNumCardsUsed() - 1).getCardID() , "drawable", getPackageName());
            displayCard.setImageResource(resID);
        }
    }

    private void displayDefaultWidgets() {
        displayCardName.setText(getString(R.string.test_display_card_name_default));
        displayCard.setImageResource(R.drawable.gray);
    }

    private void setupDrawBtn() {
        Button btnDrawCard = (Button)findViewById(R.id.btnGameDrawCard);
        btnDrawCard.setOnClickListener(v -> {
            cardDrawn = game.getDeck().drawCard();
            displayCardName.setText(getString(R.string.test_display_card_name, cardDrawn.getCardID()));
            int resID = getResources().getIdentifier(cardDrawn.getCardID() , "drawable", getPackageName());
            displayCard.setImageResource(resID);
            game.getDeck().createDeckStringOutput();
        });
    }

    public static Intent makeIntent(Context context) {
        return new Intent(context, GameTesterActivity.class);
    }
}