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
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Query;
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

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class ThanhToanActivity extends AppCompatActivity {
    private Toolbar toolbar;
    private ImageView imvBack;
    private TextView tvTitle;
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
    private RecyclerView rcvSanPham;
    private TextView tvSoSanPham;
    private TextView tvTien;
    private TextView tvTitlePhiVanChuyen;
    private TextView tvPhiVanChuyen;
    private TextView tvTongTien;
    private TextView tvNhapMaGiamGia;
    private TextView tvNhapGhiChu;
    private TextView tvDangHang;


    private List<GioHang> gioHangList;
    private List<DonHangChiTiet> donHangChiTietList;


    private String hoTen;
    private String diaChi;
    private String sdt;

    private static int tongSoSP;
    private static int thoiGianGiaoHang;
    private static int count; // biến này dùng để xác định khi nào client lấy hết dữ liệu, sau đó hiện thị các thông tin.
    private static int soTienVanChuyen;
    private static int soTienThanhToan;

    private String ghiChu;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_thanh_toan);
        initView();
        setUpToolbar();
        setUpCarDiaChi();
        setUpThemDiaChi();
        setUpSanPhamList();
        setUpThoiGianGiaoAndTienThanhToan();
        setUpDatHang();
        setUpGhiChu();
        setUpDoiThoiGian();
    }

    private void setUpDoiThoiGian() {
        tvDoiThoiGianGH.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               OverUtils.makeToast(ThanhToanActivity.this, "Chúng tôi chưa hỗ trợ chức năng này");
            }
        });
    }

    private void setUpGhiChu() {
        tvNhapGhiChu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(ThanhToanActivity.this);
                bottomSheetDialog.setContentView(R.layout.layout_ghi_chu);

                EditText edtGhiChu = bottomSheetDialog.findViewById(R.id.edtGhiChu);
                Button btnGhiChu = bottomSheetDialog.findViewById(R.id.btnGhiChu);

                btnGhiChu.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        ThanhToanActivity.this.ghiChu = edtGhiChu.getText().toString().trim();
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
                if(!donHangChiTietList.isEmpty()) {
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
                                xoaGioHang();
                            }
                        }
                    });
                } else {
                    OverUtils.makeToast(ThanhToanActivity.this, "Quý khánh vui lòng chọn sản phẩm");
                }
            }
        });
    }

    private void xoaGioHang() {
        HomeActivity.userLogin.setGio_hang(new ArrayList<>());
        UserUtils.getDbRefUser().child(HomeActivity.userLogin.getId()).setValue(HomeActivity.userLogin, new DatabaseReference.CompletionListener() {
            @Override
            public void onComplete(@Nullable DatabaseError error, @NonNull DatabaseReference ref) {
                if(error == null) {
                    OverUtils.makeToast(ThanhToanActivity.this, "Đặt hàng thành công");
                    Intent intent = new Intent(ThanhToanActivity.this, HomeActivity.class);
                    intent.setAction(OverUtils.GO_TO_ORDER_FRAGMENT);
                    startActivity(intent);
                } else {
                    OverUtils.makeToast(ThanhToanActivity.this, OverUtils.ERROR_MESSAGE);
                }
            }
        });
    }

    private void setUpThoiGianGiaoAndTienThanhToan() {
        donHangChiTietList = new ArrayList<>();
        for(GioHang gioHang : gioHangList) {
            count++;
            tongSoSP += gioHang.getSo_luong();
            Query query = ProductUtils.getDbRfProduct().orderByChild("id").equalTo(gioHang.getMa_sp());
            query.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    List<Product> productList = ProductUtils.getAllProduct(snapshot);
                    if(!productList.isEmpty()) {
                        Product product = productList.get(0);
                        // lấy đơn hàng chi tiet
                        DonHangChiTiet donHangChiTiet = new DonHangChiTiet();
                        donHangChiTiet.setSo_luong(gioHang.getSo_luong());
                        donHangChiTiet.setProduct(product);
                        donHangChiTietList.add(donHangChiTiet);

                        // lấy thời gian giao hàng và phí
                        thoiGianGiaoHang += (product.getThoiGianCheBien() * gioHang.getSo_luong());
                        soTienThanhToan += ((product.getGia_ban() - (product.getGia_ban() * product.getKhuyen_mai())) * gioHang.getSo_luong());
                        if(count == gioHangList.size()) {
                            tvSoSanPham.setText(tongSoSP + " sản phẩm");
                            tvTien.setText(OverUtils.currencyFormat.format(soTienThanhToan));
                            tvTongTien.setText(OverUtils.currencyFormat.format(soTienThanhToan + soTienVanChuyen));

                            // thời gian giao hàng bằng thời gian hiện tại + thời gian chế biến + thời gian ship (30 phút)
                            long thoiGian = System.currentTimeMillis() + TimeUnit.MINUTES.toMillis(thoiGianGiaoHang) + TimeUnit.MILLISECONDS.toMillis(30);
                            String thoiGianGiaoHang = OverUtils.getSimpleDateFormat().format(new Date(thoiGian));
                            tvtThoiGianGiaoHang.setText(thoiGianGiaoHang);
                        }
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {}
            });
        }
    }

    private void setUpSanPhamList() {
        gioHangList = HomeActivity.userLogin.getGio_hang();
        GioHangAdapter gioHangAdapter = new GioHangAdapter(gioHangList);
        rcvSanPham.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        rcvSanPham.setAdapter(gioHangAdapter);
    }

    private void setUpThemDiaChi() {
        tvThemDiaChi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(ThanhToanActivity.this);
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
                ThanhToanActivity.this.onBackPressed();
            }
        });
    }

    private void initView() {
        toolbar = findViewById(R.id.toolbar);
        imvBack = findViewById(R.id.imvBack);
        tvTitle = findViewById(R.id.tvTitle);
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
        rcvSanPham = findViewById(R.id.rcvSanPham);
        tvSoSanPham = findViewById(R.id.tvSoSanPham);
        tvTien = findViewById(R.id.tvTien);
        tvTitlePhiVanChuyen = findViewById(R.id.tvTitlePhiVanChuyen);
        tvPhiVanChuyen = findViewById(R.id.tvPhiVanChuyen);
        tvTongTien = findViewById(R.id.tvTongTien);
        tvNhapMaGiamGia = findViewById(R.id.tvNhapMaGiamGia);
        tvNhapGhiChu = findViewById(R.id.tvNhapGhiChu);
        tvDangHang = findViewById(R.id.tvDangHang);

        tongSoSP = 0;
        thoiGianGiaoHang = 0;
        count = 0;
        soTienThanhToan = 0;
        soTienVanChuyen = 0;

        ghiChu = "";

    }
}