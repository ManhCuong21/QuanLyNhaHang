package com.nhomduan.quanlyungdungdathang.Model;

public class DonHangChiTiet {
    private String id;
    private String ma_sp;
    private String so_luong;

    public DonHangChiTiet(String id, String ma_sp, String so_luong) {
        this.id = id;
        this.ma_sp = ma_sp;
        this.so_luong = so_luong;
    }


    public DonHangChiTiet() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getMa_sp() {
        return ma_sp;
    }

    public void setMa_sp(String ma_sp) {
        this.ma_sp = ma_sp;
    }

    public String getSo_luong() {
        return so_luong;
    }

    public void setSo_luong(String so_luong) {
        this.so_luong = so_luong;
    }
}
