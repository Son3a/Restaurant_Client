package com.nsb.restaurant.fragment;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.widget.NestedScrollView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.ViewPager2;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.nsb.restaurant.R;
import com.nsb.restaurant.adapter.CategoryAdapter;
import com.nsb.restaurant.adapter.FoodAdapter;
import com.nsb.restaurant.adapter.PhotoAdapter;
import com.nsb.restaurant.listener.CategoryListener;
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

public class MenuFragment extends Fragment implements CategoryListener {
    private View menuView;
    private RecyclerView recyclerViewCategory, recycleViewFood;
    private CategoryAdapter categoryAdapter;
    private List<CategoryModel> categoryModelList;
    private List<FoodModel> foodModelList;
    private FoodAdapter foodAdapter;
    private NestedScrollView layoutNested;
    private ProgressBar progressBar;
    private List<Boolean> listSelected;

    private ViewPager2 viewPager2;
    private CircleIndicator3 indicator3;

    private Handler handler = new Handler();
    private Runnable runnable = new Runnable() {
        @Override
        public void run() {
            if (viewPager2.getCurrentItem() == foodModelList.size() - 1) {
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


    private void setEvent() {
        getAllFood();
    }

    private void init() {
//        recyclerViewCategory = menuView.findViewById(R.id.recycleViewCategory);
//        categoryModelList = new ArrayList<>();
//        recycleViewFood = menuView.findViewById(R.id.recycleViewFoods);
        foodModelList = new ArrayList<>();
        viewPager2 = menuView.findViewById(R.id.viewPager2);
        indicator3 = menuView.findViewById(R.id.circleIndicator);
//        layoutNested = menuView.findViewById(R.id.layoutNested);
//        progressBar = menuView.findViewById(R.id.pbLoading);
    }

    //
//    private void setEvent() {
//        setCategoryAdapter();
//        setFoodAdapter();
//        getCategories();
//        getAllFood();
//    }
//
//    private void setCategoryAdapter() {
//        listSelected = new ArrayList<>();
//        categoryAdapter = new CategoryAdapter(categoryModelList, this, listSelected);
//        recyclerViewCategory.setAdapter(categoryAdapter);
//    }
//
//    private void setFoodAdapter() {
//        foodAdapter = new FoodAdapter(foodModelList);
//        recycleViewFood.setAdapter(foodAdapter);
//    }
//
//    private void getFoodsByCategory(String idCategory) {
//        String url = Constant.URL_DEV + "/food/get-food-by-category/" + idCategory;
//        progressBar.setVisibility(View.VISIBLE);
//        recycleViewFood.setVisibility(View.GONE);
//        foodModelList.clear();
//
//
//        RequestQueue queue = Volley.newRequestQueue(getActivity());
//
//
//        JsonObjectRequest sr = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
//            @Override
//            public void onResponse(JSONObject response) {
//                try {
//                    JSONArray jsonArray = response.getJSONArray("data").getJSONArray(0);
//                    String saleOff, priceSaleOff;
//                    for (int i = 0; i < jsonArray.length(); i++) {
//                        JSONObject jsonObject = jsonArray.getJSONObject(i);
//                        saleOff = jsonObject.get("KHUYENMAI") != JSONObject.NULL ? jsonObject.getString("KHUYENMAI") : "";
//                        priceSaleOff = jsonObject.get("GIA_KM") != JSONObject.NULL ? jsonObject.getString("GIA_KM") : "";
//                        foodModelList.add(new FoodModel(
//                                jsonObject.getString("MAMA").trim(),
//                                jsonObject.getString("TEN").trim(),
//                                jsonObject.getString("GIA").trim(),
//                                jsonObject.getString("HINH_ANH").trim(),
//                                saleOff,
//                                priceSaleOff
//                        ));
//                        Log.d("Food", foodModelList.get(i).getImage());
//                    }
//                    foodAdapter.notifyDataSetChanged();
//                    progressBar.setVisibility(View.GONE);
//                    recycleViewFood.setVisibility(View.VISIBLE);
//                } catch (JSONException e) {
//                    Log.d("Error", e.getMessage());
//                }
//            }
//        }, new Response.ErrorListener() {
//            @Override
//            public void onErrorResponse(VolleyError error) {
//                Log.d("Error", error.toString());
//            }
//        }) {
//
//            @Override
//            public Map<String, String> getHeaders() throws AuthFailureError {
//                HashMap<String, String> headers = new HashMap<String, String>();
//                headers.put("Content-Type", "application/json");
//                return headers;
//            }
//        };
//        sr.setRetryPolicy(new DefaultRetryPolicy(
//                0,
//                -1,
//                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
//        queue.add(sr);
//    }
//
//    private void getCategories() {
//        String url = Constant.URL_DEV + "/category/get-categories";
//        new Thread(new Runnable() {
//            @Override
//            public void run() {
//                RequestQueue queue = Volley.newRequestQueue(getActivity());
//
//                JsonObjectRequest sr = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
//                    @Override
//                    public void onResponse(JSONObject response) {
//                        try {
//                            JSONArray jsonArray = response.getJSONArray("data").getJSONArray(0);
//                            for (int i = 0; i < jsonArray.length(); i++) {
//                                JSONObject jsonObject = jsonArray.getJSONObject(i);
//                                categoryModelList.add(new CategoryModel(
//                                        jsonObject.get("MALOAI").toString().trim(),
//                                        jsonObject.get("TEN").toString().trim(),
//                                        jsonObject.get("HINH_ANH").toString().trim()));
//                                Log.d("Category", categoryModelList.get(i).getImage());
//                                listSelected.add(false);
//                            }
//                            getActivity().runOnUiThread(new Runnable() {
//                                @Override
//                                public void run() {
//                                    categoryAdapter.notifyDataSetChanged();
//                                }
//                            });
//                        } catch (JSONException e) {
//                            Log.d("Error", e.getMessage());
//                        }
//                    }
//                }, new Response.ErrorListener() {
//                    @Override
//                    public void onErrorResponse(VolleyError error) {
//                        Log.d("Error", error.toString());
//                        progressBar.setVisibility(View.GONE);
//                    }
//                }) {
//
//                    @Override
//                    public Map<String, String> getHeaders() throws AuthFailureError {
//                        HashMap<String, String> headers = new HashMap<String, String>();
//                        headers.put("Content-Type", "application/json");
//                        return headers;
//                    }
//                };
//                sr.setRetryPolicy(new DefaultRetryPolicy(
//                        0,
//                        -1,
//                        DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
//                queue.add(sr);
//            }
//        }).start();
//
//    }
//

    private void setViewPagerAdapter() {
        PhotoAdapter adapter = new PhotoAdapter(getActivity(), foodModelList);
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

    private void getAllFood() {
        String url = Constant.URL_DEV + "/food/get-price-food-with-sale-off";
        //progressBar.setVisibility(View.VISIBLE);
        RequestQueue queue = Volley.newRequestQueue(getActivity());

        JsonObjectRequest sr = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    JSONArray jsonArray = response.getJSONArray("data");
                    String priceSaleOff, saleOff;
                    int count = 0;  //test
                    for (int i = 0; i < jsonArray.length(); i++) {
                        if (count == 5) break;  //test
                        count++;    //test
                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                        saleOff = jsonObject.get("KHUYENMAI") != JSONObject.NULL ? jsonObject.getString("KHUYENMAI") : "";
                        priceSaleOff = jsonObject.get("GIA_KM") != JSONObject.NULL ? jsonObject.getString("GIA_KM") : "";
                        foodModelList.add(new FoodModel(
                                jsonObject.getString("MAMA").trim(),
                                jsonObject.getString("TEN").trim(),
                                jsonObject.getString("GIA").trim(),
                                jsonObject.getString("HINH_ANH").trim(),
                                saleOff,
                                priceSaleOff
                        ));
                        Log.d("Food", foodModelList.get(i).getImage());
                    }

                    setViewPagerAdapter();
                    //progressBar.setVisibility(View.GONE);
                    //foodAdapter.notifyDataSetChanged();
                } catch (JSONException e) {
                    Log.d("Error", e.getMessage());
                    progressBar.setVisibility(View.GONE);
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("Error", error.toString());
                progressBar.setVisibility(View.GONE);
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
}
