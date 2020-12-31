package com.example.app_blackjack.ui;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.example.app_blackjack.R;
import com.example.app_blackjack.model.DataHandler;
import com.example.app_blackjack.model.User;
import com.google.gson.Gson;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.Objects;

public class UserLoginActivity extends AppCompatActivity {
    // Reference the singleton instance
    private final DataHandler dHandler = DataHandler.getInstance();

    // UI Widgets
    private EditText eTextUsername;
    private EditText eTextPassword;
    private TextView textInvalidCredentials;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_login);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        setupWidgets();
    }

    private void setupWidgets() {
        eTextUsername = (EditText)findViewById(R.id.editTextUsername);
        eTextPassword = (EditText)findViewById(R.id.editTextPassword);
        textInvalidCredentials = (TextView)findViewById(R.id.textInvalidCredentials);
        setupLoginButton();
        setupCreateAccountButton();
    }

    private void setupLoginButton() {
        Button btnLogin = (Button)findViewById(R.id.btnLogin);
        btnLogin.setOnClickListener(v -> {
            try {
                validateLoginCredentials();
            } catch (JSONException e) {
                e.printStackTrace();
            }
        });
    }

    private void validateLoginCredentials() throws JSONException {
        if (eTextUsername.getText().toString().matches(getString(R.string.empty_string)) ||
                eTextPassword.getText().toString().matches(getString(R.string.empty_string))) {
            textInvalidCredentials.setText(getString(R.string.invalid_credentials_empty));
        } else {
            SharedPreferences userPref = getSharedPreferences("PREF_USERS", MODE_PRIVATE);
            String keyFromPref = userPref.getString(eTextUsername.getText().toString(), null);
            if (keyFromPref == null) {
                textInvalidCredentials.setText(getString(R.string.invalid_user_dne));
            } else {
                JSONObject fetchUser = new JSONObject(keyFromPref);
                if (!eTextPassword.getText().toString().matches(fetchUser.optString("password"))) {
                    textInvalidCredentials.setText(getString(R.string.invalid_password));
                }
                else {
                    Gson gson = new Gson();
                    dHandler.setUser(gson.fromJson(keyFromPref, User.class));
                    dHandler.setUserLoggedIn(true);
                    dHandler.setDataLoadedFromSharedPref(true);
                    saveUserSessionIntoSharedPref();
                    Toast.makeText(this, getString(R.string.toast_login_successful), Toast.LENGTH_SHORT).show();
                    finish();
                }
            }
        }
    }

    private void saveUserSessionIntoSharedPref() {
        SharedPreferences sessionPref = getSharedPreferences("PREF_USER_SESSION", MODE_PRIVATE);
        SharedPreferences.Editor sessionEditor = sessionPref.edit();
        sessionEditor.putString("userSessionKey", dHandler.getUser().getUsername());
        sessionEditor.putBoolean("userLoggedIn", dHandler.isUserLoggedIn());
        sessionEditor.apply();
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