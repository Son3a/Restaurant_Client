package com.nsb.restaurant.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.nsb.restaurant.databinding.ItemBookingHistoryBinding;
import com.nsb.restaurant.listener.BookingListener;
import com.nsb.restaurant.model.BookingModel;
import com.nsb.restaurant.util.Constant;

import java.util.List;

public class BookingHistoryAdapter extends RecyclerView.Adapter<BookingHistoryAdapter.BookingHistoryHolder> {
    private final List<BookingModel> bookingModelList;
    private final BookingListener listener;

    public BookingHistoryAdapter(List<BookingModel> bookingModelList, BookingListener listener) {
        this.bookingModelList = bookingModelList;
        this.listener = listener;
    }

    @NonNull
    @Override
    public BookingHistoryHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemBookingHistoryBinding itemBookingHistoryBinding = ItemBookingHistoryBinding.inflate(
                LayoutInflater.from(parent.getContext()),
                parent,
                false
        );
        return new BookingHistoryHolder(itemBookingHistoryBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull BookingHistoryHolder holder, int position) {
        holder.setData(bookingModelList.get(position));
    }

    @Override
    public int getItemCount() {
        return bookingModelList.size();
    }

    class BookingHistoryHolder extends RecyclerView.ViewHolder {
        ItemBookingHistoryBinding binding;

        public BookingHistoryHolder(ItemBookingHistoryBinding itemBookingHistoryBinding) {
            super(itemBookingHistoryBinding.getRoot());
            binding = itemBookingHistoryBinding;
        }

        void setData(BookingModel bookingModel) {
            binding.textTimeBooking.setText(Constant.formatTimeDDMMYYYY(bookingModel.getTimeBooking()));
            binding.textNumOfPeople.setText(bookingModel.getNumOfPeople() + " người");
            binding.textStatus.setText(bookingModel.getStatus());
            binding.getRoot().setOnClickListener(v -> {
                listener.onClickBooking(bookingModel);
            });
            if(bookingModel.getStatus().equalsIgnoreCase(Constant.STATUS_END) || bookingModel.getStatus().equalsIgnoreCase(Constant.STATUS_CANCELED)){
                binding.textBookingAgain.setVisibility(View.VISIBLE);
            } else {
                binding.textBookingAgain.setVisibility(View.GONE);
            }
        }
    }


}
