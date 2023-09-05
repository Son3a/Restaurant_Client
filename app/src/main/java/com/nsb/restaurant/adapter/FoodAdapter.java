package com.nsb.restaurant.adapter;

import static com.nsb.restaurant.util.Constant.formatSalary;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.nsb.restaurant.databinding.ItemFoodBinding;
import com.nsb.restaurant.model.FoodModel;
import com.squareup.picasso.Picasso;

import java.util.List;

public class FoodAdapter extends RecyclerView.Adapter<FoodAdapter.FoodHolder> {
    private final List<FoodModel> foodModelList;

    public FoodAdapter(List<FoodModel> foodModelList) {
        this.foodModelList = foodModelList;
    }

    @NonNull
    @Override
    public FoodHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemFoodBinding itemFoodBinding = ItemFoodBinding.inflate(
                LayoutInflater.from(parent.getContext()),
                parent,
                false
        );
        return new FoodHolder(itemFoodBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull FoodHolder holder, int position) {
        holder.setData(foodModelList.get(position));
    }

    @Override
    public int getItemCount() {
        return foodModelList.size();
    }

    class FoodHolder extends RecyclerView.ViewHolder {
        ItemFoodBinding binding;

        FoodHolder(ItemFoodBinding itemFoodBinding) {
            super(itemFoodBinding.getRoot());
            binding = itemFoodBinding;
        }

        void setData(FoodModel foodModel) {
            binding.textNameFood.setText(foodModel.getName());
            binding.textPrice.setText(formatSalary(foodModel.getPrice()) + "đ");
            if (foodModel.getImage() != null && !foodModel.getImage().equals("")) {
                Picasso.get().load(foodModel.getImage()).into(binding.imageFood);
            }
            if (!foodModel.getSaleOff().equals("")) {
                binding.textSaleOff.setText("-" + foodModel.getSaleOff() + "%");
                binding.textPriceSales.setText(formatSalary(foodModel.getPrice()) + "đ");
                binding.textPrice.setText(formatSalary(foodModel.getPriceSaleOff()) + "đ");
                binding.layoutSaleoff.setVisibility(View.VISIBLE);
                binding.layoutPriceSales.setVisibility(View.VISIBLE);
            }
        }
    }
}
