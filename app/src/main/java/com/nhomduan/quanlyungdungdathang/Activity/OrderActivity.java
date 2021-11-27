package com.nhomduan.quanlyungdungdathang.Activity;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DatabaseError;
import com.nhomduan.quanlyungdungdathang.Adapter.OrderAdapter;
import com.nhomduan.quanlyungdungdathang.Dao.OrderDao;
import com.nhomduan.quanlyungdungdathang.Interface.IAfterGetAllObject;
import com.nhomduan.quanlyungdungdathang.Model.DonHang;
import com.nhomduan.quanlyungdungdathang.R;

import java.util.ArrayList;
import java.util.List;

public class OrderActivity extends AppCompatActivity {

    private List<DonHang> list = new ArrayList<>();
    private OrderAdapter adapter;
    private RecyclerView recyclerView;
    private Intent intent;
    private String idUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order);
        intent = getIntent();
        idUser = intent.getStringExtra("idUser");
        initView();
        getListDonHang();
        adapter = new OrderAdapter(this, list);
        recyclerView.setAdapter(adapter);
    }

    private void initView() {
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(this, DividerItemDecoration.VERTICAL);
        recyclerView = findViewById(R.id.rv_donhang);
        recyclerView.addItemDecoration(dividerItemDecoration);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
    }

    private void getListDonHang() {
        OrderDao.getInstance().getAllDonHang(new IAfterGetAllObject() {
            @Override
            public void iAfterGetAllObject(Object obj) {
                list= (List<DonHang>) obj;
                adapter.setData(list);
            }

            @Override
            public void onError(DatabaseError error) {

            }
        });
    }
}