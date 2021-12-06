package com.nhomduan.quanlyungdungdathang.Activity;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;

import com.nhomduan.quanlyungdungdathang.Adapter.LoginAdapter;
import com.nhomduan.quanlyungdungdathang.Interface.IAfterRequestPermission;
import com.nhomduan.quanlyungdungdathang.R;
import com.google.android.material.tabs.TabLayout;
import com.nhomduan.quanlyungdungdathang.Utils.OverUtils;

public class LoginActivity extends AppCompatActivity {

    TabLayout tabLayout;
    ViewPager viewPager;
    ImageView fb, google, twitter;
    float v = 0;

    public TabLayout getTabLayout() {
        return tabLayout;
    }

    public void setTabLayout(TabLayout tabLayout) {
        this.tabLayout = tabLayout;
    }

    private final ActivityResultLauncher<String> requestPermissionLauncher =
            registerForActivityResult(new ActivityResultContracts.RequestPermission(), isGranted -> {
                if (isGranted) {
                    callPhone();
                } else {
                    OverUtils.makeToast(LoginActivity.this, "Permission denied!");
                }
            });

    public void requestPermissions(String permission, IAfterRequestPermission onAfterRequestPermission) {
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if(checkSelfPermission(permission) == PackageManager.PERMISSION_GRANTED) {
                onAfterRequestPermission.onAfterRequestPermission(true);
            } else {
                requestPermissionLauncher.launch(permission);
            }
        } else {
            onAfterRequestPermission.onAfterRequestPermission(true);
        }
    }

    private void callPhone() {
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_CALL);
        intent.setData(Uri.parse("tel:1900 1000"));
        startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        tabLayout = findViewById(R.id.tab_layout);
        viewPager = findViewById(R.id.view_pager);
        fb = findViewById(R.id.fab_fb);
        google = findViewById(R.id.fab_google);
        twitter = findViewById(R.id.fab_twitter);


        tabLayout.addTab(tabLayout.newTab().setText("Đăng nhập"));
        tabLayout.addTab(tabLayout.newTab().setText("Đăng ký"));

        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);


        final LoginAdapter adapter = new LoginAdapter(getSupportFragmentManager(), this);

        viewPager.setAdapter(adapter);
        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        tabLayout.setupWithViewPager(viewPager);

        fb.setTranslationY(300);
        google.setTranslationY(300);
        twitter.setTranslationY(300);
        tabLayout.setTranslationY(300);


        fb.setAlpha(v);
        google.setAlpha(v);
        twitter.setAlpha(v);
        tabLayout.setAlpha(v);


        fb.animate().translationY(0).alpha(1).setDuration(2000).setStartDelay(400).start();
        google.animate().translationY(0).alpha(1).setDuration(2000).setStartDelay(600).start();
        twitter.animate().translationY(0).alpha(1).setDuration(2000).setStartDelay(800).start();
        tabLayout.animate().translationY(0).alpha(1).setDuration(2000).setStartDelay(100).start();

        triggerCallPhone();

    }

    private void triggerCallPhone() {
        Intent intent = getIntent();
        if(intent != null) {
            if(intent.getAction() != null) {
                if(intent.getAction().equals("CALL_PHONE")) {
                    requestPermissions(Manifest.permission.CALL_PHONE, new IAfterRequestPermission() {
                        @Override
                        public void onAfterRequestPermission(boolean request) {
                            if(request) {
                                callPhone();
                            } else {
                                OverUtils.makeToast(LoginActivity.this, "Không có quyền gọi");
                            }
                        }
                    });
                }
            }

        }
    }

}