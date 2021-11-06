package com.nhomduan.quanlyungdungdathang.Activity;

import static java.security.AccessController.getContext;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthOptions;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.nhomduan.quanlyungdungdathang.Model.User;
import com.nhomduan.quanlyungdungdathang.R;
import com.nhomduan.quanlyungdungdathang.Utils.OverUtils;
import com.nhomduan.quanlyungdungdathang.Utils.UserUtils;

import java.util.Locale;
import java.util.concurrent.TimeUnit;

public class NhapOTPActivity extends AppCompatActivity implements View.OnClickListener {

    private EditText edtOTP;
    private Button btnNhapOTP;
    private TextView tvGuiLaiOTP;

    private User user;
    private String verificationId;

    // biến môi trường của Auth của firebase
    private FirebaseAuth mAuth;
    private PhoneAuthProvider.ForceResendingToken forceResendingToken;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nhap_otp);
        initView();

        // get dữ liệu từ trang đăng kí gửi qua
        getDataIntent();

        // set thời gian gửi lại OTP
        setTimeNhapOTPAgain();

        // Initialize Firebase Auth
        mAuth = FirebaseAuth.getInstance();

        btnNhapOTP.setOnClickListener(this);

    }

    private void initView() {
        edtOTP = findViewById(R.id.edtOTP);
        btnNhapOTP = findViewById(R.id.btnNhapOTP);
        tvGuiLaiOTP = findViewById(R.id.tvGuiLaiOTP);

        // disable clickEnable của tvGuiLaiOTP
        tvGuiLaiOTP.setClickable(false);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btnNhapOTP:
                String code = edtOTP.getText().toString().trim();
                if(code.isEmpty()) {
                    OverUtils.makeToast(getApplicationContext(), "Quý khánh vui lòng nhập mã OTP");
                    return;
                }
                sendCode(code);
                break;
            case R.id.tvGuiLaiOTP:
                onClickOTPAgain(user);
                break;

        }
    }


    private void getDataIntent() {
        user = (User) getIntent().getSerializableExtra("user");
        verificationId = getIntent().getStringExtra("verificationId");
    }

    private void onClickOTPAgain(User user) {
        PhoneAuthOptions options =
                PhoneAuthOptions.newBuilder(mAuth)
                        .setPhoneNumber(user.getPhone_number())       // Phone number to verify
                        .setTimeout(60L, TimeUnit.SECONDS) // Timeout and unit
                        .setActivity(this)
                        .setForceResendingToken(forceResendingToken)// Activity (for callback binding)
                        .setCallbacks(new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {

                            @Override
                            public void onVerificationCompleted(PhoneAuthCredential credential) {
                                Log.d("TAG", "onVerificationCompleted:" + credential);
                                signInWithPhoneAuthCredential(credential, user);
                            }

                            @Override
                            public void onVerificationFailed(FirebaseException e) {
                                Log.w("TAG", "onVerificationFailed", e);
                                Toast.makeText(getApplicationContext(), "số điện thoại đã bị chặn, thử lại sau!", Toast.LENGTH_LONG).show();
                            }

                            @Override
                            public void onCodeSent(@NonNull String verificationId,
                                                   @NonNull PhoneAuthProvider.ForceResendingToken token) {
                                Log.d("TAG", "onCodeSent:" + verificationId);
                                NhapOTPActivity.this.verificationId = verificationId;
                                forceResendingToken = token;
                            }


                        })
                        // OnVerificationStateChangedCallbacks
                        .build();
        PhoneAuthProvider.verifyPhoneNumber(options);
    }

    private void signInWithPhoneAuthCredential(PhoneAuthCredential credential, User user) {
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            Log.d("TAG", "signInWithCredential:success");
//                            FirebaseUser user = task.getResult().getUser();
                            UserUtils.getDbRefUser()
                                    .push().setValue(user.toMap(), new DatabaseReference.CompletionListener() {
                                @Override
                                public void onComplete(@Nullable DatabaseError error, @NonNull DatabaseReference ref) {
                                    goToLoginFragment(user);
                                }
                            });
                        } else {
                            Toast.makeText(getApplicationContext(), "Vui lòng kiểm tra lại OTP", Toast.LENGTH_SHORT).show();
                            Log.w("TAG", "signInWithCredential:failure", task.getException());
                            if (task.getException() instanceof FirebaseAuthInvalidCredentialsException) {
                                Toast.makeText(getApplicationContext(), "!!Vui lòng kiểm tra lại OTP", Toast.LENGTH_SHORT).show();
                            }
                        }
                    }
                });
    }

    private void setTimeNhapOTPAgain() {
        long duration = TimeUnit.MINUTES.toMillis(1);
        new CountDownTimer(duration, 1000) {
            @Override
            public void onTick(long l) {
                String sDuration = String.format(Locale.ENGLISH, "%02d : %02d",
                        TimeUnit.MILLISECONDS.toMinutes(l),
                        TimeUnit.MILLISECONDS.toSeconds(l) - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(l)));
                tvGuiLaiOTP.setText("OTP sẽ được gửi tới bạn trong vòng " + sDuration);
            }

            @Override
            public void onFinish() {
                tvGuiLaiOTP.setClickable(true);
                tvGuiLaiOTP.setText("Nhấn gửi lại nếu bạn chưa nhận được OTP!");
            }
        }.start();
    }

    private void sendCode(String code) {
        PhoneAuthCredential credential = PhoneAuthProvider.getCredential(verificationId, code);
        signInWithPhoneAuthCredential(credential, user);
    }

    private void goToLoginFragment(User user) {
        Intent intent = new Intent(this, LoginActivity.class);
        intent.putExtra("user", user);
        startActivity(intent);
    }


}