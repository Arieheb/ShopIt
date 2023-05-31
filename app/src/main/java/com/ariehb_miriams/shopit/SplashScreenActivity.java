package com.ariehb_miriams.shopit;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

public class SplashScreenActivity extends AppCompatActivity {
    private static final int SPLASH_DURATION = 1000;
    private static final int LOOP_COUNT = 3;
    private int loopCounter = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);
        getSupportActionBar().hide();
        splashLoop();
    }
    private void splashLoop() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if (loopCounter < LOOP_COUNT) {
                    // Continue looping
                    loopCounter++;
                    splashLoop();
                } else {
                    // Start your main activity after the splash loop completes
                    Intent intent = new Intent(SplashScreenActivity.this, SignInActivity.class);
                    startActivity(intent);
                    finish();
                }
            }
        }, SPLASH_DURATION);

    }
}