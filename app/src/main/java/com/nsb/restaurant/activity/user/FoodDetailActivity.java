package com.nsb.restaurant.activity.user;

import static com.nsb.restaurant.util.Constant.formatSalary;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.nsb.restaurant.databinding.ActivityFoodDetailBinding;
import com.nsb.restaurant.model.FoodModel;
import com.nsb.restaurant.util.Constant;
import com.squareup.picasso.Picasso;

public class FoodDetailActivity extends AppCompatActivity {
    private ActivityFoodDetailBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        binding = ActivityFoodDetailBinding.inflate(getLayoutInflater());
        super.onCreate(savedInstanceState);
        setContentView(binding.getRoot());

        back();
        loading();
    }

    private void back() {
        binding.imageBack.setOnClickListener(v -> {
            finish();
        });
    }

    private void loading() {
        FoodModel foodModel = (FoodModel) getIntent().getSerializableExtra(Constant.FOOD_MODEL);

        if(foodModel != null){
            Picasso.get().load(foodModel.getImage()).into(binding.imageFood);

            binding.textNameFood.setText(foodModel.getName());

            binding.textPriceCurrent.setText(formatSalary(foodModel.getPrice()) + "đ");

            if (!foodModel.getSaleOff().equals("")) {
                int priceCurrent = Integer.parseInt(foodModel.getPrice()) - Integer.parseInt(foodModel.getPriceSaleOff());
                binding.textSaleOff.setText("-" + foodModel.getSaleOff() + "%");
                binding.textPricePriceOff.setText(formatSalary(foodModel.getPrice()) + "đ");
                binding.textPriceCurrent.setText(formatSalary(String.valueOf(priceCurrent)) + "đ");
                binding.layoutSaleOff.setVisibility(View.VISIBLE);
                binding.layoutPriceSaleOff.setVisibility(View.VISIBLE);
            }
        }
    }
}