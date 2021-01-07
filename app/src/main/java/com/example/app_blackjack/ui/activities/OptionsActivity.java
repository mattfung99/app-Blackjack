package com.example.app_blackjack.ui.activities;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.example.app_blackjack.R;
import com.example.app_blackjack.model.DataHandler;
import com.google.gson.Gson;
import java.util.Objects;

public class OptionsActivity extends AppCompatActivity {
    // Reference the singleton instance
    private final DataHandler dHandler = DataHandler.getInstance();

    // UI Widgets
    private RadioButton[] rgBtnNumDecks;
    private RadioButton[] rgBtnChosenCardDesign;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_options);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        setupWidgets();
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (!dHandler.isDataCleared()) {
            if (dHandler.isUserLoggedIn()) {
                saveUserIntoSharedPref();
            } else {
                saveSessionIntoSharedPref();
            }
            Toast.makeText(this, getString(R.string.toast_changes_saved_options), Toast.LENGTH_SHORT).show();
        }
        dHandler.setDataCleared(false);
    }

    private void setupWidgets() {
        setupCurrentOptions();
        setupRadioGroups();
        setupClearDataButton();
    }

    private void setupCurrentOptions() {
        TextView displayCurrentOptions = (TextView)findViewById(R.id.textDisplayCurrentOptions);
        if (dHandler.isUserLoggedIn()) {
            displayCurrentOptions.setText(getString(R.string.text_label_current_options, dHandler.getUser().getUsername()));
        } else {
            displayCurrentOptions.setText(getString(R.string.text_label_current_options_default));
        }
    }

    private void setupRadioGroups() {
        rgBtnNumDecks = createRadioButtons(
                3,
                true,
                getResources().getIdentifier("radioGroupNumDecks" , "id", getPackageName()),
                getResources().getIdentifier("list_num_decks", "array", getPackageName())
        );
        selectDefaultOption(true);
        rgBtnChosenCardDesign = createRadioButtons(
                5,
                false,
                getResources().getIdentifier("radioGroupCardDesign" , "id", getPackageName()),
                getResources().getIdentifier("list_card_chosen", "array", getPackageName())
        );
        selectDefaultOption(false);
    }

    private RadioButton[] createRadioButtons(int rgSize, boolean rgIndicator, int resID, int listResID) {
        RadioGroup tempRG = (RadioGroup)findViewById(resID);
        int[] listButtons = getResources().getIntArray(listResID);
        RadioButton[] tempRGBtn = new RadioButton[rgSize];
        for (int i = 0; i < rgSize; i++)
        {
            int finalI = i;
            final int numPanel = listButtons[i];
            tempRGBtn[i] = new RadioButton(this);
            setRGProperties(rgIndicator, tempRGBtn[i], numPanel, finalI);
            tempRGBtn[i].setLayoutParams(new RadioGroup.LayoutParams(RadioGroup.LayoutParams.MATCH_PARENT, RadioGroup.LayoutParams.WRAP_CONTENT));
            tempRGBtn[i].setOnClickListener(v -> rgSetOnClickListener(rgIndicator, finalI, numPanel));
            tempRG.addView(tempRGBtn[i]);
        }
        return tempRGBtn;
    }

    private void setRGProperties(boolean rgIndicator, RadioButton tempRGBtn, int numPanel, int index) {
        if (rgIndicator) {
            tempRGBtn.setText(String.valueOf(index + 1));
        } else {
            tempRGBtn.setText(setTextCardChosen(numPanel));
        }
    }

    private void rgSetOnClickListener(boolean rgIndicator, int index, final int rgIndex) {
        if (rgIndicator) {
            if (dHandler.isUserLoggedIn()) {
                dHandler.getUser().setNumDecks(index + 1);
            } else {
                dHandler.setDefaultNumDecks(index + 1);
            }
        } else {
            if (dHandler.isUserLoggedIn()) {
                dHandler.getUser().setChosenCardDesign(setTextCardChosen(rgIndex).toLowerCase());
            } else {
                dHandler.setDefaultChosenCardDesign(setTextCardChosen(rgIndex).toLowerCase());
            }
        }
    }

    private void selectDefaultOption(boolean rgBtnIndicator) {
        if (rgBtnIndicator) {
            if (dHandler.isUserLoggedIn()) {
                System.out.println(dHandler.getUser().getNumDecks());
                rgBtnNumDecks[dHandler.getUser().getNumDecks() - 1].setChecked(true);
            } else {
                System.out.println(dHandler.getDefaultNumDecks());
                rgBtnNumDecks[dHandler.getDefaultNumDecks() - 1].setChecked(true);
            }
        } else {
            if (dHandler.isUserLoggedIn()) {
                rgBtnChosenCardDesign[setTextCardChosen(dHandler.getUser().getChosenCardDesign())].setChecked(true);
            } else {
                rgBtnChosenCardDesign[setTextCardChosen(dHandler.getDefaultChosenCardDesign())].setChecked(true);
            }
        }
    }

    private void setupClearDataButton() {
        Button btnClearData = (Button)findViewById(R.id.btnClearData);
        btnClearData.setOnClickListener(v -> {
            dHandler.clearData();
            if (dHandler.isUserLoggedIn()) {
                saveUserIntoSharedPref();
                saveUserSessionIntoSharedPref(true);
                Toast.makeText(this, getString(R.string.toast_user_reset), Toast.LENGTH_SHORT).show();
            } else {
                saveSessionIntoSharedPref();
                saveUserSessionIntoSharedPref(false);
                Toast.makeText(this, getString(R.string.toast_default_reset), Toast.LENGTH_SHORT).show();
            }
            dHandler.setDataCleared(true);
            finish();
        });
    }

    private String setTextCardChosen(int rgIndex) {
        switch (rgIndex) {
            case 0:
                return "Blue";
            case 1:
                return "Gray";
            case 2:
                return "Green";
            case 3:
                return "Purple";
            case 4:
                return "Red";
            case 5:
                return "Yellow";
            default:
                return "error";
        }
    }

    private int setTextCardChosen(String currCardDesign) {
        switch (currCardDesign) {
            case "blue":
                return 0;
            case "gray":
                return 1;
            case "green":
                return 2;
            case "purple":
                return 3;
            case "red":
                return 4;
            case "yellow":
                return 5;
            default:
                return -1;
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

    private void saveUserSessionIntoSharedPref(boolean dataIndicator) {
        SharedPreferences sessionPref = getSharedPreferences("PREF_USER_SESSION", MODE_PRIVATE);
        SharedPreferences.Editor sessionEditor = sessionPref.edit();
        if (dataIndicator) {
            sessionEditor.putBoolean("userGameStarted", dHandler.isUserGameStarted());
        } else {
            sessionEditor.putBoolean("randomGameStarted", dHandler.isRandomGameStarted());
        }
        sessionEditor.apply();
    }

    private void saveSessionIntoSharedPref() {
        SharedPreferences sessionPref = getSharedPreferences("PREF_USER_SESSION", MODE_PRIVATE);
        SharedPreferences.Editor sessionEditor = sessionPref.edit();
        sessionEditor.putInt("defaultSettingsNumDecks", dHandler.getDefaultNumDecks());
        sessionEditor.putString("defaultSettingsCardDesign", dHandler.getDefaultChosenCardDesign());
        sessionEditor = putDouble(sessionEditor, "defaultBalance", dHandler.getDefaultBalance());
        sessionEditor.apply();
    }

    private SharedPreferences.Editor putDouble(final SharedPreferences.Editor edit, final String key, final double value) {
        return edit.putLong(key, Double.doubleToRawLongBits(value));
    }

    public static Intent makeIntent(Context context) {
        return new Intent(context, OptionsActivity.class);
    }
}