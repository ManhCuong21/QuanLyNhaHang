package com.nhomduan.quanlyungdungdathang.Fragment;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.nhomduan.quanlyungdungdathang.Activity.HomeActivity;
import com.nhomduan.quanlyungdungdathang.Adapter.FavoriteProductAdapter;
import com.nhomduan.quanlyungdungdathang.Interface.ItemTouchHelpListener;
import com.nhomduan.quanlyungdungdathang.Model.Product;
import com.nhomduan.quanlyungdungdathang.R;
import com.nhomduan.quanlyungdungdathang.Utils.ProductUtils;
import com.nhomduan.quanlyungdungdathang.Utils.RecyclerViewItemTouchHelper;

import java.util.ArrayList;
import java.util.List;

public class LikeProductFragment extends Fragment implements ItemTouchHelpListener {

    private HomeActivity activity;
    private FragmentManager fragmentManager;
    private Toolbar toolbar;
    private RecyclerView rcvSanPhamYeuThich;
    private LinearLayout viewRoot;




    private List<Product> productList;
    private FavoriteProductAdapter favoriteProductAdapter;





    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_like_product, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initView(view);
        Log.e("dsf", "fsdf");
        activity.setSupportActionBar(toolbar);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fragmentManager.beginTransaction()
                        .replace(R.id.fragmentContainerViewHome, new HomeFragment())
                        .commit();
            }
        });
    }

    @Override
    public void onStart() {
        super.onStart();
        setUpListSPYeuThich();
    }

    private void setUpListSPYeuThich() {
        productList = new ArrayList<>();
        favoriteProductAdapter = new FavoriteProductAdapter(getContext(), productList);
        rcvSanPhamYeuThich.setLayoutManager(new LinearLayoutManager(getContext()));
        rcvSanPhamYeuThich.addItemDecoration(new DividerItemDecoration(getContext(), DividerItemDecoration.VERTICAL));
        rcvSanPhamYeuThich.setAdapter(favoriteProductAdapter);

        ItemTouchHelper.SimpleCallback simpleCallback =
                new RecyclerViewItemTouchHelper(0, ItemTouchHelper.LEFT, this);
        new ItemTouchHelper(simpleCallback).attachToRecyclerView(rcvSanPhamYeuThich);


        List<String> list = HomeActivity.userLogin.getMa_sp_da_thich();
        if(list != null) {
            for(String maSP : list) {
                ProductUtils.getDbRfProduct().child(maSP).get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
                    @SuppressLint("NotifyDataSetChanged")
                    @Override
                    public void onComplete(@NonNull Task<DataSnapshot> task) {
                        if(task.isSuccessful() && task.getResult() != null) {
                            Product product = task.getResult().getValue(Product.class);
                            if(product != null) {
                                productList.add(product);
                                favoriteProductAdapter.notifyDataSetChanged();
                            }
                        }
                    }
                });
            }
        }
    }

    private void initView(View view) {
        toolbar = view.findViewById(R.id.toolbar);
        rcvSanPhamYeuThich = view.findViewById(R.id.rcvSanPhamYeuThich);
        activity = (HomeActivity) requireActivity();
        fragmentManager = getParentFragmentManager();
        viewRoot = view.findViewById(R.id.viewRoot);

    }

    @Override
    public void onSwiped(RecyclerView.ViewHolder viewHolder) {
        if(viewHolder instanceof FavoriteProductAdapter.ViewHolder) {
            String productName = productList.get(viewHolder.getAdapterPosition()).getName();

            final Product product = productList.get(viewHolder.getAdapterPosition());
            final int indexDelete = viewHolder.getAdapterPosition();

            // remove item ;
            favoriteProductAdapter.removeItem(product,indexDelete);
            Snackbar snackbar = Snackbar.make(viewRoot, productName + " removed !", Snackbar.LENGTH_LONG);
            snackbar.setAction("UNDO", new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    favoriteProductAdapter.undoItem(product, indexDelete);
                    if(indexDelete == 0 || indexDelete == productList.size() - 1) {
                        rcvSanPhamYeuThich.scrollToPosition(indexDelete);
                    }
                }
            });
            snackbar.setActionTextColor(Color.YELLOW);
            snackbar.show();
        }
    }
}