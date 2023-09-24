package com.nsb.restaurant.adapter;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.nsb.restaurant.fragment.PhotoSlideFragment;
import com.nsb.restaurant.model.FoodModel;

import java.io.Serializable;
import java.util.List;

public class PhotoAdapter extends FragmentStateAdapter {
    private List<FoodModel> listPhotos;

    public PhotoAdapter(@NonNull FragmentActivity fragmentActivity, List<FoodModel> listPhotos) {
        super(fragmentActivity);
        this.listPhotos = listPhotos;
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        FoodModel foodModel = listPhotos.get(position);

        Bundle bundle = new Bundle();
        bundle.putSerializable("Photo", (Serializable) foodModel);

        PhotoSlideFragment photoFragment = new PhotoSlideFragment();
        photoFragment.setArguments(bundle);

        return photoFragment;
    }

    @Override
    public int getItemCount() {
        if (listPhotos != null) {
            return listPhotos.size();
        }
        return 0;
    }
}
