package com.nhomduan.quanlyungdungdathang.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DatabaseError;
import com.nhomduan.quanlyungdungdathang.Adapter.ChiTietDonHangAdapter;
import com.nhomduan.quanlyungdungdathang.Dao.OrderDao;
import com.nhomduan.quanlyungdungdathang.Interface.IAfterGetAllObject;
import com.nhomduan.quanlyungdungdathang.Model.DonHang;
import com.nhomduan.quanlyungdungdathang.Model.Shipper;
import com.nhomduan.quanlyungdungdathang.R;
import com.nhomduan.quanlyungdungdathang.Utils.OverUtils;

import java.util.Date;

public class ChiTietDonHangActivity extends AppCompatActivity {

    private Toolbar toolbarChiTietDonHang;
    private TextView tvHoTen;
    private TextView tvDiaChi;
    private TextView tvSDT;
    private TextView tvThoiGianDatHang;
    private TextView tvShipper;
    private TextView tvTitleShipper;
    private TextView tvTongSoSanPham;
    private TextView tvTien;
    private TextView tvPhiVanChuyen;
    private TextView tvTongTien;
    private TextView tvMaGiamGia;
    private TextView tvGhiChu;
    private TextView tvThoiGianGiaoHang;
    private LinearLayout lyThoiGianGiaoHang;

    private RecyclerView rcvChiTietDonHang;
    private ChiTietDonHangAdapter chiTietDonHangAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chi_tiet_don_hang);
        init();
        getDonHangDaChon();


    }

    private void getDonHangDaChon() {
        Intent intent = getIntent();
        String id = intent.getStringExtra("donHangID");
        OrderDao.getInstance().getDonHangById(id, new IAfterGetAllObject() {
            @Override
            public void iAfterGetAllObject(Object obj) {
                DonHang donHang = (DonHang) obj;

                tvHoTen.setText(donHang.getHo_ten());
                tvDiaChi.setText(donHang.getDia_chi());
                tvSDT.setText(donHang.getSdt());
                tvThoiGianDatHang.setText(donHang.getThoiGianDatHang());
                tvTien.setText(donHang.getTong_tien() + "đ");
                tvTongTien.setText(donHang.getTong_tien() + "đ");
                tvTongSoSanPham.setText("Tổng " + donHang.getDon_hang_chi_tiets().size() + " sản phẩm");

                chiTietDonHangAdapter = new ChiTietDonHangAdapter(ChiTietDonHangActivity.this, donHang.getDon_hang_chi_tiets());
                rcvChiTietDonHang.setAdapter(chiTietDonHangAdapter);


                if (donHang.getGhi_chu().isEmpty()) {
                    tvGhiChu.setText("None");
                } else if (!donHang.getGhi_chu().isEmpty()) {
                    tvGhiChu.setText(donHang.getGhi_chu());
                }

                Shipper shipper = donHang.getShipper();
                if (shipper != null) {
                    tvShipper.setText(shipper.getName() + " - " + shipper.getPhone_number());
                    tvShipper.setVisibility(View.VISIBLE);
                    tvTitleShipper.setVisibility(View.VISIBLE);
                } else {
                    tvShipper.setVisibility(View.GONE);
                    tvTitleShipper.setVisibility(View.GONE);
                }

                if (donHang.getThoiGianGiaoHang() != null) {
                    long time = Long.parseLong(donHang.getThoiGianGiaoHang());
                    lyThoiGianGiaoHang.setVisibility(View.VISIBLE);
                    tvThoiGianGiaoHang.setText(OverUtils.getSimpleDateFormat().format(new Date(time)));
                } else {
                    lyThoiGianGiaoHang.setVisibility(View.GONE);
                }


            }

            @Override
            public void onError(DatabaseError error) {

            }
        });

    }

    private void init() {
        toolbarChiTietDonHang = findViewById(R.id.toolbar_ChiTietDonHang);
        tvHoTen = findViewById(R.id.tvHoTen_ChiTietDonHang);
        tvDiaChi = findViewById(R.id.tvDiaChi_ChiTietDonHang);
        tvSDT = findViewById(R.id.tvSDT_ChiTietDonHang);
        tvThoiGianDatHang = findViewById(R.id.tvThoiGianDatHang_ChiTietGiaoHang);
        tvTongSoSanPham = findViewById(R.id.tvTongSoSanPham_ChiTietGiaoHang);
        tvTien = findViewById(R.id.tvTien_ChiTietDonHang);
        tvPhiVanChuyen = findViewById(R.id.tvPhiVanChuyen_ChiTietGiaoHang);
        tvTongTien = findViewById(R.id.tvTongTien_ChiTietDonHang);
        tvMaGiamGia = findViewById(R.id.tvMaGiamGia_ChiTietDonHang);
        tvGhiChu = findViewById(R.id.tv_GhiChu_ChiTietDonHang);
        tvShipper = findViewById(R.id.tvShipper_ChiTietDonHang);
        tvTitleShipper = findViewById(R.id.tvTitleShipper);
        tvThoiGianGiaoHang = findViewById(R.id.tvThoiGianGiaoHang_ChiTietGiaoHang);
        lyThoiGianGiaoHang = findViewById(R.id.layout_tggh);

        rcvChiTietDonHang = findViewById(R.id.rcv_ChiTietDonHang);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(ChiTietDonHangActivity.this);
        rcvChiTietDonHang.setLayoutManager(linearLayoutManager);
        rcvChiTietDonHang.addItemDecoration(new DividerItemDecoration(ChiTietDonHangActivity.this, DividerItemDecoration.VERTICAL));
        rcvChiTietDonHang.setHasFixedSize(true);


        toolbarChiTietDonHang.setOnClickListener(v -> onBackPressed());
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}