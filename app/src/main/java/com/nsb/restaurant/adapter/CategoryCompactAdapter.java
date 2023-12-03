package com.nsb.restaurant.adapter;

import android.content.res.ColorStateList;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.nsb.restaurant.R;
import com.nsb.restaurant.databinding.ItemCategoryFoodCompactBinding;
import com.nsb.restaurant.listener.CategoryListener;
import com.nsb.restaurant.model.CategoryModel;
import com.squareup.picasso.Picasso;

import java.util.List;

public class CategoryCompactAdapter extends RecyclerView.Adapter<CategoryCompactAdapter.CategoryHolder> {

    private final List<CategoryModel> categoryModelList;
    private final CategoryListener listener;
    private final List<Boolean> listSelected;
    private int default_position = 0;


    public CategoryCompactAdapter(List<CategoryModel> categoryModelList, CategoryListener listener, List<Boolean> listSelected) {
        this.categoryModelList = categoryModelList;
        this.listener = listener;
        this.listSelected = listSelected;
    }

    @NonNull
    @Override
    public CategoryHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemCategoryFoodCompactBinding itemCategoryFoodCompactBinding = ItemCategoryFoodCompactBinding.inflate(
                LayoutInflater.from(parent.getContext()),
                parent,
                false
        );

        return new CategoryHolder(itemCategoryFoodCompactBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull CategoryHolder holder, int position) {
        holder.setData(categoryModelList.get(position));

        if (listSelected.get(position) == false) {
            holder.binding.layoutImage.setBackgroundTintList(ColorStateList.valueOf(0xFFFFFFFF));
        } else {
            holder.binding.layoutImage.setBackgroundTintList(ColorStateList.valueOf(0x6EAEAD));
        }
        holder.binding.getRoot().setOnClickListener(v -> {
            listSelected.set(default_position,false);
            listSelected.set(position, true);
            notifyItemChanged(position);
            notifyItemChanged(default_position);
            default_position = position;

            listener.onClickCategory(categoryModelList.get(position));
        });
    }

    @Override
    public int getItemCount() {
        return categoryModelList.size();
    }

    class CategoryHolder extends RecyclerView.ViewHolder {
        ItemCategoryFoodCompactBinding binding;

        CategoryHolder(ItemCategoryFoodCompactBinding itemCategoryFoodBinding) {
            super(itemCategoryFoodBinding.getRoot());
            binding = itemCategoryFoodBinding;
        }

        void setData(CategoryModel categoryModel) {
            binding.textCategory.setText(categoryModel.getName());
            if (categoryModel.getImage() != null && !categoryModel.getImage().equals("")) {
                Picasso.get().load(categoryModel.getImage()).into(binding.imageCategory);
            }
        }
    }
}
