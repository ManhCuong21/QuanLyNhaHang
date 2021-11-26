package com.nhomduan.quanlyungdungdathang.Fragment;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.text.method.PasswordTransformationMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.google.firebase.database.DatabaseError;
import com.nhomduan.quanlyungdungdathang.Activity.AvatarActivity;
import com.nhomduan.quanlyungdungdathang.Activity.HomeActivity;
import com.nhomduan.quanlyungdungdathang.Activity.LoginActivity;
import com.nhomduan.quanlyungdungdathang.Activity.OrderActivity;
import com.nhomduan.quanlyungdungdathang.Dao.UserDao;
import com.nhomduan.quanlyungdungdathang.Interface.IAfterInsertObject;
import com.nhomduan.quanlyungdungdathang.Interface.IAfterUpdateObject;
import com.nhomduan.quanlyungdungdathang.Model.User;
import com.nhomduan.quanlyungdungdathang.R;
import com.nhomduan.quanlyungdungdathang.Utils.OverUtils;
import com.squareup.picasso.Picasso;

public class ProfileFragment extends Fragment {
    private View mView;
    private ImageView imgEdit, imgAvatar;
    private TextView tvUsername;
    private Button btnCancel, btnChange, btnLogout, btnChangeAdress, btnComfirm;
    private EditText edUsername, edPass, edPassRepeat, edAddress;
    private HomeActivity homeActivity;
    private ToggleButton btnCheckPass2;
    private ToggleButton btnCheckPass;
    private Toolbar toolbar;
    private CardView cvAddress, cvOrder, cvSupport;
    private FragmentManager fragmentManager;
    private User user = HomeActivity.userLogin;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        homeActivity= (HomeActivity) getActivity();
        mView = inflater.inflate(R.layout.fragment_profile, container, false);
        initView();

        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        if (HomeActivity.userLogin.getHinhanh() != null) {
            Picasso.get()
                    .load(HomeActivity.userLogin.getHinhanh())
                    .placeholder(R.drawable.ic_image)
                    .into(imgAvatar);
        }

        String urlImage = null;
        if (getArguments() != null) {
            urlImage = getArguments().getString("img");
            Picasso.get()
                    .load(urlImage)
                    .placeholder(R.drawable.ic_image)
                    .into(imgAvatar);
            HomeActivity.userLogin.setHinhanh(urlImage);
            UserDao.getInstance().insertUser(user, new IAfterInsertObject() {
                @Override
                public void onSuccess(Object obj) {
                    OverUtils.makeToast(getContext(), "Avatar đã được đổi");
                }
                @Override
                public void onError(DatabaseError exception) {

                }
            });
        }

        homeActivity.setSupportActionBar(toolbar);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fragmentManager.beginTransaction()
                        .replace(R.id.fragmentContainerViewHome, new HomeFragment())
                        .commit();
            }
        });
        imgEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openDiaLog(user);
            }
        });
        btnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                logoutMethod();
            }
        });
        tvUsername.setText(HomeActivity.userLogin.getUsername());

        imgAvatar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getContext(), AvatarActivity.class));
            }
        });

        cvAddress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openDiaLogAddress(user);
            }
        });
        cvOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Intent intent=new Intent(getContext(), OrderActivity.class);
//                intent.putExtra("idUser",HomeActivity.userLogin.getUsername());
//                startActivity(intent);
            }
        });
        cvSupport.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        return mView;
    }
    private void initView() {
        cvAddress = mView.findViewById(R.id.cv_diachi);
        cvOrder = mView.findViewById(R.id.cv_donhang);
        cvSupport = mView.findViewById(R.id.cv_trungtamhotro);
        homeActivity = (HomeActivity) getActivity();
        toolbar = mView.findViewById(R.id.toolbar_profile);
        imgAvatar = mView.findViewById(R.id.img_avatar_profile);
        imgEdit = mView.findViewById(R.id.img_edit_profile);
        tvUsername = mView.findViewById(R.id.tv_accountname_profile);
        btnLogout = mView.findViewById(R.id.btn_logout_account);
        fragmentManager = getParentFragmentManager();
    }

    private void logoutMethod() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle("Bạn có muốn thoát không")
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        startActivity(new Intent(getContext(), LoginActivity.class));
                        try {
                            ProfileFragment.this.finalize();
                        } catch (Throwable throwable) {
                            throwable.printStackTrace();
                        }
                    }
                })
                .setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
        Dialog dialog = builder.create();
        dialog.show();
    }

    private void openDiaLogAddress(User user) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        View view = LayoutInflater.from(getContext()).inflate(R.layout.dialog_address_user, null);
        builder.setView(view);
        Dialog dialog = builder.create();
        dialog.show();
        edAddress = view.findViewById(R.id.edtAddressUser_profile);
        btnChangeAdress = view.findViewById(R.id.btnChangeAddress_profile);
        btnComfirm = view.findViewById(R.id.btnComfirmAddress_profile);
        edAddress.setText(HomeActivity.userLogin.getAddress());
        btnChangeAdress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String address = edAddress.getText().toString();
                if (address.isEmpty()) {
                    OverUtils.makeToast(getContext(), "Không để trống thông tin");
                } else {
                    user.setAddress(address);
                    UserDao.getInstance().updateUser(user, user.editAdress(), new IAfterUpdateObject() {
                        @Override
                        public void onSuccess(Object obj) {
                            OverUtils.makeToast(getContext(), "Thay đổi địa chỉ thành công");
                            dialog.dismiss();
                        }

                        @Override
                        public void onError(DatabaseError error) {
                            OverUtils.makeToast(getContext(), "Thay đổi địa chỉ thất bại");
                            dialog.dismiss();
                        }
                    });
                }
            }

        });
        btnComfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
    }

    private void openDiaLog(User user) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        View view = LayoutInflater.from(getContext()).inflate(R.layout.dialog_edit_user, null);
        builder.setView(view);
        Dialog dialog = builder.create();
        dialog.show();
        btnCheckPass2 = view.findViewById(R.id.btnCheckPass2);
        btnCheckPass = view.findViewById(R.id.btnCheckPass);
        btnCheckPass.setOnClickListener(v -> {
            if (btnCheckPass.isChecked()){
                edPass.setTransformationMethod(null);
            }else {
                edPass.setTransformationMethod(new PasswordTransformationMethod());
            }
        });

        btnCheckPass2.setOnClickListener(v -> {
            if (btnCheckPass2.isChecked()) {
                edPassRepeat.setTransformationMethod(null);
            } else {
                edPassRepeat.setTransformationMethod(new PasswordTransformationMethod());
            }
        });


        edUsername = view.findViewById(R.id.edtTenNguoiDung_profile);
        edPass = view.findViewById(R.id.edtMatKhau_profile);
        edPassRepeat = view.findViewById(R.id.edtNhapLaiMatKhau_profile);
        btnChange = view.findViewById(R.id.btnChangeThongTin_profile);
        btnCancel = view.findViewById(R.id.btnHuyChangeThongTin_profile);

        edPass.setText(HomeActivity.userLogin.getPassword());
        edPassRepeat.setText(HomeActivity.userLogin.getPassword());
        edUsername.setText(HomeActivity.userLogin.getName());


        btnChange.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = edUsername.getText().toString();
                String pass = edPass.getText().toString();
                String passRepeat = edPassRepeat.getText().toString();
                if (validate(name, pass, passRepeat)) {
                    user.setPassword(pass);
                    user.setName(name);
                    user.setName(name);
                    UserDao.getInstance().updateUser(user, user.editUser(), new IAfterUpdateObject() {
                        @Override
                        public void onSuccess(Object obj) {
                            OverUtils.makeToast(getContext(), "Thay đổi thông tin thành công");
                            dialog.dismiss();
                        }

                        @Override
                        public void onError(DatabaseError error) {
                            OverUtils.makeToast(getContext(), "Thay đổi thông tin thất bại");
                            dialog.dismiss();
                        }
                    });

                }
            }

        });
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                edPass.setText("");
                edPassRepeat.setText("");
                edUsername.setText("");
            }
        });
    }

    private boolean validate(String username, String password, String rePassword) {
        if (password.isEmpty() || rePassword.isEmpty() || username.isEmpty()) {
            OverUtils.makeToast(getContext(), "Quý khánh vui lòng nhập đầy đủ thông tin");
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

}