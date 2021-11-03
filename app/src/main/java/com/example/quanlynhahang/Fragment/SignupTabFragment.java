package com.example.quanlynhahang.Fragment;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ToggleButton;

import com.example.quanlynhahang.R;


public class SignupTabFragment extends Fragment {

    public SignupTabFragment() {
    }


    public static SignupTabFragment newInstance(String param1, String param2) {
        SignupTabFragment fragment = new SignupTabFragment();
        Bundle args = new Bundle();
        return fragment;
    }
    EditText edtTenNguoiDung,edtTenDangNhap,edtMatKhau,edtNhapLaiMatKhau;
    Button btnDangKy;
    ToggleButton btnCheckPass,btnCheckPass2;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        ViewGroup root = (ViewGroup) inflater.inflate(R.layout.fragment_signup_tab, container, false);

        edtTenNguoiDung = root.findViewById(R.id.edtTenNguoiDung);
        edtTenDangNhap = root.findViewById(R.id.edtTenDangNhap);
        edtMatKhau = root.findViewById(R.id.edtMatKhau);
        edtNhapLaiMatKhau = root.findViewById(R.id.edtNhapLaiMatKhau);
        btnDangKy = root.findViewById(R.id.btnDangKy);
        btnCheckPass = root.findViewById(R.id.btnCheckPass);
        btnCheckPass2 = root.findViewById(R.id.btnCheckPass2);


        return root;
    }
}