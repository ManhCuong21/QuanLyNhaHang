package com.nhomduan.quanlyungdungdathang.Activity;

import static com.nhomduan.quanlyungdungdathang.Utils.OverUtils.ERROR_MESSAGE;

import android.content.Intent;
import android.graphics.Paint;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.ToggleButton;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DatabaseError;
import com.nhomduan.quanlyungdungdathang.Dao.GioHangDao;
import com.nhomduan.quanlyungdungdathang.Dao.ProductDao;
import com.nhomduan.quanlyungdungdathang.Dao.UserDao;
import com.nhomduan.quanlyungdungdathang.Interface.IAfterGetAllObject;
import com.nhomduan.quanlyungdungdathang.Interface.IAfterInsertObject;
import com.nhomduan.quanlyungdungdathang.Interface.IAfterUpdateObject;
import com.nhomduan.quanlyungdungdathang.LocalDatabase.LocalUserDatabase;
import com.nhomduan.quanlyungdungdathang.Model.GioHang;
import com.nhomduan.quanlyungdungdathang.Model.Product;
import com.nhomduan.quanlyungdungdathang.Model.User;
import com.nhomduan.quanlyungdungdathang.R;
import com.nhomduan.quanlyungdungdathang.Utils.OverUtils;
import com.squareup.picasso.Picasso;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;


public class ShowProductActivity extends AppCompatActivity {

    private TextView tvNameProduct;
    private TextView btnDecrease;
    private TextView tvQuantity;
    private TextView btnIncrease;
    private TextView tvreduce;
    private TextView tvPriceProduct, tvSalePriceProduct;
    private TextView tvDescription;
    private TextView tvPreservation;
    private TextView tvRation;
    private TextView tvTimeManagement;
    private TextView tvProcessingTime;
    private ImageView imgProduct;
    private Button btnMuaNgay;

    private ToggleButton btnLike;
    private int soLuong = 1;

    private Product productDaChon;
    private User user;


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
        user = OverUtils.getUserLogin(ShowProductActivity.this);
        Intent intent = getIntent();
        String productId = intent.getStringExtra("productId");
        ProductDao.getInstance().getProductByIdListener(productId, new IAfterGetAllObject() {
            @Override
            public void iAfterGetAllObject(Object obj) {
                if(obj != null) {
                    Product product = (Product) obj;
                    if(product.getId() == null) {
                        Intent intent = new Intent(getApplicationContext(), HomeActivity.class);
                        intent.setAction(OverUtils.FROM_SHOW_PRODUCT);
                        startActivity(intent);
                        finish();
                    } else {
                        productDaChon = product;
                        buildComponent();
                    }
                } else {
                    Intent intent = new Intent(getApplicationContext(), HomeActivity.class);
                    intent.setAction(OverUtils.FROM_SHOW_PRODUCT);
                    startActivity(intent);
                    finish();
                }
            }

            @Override
            public void onError(DatabaseError error) {
                OverUtils.makeToast(ShowProductActivity.this, ERROR_MESSAGE);
            }
        });
    }

    private void setUpBtnLike() {
        btnLike.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (btnLike.isChecked()) {

                    // xử lý phần user
                    List<String> sanPhamYeuThichList = user.getMa_sp_da_thich();
                    if(sanPhamYeuThichList == null) {
                        sanPhamYeuThichList = new ArrayList<>();
                    }
                    sanPhamYeuThichList.add(productDaChon.getId());
                    user.setMa_sp_da_thich(sanPhamYeuThichList);
                    UserDao.getInstance().updateUser(user,
                            user.toMapSPDaThich(), new IAfterUpdateObject() {
                                @Override
                                public void onSuccess(Object obj) {
                                    LocalUserDatabase.getInstance(ShowProductActivity.this).getUserDao().update(user);
                                }

                                @Override
                                public void onError(DatabaseError error) {

                                }
                            });

                    // xử lý phần sản phẩm
                    productDaChon.setRate(productDaChon.getRate() + 1);
                    ProductDao.getInstance().updateProduct(productDaChon, productDaChon.toMapRate());


                } else {

                    // xử lý phần user
                    List<String> sanPhamYeuThichList = user.getMa_sp_da_thich();
                    int viTri = 0; // vị trí này dùng để xóa cái sp mà user đã thích từ trước
                    for (int i = 0; i < sanPhamYeuThichList.size(); i++) {
                        if (sanPhamYeuThichList.get(i).equals(productDaChon.getId())) {
                            viTri = i;
                        }
                    }
                    sanPhamYeuThichList.remove(viTri);
                    user.setMa_sp_da_thich(sanPhamYeuThichList);
                    UserDao.getInstance().updateUser(user,
                            user.toMapSPDaThich(), new IAfterUpdateObject() {
                                @Override
                                public void onSuccess(Object obj) {
                                    LocalUserDatabase.getInstance(ShowProductActivity.this).getUserDao().update(user);
                                }

                                @Override
                                public void onError(DatabaseError error) {

                                }
                            });

                    // xử lý phần sản phẩm
                    productDaChon.setRate(productDaChon.getRate() - 1);
                    ProductDao.getInstance().updateProduct(productDaChon, productDaChon.toMapRate());
                }
            }
        });
    }

    private void buildComponent() {
        String name = productDaChon.getName();
        String img = productDaChon.getImage();
        int price = productDaChon.getGia_ban();
        float sale = productDaChon.getKhuyen_mai();
        String description = productDaChon.getMota();
        String preservation = productDaChon.getThong_tin_bao_quan();
        String khauPhan = productDaChon.getKhau_phan();
        String Daysofstorage = productDaChon.getBao_quan();
        int Processingtime = productDaChon.getThoiGianCheBien();


        if(!productDaChon.getTrang_thai().equals(OverUtils.HOAT_DONG)) {
            if(productDaChon.getTrang_thai().equals(OverUtils.DUNG_KINH_DOANH)) {
                btnMuaNgay.setText("Dừng Bán");
            } else if(productDaChon.getTrang_thai().equals(OverUtils.HET_HANG)){
                btnMuaNgay.setText("Hết Hàng");
            } else if(productDaChon.getTrang_thai().equals(OverUtils.SAP_RA_MAT)) {
                btnMuaNgay.setText("Sắp ra mắt");
            }
            btnMuaNgay.setEnabled(false);
        } else {
            btnMuaNgay.setEnabled(true);
            btnMuaNgay.setOnClickListener(v -> {
                Intent intent = new Intent(ShowProductActivity.this, ThanhToanNgayActivity.class);
                intent.putExtra("productId", productDaChon.getId());
                intent.putExtra("so_luong", soLuong);
                startActivity(intent);
            });
        }

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
        tvProcessingTime.setText(Processingtime + " phút");

        // xử lý phần yêu thích của người dùng, nếu người dùng đã yêu thích từ trước hiển thị tim màu cam, ngược lại tim màu trắng
        List<String> sanPhamYeuThichList = user.getMa_sp_da_thich();
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
        btnMuaNgay = findViewById(R.id.btnMuaNgay);

        btnLike = findViewById(R.id.btnLike);
        tvQuantity.setText(String.valueOf(soLuong));
    }



    public void btnAddToCard(View view) {
        GioHang gioHang = new GioHang(productDaChon.getId(), soLuong);
        List<GioHang> gioHangList = user.getGio_hang();
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
                        int soLuong = dhct.getSo_luong() + gioHang.getSo_luong();
                        if(soLuong > 50) {
                            OverUtils.makeToast(ShowProductActivity.this,"Số lượng hàng của 1 sản phẩm phẩm không quá 50 sp");
                        } else {
                            dhct.setSo_luong(soLuong);
                        }
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
        user.setGio_hang(gioHangList);
        GioHangDao.getInstance().insertGioHang(user,
                user.getGio_hang(),
                new IAfterInsertObject() {
                    @Override
                    public void onSuccess(Object obj) {
                        LocalUserDatabase.getInstance(ShowProductActivity.this).getUserDao().update(user);
                        OverUtils.makeToast(ShowProductActivity.this, "Thêm thành công");
                    }

                    @Override
                    public void onError(DatabaseError exception) {
                        OverUtils.makeToast(ShowProductActivity.this, ERROR_MESSAGE);
                    }
                });
    }

}