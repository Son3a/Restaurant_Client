package com.nsb.restaurant.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;
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
import com.github.ybq.android.spinkit.SpinKitView;
import com.nsb.restaurant.R;
import com.nsb.restaurant.activity.FoodDetailActivity;
import com.nsb.restaurant.adapter.FoodToOrderAdapter;
import com.nsb.restaurant.adapter.PhotoAdapter;
import com.nsb.restaurant.listener.CategoryListener;
import com.nsb.restaurant.listener.FoodListener;
import com.nsb.restaurant.model.CategoryModel;
import com.nsb.restaurant.model.FoodModel;
import com.nsb.restaurant.util.Constant;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import me.relex.circleindicator.CircleIndicator3;

public class MenuFragment extends Fragment implements CategoryListener, FoodListener {
    private View menuView;
    private RecyclerView rcvListFoods;
    private FoodToOrderAdapter foodAdapter;
    private List<FoodModel> listFoods, listFoodBestSeller;
    private ImageView buttonFindJob;
    private EditText textDataSearch;
    private ViewPager2 viewPager2;
    private CircleIndicator3 indicator3;
    private LinearLayout layoutBestSeller;
    private TextView textTitle;
    private SwipeRefreshLayout layoutRefresh;
    private ProgressBar pbLoading;

    private Handler handler = new Handler();
    private Runnable runnable = new Runnable() {
        @Override
        public void run() {
            if (viewPager2.getCurrentItem() == listFoodBestSeller.size() - 1) {
                viewPager2.setCurrentItem(0);
            } else {
                viewPager2.setCurrentItem(viewPager2.getCurrentItem() + 1);
            }
        }
    };

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        menuView = inflater.inflate(R.layout.fragment_menu1, container, false);

        init();
        setEvent();

        return menuView;
    }

    private void init() {
        rcvListFoods = menuView.findViewById(R.id.rcvListFoods);
        viewPager2 = menuView.findViewById(R.id.viewPager2);
        indicator3 = menuView.findViewById(R.id.circleIndicator);
        listFoods = new ArrayList<>();
        foodAdapter = new FoodToOrderAdapter(listFoods, this);
        rcvListFoods.setAdapter(foodAdapter);
        buttonFindJob = menuView.findViewById(R.id.imageSearch);
        textDataSearch = menuView.findViewById(R.id.textSearch);
        listFoodBestSeller = new ArrayList<>();
        layoutBestSeller = menuView.findViewById(R.id.layoutBestSeller);
        textTitle = menuView.findViewById(R.id.textTitle);
        layoutRefresh = menuView.findViewById(R.id.layoutRefresh);
        pbLoading = menuView.findViewById(R.id.pbLoading);
    }

    private void setEvent() {
        findFood();
        clickFindJob();
        getBestSeller();
        refreshData();
    }

    private void refreshData() {
        layoutRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                findFood();
            }
        });
    }

    private void setViewPagerAdapter(List<FoodModel> listFood) {
        if (listFood.size() == 0) {
            layoutBestSeller.setVisibility(View.GONE);
        } else {
            PhotoAdapter adapter = new PhotoAdapter(getActivity(), listFood);
            viewPager2.setAdapter(adapter);
            indicator3.setViewPager(viewPager2);

            viewPager2.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
                @Override
                public void onPageSelected(int position) {
                    super.onPageSelected(position);
                    handler.removeCallbacks(runnable);
                    handler.postDelayed(runnable, 5000);
                }
            });
        }
    }

    private void clickFindJob() {
        buttonFindJob.setOnClickListener(v -> {
            pbLoading.setVisibility(View.VISIBLE);
            buttonFindJob.setVisibility(View.GONE);
            Constant.hideKeyboardFrom(getActivity(), textDataSearch);
            findFood();
        });

        textDataSearch.setOnEditorActionListener((textView, i, keyEvent) -> {
            if (i == EditorInfo.IME_ACTION_SEARCH) {
                pbLoading.setVisibility(View.VISIBLE);
                buttonFindJob.setVisibility(View.GONE);
                Constant.hideKeyboardFrom(getActivity(), textDataSearch);
                findFood();
                return true;
            }
            return false;
        });
    }

    private void findFood() {
        try {
            if (textDataSearch.getText() == null || textDataSearch.getText().toString().equals("")) {
                callAPIFindFood("");
            } else {
                callAPIFindFood(textDataSearch.getText().toString().trim());
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

                        Log.d("Food", listFoods.get(i).getImage());
                    }
                    if (!data.equals("")) {
                        layoutBestSeller.setVisibility(View.GONE);
                        textTitle.setText("Kết quả tìm kiếm: " + data);
                    } else {
                        layoutBestSeller.setVisibility(View.VISIBLE);
                        textTitle.setText("Gợi ý món ăn");
                    }
                    layoutRefresh.setRefreshing(false);
                    foodAdapter.notifyDataSetChanged();

                    pbLoading.setVisibility(View.GONE);
                    buttonFindJob.setVisibility(View.VISIBLE);

                } catch (JSONException e) {
                    Log.d("Error", e.getMessage());
                    pbLoading.setVisibility(View.GONE);
                    buttonFindJob.setVisibility(View.VISIBLE);
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("Error", error.toString());
                pbLoading.setVisibility(View.GONE);
                buttonFindJob.setVisibility(View.VISIBLE);
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
                    setViewPagerAdapter(listFoodBestSeller);

                } catch (JSONException e) {
                    Log.d("Error", e.getMessage());
                    //progressBar.setVisibility(View.GONE);
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("Error", error.toString());
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
}
