package com.example.app_blackjack.ui;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import com.example.app_blackjack.R;
import com.example.app_blackjack.model.Card;
import com.example.app_blackjack.model.DataHandler;
import com.example.app_blackjack.model.Game;
import java.util.Objects;

public class GameActivity extends AppCompatActivity {
    // Reference the singleton instance
    private final DataHandler dHandler = DataHandler.getInstance();
    private Game game;
    private Card cardDrawn;

    // UI Widgets
    private TextView textDealerScore;
    private TextView textUserScore;
    private TextView textUserBet;
    private Button btnHit;
    private Button btnStand;
    private LinearLayout containerDealerCards;
    private LinearLayout containerUserCards;
    private final ImageView[] DECK_CARDS = new ImageView[3];
    private final ImageButton[] BETTING_CHIPS = new ImageButton[4];

    private enum State {
        SELECT_BET,
        USER_GAMEPLAY,
        DEALER_GAMEPLAY,                // Note to self: Whether or not the dealer will draw a card will be pre-determined, this state will simply animate it
        RESULT,
        RESET
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        setupReferences();
        setupWidgets();
    }

    private void stateMachine(State currState) {
        switch (currState) {
            case SELECT_BET:
                this.setTitle(getResources().getStringArray(R.array.game_instructions)[0]);
                break;
            case USER_GAMEPLAY:
                this.setTitle(getResources().getStringArray(R.array.game_instructions)[1]);
                break;
            case DEALER_GAMEPLAY:
                this.setTitle(getResources().getStringArray(R.array.game_instructions)[2]);
                break;
            case RESULT:
                this.setTitle(getResources().getStringArray(R.array.game_instructions)[3]);
                break;
            case RESET:
                break;
        }
    }

    private void setupReferences() {
        textDealerScore = (TextView)findViewById(R.id.textDealerScore);
        textUserScore = (TextView)findViewById(R.id.textUserScore);
        textUserBet = (TextView)findViewById(R.id.textUserBet);
        btnHit = (Button)findViewById(R.id.btnHit);
        btnStand = (Button)findViewById(R.id.btnStand);
        containerDealerCards = (LinearLayout)findViewById(R.id.containerDealerCards);
        containerUserCards = (LinearLayout)findViewById(R.id.containerUserCards);

    }

    private void setupWidgets() {
        setupDeckCards();
        setupBettingChips();
    }

    private void setupDeckCards() {
        int index = 1,
            resID;
        for (ImageView deckCard : DECK_CARDS) {
            resID = getResources().getIdentifier("deckCard" + index , "id", getPackageName());
            deckCard = (ImageButton) findViewById(resID);
            resID = getResources().getIdentifier(game.getGameChosenCardDesign(), "drawable", getPackageName());
            deckCard.setImageResource(resID);
            index++;
        }
    }

    private void setupBettingChips() {
        int index = 0,
            resID;
        for (ImageButton imBtn : BETTING_CHIPS) {
            resID = getResources().getIdentifier("imageBtn" + fetchChipName(index) , "id", getPackageName());
            imBtn = (ImageButton) findViewById(resID);
            int finalIndex = index;
            imBtn.setOnClickListener(v -> game.setUserBetAmount(fetchChipValue(finalIndex)));
            index++;
        }
    }

    private int fetchChipValue(int index) {
        switch (index) {
            case 0:
                return 10;
            case 1:
                return 25;
            case 2:
                return 50;
            case 3:
                return 100;
            default:
                return -1;
        }
    }

    private String fetchChipName(int index) {
        switch (index) {
            case 0:
                return "Blue";
            case 1:
                return "Green";
            case 2:
                return "Red";
            case 3:
                return "Black";
            default:
                return "error";
        }
    }

    public static Intent makeIntent(Context context) {
        return new Intent(context, GameActivity.class);
    }
}