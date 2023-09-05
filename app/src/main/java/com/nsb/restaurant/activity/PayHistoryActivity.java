package com.nsb.restaurant.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.nsb.restaurant.adapter.FoodBillAdapter;
import com.nsb.restaurant.adapter.PayHistoryAdapter;
import com.nsb.restaurant.databinding.ActivityPayHistoryBinding;
import com.nsb.restaurant.fragment.BookingFragment;
import com.nsb.restaurant.model.DepositModel;
import com.nsb.restaurant.model.FoodModel;
import com.nsb.restaurant.util.Constant;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PayHistoryActivity extends AppCompatActivity {
    private ActivityPayHistoryBinding binding;
    private List<FoodModel> foodModelList;
    private FoodBillAdapter foodBillAdapter;
    private int totalMoney = 0;
    private DepositModel depositModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        binding = ActivityPayHistoryBinding.inflate(getLayoutInflater());
        super.onCreate(savedInstanceState);
        setContentView(binding.getRoot());
        depositModel = (DepositModel) getIntent().getSerializableExtra(Constant.DEPOSIT_OBJECT);

        loading();
        back();
    }

    private void loading() {
        binding.textDeposit.setText(Constant.formatSalary(depositModel.getMoneyDeposit()) + " vnd");
        binding.textDepositCode.setText(depositModel.getIdDeposit());
        binding.textTimeDeposit.setText(formatTime(depositModel.getTimeDeposit()));
        binding.textTypePay.setText(depositModel.getTypePay());
    }

    private void back(){
        binding.buttonBack.setOnClickListener(v->{
            onBackPressed();
        });
    }

    private String formatDate(String time) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
        SimpleDateFormat output = new SimpleDateFormat("dd-mm-yyyy");

        String newTime = null;
        try {
            newTime = output.format(sdf.parse(time));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return newTime;
    }

    private String formatTime(String time) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
        SimpleDateFormat output = new SimpleDateFormat("HH:mm dd/MM/yyyy");

        String newTime = null;
        try {
            newTime = output.format(sdf.parse(time));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return newTime;
    }

//    private void getListFoodsBooking() throws JSONException {
//        String url = Constant.URL_DEV + "/food/get-food-booking";
//        foodModelList = new ArrayList<>();
////        binding.recycleViewListFoods.setVisibility(View.GONE);
//        RequestQueue queue = Volley.newRequestQueue(PayHistoryActivity.this);
//
//        JSONObject jsonRequest = new JSONObject();
//        Log.d("Result", depositModel.getIdTable() + "  " + depositModel.getIdBooking());
//        jsonRequest.put("idBooking", depositModel.getIdBooking());
//
//        JsonObjectRequest sr = new JsonObjectRequest(Request.Method.POST, url, jsonRequest, new Response.Listener<JSONObject>() {
//            @Override
//            public void onResponse(JSONObject response) {
//                try {
//                    JSONArray jsonArray = response.getJSONArray("data");
//                    Log.d("responsE", response.toString());
//                    for (int i = 0; i < jsonArray.length(); i++) {
//                        JSONObject jsonObject = jsonArray.getJSONObject(i);
//                        foodModelList.add(new FoodModel(
//                                jsonObject.getString("TEN"),
//                                jsonObject.getString("GIA").trim(),
//                                Integer.parseInt(jsonObject.getString("SO_LUONG"))
//                        ));
//                        Log.d("Foodd", foodModelList.get(i).toString());
//                        totalMoney += Integer.parseInt(jsonObject.getString("GIA")) * Integer.parseInt(jsonObject.getString("SO_LUONG"));
//                    }
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
}