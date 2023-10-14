package com.nsb.restaurant.activity.admin;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.nsb.restaurant.databinding.ActivityAddFoodBinding;

public class AddFoodActivity extends AppCompatActivity {
    ActivityAddFoodBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        binding = ActivityAddFoodBinding.inflate(getLayoutInflater());
        super.onCreate(savedInstanceState);
        setContentView(binding.getRoot());
    }
}