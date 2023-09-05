package com.nsb.restaurant.fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.nsb.restaurant.R;
import com.nsb.restaurant.activity.BookingHistoryActivity;
import com.nsb.restaurant.activity.ChangeInfoActivity;
import com.nsb.restaurant.activity.PaymentHistoryActivity;
import com.nsb.restaurant.activity.LoginActivity;
import com.nsb.restaurant.activity.UpdatePasswordActivity;
import com.nsb.restaurant.util.Constant;
import com.nsb.restaurant.util.PreferenceManager;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

public class AccountFragment extends Fragment {
    private View accountView;
    private TextView textProfile, textPaymentHistory, textBookingHistory, textLogout, textChangePW, textName;
    private ImageView imageAvatar;
    private PreferenceManager preferenceManager;
    private ProgressBar pbLoadingImage;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        accountView = inflater.inflate(R.layout.fragment_account, container, false);

        init();
        setEvent();

        return accountView;
    }

    private void init() {
        pbLoadingImage = accountView.findViewById(R.id.pbLoadingImage);
        textProfile = accountView.findViewById(R.id.textProfile);
        textPaymentHistory = accountView.findViewById(R.id.textPaymentHistory);
        textBookingHistory = accountView.findViewById(R.id.textBookingHistory);
        textLogout = accountView.findViewById(R.id.textLogout);
        textChangePW = accountView.findViewById(R.id.textUpdatePassword);
        preferenceManager = new PreferenceManager(getActivity());
        textName = accountView.findViewById(R.id.textName);
        imageAvatar = accountView.findViewById(R.id.imageAvatar);
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
            Picasso.get().load(preferenceManager.getString(Constant.AVATAR)).into(imageAvatar, new Callback() {
                @Override
                public void onSuccess() {
                    pbLoadingImage.setVisibility(View.GONE);
                }

                @Override
                public void onError(Exception e) {
                    pbLoadingImage.setVisibility(View.GONE);
                }
            });
        }
    }

    private void logout() {
        textLogout.setOnClickListener(v -> {
            preferenceManager.putBoolean(Constant.IS_SIGNED_IN, false);
            preferenceManager.clear();
            getActivity().finish();
            Intent intent = new Intent(getActivity(), LoginActivity.class);
            startActivity(intent);
        });
    }

    private void changeInfo() {
        textProfile.setOnClickListener(v -> {
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
