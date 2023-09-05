package com.nsb.restaurant.adapter;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.nsb.restaurant.databinding.ItemTopFoodBinding;
import com.nsb.restaurant.model.FoodModel;
import com.squareup.picasso.Picasso;

import java.util.List;

public class TopFoodAdapter extends RecyclerView.Adapter<TopFoodAdapter.FoodHolder> {
    private final List<FoodModel> foodModelList;

    public TopFoodAdapter(List<FoodModel> foodModelList) {
        this.foodModelList = foodModelList;
    }

    @NonNull
    @Override
    public FoodHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemTopFoodBinding itemTopFoodBinding = ItemTopFoodBinding.inflate(
                LayoutInflater.from(parent.getContext()),
                parent,
                false
        );
        return new FoodHolder(itemTopFoodBinding);
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
        ItemTopFoodBinding binding;

        FoodHolder(ItemTopFoodBinding itemTopFoodBinding) {
            super(itemTopFoodBinding.getRoot());
            binding = itemTopFoodBinding;
        }

        void setData(FoodModel foodModel) {
            binding.textContent.setText(foodModel.getName());
            if(foodModel.getImage() != null && !foodModel.getImage().equals("")){
                Picasso.get().load(foodModel.getImage()).into(binding.imageFood);
            }
        }
    }
}
