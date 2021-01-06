package com.example.app_blackjack.ui;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatDialogFragment;
import com.example.app_blackjack.R;
import com.example.app_blackjack.model.DataHandler;
import org.jetbrains.annotations.NotNull;

public class MessageFragment extends AppCompatDialogFragment
{
    // Reference the singleton instance
    private final DataHandler dHandler = DataHandler.getInstance();

    // Globals
    private final int messageType;
    private final double amount;
    private final Intent intent;

    public MessageFragment(int messageType, Intent intent) {
        this.messageType = messageType;
        this.amount = 0.0;
        this.intent = intent;
    }

    public MessageFragment(int messageType, double amount, Intent intent) {
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
        if (dHandler.isUserLoggedIn()) {
            dHandler.setUserGameStarted(false);
            setStatistics(messageType);
        } else {
            dHandler.setRandomGameStarted(false);
            if (dHandler.getDefaultBalance() < 10.0) {
                dHandler.setDefaultBalance(2000.0);
            }
        }
        requireActivity().finish();
    }

    private void setStatistics(int messageType) {
        switch (messageType) {
            case -1:
                dHandler.getUser().setGamesLost(dHandler.getUser().getGamesLost() + 1);
                if (amount > dHandler.getUser().getUserMostMoneyLost()) {
                    dHandler.getUser().setUserMostMoneyLost(amount);
                }
                if (dHandler.getGame().getUserBetAmount() > dHandler.getMostMoneyLost()) {
                    dHandler.setMostMoneyLost(amount);
                    dHandler.setUserMostMoneyLost(dHandler.getUser().getUsername());
                }
                if (dHandler.getUser().getBalance() < 10.0) {
                    dHandler.getUser().setNumTimesBankrupted(dHandler.getUser().getNumTimesBankrupted() + 1);
                    dHandler.getUser().setBalance(5000.0);
                }
                break;
            case 0:
                if (dHandler.isUserLoggedIn()) {
                    dHandler.getUser().setBalance(dHandler.getUser().getBalance() + dHandler.getGame().getUserBetAmount());
                } else {
                    dHandler.setDefaultBalance(dHandler.getDefaultBalance() + dHandler.getGame().getUserBetAmount());
                }
                break;
            case 1:
                dHandler.getUser().setGamesWon(dHandler.getUser().getGamesWon() + 1);
                if (amount > dHandler.getUser().getUserMostMoneyWon()) {
                    dHandler.getUser().setUserMostMoneyWon(amount);
                }
                if (amount > dHandler.getMostMoneyWon()) {
                    dHandler.setMostMoneyWon(amount);
                    dHandler.setUserMostMoneyWon(dHandler.getUser().getUsername());
                }
                if (dHandler.isUserLoggedIn()) {
                    dHandler.getUser().setBalance(dHandler.getUser().getBalance() + amount);
                } else {
                    dHandler.setDefaultBalance(dHandler.getDefaultBalance() + amount);
                }
                break;
        }
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
}
