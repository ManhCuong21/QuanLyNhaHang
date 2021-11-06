package com.example.quanlynhahang.Adapter;

import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.quanlynhahang.R;

public class PopularAdapter extends RecyclerView.Adapter<PopularAdapter.ViewHoler> {




    @Override
    public ViewHoler onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull PopularAdapter.ViewHoler holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 0;
    }

    public class ViewHoler extends RecyclerView.ViewHolder {
        TextView tvNameProduct,tvPriceProduct;
        ImageView imgProduct;
        Button btnAddProduct;
        public ViewHoler(@NonNull View itemView) {
            super(itemView);
            tvNameProduct = itemView.findViewById(R.id.tvNameProduct);
            tvPriceProduct = itemView.findViewById(R.id.tvPriceProduct);
            imgProduct = itemView.findViewById(R.id.imgProduct);
            btnAddProduct = itemView.findViewById(R.id.btnAddProduct);
        }
    }
}
