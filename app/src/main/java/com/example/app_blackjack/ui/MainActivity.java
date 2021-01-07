package com.example.app_blackjack.ui;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.ColorStateList;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.Toast;
import com.example.app_blackjack.R;
import com.example.app_blackjack.model.DataHandler;
import com.example.app_blackjack.model.Game;
import com.example.app_blackjack.model.User;
import com.google.gson.Gson;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.InputStream;

public class MainActivity extends AppCompatActivity {
    // Reference the singleton instance
    private final DataHandler dHandler = DataHandler.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        System.out.println("Is created");
        setupButtons();
        if (!dHandler.isUserSessionLoadedFromSharedPref()) {
            retrieveUserSessionFromSharedPref();
            retrieveStatsFromSharedPref();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        setMenuButtonsAppearance();
        invalidateOptionsMenu();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        updateOptionsMenu(menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        menu.clear();
        updateOptionsMenu(menu);
        return super.onPrepareOptionsMenu(menu);
    }

    private void updateOptionsMenu(Menu menu) {
        if (dHandler.isUserLoggedIn()) {
            if (!dHandler.isUserLoadedFromSharedPref()) {
                retrieveUserFromSharedPref();
                if (dHandler.isUserGameStarted()) {
                    retrieveGameFromSharedPref(dHandler.isUserLoggedIn());
                }
            }
            getMenuInflater().inflate(R.menu.logged_in_main, menu);
            displayProfilePicture(menu);
            menu.findItem(R.id.user_profile).setTitle(dHandler.getUser().getUsername());
        } else {
            if (!dHandler.isNonUserLoadedFromSharedPref()) {
                if (dHandler.isRandomGameStarted()) {
                    retrieveGameFromSharedPref(dHandler.isUserLoggedIn());
                }
                dHandler.setNonUserLoadedFromSharedPref(true);
            }
            retrieveSessionOptionsIntoSharedPref();
            getMenuInflater().inflate(R.menu.not_logged_in_main, menu);
        }
    }

    private void displayProfilePicture(Menu menu) {
        // Check if child already has a profile picture set
        if (!dHandler.getUser().getFilepath().equals(getString(R.string.empty_file_path))) {
            // Display the custom set profile picture
            menu.findItem(R.id.user_picture).setIcon(setUserPhoto(Uri.fromFile(new File(dHandler.getUser().getFilepath()))));
        } else {
            // Display default profile picture
            menu.findItem(R.id.user_picture).setIcon(R.drawable.default_user);
        }
    }

    private Drawable setUserPhoto(Uri path) {
        try {
            InputStream inputStream = getContentResolver().openInputStream(path);
            return Drawable.createFromStream(inputStream, path.toString());
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return dHandler.isUserLoggedIn() ? fetchLoggedInAction(item) : fetchNotLoggedInAction(item);
    }

    private void setupButtons() {
        setupStartButton();
        setupRestartButton();
        setupOptionsButton();
        setupHelpButton();
    }

    private void setupStartButton() {
        Button btnStart = (Button)findViewById(R.id.btnMenuStart);
        btnStart.setOnClickListener(v -> {
            Intent intent = GameActivity.makeIntent(MainActivity.this);
            startActivity(intent);
        });
    }

    private void setupRestartButton() {
        Button btnRestart = (Button)findViewById(R.id.btnMenuRestart);
        btnRestart.setOnClickListener(v -> {
            if (dHandler.isUserLoggedIn()) {
                dHandler.setUserGameStarted(false);
                dHandler.getUser().setThisUserGameStarted(false);
                dHandler.getUser().setGamesForfeited(dHandler.getUser().getGamesForfeited() + 1);
                saveUserIntoSharedPref();
            } else {
                dHandler.setRandomGameStarted(false);
            }
            saveUserSessionIntoSharedPref();
            setMenuButtonsAppearance();
        });
    }

    private void setupOptionsButton() {
        Button btnOptions = (Button)findViewById(R.id.btnMenuOptions);
        btnOptions.setOnClickListener(v -> {
            Intent optionsIntent = OptionsActivity.makeIntent(MainActivity.this);
            startActivity(optionsIntent);
        });
    }

    private void setupHelpButton() {
        Button btnHelp = (Button)findViewById(R.id.btnMenuHelp);
        btnHelp.setOnClickListener(v -> {
        });
    }

    @SuppressLint("NonConstantResourceId")
    private boolean fetchNotLoggedInAction(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.user_profile: case R.id.action_log_in:
                Intent loginIntent = UserLoginActivity.makeIntent(MainActivity.this);
                startActivity(loginIntent);
                break;
            case R.id.action_create_user:
                Intent createUserIntent = CreateUserActivity.makeIntent(MainActivity.this);
                createUserIntent.putExtra("edit", false);
                startActivity(createUserIntent);
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @SuppressLint("NonConstantResourceId")
    private boolean fetchLoggedInAction(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_edit_user:
                Intent editUserIntent = CreateUserActivity.makeIntent(MainActivity.this);
                editUserIntent.putExtra("edit", true);
                startActivity(editUserIntent);
                break;
            case R.id.action_view_profile:
                Intent viewProfileIntent = ViewProfileActivity.makeIntent(MainActivity.this);
                startActivity(viewProfileIntent);
                break;
            case R.id.action_sign_out:
                dHandler.setUser(new User("error", "error404", ""));
                dHandler.setUserLoggedIn(false);
                retrieveSessionOptionsIntoSharedPref();
                if (dHandler.isRandomGameStarted()) {
                    retrieveGameFromSharedPref(dHandler.isUserLoggedIn());
                }
                saveLoggedOutSessionIntoSharedPref(false);
                Toast.makeText(this, getString(R.string.toast_logout_successful), Toast.LENGTH_SHORT).show();
                finish();
                startActivity(getIntent());
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void setMenuButtonsAppearance() {
        if (dHandler.isUserLoggedIn()) {
            setMenuButtonsAppearance(dHandler.isUserGameStarted());
        } else {
            setMenuButtonsAppearance(dHandler.isRandomGameStarted());
        }
    }

    private void setMenuButtonsAppearance(boolean isGameStarted) {
        Button btnMenuStart = (Button)findViewById(R.id.btnMenuStart);
        Button btnMenuRestart = (Button)findViewById(R.id.btnMenuRestart);
        if (isGameStarted) {
            btnMenuStart.setText(getString(R.string.menu_continue));
            btnMenuRestart.setTextColor(ColorStateList.valueOf(ContextCompat.getColor(this, R.color.default_text_color)));
            btnMenuRestart.setBackgroundTintList(ColorStateList.valueOf(ContextCompat.getColor(this, R.color.default_background_color)));
        } else {
            btnMenuStart.setText(getString(R.string.menu_start));
            btnMenuRestart.setTextColor(ColorStateList.valueOf(ContextCompat.getColor(this, R.color.faded_text_color)));
            btnMenuRestart.setBackgroundTintList(ColorStateList.valueOf(ContextCompat.getColor(this, R.color.faded_background_color)));
        }
        btnMenuRestart.setEnabled(isGameStarted);
    }

    private void retrieveGameFromSharedPref(boolean userLoggedIn) {
        SharedPreferences sessionPref = getSharedPreferences("PREF_USER_SESSION", MODE_PRIVATE);
        SharedPreferences gamePref = getSharedPreferences("PREF_GAMES", MODE_PRIVATE);
        Gson gson = new Gson();
        if (userLoggedIn) {
            String json = gamePref.getString(dHandler.getUser().getUsername() + "Game", "error");
            Game currGame = gson.fromJson(json, Game.class);
            dHandler.setGame(currGame);
        } else {
            String json = gamePref.getString("randomGame", "error");
            Game currGame = gson.fromJson(json, Game.class);
            dHandler.setGame(currGame);
        }
    }

    private void retrieveUserFromSharedPref() {
        SharedPreferences sessionPref = getSharedPreferences("PREF_USER_SESSION", MODE_PRIVATE);
        SharedPreferences userPref = getSharedPreferences("PREF_USERS", MODE_PRIVATE);
        Gson gson = new Gson();
        String json = userPref.getString(sessionPref.getString("userSessionKey", "error"), "error");
        User currUser = gson.fromJson(json, User.class);
        dHandler.setUser(currUser);
        dHandler.setUserLoadedFromSharedPref(true);
    }

    private void retrieveUserSessionFromSharedPref() {
        SharedPreferences sessionPref = getSharedPreferences("PREF_USER_SESSION", MODE_PRIVATE);
        dHandler.setUserLoggedIn(sessionPref.getBoolean("userLoggedIn", false));
        dHandler.setUserGameStarted(sessionPref.getBoolean("userGameStarted", false));
        dHandler.setRandomGameStarted(sessionPref.getBoolean("randomGameStarted", false));
        dHandler.setUserSessionLoadedFromSharedPref(true);
    }

    private void retrieveSessionOptionsIntoSharedPref() {
        SharedPreferences sessionPref = getSharedPreferences("PREF_USER_SESSION", MODE_PRIVATE);
        dHandler.setDefaultNumDecks(sessionPref.getInt("defaultSettingsNumDecks", 1));
        dHandler.setDefaultChosenCardDesign(sessionPref.getString("defaultSettingsCardDesign", "blue"));
        dHandler.setDefaultBalance(getDouble(sessionPref, "defaultBalance", 2000.0));
    }

    private void retrieveStatsFromSharedPref() {
        SharedPreferences statsPref = getSharedPreferences("PREF_STATISTICS", MODE_PRIVATE);
        dHandler.setMostMoneyWon(getDouble(statsPref, "allTimeMostMoneyWon", 0.0));
        dHandler.setUserMostMoneyWon(statsPref.getString("allTimeMostMoneyWonUser", "error"));
        dHandler.setUserMostMoneyLost(statsPref.getString("allTimeMostMoneyLostUser", "error"));
        dHandler.setMostMoneyLost(getDouble(statsPref, "allTimeMostMoneyLost", 0.0));
    }

    private double getDouble(final SharedPreferences statsPrefs, final String key, final double defaultValue) {
        return Double.longBitsToDouble(statsPrefs.getLong(key, Double.doubleToLongBits(defaultValue)));
    }

    private void saveLoggedOutSessionIntoSharedPref(boolean userNotLoggedOut) {
        SharedPreferences sessionPref = getSharedPreferences("PREF_USER_SESSION", MODE_PRIVATE);
        SharedPreferences.Editor sessionEditor = sessionPref.edit();
        sessionEditor.putString("userSessionKey", "error");
        sessionEditor.putBoolean("userLoggedIn", userNotLoggedOut);
        sessionEditor.apply();
    }

    private void saveUserSessionIntoSharedPref() {
        SharedPreferences sessionPref = getSharedPreferences("PREF_USER_SESSION", MODE_PRIVATE);
        SharedPreferences.Editor sessionEditor = sessionPref.edit();
        if (dHandler.isUserLoggedIn()) {
            sessionEditor.putBoolean("userGameStarted", dHandler.isUserGameStarted());
        } else {
            sessionEditor.putBoolean("randomGameStarted", dHandler.isRandomGameStarted());
        }
        sessionEditor.apply();
    }

    private void saveUserIntoSharedPref() {
        SharedPreferences userPref = getSharedPreferences("PREF_USERS", MODE_PRIVATE);
        SharedPreferences.Editor userEditor = userPref.edit();
        Gson gson = new Gson();
        String json = gson.toJson(dHandler.getUser());
        userEditor.putString(dHandler.getUser().getUsername(), json);
        userEditor.apply();
    }
}