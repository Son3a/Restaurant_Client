package com.nsb.restaurant.adapter;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.nsb.restaurant.databinding.ItemSaleOffBinding;
import com.nsb.restaurant.model.SaleModel;

import java.util.List;

public class SaleOffAdapter extends RecyclerView.Adapter<SaleOffAdapter.SaleOffHolder> {
    private final List<SaleModel> saleModelList;

    public SaleOffAdapter(List<SaleModel> saleModelList) {
        this.saleModelList = saleModelList;
    }

    @NonNull
    @Override
    public SaleOffHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemSaleOffBinding itemSaleOffBinding = ItemSaleOffBinding.inflate(
                LayoutInflater.from(parent.getContext()),
                parent,
                false
        );
        return new SaleOffHolder(itemSaleOffBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull SaleOffHolder holder, int position) {
        holder.setData(saleModelList.get(position));
    }

    @Override
    public int getItemCount() {
        return saleModelList.size();
    }

    class SaleOffHolder extends RecyclerView.ViewHolder {
        ItemSaleOffBinding binding;

        SaleOffHolder(ItemSaleOffBinding itemSaleOffBinding) {
            super(itemSaleOffBinding.getRoot());
            this.binding = itemSaleOffBinding;
        }

        void setData(SaleModel saleModel) {

        }
    }
}
