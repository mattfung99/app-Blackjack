package com.example.app_blackjack.ui;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatDialogFragment;
import com.example.app_blackjack.R;
import com.example.app_blackjack.model.DataHandler;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

public class MessageFragment extends AppCompatDialogFragment
{
    // Reference the singleton instance
    private final DataHandler dHandler = DataHandler.getInstance();

    // Globals
    private final int messageType;
    private final double amount;

    public MessageFragment(int messageType) {
        this.messageType = messageType;
        this.amount = 0.0;
    }

    public MessageFragment(int messageType, double amount) {
        this.messageType = messageType;
        this.amount = amount;
    }

    @NotNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState)
    {
        // Create the view to show
        View v = LayoutInflater.from(getActivity()).inflate(R.layout.fragment_message_fragment, null);

        // Build the alert Dialog
        return new AlertDialog.Builder(getActivity())
                .setTitle("Game Over")
                .setMessage(fetchMessageType())
                .setPositiveButton("Return to menu", (dialog, which) -> {
                    if (dHandler.isUserLoggedIn()) {
                        dHandler.setUserGameStarted(false);
                    } else {
                            dHandler.setRandomGameStarted(false);
                    }
                    requireActivity().finish();
                })
                .setNegativeButton("Restart game", (dialog, which) -> {
                    dHandler.getGame().setDialogCancelled(true);
                    dialog.cancel();
                })
                .setView(v)
                .create();
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
