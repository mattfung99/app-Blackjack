package com.example.app_blackjack.ui;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import com.example.app_blackjack.R;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.not_logged_in_main, menu);
//        getMenuInflater().inflate(R.menu.logged_in_main, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return fetchNotLoggedInAction(item);
//        return fetchLoggedInAction(item);
    }

    @SuppressLint("NonConstantResourceId")
    private boolean fetchNotLoggedInAction(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.user_profile: case R.id.action_log_in:
                Intent loginIntent = UserLoginActivity.makeIntent(MainActivity.this);
                startActivity(loginIntent);
                break;
            case R.id.action_create_user:
                Intent createUserIntent = CreateUserActivity.makeIntent(MainActivity.this);
                startActivity(createUserIntent);
                break;
        }
        return super.onOptionsItemSelected(item);
    }

//    @SuppressLint("NonConstantResourceId")
//    private boolean fetchLoggedInAction(MenuItem item) {
//        switch (item.getItemId()) {
//            case R.id.action_edit_user:
//                Intent editUserIntent = EditUserActivity.makeIntent(MainActivity.this);
//                startActivity(editUserIntent);
//                break;
//            case R.id.action_view_profile:
//                Intent viewProfileIntent = ViewProfileActivity.makeIntent(MainActivity.this);
//                startActivity(viewProfileIntent);
//                break;
//            case R.id.action_sign_out:
//                // To be added
//                break;
//        }
//        return super.onOptionsItemSelected(item);
//    }
}