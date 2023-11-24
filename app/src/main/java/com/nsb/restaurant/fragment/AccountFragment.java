package com.nsb.restaurant.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;

import com.google.android.material.button.MaterialButton;
import com.nsb.restaurant.R;
import com.nsb.restaurant.activity.user.BookingHistoryActivity;
import com.nsb.restaurant.activity.ChangeInfoActivity;
import com.nsb.restaurant.activity.user.PaymentHistoryActivity;
import com.nsb.restaurant.activity.LoginActivity;
import com.nsb.restaurant.activity.UpdatePasswordActivity;
import com.nsb.restaurant.util.Constant;
import com.nsb.restaurant.util.PreferenceManager;
import com.squareup.picasso.Picasso;

public class AccountFragment extends Fragment {
    private View accountView;
    private TextView textPaymentHistory, textBookingHistory, textChangePW, textName;
    private ImageView imageAvatar;
    private PreferenceManager preferenceManager;
    private ConstraintLayout layoutInfo;
    private MaterialButton buttonLogout;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        accountView = inflater.inflate(R.layout.fragment_account, container, false);

        init();
        setEvent();

        return accountView;
    }

    private void init() {
        textPaymentHistory = accountView.findViewById(R.id.textPaymentHistory);
        textBookingHistory = accountView.findViewById(R.id.textBookingHistory);
        textChangePW = accountView.findViewById(R.id.textUpdatePassword);
        preferenceManager = new PreferenceManager(getActivity());
        textName = accountView.findViewById(R.id.textName);
        imageAvatar = accountView.findViewById(R.id.imageAvatar);
        layoutInfo = accountView.findViewById(R.id.layoutInfo);
        buttonLogout = accountView.findViewById(R.id.buttonLogout);
    }

    private void setEvent() {
        loading();
        gotoBookingHistory();
        gotoPaymentHistory();
        changePassword();
        changeInfo();
        logout();
    }

    private void loading() {
        textName.setText(preferenceManager.getString(Constant.USER_NAME));
        if (preferenceManager.getString(Constant.AVATAR) != null && !preferenceManager.getString(Constant.AVATAR).equals("")) {
            Picasso.get().load(preferenceManager.getString(Constant.AVATAR)).into(imageAvatar);
        }
    }

    private void logout() {
        buttonLogout.setOnClickListener(v -> {
            preferenceManager.putBoolean(Constant.IS_SIGNED_IN, false);
            preferenceManager.clear();
            getActivity().finish();
            Intent intent = new Intent(getActivity(), LoginActivity.class);
            startActivity(intent);
        });
    }

    private void changeInfo() {
        layoutInfo.setOnClickListener(v -> {
            startActivity(new Intent(getActivity(), ChangeInfoActivity.class));
        });
    }

    private void changePassword() {
        textChangePW.setOnClickListener(v -> {
            startActivity(new Intent(getActivity(), UpdatePasswordActivity.class));
        });
    }

    private void gotoBookingHistory() {
        textBookingHistory.setOnClickListener(v -> {
            startActivity(new Intent(getActivity(), BookingHistoryActivity.class));
        });
    }

    private void gotoPaymentHistory() {
        textPaymentHistory.setOnClickListener(v -> {
            startActivity(new Intent(getActivity(), PaymentHistoryActivity.class));
        });
    }
}
