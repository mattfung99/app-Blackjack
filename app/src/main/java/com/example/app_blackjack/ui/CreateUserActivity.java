package com.example.app_blackjack.ui;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import com.example.app_blackjack.R;
import java.util.Objects;

public class CreateUserActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_user);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
    }

    public static Intent makeIntent(Context context) {
        return new Intent(context, CreateUserActivity.class);
    }
}