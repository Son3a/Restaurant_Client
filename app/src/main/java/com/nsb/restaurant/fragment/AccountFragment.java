package com.nsb.restaurant.fragment;

import android.content.Intent;
import android.content.res.ColorStateList;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.google.android.material.button.MaterialButton;
import com.nsb.restaurant.R;
import com.nsb.restaurant.activity.ChangeInfoActivity;
import com.nsb.restaurant.activity.LoginActivity;
import com.nsb.restaurant.activity.UpdatePasswordActivity;
import com.nsb.restaurant.activity.user.BookingHistoryActivity;
import com.nsb.restaurant.activity.user.MainUserActivity;
import com.nsb.restaurant.activity.user.PaymentHistoryActivity;
import com.nsb.restaurant.databinding.FragmentAccountBinding;
import com.nsb.restaurant.util.Constant;
import com.nsb.restaurant.util.PreferenceManager;
import com.squareup.picasso.Picasso;

public class AccountFragment extends Fragment {
    private FragmentAccountBinding binding;
    private PreferenceManager preferenceManager;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentAccountBinding.inflate(getLayoutInflater());

        init();
        setEvent();

        return binding.getRoot();
    }

    private void init() {
        preferenceManager = new PreferenceManager(getActivity());
    }

    private void setEvent() {
        setStateBottomSheet();
        loading();
        gotoBookingHistory();
        gotoPaymentHistory();
        changePassword();
        changeInfo();
        logout();
    }

    private void setStateBottomSheet() {
        Constant.setTransparentBottomSheet(getContext(), binding.getRoot());
        Constant.setTransparentBottomSheet(getContext(), binding.layoutContent);
        Constant.setTransparentBottomSheet(getContext(), binding.layoutInfo);
    }

    private void loading() {
        binding.textName.setText(preferenceManager.getString(Constant.USER_NAME));
        if (preferenceManager.getString(Constant.AVATAR) != null && !preferenceManager.getString(Constant.AVATAR).equals("")) {
            Picasso.get().load(preferenceManager.getString(Constant.AVATAR)).into(binding.imageAvatar);
        }
    }

    private void logout() {
        binding.buttonLogout.setOnClickListener(v -> {
            preferenceManager.putBoolean(Constant.IS_SIGNED_IN, false);
            preferenceManager.clear();
            getActivity().finish();
            Intent intent = new Intent(getActivity(), LoginActivity.class);
            startActivity(intent);
        });
    }

    private void changeInfo() {
        binding.layoutInfo.setOnClickListener(v -> {
            startActivity(new Intent(getActivity(), ChangeInfoActivity.class));
        });
    }

    private void changePassword() {
        binding.textUpdatePassword.setOnClickListener(v -> {
            startActivity(new Intent(getActivity(), UpdatePasswordActivity.class));
        });
    }

    private void gotoBookingHistory() {
        binding.textBookingHistory.setOnClickListener(v -> {
            startActivity(new Intent(getActivity(), BookingHistoryActivity.class));
        });
    }

    private void gotoPaymentHistory() {
        binding.textPaymentHistory.setOnClickListener(v -> {
            startActivity(new Intent(getActivity(), PaymentHistoryActivity.class));
        });
    }
}
