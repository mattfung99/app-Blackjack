package com.example.app_blackjack.prototype;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import com.example.app_blackjack.R;
import com.example.app_blackjack.model.Card;
import com.example.app_blackjack.model.Deck;
import java.util.Objects;

public class DeckTesterActivity extends AppCompatActivity {
    private Deck deck;
    private Card cardDrawn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_deck_tester);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        deck = new Deck();
        deck.createDeck();
//        deck.shuffleDeck();
        System.out.println(deck);
        setupDrawBtn();
    }

    private void setupDrawBtn() {
        TextView displayCardName = (TextView)findViewById(R.id.textDisplayCardName);
        ImageView displayCard = (ImageView)findViewById(R.id.imageDisplayCard);
        Button btnDrawCard = (Button)findViewById(R.id.btnTestDrawCard);
        btnDrawCard.setOnClickListener(v -> {
            cardDrawn = deck.drawCard();
            displayCardName.setText(getString(R.string.test_display_card_name, cardDrawn.getCardID()));
            int resID = getResources().getIdentifier(cardDrawn.getCardID() , "drawable", getPackageName());
            displayCard.setImageResource(resID);
            System.out.println(deck.getUsedDeck().get(deck.getNumCardsUsed() - 1).getCardID());
            ObjectAnimator animation = ObjectAnimator.ofFloat(displayCard, "x", -30f);
            animation.setDuration(1000);
            AnimatorSet animatorSet = new AnimatorSet();
            animatorSet.playTogether(animation);
            animatorSet.start();
        });
    }

    public static Intent makeIntent(Context context) {
        return new Intent(context, DeckTesterActivity.class);
    }
}