package com.nsb.restaurant.adapter;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.nsb.restaurant.fragment.AccountFragment;
import com.nsb.restaurant.fragment.BookingFragment;
import com.nsb.restaurant.fragment.InfoRestaurantFragment;
import com.nsb.restaurant.fragment.MenuFragment;

public class MainUserAdapter extends FragmentStateAdapter {
    public MainUserAdapter(@NonNull FragmentActivity fragmentActivity) {
        super(fragmentActivity);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        switch (position) {
            case 0:
                return new InfoRestaurantFragment();
            case 1:
                return new MenuFragment();
            case 2:
                return new BookingFragment();
            case 3:
                return new AccountFragment();
            default:
                return new InfoRestaurantFragment();
        }
    }

    @Override
    public int getItemCount() {
        return 4;
    }
}
