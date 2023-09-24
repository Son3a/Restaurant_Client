package com.nsb.restaurant.fragment;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.widget.NestedScrollView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.nsb.restaurant.R;
import com.nsb.restaurant.adapter.FoodAdapter;
import com.nsb.restaurant.adapter.SaleOffAdapter;
import com.nsb.restaurant.adapter.TopFoodAdapter;
import com.nsb.restaurant.model.FoodModel;
import com.nsb.restaurant.model.SaleModel;
import com.nsb.restaurant.util.Constant;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class OverviewFragment extends Fragment {
    private View overviewFragment;
    private TextView textHotLine;
    private RecyclerView recyclerViewSaleOff, recycleViewTopFood;
    private SaleOffAdapter saleOffAdapter;
    private List<SaleModel> saleModelList;
    private NestedScrollView layoutNested;
    private TopFoodAdapter topFoodAdapter;
    private List<FoodModel> foodModelList;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        overviewFragment = inflater.inflate(R.layout.fragment_overview, container, false);

        init();
        setEvent();
        return overviewFragment;
    }

    private void init() {
        textHotLine = overviewFragment.findViewById(R.id.textHotLine);
        recyclerViewSaleOff = overviewFragment.findViewById(R.id.recycleViewSaleOff);
        layoutNested = overviewFragment.findViewById(R.id.layoutContent);
        recycleViewTopFood = overviewFragment.findViewById(R.id.recycleViewTopFood);
        foodModelList = new ArrayList<>();
        saleModelList = new ArrayList<>();
    }

    private void setEvent() {
        call();
        setSaleOffAdapter();
        setTopFoodAdapter();
        getTopFoods();
    }

    private void call() {
        textHotLine.setOnClickListener(v -> {
            String phone = textHotLine.getText().toString();
            Intent intent = new Intent(Intent.ACTION_DIAL, Uri.fromParts("tel", phone, null));
            startActivity(intent);

        });
    }

    private void setTopFoodAdapter() {
        topFoodAdapter = new TopFoodAdapter(foodModelList);
        recycleViewTopFood.setAdapter(topFoodAdapter);
    }

    private void getTopFoods() {
        String url = Constant.URL_DEV + "/food/get-top-foods/30";
//        progressBar.setVisibility(View.VISIBLE);
//        recycleViewFood.setVisibility(View.GONE);
        foodModelList.clear();


        RequestQueue queue = Volley.newRequestQueue(getActivity());


        JsonObjectRequest sr = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    JSONArray jsonArray = response.getJSONArray("data");
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                        if(jsonObject.getString("SO_LUOT_MUA").toString().equals("0")) continue;

                        foodModelList.add(new FoodModel(
                                jsonObject.get("MAMA").toString().trim(),
                                jsonObject.get("TEN").toString().trim(),
                                jsonObject.get("HINH_ANH").toString().trim()));
                        Log.d("Food", foodModelList.get(i).getImage());
                    }
                    topFoodAdapter.notifyDataSetChanged();
//                    progressBar.setVisibility(View.GONE);
//                    recycleViewFood.setVisibility(View.VISIBLE);
                } catch (JSONException e) {
                    Log.d("Error", e.getMessage());
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("Error", error.toString());
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

    private void setSaleOffAdapter() {
        saleModelList.add(new SaleModel("1", "1", "1", "1", "1", "1"));
        saleModelList.add(new SaleModel("1", "1", "1", "1", "1", "1"));
        saleModelList.add(new SaleModel("1", "1", "1", "1", "1", "1"));
        saleModelList.add(new SaleModel("1", "1", "1", "1", "1", "1"));
        saleModelList.add(new SaleModel("1", "1", "1", "1", "1", "1"));
        saleModelList.add(new SaleModel("1", "1", "1", "1", "1", "1"));
        saleModelList.add(new SaleModel("1", "1", "1", "1", "1", "1"));

        saleOffAdapter = new SaleOffAdapter(saleModelList);
        recyclerViewSaleOff.setAdapter(saleOffAdapter);
    }
}
