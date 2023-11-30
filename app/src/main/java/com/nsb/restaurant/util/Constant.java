package com.nsb.restaurant.util;

import android.app.Activity;
import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Rect;
import android.util.Log;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.inputmethod.InputMethodManager;

import androidx.core.content.ContextCompat;

import com.nsb.restaurant.R;
import com.nsb.restaurant.activity.user.MainUserActivity;
import com.nsb.restaurant.listener.EventKeyboard;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;

public class Constant {
    public static final String URL_DEV = "https://restaurant-server-ba.onrender.com/api/v1";
    public static final String URL_LOCAL = "http://localhost:5000/api/v1";
    public static final String CLIENT_ID = "AZCGqVmSdvQe2VYD5APynpWfuU2xwBUg0WrNL10suXeeENCgbYI5ea3THozfZISdVENXgVyo6z3SV59B";
    public static final String CLIENT_SECRET = "EGrLhcp6-RDJQzX7FDln8bACmDdm4skzeEu4uptYk0zv1cx6pACNFqIZ3WX3tWboGZpwSgF_qk5yKG2L";
    public static final String WAITING_BOOKING_TABLE = "Chờ duyệt";
    public static final String ID_BOOKING = "idBooking";
    public static final String TRANSFER = "Chuyển khoản";
    public static final String CASH = "Tiền mặt";
    public static final String BOOKING_OBJECT = "Booking";
    public static final String DEPOSIT_OBJECT = "depositObject";
    public static final String STATUS_CONFIRMED = "Đã duyệt";
    public static final String STATUS_CANCELED = "Đã hủy";
    public static final String STATUS_WAITING = "Chờ duyệt";
    public static final String STATUS_RECEPTION = "Tiếp khách";
    public static final String ID_USER = "idUser";
    public static final String STATUS_END = "Kết thúc";
    public static final String IS_HISTORY_ACTIVITY = "isHistoryActivity";
    public static final String IS_SIGNED_IN = "isSignIn";
    public static final String KEY_PREFERENCE_NAME = "chatAppPreference";
    public static final String TOKEN = "token";
    public static final String OTP = "otp";
    public static final String EMAIL = "email";
    public static final String IS_ADD_FOOD = "idAddFood";
    public static final String USER_NAME = "userName";
    public static final String PHONE_NUM = "phoneNum";
    public static final String AVATAR = "avatar";
    public static final String IS_BOOKING = "isBooking";
    public static final String FOOD_MODEL = "foodModel";

    public static String formatSalary(String salary) {

        NumberFormat df = NumberFormat.getCurrencyInstance();
        DecimalFormatSymbols dfs = new DecimalFormatSymbols();
        dfs.setGroupingSeparator('.');
        df.setMaximumFractionDigits(0);
        dfs.setCurrencySymbol("");
        ((DecimalFormat) df).setDecimalFormatSymbols(dfs);
        String result = df.format(Integer.parseInt(salary));
        Log.d("Sla", result);
        return result;
    }

    public static String convertToUsMoney(int vndMoney) {
        float priceUsMoney = (float) 0.000042;
        float convertMoney = Math.round((vndMoney * priceUsMoney) * 100) / 100;
        return String.valueOf(convertMoney);
    }

    public static String formatTimeDDMMYYYY(String time) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
        SimpleDateFormat output = new SimpleDateFormat("dd/MM/yyyy HH:mm");

        String newTime = null;
        try {
            newTime = output.format(sdf.parse(time));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return newTime;
    }

    public static void hideKeyboardFrom(Context context, View view) {
        InputMethodManager imm = (InputMethodManager) context.getSystemService(Activity.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    public static void eventKeyBoard(View view, EventKeyboard eventKeyboard) {
        view.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                Rect r = new Rect();
                view.getWindowVisibleDisplayFrame(r);
                int screenHeight = view.getRootView().getHeight();

                // r.bottom is the position above soft keypad or device button.
                // if keypad is shown, the r.bottom is smaller than that before.
                int keypadHeight = screenHeight - r.bottom;

                if (keypadHeight > screenHeight * 0.15) { // 0.15 ratio is perhaps enough to determine keypad height.
                    // keyboard is opened
                    eventKeyboard.showKeyboard();
                } else {
                    // keyboard is closed
                    eventKeyboard.hideKeyboard();
                }
            }
        });
    }

    public static void setTransparentBottomSheet(Context context, View v) {
        v.setOnTouchListener((view, motionEvent) -> {
            MainUserActivity.binding.bottomNavigationView.setBackgroundResource(R.drawable.background_bottom_sheet_transparent);
            MainUserActivity.binding.bottomNavigationView.setItemIconTintList(ColorStateList.valueOf(ContextCompat.getColor(context, R.color.primary_opacity)));
            MainUserActivity.binding.cvScanQRCode.setBackgroundTintList(ColorStateList.valueOf(ContextCompat.getColor(context, R.color.primary_opacity)));
            return false;
        });
    }
}
