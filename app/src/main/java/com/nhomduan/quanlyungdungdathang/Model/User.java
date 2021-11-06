package com.nhomduan.quanlyungdungdathang.Model;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class User implements Serializable {
    private String username;
    private String password;
    private String phone_number;
    private boolean enabled;

    private List<String> ma_sp_da_thich;
    private List<ThongTinGiaoHang> thong_tin_giao_hang;

    public User() {
    }

    public User(String username, String password) {
        this.username = username;
        this.password = password;
    }

    public User(String username, String password, String phone_number, boolean enabled) {
        this.username = username;
        this.password = password;
        this.phone_number = phone_number;
        this.enabled = enabled;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPhone_number() {
        return phone_number;
    }

    public void setPhone_number(String phone_number) {
        this.phone_number = phone_number;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public List<String> getMa_sp_da_thich() {
        return ma_sp_da_thich;
    }

    public void setMa_sp_da_thich(List<String> ma_sp_da_thich) {
        this.ma_sp_da_thich = ma_sp_da_thich;
    }

    public List<ThongTinGiaoHang> getThong_tin_giao_hang() {
        return thong_tin_giao_hang;
    }

    public void setThong_tin_giao_hang(List<ThongTinGiaoHang> thong_tin_giao_hang) {
        this.thong_tin_giao_hang = thong_tin_giao_hang;
    }

    @Override
    public String toString() {
        return "User{" +
                "username='" + username + '\'' +
                ", password='" + password + '\'' +
                ", phone_number='" + phone_number + '\'' +
                ", enabled=" + enabled +
                ", ma_sp_da_thich=" + ma_sp_da_thich +
                ", thong_tin_giao_hang=" + thong_tin_giao_hang +
                '}';
    }

    public Map<String, Object> toMap() {
        HashMap<String, Object> map = new HashMap<String, Object>();
        map.put("username", username);
        map.put("password", password);
        map.put("enable", enabled);
        map.put("phone_number", phone_number);
        return map;
    }
}
