package com.nhomduan.quanlyungdungdathang.Utils;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class DonHangUtils {
    public static DatabaseReference getDbRfDonHang() {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        return database.getReference("don_hang");
    }
}
