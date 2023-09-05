package com.nsb.restaurant.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.viewmodel.CreationExtras;

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
import com.nsb.restaurant.databinding.ActivityLoginBinding;
import com.nsb.restaurant.model.FoodModel;
import com.nsb.restaurant.util.Constant;
import com.nsb.restaurant.util.PreferenceManager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;


public class LoginActivity extends AppCompatActivity {
    private ActivityLoginBinding binding;
    private PreferenceManager preferenceManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        binding = ActivityLoginBinding.inflate(getLayoutInflater());
        super.onCreate(savedInstanceState);
        setContentView(binding.getRoot());
        preferenceManager = new PreferenceManager(LoginActivity.this);
        if (preferenceManager.getBoolean(Constant.IS_SIGNED_IN)) {
            finish();
            startActivity(new Intent(getApplicationContext(), MainActivity.class));
        }


        gotoSignUp();
        forgotPassword();
        confirmLogin();
    }

    private void gotoSignUp() {
        binding.textRegister.setOnClickListener(v -> {
            startActivity(new Intent(getApplicationContext(), SignUpActivity.class));
        });
    }

    private void forgotPassword() {
        binding.textForgotPassword.setOnClickListener(v -> {
            startActivity(new Intent(getApplicationContext(), ForgotPasswordActivity.class));
        });
    }

    private boolean isEmpty() {
        if (binding.textEmail.getText().toString().equals("") || binding.edtPassword.getText().toString().equals("")) {
            return true;
        }
        return false;
    }

    private void confirmLogin() {
        binding.buttonLogin.setOnClickListener(v -> {
            Log.d("Tesstt", binding.textEmail.getText().toString());
            if (isEmpty()) {
                Toast.makeText(this, "Bạn phải nhập đầu đủ email và password!", Toast.LENGTH_SHORT).show();
                return;
            }

            try {
                login();
            } catch (JSONException e) {
                e.printStackTrace();
            }
        });
    }

    private void login() throws JSONException {
        String url = Constant.URL_DEV + "/auth/login";
        binding.buttonLogin.setVisibility(View.INVISIBLE);
        binding.pbLoading.setVisibility(View.VISIBLE);

        String email = binding.textEmail.getText().toString().trim();
        String password = binding.edtPassword.getText().toString().trim();

        Log.d("Check", email + "  " + password);

        JSONObject jsonRequest = new JSONObject();
        jsonRequest.put("email", email);
        jsonRequest.put("password", password);
        jsonRequest.put("role", "1");

        RequestQueue queue = Volley.newRequestQueue(LoginActivity.this);


        JsonObjectRequest sr = new JsonObjectRequest(Request.Method.POST, url, jsonRequest, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    preferenceManager.putBoolean(Constant.IS_SIGNED_IN, true);
                    preferenceManager.putString(Constant.TOKEN, response.getString("token").toString());
                    preferenceManager.putString(Constant.EMAIL, response.getJSONArray("data").getJSONObject(0).getString("EMAIL").toString().trim());
                    preferenceManager.putString(Constant.USER_NAME, response.getJSONArray("data").getJSONObject(0).getString("USERNAME").toString().trim());
                    preferenceManager.putString(Constant.PHONE_NUM, response.getJSONArray("data").getJSONObject(0).getString("SDT"));
                    preferenceManager.putString(Constant.AVATAR, response.getJSONArray("data").getJSONObject(0).getString("HINHANH"));
                    preferenceManager.putString(Constant.ID_USER, response.getJSONArray("data").getJSONObject(0).getString("USERID"));
                    binding.buttonLogin.setVisibility(View.VISIBLE);
                    binding.pbLoading.setVisibility(View.INVISIBLE);
                    Toast.makeText(LoginActivity.this, "Login Successful", Toast.LENGTH_SHORT).show();

                    Log.d("Token", response.getString("token").toString());

                    //finish();

                    Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);
                } catch (JSONException e) {
                    Log.d("Error", e.getMessage());
                    binding.buttonLogin.setVisibility(View.VISIBLE);
                    binding.pbLoading.setVisibility(View.INVISIBLE);
                    Toast.makeText(LoginActivity.this, "Email hoặc mật khẩu không đúng!", Toast.LENGTH_SHORT).show();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                binding.buttonLogin.setVisibility(View.VISIBLE);
                binding.pbLoading.setVisibility(View.INVISIBLE);
                Toast.makeText(LoginActivity.this, "Email hoặc password không đúng!", Toast.LENGTH_SHORT).show();
                Log.d("Error", error.toString());
            }
        }) {

            @Override
            public Map<String, String> getHeaders() {
                HashMap<String, String> headers = new HashMap<String, String>();
                headers.put("Content-Type", "application/json");
                return headers;
            }
        };
        sr.setRetryPolicy(new DefaultRetryPolicy() {
            @Override
            public int getCurrentTimeout() {
                return 50000;
            }

            @Override
            public int getCurrentRetryCount() {
                return 50000;
            }

            @Override
            public void retry(VolleyError error) {

                binding.buttonLogin.setVisibility(View.VISIBLE);
                binding.pbLoading.setVisibility(View.INVISIBLE);
                Toast.makeText(LoginActivity.this, "Email hoặc password không đúng!", Toast.LENGTH_SHORT).show();
            }
        });
        queue.add(sr);
    }

}