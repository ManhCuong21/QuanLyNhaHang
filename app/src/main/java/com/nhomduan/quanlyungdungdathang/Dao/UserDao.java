package com.nhomduan.quanlyungdungdathang.Dao;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.gms.tasks.OnCanceledListener;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.nhomduan.quanlyungdungdathang.Fragment.DanhSachDonHangByTTFragment;
import com.nhomduan.quanlyungdungdathang.Interface.IAfterDeleteObject;
import com.nhomduan.quanlyungdungdathang.Interface.IAfterGetAllObject;
import com.nhomduan.quanlyungdungdathang.Interface.IAfterInsertObject;
import com.nhomduan.quanlyungdungdathang.Interface.IAfterUpdateObject;
import com.nhomduan.quanlyungdungdathang.Model.DonHang;
import com.nhomduan.quanlyungdungdathang.Model.GioHang;
import com.nhomduan.quanlyungdungdathang.Model.User;


import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class UserDao {
    private static UserDao instance;

    private UserDao() {
    }

    public static UserDao getInstance() {
        if (instance == null) {
            instance = new UserDao();
        }
        return instance;
    }


    public void getAllUserListener(IAfterGetAllObject iAfterGetAllObject) {
        FirebaseDatabase.getInstance().getReference()
                .child("user").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                List<User> userList = new ArrayList<>();
                for (DataSnapshot data : snapshot.getChildren()) {
                    if (data != null) {
                        User user = data.getValue(User.class);
                        userList.add(user);
                    }
                }
                iAfterGetAllObject.iAfterGetAllObject(userList);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                iAfterGetAllObject.onError(error);
            }
        });
    }

    public void getAllUser(IAfterGetAllObject iAfterGetAllObject) {
        FirebaseDatabase.getInstance().getReference().child("user").get()
                .addOnSuccessListener(dataSnapshot -> {
                    List<User> userList = new ArrayList<>();
                    for (DataSnapshot data : dataSnapshot.getChildren()) {
                        if (data != null) {
                            User user = data.getValue(User.class);
                            userList.add(user);
                        }
                    }
                    iAfterGetAllObject.iAfterGetAllObject(userList);
                })
                .addOnFailureListener(e -> Log.e("TAG", "onFailure: "));
    }

    public void insertUser(User user, IAfterInsertObject iAfterInsertObject) {
        FirebaseDatabase.getInstance().getReference().child("user").child(user.getUsername())
                .setValue(user, (error, ref) -> {
                    if (error == null) {
                        iAfterInsertObject.onSuccess(user);
                    } else {
                        iAfterInsertObject.onError(error);
                    }
                });
    }

    public void updateUser(User user, Map<String, Object> map, IAfterUpdateObject iAfterUpdateObject) {
        FirebaseDatabase.getInstance().getReference().child("user").child(user.getUsername())
                .updateChildren(map, (error, ref) -> {
                    if (error == null) {
                        iAfterUpdateObject.onSuccess(user); // trả về user đã được update
                    } else {
                        iAfterUpdateObject.onError(error);
                    }
                });
    }

    public void updateUser(User user, Map<String, Object> map) {
        FirebaseDatabase.getInstance().getReference().child("user").child(user.getUsername())
                .updateChildren(map);
    }


    public void getUserByUserName(String userName, IAfterGetAllObject iAfterGetAllObject) {
        FirebaseDatabase.getInstance().getReference().child("user").child(userName)
                .get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                DataSnapshot dataSnapshot = task.getResult();
                if (dataSnapshot != null) {
                    User user = dataSnapshot.getValue(User.class);
                    iAfterGetAllObject.iAfterGetAllObject(user);
                } else {
                    iAfterGetAllObject.iAfterGetAllObject(new User());
                }
            }
        });
    }


    public void getGioHangOfUser(User user, IAfterGetAllObject iAfterGetAllObject) {
        FirebaseDatabase.getInstance().getReference().child("user").child(user.getUsername())
                .child("gio_hang").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                List<GioHang> gioHangList = new ArrayList<>();
                for (DataSnapshot data : snapshot.getChildren()) {
                    GioHang gioHang = data.getValue(GioHang.class);
                    gioHangList.add(gioHang);
                }
                iAfterGetAllObject.iAfterGetAllObject(gioHangList);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                iAfterGetAllObject.onError(error);
            }

        });
    }


    public void getSanPhamYeuThichOfUser(User user, IAfterGetAllObject iAfterGetAllObject) {
        FirebaseDatabase.getInstance().getReference()
                .child("user").child(user.getUsername()).child("ma_sp_da_thich")
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        List<String> sanPhamYeuThichList = new ArrayList<>();
                        for(DataSnapshot data : snapshot.getChildren()) {
                            String maSP = data.getValue(String.class);
                            sanPhamYeuThichList.add(maSP);
                        }
                        iAfterGetAllObject.iAfterGetAllObject(sanPhamYeuThichList);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        iAfterGetAllObject.onError(error);
                    }
                });
    }

    public void getDonHangByUser(User user, IAfterGetAllObject iAfterGetAllObject) {
        Query query = FirebaseDatabase.getInstance().getReference().child("don_hang")
                .orderByChild("user_id").equalTo(user.getUsername());
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                List<DonHang> donHangList = new ArrayList<>();
                for(DataSnapshot data : snapshot.getChildren()) {
                    DonHang donHang = data.getValue(DonHang.class);
                    if(donHang != null) {
                        donHangList.add(donHang);
                    }
                }
                iAfterGetAllObject.iAfterGetAllObject(donHangList);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                iAfterGetAllObject.onError(error);
            }
        });

    }
}
