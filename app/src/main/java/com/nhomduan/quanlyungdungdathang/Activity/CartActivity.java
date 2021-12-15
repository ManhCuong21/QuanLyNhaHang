package com.nhomduan.quanlyungdungdathang.Activity;

import static com.nhomduan.quanlyungdungdathang.Utils.OverUtils.ERROR_MESSAGE;
import static com.nhomduan.quanlyungdungdathang.Utils.OverUtils.HOAT_DONG;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.nhomduan.quanlyungdungdathang.Adapter.CartAdapter;
import com.nhomduan.quanlyungdungdathang.Dao.ProductDao;
import com.nhomduan.quanlyungdungdathang.Dao.UserDao;
import com.nhomduan.quanlyungdungdathang.Interface.IAfterGetAllObject;
import com.nhomduan.quanlyungdungdathang.Interface.IAfterUpdateObject;
import com.nhomduan.quanlyungdungdathang.Interface.OnChangeSoLuongItem;
import com.nhomduan.quanlyungdungdathang.Interface.OnClickItem;
import com.nhomduan.quanlyungdungdathang.Model.GioHang;
import com.nhomduan.quanlyungdungdathang.Model.Product;
import com.nhomduan.quanlyungdungdathang.Model.User;
import com.nhomduan.quanlyungdungdathang.R;
import com.nhomduan.quanlyungdungdathang.Utils.OverUtils;

import java.util.ArrayList;
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
    }

    static int soLuongSPPhuHop = 0;
    @Override
    protected void onStart() {
        super.onStart();
        setUpGioHangList();

        tvThanhToan.setOnClickListener(v -> {
            if (gioHangList == null || gioHangList.size() == 0) {
                OverUtils.makeToast(CartActivity.this, "Giỏ hàng của quý khách đang trống");
            } else {
                int count = 0;
                for(int i = 0; i < gioHangList.size(); i++) {
                    count++;
                    int finalCount = count;
                    ProductDao.getInstance().getProductById(gioHangList.get(i).getMa_sp(), new IAfterGetAllObject() {
                        @Override
                        public void iAfterGetAllObject(Object obj) {
                            if(obj != null) {
                                Product product = (Product) obj;
                                if(product.getTrang_thai().equals(HOAT_DONG)) {
                                    soLuongSPPhuHop++;
                                }
                                if(finalCount == gioHangList.size()) {
                                    if(soLuongSPPhuHop == 0) {
                                        OverUtils.makeToast(CartActivity.this, "Giỏ hàng không có sản phẩm phù hợp");
                                    } else {
                                        soLuongSPPhuHop = 0;
                                        Intent intent = new Intent(CartActivity.this, ThanhToanActivity.class);
                                        startActivity(intent);
                                    }
                                }
                            }
                        }

                        @Override
                        public void onError(DatabaseError error) {

                        }
                    });
                }

            }

        });
    }

    private void setUpToolbar() {
        setSupportActionBar(toolbar);
        toolbar.setNavigationOnClickListener(v -> CartActivity.this.onBackPressed());
    }

    private void initView() {
        toolbar = findViewById(R.id.toolbar);
        recyclerViewCart = findViewById(R.id.recyclerViewCart);
        tvThanhToan = findViewById(R.id.tvThanhToan);
    }


    private void setUpGioHangList() {
        gioHangList = new ArrayList<>();
        cartAdapter = new CartAdapter(CartActivity.this, gioHangList, CartActivity.this, CartActivity.this);
        recyclerViewCart.setLayoutManager(new LinearLayoutManager(CartActivity.this));
        recyclerViewCart.setAdapter(cartAdapter);

        UserDao.getInstance().getGioHangOfUser(OverUtils.getUserLogin(CartActivity.this), new IAfterGetAllObject() {
            @Override
            public void iAfterGetAllObject(Object obj) {
                gioHangList = (List<GioHang>) obj;
                cartAdapter.setData(gioHangList);
            }

            @Override
            public void onError(DatabaseError error) {
                OverUtils.makeToast(CartActivity.this, ERROR_MESSAGE);
            }
        });


    }

    @Override
    public void onClickItem(Object obj) {
        String productId = (String) obj;
        Intent intent = new Intent(CartActivity.this, ShowProductActivity.class);
        intent.putExtra("productId", productId);
        startActivity(intent);
    }

    @Override
    public void onDeleteItem(Object obj) {
        GioHang gioHang = (GioHang) obj;
        gioHangList.remove(gioHang);
        cartAdapter.setData(gioHangList);
        OverUtils.getUserLogin(CartActivity.this).setGio_hang(gioHangList);
        UserDao.getInstance().updateUser(OverUtils.getUserLogin(CartActivity.this),
                OverUtils.getUserLogin(CartActivity.this).toMapGioHang());
    }


    @Override
    public void onChangeItem(int viTriItem, GioHang gioHang) {
        gioHangList.set(viTriItem, gioHang);
        OverUtils.getUserLogin(CartActivity.this).setGio_hang(gioHangList);
        UserDao.getInstance().updateUser(OverUtils.getUserLogin(CartActivity.this),
                OverUtils.getUserLogin(CartActivity.this).toMapGioHang());
        cartAdapter.setData(gioHangList);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}