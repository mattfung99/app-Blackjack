package com.example.app_blackjack.ui.fragments;

import android.content.SharedPreferences;
import android.content.res.ColorStateList;
import android.widget.Button;
import androidx.core.content.ContextCompat;
import com.example.app_blackjack.R;
import com.example.app_blackjack.model.DataHandler;
import com.google.gson.Gson;

import static android.content.Context.MODE_PRIVATE;

public class RestartFragment extends DefaultFragment {
    // Reference the singleton instance
    private final DataHandler dHandler = DataHandler.getInstance();

    public RestartFragment(String setTitle, String setMessage) {
        super(setTitle, setMessage);
    }

    @Override
    public void doAction() {
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
    }

    private void setMenuButtonsAppearance() {
        if (dHandler.isUserLoggedIn()) {
            setMenuButtonsAppearance(dHandler.isUserGameStarted());
        } else {
            setMenuButtonsAppearance(dHandler.isRandomGameStarted());
        }
    }

    private void setMenuButtonsAppearance(boolean isGameStarted) {
        Button btnMenuStart = (Button) requireActivity().findViewById(R.id.btnMenuStart);
        Button btnMenuRestart = (Button) requireActivity().findViewById(R.id.btnMenuRestart);
        if (isGameStarted) {
            btnMenuStart.setText(getString(R.string.menu_continue));
            btnMenuRestart.setTextColor(ColorStateList.valueOf(ContextCompat.getColor(requireContext(), R.color.default_text_color)));
            btnMenuRestart.setBackgroundTintList(ColorStateList.valueOf(ContextCompat.getColor(requireContext(), R.color.default_background_color)));
        } else {
            btnMenuStart.setText(getString(R.string.menu_start));
            btnMenuRestart.setTextColor(ColorStateList.valueOf(ContextCompat.getColor(requireContext(), R.color.faded_text_color)));
            btnMenuRestart.setBackgroundTintList(ColorStateList.valueOf(ContextCompat.getColor(requireContext(), R.color.faded_background_color)));
        }
        btnMenuRestart.setEnabled(isGameStarted);
    }

    private void saveUserSessionIntoSharedPref() {
        SharedPreferences sessionPref = requireActivity().getSharedPreferences("PREF_USER_SESSION", MODE_PRIVATE);
        SharedPreferences.Editor sessionEditor = sessionPref.edit();
        if (dHandler.isUserLoggedIn()) {
            sessionEditor.putBoolean("userGameStarted", dHandler.isUserGameStarted());
        } else {
            sessionEditor.putBoolean("randomGameStarted", dHandler.isRandomGameStarted());
        }
        sessionEditor.apply();
    }

    private void saveUserIntoSharedPref() {
        SharedPreferences userPref = requireActivity().getSharedPreferences("PREF_USERS", MODE_PRIVATE);
        SharedPreferences.Editor userEditor = userPref.edit();
        Gson gson = new Gson();
        String json = gson.toJson(dHandler.getUser());
        userEditor.putString(dHandler.getUser().getUsername(), json);
        userEditor.apply();
    }
}
