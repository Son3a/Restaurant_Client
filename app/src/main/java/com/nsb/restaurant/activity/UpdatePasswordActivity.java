package com.nsb.restaurant.activity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.nsb.restaurant.databinding.ActivityUpdatePasswordBinding;
import com.nsb.restaurant.util.Constant;
import com.nsb.restaurant.util.PreferenceManager;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class UpdatePasswordActivity extends AppCompatActivity {
    private ActivityUpdatePasswordBinding binding;
    private PreferenceManager preferenceManager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        binding = ActivityUpdatePasswordBinding.inflate(getLayoutInflater());
        super.onCreate(savedInstanceState);
        setContentView(binding.getRoot());
        preferenceManager = new PreferenceManager(UpdatePasswordActivity.this);

        clickConfirm();
    }

    private boolean isEmpty() {
        if (binding.tiePassword.getText().equals("") || binding.tiePasswordCurrent.getText().equals("") || binding.tieConfirmPassword.getText().equals("")) {
            return true;
        }
        return false;
    }

    private void clickConfirm() {
        binding.btnChangePassword.setOnClickListener(v -> {
            if (isEmpty()) {
                Toast.makeText(this, "Bạn không được để trống!", Toast.LENGTH_SHORT).show();
                return;
            }
            if (!binding.tiePassword.getText().toString().equals(binding.tieConfirmPassword.getText().toString())) {
                Toast.makeText(this, "Mật khẩu mới không khớp!", Toast.LENGTH_SHORT).show();
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
        String url = Constant.URL_DEV + "/auth/change-password";
        binding.btnChangePassword.setVisibility(View.INVISIBLE);
        binding.pbLoading.setVisibility(View.VISIBLE);

        RequestQueue queue = Volley.newRequestQueue(UpdatePasswordActivity.this);


        JSONObject jsonObject = new JSONObject();
        jsonObject.put("password", binding.tiePassword.getText().toString());
        jsonObject.put("newPassword", binding.tieConfirmPassword.getText().toString());

        JsonObjectRequest sr = new JsonObjectRequest(Request.Method.POST, url, jsonObject, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                binding.btnChangePassword.setVisibility(View.INVISIBLE);
                binding.pbLoading.setVisibility(View.INVISIBLE);

                Toast.makeText(UpdatePasswordActivity.this, "Đổi mật khẩu thành công!", Toast.LENGTH_SHORT).show();


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                binding.btnChangePassword.setVisibility(View.VISIBLE);
                binding.pbLoading.setVisibility(View.INVISIBLE);
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
        sr.setRetryPolicy(new

                DefaultRetryPolicy(
                0,
                -1,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        queue.add(sr);
    }
}