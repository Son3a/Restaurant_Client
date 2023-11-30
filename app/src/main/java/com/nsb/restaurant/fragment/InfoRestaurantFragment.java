package com.nsb.restaurant.fragment;

import static java.lang.Math.abs;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.viewpager2.widget.ViewPager2;

import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.tabs.TabLayout;
import com.nsb.restaurant.R;
import com.nsb.restaurant.activity.user.MainUserActivity;
import com.nsb.restaurant.adapter.ViewPagerInfoAdapter;
import com.nsb.restaurant.databinding.FragmentInfoRestaurantBinding;
import com.nsb.restaurant.util.Constant;

public class InfoRestaurantFragment extends Fragment {
    private FragmentInfoRestaurantBinding binding;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentInfoRestaurantBinding.inflate(getLayoutInflater());

        init();

        setEvent();
        setStateAppBar();

        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    private void init() {

    }

    private void setEvent() {
        call();
        setStateBottomSheet();
    }

    private void setStateBottomSheet() {
        Constant.setTransparentBottomSheet(getContext(), binding.layoutContent);
    }

    private void setStateAppBar() {
        binding.appBar.addOnOffsetChangedListener((appBarLayout, verticalOffset) -> {
            if (abs(verticalOffset) - appBarLayout.getTotalScrollRange() == 0) {
                binding.toolBar.setVisibility(View.VISIBLE);
            } else if (verticalOffset == 0) {

            } else {
                binding.toolBar.setVisibility(View.INVISIBLE);
            }
        });
    }

    private void call() {
        binding.textHotLine.setOnClickListener(v -> {
            String phone = binding.textHotLine.getText().toString();
            Intent intent = new Intent(Intent.ACTION_DIAL, Uri.fromParts("tel", phone, null));
            startActivity(intent);

        });
    }

//    private void setVisibleBottomAppbar(){
//        layoutNested.setOnScrollChangeListener((NestedScrollView.OnScrollChangeListener) (v, scrollX, scrollY, oldScrollX, oldScrollY) -> {
//            if(scrollY < oldScrollY){
//                listener.onScroll(false);
//            }else if(scrollY > oldScrollY){
//                listener.onScroll(true);
//            }
//        });
//    }

//    @Override
//    public void onAttach(Context context) {
//        super.onAttach(context);
//        if (context instanceof InfoRestaurantListener) {
//            //init the listener
//            listener = (InfoRestaurantListener) context;
//        } else {
//            throw new RuntimeException(context.toString()
//                    + " must implement InteractionListener");
//        }
//    }
//
//    @Override
//    public void onDetach() {
//        super.onDetach();
//        listener = null;
//    }

}
