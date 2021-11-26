package com.nhomduan.quanlyungdungdathang.Activity;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.nhomduan.quanlyungdungdathang.Adapter.AvatarAdapter;
import com.nhomduan.quanlyungdungdathang.Interface.ClickAvatar;
import com.nhomduan.quanlyungdungdathang.Model.Avatar;
import com.nhomduan.quanlyungdungdathang.R;
import com.nhomduan.quanlyungdungdathang.Utils.OverUtils;

import java.util.ArrayList;
import java.util.List;

public class AvatarActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private AvatarAdapter adapter;
    private List<Avatar> list = new ArrayList<>();
    private String mUrlImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_avatar);
        initView();
        getListAvatar();
        adapter = new AvatarAdapter(this, list);
        adapter.setClickAvatar(new ClickAvatar() {
            @Override
            public void onClickAvatar(Avatar avatar) {
                  mUrlImage=avatar.getImage();
                  sendAvatar(mUrlImage);
            }
        });
        recyclerView.setAdapter(adapter);
    }



    private void sendAvatar(String img) {
        Intent intent = new Intent(getApplicationContext(), HomeActivity.class);
        intent.putExtra("img", img);
        intent.setAction(OverUtils.GO_TO_ORDER_FROFILE_FRAGMENT);
        startActivity(intent);
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

}