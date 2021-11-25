package com.nhomduan.quanlyungdungdathang.Activity;

import static com.nhomduan.quanlyungdungdathang.Utils.OverUtils.ERROR_MESSAGE;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;

import com.github.ybq.android.spinkit.style.Wave;
import com.google.firebase.database.DatabaseError;
import com.nhomduan.quanlyungdungdathang.Dao.UserDao;
import com.nhomduan.quanlyungdungdathang.Interface.IAfterGetAllObject;
import com.nhomduan.quanlyungdungdathang.Model.User;
import com.nhomduan.quanlyungdungdathang.R;
import com.nhomduan.quanlyungdungdathang.Utils.OverUtils;

import java.util.Timer;
import java.util.TimerTask;


public class FlashActivity extends AppCompatActivity {
    private ProgressBar progressCircular;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        String pass = OverUtils.getSPInstance(FlashActivity.this, OverUtils.PASS_FILE)
                .getString("pass", OverUtils.NO_PASS);
        if (pass.equals(OverUtils.NO_PASS)) {
            setContentView(R.layout.activity_main);
        } else if (pass.equals(OverUtils.PASS_FLASH_ACTIVITY)) {
            setContentView(R.layout.activity_flash);
            progressCircular = findViewById(R.id.progress_circular);
            progressCircular.setIndeterminateDrawable(new Wave());
            Timer timer = new Timer();
            timer.schedule(new TimerTask() {
                @Override
                public void run() {
                    startActivity(new Intent(FlashActivity.this, LoginActivity.class));
                    finish();
                }
            }, 500);

        } else if (pass.equals(OverUtils.PASS_LOGIN_ACTIVITY)) {
            setContentView(R.layout.activity_flash);
            progressCircular = findViewById(R.id.progress_circular);
            progressCircular.setIndeterminateDrawable(new Wave());
            Timer timer = new Timer();
            timer.schedule(new TimerTask() {
                @Override
                public void run() {
                    String userName = OverUtils.getSPInstance(FlashActivity.this, OverUtils.ACCOUNT_FILE)
                            .getString("username", "");
                    UserDao.getInstance().getUserByUserName(userName, new IAfterGetAllObject() {
                        @Override
                        public void iAfterGetAllObject(Object obj) {
                            User user = (User) obj;
                            if (user.getUsername() != null) {
                                Intent intent = new Intent(FlashActivity.this, HomeActivity.class);
                                intent.putExtra("user", user);
                                startActivity(intent);
                                finish();
                            } else {
                                OverUtils.makeToast(FlashActivity.this, "Không tồn tại user này!");
                            }
                        }

                        @Override
                        public void onError(DatabaseError error) {
                        }
                    });
                }
            }, 500);
        }
    }


    public void btnStart(View view) {
        SharedPreferences.Editor editor = OverUtils.getSPInstance(FlashActivity.this, OverUtils.PASS_FILE).edit();
        editor.putString("pass", OverUtils.PASS_FLASH_ACTIVITY);
        editor.apply();
        startActivity(new Intent(FlashActivity.this, LoginActivity.class));
    }
}
