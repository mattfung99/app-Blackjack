package com.example.app_blackjack.ui.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.method.LinkMovementMethod;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import com.example.app_blackjack.R;
import java.util.Objects;

public class HelpActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_help);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        createHyperLinks();
    }

    private void createHyperLinks() {
        TextView textStartIcon = (TextView)findViewById(R.id.textStartIcon);
        textStartIcon.setMovementMethod(LinkMovementMethod.getInstance());

        TextView textOptionsIcon = (TextView)findViewById(R.id.textOptionsIcon);
        textOptionsIcon.setMovementMethod(LinkMovementMethod.getInstance());

        TextView textHelpIcon = (TextView)findViewById(R.id.textHelpIcon);
        textHelpIcon.setMovementMethod(LinkMovementMethod.getInstance());

        TextView textCardImages = (TextView)findViewById(R.id.textCardImages);
        textCardImages.setMovementMethod(LinkMovementMethod.getInstance());

        TextView textChipsImages = (TextView)findViewById(R.id.textChipsImages);
        textChipsImages.setMovementMethod(LinkMovementMethod.getInstance());

        TextView textDeveloper = (TextView)findViewById(R.id.textDeveloper);
        textDeveloper.setMovementMethod(LinkMovementMethod.getInstance());
    }

    public static Intent makeIntent(Context context) {
        return new Intent(context, HelpActivity.class);
    }
}