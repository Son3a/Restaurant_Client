package com.nsb.restaurant.fragment;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.widget.NestedScrollView;
import androidx.fragment.app.Fragment;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.nsb.restaurant.R;
import com.nsb.restaurant.activity.OrderFoodActivity;
import com.nsb.restaurant.model.BookingModel;
import com.nsb.restaurant.model.FoodModel;
import com.nsb.restaurant.util.Constant;
import com.nsb.restaurant.util.PreferenceManager;

import net.yslibrary.android.keyboardvisibilityevent.KeyboardVisibilityEvent;
import net.yslibrary.android.keyboardvisibilityevent.KeyboardVisibilityEventListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicBoolean;

public class BookingFragment extends Fragment {
    private View bookingView, bottomSheetAmountPeopleView, mainView;
    private BottomSheetDialog bottomSheetAmountPeople;
    private TextView textAmount, textDate, textTime;
    private EditText textNameClient, textPhone, textMail, textNote, textAmountPeople;
    private ImageButton buttonCloseBottom;
    private NestedScrollView layoutNested;
    private RadioGroup radioGroup1, radioGroup2;
    private String amountPeople;
    private Button buttonBooking, buttonAmount;
    private ProgressBar pbLoading;
    private String timeBooking;
    private String dateBooking;
    public static String timeBookingTable;
    public static String numOfPeople;
    public static List<String> idTables;
    public static String stringNote;
    public static String nameTable = "";
    PreferenceManager preferenceManager;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        bookingView = inflater.inflate(R.layout.fragment_booking, container, false);
        bottomSheetAmountPeopleView = inflater.inflate(R.layout.bottom_amount_people, container, false);
        mainView = inflater.inflate(R.layout.activity_main, container, false);
        preferenceManager = new PreferenceManager(getActivity());

        init();
        setBottomSheetAmountPeople();


        setEvent();
        return bookingView;
    }

    private void init() {
        textAmount = bookingView.findViewById(R.id.textAmount);
        buttonCloseBottom = bottomSheetAmountPeopleView.findViewById(R.id.imageClose);
        layoutNested = bookingView.findViewById(R.id.layoutNested);
        textDate = bookingView.findViewById(R.id.textDateBooking);
        textTime = bookingView.findViewById(R.id.textTimeBooking);
        textMail = bookingView.findViewById(R.id.textEmail);
        textPhone = bookingView.findViewById(R.id.textPhone);
        textNameClient = bookingView.findViewById(R.id.textName);
        textNote = bookingView.findViewById(R.id.textNote);
        buttonBooking = bookingView.findViewById(R.id.buttonBooking);
        pbLoading = bookingView.findViewById(R.id.pbLoading);
        textAmountPeople = bottomSheetAmountPeopleView.findViewById(R.id.textAmountPeople);
        buttonAmount = bottomSheetAmountPeopleView.findViewById(R.id.buttonSubmitAmount);

        radioGroup1 = bottomSheetAmountPeopleView.findViewById(R.id.radioGroup1);
        radioGroup2 = bottomSheetAmountPeopleView.findViewById(R.id.radioGroup2);
        idTables = new ArrayList<>();
    }

    private void setEvent() {
        textNameClient.setText(preferenceManager.getString(Constant.USER_NAME));
        textPhone.setText(preferenceManager.getString(Constant.PHONE_NUM));
        textMail.setText(preferenceManager.getString(Constant.EMAIL));
        openBottomSheetAmountPeople();
        closeBottomSheet();
        setRadioGroup();
        pickDate();
        pickTime();
        bookingClick();
        setDateTime();
        amountClick();
    }


    private void bookingClick() {
        buttonBooking.setOnClickListener(v -> {
            try {
                if (textAmountPeople != null && !textAmountPeople.getText().toString().equals("")) {
                    getTableToBooking();
                } else {
                    Toast.makeText(getActivity(), "Type num of people", Toast.LENGTH_SHORT).show();
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }
        });
    }

    private void getTableToBooking() throws JSONException {
        String time = dateBooking + " " + timeBooking;
        Log.d("Time", time);
        String url = Constant.URL_DEV + "/booking/get-tables-to-booking";

//        pbLoading.setVisibility(View.VISIBLE);

        RequestQueue queue = Volley.newRequestQueue(getActivity());
        JSONObject jsonRequest = new JSONObject();
        jsonRequest.put("amountPeople", textAmountPeople.getText().toString());
        jsonRequest.put("time", time);

        JsonObjectRequest sr = new JsonObjectRequest(Request.Method.POST, url, jsonRequest, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    JSONArray jsonArray = response.getJSONArray("data");

                    timeBookingTable = time;
                    numOfPeople = textAmountPeople.getText().toString();
                    stringNote = textNote != null ? textNote.getText().toString() : "";

                    nameTable = "";
                    idTables.clear();
                    for(int i = 0; i < jsonArray.length();i++){
                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                        nameTable += jsonObject.getString("tenban").trim() + " ";
                        idTables.add(jsonArray.getJSONObject(i).getString("maban"));
                    }

                    Log.d("Table", nameTable);

                    DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
                    Date date = new Date();

                    BookingModel bookingModel =  new BookingModel(
                            time,
                            numOfPeople,
                            Constant.WAITING_BOOKING_TABLE,
                            dateFormat.format(date),
                            textNameClient.getText().toString(),
                            textPhone.getText().toString(),
                            textMail.getText().toString(),
                            nameTable);

                    Intent intent = new Intent(getActivity(), OrderFoodActivity.class);
                    intent.putExtra(Constant.BOOKING_OBJECT, bookingModel);
                    startActivity(intent);

                    Log.d("IDBAN", textNameClient.getText().toString());
                } catch (JSONException e) {
                    Log.d("Error", e.getMessage());
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("Error", error.toString());
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

    private void setBottomSheetAmountPeople() {
        bottomSheetAmountPeople = new BottomSheetDialog(getActivity());
        bottomSheetAmountPeople.setContentView(bottomSheetAmountPeopleView);
    }

    private void showKeyboard() {
        KeyboardVisibilityEvent.setEventListener(
                getActivity(),
                new KeyboardVisibilityEventListener() {
                    @Override
                    public void onVisibilityChanged(boolean isOpen) {

                    }
                });
    }

    private void closeBottomSheet() {
        buttonCloseBottom.setOnClickListener(v -> {
            bottomSheetAmountPeople.dismiss();
        });
    }

    private void openBottomSheetAmountPeople() {
        textAmount.setOnClickListener(v -> {
            showKeyboard();
            bottomSheetAmountPeople.show();
        });
    }

    private void setDateTime() {
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.MINUTE, 30);

        String day = String.valueOf(calendar.get(Calendar.DAY_OF_MONTH));
        String month = String.valueOf(calendar.get(Calendar.MONTH) + 1);
        String year = String.valueOf(calendar.get(Calendar.YEAR));
        String hour = String.valueOf(calendar.get(Calendar.HOUR_OF_DAY));
        String minute = String.valueOf(calendar.get(Calendar.MINUTE));

        if (hour.length() <= 1) {
            hour = "0" + hour;
        }
        if (minute.length() <= 1) {
            minute = "0" + minute;
        }
        textTime.setText(hour + ":" + minute);
        textDate.setText(day + " tháng " + month);
        timeBooking = hour + ":" + minute;
        dateBooking = year + "/" + month + "/" + day;
    }

    private void pickDate() {
        textDate.setOnClickListener(v -> {
            Calendar calendar = Calendar.getInstance();
            int yearNow = calendar.get(Calendar.YEAR);
            int monthNow = calendar.get(Calendar.MONTH);
            int dayNow = calendar.get(Calendar.DAY_OF_MONTH);

            DatePickerDialog datePickerDialog = new DatePickerDialog(getActivity(), new DatePickerDialog.OnDateSetListener() {
                @Override
                public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                    if (checkTime(day, month + 1, year)){
                        textDate.setText(day + " tháng " + (month + 1));
                        dateBooking = year + "/" + (month + 1) + "/" + day;
                    } else {
                        textDate.setText(dayNow + " tháng " + (monthNow + 1));
                        dateBooking = yearNow + "/" + (monthNow + 1) + "/" + dayNow;
                    }
                }
            }, yearNow, monthNow, dayNow);
            datePickerDialog.show();
        });
    }

    private boolean checkTime(int day, int month, int year) {
        Calendar calendar = Calendar.getInstance();
        int yearNow = calendar.get(Calendar.YEAR);
        int monthNow = calendar.get(Calendar.MONTH) + 1;
        int dayNow = calendar.get(Calendar.DAY_OF_MONTH);
        if (year > yearNow) return true;
        if (year == yearNow && month > monthNow) return true;
        if (year == yearNow && month == monthNow && day >= dayNow) return true;
        return false;
    }

    private void pickTime() {
        textTime.setOnClickListener(v -> {
            Calendar calendar = Calendar.getInstance();
            calendar.add(Calendar.MINUTE, 30);
            int hourNow = calendar.get(Calendar.HOUR_OF_DAY);
            int minuteNow = calendar.get(Calendar.MINUTE);

            TimePickerDialog timePickerDialog = new TimePickerDialog(getActivity(), new TimePickerDialog.OnTimeSetListener() {
                @Override
                public void onTimeSet(TimePicker timePicker, int hour, int minute) {
                    String stringHour = String.valueOf(hour), stringMinute = String.valueOf(minute);
                    if (hour < 10) {
                        stringHour = "0" + stringHour;
                    }
                    if (minute < 10) {
                        stringMinute = "0" + stringMinute;
                    }
                    textTime.setText(stringHour + ":" + stringMinute);
                    timeBooking = stringHour + ":" + stringMinute;
                }
            }, hourNow, minuteNow, true);
            timePickerDialog.show();
        });

    }

    private void amountClick() {
        buttonAmount.setOnClickListener(v -> {
            if (textAmountPeople != null && !textAmountPeople.equals("")) {
                textAmount.setText(textAmountPeople.getText().toString() + " người");
                bottomSheetAmountPeople.dismiss();
            }
        });
    }

    private void setRadioGroup() {

        AtomicBoolean flag = new AtomicBoolean(true);
        radioGroup1.setOnCheckedChangeListener((group, checkedId) -> {
            if (flag.get()) {
                radioGroup2.clearCheck();
                flag.set(false);

            }
            if (group.getCheckedRadioButtonId() != -1) {
                RadioButton radioButton = (RadioButton) group.findViewById(checkedId);
                amountPeople = radioButton.getText().toString();

                int value = Integer.parseInt(amountPeople.replaceAll("[^0-9]", ""));
                textAmountPeople.setText(String.valueOf(value));
            }

        });
        radioGroup2.setOnCheckedChangeListener((group, checkedId) -> {
            if (!flag.get()) {
                radioGroup1.clearCheck();
                flag.set(true);

            }
            if (group.getCheckedRadioButtonId() != -1) {
                RadioButton radioButton = (RadioButton) group.findViewById(checkedId);
                amountPeople = radioButton.getText().toString();

                int value = Integer.parseInt(amountPeople.replaceAll("[^0-9]", ""));
                textAmountPeople.setText(String.valueOf(value));
            }

        });
    }
}
