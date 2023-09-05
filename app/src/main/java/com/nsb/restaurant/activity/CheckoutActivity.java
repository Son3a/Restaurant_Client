package com.nsb.restaurant.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
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
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.TextHttpResponseHandler;
import com.nsb.restaurant.databinding.ActivityCheckoutBinding;
import com.nsb.restaurant.fragment.BookingFragment;
import com.nsb.restaurant.model.FoodModel;
import com.nsb.restaurant.util.Constant;
import com.nsb.restaurant.util.PreferenceManager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cz.msebera.android.httpclient.Header;

public class CheckoutActivity extends AppCompatActivity {
    ActivityCheckoutBinding binding;
    private PreferenceManager preferenceManager;
    String orderID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        binding = ActivityCheckoutBinding.inflate(getLayoutInflater());
        super.onCreate(savedInstanceState);
        setContentView(binding.getRoot());
        preferenceManager = new PreferenceManager(CheckoutActivity.this);


        Uri redirectUri = getIntent().getData();
        orderID = redirectUri.getQueryParameter("token");

        binding.textDeposit.setText(Constant.formatSalary(String.valueOf(OrderFoodActivity.depositMoney)) + " vnd");

        binding.buttonConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                captureOrder(orderID);
            }
        });
    }

    void captureOrder(String orderID) {
        //get the accessToken from MainActivity
        String accessToken = OrderFoodActivity.getMyAccessToken();
        binding.pbLoading.setVisibility(View.VISIBLE);
        binding.layoutConfirm.setVisibility(View.INVISIBLE);

        AsyncHttpClient client = new AsyncHttpClient();
        client.addHeader("Accept", "application/json");
        client.addHeader("Content-type", "application/json");
        client.addHeader("Authorization", "Bearer " + accessToken);

        client.post("https://api-m.sandbox.paypal.com/v2/checkout/orders/" + orderID + "/capture", new TextHttpResponseHandler() {
            @Override
            public void onFailure(int statusCode, Header[] headers, String response, Throwable throwable) {
                Log.d("Error", response);
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, String response) {
                try {
                    JSONObject jobj = new JSONObject(response);

                    if (preferenceManager.getBoolean(Constant.IS_ADD_FOOD)) {
                        Log.d("CheckFood", OrderFoodActivity.idBooking + "  "+ OrderFoodActivity.idTable);
                       // bookingFood(OrderFoodActivity.listFoodBooked, OrderFoodActivity.idBooking, OrderFoodActivity.idTable);
                        deposit(OrderFoodActivity.idBooking, OrderFoodActivity.listFoodBooked, OrderFoodActivity.idTable);

                    } else {
                        bookingAndDeposit();
                    }

                    Log.d("Success", "Pay success");
                } catch (JSONException e) {
                    binding.pbLoading.setVisibility(View.INVISIBLE);
                    binding.layoutConfirm.setVisibility(View.VISIBLE);
                    e.printStackTrace();
                }
            }
        });
    }

    private void deposit(String idBooking, List<FoodModel> foodModelList, String idTable) throws JSONException {
        String url = Constant.URL_DEV + "/booking/deposit";
//        binding.recycleViewListFoods.setVisibility(View.GONE);
        RequestQueue queue = Volley.newRequestQueue(CheckoutActivity.this);


        JSONObject jsonRequest = new JSONObject();
        jsonRequest.put("typePay", Constant.TRANSFER);
        jsonRequest.put("moneyDeposit", OrderFoodActivity.depositMoney);
        jsonRequest.put("idBooking", idBooking);


        JsonObjectRequest sr = new JsonObjectRequest(Request.Method.POST, url, jsonRequest, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {

                try {
                    bookingFood(foodModelList,idBooking, idTable);
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

    private void bookingAndDeposit() throws JSONException {
        Log.d("Success", "Pay success");
        String url = Constant.URL_DEV + "/booking/booking-table-and-deposit";
//        binding.recycleViewListFoods.setVisibility(View.GONE);
        RequestQueue queue = Volley.newRequestQueue(CheckoutActivity.this);

        JSONArray jsonArray = new JSONArray();
        for (int i = 0; i < BookingFragment.idTables.size(); i++) {
            Log.d("idTabless", BookingFragment.idTables.get(i));
            jsonArray.put(BookingFragment.idTables.get(i));
        }

        JSONObject jsonRequest = new JSONObject();
        jsonRequest.put("amountPeople", BookingFragment.numOfPeople);
        jsonRequest.put("note", BookingFragment.stringNote);
        jsonRequest.put("timeBooking", BookingFragment.timeBookingTable);
        jsonRequest.put("idTables", jsonArray);
        jsonRequest.put("typePay", Constant.TRANSFER);
        jsonRequest.put("moneyDeposit", OrderFoodActivity.depositMoney);


        JsonObjectRequest sr = new JsonObjectRequest(Request.Method.POST, url, jsonRequest, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
//                binding.pbLoadingAll.setVisibility(View.INVISIBLE);

                try {
                    bookingFood(OrderFoodActivity.foodModelListOrdering, response.getJSONObject("data").getString("mapd"), BookingFragment.idTables.get(0).trim());
                } catch (JSONException e) {
                    Log.d("Error", e.getMessage());
                    binding.pbLoading.setVisibility(View.INVISIBLE);
                    binding.layoutConfirm.setVisibility(View.VISIBLE);
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

    private void bookingFood(List<FoodModel> foodModelList, String idBooking, String idTable) throws JSONException {
        String url = Constant.URL_DEV + "/booking/booking-food";
//        binding.recycleViewListFoods.setVisibility(View.GONE);
        RequestQueue queue = Volley.newRequestQueue(CheckoutActivity.this);

        JSONArray jsonArray = new JSONArray();
        for (FoodModel foodModel : foodModelList) {
            Log.d("Food", foodModel.toString());
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("idBooking", idBooking);
            jsonObject.put("idTable", idTable);
            jsonObject.put("idFood", foodModel.getId());
            jsonObject.put("amountFood", foodModel.getNumOfFood());
            jsonObject.put("price", foodModel.getPrice());
            jsonArray.put(jsonObject);
        }

        JSONObject jsonRequest = new JSONObject();
        jsonRequest.put("listFood", jsonArray);


        JsonObjectRequest sr = new JsonObjectRequest(Request.Method.POST, url, jsonRequest, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
//                binding.pbLoadingAll.setVisibility(View.INVISIBLE);
                binding.pbLoading.setVisibility(View.INVISIBLE);
                binding.layoutConfirm.setVisibility(View.VISIBLE);
                Intent intent = new Intent(CheckoutActivity.this, PaySuccessActivity.class);
                finish();
                startActivity(intent);
                Log.d("Success", "booking food success");
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("Error", error.toString());
                binding.pbLoading.setVisibility(View.INVISIBLE);
                binding.layoutConfirm.setVisibility(View.VISIBLE);
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


}