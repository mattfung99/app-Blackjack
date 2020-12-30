package com.example.app_blackjack.ui;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import com.example.app_blackjack.R;
import java.util.Objects;

public class UserLoginActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_login);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        setupButtons();
    }

    private void setupButtons() {
        setupLoginButton();
        setupCreateAccountButton();
    }

    private void setupLoginButton() {

    }

    private void setupCreateAccountButton() {
        TextView textCreateAccount = (TextView) findViewById(R.id.textCreateAccount);
        textCreateAccount.setOnClickListener(v -> {
            Intent createUserIntent = CreateUserActivity.makeIntent(UserLoginActivity.this);
            createUserIntent.putExtra("edit", false);
            startActivity(createUserIntent);
        });
    }

    public static Intent makeIntent(Context context) {
        return new Intent(context, UserLoginActivity.class);
    }
}