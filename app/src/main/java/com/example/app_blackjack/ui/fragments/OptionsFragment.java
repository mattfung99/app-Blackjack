package com.example.app_blackjack.ui.fragments;

import android.content.SharedPreferences;
import android.widget.Toast;
import com.example.app_blackjack.R;
import com.example.app_blackjack.model.DataHandler;
import com.google.gson.Gson;

import static android.content.Context.MODE_PRIVATE;

public class OptionsFragment extends DefaultFragment {
    // Reference the singleton instance
    private final DataHandler dHandler = DataHandler.getInstance();

    public OptionsFragment(String setTitle, String setMessage) {
        super(setTitle, setMessage);
    }

    @Override
    public void doAction() {
        dHandler.clearData();
        if (dHandler.isUserLoggedIn()) {
            saveUserIntoSharedPref();
            saveUserSessionIntoSharedPref(true);
            Toast.makeText(getContext(), getString(R.string.toast_user_reset), Toast.LENGTH_SHORT).show();
        } else {
            saveSessionIntoSharedPref();
            saveUserSessionIntoSharedPref(false);
            Toast.makeText(getContext(), getString(R.string.toast_default_reset), Toast.LENGTH_SHORT).show();
        }
        dHandler.setDataCleared(true);
        requireActivity().finish();
    }

    private void saveUserIntoSharedPref() {
        SharedPreferences userPref = requireActivity().getSharedPreferences("PREF_USERS", MODE_PRIVATE);
        SharedPreferences.Editor userEditor = userPref.edit();
        Gson gson = new Gson();
        String json = gson.toJson(dHandler.getUser());
        userEditor.putString(dHandler.getUser().getUsername(), json);
        userEditor.apply();
    }

    private void saveUserSessionIntoSharedPref(boolean dataIndicator) {
        SharedPreferences sessionPref = requireActivity().getSharedPreferences("PREF_USER_SESSION", MODE_PRIVATE);
        SharedPreferences.Editor sessionEditor = sessionPref.edit();
        if (dataIndicator) {
            sessionEditor.putBoolean("userGameStarted", dHandler.isUserGameStarted());
        } else {
            sessionEditor.putBoolean("randomGameStarted", dHandler.isRandomGameStarted());
        }
        sessionEditor.apply();
    }

    private void saveSessionIntoSharedPref() {
        SharedPreferences sessionPref = requireActivity().getSharedPreferences("PREF_USER_SESSION", MODE_PRIVATE);
        SharedPreferences.Editor sessionEditor = sessionPref.edit();
        sessionEditor.putInt("defaultSettingsNumDecks", dHandler.getDefaultNumDecks());
        sessionEditor.putString("defaultSettingsCardDesign", dHandler.getDefaultChosenCardDesign());
        sessionEditor = putDouble(sessionEditor, "defaultBalance", dHandler.getDefaultBalance());
        sessionEditor.apply();
    }

    private SharedPreferences.Editor putDouble(final SharedPreferences.Editor edit, final String key, final double value) {
        return edit.putLong(key, Double.doubleToRawLongBits(value));
    }
}
