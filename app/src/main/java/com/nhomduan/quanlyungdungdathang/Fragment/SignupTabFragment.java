package com.nhomduan.quanlyungdungdathang.Fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ToggleButton;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthOptions;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.nhomduan.quanlyungdungdathang.Activity.LoginActivity;
import com.nhomduan.quanlyungdungdathang.Activity.NhapOTPActivity;
import com.nhomduan.quanlyungdungdathang.Model.User;
import com.nhomduan.quanlyungdungdathang.R;
import com.nhomduan.quanlyungdungdathang.Utils.OverUtils;
import com.nhomduan.quanlyungdungdathang.Utils.UserUtils;

import java.util.List;
import java.util.concurrent.TimeUnit;


public class SignupTabFragment extends Fragment implements View.OnClickListener {

    private EditText edtSoDienThoai;
    private EditText edtTenDangNhap;
    private EditText edtMatKhau;
    private EditText edtNhapLaiMatKhau;
    private ToggleButton btnCheckPass2;
    private ToggleButton btnCheckPass;
    private Button btnHuyDangKi;
    private Button btnDangKy;

    private LoginActivity loginActivity;

    // biến môi trường kết nối của Auth của Firebase
    private FirebaseAuth mAuth;

    public SignupTabFragment() {
    }

    public static SignupTabFragment newInstance(String param1, String param2) {
        SignupTabFragment fragment = new SignupTabFragment();
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

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        ViewGroup root = (ViewGroup) inflater.inflate(R.layout.fragment_signup_tab, container, false);
        return root;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // khởi tạo các view-component
        initView(view);

        // Initialize Firebase Auth
        mAuth = FirebaseAuth.getInstance();

        // khởi tạo phương thức onClick cho các button -- override lại pt onClick
        btnDangKy.setOnClickListener(this);
        btnHuyDangKi.setOnClickListener(this);


    }

    private void initView(View view) {
        edtSoDienThoai = view.findViewById(R.id.edtSoDienThoai);
        edtTenDangNhap = view.findViewById(R.id.edtTenDangNhap);
        edtMatKhau = view.findViewById(R.id.edtMatKhau);
        edtNhapLaiMatKhau = view.findViewById(R.id.edtNhapLaiMatKhau);
        btnCheckPass2 = view.findViewById(R.id.btnCheckPass2);
        btnHuyDangKi = view.findViewById(R.id.btnHuyDangKi);
        btnDangKy = view.findViewById(R.id.btnDangKy);
        btnCheckPass = view.findViewById(R.id.btnCheckPass);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btnDangKy:
                dangKiTaiKhoan();
                break;
            case R.id.btnCheckPass:
                break;
            case R.id.btnCheckPass2:
                break;
            case R.id.btnHuyDangKi:
                clearForm();
                break;
        }
    }

    private void clearForm() {
        edtTenDangNhap.setText("");
        edtMatKhau.setText("");
        edtNhapLaiMatKhau.setText("");
        edtSoDienThoai.setText("");
    }

    private void dangKiTaiKhoan() {
        String phone_number = edtSoDienThoai.getText().toString().trim();
        String username = edtTenDangNhap.getText().toString().trim();
        String password = edtMatKhau.getText().toString().trim();
        String rePassword = edtNhapLaiMatKhau.getText().toString().trim();
        if (validate(phone_number, username, password, rePassword)) {
            String userId = UserUtils.getDbRefUser().push().getKey();
            User user = new User(userId, username, password, phone_number, true);
            DatabaseReference ref = UserUtils.getDbRefUser();
            ref.keepSynced(true);
            ref.get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DataSnapshot> task) {
                    if(task.isSuccessful())  {
                        DataSnapshot dataSnapshot = task.getResult();
                        if (dataSnapshot != null) {
                            List<User> userList = UserUtils.getAllUser(dataSnapshot);
                            boolean valid = checkUser(user, userList);
                            if (valid) {
                                onClickVerifyPhone(user);
                            }
                        } else {
                            OverUtils.makeToast(getContext(), "Lỗi thực hiện");
                        }
                    } else {
                        OverUtils.makeToast(getContext(), "Lỗi thực hiện");
                    }
                }
            });
        }


    }

    private void onClickVerifyPhone(User user) {
        PhoneAuthOptions options =
                PhoneAuthOptions.newBuilder(mAuth)
                        .setPhoneNumber(user.getPhone_number())       // Phone number to verify
                        .setTimeout(60L, TimeUnit.SECONDS) // Timeout and unit
                        .setActivity(loginActivity)                 // Activity (for callback binding)
                        .setCallbacks(new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {

                            @Override
                            public void onVerificationCompleted(PhoneAuthCredential credential) {
                                Log.d("TAG", "onVerificationCompleted:" + credential);
                                signInWithPhoneAuthCredential(credential, user);
                            }

                            @Override
                            public void onVerificationFailed(FirebaseException e) {
                                Log.w("TAG", "onVerificationFailed", e);
                                OverUtils.makeToast(getContext(), "Lỗi thực hiện");
                            }

                            @Override
                            public void onCodeSent(@NonNull String verificationId,
                                                   @NonNull PhoneAuthProvider.ForceResendingToken token) {
                                Log.d("TAG", "onCodeSent:" + verificationId);
                                goToEnterOTPActivity(user, verificationId);
                            }


                        }).build();
        PhoneAuthProvider.verifyPhoneNumber(options);
    }

    private void signInWithPhoneAuthCredential(PhoneAuthCredential credential, User user) {
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(loginActivity, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d("TAG", "signInWithCredential:success");
//                            FirebaseUser user = task.getResult().getUser();
                            // Update UI
                            UserUtils.getDbRefUser()
                                    .child(user.getId()).setValue(user.toMap(), new DatabaseReference.CompletionListener() {
                                @Override
                                public void onComplete(@Nullable DatabaseError error, @NonNull DatabaseReference ref) {
                                    goToLoginFragment(user);
                                }
                            });

                        } else {
                            // Sign in failed, display a message and update the UI
                            Log.w("TAG", "signInWithCredential:failure", task.getException());
                            if (task.getException() instanceof FirebaseAuthInvalidCredentialsException) {
                                OverUtils.makeToast(getContext(), "Quý khánh vui lòng tra lại OTP");
                            }
                        }
                    }
                });
    }


    private void goToLoginFragment(User user) {
        loginActivity.getTabLayout().setId(0);
    }

    private void goToEnterOTPActivity(User user, String verificationId) {
        Intent intent = new Intent(getContext(), NhapOTPActivity.class);
        intent.putExtra("user", user);
        intent.putExtra("verificationId", verificationId);
        startActivity(intent);
    }

    private boolean validate(String phone_number, String username, String password, String rePassword) {
        if (password.isEmpty() || rePassword.isEmpty() || username.isEmpty() || phone_number.isEmpty()) {
            OverUtils.makeToast(getContext(), "Quý khánh vui lòng nhập đầy đủ thông tin");
            return false;
        }

        if (username.length() <= 5) {
            OverUtils.makeToast(getContext(), "Quý khánh vui lòng đặt tên đăng nhập từ 6 kí tự trở lên");
            return false;
        }

        if (!phone_number.matches("^\\+84\\d{9,10}$")) {
            OverUtils.makeToast(getContext(), "Sai định dạng số điện thoại");
            return false;
        }

        if (!password.equals(rePassword)) {
            OverUtils.makeToast(getContext(), "Mật khẩu không trùng khớp");
            return false;
        }

        if (password.matches("^(?=.*?[A-Z])(?=.*?[a-z])(?=.*?[0-9])(?=.*?[#?!@$%^&*-]).{8,}$")) {
        } else {
            OverUtils.makeToast(getContext(),
                    "Mật khẩu cần 8 kí tự trở lên, trong đó có chứa kí tự đặc biệt, chữ cái viết hoa và số");
            return false;
        }
        return true;
    }

    private boolean checkUser(User user, List<User> userList) {
        for (User u : userList) {
            if (u.getUsername().equals(user.getUsername())) {
                OverUtils.makeToast(getContext(), "Hệ thống đã tồn tại tên đăng nhập này");
                return false;
            }
            if (u.getPhone_number().equals(user.getPhone_number())) {
                OverUtils.makeToast(getContext(), "Số điện thoại này đã được đăng kí trong hệ thống");
                return false;
            }
        }
        return true;
    }
}