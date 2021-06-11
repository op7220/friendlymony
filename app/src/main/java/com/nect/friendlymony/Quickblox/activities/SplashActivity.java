package com.nect.friendlymony.Quickblox.activities;

import android.os.Bundle;
import android.os.Handler;

import com.nect.friendlymony.Activity.MainActivity;
import com.nect.friendlymony.Quickblox.services.LoginService;
import com.nect.friendlymony.Quickblox.utils.SharedPrefsHelper;
import com.nect.friendlymony.R;
import com.quickblox.messages.services.SubscribeService;

public class SplashActivity extends BaseActivity {
    private static final int SPLASH_DELAY = 1500;

    private SharedPrefsHelper sharedPrefsHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        sharedPrefsHelper = SharedPrefsHelper.getInstance();

        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if (sharedPrefsHelper.hasQbUser()) {
                    LoginService.start(SplashActivity.this, sharedPrefsHelper.getQbUser());
                    SubscribeService.subscribeToPushes(SplashActivity.this,false);
                    OpponentsActivity.start(SplashActivity.this);
                } else {
                    LoginActivity.start(SplashActivity.this);
                }
                finish();
            }
        }, SPLASH_DELAY);
    }


}