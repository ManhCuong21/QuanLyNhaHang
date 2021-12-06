package com.nhomduan.quanlyungdungdathang.Activity;

import static android.view.View.INVISIBLE;
import static android.view.View.VISIBLE;


import static com.nhomduan.quanlyungdungdathang.Activity.HomeActivity.userLogin;
import static com.nhomduan.quanlyungdungdathang.Utils.OverUtils.ERROR_MESSAGE;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.nhomduan.quanlyungdungdathang.Dao.OrderDao;
import com.nhomduan.quanlyungdungdathang.Dao.ProductDao;
import com.nhomduan.quanlyungdungdathang.Dao.UserDao;
import com.nhomduan.quanlyungdungdathang.Interface.IAfterGetAllObject;
import com.nhomduan.quanlyungdungdathang.Interface.IAfterInsertObject;
import com.nhomduan.quanlyungdungdathang.Interface.IAfterUpdateObject;
import com.nhomduan.quanlyungdungdathang.Model.DonHang;
import com.nhomduan.quanlyungdungdathang.Model.DonHangChiTiet;
import com.nhomduan.quanlyungdungdathang.Model.Product;
import com.nhomduan.quanlyungdungdathang.Model.TrangThai;
import com.nhomduan.quanlyungdungdathang.Model.User;
import com.nhomduan.quanlyungdungdathang.R;
import com.nhomduan.quanlyungdungdathang.Utils.OverUtils;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

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
    private TextView tvThoiGianGiaoHang;
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
                UserDao.getInstance().getUserByUserName(userLogin.getUsername(), new IAfterGetAllObject() {
                    @Override
                    public void iAfterGetAllObject(Object obj) {
                        User user = (User) obj;
                        if(user.getUsername() != null && user.isEnable()) {
                            DonHang donHang = new DonHang();
                            donHang.setUser_id(userLogin.getUsername());
                            donHang.setDia_chi(userLogin.getAddress());
                            donHang.setHo_ten(userLogin.getName());
                            donHang.setDon_hang_chi_tiets(donHangChiTietList);
                            donHang.setGhi_chu(ghiChu);
                            donHang.setSdt(userLogin.getPhone_number());
                            donHang.setTrang_thai(TrangThai.CXN.getTrangThai());
                            donHang.setThoiGianDatHang(OverUtils.getSimpleDateFormat().format(new Date(System.currentTimeMillis())));
                            donHang.setTong_tien(soTienThanhToan + soTienVanChuyen);
                            String key = FirebaseDatabase.getInstance().getReference().child("don_hang").push().getKey();
                            donHang.setId(key);
                            OrderDao.getInstance().insertDonHang(donHang, new IAfterInsertObject() {
                                @Override
                                public void onSuccess(Object obj) {
                                    OverUtils.makeToast(ThanhToanNgayActivity.this, "Đặt hàng thành công");
                                    Intent intent = new Intent(ThanhToanNgayActivity.this, HomeActivity.class);
                                    intent.setAction(OverUtils.GO_TO_ORDER_FRAGMENT);
                                    startActivity(intent);
                                    finish();
                                }

                                @Override
                                public void onError(DatabaseError exception) {
                                    OverUtils.makeToast(ThanhToanNgayActivity.this, ERROR_MESSAGE);
                                }
                            });
                        } else {
                            OverUtils.makeToast(ThanhToanNgayActivity.this, "Tài khoản của bạn đã bị khóa");
                        }
                    }

                    @Override
                    public void onError(DatabaseError error) {

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
                        userLogin.setPhone_number(sdt);
                        userLogin.setAddress(diaChi);
                        userLogin.setName(hoTen);

                        UserDao.getInstance().updateUser(userLogin,
                                userLogin.toMapThongTinGiaoHang(),
                                new IAfterUpdateObject() {
                                    @Override
                                    public void onSuccess(Object obj) {
                                        OverUtils.makeToast(getApplicationContext(), "Thành công");
                                        rcvDiaChi.setVisibility(VISIBLE);
                                        tvHoTen.setText(hoTen);
                                        tvDiaChiGiaoHang.setText(diaChi);
                                        tvSDT.setText(sdt);
                                        tvDangHang.setBackgroundColor(Color.RED);
                                        tvDangHang.setClickable(true);
                                        bottomSheetDialog.cancel();
                                    }

                                    @Override
                                    public void onError(DatabaseError error) {
                                        OverUtils.makeToast(ThanhToanNgayActivity.this, ERROR_MESSAGE);
                                    }
                                });
                    }
                });


                bottomSheetDialog.show();
            }
        });
    }

    private void setUpCarDiaChi() {
        hoTen = userLogin.getName();
        diaChi = userLogin.getAddress();
        sdt = userLogin.getPhone_number();
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
        int soLuongDaChon = intent.getIntExtra("so_luong", 0);

        ProductDao.getInstance().getProductByIdListener(productId, new IAfterGetAllObject() {
            @Override
            public void iAfterGetAllObject(Object obj) {
                productDaChon = (Product) obj;
                donHangChiTietList = new ArrayList<>();
                donHangChiTietList.add(new DonHangChiTiet(productDaChon, soLuongDaChon));
                buildComponentSanPham(productDaChon, soLuongDaChon);
            }

            @Override
            public void onError(DatabaseError error) {
                OverUtils.makeToast(ThanhToanNgayActivity.this, ERROR_MESSAGE);
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

        long timeGiaoHang = System.currentTimeMillis() +
                TimeUnit.MINUTES.toMillis(productDaChon.getThoiGianCheBien()) +
                TimeUnit.MINUTES.toMillis(30);
        tvThoiGianGiaoHang.setText(OverUtils.getSimpleDateFormat().format(new Date(timeGiaoHang)));

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
        tvThoiGianGiaoHang = findViewById(R.id.tvThoiGianGiaoHang);
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

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}