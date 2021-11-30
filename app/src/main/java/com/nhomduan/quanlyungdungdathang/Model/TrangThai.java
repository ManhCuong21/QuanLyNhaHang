package com.nhomduan.quanlyungdungdathang.Model;

import java.io.Serializable;

public enum TrangThai implements Serializable {
    CXN("Chưa xác nhận"), CB("Chế biến"), DGH("Đang giao hàng"), HT("Hoàn thành"), HD("Hủy đơn");

    private String trangThai;

    private TrangThai(String trangThai) {
        this.trangThai = trangThai;
    }

    public String getTrangThai() {
        return trangThai;
    }

    public void setTrangThai(String trangThai) {
        this.trangThai = trangThai;
    }
}
