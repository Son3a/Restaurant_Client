package com.nsb.restaurant.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import androidx.viewpager2.widget.ViewPager2;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.nsb.restaurant.activity.admin.AddFoodActivity;
import com.nsb.restaurant.activity.user.FoodDetailActivity;
import com.nsb.restaurant.adapter.FoodMenuAdapter;
import com.nsb.restaurant.adapter.FoodToOrderAdapter;
import com.nsb.restaurant.adapter.PhotoAdapter;
import com.nsb.restaurant.databinding.FragmentMenu1Binding;
import com.nsb.restaurant.listener.CategoryListener;
import com.nsb.restaurant.listener.FoodListener;
import com.nsb.restaurant.model.CategoryModel;
import com.nsb.restaurant.model.FoodModel;
import com.nsb.restaurant.util.Constant;
import com.nsb.restaurant.util.LoadingDialog;
import com.nsb.restaurant.util.PreferenceManager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MenuFragment extends Fragment implements CategoryListener, FoodListener {
    private FragmentMenu1Binding binding;
    private FoodMenuAdapter foodAdapter;
    private List<FoodModel> listFoods, listFoodBestSeller;
    private PreferenceManager preferenceManager;
    private LoadingDialog loadingDialog;

    private Handler handler = new Handler();
    private Runnable runnable = new Runnable() {
        @Override
        public void run() {
            if (listFoodBestSeller != null && listFoodBestSeller.size() == 0) {
                binding.layoutBestSeller.setVisibility(View.GONE);
            } else {
                binding.layoutBestSeller.setVisibility(View.VISIBLE);
                if (binding.viewPager2.getCurrentItem() == listFoodBestSeller.size() - 1) {
                    binding.viewPager2.setCurrentItem(0);
                } else {
                    binding.viewPager2.setCurrentItem(binding.viewPager2.getCurrentItem() + 1);
                }
            }
        }
    };

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentMenu1Binding.inflate(getLayoutInflater());

        init();
        setEvent();

        return binding.getRoot();
    }

    private void init() {
        preferenceManager = new PreferenceManager(getContext());
        loadingDialog = new LoadingDialog(getContext());
        listFoods = new ArrayList<>();
        foodAdapter = new FoodMenuAdapter(listFoods, this);
        listFoodBestSeller = new ArrayList<>();
        binding.rcvListFoods.setAdapter(foodAdapter);
    }

    private void setEvent() {
        findFood();
        clickFindJob();
        refreshData();
        gotoAddFood();
        setStateBottomSheet();
    }

    private void setStateBottomSheet() {
        Constant.setTransparentBottomSheet(getContext(), binding.rcvListFoods);
        Constant.setTransparentBottomSheet(getContext(), binding.layoutBestSeller);
        Constant.setTransparentBottomSheet(getContext(), binding.rcvListFoods);
    }

    private void refreshData() {

        binding.layoutRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                binding.layoutRefresh.setRefreshing(false);
                loadingDialog.showDialog();
                findFood();
            }
        });
    }

    private void gotoAddFood() {
        binding.imageAddFood.setOnClickListener(v -> {
            startActivity(new Intent(getActivity(), AddFoodActivity.class));
        });
    }

    private void setViewPagerAdapter(List<FoodModel> listFood) {
        binding.layoutBestSeller.setVisibility(View.VISIBLE);
        PhotoAdapter adapter = new PhotoAdapter(getActivity(), listFood);
        binding.viewPager2.setAdapter(adapter);
        binding.circleIndicator.setViewPager(binding.viewPager2);

        binding.viewPager2.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                handler.removeCallbacks(runnable);
                handler.postDelayed(runnable, 5000);
            }
        });
    }

    private void clickFindJob() {
        binding.imageSearch.setOnClickListener(v -> {
            loadingDialog.showDialog();
            Constant.hideKeyboardFrom(getActivity(), binding.textSearch);
            findFood();
        });

        binding.textSearch.setOnEditorActionListener((textView, i, keyEvent) -> {
            if (i == EditorInfo.IME_ACTION_SEARCH) {
                loadingDialog.showDialog();
                Constant.hideKeyboardFrom(getActivity(), binding.textSearch);
                findFood();
                return true;
            }
            return false;
        });
    }

    private void findFood() {
        try {
            if (binding.textSearch.getText() == null || binding.textSearch.getText().toString().equals("")) {
                callAPIFindFood("");
                getBestSeller();
            } else {
                callAPIFindFood(binding.textSearch.getText().toString().trim());
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    private void callAPIFindFood(String data) throws JSONException {
        Log.d("FindFood", "starting...");
        String url = Constant.URL_DEV + "/food/find-food";
        RequestQueue queue = Volley.newRequestQueue(getActivity());

        listFoods.clear();

        JSONObject jsonReq = new JSONObject();
        jsonReq.put("data", data);


        JsonObjectRequest sr = new JsonObjectRequest(Request.Method.POST, url, jsonReq, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {

                try {
                    JSONArray jsonArray = response.getJSONArray("data");
                    String priceSaleOff, saleOff;
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                        saleOff = jsonObject.get("KHUYENMAI") != JSONObject.NULL ? jsonObject.getString("KHUYENMAI") : "";
                        priceSaleOff = jsonObject.get("GIA_KM") != JSONObject.NULL ? jsonObject.getString("GIA_KM") : "";
                        listFoods.add(new FoodModel(
                                jsonObject.getString("MAMA").trim(),
                                jsonObject.getString("TEN").trim(),
                                jsonObject.getString("GIA").trim(),
                                jsonObject.getString("HINH_ANH").trim(),
                                saleOff,
                                priceSaleOff
                        ));


                    }
                    Log.d("Food", data);
                    loadingDialog.hideDialog();
                    if (listFoods.size() == 0) {
                        binding.textTitle.setText("Món ăn không tồn tại!");
                    } else {
                        binding.layoutFood.setVisibility(View.VISIBLE);
                        if(!data.isEmpty()){
                            binding.textTitle.setText("Kết quả tìm kiếm");
                        } else {
                            binding.textTitle.setText("Gợi ý món ăn");
                        }
                    }
                    binding.layoutRefresh.setRefreshing(false);
                    foodAdapter.notifyDataSetChanged();

                } catch (JSONException e) {
                    Log.d("Error", e.getMessage());
                    loadingDialog.hideDialog();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("Error", error.toString());
                loadingDialog.hideDialog();
            }
        }) {

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap<String, String>();
                headers.put("Content-Type", "application/json");
                return headers;
            }
        };
        sr.setRetryPolicy(new DefaultRetryPolicy(
                0,
                -1,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        queue.add(sr);
    }

    private void getBestSeller() {
        String url = Constant.URL_DEV + "/food/get-top-foods/" + 60;
        //progressBar.setVisibility(View.VISIBLE);
        RequestQueue queue = Volley.newRequestQueue(getActivity());
        listFoodBestSeller.clear();

        JsonObjectRequest sr = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    JSONArray jsonArray = response.getJSONArray("data");
                    String priceSaleOff, saleOff;
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                        if (jsonObject.getString("SO_LUONG").equals("0")) {
                            break;
                        }
                        saleOff = jsonObject.get("KHUYENMAI") != JSONObject.NULL ? jsonObject.getString("KHUYENMAI") : "";
                        priceSaleOff = jsonObject.get("GIA_KM") != JSONObject.NULL ? jsonObject.getString("GIA_KM") : "";
                        listFoodBestSeller.add(new FoodModel(
                                jsonObject.getString("MAMA").trim(),
                                jsonObject.getString("TEN").trim(),
                                jsonObject.getString("GIA").trim(),
                                jsonObject.getString("HINH_ANH").trim(),
                                saleOff,
                                priceSaleOff
                        ));
                        Log.d("Food", listFoodBestSeller.get(i).getImage());
                    }
                    Log.d("BestSeller", listFoodBestSeller.size() + "");
                    loadingDialog.hideDialog();
                    if (listFoodBestSeller.size() == 0) {
                        binding.layoutBestSeller.setVisibility(View.GONE);
                    } else {
                        setViewPagerAdapter(listFoodBestSeller);
                    }

                } catch (JSONException e) {
                    Log.d("Error", e.getMessage());
                    //progressBar.setVisibility(View.GONE);
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("Error", error.toString());
                loadingDialog.hideDialog();
                //progressBar.setVisibility(View.GONE);
            }
        }) {

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap<String, String>();
                headers.put("Content-Type", "application/json");
                return headers;
            }
        };
        sr.setRetryPolicy(new DefaultRetryPolicy(
                0,
                -1,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        queue.add(sr);
    }

    @Override
    public void onClickCategory(CategoryModel categoryModel) {
        //getFoodsByCategory(categoryModel.getId());
    }

    @Override
    public void onResume() {
        super.onResume();
        handler.postDelayed(runnable, 3000);
    }

    @Override
    public void onPause() {
        super.onPause();
        handler.removeCallbacks(runnable);
    }

    @Override
    public void onClickFood(FoodModel foodModel) {
        Intent intent = new Intent(getActivity(), FoodDetailActivity.class);
        intent.putExtra(Constant.FOOD_MODEL, foodModel);
        startActivity(intent);
    }

    @Override
    public void onRemove(FoodModel foodModel) {

    }

    @Override
    public void onStart() {
        super.onStart();
        Log.d("Menu", "on Start");
    }


}
