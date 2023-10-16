package com.nsb.restaurant.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.nsb.restaurant.R;
import com.nsb.restaurant.activity.user.FoodDetailActivity;
import com.nsb.restaurant.model.FoodModel;
import com.nsb.restaurant.util.Constant;
import com.squareup.picasso.Picasso;

public class PhotoSlideFragment extends Fragment {
    private View photoView;
    private ImageView imageView;
    private TextView textPriceSaleOff;
    private FrameLayout layoutSaleOff;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        photoView = inflater.inflate(R.layout.fragment_photo, container, false);
        textPriceSaleOff = photoView.findViewById(R.id.textSaleOff);
        layoutSaleOff = photoView.findViewById(R.id.layoutSaleOff);

        Bundle bundle = getArguments();
        FoodModel foodModel = (FoodModel) bundle.get("Photo");

        imageView = photoView.findViewById(R.id.image_slide_photo);
        Picasso.get().load(foodModel.getImage())
                .into(imageView);

        if (!foodModel.getPriceSaleOff().equals("")) {
            textPriceSaleOff.setText("-" + foodModel.getSaleOff() + "%");
            layoutSaleOff.setVisibility(View.VISIBLE);
        }

        imageView.setOnClickListener(v->{
            Intent intent = new Intent(getActivity(), FoodDetailActivity.class);
            intent.putExtra(Constant.FOOD_MODEL, foodModel);
            startActivity(intent);
        });

        return photoView;
    }
}
