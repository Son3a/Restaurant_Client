package com.nsb.restaurant.activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.nsb.restaurant.databinding.ActivitySignUpBinding;
import com.nsb.restaurant.util.Constant;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class SignUpActivity extends AppCompatActivity {
    private ActivitySignUpBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        binding = ActivitySignUpBinding.inflate(getLayoutInflater());
        super.onCreate(savedInstanceState);
        setContentView(binding.getRoot());

        backToLogin();
        eventConfirm();
    }

    private void backToLogin() {
        Intent intent = new Intent(this, LoginActivity.class);
        finish();
        startActivity(intent);
    }

    private boolean isEmpty() {
        if (binding.inputEmail.getText().toString().equals("") || binding.inputPassword.getText().toString().equals("") ||
                binding.inputConfirmPassword.getText().toString().equals("") || binding.inputFName.getText().toString().equals("") ||
                binding.inputLName.getText().toString().equals("") || binding.inputPhone.getText().toString().equals("")) {
            return true;
        }
        return false;
    }

    private void eventConfirm() {
        binding.buttonSignUp.setOnClickListener(v -> {
            if (isEmpty()) {
                Toast.makeText(this, "Bạn cần điền đủ thông tin!", Toast.LENGTH_SHORT).show();
                return;
            }
            if (!binding.inputPassword.getText().toString().equals(binding.inputConfirmPassword.getText().toString().trim())) {
                Toast.makeText(this, "Mật khẩu không khớp", Toast.LENGTH_SHORT).show();
                return;
            }
                try {
                    signUp();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
        });
    }

    private void signUp() throws JSONException {
        String url = Constant.URL_DEV + "/auth/register";
        binding.buttonSignUp.setVisibility(View.INVISIBLE);
        binding.pbLoading.setVisibility(View.VISIBLE);

        String email = binding.inputEmail.getText().toString().trim();
        String password = binding.inputPassword.getText().toString().trim();
        String fName = binding.inputFName.getText().toString().trim();
        String lName = binding.inputLName.getText().toString().trim();
        String phone = binding.inputPhone.getText().toString().trim();

        JSONObject jsonRequest = new JSONObject();
        jsonRequest.put("email", email);
        jsonRequest.put("password", password);
        jsonRequest.put("phoneNum", phone);
        jsonRequest.put("avatar", "");
        jsonRequest.put("idRole", "Q01");
        jsonRequest.put("fName", fName);
        jsonRequest.put("lName", lName);
        jsonRequest.put("role", "1");

        RequestQueue queue = Volley.newRequestQueue(SignUpActivity.this);


        JsonObjectRequest sr = new JsonObjectRequest(Request.Method.POST, url, jsonRequest, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                //Toast.makeText(SignUpActivity.this, "Tạo tài khoản thành công!", Toast.LENGTH_SHORT).show();
                finish();
                startActivity(new Intent(getApplicationContext(), LoginActivity.class));
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                binding.buttonSignUp.setVisibility(View.VISIBLE);
                binding.pbLoading.setVisibility(View.INVISIBLE);
                Toast.makeText(SignUpActivity.this, error.toString(), Toast.LENGTH_SHORT).show();
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
                Log.d("Error", error.toString());
            }
        });
        queue.add(sr);
    }

//    private void uploadImageToCloud(File file) {
//        new Thread(new Runnable() {
//            @Override
//            public void run() {
//                try {
//                    Cloudinary cloudinary = new Cloudinary(ObjectUtils.asMap(
//                            "cloud_name", "dnstykqpa",
//                            "api_key", "592721243373484",
//                            "api_secret", "II9-bCcD8CkphOGPwwiClJLx7zQ"));
//
//                    Map uploadResult = cloudinary.uploader().upload((file),
//                            ObjectUtils.asMap("public_id", file.getName()));
//                    urlImage = uploadResult.get("url").toString();
//                    Log.d("imageAvatar", urlImage.length() + ": " + urlImage);
//                } catch (Exception e) {
//                    Log.d("ErrorUpload", e.getMessage());
//                    e.printStackTrace();
//                }
//            }
//        }).start();
//
//    }
}