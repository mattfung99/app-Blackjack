package com.example.app_blackjack.ui.activities;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import com.example.app_blackjack.R;

public class BootupActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bootup);
        TextView gameTitle = (TextView)findViewById(R.id.gameTitle);
        TextView gameSubTitle = (TextView)findViewById(R.id.gameSubTitle);
        ImageView flyingCard = (ImageView)findViewById(R.id.flyingCard);
        beginAnimation(gameTitle, gameSubTitle, flyingCard);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.skip_bootup, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_skip) {
            createIntent();
        }
        return super.onOptionsItemSelected(item);
    }

    private void beginAnimation(TextView gameTitle, TextView gameSubTitle, ImageView flyingCard) {
        translateCard(flyingCard, -5000f, 0);
        enlargeTitle(gameTitle);
        spinTitle(gameTitle);

        runOnUiThread(() -> new Handler().postDelayed(() -> {
                    flyingCard.setVisibility(View.VISIBLE);
                    translateCard(flyingCard, 5000f, 1000);
                }, 3500)
        );
        runOnUiThread(() -> new Handler().postDelayed(() -> gameSubTitle.setVisibility(View.VISIBLE), 4500));
        runOnUiThread(() -> new Handler().postDelayed(() -> {
            if (!isDestroyed()) {
                createIntent();
            }
        }, 8000));
    }

    private void enlargeTitle(TextView menuTitle) {
        ObjectAnimator scaleDownX = ObjectAnimator.ofFloat(menuTitle, "scaleX", 14.5f);
        ObjectAnimator scaleDownY = ObjectAnimator.ofFloat(menuTitle, "scaleY", 14.5f);
        scaleDownX.setDuration(3000);
        scaleDownY.setDuration(3000);
        AnimatorSet scaleDown = new AnimatorSet();
        scaleDown.play(scaleDownX).with(scaleDownY);
        scaleDown.start();
    }
    private void spinTitle(TextView menuTitle) {
        RotateAnimation rotate = new RotateAnimation(0, 2880, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        rotate.setDuration(3000);
        rotate.setInterpolator(new LinearInterpolator());
        menuTitle.startAnimation(rotate);
    }

    private void translateCard(ImageView iv, float pos, int duration) {
        ObjectAnimator animation = ObjectAnimator.ofFloat(iv, "x", pos);
        animation.setDuration(duration);
        AnimatorSet animatorSet = new AnimatorSet();
        animatorSet.playTogether(animation);
        animatorSet.start();
    }

    private void createIntent() {
        Intent bootupIntent = MainActivity.makeIntent(BootupActivity.this);
        startActivity(bootupIntent);
        overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
        finish();
    }
}