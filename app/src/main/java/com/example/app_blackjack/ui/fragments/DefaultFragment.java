package com.example.app_blackjack.ui.fragments;

import android.app.AlertDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatDialogFragment;
import com.example.app_blackjack.R;
import org.jetbrains.annotations.NotNull;

abstract class DefaultFragment extends AppCompatDialogFragment
{
    private final String setTitle;
    private final String setMessage;

    public DefaultFragment(String setTitle, String setMessage) {
        this.setTitle = setTitle;
        this.setMessage = setMessage;
    }

    @NotNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        // Create the view to show
        View v = LayoutInflater.from(getActivity()).inflate(R.layout.fragment_message_fragment, null);

        // Build the alert Dialog
        return new AlertDialog.Builder(getActivity())
                .setTitle(setTitle)
                .setMessage(setMessage)
                .setView(v)
                .setPositiveButton(android.R.string.ok, (dialog, which) -> doAction())
                .setNegativeButton(android.R.string.cancel, (dialog, which) -> dialog.cancel())
                .create();
    }

    public abstract void doAction();
}
