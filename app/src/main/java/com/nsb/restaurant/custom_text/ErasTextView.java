package com.nsb.restaurant.custom_text;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatTextView;

public class ErasTextView extends AppCompatTextView {
    public ErasTextView(@NonNull Context context) {
        super(context);
        setFontsTextView();
    }

    public ErasTextView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        setFontsTextView();
    }

    public ErasTextView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        setFontsTextView();
    }

    private void setFontsTextView() {
        Typeface typeface = Utils.getErasTypeface(getContext());
        setTypeface(typeface);
    }
}
