package com.example.quanlynhahang.Fragment;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.example.quanlynhahang.R;


public class LoginTabFragment extends Fragment {


    public LoginTabFragment() {
    }


    public static LoginTabFragment newInstance(String param1, String param2) {
        LoginTabFragment fragment = new LoginTabFragment();
        Bundle args = new Bundle();
        return fragment;
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
}
