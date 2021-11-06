package com.example.quanlynhahang.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.quanlynhahang.R;

public class ShowProductActivity extends AppCompatActivity {

    private TextView tvNameProduct;
    private TextView tvPriceProduct;
    private TextView btnDecrease;
    private TextView tvQuantity;
    private TextView btnIncrease;
    private TextView tvDescription;
    private Button btnAddToCard;
    int i =0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_product);
        anhXa();
        updateQuantity();

    }

    private void updateQuantity() {
        btnDecrease.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (i==0){
                    return;
                }else {
                    i-=1;
                    tvQuantity.setText(String.valueOf(i));
                }
            }
        });
        btnIncrease.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                i+=1;
                tvQuantity.setText(String.valueOf(i));
            }
        });
    }

    private void anhXa(){

        tvNameProduct = findViewById(R.id.tvNameProduct);
        tvPriceProduct =  findViewById(R.id.tvPriceProduct);
        btnDecrease =  findViewById(R.id.btnDecrease);
        tvQuantity =  findViewById(R.id.tvQuantity);
        btnIncrease =  findViewById(R.id.btnIncrease);
        tvDescription =  findViewById(R.id.tvDescription);
        btnAddToCard =  findViewById(R.id.btnAddToCard);

    }
}