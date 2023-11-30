package com.nsb.restaurant.activity.user;

import androidx.activity.result.ActivityResultLauncher;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import android.content.DialogInterface;
import android.content.res.ColorStateList;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.google.android.material.navigation.NavigationBarView;
import com.journeyapps.barcodescanner.CaptureActivity;
import com.journeyapps.barcodescanner.ScanContract;
import com.journeyapps.barcodescanner.ScanOptions;
import com.nsb.restaurant.R;
import com.nsb.restaurant.adapter.MainUserAdapter;
import com.nsb.restaurant.databinding.ActivityMainBinding;
import com.nsb.restaurant.fragment.AccountFragment;
import com.nsb.restaurant.fragment.BookingFragment;
import com.nsb.restaurant.fragment.InfoRestaurantFragment;
import com.nsb.restaurant.fragment.MenuFragment;
import com.nsb.restaurant.fragment.OrderFoodFragment;
import com.nsb.restaurant.listener.EventKeyboard;
import com.nsb.restaurant.util.Constant;


public class MainUserActivity extends AppCompatActivity {
    public static ActivityMainBinding binding;
    private MainUserAdapter mainUserAcAdapter;
//    private InfoRestaurantFragment infoFragment;
//    private BookingFragment bookingFragment;
//    private MenuFragment menuFragment;
//    private AccountFragment accountFragment;
//    private OrderFoodFragment orderFoodFragment;
//    private Fragment activeFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        super.onCreate(savedInstanceState);
        setContentView(binding.getRoot());

        init();
        setEvent();
        setBottomNavigationView();
    }

    private void init() {
        mainUserAcAdapter = new MainUserAdapter(MainUserActivity.this);
        binding.viewPager2.setAdapter(mainUserAcAdapter);
        binding.viewPager2.setUserInputEnabled(false);
        binding.viewPager2.setOffscreenPageLimit(4);

    }

    private void setEvent() {
        clickScanner();
        hideKeyBottomSheet();
    }

    private void hideKeyBottomSheet() {
        Constant.eventKeyBoard(binding.getRoot(), new EventKeyboard() {
            @Override
            public void showKeyboard() {
                binding.bottomNavigationView.setVisibility(View.GONE);
                binding.cvScanQRCode.setVisibility(View.GONE);
            }

            @Override
            public void hideKeyboard() {
                binding.bottomNavigationView.setVisibility(View.VISIBLE);
                binding.cvScanQRCode.setVisibility(View.VISIBLE);
            }
        });
    }

    private void setBottomNavigationView() {
        binding.bottomNavigationView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                MainUserActivity.binding.bottomNavigationView.setBackgroundResource(R.drawable.background_bottom_sheet_transparent);
                MainUserActivity.binding.bottomNavigationView.setItemIconTintList(ColorStateList.valueOf(ContextCompat.getColor(MainUserActivity.this, R.color.primary)));
                MainUserActivity.binding.cvScanQRCode.setBackgroundTintList(ColorStateList.valueOf(ContextCompat.getColor(MainUserActivity.this, R.color.primary)));

                switch (item.getItemId()) {
                    case R.id.menu_home:
                        binding.viewPager2.setCurrentItem(0, false);
                        break;
                    case R.id.menu_food:
                        binding.viewPager2.setCurrentItem(1, false);
                        break;
                    case R.id.menu_booking:
                        binding.viewPager2.setCurrentItem(2, false);
                        break;
                    case R.id.menu_account:
                        binding.viewPager2.setCurrentItem(3, false);
                        break;
                }
                return true;
            }
        });
    }

    private void clickScanner() {
        binding.imageQrcode.setOnClickListener(v -> {
            scanQRCode();
        });
    }

    ActivityResultLauncher<ScanOptions> barLauncher = registerForActivityResult(new ScanContract(), result -> {
        if (result.getContents() != null) {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("Result");
            builder.setMessage(result.getContents());
            builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    dialogInterface.dismiss();
                }
            }).show();
        }
    });

    private void scanQRCode() {
        ScanOptions options = new ScanOptions();
        options.setPrompt("Volume up to flash on");
        options.setBeepEnabled(true);
        options.setOrientationLocked(true);
        options.setCaptureActivity(CaptureActivity.class);
        barLauncher.launch(options);
    }
}