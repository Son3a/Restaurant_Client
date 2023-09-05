package com.nsb.restaurant.adapter;

import static com.nsb.restaurant.util.Constant.formatSalary;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.nsb.restaurant.databinding.ItemBillBinding;
import com.nsb.restaurant.model.FoodModel;

import java.util.List;

public class FoodBillAdapter extends RecyclerView.Adapter<FoodBillAdapter.FoodBillHolder> {
    private final List<FoodModel> foodModelList;

    public FoodBillAdapter(List<FoodModel> foodModelList) {
        this.foodModelList = foodModelList;
    }

    @NonNull
    @Override
    public FoodBillHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemBillBinding itemBillBinding = ItemBillBinding.inflate(
                LayoutInflater.from(parent.getContext()),
                parent,
                false
        );
        return new FoodBillHolder(itemBillBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull FoodBillHolder holder, int position) {
        holder.setData(foodModelList.get(position));
    }

    @Override
    public int getItemCount() {
        return foodModelList.size();
    }

    class FoodBillHolder extends RecyclerView.ViewHolder {
        ItemBillBinding binding;

        FoodBillHolder(ItemBillBinding itemBillBinding) {
            super(itemBillBinding.getRoot());
            binding = itemBillBinding;
        }

        void setData(FoodModel foodModel) {
            binding.textNameFood.setText(foodModel.getName());
            binding.textMoney.setText(formatSalary(foodModel.getPrice()) + " vnd");
            binding.textAmount.setText(String.valueOf(foodModel.getNumOfFood()));
        }
    }
}
