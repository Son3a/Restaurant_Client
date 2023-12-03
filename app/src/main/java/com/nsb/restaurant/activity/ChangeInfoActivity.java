package com.nsb.restaurant.activity;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.webkit.URLUtil;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import com.nsb.restaurant.activity.user.MainUserActivity;
import com.nsb.restaurant.databinding.ActivityChangeInfoBinding;
import com.nsb.restaurant.model.FoodModel;
import com.nsb.restaurant.util.Constant;
import com.nsb.restaurant.util.PreferenceManager;
import com.nsb.restaurant.util.RealPathUtil;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


public class ChangeInfoActivity extends AppCompatActivity {
    private ActivityChangeInfoBinding binding;
    private String encodeImage;
    private PreferenceManager preferenceManager;
    private boolean isChangeAvatar = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        binding = ActivityChangeInfoBinding.inflate(getLayoutInflater());
        super.onCreate(savedInstanceState);
        setContentView(binding.getRoot());
        preferenceManager = new PreferenceManager(ChangeInfoActivity.this);

        try {
            loading();
        } catch (JSONException e) {
            e.printStackTrace();
        }

        back();
        getFileMedia();
        eventConfirm();
    }

    private void loading() throws JSONException {
        String url = Constant.URL_DEV + "/auth/get-info-user";
        binding.pbLoading1.setVisibility(View.VISIBLE);
        RequestQueue queue = Volley.newRequestQueue(ChangeInfoActivity.this);

        JsonObjectRequest sr = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    binding.inputFName.setText(response.getJSONArray("data").getJSONObject(0).getString("HO").trim());
                    binding.inputLName.setText(response.getJSONArray("data").getJSONObject(0).getString("TEN").trim());
                    if (response.getJSONArray("data").getJSONObject(0).getString("GIOITINH").trim().equalsIgnoreCase("Nam")) {
                        binding.rbMan.setChecked(true);
                    } else if (response.getJSONArray("data").getJSONObject(0).getString("GIOITINH").trim().equalsIgnoreCase("Nữ")) {
                        binding.rbWoMan.setChecked(true);
                    }
                    if (!response.getJSONArray("data").getJSONObject(0).getString("DIACHI").trim().equals("null")) {
                        binding.inputAddress.setText(response.getJSONArray("data").getJSONObject(0).getString("DIACHI").trim());
                    }

                    binding.inputPhone.setText(response.getJSONArray("data").getJSONObject(0).getString("SDT").trim());

                    if (!response.getJSONArray("data").getJSONObject(0).getString("HINHANH").trim().equals("")) {
                        binding.textAddImage.setVisibility(View.INVISIBLE);
                        encodeImage = response.getJSONArray("data").getJSONObject(0).getString("HINHANH").trim();
                        if (!URLUtil.isValidUrl(encodeImage)) {
                            binding.imageProfile.setImageBitmap(Constant.getBitmapFromEncodedString(encodeImage));
                        } else {
                            Picasso.get().load(encodeImage).into(binding.imageProfile);
                        }

                    }

                    binding.pbLoading1.setVisibility(View.GONE);
                    binding.layoutContent.setVisibility(View.VISIBLE);
                } catch (JSONException e) {
                    Log.d("Error", e.getMessage());
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("Error", error.toString());
                binding.pbLoading1.setVisibility(View.GONE);
            }
        }) {

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap<String, String>();
                headers.put("Content-Type", "application/json");
                headers.put("authorization", preferenceManager.getString(Constant.TOKEN));
                return headers;
            }
        };
        sr.setRetryPolicy(new DefaultRetryPolicy(
                0,
                -1,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        queue.add(sr);
    }

    private void back() {
        binding.imageBack.setOnClickListener(v -> {
            onBackPressed();
        });
    }

    private boolean isEmpty() {
        if (binding.inputAddress.getText().toString().equals("") || binding.inputFName.getText().toString().equals("") ||
                binding.radioGroup.getCheckedRadioButtonId() == -1 || binding.inputLName.getText().toString().equals("") ||
                binding.inputPhone.getText().toString().equals("") ||
                encodeImage.isEmpty()) {
            return true;
        }
        return false;
    }

    private void eventConfirm() {
        binding.buttonConfirm.setOnClickListener(v -> {
            if (isEmpty()) {
                Toast.makeText(this, "Bạn cần điền đủ thông tin!", Toast.LENGTH_SHORT).show();
                return;
            }
            try {
                changeInfo();
            } catch (JSONException e) {
                e.printStackTrace();
            }
        });
    }

    private void getFileMedia() {
        binding.imageProfile.setOnClickListener(v -> {
            Intent i = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            i.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            pickImage.launch(i);
        });
    }

    private final ActivityResultLauncher<Intent> pickImage = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if (result.getResultCode() == RESULT_OK) {
                    if (result.getData() != null) {
                        Uri imageUri = result.getData().getData();
                        try {
                            InputStream inputStream = getContentResolver().openInputStream(imageUri);
                            Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
                            binding.imageProfile.setImageBitmap(bitmap);
                            encodeImage = Constant.encodeImage(bitmap);
                            Log.d("endCode", encodeImage);
                        } catch (FileNotFoundException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
    );

    private void changeInfo() throws JSONException {
        if (encodeImage.equals("")) return;
        Log.d("Process", "Changing...");
        String url = Constant.URL_DEV + "/auth/update-info-user";
        binding.progressBar.setVisibility(View.VISIBLE);
        binding.buttonConfirm.setVisibility(View.GONE);
        RequestQueue queue = Volley.newRequestQueue(ChangeInfoActivity.this);

        JSONObject jsonObject = new JSONObject();
        jsonObject.put("fName", binding.inputFName.getText().toString().trim());
        jsonObject.put("lName", binding.inputLName.getText().toString().trim());
        jsonObject.put("numPhone", binding.inputPhone.getText().toString().trim());
        jsonObject.put("address", binding.inputAddress.getText().toString().trim());
        if (binding.rbMan.isChecked()) {
            jsonObject.put("gender", "Nam");
        } else if (binding.rbWoMan.isChecked()) {
            jsonObject.put("gender", "Nữ");
        }

        jsonObject.put("avatar", encodeImage);

        JsonObjectRequest sr = new JsonObjectRequest(Request.Method.POST, url, jsonObject, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                binding.buttonConfirm.setVisibility(View.VISIBLE);
                binding.progressBar.setVisibility(View.GONE);
                Log.d("Process", "Change successfully!");
                Toast.makeText(ChangeInfoActivity.this, "Thay đổi thành công!", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(ChangeInfoActivity.this, MainUserActivity.class);
                intent.putExtra(Constant.AVATAR, encodeImage);
                intent.putExtra(Constant.NAME_USER, binding.inputFName.getText().toString().trim() + binding.inputLName.getText().toString().trim());
                setResult(RESULT_OK, intent);
                finish();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("Error", error.toString());
                binding.buttonConfirm.setVisibility(View.VISIBLE);
                binding.progressBar.setVisibility(View.GONE);
            }
        }) {

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap<String, String>();
                headers.put("Content-Type", "application/json");
                Log.d("AuthChange",preferenceManager.getString(Constant.TOKEN));
                headers.put("authorization", preferenceManager.getString(Constant.TOKEN));
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