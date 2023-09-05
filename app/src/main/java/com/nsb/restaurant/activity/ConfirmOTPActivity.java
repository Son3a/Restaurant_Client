package com.nsb.restaurant.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
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
import com.nsb.restaurant.databinding.ActivityConfirmOtpactivityBinding;
import com.nsb.restaurant.util.Constant;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class ConfirmOTPActivity extends AppCompatActivity {
    private ActivityConfirmOtpactivityBinding binding;
    private String OTP = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        binding = ActivityConfirmOtpactivityBinding.inflate(getLayoutInflater());
        super.onCreate(savedInstanceState);
        setContentView(binding.getRoot());

        OTP = getIntent().getStringExtra(Constant.OTP);

        eventSenAgain();

        eventConfirmOTP();
    }

    private void eventSenAgain() {
        binding.buttonSendAgain.setOnClickListener(v -> {
            try {
                sendAgainOTP();
            } catch (JSONException e) {
                e.printStackTrace();
            }
        });
    }

    private void eventConfirmOTP() {
        binding.buttonSend.setOnClickListener(v -> {
            confirmOtp();
        });
    }

    private void confirmOtp() {
        if (binding.textOTP.getText() == null || binding.textOTP.getText().toString().trim().equals("")) {
            Toast.makeText(this, "Nhập mã OTP!", Toast.LENGTH_SHORT).show();
            return;
        }
        if (OTP.equals(binding.textOTP.getText().toString().trim())) {
            Intent intent = new Intent(ConfirmOTPActivity.this, ChangePasswordActivity.class);
            intent.putExtra(Constant.EMAIL, getIntent().getStringExtra(Constant.EMAIL));
            startActivity(intent);
        } else {
            Toast.makeText(this, "Mã OTP không chính xác!", Toast.LENGTH_SHORT).show();
        }
    }

    private void sendAgainOTP() throws JSONException {
        OTP = "";
        String url = Constant.URL_DEV + "/auth/forgot-password";

        binding.pbLoading1.setVisibility(View.VISIBLE);
        binding.buttonSendAgain.setVisibility(View.INVISIBLE);
        RequestQueue queue = Volley.newRequestQueue(ConfirmOTPActivity.this);

        JSONObject jsonRequest = new JSONObject();
        jsonRequest.put("email", getIntent().getStringExtra(Constant.EMAIL));


        JsonObjectRequest sr = new JsonObjectRequest(Request.Method.POST, url, jsonRequest, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    OTP = response.getString("data");
                    binding.pbLoading1.setVisibility(View.INVISIBLE);
                    binding.buttonSendAgain.setVisibility(View.VISIBLE);
                    Toast.makeText(ConfirmOTPActivity.this, "Gửi mã thành công!", Toast.LENGTH_SHORT).show();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(ConfirmOTPActivity.this, error.getMessage(), Toast.LENGTH_SHORT).show();
                binding.pbLoading1.setVisibility(View.INVISIBLE);
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