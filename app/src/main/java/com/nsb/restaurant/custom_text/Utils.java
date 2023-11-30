package com.nsb.restaurant.custom_text;

import android.content.Context;
import android.graphics.Typeface;

public class Utils {
    private static Typeface erasTypeface;

    public static Typeface getErasTypeface(Context context) {
        if(erasTypeface == null){
            erasTypeface = Typeface.createFromAsset(context.getAssets(), "fonts/ErasDemiITC.ttf");
        }
        return erasTypeface;
    }
}