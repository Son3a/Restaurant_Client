package com.nsb.restaurant.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.nsb.restaurant.R;
import com.nsb.restaurant.model.FoodModel;
import com.squareup.picasso.Picasso;

public class PhotoSlideFragment extends Fragment {
    private View photoView;
    private ImageView imageView;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        photoView = inflater.inflate(R.layout.fragment_photo, container, false);

        Bundle bundle = getArguments();
        FoodModel foodModel = (FoodModel) bundle.get("Photo");

        imageView = photoView.findViewById(R.id.image_slide_photo);
        Picasso.get().load(foodModel.getImage())
                .into(imageView);

        return photoView;
    }
}
