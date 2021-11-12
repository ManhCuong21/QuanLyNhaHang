package com.nhomduan.quanlyungdungdathang.Adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.nhomduan.quanlyungdungdathang.Interface.UpdateRecyclerView;
import com.nhomduan.quanlyungdungdathang.Model.CategoryDomain;
import com.nhomduan.quanlyungdungdathang.Model.LoaiSP;
import com.nhomduan.quanlyungdungdathang.Model.Product;
import com.nhomduan.quanlyungdungdathang.R;
import com.nhomduan.quanlyungdungdathang.Utils.OverUtils;
import com.nhomduan.quanlyungdungdathang.Utils.ProductUtils;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.ViewHolder> {

    Context context;
    List<LoaiSP> categoryArrayList;
    List<Product> productArrayList;
    UpdateRecyclerView updateRecyclerView;

    @SuppressLint("NotifyDataSetChanged")
    public void setData(List<LoaiSP> categoryArrayList) {
        this.categoryArrayList = categoryArrayList;
        notifyDataSetChanged();
    }

    public CategoryAdapter(Context context, List<LoaiSP> categoryArrayList, UpdateRecyclerView updateRecyclerView) {
        this.context = context;
        this.categoryArrayList = categoryArrayList;
        this.updateRecyclerView = updateRecyclerView;
    }

    @Override
    public ViewHolder onCreateViewHolder(@NonNull  ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.viewholder_cat,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CategoryAdapter.ViewHolder holder, @SuppressLint("RecyclerView") int position) {
        LoaiSP category = categoryArrayList.get(position);
        holder.categoryName.setText(category.getName());
        Picasso.get()
                .load(category.getHinhanh())
                .placeholder(R.drawable.ic_image)
                .into(holder.categoryPic);
        holder.mainLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                productArrayList = new ArrayList<>();
                Query query =ProductUtils.getDbRfProduct().orderByChild("loai_sp").equalTo(category.getId());
                query.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        productArrayList.clear();
                        for(DataSnapshot data : snapshot.getChildren()) {
                            Product product = data.getValue(Product.class);
                            productArrayList.add(product);
                        }
                        updateRecyclerView.callback(position, productArrayList);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        OverUtils.makeToast(context, OverUtils.ERROR_MESSAGE);
                    }
                });
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

