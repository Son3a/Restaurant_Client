package com.nsb.restaurant.util;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;

import com.nsb.restaurant.R;

public class LoadingDialog {
    Context context;
    Dialog dialog;

    public LoadingDialog(Context context) {
        this.context = context;
    }

    public void showDialog() {
        dialog = new Dialog(context);
        dialog.setContentView(R.layout.layout_loading);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.setCancelable(false);
        dialog.create();
        dialog.show();
    }

    public void hideDialog() {
        if (dialog != null) {
            dialog.dismiss();
        }
    }
}
