package com.nsb.restaurant.util;

import android.util.Log;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;

public class Constant {
    public static final String URL_DEV = "https://restaurant-server-ba.onrender.com/api/v1";
    //public static final String URL_DEV = "http://192.168.137.1:5000/api/v1";
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

    public static String formatSalary(String salary) {
        Log.d("Sla", salary);
        NumberFormat df = NumberFormat.getCurrencyInstance();
        DecimalFormatSymbols dfs = new DecimalFormatSymbols();
        dfs.setGroupingSeparator('.');
        df.setMaximumFractionDigits(0);
        dfs.setCurrencySymbol("");
        ((DecimalFormat) df).setDecimalFormatSymbols(dfs);
        String result = df.format(Integer.parseInt(salary));
        return result.substring(0, result.length() - 1);
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
}
