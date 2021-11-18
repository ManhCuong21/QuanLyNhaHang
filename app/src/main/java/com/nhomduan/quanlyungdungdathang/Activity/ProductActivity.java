package com.nhomduan.quanlyungdungdathang.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.Query;
import com.nhomduan.quanlyungdungdathang.Adapter.CategoryAdapter;
import com.nhomduan.quanlyungdungdathang.Adapter.ProductAdapter;
import com.nhomduan.quanlyungdungdathang.Interface.UpdateRecyclerView;
import com.nhomduan.quanlyungdungdathang.Model.LoaiSP;
import com.nhomduan.quanlyungdungdathang.Model.Product;
import com.nhomduan.quanlyungdungdathang.R;
import com.nhomduan.quanlyungdungdathang.Utils.LoaiSanPhamUtils;
import com.nhomduan.quanlyungdungdathang.Utils.ProductUtils;

import java.util.ArrayList;
import java.util.List;

public class ProductActivity extends AppCompatActivity implements UpdateRecyclerView {

    RecyclerView recyclerViewCategoryProduct, recyclerViewProduct;

    ProductAdapter productAdapter;
    CategoryAdapter categoryAdapter;

    List<Product> productArrayList;
    List<LoaiSP> categoryArrayList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product);
        anhXa();
        recyclerViewCategory();
        recyclerViewProduct();
    }

    private void anhXa() {
        recyclerViewCategoryProduct = findViewById(R.id.recyclerViewCategoryProduct);
        recyclerViewProduct = findViewById(R.id.recyclerViewProduct);
    }

    private void recyclerViewCategory() {
        categoryArrayList = new ArrayList<>();
        categoryAdapter = new CategoryAdapter(this, categoryArrayList, this);
        recyclerViewCategoryProduct.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        recyclerViewCategoryProduct.setAdapter(categoryAdapter);

        LoaiSanPhamUtils.getDbRfLoaiSP().addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                LoaiSP loaiSP = snapshot.getValue(LoaiSP.class);
                if (loaiSP != null) {
                    categoryArrayList.add(loaiSP);
                    categoryAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                LoaiSP loaiSP = snapshot.getValue(LoaiSP.class);
                if(loaiSP == null || categoryArrayList == null || categoryArrayList.isEmpty()) {
                    return;
                }
                for (int i = 0; i < categoryArrayList.size(); i++) {
                    if(categoryArrayList.get(i).getId().equals(loaiSP.getId())) {
                        categoryArrayList.set(i, loaiSP);
                        categoryAdapter.notifyDataSetChanged();
                    }
                }
            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) {
                LoaiSP loaiSP = snapshot.getValue(LoaiSP.class);
                if(loaiSP == null || categoryArrayList == null || categoryArrayList.isEmpty()) {
                    return;
                }
                for (int i = 0; i < categoryArrayList.size(); i++) {
                    if(categoryArrayList.get(i).getId().equals(loaiSP.getId())) {
                        categoryArrayList.remove(i);
                        categoryAdapter.notifyDataSetChanged();
                    }
                }
            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
    }

    private void recyclerViewProduct() {
        productArrayList = new ArrayList<>();
        productAdapter = new ProductAdapter(this, productArrayList);
        recyclerViewProduct.setLayoutManager(new LinearLayoutManager(this));
        recyclerViewProduct.setAdapter(productAdapter);

        String categoryId = getIntent().getStringExtra("categoryId");
        Query query = ProductUtils.getDbRfProduct().orderByChild("loai_sp").equalTo(categoryId);
        query.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                Product product = snapshot.getValue(Product.class);
                if (product != null) {
                    productArrayList.add(product);
                    productAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                Product product = snapshot.getValue(Product.class);
                if (product == null || productArrayList == null || productArrayList.isEmpty()) {
                    return;
                }
                for (int i = 0; i < productArrayList.size(); i++) {
                    if (productArrayList.get(i).getId().equals(product.getId())) {
                        productArrayList.set(i, product);
                        productAdapter.notifyDataSetChanged();
                    }
                }
            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) {
                Product product = snapshot.getValue(Product.class);
                if (product == null || productArrayList == null || productArrayList.isEmpty()) {
                    return;
                }
                for (int i = 0; i < productArrayList.size(); i++) {
                    if (productArrayList.get(i).getId().equals(product.getId())) {
                        productArrayList.remove(i);
                        productAdapter.notifyDataSetChanged();
                    }
                }
            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }


    public void btnReturn(View view) {
        Intent intent = new Intent(this, HomeActivity.class);
        startActivity(intent);
    }

    @Override
    public void callback(String categoryId) {
        productArrayList.clear();
        Query query = ProductUtils.getDbRfProduct().orderByChild("loai_sp").equalTo(categoryId);
        query.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                Product product = snapshot.getValue(Product.class);
                if (product != null) {
                    productArrayList.add(product);
                    productAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                Product product = snapshot.getValue(Product.class);
                if (product == null || productArrayList == null || productArrayList.isEmpty()) {
                    return;
                }
                for (int i = 0; i < productArrayList.size(); i++) {
                    if (productArrayList.get(i).getId().equals(product.getId())) {
                        productArrayList.set(i, product);
                        productAdapter.notifyDataSetChanged();
                    }
                }
            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) {
                Product product = snapshot.getValue(Product.class);
                if (product == null || productArrayList == null || productArrayList.isEmpty()) {
                    return;
                }
                for (int i = 0; i < productArrayList.size(); i++) {
                    if (productArrayList.get(i).getId().equals(product.getId())) {
                        productArrayList.remove(i);
                        productAdapter.notifyDataSetChanged();
                    }
                }
            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}