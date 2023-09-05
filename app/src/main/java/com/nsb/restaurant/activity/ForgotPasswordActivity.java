package com.nsb.restaurant.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.nsb.restaurant.databinding.ActivityForgotPasswordBinding;
import com.nsb.restaurant.fragment.BookingFragment;
import com.nsb.restaurant.model.BookingModel;
import com.nsb.restaurant.util.Constant;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class ForgotPasswordActivity extends AppCompatActivity {
    private ActivityForgotPasswordBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        binding = ActivityForgotPasswordBinding.inflate(getLayoutInflater());
        super.onCreate(savedInstanceState);
        setContentView(binding.getRoot());

        sendOtp();
    }

    private void sendOtp() {
        binding.buttonSend.setOnClickListener(v -> {
            if (binding.inputEmail.getText() == null || binding.inputEmail.getText().toString().trim().equals("")) {
                Toast.makeText(this, "Nhập email!", Toast.LENGTH_SHORT).show();
                return;
            }
            try {
                getOtp();
            } catch (JSONException e) {
                e.printStackTrace();
            }
        });
    }

    private void getOtp() throws JSONException {
        String url = Constant.URL_DEV + "/auth/forgot-password";
//        binding.recycleViewListFoods.setVisibility(View.GONE);
        binding.pbLoading.setVisibility(View.VISIBLE);
        binding.buttonSend.setVisibility(View.INVISIBLE);
        RequestQueue queue = Volley.newRequestQueue(ForgotPasswordActivity.this);

        JSONObject jsonRequest = new JSONObject();
        jsonRequest.put("email", binding.inputEmail.getText().toString().trim());

        Log.d("CheckMail", binding.inputEmail.toString().trim());

        JsonObjectRequest sr = new JsonObjectRequest(Request.Method.POST, url, jsonRequest, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    String otp = response.getString("data");
                    binding.pbLoading.setVisibility(View.INVISIBLE);
                    binding.buttonSend.setVisibility(View.VISIBLE);

                    Intent intent = new Intent(getApplicationContext(), ConfirmOTPActivity.class);
                    intent.putExtra(Constant.OTP, otp);
                    intent.putExtra(Constant.EMAIL, binding.inputEmail.getText().toString().trim());
                    startActivity(intent);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(ForgotPasswordActivity.this, error.getMessage(), Toast.LENGTH_SHORT).show();
                binding.pbLoading.setVisibility(View.INVISIBLE);
                binding.buttonSend.setVisibility(View.VISIBLE);
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