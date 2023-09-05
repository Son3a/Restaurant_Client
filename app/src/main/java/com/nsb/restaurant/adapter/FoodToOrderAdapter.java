package com.nsb.restaurant.adapter;

import static com.nsb.restaurant.util.Constant.formatSalary;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

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
        ItemFoodToOrderBinding itemLayoutFoodBinding = ItemFoodToOrderBinding.inflate(
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
        ItemFoodToOrderBinding binding;

        FoodToOrderHolder(ItemFoodToOrderBinding itemLayoutFoodBinding) {
            super(itemLayoutFoodBinding.getRoot());
            binding = itemLayoutFoodBinding;
        }

        void setData(FoodModel foodModel) {
            binding.textNameFood.setText(foodModel.getName());
            binding.textPrice.setText(formatSalary(foodModel.getPrice()) + " vnd");
            if (foodModel.getImage() != null && !foodModel.getImage().equals("")) {
                Picasso.get().load(foodModel.getImage()).into(binding.imageFood);
            }
            binding.getRoot().setOnClickListener(v->{
                listener.onClickFood(foodModel);
            });
        }
    }
}
