package com.nsb.restaurant.adapter;

import static com.nsb.restaurant.util.Constant.formatSalary;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.nsb.restaurant.databinding.ItemFoodCategoryBinding;
import com.nsb.restaurant.databinding.ItemFoodToOrderBinding;
import com.nsb.restaurant.listener.FoodListener;
import com.nsb.restaurant.model.FoodModel;
import com.squareup.picasso.Picasso;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.NumberFormat;
import java.util.List;

public class FoodToOrderAdapter extends RecyclerView.Adapter<FoodToOrderAdapter.FoodToOrderHolder> {
    private final List<FoodModel> foodModelList;
    private final FoodListener listener;

    public FoodToOrderAdapter(List<FoodModel> foodModelList, FoodListener listener) {
        this.foodModelList = foodModelList;
        this.listener = listener;
    }

    @NonNull
    @Override
    public FoodToOrderHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemFoodCategoryBinding itemLayoutFoodBinding = ItemFoodCategoryBinding.inflate(
                LayoutInflater.from(parent.getContext()),
                parent,
                false
        );
        return new FoodToOrderHolder(itemLayoutFoodBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull FoodToOrderHolder holder, int position) {
        holder.setData(foodModelList.get(position));
    }

    @Override
    public int getItemCount() {
        return foodModelList.size();
    }

    class FoodToOrderHolder extends RecyclerView.ViewHolder {
        ItemFoodCategoryBinding binding;

        FoodToOrderHolder(ItemFoodCategoryBinding itemLayoutFoodBinding) {
            super(itemLayoutFoodBinding.getRoot());
            binding = itemLayoutFoodBinding;
        }

        void setData(FoodModel foodModel) {
            binding.textNameFood.setText(foodModel.getName());
            binding.textPrice.setText(formatSalary(foodModel.getPrice()) + " vnd");
            if (foodModel.getImage() != null && !foodModel.getImage().equals("")) {
                Picasso.get().load(foodModel.getImage()).into(binding.imageFood);
            }
            binding.getRoot().setOnClickListener(v -> {
                listener.onClickFood(foodModel);
            });
            if (foodModel.getSaleOff() != null && !foodModel.getSaleOff().equals("")) {
                int priceCurrent = Integer.parseInt(foodModel.getPrice()) - Integer.parseInt(foodModel.getPriceSaleOff());
                binding.textSaleOff.setText("-" + foodModel.getSaleOff() + "%");
                binding.textPriceSaleOff.setText(formatSalary(foodModel.getPrice()) + "đ");
                binding.textPrice.setText(formatSalary(String.valueOf(priceCurrent)) + "đ");
                binding.layoutSaleOff.setVisibility(View.VISIBLE);
                binding.layoutPriceSaleOff.setVisibility(View.VISIBLE);
            }
        }
    }
}
