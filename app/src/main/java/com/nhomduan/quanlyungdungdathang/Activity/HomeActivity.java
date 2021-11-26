package com.nhomduan.quanlyungdungdathang.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;
import com.nhomduan.quanlyungdungdathang.Fragment.OrderFragment;
import com.nhomduan.quanlyungdungdathang.Fragment.HomeFragment;
import com.nhomduan.quanlyungdungdathang.Fragment.LikeProductFragment;
import com.nhomduan.quanlyungdungdathang.Fragment.ProfileFragment;
import com.nhomduan.quanlyungdungdathang.Model.User;
import com.nhomduan.quanlyungdungdathang.R;
import com.nhomduan.quanlyungdungdathang.Utils.OverUtils;


public class HomeActivity extends AppCompatActivity {

    BottomNavigationView bottomNavigationView;
    public static User userLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        initView();
        getUserLogining();
        triggerOrderFragment();


        bottomNavigationView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                Fragment selected = null;
                switch (item.getItemId()) {
                    case R.id.nav_home:
                        selected = new HomeFragment();
                        break;
                    case R.id.nav_order:
                        selected = new OrderFragment();
                        break;
                    case R.id.nav_like:
                        selected = new LikeProductFragment();
                        break;
                    case R.id.nav_profile:
                        selected = new ProfileFragment();
                        break;
                }
                getSupportFragmentManager().beginTransaction().replace(R.id.fragmentContainerViewHome, selected).commit();
                return true;
            }
        });
    }

    private void initView() {
        bottomNavigationView = findViewById(R.id.bottom_nav_home);
    }

    private void triggerOrderFragment() {
        Intent intent = getIntent();
        if(intent != null) {
            if(intent.getAction() != null) {
                if(intent.getAction().equals(OverUtils.GO_TO_ORDER_FRAGMENT)) {
                    getSupportFragmentManager().beginTransaction()
                            .replace(R.id.fragmentContainerViewHome, new OrderFragment())
                            .commit();
                    bottomNavigationView.setSelectedItemId(R.id.nav_order);
                }
            }

        }

    }



    private void getUserLogining() {
        Intent intent = getIntent();
        User user = (User) intent.getSerializableExtra("user");
        if (user != null) {
            userLogin = user;
        }
    }


    public void btnCart(View view) {
        Intent intent = new Intent(this, CartActivity.class);
        startActivity(intent);
    }
//    private void getUserLogining() {
//        Intent intent = getIntent();
//        User user = (User) intent.getSerializableExtra("user");
//        if (user != null) {
//            userLogin = user;
//        }
//    }

    //    private void getUserLogining() {
//        Intent intent = getIntent();
//        User user = (User) intent.getSerializableExtra("user");
//        if (user != null) {
//            userLogin = user;
//        }
//    }

    //    private void getUserLogining() {
//        Intent intent = getIntent();
//        User user = (User) intent.getSerializableExtra("user");
//        if (user != null) {
//            userLogin = user;
//        }
//    }fdsf



}

