package com.example.quanlynhahang.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import com.example.quanlynhahang.Adapter.CategoryAdapter;
import com.example.quanlynhahang.Adapter.ProductAdapter;
import com.example.quanlynhahang.Interface.UpdateRecyclerView;
import com.example.quanlynhahang.Model.Category;
import com.example.quanlynhahang.Model.Product;
import com.example.quanlynhahang.R;

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
        ArrayList<Category> categoryArrayList = new ArrayList<>();
        categoryArrayList.add(new Category("Title 1",R.drawable.hamburger));
        categoryArrayList.add(new Category("Title 2",R.drawable.hamburger));
        categoryArrayList.add(new Category("Title 3",R.drawable.hamburger));
        categoryArrayList.add(new Category("Title 4",R.drawable.hamburger));
        categoryArrayList.add(new Category("Title 5",R.drawable.hamburger));
        CategoryAdapter categoryAdapter = new CategoryAdapter(this,this);
        recyclerViewCategoryProduct.setLayoutManager(new LinearLayoutManager(this,LinearLayoutManager.HORIZONTAL,false));
        recyclerViewCategoryProduct.setAdapter(categoryAdapter);
    }

    private void recyclerViewProduct(){
        productArrayList = new ArrayList<>();
        productArrayList.add(new Product(1,"Hamburger","Hamburger bò",2000,R.drawable.hamburger,10));
        productArrayList.add(new Product(2,"Pizza","Pizza bò",3000,R.drawable.pizza,30));
        productArrayList.add(new Product(3,"Hotdog","Hotdog bò",4000,R.drawable.hamburger,40));
        productArrayList.add(new Product(4,"Hamburger","Hamburger bò",5000,R.drawable.hamburger,50));
        productArrayList.add(new Product(5,"Hamburger","Hamburger bò",6000,R.drawable.hamburger,60));
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