package com.example.app_blackjack.prototype;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_tester);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);

        game = dHandler.getGame();

        System.out.println(game.getDeck());

        int numDecks = 2;

//        game.getDeck().createDeck();

        if (numDecks > 1) {
//            game.setDeck(game.createMultiDeck(game.getDeck(), numDecks - 1));
        }
//        game.getDeck().shuffleDeck();
//        System.out.println(game.getDeck());
//        setupDrawBtn();
    }

    private void setupDrawBtn() {
        TextView displayCardName = (TextView)findViewById(R.id.textGameDisplayCardName);
        displayCardName.setText(getString(R.string.test_display_card_name_default));
        ImageView displayCard = (ImageView)findViewById(R.id.imageGameDisplayCard);
        displayCard.setImageResource(R.drawable.gray);
        Button btnDrawCard = (Button)findViewById(R.id.btnGameDrawCard);
        btnDrawCard.setOnClickListener(v -> {
            cardDrawn = game.getDeck().drawCard();
            displayCardName.setText(getString(R.string.test_display_card_name, cardDrawn.getCardID()));
            int resID = getResources().getIdentifier(cardDrawn.getCardID() , "drawable", getPackageName());
            displayCard.setImageResource(resID);
        });
    }

    public static Intent makeIntent(Context context) {
        return new Intent(context, GameTesterActivity.class);
    }
}