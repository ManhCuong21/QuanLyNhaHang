package com.nhomduan.quanlyungdungdathang.Utils;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.nhomduan.quanlyungdungdathang.Model.LoaiSP;


import java.util.ArrayList;
import java.util.List;

public class LoaiSanPhamUtils {

    public static DatabaseReference getDbRfLoaiSP() {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        return database.getReference("loai_sp");
    }

    public static List<LoaiSP> getAllLoaiSP(DataSnapshot dataSnapshot) {
        List<LoaiSP> result = new ArrayList<>();
        for(DataSnapshot obj : dataSnapshot.getChildren()) {
            LoaiSP loaiSP = obj.getValue(LoaiSP.class);
            if(loaiSP != null) {
                result.add(loaiSP);
            }
        }
        return result;
    }




}
