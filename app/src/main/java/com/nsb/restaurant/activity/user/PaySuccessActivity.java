package com.nsb.restaurant.activity.user;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.nsb.restaurant.adapter.FoodBillAdapter;
import com.nsb.restaurant.databinding.ActivityPaySuccessBinding;
import com.nsb.restaurant.fragment.BookingFragment;
import com.nsb.restaurant.model.FoodModel;
import com.nsb.restaurant.util.Constant;

import java.util.Calendar;
import java.util.List;

public class PaySuccessActivity extends AppCompatActivity {
    private ActivityPaySuccessBinding binding;
    private FoodBillAdapter foodBillAdapter;
    private List<FoodModel> foodModelList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        binding = ActivityPaySuccessBinding.inflate(getLayoutInflater());
        super.onCreate(savedInstanceState);
        setContentView(binding.getRoot());

        init();

        setEvent();
    }

    private void init() {
        foodBillAdapter = new FoodBillAdapter(OrderFoodActivity.foodModelListOrdering);
        binding.recycleListFoods.setAdapter(foodBillAdapter);
        binding.textTotal.setText(Constant.formatSalary(String.valueOf(OrderFoodActivity.totalMoneyPay)) + " vnd");
        binding.textDeposit.setText(Constant.formatSalary(String.valueOf(OrderFoodActivity.depositMoney)) + " vnd");
        binding.textDeposit1.setText(Constant.formatSalary(String.valueOf(OrderFoodActivity.depositMoney)) + " vnd");
        int restMoney = OrderFoodActivity.totalMoneyPay - OrderFoodActivity.depositMoney;
        binding.textRest.setText(Constant.formatSalary(String.valueOf(restMoney)) + " vnd");
        binding.textTimeComing.setText(BookingFragment.timeBookingTable);
        binding.textTable.setText(BookingFragment.nameTable.trim());
    }

    private void setEvent() {
        setTime();
        back();
    }

    private void setTime() {
        Calendar calendar = Calendar.getInstance();

        int day = calendar.get(Calendar.DAY_OF_MONTH);
        int month = calendar.get(Calendar.MONTH);
        int year = calendar.get(Calendar.YEAR);
        int hour = calendar.get(Calendar.HOUR_OF_DAY); // gets hour in 24h format
        int minute = calendar.get(Calendar.MINUTE);

        String date = day + " thg " + (month + 1) + "," + year;
        String time = hour + ":" + minute;

        binding.textTime.setText(time);
        binding.textDate.setText(date);
    }

    private void back(){
        binding.buttonBack.setOnClickListener(v->{
            finish();
        });
    }
}