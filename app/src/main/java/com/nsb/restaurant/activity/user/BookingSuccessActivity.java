package com.nsb.restaurant.activity.user;

import androidx.appcompat.app.AppCompatActivity;

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
import com.nsb.restaurant.databinding.ActivityBookingSuccessBinding;
import com.nsb.restaurant.fragment.BookingFragment;
import com.nsb.restaurant.model.BookingModel;
import com.nsb.restaurant.util.Constant;
import com.nsb.restaurant.util.PreferenceManager;

import org.json.JSONException;
import org.json.JSONObject;

import java.net.HttpURLConnection;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Map;

public class BookingSuccessActivity extends AppCompatActivity {
    ActivityBookingSuccessBinding binding;
    private PreferenceManager preferenceManager;
    BookingModel bookingModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        binding = ActivityBookingSuccessBinding.inflate(getLayoutInflater());
        super.onCreate(savedInstanceState);
        setContentView(binding.getRoot());
        preferenceManager = new PreferenceManager(BookingSuccessActivity.this);
        bookingModel = (BookingModel) getIntent().getSerializableExtra(Constant.BOOKING_OBJECT);
//        try {
//            booking();
//        } catch (JSONException e) {
//            e.printStackTrace();
//        }
        loading();
        back();
        eventCancelBooking();
    }

    private void loading() {
        if ((bookingModel.getStatus().equalsIgnoreCase(Constant.STATUS_CONFIRMED)|| bookingModel.getStatus().equalsIgnoreCase(Constant.STATUS_WAITING)) &&
                getIntent().getExtras().containsKey(Constant.IS_HISTORY_ACTIVITY)) {
            binding.buttonCancel.setVisibility(View.VISIBLE);
            binding.buttonBack.setVisibility(View.GONE);
        } else {
            binding.buttonCancel.setVisibility(View.GONE);
            binding.buttonBack.setVisibility(View.VISIBLE);
        }
        Log.d("TimeCC", bookingModel.getCreatedTime() + "         " + bookingModel.getTimeBooking());

        if(getIntent().getExtras().containsKey(Constant.IS_HISTORY_ACTIVITY)){
            binding.textTime.setText(formatTimeHHMM1(bookingModel.getCreatedTime()));
            binding.textDate.setText(formatTimeDDMMYYYY1(bookingModel.getCreatedTime()));
        } else {
            binding.textTime.setText(formatTimeHHMM(bookingModel.getCreatedTime()));
            binding.textDate.setText(formatTimeDDMMYYYY(bookingModel.getCreatedTime()));
        }

        Log.d("CheckTime", bookingModel.getCreatedTime()+'\n' + bookingModel.getTimeBooking());


        binding.textTable.setText(bookingModel.getTableName());
        binding.textTimeBooking.setText(Constant.formatTimeDDMMYYYY(bookingModel.getTimeBooking()));
        binding.textAmount.setText(bookingModel.getNumOfPeople() + " người");
        binding.textName.setText(bookingModel.getNameClient());
        binding.textPhone.setText(bookingModel.getPhoneNumClient());
        binding.textEmail.setText(bookingModel.getEmailClient());
        binding.textNote.setText(BookingFragment.stringNote);
        binding.textStatus.setText(bookingModel.getStatus());
    }

    private void back() {
        binding.buttonBack.setOnClickListener(v -> {
            finish();
        });
    }


    private String formatTimeDDMMYYYY(String time) {
        String newTime = null;
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
            SimpleDateFormat output = new SimpleDateFormat("dd/MM/yyyy");
            newTime = output.format(sdf.parse(time));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return newTime;
    }

    private String formatTimeHHMM(String time) {
        String newTime = null;
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
            SimpleDateFormat output = new SimpleDateFormat("HH:mm");
            newTime = output.format(sdf.parse(time));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return newTime;
    }

    private String formatTimeDDMMYYYY1(String time) {
        String newTime = null;
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
            SimpleDateFormat output = new SimpleDateFormat("dd/MM/yyyy");
            newTime = output.format(sdf.parse(time));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return newTime;
    }

    private String formatTimeHHMM1(String time) {
        String newTime = null;
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
            SimpleDateFormat output = new SimpleDateFormat("HH:mm");
            newTime = output.format(sdf.parse(time));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return newTime;
    }

    private void eventCancelBooking(){
        binding.buttonCancel.setOnClickListener(v->{
            try {
                cancelBooking();
            } catch (JSONException e) {
                e.printStackTrace();
            }
        });
    }

    private void cancelBooking() throws JSONException {
        String url = Constant.URL_DEV + "/booking/cancel-booking";
        RequestQueue queue = Volley.newRequestQueue(BookingSuccessActivity.this);

        binding.buttonCancel.setVisibility(View.INVISIBLE);
        binding.pbLoading.setVisibility(View.VISIBLE);

        JSONObject jsonRequest = new JSONObject();
        jsonRequest.put("idBooking", bookingModel.getIdBooking());



        JsonObjectRequest sr = new JsonObjectRequest(Request.Method.PUT, url, jsonRequest, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                binding.buttonCancel.setVisibility(View.VISIBLE);
                binding.pbLoading.setVisibility(View.INVISIBLE);
                Toast.makeText(BookingSuccessActivity.this, "Hủy thành công!", Toast.LENGTH_SHORT).show();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                binding.buttonCancel.setVisibility(View.VISIBLE);
                binding.pbLoading.setVisibility(View.INVISIBLE);
                if(error.networkResponse.statusCode == HttpURLConnection.HTTP_INTERNAL_ERROR) {
                    Toast.makeText(BookingSuccessActivity.this, "Qúa thời gian hủy!", Toast.LENGTH_SHORT).show();
                }

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