package com.nsb.restaurant.activity.user;

import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.nsb.restaurant.adapter.FoodBillAdapter;
import com.nsb.restaurant.databinding.ActivityPayHistoryBinding;
import com.nsb.restaurant.model.DepositModel;
import com.nsb.restaurant.model.FoodModel;
import com.nsb.restaurant.util.Constant;
import com.nsb.restaurant.util.LoadingDialog;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PayHistoryActivity extends AppCompatActivity {
    private ActivityPayHistoryBinding binding;
    private List<FoodModel> foodModelList;
    private FoodBillAdapter foodBillAdapter;
    private int totalMoney = 0;
    private DepositModel depositModel;
    private LoadingDialog loadingDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        binding = ActivityPayHistoryBinding.inflate(getLayoutInflater());
        super.onCreate(savedInstanceState);
        setContentView(binding.getRoot());
        depositModel = (DepositModel) getIntent().getSerializableExtra(Constant.DEPOSIT_OBJECT);
        loadingDialog = new LoadingDialog(PayHistoryActivity.this);

        loading();
        back();
        try {
            getListFoodsBooking();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void loading() {
        try {
            binding.textDeposit.setText(Constant.formatSalary(depositModel.getMoneyDeposit()) + " vnd");
            binding.textCodeDeposit.setText(depositModel.getIdDeposit());
            binding.textTypePay.setText(depositModel.getTypePay());
            setTime(depositModel.getTimeDeposit());
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    private void back() {
        binding.buttonBack.setOnClickListener(v -> {
            onBackPressed();
        });
    }

    private void setTime(String time) throws ParseException {
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
        Date date = df.parse(time);

        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);

        int day = calendar.get(Calendar.DAY_OF_MONTH);
        int month = calendar.get(Calendar.MONTH);
        int year = calendar.get(Calendar.YEAR);
        int hour = calendar.get(Calendar.HOUR_OF_DAY); // gets hour in 24h format
        int minute = calendar.get(Calendar.MINUTE);

        String dateString = day + " thg " + (month + 1) + "," + year;
        String timeString = hour + ":" + minute;

        binding.textTime.setText(timeString);
        binding.textDate.setText(dateString);
    }

    private void getListFoodsBooking() throws JSONException {
        String url = Constant.URL_DEV + "/food/get-food-booking";
        loadingDialog.showDialog();
        foodModelList = new ArrayList<>();
        RequestQueue queue = Volley.newRequestQueue(PayHistoryActivity.this);

        JSONObject jsonRequest = new JSONObject();
        Log.d("Result", depositModel.getIdTable() + "  " + depositModel.getIdBooking());
        jsonRequest.put("idBooking", depositModel.getIdBooking());

        JsonObjectRequest sr = new JsonObjectRequest(Request.Method.POST, url, jsonRequest, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    JSONArray jsonArray = response.getJSONArray("data");
                    Log.d("responsE", response.toString());
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                        foodModelList.add(new FoodModel(
                                jsonObject.getString("TEN"),
                                jsonObject.getString("GIA").trim(),
                                Integer.parseInt(jsonObject.getString("SO_LUONG"))
                        ));
                        Log.d("Foodd", foodModelList.get(i).toString());
                        totalMoney += Integer.parseInt(jsonObject.getString("GIA")) * Integer.parseInt(jsonObject.getString("SO_LUONG"));
                    }

                    foodBillAdapter = new FoodBillAdapter(foodModelList);
                    binding.recycleListFoods.setAdapter(foodBillAdapter);
                    loadingDialog.hideDialog();
                    binding.layoutMain.setVisibility(View.VISIBLE);
                } catch (JSONException e) {
                    loadingDialog.hideDialog();
                    Log.d("Error", e.getMessage());
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
}