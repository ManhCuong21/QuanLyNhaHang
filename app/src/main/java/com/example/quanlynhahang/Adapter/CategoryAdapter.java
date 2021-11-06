package com.example.quanlynhahang.Adapter;

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

import com.example.quanlynhahang.Activity.MainActivity;
import com.example.quanlynhahang.Interface.UpdateRecyclerView;
import com.example.quanlynhahang.Model.Category;
import com.example.quanlynhahang.Model.Product;
import com.example.quanlynhahang.R;

import java.util.ArrayList;

public class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.ViewHolder> {

    Context context;
    UpdateRecyclerView updateRecyclerView;
    boolean check = true;
    boolean select = true;

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
        switch (position){
            case 0:
                holder.categoryName.setText("Pizza");
                holder.categoryPic.setImageResource(R.drawable.pizza);
                holder.mainLayout.setBackground(ContextCompat.getDrawable(holder.itemView.getContext(),R.drawable.category_bg_1));
                holder.mainLayout.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        ArrayList<Product> productArrayList = new ArrayList<>();
                        productArrayList.add(new Product(1,"Hamburger","Hamburger bò",2000,R.drawable.hamburger,10));
                        productArrayList.add(new Product(2,"Pizza","Pizza bò",3000,R.drawable.hamburger,30));
                        productArrayList.add(new Product(3,"Hotdog","Hotdog bò",4000,R.drawable.hamburger,40));
                        productArrayList.add(new Product(4,"Hamburger","Hamburger bò",5000,R.drawable.hamburger,50));
                        productArrayList.add(new Product(5,"Hamburger","Hamburger bò",6000,R.drawable.hamburger,60));
                        updateRecyclerView.callback(0,productArrayList);
                    }
                });
                break;
            case 1:
                holder.categoryName.setText("Burger");
                holder.categoryPic.setImageResource(R.drawable.hamburger);
                holder.mainLayout.setBackground(ContextCompat.getDrawable(holder.itemView.getContext(),R.drawable.category_bg_2));
                holder.mainLayout.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        ArrayList<Product> productArrayList = new ArrayList<>();
                        productArrayList.add(new Product(1,"Hamburger","Hamburger bò",2000,R.drawable.hotdog,10));
                        productArrayList.add(new Product(2,"Pizza","Pizza bò",3000,R.drawable.hotdog,30));
                        productArrayList.add(new Product(3,"Hotdog","Hotdog bò",4000,R.drawable.hotdog,40));
                        productArrayList.add(new Product(4,"Hamburger","Hamburger bò",5000,R.drawable.hotdog,50));
                        productArrayList.add(new Product(5,"Hamburger","Hamburger bò",6000,R.drawable.hotdog,60));
                        updateRecyclerView.callback(1,productArrayList);
                    }
                });
                break;
            case 2:
                holder.categoryName.setText("Fast food");
                holder.categoryPic.setImageResource(R.drawable.hotdog);
                holder.mainLayout.setBackground(ContextCompat.getDrawable(holder.itemView.getContext(),R.drawable.category_bg_3));
                holder.mainLayout.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        ArrayList<Product> productArrayList = new ArrayList<>();
                        productArrayList.add(new Product(1,"Hamburger","Hamburger bò",2000,R.drawable.salad,10));
                        productArrayList.add(new Product(2,"Pizza","Pizza bò",3000,R.drawable.salad,30));
                        productArrayList.add(new Product(3,"Hotdog","Hotdog bò",4000,R.drawable.salad,40));
                        productArrayList.add(new Product(4,"Hamburger","Hamburger bò",5000,R.drawable.salad,50));
                        productArrayList.add(new Product(5,"Hamburger","Hamburger bò",6000,R.drawable.salad,60));
                        updateRecyclerView.callback(2,productArrayList);
                    }
                });
                break;

            case 3:
                holder.categoryName.setText("Drink");
                holder.categoryPic.setImageResource(R.drawable.drink);
                holder.mainLayout.setBackground(ContextCompat.getDrawable(holder.itemView.getContext(),R.drawable.category_bg_4));
                break;
            case 4:
                holder.categoryName.setText("Salad");
                holder.categoryPic.setImageResource(R.drawable.salad);
                holder.mainLayout.setBackground(ContextCompat.getDrawable(holder.itemView.getContext(),R.drawable.category_bg_5));
                break;
        }

        if (check){
            ArrayList<Product> productArrayList = new ArrayList<>();
            productArrayList.add(new Product(1,"Hamburger","Hamburger bò",2000,R.drawable.hamburger,10));
            productArrayList.add(new Product(2,"Pizza","Pizza bò",3000,R.drawable.pizza,30));
            productArrayList.add(new Product(3,"Hotdog","Hotdog bò",4000,R.drawable.hamburger,40));
            productArrayList.add(new Product(4,"Hamburger","Hamburger bò",5000,R.drawable.hamburger,50));
            productArrayList.add(new Product(5,"Hamburger","Hamburger bò",6000,R.drawable.hamburger,60));

            updateRecyclerView.callback(position,productArrayList);
            check = false;
        }


    }

    @Override
    public int getItemCount() {
        return 5;
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
