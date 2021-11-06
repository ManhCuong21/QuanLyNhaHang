package com.nhomduan.quanlyungdungdathang.Fragment;

import static androidx.constraintlayout.motion.widget.TransitionBuilder.validate;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.nhomduan.quanlyungdungdathang.Activity.HomeActivity;
import com.nhomduan.quanlyungdungdathang.Activity.LoginActivity;
import com.nhomduan.quanlyungdungdathang.Model.User;
import com.nhomduan.quanlyungdungdathang.R;
import com.nhomduan.quanlyungdungdathang.Utils.OverUtils;
import com.nhomduan.quanlyungdungdathang.Utils.UserUtils;

import java.util.List;


public class LoginTabFragment extends Fragment implements View.OnClickListener {

    private LoginActivity loginActivity;

    public LoginTabFragment() {
    }


    public static LoginTabFragment newInstance(String param1, String param2) {
        LoginTabFragment fragment = new LoginTabFragment();
        Bundle args = new Bundle();
        return fragment;
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        loginActivity = (LoginActivity) getActivity();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    EditText edtTenDangNhap,edtMatKhau;
    TextView tvQuenMatKhau;
    Button btnDangNhap,btnHuyDangNhap;
    ToggleButton btnCheckPass;
    CheckBox ckbGhiNho;
    float v = 0;
    SharedPreferences sharedPreferences;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        ViewGroup root = (ViewGroup) inflater.inflate(R.layout.fragment_login_tab, container, false);

        sharedPreferences = getContext().getSharedPreferences("sharePrefer", Context.MODE_PRIVATE);


        edtTenDangNhap = root.findViewById(R.id.edtTenDangNhap);
        edtMatKhau = root.findViewById(R.id.edtMatKhau);
        tvQuenMatKhau = root.findViewById(R.id.tvQuenMatKhau);
        btnDangNhap = root.findViewById(R.id.btnDangNhap);
        btnCheckPass = root.findViewById(R.id.btnCheckPass);
        btnHuyDangNhap = root.findViewById(R.id.btnHuyDangNhap);
        ckbGhiNho = root.findViewById(R.id.ckbGhiNho);

        edtTenDangNhap.setTranslationX(800);
        edtMatKhau.setTranslationX(800);
        tvQuenMatKhau.setTranslationX(800);
        btnCheckPass.setTranslationX(800);
        btnDangNhap.setTranslationX(800);
        btnHuyDangNhap.setTranslationX(800);
        ckbGhiNho.setTranslationX(800);

        edtTenDangNhap.setAlpha(v);
        edtMatKhau.setAlpha(v);
        tvQuenMatKhau.setAlpha(v);
        btnCheckPass.setAlpha(v);
        btnDangNhap.setAlpha(v);
        btnHuyDangNhap.setAlpha(v);
        ckbGhiNho.setAlpha(v);

        edtTenDangNhap.animate().translationX(0).alpha(1).setDuration(800).setStartDelay(300).start();
        edtMatKhau.animate().translationX(0).alpha(1).setDuration(800).setStartDelay(500).start();
        tvQuenMatKhau.animate().translationX(0).alpha(1).setDuration(800).setStartDelay(500).start();
        btnCheckPass.animate().translationX(0).alpha(1).setDuration(800).setStartDelay(500).start();
        btnDangNhap.animate().translationX(0).alpha(1).setDuration(800).setStartDelay(700).start();
        btnHuyDangNhap.animate().translationX(0).alpha(1).setDuration(800).setStartDelay(700).start();
        ckbGhiNho.animate().translationX(0).alpha(1).setDuration(800).setStartDelay(700).start();

        return root;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initAccount();

        btnDangNhap.setOnClickListener(this);
        btnHuyDangNhap.setOnClickListener(this);



    }

    private void initAccount() {
        SharedPreferences sharedPreferences =
                loginActivity.getSharedPreferences("ACCOUNT_FILE",Context.MODE_PRIVATE);
        boolean isChecked = sharedPreferences.getBoolean("remember", false);
        if(isChecked) {
            String userName = sharedPreferences.getString("username", "");
            String password = sharedPreferences.getString("password", "");

            edtTenDangNhap.setText(userName);
            edtMatKhau.setText(password);
            ckbGhiNho.setChecked(true);
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btnDangNhap:
                dangNhap();
                break;
            case R.id.btnHuyDangNhap:
                clearForm();
                break;
            case R.id.btnCheckPass:
                break;
        }
    }

    private void clearForm() {
        edtTenDangNhap.setText("");
        edtMatKhau.setText("");
        ckbGhiNho.setChecked(false);
    }

    private void dangNhap() {
        String userName = edtTenDangNhap.getText().toString().trim();
        String password = edtMatKhau.getText().toString().trim();
        boolean isChecked;
        if(ckbGhiNho.isChecked()) {
            isChecked = true;
        } else {
            isChecked = false;
        }

        if(validate(userName, password)) {
            UserUtils.getDbRefUser().get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DataSnapshot> task) {
                    if(task.isSuccessful()) {
                        DataSnapshot dataSnapshot = task.getResult();
                        if(dataSnapshot != null) {
                            List<User> userList = UserUtils.getAllUser(dataSnapshot);
                            if(isUser(new User(userName, password), userList)) {
                                rememberAccount(userName, password, isChecked);
                                goToHomeActivity(userName);
                            } else {
                                OverUtils.makeToast(getContext(), "Tên đăng nhập hoặc mật khẩu không chính xác");
                            }
                        } else {
                            OverUtils.makeToast(getContext(), OverUtils.ERROR_MESSAGE);
                        }
                    }
                }
            });
        }
    }

    private void goToHomeActivity(String userName) {
        Intent intent = new Intent(getContext(), HomeActivity.class);
        intent.putExtra("user", userName);
        startActivity(intent);
        loginActivity.finish();
    }

    private void rememberAccount(String userName, String password, boolean isChecked) {
        SharedPreferences sharedPreferences =
                loginActivity.getSharedPreferences("ACCOUNT_FILE",Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        editor.putString("username", userName);
        editor.putString("password", password);
        editor.putBoolean("remember", isChecked);

        editor.apply();
    }

    private boolean validate(String userName, String password) {
        if(userName.isEmpty() || password.isEmpty()) {
            OverUtils.makeToast(getContext(), "Quý khánh vui lòng nhập đầy đủ thông tin");
            return false;
        }
        return true;
    }

    private boolean isUser(User user, List<User> userList) {
        for(User u : userList) {
            if(user.getUsername().equals(u.getUsername()) && user.getPassword().equals(u.getPassword())) {
                return true;
            }
        }
        return false;
    }
}
