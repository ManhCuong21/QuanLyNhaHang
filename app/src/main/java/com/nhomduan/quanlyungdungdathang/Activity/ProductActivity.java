package com.nhomduan.quanlyungdungdathang.Activity;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.nhomduan.quanlyungdungdathang.Adapter.CategoryAdapter;
import com.nhomduan.quanlyungdungdathang.Adapter.ProductAdapter;
import com.nhomduan.quanlyungdungdathang.Interface.UpdateRecyclerView;
import com.nhomduan.quanlyungdungdathang.Model.CategoryDomain;
import com.nhomduan.quanlyungdungdathang.Model.Product;
import com.nhomduan.quanlyungdungdathang.R;

import java.util.ArrayList;

public class ProductActivity extends AppCompatActivity implements UpdateRecyclerView {

    RecyclerView recyclerViewCategoryProduct,recyclerViewProduct;
    ProductAdapter productAdapter;
    ArrayList<Product> productArrayList;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product);
        anhXa();

        recyclerViewCategory();

        recyclerViewProduct();


    }



    private void anhXa(){
        recyclerViewCategoryProduct = findViewById(R.id.recyclerViewCategoryProduct);
        recyclerViewProduct = findViewById(R.id.recyclerViewProduct);
    }

    private void recyclerViewCategory() {
        ArrayList<CategoryDomain> categoryArrayList = new ArrayList<>();
        categoryArrayList.add(new CategoryDomain("Title 1", R.drawable.hamburger));
        categoryArrayList.add(new CategoryDomain("Title 2", R.drawable.hamburger));
        categoryArrayList.add(new CategoryDomain("Title 3", R.drawable.hamburger));
        categoryArrayList.add(new CategoryDomain("Title 4", R.drawable.hamburger));
        categoryArrayList.add(new CategoryDomain("Title 5", R.drawable.hamburger));
        CategoryAdapter categoryAdapter = new CategoryAdapter(this,this);
        recyclerViewCategoryProduct.setLayoutManager(new LinearLayoutManager(this,LinearLayoutManager.HORIZONTAL,false));
        recyclerViewCategoryProduct.setAdapter(categoryAdapter);
    }

    private void recyclerViewProduct(){
        productArrayList = new ArrayList<>();

        productAdapter = new ProductAdapter(this,productArrayList);
        recyclerViewProduct.setLayoutManager(new LinearLayoutManager(this));
        recyclerViewProduct.setAdapter(productAdapter);
    }


    @Override
    public void callback(int position, ArrayList<Product> list) {
        productAdapter = new ProductAdapter(this,list);
        productAdapter.notifyDataSetChanged();
        recyclerViewProduct.setAdapter(productAdapter);
    }
}