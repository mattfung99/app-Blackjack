package com.example.app_blackjack.ui;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatDialogFragment;
import com.example.app_blackjack.R;
import com.example.app_blackjack.model.DataHandler;
import com.google.gson.Gson;

import org.jetbrains.annotations.NotNull;
import static android.content.Context.MODE_PRIVATE;

public class ResultFragment extends AppCompatDialogFragment
{
    // Reference the singleton instance
    private final DataHandler dHandler = DataHandler.getInstance();

    // Globals
    private final int messageType;
    private final double amount;
    private final Intent intent;

    public ResultFragment(int messageType, Intent intent) {
        this.messageType = messageType;
        this.amount = 0.0;
        this.intent = intent;
    }

    public ResultFragment(int messageType, double amount, Intent intent) {
        this.messageType = messageType;
        this.amount = amount;
        this.intent = intent;
    }

    @NotNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        // Create the view to show
        View v = LayoutInflater.from(getActivity()).inflate(R.layout.fragment_message_fragment, null);

        // Build the alert Dialog
        return new AlertDialog.Builder(getActivity())
                .setTitle(getString(R.string.message_fragment_title))
                .setMessage(fetchMessageType())
                .setPositiveButton(getString(R.string.btn_positive), (dialog, which) -> resetGame())
                .setNegativeButton(getString(R.string.btn_negative), (dialog, which) -> {
                    resetGame();
                    startActivity(intent);
                })
                .setView(v)
                .create();
    }

    private void resetGame() {
        setStatistics(messageType);
        if (dHandler.isUserLoggedIn()) {
            dHandler.setUserGameStarted(false);
            dHandler.getUser().setThisUserGameStarted(false);
            saveUserIntoSharedPref();
        } else {
            dHandler.setRandomGameStarted(false);
        }
        saveUserSessionIntoSharedPref();
        requireActivity().finish();
    }

    private void setStatistics(int messageType) {
        switch (messageType) {
            case -1:
                if (dHandler.isUserLoggedIn()) {
                    dHandler.getUser().setGamesLost(dHandler.getUser().getGamesLost() + 1);
                    if (amount > dHandler.getUser().getUserMostMoneyLost()) {
                        dHandler.getUser().setUserMostMoneyLost(amount);
                        saveStatsIntoSharedPref();
                    }
                    if (dHandler.getGame().getUserBetAmount() > dHandler.getMostMoneyLost()) {
                        dHandler.setMostMoneyLost(amount);
                        dHandler.setUserMostMoneyLost(dHandler.getUser().getUsername());
                        saveStatsIntoSharedPref();
                    }
                    if (dHandler.getUser().getBalance() < 10.0) {
                        dHandler.getUser().setNumTimesBankrupted(dHandler.getUser().getNumTimesBankrupted() + 1);
                        dHandler.getUser().setBalance(5000.0);
                    }
                    saveUserIntoSharedPref();
                } else {
                    if (dHandler.getDefaultBalance() < 10.0) {
                        dHandler.setDefaultBalance(2000.0);
                        saveSessionOptionsIntoSharedPref();
                    }
                }
                break;
            case 0:
                if (dHandler.isUserLoggedIn()) {
                    dHandler.getUser().setBalance(dHandler.getUser().getBalance() + dHandler.getGame().getUserBetAmount());
                    saveUserIntoSharedPref();
                } else {
                    dHandler.setDefaultBalance(dHandler.getDefaultBalance() + dHandler.getGame().getUserBetAmount());
                    saveSessionOptionsIntoSharedPref();
                }
                break;
            case 1:
                if (dHandler.isUserLoggedIn()) {
                    dHandler.getUser().setGamesWon(dHandler.getUser().getGamesWon() + 1);
                    if (amount > dHandler.getUser().getUserMostMoneyWon()) {
                        dHandler.getUser().setUserMostMoneyWon(amount);
                        saveStatsIntoSharedPref();
                    }
                    if (amount > dHandler.getMostMoneyWon()) {
                        dHandler.setMostMoneyWon(amount);
                        dHandler.setUserMostMoneyWon(dHandler.getUser().getUsername());
                        saveStatsIntoSharedPref();
                    }
                    dHandler.getUser().setBalance(dHandler.getUser().getBalance() + amount);
                    saveUserIntoSharedPref();
                } else {
                    dHandler.setDefaultBalance(dHandler.getDefaultBalance() + amount);
                    saveSessionOptionsIntoSharedPref();
                }
                break;
        }
    }

    private void saveStatsIntoSharedPref() {
        SharedPreferences statsPref = getActivity().getSharedPreferences("PREF_STATISTICS", MODE_PRIVATE);
        SharedPreferences.Editor statsEditor = statsPref.edit();
        statsEditor = putDouble(statsEditor, "allTimeMostMoneyWon", dHandler.getMostMoneyWon());
        statsEditor.putString("allTimeMostMoneyWonUser", dHandler.getUserMostMoneyWon());
        statsEditor = putDouble(statsEditor, "allTimeMostMoneyLost", dHandler.getMostMoneyLost());
        statsEditor.putString("allTimeMostMoneyLostUser", dHandler.getUserMostMoneyLost());
    }

    private String fetchMessageType() {
        switch (messageType) {
            case -1:
                return getString(R.string.result_loss, amount);
            case 0:
                return getString(R.string.result_push);
            case 1:
                return getString(R.string.result_win, amount);
            default:
                return getString(R.string.empty_string);
        }
    }

    private void saveSessionOptionsIntoSharedPref() {
        SharedPreferences sessionPref = getActivity().getSharedPreferences("PREF_USER_SESSION", MODE_PRIVATE);
        SharedPreferences.Editor sessionEditor = sessionPref.edit();
        sessionEditor = putDouble(sessionEditor, "defaultBalance", dHandler.getDefaultBalance());
        sessionEditor.apply();
    }

    private SharedPreferences.Editor putDouble(final SharedPreferences.Editor edit, final String key, final double value) {
        return edit.putLong(key, Double.doubleToRawLongBits(value));
    }

    private void saveUserSessionIntoSharedPref() {
        SharedPreferences sessionPref = getActivity().getSharedPreferences("PREF_USER_SESSION", MODE_PRIVATE);
        SharedPreferences.Editor sessionEditor = sessionPref.edit();
        if (dHandler.isUserLoggedIn()) {
            sessionEditor.putBoolean("userGameStarted", dHandler.isUserGameStarted());
        } else {
            sessionEditor.putBoolean("randomGameStarted", dHandler.isRandomGameStarted());
        }
        sessionEditor.apply();
    }

    private void saveUserIntoSharedPref() {
        SharedPreferences userPref = getActivity().getSharedPreferences("PREF_USERS", MODE_PRIVATE);
        SharedPreferences.Editor userEditor = userPref.edit();
        Gson gson = new Gson();
        String json = gson.toJson(dHandler.getUser());
        userEditor.putString(dHandler.getUser().getUsername(), json);
        userEditor.apply();
    }
}
