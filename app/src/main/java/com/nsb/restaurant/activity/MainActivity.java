package com.nsb.restaurant.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.TranslateAnimation;

import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.navigation.NavigationBarView;
import com.nsb.restaurant.R;
import com.nsb.restaurant.databinding.ActivityMainBinding;
import com.nsb.restaurant.fragment.AccountFragment;
import com.nsb.restaurant.fragment.BookingFragment;
import com.nsb.restaurant.fragment.InfoRestaurantFragment;
import com.nsb.restaurant.fragment.MenuFragment;
import com.nsb.restaurant.fragment.OrderFoodFragment;
import com.nsb.restaurant.fragment.OverviewFragment;
import com.nsb.restaurant.listener.ListenFromFragment;
import com.nsb.restaurant.util.Constant;


public class MainActivity extends AppCompatActivity implements
        MenuFragment.MenuInterface,
        OverviewFragment.OverviewInterface,
        BookingFragment.BookingInterface {

    ActivityMainBinding binding;
    private InfoRestaurantFragment infoFragment;
    private BookingFragment bookingFragment;
    private MenuFragment menuFragment;
    private AccountFragment accountFragment;
    private OrderFoodFragment orderFoodFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        super.onCreate(savedInstanceState);
        setContentView(binding.getRoot());

        init();
        setEvent();
        setBottomNavigationView();


    }

    private void init() {
        infoFragment = new InfoRestaurantFragment();
        bookingFragment = new BookingFragment();
        menuFragment = new MenuFragment();
        accountFragment = new AccountFragment();
        orderFoodFragment = new OrderFoodFragment();
    }

    private void setEvent() {

    }

    private void setBottomNavigationView() {
        if (getIntent().getExtras() != null && getIntent().getExtras().containsKey(Constant.IS_BOOKING) == true) {
            getSupportFragmentManager().beginTransaction().replace(R.id.container, bookingFragment).commit();
            return;
        }
        getSupportFragmentManager().beginTransaction().replace(R.id.container, infoFragment).commit();
        binding.bottomNavigationView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.menu_home:
                        getSupportFragmentManager().beginTransaction().replace(R.id.container, infoFragment).commit();
                        return true;
                    case R.id.menu_booking:
                        getSupportFragmentManager().beginTransaction().replace(R.id.container, bookingFragment).commit();
                        return true;
                    case R.id.menu_order:
                        getSupportFragmentManager().beginTransaction().replace(R.id.container, orderFoodFragment).commit();
                        return true;
                    case R.id.menu_account:
                        getSupportFragmentManager().beginTransaction().replace(R.id.container, accountFragment).commit();
                        return true;
                }

                return false;
            }
        });
    }

    @Override
    public void setVisibleAppBar(Boolean isUp) {
        if (isUp == false) {
            binding.fab.setVisibility(View.INVISIBLE);
            binding.bottomAppBar.setVisibility(View.INVISIBLE);
        } else {
            binding.fab.setVisibility(View.VISIBLE);
            binding.bottomAppBar.setVisibility(View.VISIBLE);
        }
    }
}