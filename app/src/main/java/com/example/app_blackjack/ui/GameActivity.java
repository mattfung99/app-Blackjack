package com.example.app_blackjack.ui;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import com.example.app_blackjack.R;
import com.example.app_blackjack.model.Card;
import com.example.app_blackjack.model.DataHandler;
import com.example.app_blackjack.model.Game;
import com.google.gson.Gson;
import java.util.Objects;

public class GameActivity extends AppCompatActivity {
    // Reference the singleton instance
    private final DataHandler dHandler = DataHandler.getInstance();
    private Game game;

    // UI Widgets
    private TextView textDealerScore;
    private TextView textUserScore;
    private TextView textUserBet;
    private TextView textUserBalance;
    private Button btnHit;
    private Button btnStand;
    private LinearLayout containerDealerCards;
    private LinearLayout containerUserCards;
    private final ImageView[] DECK_CARDS = new ImageView[3];
    private final ImageButton[] BETTING_CHIPS = new ImageButton[4];
    private ImageView hiddenCard;

    private enum State {
        SELECT_BET,
        DEAL_CARDS,
        USER_GAMEPLAY,
        DEALER_GAMEPLAY,                // Note to self: Whether or not the dealer will draw a card will be pre-determined, this state will simply animate it
        RESULT_LOSS,
        RESULT_PUSH,
        RESULT_WIN
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        setupReferences();
        setupGame();
    }

    private void stateMachine(State currState) {
        switch (currState) {
            case SELECT_BET:
                this.setTitle(getResources().getStringArray(R.array.game_instructions)[0]);
                textDealerScore.setText(getString(R.string.text_dealer_score_default));
                setBettingChips(true);
                setButtons(false);
                break;
            case DEAL_CARDS:
                dealCards();
                textDealerScore.setText(getString(R.string.text_dealer_score, game.getDealerDeck().get(1).getCardValue()));
                break;
            case USER_GAMEPLAY:
                this.setTitle(getResources().getStringArray(R.array.game_instructions)[1]);
                setBettingChips(false);
                setButtons(true);
                break;
            case DEALER_GAMEPLAY:
                this.setTitle(getResources().getStringArray(R.array.game_instructions)[2]);
                textDealerScore.setText(getString(R.string.text_dealer_score, game.getDealerScore()));
                int resID = getResources().getIdentifier(game.getDealerDeck().get(0).getCardID(), "drawable", getPackageName());
                hiddenCard.setImageResource(resID);
                setButtons(false);
                break;
            case RESULT_LOSS:
                setButtons(false);
                this.setTitle(getString(R.string.result_loss, game.getUserBetAmount()));
                createAlertDialog(-1);
                break;
            case RESULT_PUSH:
                this.setTitle(getString(R.string.result_push));
                if (dHandler.isUserLoggedIn()) {
                    dHandler.getUser().setBalance(dHandler.getUser().getBalance() + game.getUserBetAmount());
                } else {
                    dHandler.setDefaultBalance(dHandler.getDefaultBalance() + game.getUserBetAmount());
                }
                updateScoreboard();
                createAlertDialog(0);
                break;
            case RESULT_WIN:
                this.setTitle(getString(R.string.result_win, game.getUserBetAmount() * 2));
                if (dHandler.isUserLoggedIn()) {
                    dHandler.getUser().setBalance(dHandler.getUser().getBalance() + 2 * game.getUserBetAmount());
                } else {
                    dHandler.setDefaultBalance(dHandler.getDefaultBalance() + 2 * game.getUserBetAmount());
                }
                updateScoreboard();
                createAlertDialog(1);
                break;
        }
    }

    private void setupReferences() {
        textDealerScore = (TextView)findViewById(R.id.textDealerScore);
        textUserScore = (TextView)findViewById(R.id.textUserScore);
        textUserBet = (TextView)findViewById(R.id.textUserBet);
        textUserBalance = (TextView)findViewById(R.id.textUserBalance);
        btnHit = (Button)findViewById(R.id.btnHit);
        btnStand = (Button)findViewById(R.id.btnStand);
        containerDealerCards = (LinearLayout)findViewById(R.id.containerDealerCards);
        containerUserCards = (LinearLayout)findViewById(R.id.containerUserCards);
    }

    private void setupGame() {
        if (dHandler.isUserLoggedIn())
        {
            if (dHandler.isUserGameStarted()) {
                game = dHandler.getGame();
//                displayWidgets(true);
            } else {
                createGame(true);
                dHandler.setUserGameStarted(true);
                dHandler.getUser().setThisUserGameStarted(true);
//                saveUserIntoSharedPref();
            }
        } else {
            if (dHandler.isRandomGameStarted()) {
                game = dHandler.getGame();
//                displayWidgets(false);
            } else {
                createGame(false);
                dHandler.setRandomGameStarted(true);
            }
        }
        setupWidgets();
        stateMachine(State.valueOf(game.getCurrState()));
        updateScoreboard();
    }

    private void setupWidgets() {
        setupDeckCards();
        setupBettingChips();
        setupHitButton();
        setupStandButton();
    }

    private void setupDeckCards() {
        int index = 1,
            resID;
        for (ImageView deckCard : DECK_CARDS) {
            resID = getResources().getIdentifier("deckCard" + index , "id", getPackageName());
            deckCard = (ImageView) findViewById(resID);
            resID = getResources().getIdentifier(game.getGameChosenCardDesign() + "_flipped", "drawable", getPackageName());
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
            imBtn.setOnClickListener(v -> validateBet(finalIndex));
            BETTING_CHIPS[index] = imBtn;
            index++;
        }
    }

    private void validateBet(int index) {
        if (dHandler.isUserLoggedIn()) {
            if (dHandler.getUser().getBalance() - fetchChipValue(index) < 0.0) {
                Toast.makeText(this, getString(R.string.toast_invalid_bet), Toast.LENGTH_SHORT).show();
            } else {
                game.setUserBetAmount(fetchChipValue(index));
                dHandler.getUser().setBalance(dHandler.getUser().getBalance() - fetchChipValue(index));
                betValidated();
            }
        } else {
            if (dHandler.getDefaultBalance() - fetchChipValue(index) < 0.0) {
                Toast.makeText(this, getString(R.string.toast_invalid_bet), Toast.LENGTH_SHORT).show();
            } else {
                game.setUserBetAmount(fetchChipValue(index));
                dHandler.setDefaultBalance(dHandler.getDefaultBalance() - fetchChipValue(index));
                betValidated();
            }
        }
    }

    private void betValidated() {
        updateScoreboard();
        stateMachine(State.DEAL_CARDS);
        game.setCurrState(State.valueOf("DEAL_CARDS").toString());
    }

    private void setupHitButton() {
        btnHit.setOnClickListener(v -> {
            Card cardDrawn = game.getDeck().drawCard();
            game.addCardUserDeck(cardDrawn);
            expandContainer((int)getResources().getDimension(R.dimen.container_space), true);
            ImageView ivCard = setCardProperties(cardDrawn);
            setCardMargin(ivCard);
            containerUserCards.addView(ivCard);
            checkAceException(cardDrawn, true);
            validateUserScore();
            updateScoreboard();
        });
    }

    private void checkAceException(Card cardDrawn, boolean containerIndicator) {
        if (containerIndicator) {
            if (cardDrawn.getRank().toString().equals("ACE") && (game.getUserScore() + cardDrawn.getCardValue() > 21)) {
                game.setUserScore(game.getUserScore() + 1);
            } else {
                game.setUserScore(game.getUserScore() + cardDrawn.getCardValue());
            }
        } else {
            if (cardDrawn.getRank().toString().equals("ACE") && (game.getDealerScore() + cardDrawn.getCardValue() > 21)) {
                game.setDealerScore(game.getDealerScore() + 1);
            } else {
                game.setDealerScore(game.getDealerScore() + cardDrawn.getCardValue());
            }
        }
    }

    private void validateUserScore() {
        if (game.getUserScore() > 21) {
            stateMachine(State.RESULT_LOSS);
            game.setCurrState(State.valueOf("RESULT_LOSS").toString());
        }
    }

    private void setupStandButton() {
        btnStand.setOnClickListener(v -> {
            stateMachine(State.DEALER_GAMEPLAY);
            game.setCurrState(State.valueOf("DEALER_GAMEPLAY").toString());
        });
    }

    private void updateScoreboard() {
        textUserScore.setText(getString(R.string.text_user_score, game.getUserScore()));
        textUserBet.setText(getString(R.string.text_user_bet, game.getUserBetAmount()));
        if (dHandler.isUserLoggedIn()) {
            textUserBalance.setText(getString(R.string.text_user_balance, dHandler.getUser().getBalance()));
        } else {
            textUserBalance.setText(getString(R.string.text_user_balance, dHandler.getDefaultBalance()));
        }
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
        setDealCards();
    }

    private void setDealCards() {
        game.addCardUserDeck(game.getDeck().drawCard());
        game.addCardUserDeck(game.getDeck().drawCard());
        game.addCardDealerDeck(game.getDeck().drawCard());
        game.addCardDealerDeck(game.getDeck().drawCard());
        System.out.println(game);
    }

    private void setBettingChips(boolean isEnabled) {
        for (ImageButton imBtn : BETTING_CHIPS) {
            imBtn.setEnabled(isEnabled);
        }
    }

    private void setButtons(boolean isEnabled) {
        btnHit.setEnabled(isEnabled);
        btnStand.setEnabled(isEnabled);
    }

    private void dealCards() {
        expandContainer(game.getUserDeckSize() * (int)getResources().getDimension(R.dimen.container_space), true);
        drawCards(true);
        expandContainer(game.getDealerDeckSize() * (int)getResources().getDimension(R.dimen.container_space), false);
        drawCards(false);
        updateScoreboard();
        stateMachine(State.USER_GAMEPLAY);
        game.setCurrState(State.valueOf("USER_GAMEPLAY").toString());
    }

    private void expandContainer(int addedWidth, boolean containerIndicator) {
        int currContainerWidth;
        if (containerIndicator) {
            currContainerWidth = containerUserCards.getLayoutParams().width;
            containerUserCards.getLayoutParams().width = currContainerWidth + addedWidth;
        } else {
            currContainerWidth = containerDealerCards.getLayoutParams().width;
            containerDealerCards.getLayoutParams().width = currContainerWidth + addedWidth;
        }
    }

    private void drawCards(boolean containerIndicator) {
        int index = 0;
        for (Card card : (containerIndicator ? game.getUserDeck() : game.getDealerDeck())) {
            ImageView ivCard = setCardProperties(card);
            setCardProperties(ivCard, card, index, containerIndicator);
            index++;
        }
//        saveGameInSharedPref();
    }

    private ImageView setCardProperties(Card card) {
        int resID = getResources().getIdentifier(card.getCardID(), "drawable", getPackageName());
        ImageView ivCard = new ImageView(this);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                (int) getResources().getDimension(R.dimen.card_width),
                (int) getResources().getDimension(R.dimen.card_height)
        );
        ivCard.setLayoutParams(params);
        ivCard.setImageResource(resID);
        return ivCard;
    }

    private void setCardProperties(ImageView ivCard, Card card, int index, boolean containerIndicator) {
        if (containerIndicator) {
            if (index > 0) {
                setCardMargin(ivCard);
            }
            checkAceException(card, true);
            containerUserCards.addView(ivCard);
        } else {
            if (index == 0) {
                int resID = getResources().getIdentifier(game.getGameChosenCardDesign(), "drawable", getPackageName());
                ivCard.setImageResource(resID);
                hiddenCard = ivCard;
            }
            if (index > 0) {
                setCardMargin(ivCard);
            }
            checkAceException(card, false);
            containerDealerCards.addView(ivCard);
        }
    }

    private void setCardMargin(ImageView ivCard) {
        ViewGroup.MarginLayoutParams marginParams = new ViewGroup.MarginLayoutParams(ivCard.getLayoutParams());
        marginParams.setMargins((int)getResources().getDimension(R.dimen.card_margin), 0, 0, 0);
        RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(marginParams);
        ivCard.setLayoutParams(layoutParams);
    }

    private void createAlertDialog(int messageType) {
        FragmentManager manager = getSupportFragmentManager();
        MessageFragment dialog;
        switch (messageType) {
            case -1: case 1:
                dialog = new MessageFragment(messageType, game.getUserBetAmount(), getIntent());
                dialog.show(manager, getString(R.string.message_fragment_title));
                break;
            case 0:
                dialog = new MessageFragment(messageType, getIntent());
                dialog.show(manager, getString(R.string.message_fragment_title));
                break;
        }
    }

    private double fetchChipValue(int index) {
        switch (index) {
            case 0:
                return 10.0;
            case 1:
                return 25.0;
            case 2:
                return 50.0;
            case 3:
                return 100.0;
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
        return new Intent(context, GameActivity.class);
    }
}