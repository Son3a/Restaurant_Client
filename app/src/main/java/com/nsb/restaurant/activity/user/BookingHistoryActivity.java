package com.nsb.restaurant.activity.user;

import androidx.appcompat.app.AppCompatActivity;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.nsb.restaurant.adapter.BookingHistoryAdapter;
import com.nsb.restaurant.databinding.ActivityBookingHistoryBinding;
import com.nsb.restaurant.listener.BookingListener;
import com.nsb.restaurant.model.BookingModel;
import com.nsb.restaurant.util.Constant;
import com.nsb.restaurant.util.PreferenceManager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BookingHistoryActivity extends AppCompatActivity implements BookingListener {
    private ActivityBookingHistoryBinding binding;
    private BookingHistoryAdapter bookingHistoryAdapter;
    private List<BookingModel> bookingModelList;
    private PreferenceManager preferenceManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        binding = ActivityBookingHistoryBinding.inflate(getLayoutInflater());
        super.onCreate(savedInstanceState);
        setContentView(binding.getRoot());

        init();
        setEvent();
    }

    private void init() {
        preferenceManager = new PreferenceManager(BookingHistoryActivity.this);
    }

    private void refresh() {
        binding.layoutRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getBookingHistory();
            }
        });
    }

    private void setEvent() {
        back();
        getBookingHistory();
        refresh();
        gotoBooking();
    }

    private void gotoBooking(){
//        binding.buttonBooking.setOnClickListener(v->{
//            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
//            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
//            intent.putExtra(Constant.IS_BOOKING, true);
//            startActivity(intent);
//        });
    }

    private void back() {
        binding.imageBack.setOnClickListener(v -> {
            onBackPressed();
        });
    }

    private void setAdapter() {
        bookingHistoryAdapter = new BookingHistoryAdapter(bookingModelList, this);
        binding.recycleViewBookingHistory.setAdapter(bookingHistoryAdapter);
    }

    private void getBookingHistory() {
        String url = Constant.URL_DEV + "/booking/get-booking-history-by-client";
        bookingModelList = new ArrayList<>();
        binding.pbLoading.setVisibility(View.VISIBLE);
        RequestQueue queue = Volley.newRequestQueue(BookingHistoryActivity.this);

        JsonObjectRequest sr = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
//                binding.pbLoadingAll.setVisibility(View.INVISIBLE);
                try {
                    JSONArray jsonArray = response.getJSONArray("data").getJSONArray(0);
                    Log.d("LENG", "onResponse: " + jsonArray.length());
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonObject = jsonArray.getJSONObject(i);

                        bookingModelList.add(new BookingModel(
                                jsonObject.getString("MAPD"),
                                jsonObject.getString("NGAYDAT"),
                                jsonObject.getString("SONGUOI"),
                                jsonObject.getString("TRANGTHAI"),
                                jsonObject.getString("NGAYTAO"),
                                jsonObject.getString("HOTEN"),
                                jsonObject.getString("SDT"),
                                jsonObject.getString("EMAIL").trim(),
                                jsonObject.getString("TENBAN")));
                    }
                    binding.pbLoading.setVisibility(View.GONE);
                    setAdapter();
                    bookingHistoryAdapter.notifyDataSetChanged();
                    binding.layoutRefresh.setRefreshing(false);
                    if (bookingModelList.size() == 0) {
                        binding.textEmpty.setVisibility(View.VISIBLE);
                        //binding.buttonBooking.setVisibility(View.VISIBLE);
                    } else {
                        binding.textEmpty.setVisibility(View.GONE);
                        //binding.buttonBooking.setVisibility(View.GONE);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
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
                headers.put("authorization", "Bearer " + preferenceManager.getString(Constant.TOKEN));
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
    public void onClickBooking(BookingModel bookingModel) {
        Intent intent = new Intent(BookingHistoryActivity.this, BookingSuccessActivity.class);
        intent.putExtra(Constant.BOOKING_OBJECT, bookingModel);
        intent.putExtra(Constant.IS_HISTORY_ACTIVITY, "1");
        startActivity(intent);
    }
}