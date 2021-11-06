package com.nhomduan.quanlyungdungdathang.Adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.nhomduan.quanlyungdungdathang.Activity.MainActivity;
import com.nhomduan.quanlyungdungdathang.Interface.UpdateRecyclerView;
import com.nhomduan.quanlyungdungdathang.Model.CategoryDomain;
import com.nhomduan.quanlyungdungdathang.Model.Product;
import com.nhomduan.quanlyungdungdathang.R;

import java.util.ArrayList;

public class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.ViewHolder> {

    Context context;
    ArrayList<CategoryDomain> categoryArrayList;
    ArrayList<Product> productArrayList;
    UpdateRecyclerView updateRecyclerView;

    public CategoryAdapter(Context context) {
        this.context = context;
    }

    public CategoryAdapter(Context context, UpdateRecyclerView updateRecyclerView) {
        this.context = context;
        this.updateRecyclerView = updateRecyclerView;
    }

    @Override
    public ViewHolder onCreateViewHolder(@NonNull  ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.viewholder_cat,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CategoryAdapter.ViewHolder holder, int position) {
        CategoryDomain category = categoryArrayList.get(position);
        holder.categoryName.setText(category.getTitle());
        holder.categoryPic.setImageResource(category.getPic());
        holder.mainLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateRecyclerView.callback(position,productArrayList); // mảng theo từng tên thể loại
            }
        });



    }

    @Override
    public int getItemCount() {
        return categoryArrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ConstraintLayout mainLayout;
        TextView categoryName;
        ImageView categoryPic;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            mainLayout = itemView.findViewById(R.id.mainLayout);
            categoryName = itemView.findViewById(R.id.categoryName);
            categoryPic = itemView.findViewById(R.id.categoryPic);
        }
    }
}

