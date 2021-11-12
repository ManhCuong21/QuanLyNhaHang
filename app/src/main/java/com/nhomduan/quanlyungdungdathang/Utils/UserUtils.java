package com.nhomduan.quanlyungdungdathang.Utils;

import android.content.Context;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.core.utilities.Utilities;
import com.nhomduan.quanlyungdungdathang.Model.User;

import java.util.ArrayList;
import java.util.List;

public class UserUtils {

    private static FirebaseDatabase mDatabase;

    /**
     *
     * @return trả về một list các user trong hệ thống
     */
    public static List<User> getAllUser(DataSnapshot dataSnapshot) {
        List<User> result = new ArrayList<>();
        for(DataSnapshot obj : dataSnapshot.getChildren()) {
            User user = obj.getValue(User.class);
            if(user != null) {
                result.add(user);
            }
        }
        return result;
    }

    /**
     * trả về một các tham chiếu đến database tương tự như SQLdatabase của Sqlite
     * @return cụ thể là là cây(bảng) user trên db
     */
    public static DatabaseReference getDbRefUser() {
        if (mDatabase == null) {
            mDatabase = FirebaseDatabase.getInstance();
            mDatabase.setPersistenceEnabled(true);
        }
        return mDatabase.getReference("user");
    }
}


