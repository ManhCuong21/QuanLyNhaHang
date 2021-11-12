package com.nhomduan.quanlyungdungdathang.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.nhomduan.quanlyungdungdathang.Model.Product;
import com.nhomduan.quanlyungdungdathang.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.ViewHolder> {

    Context context;
    List<Product> list;

    public ProductAdapter(Context context, List<Product> list) {
        this.context = context;
        this.list = list;
    }

    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.viewholder_product,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Product product = list.get(position);
        Picasso.get()
                .load(product.getImage())
                .placeholder(R.drawable.ic_image)
                .into(holder.imgProduct);
        holder.tvNameProduct.setText(product.getName());
        holder.tvCategoryProduct.setText(product.getLoai_sp());

        holder.tvTimeProduct.setText(product.getThoiGianCheBien() +" min");
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        ImageView imgProduct,imgHearthLike;
        TextView tvCategoryProduct,tvNameProduct,tvTimeProduct;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imgProduct = itemView.findViewById(R.id.imgProduct);
            imgHearthLike = itemView.findViewById(R.id.imgHearthLike);
            tvCategoryProduct = itemView.findViewById(R.id.tvCategoryProduct);
            tvNameProduct = itemView.findViewById(R.id.tvNameProduct);
            tvTimeProduct = itemView.findViewById(R.id.tvTimeProduct);
        }
    }
}