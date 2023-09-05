package com.nsb.restaurant.adapter;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Lifecycle;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.nsb.restaurant.fragment.MenuFragment;
import com.nsb.restaurant.fragment.OverviewFragment;

public class ViewPagerInfoAdapter extends FragmentStateAdapter {


    public ViewPagerInfoAdapter(@NonNull FragmentManager fragmentManager, @NonNull Lifecycle lifecycle) {
        super(fragmentManager, lifecycle);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        if (position == 0){
            return new OverviewFragment();
        }
        return new MenuFragment();
    }

    @Override
    public int getItemCount() {
        return 2;
    }
}
