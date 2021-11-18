package com.nhomduan.quanlyungdungdathang.Fragment;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.nhomduan.quanlyungdungdathang.Activity.HomeActivity;
import com.nhomduan.quanlyungdungdathang.Activity.ProductActivity;
import com.nhomduan.quanlyungdungdathang.Adapter.CategoryAdapter;
import com.nhomduan.quanlyungdungdathang.Interface.OnGoToOrderFragment;
import com.nhomduan.quanlyungdungdathang.Interface.UpdateRecyclerView;
import com.nhomduan.quanlyungdungdathang.Model.LoaiSP;
import com.nhomduan.quanlyungdungdathang.Model.Product;
import com.nhomduan.quanlyungdungdathang.R;
import com.nhomduan.quanlyungdungdathang.Utils.LoaiSanPhamUtils;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class HomeFragment extends Fragment  implements UpdateRecyclerView, OnGoToOrderFragment {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    private RecyclerView recyclerViewCategoryList;
    private CategoryAdapter categoryAdapter;
    private List<LoaiSP> loaiSPList;
    View view;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_home, container, false);
        recyclerViewCategory();

        return view;
    }

    private void recyclerViewCategory() {
        recyclerViewCategoryList = view.findViewById(R.id.recyclerViewCategoryList);
        recyclerViewCategoryList.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
        loaiSPList = new ArrayList<>();
        categoryAdapter = new CategoryAdapter(getContext(), loaiSPList, this);
        recyclerViewCategoryList.setAdapter(categoryAdapter);

        LoaiSanPhamUtils.getDbRfLoaiSP().addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                LoaiSP loaiSP = snapshot.getValue(LoaiSP.class);
                if(loaiSP != null) {
                    loaiSPList.add(loaiSP);
                    categoryAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }


    @Override
    public void callback(String categoryId) {
        Intent intent = new Intent(getContext(), ProductActivity.class);
        intent.putExtra("categoryId", categoryId);
        startActivity(intent);
    }

    @Override
    public void onGoTo() {

    }
}