package com.nsb.restaurant.adapter;

import static com.nsb.restaurant.util.Constant.formatSalary;

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

import java.util.List;

public class FoodMenuAdapter extends RecyclerView.Adapter<FoodMenuAdapter.FoodMenuHolder> {
    private final List<FoodModel> foodModelList;
    private final FoodListener listener;

    public FoodMenuAdapter(List<FoodModel> foodModelList, FoodListener listener) {
        this.foodModelList = foodModelList;
        this.listener = listener;
    }

    @NonNull
    @Override
    public FoodMenuAdapter.FoodMenuHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemFoodToOrderBinding itemLayoutFoodBinding = ItemFoodToOrderBinding.inflate(
                LayoutInflater.from(parent.getContext()),
                parent,
                false
        );
        return new FoodMenuHolder(itemLayoutFoodBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull FoodMenuHolder holder, int position) {
        holder.setData(foodModelList.get(position));
    }

    @Override
    public int getItemCount() {
        return foodModelList.size();
    }

    class FoodMenuHolder extends RecyclerView.ViewHolder {
        ItemFoodToOrderBinding binding;

        FoodMenuHolder(ItemFoodToOrderBinding itemLayoutFoodBinding) {
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
