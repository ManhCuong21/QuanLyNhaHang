package com.nhomduan.quanlyungdungdathang.Activity;

import static com.nhomduan.quanlyungdungdathang.Utils.OverUtils.ERROR_MESSAGE;

import android.os.Bundle;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.nhomduan.quanlyungdungdathang.Adapter.CategoryAdapter;
import com.nhomduan.quanlyungdungdathang.Adapter.ProductAdapter;
import com.nhomduan.quanlyungdungdathang.Interface.UpdateRecyclerView;
import com.nhomduan.quanlyungdungdathang.Model.CategoryDomain;
import com.nhomduan.quanlyungdungdathang.Model.LoaiSP;
import com.nhomduan.quanlyungdungdathang.Model.Product;
import com.nhomduan.quanlyungdungdathang.R;
import com.nhomduan.quanlyungdungdathang.Utils.LoaiSanPhamUtils;
import com.nhomduan.quanlyungdungdathang.Utils.OverUtils;

import java.util.ArrayList;
import java.util.List;

public class ProductActivity extends AppCompatActivity implements UpdateRecyclerView {

    RecyclerView recyclerViewCategoryProduct,recyclerViewProduct;
    ProductAdapter productAdapter;
    List<Product> productArrayList;

    List<LoaiSP> categoryArrayList;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product);
        anhXa();
        productArrayList = new ArrayList<>();
        getDuLieu();

        recyclerViewCategory();

        recyclerViewProduct();


    }

    private void getDuLieu() {
        productArrayList  = (List<Product>) getIntent().getSerializableExtra("list");
        Log.e("fsdf", productArrayList.size() + "");
    }


    private void anhXa(){
        recyclerViewCategoryProduct = findViewById(R.id.recyclerViewCategoryProduct);
        recyclerViewProduct = findViewById(R.id.recyclerViewProduct);
    }

    private void recyclerViewCategory() {
        categoryArrayList = new ArrayList<>();
        CategoryAdapter categoryAdapter = new CategoryAdapter(this,categoryArrayList, this);
        recyclerViewCategoryProduct.setLayoutManager(new LinearLayoutManager(this,LinearLayoutManager.HORIZONTAL,false));
        recyclerViewCategoryProduct.setAdapter(categoryAdapter);
        LoaiSanPhamUtils.getDbRfLoaiSP().get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                if(task.isSuccessful()) {
                    DataSnapshot dataSnapshot = task.getResult();
                    categoryArrayList = LoaiSanPhamUtils.getAllLoaiSP(dataSnapshot);
                    categoryAdapter.setData(categoryArrayList);
                } else {
                    OverUtils.makeToast(getApplicationContext(), ERROR_MESSAGE);
                }
            }
        });
    }

    private void recyclerViewProduct(){
        productAdapter = new ProductAdapter(this,productArrayList);
        recyclerViewProduct.setLayoutManager(new LinearLayoutManager(this));
        recyclerViewProduct.setAdapter(productAdapter);
    }


    @Override
    public void callback(int position, List<Product> list) {
        productAdapter = new ProductAdapter(this,list);
        productAdapter.notifyDataSetChanged();
        recyclerViewProduct.setAdapter(productAdapter);
    }
}