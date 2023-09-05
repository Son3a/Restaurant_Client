package com.nsb.restaurant.adapter;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.nsb.restaurant.databinding.ItemBookingHistoryBinding;
import com.nsb.restaurant.databinding.ItemBookingNowBinding;
import com.nsb.restaurant.listener.BookingListener;
import com.nsb.restaurant.model.BookingModel;
import com.nsb.restaurant.util.Constant;

import java.util.List;

public class BookingNowAdapter extends RecyclerView.Adapter<BookingNowAdapter.BookingNowHolder> {
    private final List<BookingModel> bookingModelList;
    private final BookingListener listener;

    public BookingNowAdapter(List<BookingModel> bookingModelList, BookingListener listener) {
        this.bookingModelList = bookingModelList;
        this.listener = listener;
    }

    @NonNull
    @Override
    public BookingNowHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemBookingNowBinding itemBookingHistoryBinding = ItemBookingNowBinding.inflate(
                LayoutInflater.from(parent.getContext()),
                parent,
                false
        );
        return new BookingNowHolder(itemBookingHistoryBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull BookingNowHolder holder, int position) {
        holder.setData(bookingModelList.get(position));
    }

    @Override
    public int getItemCount() {
        return bookingModelList.size();
    }

    class BookingNowHolder extends RecyclerView.ViewHolder {
        ItemBookingNowBinding binding;

        public BookingNowHolder(ItemBookingNowBinding itemBookingHistoryBinding) {
            super(itemBookingHistoryBinding.getRoot());
            binding = itemBookingHistoryBinding;
        }

        void setData(BookingModel bookingModel) {
            binding.textTimeBooking.setText(Constant.formatTimeDDMMYYYY(bookingModel.getTimeBooking()));
            binding.textNumOfPeople.setText(bookingModel.getNumOfPeople() + " người");
            binding.textStatus.setText(bookingModel.getStatus());
            binding.buttonBookingAgain.setOnClickListener(v -> {
                listener.onClickBooking(bookingModel);
            });
        }
    }


}
