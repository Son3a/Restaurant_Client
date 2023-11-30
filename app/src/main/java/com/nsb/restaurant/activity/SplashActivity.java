package com.nsb.restaurant.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;

import com.nsb.restaurant.activity.user.MainUserActivity;
import com.nsb.restaurant.databinding.ActivitySplashBinding;
import com.nsb.restaurant.util.Constant;
import com.nsb.restaurant.util.PreferenceManager;

public class SplashActivity extends AppCompatActivity {
    private ActivitySplashBinding binding;
    private PreferenceManager preferenceManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        binding = ActivitySplashBinding.inflate(getLayoutInflater());
        super.onCreate(savedInstanceState);
        setContentView(binding.getRoot());

        preferenceManager = new PreferenceManager(this);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if (preferenceManager.getBoolean(Constant.IS_SIGNED_IN)) {
                    finish();
                    startActivity(new Intent(getApplicationContext(), MainUserActivity.class));
                } else {
                    finish();
                    startActivity(new Intent(getApplicationContext(), LoginActivity.class));
                }
            }
        }, 2000);

    }
}