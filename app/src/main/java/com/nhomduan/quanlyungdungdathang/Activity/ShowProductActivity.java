package com.nhomduan.quanlyungdungdathang.Activity;

import android.content.Intent;
import android.graphics.Paint;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.ToggleButton;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.nhomduan.quanlyungdungdathang.Model.GioHang;
import com.nhomduan.quanlyungdungdathang.Model.Product;
import com.nhomduan.quanlyungdungdathang.R;
import com.nhomduan.quanlyungdungdathang.Utils.OverUtils;
import com.nhomduan.quanlyungdungdathang.Utils.ProductUtils;
import com.nhomduan.quanlyungdungdathang.Utils.UserUtils;
import com.squareup.picasso.Picasso;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;


public class ShowProductActivity extends AppCompatActivity {

    private TextView tvNameProduct;
    private TextView btnDecrease;
    private EditText tvQuantity;
    private TextView btnIncrease;
    private TextView tvreduce;
    private TextView tvPriceProduct, tvSalePriceProduct;
    private TextView tvDescription;
    private TextView tvPreservation;
    private TextView tvRation;
    private TextView tvTimeManagement;
    private TextView tvProcessingTime;
    private ImageView imgProduct;

    private ToggleButton btnLike;
    private int soLuong = 1;

    private Product productDaChon;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_product);
        anhXa();
        getDuLieu();
        updateQuantity();


        // setUp btnLike
        setUpBtnLike();
    }

    private void getDuLieu() {
        Intent intent = getIntent();
        String productId = intent.getStringExtra("productId");
        ProductUtils.getDbRfProduct().child(productId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Product product = snapshot.getValue(Product.class);
                if(product != null) {
                    productDaChon = product;
                    setText();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void setUpBtnLike() {
        btnLike.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (btnLike.isChecked()) {

                    // xử lý phần user
                    List<String> sanPhamYeuThichList = HomeActivity.userLogin.getMa_sp_da_thich();
                    if(sanPhamYeuThichList == null) {
                        sanPhamYeuThichList = new ArrayList<>();
                    }
                    sanPhamYeuThichList.add(productDaChon.getId());
                    HomeActivity.userLogin.setMa_sp_da_thich(sanPhamYeuThichList);
                    UserUtils.getDbRefUser().child(HomeActivity.userLogin.getId()).child("ma_sp_da_thich")
                            .setValue(HomeActivity.userLogin.getMa_sp_da_thich());

                    // xử lý phần sản phẩm
                    ProductUtils.getDbRfProduct().child(productDaChon.getId()).child("rate")
                            .setValue(productDaChon.getRate() + 1);


                } else {

                    // xử lý phần user
                    List<String> sanPhamYeuThichList = HomeActivity.userLogin.getMa_sp_da_thich();
                    int viTri = 0; // vị trí này dùng để xóa cái sp mà user đã thích từ trước
                    for (int i = 0; i < sanPhamYeuThichList.size(); i++) {
                        if (sanPhamYeuThichList.get(i).equals(productDaChon.getId())) {
                            viTri = i;
                        }
                    }
                    sanPhamYeuThichList.remove(viTri);
                    HomeActivity.userLogin.setMa_sp_da_thich(sanPhamYeuThichList);
                    UserUtils.getDbRefUser().child(HomeActivity.userLogin.getId()).child("ma_sp_da_thich")
                            .setValue(HomeActivity.userLogin.getMa_sp_da_thich());

                    // xử lý phần sản phẩm
                    ProductUtils.getDbRfProduct().child(productDaChon.getId()).child("rate")
                            .setValue(productDaChon.getRate() - 1);
                }
            }
        });
    }

    private void setText() {
        String name = productDaChon.getName();
        String img = productDaChon.getImage();
        int price = productDaChon.getGia_ban();
        float sale = productDaChon.getKhuyen_mai();
        String description = productDaChon.getMota();
        String preservation = productDaChon.getThong_tin_bao_quan();
        String khauPhan = productDaChon.getKhau_phan();
        String Daysofstorage = productDaChon.getBao_quan();
        int Processingtime = productDaChon.getThoiGianCheBien();

        tvNameProduct.setText(name);
        Picasso.get()
                .load(img)
                .placeholder(R.drawable.ic_image)
                .into(imgProduct);

        Locale locale = new Locale("vi", "VN");
        NumberFormat currencyFormat = NumberFormat.getNumberInstance(locale);

        if (sale > 0) {
            tvreduce.setText((int) (sale * 100) + "%");
            tvPriceProduct.setText(currencyFormat.format((int) price));
            tvPriceProduct.setPaintFlags(tvPriceProduct.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
            tvSalePriceProduct.setText(currencyFormat.format((int) (price - price * sale)));
        } else {
            tvreduce.setVisibility(View.INVISIBLE);
            tvreduce.setText("0%");
            tvPriceProduct.setText("");
            tvSalePriceProduct.setText(currencyFormat.format((int) price));
        }
        tvDescription.setText(description);
        tvPreservation.setText(preservation);
        tvRation.setText(khauPhan);
        tvTimeManagement.setText(Daysofstorage);
        tvProcessingTime.setText(Processingtime + " min");

        // xử lý phần yêu thích của người dùng, nếu người dùng đã yêu thích từ trước hiển thị tim màu cam, ngược lại tim màu trắng
        List<String> sanPhamYeuThichList = HomeActivity.userLogin.getMa_sp_da_thich();
        if (sanPhamYeuThichList != null) {
            for (String spYT : sanPhamYeuThichList) {
                if (spYT.equals(productDaChon.getId())) {
                    btnLike.setChecked(true); // set checked tự động chuyển màu sang màu cam hay đỏ gì đó thì hỏi MR.Cường :))
                }
            }
        }

    }


    private void updateQuantity() {
        btnDecrease.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(soLuong > 1) {
                    soLuong -= 1;
                    tvQuantity.setText(String.valueOf(soLuong));
                }
            }
        });
        btnIncrease.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(soLuong < 50) {
                    soLuong += 1;
                    tvQuantity.setText(String.valueOf(soLuong));
                }
            }
        });
    }

    private void anhXa() {
        tvNameProduct = findViewById(R.id.tvNameProduct);
        btnDecrease = findViewById(R.id.btnDecrease);
        tvQuantity = findViewById(R.id.tvQuantity);
        btnIncrease = findViewById(R.id.btnIncrease);
        tvreduce = findViewById(R.id.tvreduce);
        tvPriceProduct = findViewById(R.id.tvPriceProduct);
        tvDescription = findViewById(R.id.tvDescription);
        tvPreservation = findViewById(R.id.tvPreservation);
        tvRation = findViewById(R.id.tvRation);
        tvTimeManagement = findViewById(R.id.tvTimeManagement);
        tvProcessingTime = findViewById(R.id.tvProcessingTime);
        imgProduct = findViewById(R.id.imgProduct);
        tvSalePriceProduct = findViewById(R.id.tvSalePriceProduct);

        btnLike = findViewById(R.id.btnLike);
        tvQuantity.setText(String.valueOf(soLuong));
    }

    public void btnBuyNow(View view) {
        Intent intent = new Intent(ShowProductActivity.this, ThanhToanNgayActivity.class);
        intent.putExtra("productId", productDaChon.getId());
        intent.putExtra("so_luong", soLuong);
        startActivity(intent);
    }

    public void btnAddToCard(View view) {
        GioHang gioHang = new GioHang(productDaChon.getId(), soLuong);
        List<GioHang> gioHangList = HomeActivity.userLogin.getGio_hang();
        if (gioHangList == null) {
            gioHangList = new ArrayList<>();
            gioHangList.add(gioHang);
            postGioHang(gioHangList);
        } else {
            boolean tonTaiGioHangCuaSP = false;
            for (GioHang dhct : gioHangList) {
                if (dhct.getMa_sp().equals(gioHang.getMa_sp())) {
                    tonTaiGioHangCuaSP = true;
                }
            }

            if (tonTaiGioHangCuaSP) {
                for (GioHang dhct : gioHangList) {
                    if (dhct.getMa_sp().equals(gioHang.getMa_sp())) {
                        dhct.setSo_luong(dhct.getSo_luong() + gioHang.getSo_luong());
                    }
                }
                postGioHang(gioHangList);
            } else {
                gioHangList.add(gioHang);
                postGioHang(gioHangList);
            }
        }
    }

    private void postGioHang(List<GioHang> gioHangList) {
        HomeActivity.userLogin.setGio_hang(gioHangList);
        UserUtils.getDbRefUser().child(HomeActivity.userLogin.getId()).child("gio_hang")
                .setValue(HomeActivity.userLogin.getGio_hang(), new DatabaseReference.CompletionListener() {
                    @Override
                    public void onComplete(@Nullable DatabaseError error, @NonNull DatabaseReference ref) {
                        if (error == null) {
                            OverUtils.makeToast(ShowProductActivity.this, "Thêm thành công");
                            soLuong = 1;
                            tvQuantity.setText(String.valueOf(soLuong));
                        }
                    }
                });
    }

}