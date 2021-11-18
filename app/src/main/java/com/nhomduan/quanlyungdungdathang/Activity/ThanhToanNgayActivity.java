package com.nhomduan.quanlyungdungdathang.Activity;

import static android.view.View.INVISIBLE;
import static android.view.View.VISIBLE;


import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.nhomduan.quanlyungdungdathang.Adapter.GioHangAdapter;
import com.nhomduan.quanlyungdungdathang.Model.DonHang;
import com.nhomduan.quanlyungdungdathang.Model.DonHangChiTiet;
import com.nhomduan.quanlyungdungdathang.Model.GioHang;
import com.nhomduan.quanlyungdungdathang.Model.Product;
import com.nhomduan.quanlyungdungdathang.Model.TrangThai;
import com.nhomduan.quanlyungdungdathang.R;
import com.nhomduan.quanlyungdungdathang.Utils.DonHangUtils;
import com.nhomduan.quanlyungdungdathang.Utils.OverUtils;
import com.nhomduan.quanlyungdungdathang.Utils.ProductUtils;
import com.nhomduan.quanlyungdungdathang.Utils.UserUtils;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class ThanhToanNgayActivity extends AppCompatActivity {
    private Toolbar toolbar;
    private TextView tvThemDiaChi;
    private CardView rcvDiaChi;
    private TextView tvTiltleHoTen;
    private TextView tvHoTen;
    private TextView tvTitleDiaChiGiaoHang;
    private TextView tvDiaChiGiaoHang;
    private TextView textView8;
    private TextView tvSDT;
    private TextView tvtThoiGianGiaoHang;
    private TextView tvDoiThoiGianGH;
    private TextView tvSoSanPham;
    private TextView tvTien;
    private TextView tvTitlePhiVanChuyen;
    private TextView tvPhiVanChuyen;
    private TextView tvTongTien;
    private TextView tvNhapMaGiamGia;
    private TextView tvNhapGhiChu;
    private TextView tvDangHang;

    private String hoTen;
    private String diaChi;
    private String sdt;


    private ImageView imgSanPham;
    private TextView tvTenSanPham;
    private TextView tvGiaSanPham;
    private TextView tvSoLuong;

    private Product productDaChon;
    private int soLuongDaChon;

    private String ghiChu;
    private List<DonHangChiTiet> donHangChiTietList;

    private static int soTienThanhToan;
    private static int soTienVanChuyen;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_thanh_toan_ngay);
        initView();
        getDuLieu();
        setUpToolbar();
        setUpCarDiaChi();
        setUpThemDiaChi();
        setUpDatHang();
        setUpGhiChu();
        setUpDoiThoiGian();
    }

    private void setUpDoiThoiGian() {
    }

    private void setUpGhiChu() {
        tvNhapGhiChu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(ThanhToanNgayActivity.this);
                bottomSheetDialog.setContentView(R.layout.layout_ghi_chu);

                EditText edtGhiChu = bottomSheetDialog.findViewById(R.id.edtGhiChu);
                Button btnGhiChu = bottomSheetDialog.findViewById(R.id.btnGhiChu);

                btnGhiChu.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        ThanhToanNgayActivity.this.ghiChu = edtGhiChu.getText().toString().trim();
                        bottomSheetDialog.cancel();
                    }
                });

                bottomSheetDialog.show();
            }
        });
    }

    private void setUpDatHang() {
        tvDangHang.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DonHang donHang = new DonHang();
                donHang.setUser_id(HomeActivity.userLogin.getId());
                donHang.setDia_chi(HomeActivity.userLogin.getAddress());
                donHang.setHo_ten(HomeActivity.userLogin.getName());
                donHang.setDon_hang_chi_tiets(donHangChiTietList);
                donHang.setGhi_chu(ghiChu);
                donHang.setSdt(HomeActivity.userLogin.getPhone_number());
                donHang.setTrang_thai(TrangThai.CXN.getTrangThai());
                donHang.setThoiGianDatHang(OverUtils.getSimpleDateFormat().format(new Date(System.currentTimeMillis())));
                donHang.setTong_tien(soTienThanhToan + soTienVanChuyen);
                String key = DonHangUtils.getDbRfDonHang().push().getKey();
                donHang.setId(key);
                DonHangUtils.getDbRfDonHang().child(donHang.getId()).setValue(donHang, new DatabaseReference.CompletionListener() {
                    @Override
                    public void onComplete(@Nullable DatabaseError error, @NonNull DatabaseReference ref) {
                        if(error == null) {
                            OverUtils.makeToast(ThanhToanNgayActivity.this, "Đặt hàng thành công");
                            Intent intent = new Intent(ThanhToanNgayActivity.this, HomeActivity.class);
                            intent.setAction(OverUtils.GO_TO_ORDER_FRAGMENT);
                            startActivity(intent);
                        }
                    }
                });
            }
        });
    }


    private void setUpThemDiaChi() {
        tvThemDiaChi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(ThanhToanNgayActivity.this);
                bottomSheetDialog.setContentView(R.layout.layout_them_dia_chi);

                EditText edtHoTen;
                EditText edtDiaChi;
                EditText edtSDT;
                Button btnHuy;
                Button btnThemDiaChi;

                edtHoTen = bottomSheetDialog.findViewById(R.id.edtHoTen);
                edtDiaChi = bottomSheetDialog.findViewById(R.id.edtDiaChi);
                edtSDT = bottomSheetDialog.findViewById(R.id.edtSDT);
                btnHuy = bottomSheetDialog.findViewById(R.id.btnHuy);
                btnThemDiaChi = bottomSheetDialog.findViewById(R.id.btnThemDiaChi);


                if (sdt != null) {
                    edtSDT.setText(sdt);
                }
                if (hoTen != null) {
                    edtHoTen.setText(hoTen);
                    btnThemDiaChi.setText("Sửa");
                }
                if (diaChi != null) {
                    edtDiaChi.setText(diaChi);
                }
                btnHuy.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        bottomSheetDialog.cancel();
                    }
                });

                btnThemDiaChi.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String hoTen = edtHoTen.getText().toString().trim();
                        String diaChi = edtDiaChi.getText().toString().trim();
                        String sdt = edtSDT.getText().toString().trim();
                        if (hoTen.isEmpty() || diaChi.isEmpty() || sdt.isEmpty()) {
                            OverUtils.makeToast(getApplicationContext(), "Vui lòng nhập đầy đủ thông tin");
                            return;
                        }
                        if (!sdt.matches("^\\+84\\d{9,10}$")) {
                            OverUtils.makeToast(getApplicationContext(), "Vui lòng nhập đúng định dạng số điện thoại (vd: +84868358175)");
                            return;
                        }
                        HomeActivity.userLogin.setPhone_number(sdt);
                        HomeActivity.userLogin.setAddress(diaChi);
                        HomeActivity.userLogin.setName(hoTen);

                        UserUtils.getDbRefUser().child(HomeActivity.userLogin.getId())
                                .setValue(HomeActivity.userLogin, new DatabaseReference.CompletionListener() {
                                    @Override
                                    public void onComplete(@Nullable DatabaseError error, @NonNull DatabaseReference ref) {
                                        if (error == null) {
                                            OverUtils.makeToast(getApplicationContext(), "Thành công");
                                            rcvDiaChi.setVisibility(VISIBLE);
                                            tvHoTen.setText(hoTen);
                                            tvDiaChiGiaoHang.setText(diaChi);
                                            tvSDT.setText(sdt);
                                            bottomSheetDialog.cancel();
                                        }
                                    }
                                });
                    }
                });


                bottomSheetDialog.show();
            }
        });
    }

    private void setUpCarDiaChi() {
        hoTen = HomeActivity.userLogin.getName();
        diaChi = HomeActivity.userLogin.getAddress();
        sdt = HomeActivity.userLogin.getPhone_number();
        if (hoTen == null || diaChi == null) {
            rcvDiaChi.setVisibility(INVISIBLE);
        } else {
            tvDangHang.setBackgroundColor(Color.RED);
            tvDangHang.setClickable(true);
            rcvDiaChi.setVisibility(VISIBLE);
            tvThemDiaChi.setText("Sửa địa chỉ");
            setUpListDiaChiGiaoHang(hoTen, diaChi, sdt);
        }
    }

    private void setUpListDiaChiGiaoHang(String hoTen, String diaChi, String sdt) {
        tvDiaChiGiaoHang.setText(diaChi);
        tvSDT.setText(sdt);
        tvHoTen.setText(hoTen);
    }

    private void setUpToolbar() {
        setSupportActionBar(toolbar);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ThanhToanNgayActivity.this.onBackPressed();
            }
        });
    }

    private void getDuLieu() {
        Intent intent = getIntent();
        String productId = intent.getStringExtra("productId");
        int soLuong = intent.getIntExtra("so_luong", 0);

        soLuongDaChon = soLuong;

        ProductUtils.getDbRfProduct().child(productId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Product product = snapshot.getValue(Product.class);
                productDaChon = product;
                donHangChiTietList = new ArrayList<>();
                donHangChiTietList.add(new DonHangChiTiet(productDaChon, soLuongDaChon));
                buildComponentSanPham(productDaChon, soLuongDaChon);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    private void buildComponentSanPham(Product productDaChon, int soLuongDaChon) {
        Picasso.get()
                .load(productDaChon.getImage())
                .placeholder(R.drawable.ic_image)
                .into(imgSanPham);
        tvTenSanPham.setText(productDaChon.getName());
        tvSoLuong.setText("x" + soLuongDaChon);
        int giaSanPham = (int) (productDaChon.getGia_ban() - (productDaChon.getGia_ban() * productDaChon.getKhuyen_mai()));
        tvGiaSanPham.setText(OverUtils.currencyFormat.format(giaSanPham));

        tvSoSanPham.setText("Tổng " + soLuongDaChon + " sản phẩm");
        int giaCuaSanPham = (int) ((productDaChon.getGia_ban() - (productDaChon.getGia_ban() * productDaChon.getKhuyen_mai())) * soLuongDaChon);
        tvTien.setText(OverUtils.currencyFormat.format(giaCuaSanPham));
        int soTienVanChuyen = 0;
        tvTongTien.setText(OverUtils.currencyFormat.format(giaCuaSanPham + soTienVanChuyen));

        soTienThanhToan = giaCuaSanPham;
        ThanhToanNgayActivity.soTienVanChuyen = soTienVanChuyen;
    }

    private void initView() {
        toolbar = findViewById(R.id.toolbar);
        tvThemDiaChi = findViewById(R.id.tvThemDiaChi);
        rcvDiaChi = findViewById(R.id.rcvDiaChi);
        tvTiltleHoTen = findViewById(R.id.tvTiltleHoTen);
        tvHoTen = findViewById(R.id.tvHoTen);
        tvTitleDiaChiGiaoHang = findViewById(R.id.tvTitleDiaChiGiaoHang);
        tvDiaChiGiaoHang = findViewById(R.id.tvDiaChiGiaoHang);
        textView8 = findViewById(R.id.textView8);
        tvSDT = findViewById(R.id.tvSDT);
        tvtThoiGianGiaoHang = findViewById(R.id.tvtThoiGianGiaoHang);
        tvDoiThoiGianGH = findViewById(R.id.tvDoiThoiGianGH);
        tvSoSanPham = findViewById(R.id.tvSoSanPham);
        tvTien = findViewById(R.id.tvTien);
        tvTitlePhiVanChuyen = findViewById(R.id.tvTitlePhiVanChuyen);
        tvPhiVanChuyen = findViewById(R.id.tvPhiVanChuyen);
        tvTongTien = findViewById(R.id.tvTongTien);
        tvNhapMaGiamGia = findViewById(R.id.tvNhapMaGiamGia);
        tvNhapGhiChu = findViewById(R.id.tvNhapGhiChu);
        tvDangHang = findViewById(R.id.tvDangHang);

        imgSanPham = findViewById(R.id.imgSanPham);
        tvTenSanPham = findViewById(R.id.tvTenSanPham);
        tvGiaSanPham = findViewById(R.id.tvGiaSanPham);
        tvSoLuong = findViewById(R.id.tvSoLuong);

        ghiChu = "";
        soTienThanhToan = 0;
        soTienVanChuyen = 0;
    }
}