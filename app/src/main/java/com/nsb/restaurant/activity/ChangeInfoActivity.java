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
    private String urlImage = "";
    private String pathImage = "";
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
                        pathImage = response.getJSONArray("data").getJSONObject(0).getString("HINHANH").trim();
                        urlImage = response.getJSONArray("data").getJSONObject(0).getString("HINHANH").trim();
                        Picasso.get().load(response.getJSONArray("data").getJSONObject(0).getString("HINHANH").trim())
                                .into(binding.imageProfile, new Callback() {
                                    @Override
                                    public void onSuccess() {
                                        binding.pbLoadingImage.setVisibility(View.GONE);
                                    }

                                    @Override
                                    public void onError(Exception e) {
                                        binding.pbLoadingImage.setVisibility(View.GONE);
                                    }
                                });
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

    private void back() {
        binding.imageBack.setOnClickListener(v -> {
            onBackPressed();
        });
    }

    private boolean isEmpty() {
        if (binding.inputAddress.getText().toString().equals("") || binding.inputFName.getText().toString().equals("") ||
                binding.radioGroup.getCheckedRadioButtonId() == -1 || binding.inputLName.getText().toString().equals("") ||
                binding.inputPhone.getText().toString().equals("") ||
                pathImage.isEmpty()) {
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
            if (isChangeAvatar) {
                uploadImageToCloud(new File(pathImage));
            }
            while (urlImage.equals("")) {
                Log.d("Process", "Waiting upload");
            }

            try {
                changeInfo();
            } catch (JSONException e) {
                e.printStackTrace();
            }
        });
    }

    private void uploadImageToCloud(File file) {
        if (!isChangeAvatar) return;
        Log.d("Process", "Uploading...");
        binding.progressBar.setVisibility(View.VISIBLE);
        binding.buttonConfirm.setVisibility(View.GONE);

        Executors.newSingleThreadExecutor().execute(new Runnable() {
            @Override
            public void run() {
                try {
                    Cloudinary cloudinary = new Cloudinary(ObjectUtils.asMap(
                            "cloud_name", "dnstykqpa",
                            "api_key", "592721243373484",
                            "api_secret", "II9-bCcD8CkphOGPwwiClJLx7zQ"));

                    Map uploadResult = cloudinary.uploader().upload((file),
                            ObjectUtils.asMap("public_id", file.getName()));
                    urlImage = uploadResult.get("url").toString();
                    pathImage = uploadResult.get("url").toString();
                    isChangeAvatar = false;
                    preferenceManager.putString(Constant.AVATAR, urlImage);
                    Log.d("Process", "Upload successfully");
                } catch (Exception e) {
                    Log.d("ErrorUpload", e.getMessage());
                    e.printStackTrace();
                }
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
                            binding.textAddImage.setVisibility(View.GONE);
                            pathImage = RealPathUtil.getPath(ChangeInfoActivity.this, imageUri);
                            isChangeAvatar = true;
                            //uploadImageToCloud(new File(RealPathUtil.getPath(SignUpActivity.this, imageUri)));
                        } catch (FileNotFoundException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
    );

    private void changeInfo() throws JSONException {
        if (urlImage.equals("")) return;
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

        jsonObject.put("avatar", urlImage);

        JsonObjectRequest sr = new JsonObjectRequest(Request.Method.POST, url, jsonObject, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                binding.buttonConfirm.setVisibility(View.VISIBLE);
                binding.progressBar.setVisibility(View.GONE);
                Log.d("Process", "Change successfully!");
                Toast.makeText(ChangeInfoActivity.this, "Thay đổi thành công!", Toast.LENGTH_SHORT).show();
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