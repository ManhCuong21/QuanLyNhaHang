package com.nhomduan.quanlyungdungdathang.Activity;

import static com.nhomduan.quanlyungdungdathang.Utils.OverUtils.ERROR_MESSAGE;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.ProgressBar;

import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContract;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityOptionsCompat;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.nhomduan.quanlyungdungdathang.Adapter.AvatarAdapter;
import com.nhomduan.quanlyungdungdathang.Dao.UserDao;
import com.nhomduan.quanlyungdungdathang.Interface.ClickAvatar;
import com.nhomduan.quanlyungdungdathang.Interface.IAfterUpdateObject;
import com.nhomduan.quanlyungdungdathang.Model.Avatar;
import com.nhomduan.quanlyungdungdathang.R;
import com.nhomduan.quanlyungdungdathang.Utils.OverUtils;

import java.util.ArrayList;
import java.util.List;

public class AvatarActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private AvatarAdapter adapter;
    private ProgressDialog progressDialog;
    private List<Avatar> list = new ArrayList<>();
    ActivityResultLauncher<String> mGetContent = registerForActivityResult(new ActivityResultContracts.GetContent(),
            uri -> {
                progressDialog = new ProgressDialog(AvatarActivity.this);
                progressDialog.setMessage("Cập nhật ảnh ...");
                progressDialog.show();
                StorageReference fileRef =
                        FirebaseStorage.getInstance().getReference("imageAvater/").
                                child(System.currentTimeMillis() + "." + OverUtils.getExtensionFile(AvatarActivity.this, uri));
                fileRef.putFile(uri).addOnSuccessListener(taskSnapshot -> {
                    fileRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            HomeActivity.userLogin.setHinhanh(String.valueOf(uri));
                            UserDao.getInstance().updateUser(HomeActivity.userLogin, HomeActivity.userLogin.toMapAvatar(), new IAfterUpdateObject() {
                                @Override
                                public void onSuccess(Object obj) {
                                    progressDialog.cancel();
                                    OverUtils.makeToast(getApplicationContext(), "cập nhật ảnh thành công");
                                    Intent intent = new Intent(getApplicationContext(), HomeActivity.class);
                                    intent.setAction(OverUtils.GO_TO_ORDER_FROFILE_FRAGMENT);
                                    startActivity(intent);
                                }
                                @Override
                                public void onError(DatabaseError error) {
                                    OverUtils.makeToast(AvatarActivity.this, ERROR_MESSAGE);
                                }
                            });
                        }
                    });
                });
            });

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_avatar);
        initView();
        getListAvatar();
        adapter = new AvatarAdapter(this, list);
        recyclerView.setAdapter(adapter);
        adapter.setClickAvatar(avatar -> {
            if(avatar.getImage().equals("null")) {
                mGetContent.launch("image/*");
            } else {
                HomeActivity.userLogin.setHinhanh(avatar.getImage());
                UserDao.getInstance().updateUser(HomeActivity.userLogin, HomeActivity.userLogin.toMapAvatar(), new IAfterUpdateObject() {
                    @Override
                    public void onSuccess(Object obj) {
                        Intent intent = new Intent(getApplicationContext(), HomeActivity.class);
                        intent.setAction(OverUtils.GO_TO_ORDER_FROFILE_FRAGMENT);
                        startActivity(intent);
                    }

                    @Override
                    public void onError(DatabaseError error) {
                        OverUtils.makeToast(AvatarActivity.this, ERROR_MESSAGE);
                    }
                });
            }
        });
    }


    private void initView() {
        recyclerView = findViewById(R.id.rv_avatar);
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(this, DividerItemDecoration.VERTICAL);
        recyclerView.addItemDecoration(dividerItemDecoration);
        recyclerView.setLayoutManager(new GridLayoutManager(this, 2));
    }

    private void getListAvatar() {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("avatar");
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (list != null) {
                    list.clear();
                    list.add(new Avatar("Custom Avatar", "null"));
                }
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    Avatar avatar = dataSnapshot.getValue(Avatar.class);
                    list.add(avatar);
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}