package com.nsb.restaurant.adapter;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.nsb.restaurant.databinding.ItemPayBillBinding;
import com.nsb.restaurant.listener.DepositListener;
import com.nsb.restaurant.model.DepositModel;
import com.nsb.restaurant.util.Constant;

import java.util.List;

public class PayHistoryAdapter extends RecyclerView.Adapter<PayHistoryAdapter.PayHistoryHolder> {
    private final List<DepositModel> depositModelList;
    private final DepositListener listener;

    public PayHistoryAdapter(List<DepositModel> depositModelList, DepositListener listener) {
        this.depositModelList = depositModelList;
        this.listener = listener;
    }

    @NonNull
    @Override
    public PayHistoryHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemPayBillBinding itemPayBillBinding = ItemPayBillBinding.inflate(
                LayoutInflater.from(parent.getContext()),
                parent,
                false
        );

        return new PayHistoryHolder(itemPayBillBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull PayHistoryHolder holder, int position) {
        holder.setData(depositModelList.get(position));
    }

    @Override
    public int getItemCount() {
        return depositModelList.size();
    }

    class PayHistoryHolder extends RecyclerView.ViewHolder {
        ItemPayBillBinding binding;

        public PayHistoryHolder(ItemPayBillBinding itemPayBillBinding) {
            super(itemPayBillBinding.getRoot());
            binding = itemPayBillBinding;
        }

        void setData(DepositModel depositModel) {
            binding.getRoot().setOnClickListener(v -> {
                listener.onCLickDeposit(depositModel);
            });
            binding.textMoney.setText("-" + Constant.formatSalary(depositModel.getMoneyDeposit()) + "đ");
            binding.textTime.setText(Constant.formatTimeDDMMYYYY(depositModel.getTimeDeposit()));
        }
    }
}
