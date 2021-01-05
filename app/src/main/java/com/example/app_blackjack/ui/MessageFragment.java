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
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState)
    {
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
        } else {
            dHandler.setRandomGameStarted(false);
        }
        requireActivity().finish();
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
