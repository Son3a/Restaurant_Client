package com.nsb.restaurant.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.button.MaterialButton;
import com.nsb.restaurant.R;
import com.nsb.restaurant.activity.user.OrderFoodActivity;
import com.nsb.restaurant.adapter.BookingNowAdapter;
import com.nsb.restaurant.listener.BookingListener;
import com.nsb.restaurant.model.BookingModel;
import com.nsb.restaurant.util.Constant;
import com.nsb.restaurant.util.PreferenceManager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class OrderFoodFragment extends Fragment implements BookingListener {
    private View orderFoodView;
    private RecyclerView recyclerViewBooking;
    private List<BookingModel> bookingModelList;
    private BookingNowAdapter bookingNowAdapter;
    private PreferenceManager preferenceManager;
    private ProgressBar pbLoading;
    private MaterialButton buttonBooking;
    private BookingListener listener;

    public interface BookingListener{
        void gotoBooking();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        orderFoodView = inflater.inflate(R.layout.fragment_order_food, container, false);

        init();
        setEvent();

        return orderFoodView;
    }

    private void init() {
        recyclerViewBooking = orderFoodView.findViewById(R.id.recycleBooking);
        setAdapter();
        preferenceManager = new PreferenceManager(getActivity());
        pbLoading = orderFoodView.findViewById(R.id.pbLoading);
    }

    private void setEvent() {
        getBookingNow();
    }

    private void setAdapter() {
        bookingModelList = new ArrayList<>();
        bookingNowAdapter = new BookingNowAdapter(bookingModelList, this);
        recyclerViewBooking.setAdapter(bookingNowAdapter);
    }

    private void getBookingNow() {
        String url = Constant.URL_DEV + "/booking/get-booking-now";
        pbLoading.setVisibility(View.VISIBLE);
        RequestQueue queue = Volley.newRequestQueue(getActivity());

        JsonObjectRequest sr = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    JSONArray jsonArray = response.getJSONArray("data");
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                        bookingModelList.add(new BookingModel(
                                jsonObject.getString("MAPD"),
                                jsonObject.getString("NGAYDAT"),
                                jsonObject.getString("SONGUOI"),
                                jsonObject.getString("TRANGTHAI")
                        ));
                    }
                    bookingNowAdapter.notifyDataSetChanged();
                    pbLoading.setVisibility(View.GONE);

                } catch (JSONException e) {
                    e.printStackTrace();
                    pbLoading.setVisibility(View.GONE);
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("Error", error.toString());
                Toast.makeText(getActivity(),  error.toString(), Toast.LENGTH_SHORT).show();
                pbLoading.setVisibility(View.GONE);
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

    @Override
    public void onClickBooking(BookingModel bookingModel) {
        Intent intent = new Intent(getActivity(), OrderFoodActivity.class);
        intent.putExtra(Constant.ID_BOOKING, bookingModel.getIdBooking());
        startActivity(intent);
    }
}
