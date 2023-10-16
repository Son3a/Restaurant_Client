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
import com.nsb.restaurant.adapter.PayHistoryAdapter;
import com.nsb.restaurant.databinding.ActivityHistoryPaymentBinding;
import com.nsb.restaurant.listener.DepositListener;
import com.nsb.restaurant.model.DepositModel;
import com.nsb.restaurant.util.Constant;
import com.nsb.restaurant.util.PreferenceManager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PaymentHistoryActivity extends AppCompatActivity implements DepositListener {
    private ActivityHistoryPaymentBinding binding;
    private PayHistoryAdapter payHistoryAdapter;
    private List<DepositModel> depositModelList;
    private PreferenceManager preferenceManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        binding = ActivityHistoryPaymentBinding.inflate(getLayoutInflater());
        super.onCreate(savedInstanceState);
        setContentView(binding.getRoot());
        init();

        getListDeposit();
        back();
        refresh();
    }

    private void init() {
        preferenceManager = new PreferenceManager(PaymentHistoryActivity.this);
    }

    private void back() {
        binding.imageBack.setOnClickListener(v -> {
            onBackPressed();
        });
    }

    private void refresh() {
        binding.layoutRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getListDeposit();
            }
        });
    }

    private void getListDeposit() {
        String url = Constant.URL_DEV + "/deposit/get-deposit-by-client";
        depositModelList = new ArrayList<>();
        binding.pbLoading.setVisibility(View.VISIBLE);
        RequestQueue queue = Volley.newRequestQueue(PaymentHistoryActivity.this);

        JsonObjectRequest sr = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {

                try {
                    JSONArray jsonArray = response.getJSONArray("data");
                    Log.d("responsE", response.toString());
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                        depositModelList.add(new DepositModel(
                                jsonObject.getString("MAPC"),
                                jsonObject.getString("GIACOC"),
                                jsonObject.getString("NGAYCOC"),
                                jsonObject.getString("MAPD"),
                                jsonObject.getString("MABAN"),
                                jsonObject.getString("HT_THANHTOAN")
                        ));
                    }
                    binding.pbLoading.setVisibility(View.GONE);
                    payHistoryAdapter = new PayHistoryAdapter(depositModelList, PaymentHistoryActivity.this);
                    binding.recycleViewPaymentHistory.setAdapter(payHistoryAdapter);
                    binding.layoutRefresh.setRefreshing(false);
                    if (depositModelList.size() == 0) {
                        binding.textEmpty.setVisibility(View.VISIBLE);
                    } else {
                        binding.textEmpty.setVisibility(View.GONE);
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
    public void onCLickDeposit(DepositModel depositModel) {
        Intent intent = new Intent(PaymentHistoryActivity.this, PayHistoryActivity.class);
        intent.putExtra(Constant.DEPOSIT_OBJECT, depositModel);
        startActivity(intent);
    }
}