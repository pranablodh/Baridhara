package com.consistentservices.baridhara.activities;

import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.TextView;

import com.consistentservices.baridhara.BuildConfig;
import com.consistentservices.baridhara.R;

public class splashScreen extends AppCompatActivity
{
    private CardView splash_card;
    private TextView bottom_text;

    private Animation from_bottom;
    private Animation from_top;


    private static final int SPLASH_SCREEN_DURATION = 2000;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        //Hiding Action Bar
        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();

        //UI Elements Assignment
        splash_card = (CardView) findViewById(R.id.splash_card);
        bottom_text = (TextView) findViewById(R.id.bottom_text);

        checkVersion();
        Animation();

        new Handler().postDelayed(new Runnable()
        {
            @Override
            public void run()
            {
                Intent loginScreen = new Intent(splashScreen.this, login.class);
                startActivity(loginScreen);
                finish();
            }
        },SPLASH_SCREEN_DURATION);
    }

    private void Animation()
    {
        from_bottom = AnimationUtils.loadAnimation(this, R.anim.from_bottom);
        bottom_text.setAnimation(from_bottom);

        from_top = AnimationUtils.loadAnimation(this, R.anim.from_top);
        splash_card.setAnimation(from_top);
    }

    private void checkVersion()
    {
        final int versionCode = BuildConfig.VERSION_CODE;
        final String versionName = BuildConfig.VERSION_NAME;
        Log.d("Version","Version Code: "+String.valueOf(versionCode));
        Log.d("Version","Version Name: "+versionName);
    }
}