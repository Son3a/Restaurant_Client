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
import com.nsb.restaurant.databinding.ActivityChangePasswordBinding;
import com.nsb.restaurant.util.Constant;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class ChangePasswordActivity extends AppCompatActivity {
    private ActivityChangePasswordBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        binding = ActivityChangePasswordBinding.inflate(getLayoutInflater());
        super.onCreate(savedInstanceState);
        setContentView(binding.getRoot());

        eventChangePassword();
    }

    private boolean isEmpty() {
        if (binding.tiePassword.getText().equals("") || binding.tieConfirmPassword.getText().equals("")) {
            return true;
        }
        return false;
    }

    private void eventChangePassword() {
        binding.btnChangePassword.setOnClickListener(v -> {
            if (isEmpty()) {
                Toast.makeText(this, "Bạn cần nhập đủ thông tin!", Toast.LENGTH_SHORT).show();
                return;
            }
            if (!binding.tiePassword.getText().toString().equals(binding.tieConfirmPassword.getText().toString())) {
                Toast.makeText(this, "Mật khẩu không khớp!", Toast.LENGTH_SHORT).show();
                return;
            }

            try {
                changePassword();
            } catch (JSONException e) {
                e.printStackTrace();
            }
        });
    }

    private void changePassword() throws JSONException {
        String url = Constant.URL_DEV + "/auth/update-password-forgot";

        binding.pbLoading.setVisibility(View.VISIBLE);
        binding.btnChangePassword.setVisibility(View.INVISIBLE);
        RequestQueue queue = Volley.newRequestQueue(ChangePasswordActivity.this);

        JSONObject jsonRequest = new JSONObject();
        jsonRequest.put("email", getIntent().getStringExtra(Constant.EMAIL));
        jsonRequest.put("newPassword", binding.tiePassword.getText().toString());
        jsonRequest.put("role", 1);


        JsonObjectRequest sr = new JsonObjectRequest(Request.Method.PUT, url, jsonRequest, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {

                binding.pbLoading.setVisibility(View.INVISIBLE);
                binding.btnChangePassword.setVisibility(View.VISIBLE);
                Toast.makeText(ChangePasswordActivity.this, "Đổi mật khẩu thành công", Toast.LENGTH_SHORT).show();

                Intent intent = new Intent(ChangePasswordActivity.this, LoginActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(ChangePasswordActivity.this, error.getMessage(), Toast.LENGTH_SHORT).show();
                binding.pbLoading.setVisibility(View.INVISIBLE);
                binding.btnChangePassword.setVisibility(View.VISIBLE);
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