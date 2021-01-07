package com.example.app_blackjack.ui.activities;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import com.example.app_blackjack.R;
import com.example.app_blackjack.model.DataHandler;
import com.example.app_blackjack.model.User;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Objects;

public class ViewProfileActivity extends AppCompatActivity {
    // Reference the singleton instance
    private final DataHandler dHandler = DataHandler.getInstance();
    private User user;

    // UI Widgets
    private TextView textMostMoneyWon;
    private TextView textMostMoneyLost;
    private TextView textNumGamesWon;
    private TextView textNumGamesLost;
    private TextView textNumGamesForfeited;
    private TextView textNumTimesBankrupted;
    private TextView textMostMoneyWonAllTime;
    private TextView textMostMoneyLostAllTime;
    private TextView textDisplayUsername;
    private TextView textDisplayBalance;
    private ImageView displayImageUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_profile);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        user = dHandler.getUser();
        setupWidgets();
        displayWidgets();
        displayUserImage();
    }

    private void setupWidgets() {
        textMostMoneyWon = (TextView)findViewById(R.id.text_display_most_money_won);
        textMostMoneyLost = (TextView)findViewById(R.id.text_display_most_money_lost);
        textNumGamesWon = (TextView)findViewById(R.id.text_display_games_won);
        textNumGamesLost = (TextView)findViewById(R.id.text_display_games_lost);
        textNumGamesForfeited = (TextView)findViewById(R.id.text_display_games_forfeited);
        textNumTimesBankrupted = (TextView)findViewById(R.id.text_display_bankrupts);
        textMostMoneyWonAllTime = (TextView)findViewById(R.id.text_display_all_time_most_money_won);
        textMostMoneyLostAllTime = (TextView)findViewById(R.id.text_display_all_time_most_money_lost);
        textDisplayUsername = (TextView)findViewById(R.id.text_display_username);
        textDisplayBalance = (TextView)findViewById(R.id.text_display_balance);
        displayImageUser = (ImageView)findViewById(R.id.user_display_profile_picture);
    }

    private void displayWidgets() {
        Double valueZero = 0.0;
        textMostMoneyWon.setText(getString(R.string.display_most_money_won, user.getUserMostMoneyWon()));
        textMostMoneyLost.setText(getString(R.string.display_most_money_lost, user.getUserMostMoneyLost()));
        textNumGamesWon.setText(getString(R.string.display_games_won, user.getGamesWon()));
        textNumGamesLost.setText(getString(R.string.display_games_lost, user.getGamesLost()));
        textNumGamesForfeited.setText(getString(R.string.display_games_forfeited, user.getGamesForfeited()));
        textNumTimesBankrupted.setText(getString(R.string.display_bankrupts, user.getNumTimesBankrupted()));
        if (valueZero.equals(dHandler.getMostMoneyWon())) {
            textMostMoneyWonAllTime.setText(getString(R.string.display_most_money_won_all_time_default));
        } else {
            textMostMoneyWonAllTime.setText(getString(R.string.display_most_money_won_all_time, dHandler.getMostMoneyWon(), dHandler.getUserMostMoneyWon()));
        }
        if (valueZero.equals(dHandler.getMostMoneyLost())) {
            textMostMoneyLostAllTime.setText(getString(R.string.display_most_money_lost_all_time_default));
        } else {
            textMostMoneyLostAllTime.setText(getString(R.string.display_most_money_lost_all_time, dHandler.getMostMoneyLost(), dHandler.getUserMostMoneyLost()));
        }
        textDisplayUsername.setText(getString(R.string.display_username, user.getUsername()));
        textDisplayBalance.setText(getString(R.string.display_balance, user.getBalance()));
    }

    private void displayUserImage() {
        // Check if child already has a profile picture set
        if (!user.getFilepath().equals(getString(R.string.empty_file_path))) {
            // Display the custom set profile picture
            displayImageUser.setImageBitmap(setUserPhoto(Uri.fromFile(new File(user.getFilepath()))));
        } else {
            // Display default profile picture
            displayImageUser.setImageResource(R.drawable.default_user);
        }
    }

    private Bitmap setUserPhoto(Uri path) {
        try {
            InputStream inputStream = getContentResolver().openInputStream(path);
            return BitmapFactory.decodeStream(inputStream);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static Intent makeIntent(Context context) {
        return new Intent(context, ViewProfileActivity.class);
    }
}