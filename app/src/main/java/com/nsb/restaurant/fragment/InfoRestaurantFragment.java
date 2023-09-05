package com.nsb.restaurant.fragment;

import static java.lang.Math.abs;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.viewpager2.widget.ViewPager2;

import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.tabs.TabLayout;
import com.nsb.restaurant.R;
import com.nsb.restaurant.adapter.ViewPagerInfoAdapter;

public class InfoRestaurantFragment extends Fragment {

    private View infoRestaurantView;
    private AppBarLayout appBar;
    private Toolbar toolbar;
    private ViewPager2 viewPager2;
    private TabLayout tabLayout;
    private ViewPagerInfoAdapter viewPagerInfoAdapter;
//    private InfoRestaurantListener listener;

//    public interface InfoRestaurantListener{
//        void onScroll(Boolean isDown);
//    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        infoRestaurantView = inflater.inflate(R.layout.fragment_info_restaurant, container, false);

        init();

        setEvent();
        setStateAppBar();

        return infoRestaurantView;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        tabLayout = infoRestaurantView.findViewById(R.id.tabs);
        viewPager2 = infoRestaurantView.findViewById(R.id.viewPager);

        initViewPager2();
        initTabLayout();

        setTabLayout();
        setViewPager2();
    }

    private void setEvent() {

    }

    private void init(){
        appBar = infoRestaurantView.findViewById(R.id.appBar);
        toolbar = infoRestaurantView.findViewById(R.id.toolBar);
    }

    private void initTabLayout(){
        tabLayout.addTab(tabLayout.newTab().setText("Tổng quan"));
        tabLayout.addTab(tabLayout.newTab().setText("Thực đơn"));
    }

    private void setStateAppBar() {
        appBar.addOnOffsetChangedListener((appBarLayout, verticalOffset) -> {
            if (abs(verticalOffset) - appBarLayout.getTotalScrollRange() == 0) {
                toolbar.setVisibility(View.VISIBLE);
            } else if (verticalOffset == 0) {

            } else {
                toolbar.setVisibility(View.INVISIBLE);
            }
        });
    }

    private void initViewPager2(){
        FragmentManager fragmentManager = getChildFragmentManager();
        viewPagerInfoAdapter = new ViewPagerInfoAdapter(fragmentManager , getLifecycle());
        viewPager2.setAdapter(viewPagerInfoAdapter);
    }

    private void setViewPager2(){
        viewPager2.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                tabLayout.selectTab(tabLayout.getTabAt(position));
            }
        });
        viewPager2.setUserInputEnabled(false);
        viewPager2.setSaveEnabled(false);
    }

    private void setTabLayout(){
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager2.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
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
