package com.nhomduan.quanlyungdungdathang.Activity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.nhomduan.quanlyungdungdathang.Adapter.CartAdapter;
import com.nhomduan.quanlyungdungdathang.Interface.OnChangeSoLuongItem;
import com.nhomduan.quanlyungdungdathang.Interface.OnClickItem;
import com.nhomduan.quanlyungdungdathang.Model.GioHang;
import com.nhomduan.quanlyungdungdathang.R;
import com.nhomduan.quanlyungdungdathang.Utils.OverUtils;
import com.nhomduan.quanlyungdungdathang.Utils.UserUtils;

import java.util.List;

public class CartActivity extends AppCompatActivity implements OnClickItem, OnChangeSoLuongItem {

    private Toolbar toolbar;
    private RecyclerView recyclerViewCart;
    private TextView tvThanhToan;

    private List<GioHang> gioHangList;
    private CartAdapter cartAdapter;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);
        initView();
        setUpToolbar();
        setRecyclerView();

        tvThanhToan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(gioHangList == null || gioHangList.size() == 0) {
                    OverUtils.makeToast(CartActivity.this, "Giỏ hàng của quý khách đang trống");
                } else {
                    Intent intent = new Intent(CartActivity.this, ThanhToanActivity.class);
                    startActivity(intent);
                }

            }
        });

    }

    private void setUpToolbar() {
        setSupportActionBar(toolbar);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CartActivity.this.onBackPressed();
            }
        });
    }

    private void initView() {
        toolbar = findViewById(R.id.toolbar);
        recyclerViewCart = findViewById(R.id.recyclerViewCart);
        tvThanhToan = findViewById(R.id.tvThanhToan);
    }


    private void setRecyclerView(){
       gioHangList = HomeActivity.userLogin.getGio_hang();
       if(gioHangList != null) {
           cartAdapter = new CartAdapter(CartActivity.this, gioHangList, this, this);
           recyclerViewCart.setLayoutManager(new LinearLayoutManager(CartActivity.this));
           recyclerViewCart.setAdapter(cartAdapter);
       }
    }

    @Override
    public void onClickItem(Object obj) {}

    @Override
    public void onDeleteItem(Object obj) {
        GioHang gioHang = (GioHang) obj;
        gioHangList.remove(gioHang);
        cartAdapter.notifyDataSetChanged();
        HomeActivity.userLogin.setGio_hang(gioHangList);
        UserUtils.getDbRefUser().child(HomeActivity.userLogin.getId()).child("gio_hang")
                .setValue(HomeActivity.userLogin.getGio_hang(), new DatabaseReference.CompletionListener() {
                    @Override
                    public void onComplete(@Nullable DatabaseError error, @NonNull DatabaseReference ref) {
                        if (error == null) {
                            OverUtils.makeToast(CartActivity.this, "Xóa thành công");
                        }
                    }
                });
    }

    @Override
    public void onChange(Object obj) {
        GioHang gioHang = (GioHang) obj;
        for(GioHang item : gioHangList) {
            if(item.getMa_sp().equals(gioHang.getMa_sp())) {
                item.setSo_luong(gioHang.getSo_luong());
                break;
            }
        }
        cartAdapter.notifyDataSetChanged();
        HomeActivity.userLogin.setGio_hang(gioHangList);
        UserUtils.getDbRefUser().child(HomeActivity.userLogin.getId()).child("gio_hang")
                .setValue(HomeActivity.userLogin.getGio_hang());
    }
}