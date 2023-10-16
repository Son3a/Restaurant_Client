package com.nsb.restaurant.activity.user;

import androidx.activity.result.ActivityResultLauncher;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.MenuItem;

import com.google.android.material.navigation.NavigationBarView;
import com.journeyapps.barcodescanner.CaptureActivity;
import com.journeyapps.barcodescanner.ScanContract;
import com.journeyapps.barcodescanner.ScanOptions;
import com.nsb.restaurant.R;
import com.nsb.restaurant.databinding.ActivityMainBinding;
import com.nsb.restaurant.fragment.AccountFragment;
import com.nsb.restaurant.fragment.BookingFragment;
import com.nsb.restaurant.fragment.InfoRestaurantFragment;
import com.nsb.restaurant.fragment.MenuFragment;
import com.nsb.restaurant.fragment.OrderFoodFragment;
import com.nsb.restaurant.util.Constant;


public class MainUserActivity extends AppCompatActivity {

    ActivityMainBinding binding;
    private InfoRestaurantFragment infoFragment;
    private BookingFragment bookingFragment;
    private MenuFragment menuFragment;
    private AccountFragment accountFragment;
    private OrderFoodFragment orderFoodFragment;
    private Fragment activeFragment;

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
        infoFragment = new InfoRestaurantFragment();
        bookingFragment = new BookingFragment();
        menuFragment = new MenuFragment();
        accountFragment = new AccountFragment();
        orderFoodFragment = new OrderFoodFragment();
        activeFragment = new InfoRestaurantFragment();
    }

    private void setEvent() {
        clickScanner();
    }

    private void setBottomNavigationView() {
        if (getIntent().getExtras() != null && getIntent().getExtras().containsKey(Constant.IS_BOOKING) == true) {
            getSupportFragmentManager().beginTransaction().replace(R.id.container, bookingFragment).commit();
            return;
        }
        getSupportFragmentManager().beginTransaction().add(R.id.container, infoFragment, "info").commit();
        getSupportFragmentManager().beginTransaction().add(R.id.container, bookingFragment, "info").hide(bookingFragment).commit();
        getSupportFragmentManager().beginTransaction().add(R.id.container, menuFragment, "info").hide(menuFragment).commit();
        getSupportFragmentManager().beginTransaction().add(R.id.container, accountFragment, "info").hide(accountFragment).commit();
        binding.bottomNavigationView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.menu_home:
                        getSupportFragmentManager().beginTransaction().hide(activeFragment).show(infoFragment).commit();
                        activeFragment = infoFragment;
                        return true;
                    case R.id.menu_booking:
                        getSupportFragmentManager().beginTransaction().hide(activeFragment).show(bookingFragment).commit();
                        activeFragment = bookingFragment;
                        return true;
                    case R.id.menu_food:
                        getSupportFragmentManager().beginTransaction().hide(activeFragment).show(menuFragment).commit();
                        activeFragment = menuFragment;
                        return true;
                    case R.id.menu_account:
                        getSupportFragmentManager().beginTransaction().hide(activeFragment).show(accountFragment).commit();
                        activeFragment = accountFragment;
                        return true;
                }

                return false;
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