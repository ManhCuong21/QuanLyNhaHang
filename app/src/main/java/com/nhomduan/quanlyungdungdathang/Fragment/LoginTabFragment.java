package com.nhomduan.quanlyungdungdathang.Fragment;

import static com.nhomduan.quanlyungdungdathang.Utils.OverUtils.ERROR_MESSAGE;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.text.method.PasswordTransformationMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.google.firebase.database.DatabaseError;
import com.nhomduan.quanlyungdungdathang.Activity.HomeActivity;
import com.nhomduan.quanlyungdungdathang.Activity.LoginActivity;
import com.nhomduan.quanlyungdungdathang.Dao.UserDao;
import com.nhomduan.quanlyungdungdathang.Interface.IAfterGetAllObject;
import com.nhomduan.quanlyungdungdathang.Model.User;
import com.nhomduan.quanlyungdungdathang.R;
import com.nhomduan.quanlyungdungdathang.Utils.OverUtils;


public class LoginTabFragment extends Fragment implements View.OnClickListener {


    private LoginActivity loginActivity;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        loginActivity = (LoginActivity) getActivity();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    EditText edtTenDangNhap, edtMatKhau;
    TextView tvQuenMatKhau;
    Button btnDangNhap, btnHuyDangNhap;
    ToggleButton btnCheckPass;
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

        edtTenDangNhap.setTranslationX(800);
        edtMatKhau.setTranslationX(800);
        tvQuenMatKhau.setTranslationX(800);
        btnCheckPass.setTranslationX(800);
        btnDangNhap.setTranslationX(800);
        btnHuyDangNhap.setTranslationX(800);

        edtTenDangNhap.setAlpha(v);
        edtMatKhau.setAlpha(v);
        tvQuenMatKhau.setAlpha(v);
        btnCheckPass.setAlpha(v);
        btnDangNhap.setAlpha(v);
        btnHuyDangNhap.setAlpha(v);

        edtTenDangNhap.animate().translationX(0).alpha(1).setDuration(800).setStartDelay(300).start();
        edtMatKhau.animate().translationX(0).alpha(1).setDuration(800).setStartDelay(500).start();
        tvQuenMatKhau.animate().translationX(0).alpha(1).setDuration(800).setStartDelay(500).start();
        btnCheckPass.animate().translationX(0).alpha(1).setDuration(800).setStartDelay(500).start();
        btnDangNhap.animate().translationX(0).alpha(1).setDuration(800).setStartDelay(700).start();
        btnHuyDangNhap.animate().translationX(0).alpha(1).setDuration(800).setStartDelay(700).start();

        btnCheckPass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (btnCheckPass.isChecked()) {
                    edtMatKhau.setTransformationMethod(null);
                } else {
                    edtMatKhau.setTransformationMethod(new PasswordTransformationMethod());
                }
            }
        });
        return root;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        btnDangNhap.setOnClickListener(this);
        btnHuyDangNhap.setOnClickListener(this);


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
    }

    private void dangNhap() {
        String userName = edtTenDangNhap.getText().toString().trim();
        String password = edtMatKhau.getText().toString().trim();
        if (validate(userName, password)) {
            UserDao.getInstance().getUserByUserName(userName, new IAfterGetAllObject() {
                @Override
                public void iAfterGetAllObject(Object obj) {
                    User user = (User) obj;
                    if (user.getUsername() != null) {
                        if(user.getPassword().equals(password)) {
                            rememberAccount(userName, password);
                            goToHomeActivity(user);
                        } else {
                            OverUtils.makeToast(getContext(), "Vui lòng kiểm tra lại mật khẩu");
                        }
                    } else {
                        OverUtils.makeToast(getContext(), "Tài khoản không tồn tại");
                    }
                }

                @Override
                public void onError(DatabaseError error) {
                    OverUtils.makeToast(getContext(), ERROR_MESSAGE);
                }
            });
        }
    }


    private void goToHomeActivity(User user) {
        Intent intent = new Intent(getContext(), HomeActivity.class);
        intent.putExtra("user", user);

        SharedPreferences.Editor editor = OverUtils.getSPInstance(getContext(), OverUtils.PASS_FILE).edit();
        editor.putString("pass", OverUtils.PASS_LOGIN_ACTIVITY);
        editor.apply();

        startActivity(intent);
        loginActivity.finish();
    }

    private void rememberAccount(String userName, String password) {
        SharedPreferences.Editor editor = OverUtils.getSPInstance(getContext(), OverUtils.ACCOUNT_FILE).edit();
        editor.clear();
        editor.putString("username", userName);
        editor.putString("password", password);
        editor.apply();
    }

    private boolean validate(String userName, String password) {
        if (userName.isEmpty() || password.isEmpty()) {
            OverUtils.makeToast(getContext(), "Quý khánh vui lòng nhập đầy đủ thông tin");
            return false;
        }
        return true;
    }
}
