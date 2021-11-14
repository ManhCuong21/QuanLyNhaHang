package com.nhomduan.quanlyungdungdathang.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import android.os.Bundle;

import com.google.android.material.tabs.TabLayout;
import com.nhomduan.quanlyungdungdathang.Adapter.OrdersStatusAdapter;
import com.nhomduan.quanlyungdungdathang.R;

public class OrdersStatusActivity extends AppCompatActivity {
    private TabLayout tabLayout;
    private ViewPager viewPager;
    private OrdersStatusAdapter adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_status_don_hang);
        tabLayout = findViewById(R.id.tablayout_orders);
        viewPager = findViewById(R.id.viewpager_orders);
        adapter=new OrdersStatusAdapter(this.getSupportFragmentManager());
        viewPager.setAdapter(adapter);
        tabLayout.setupWithViewPager(viewPager);
    }
}