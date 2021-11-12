package com.nhomduan.quanlyungdungdathang.Model;

public enum TrangThai {
    CXN("Chưa xác nhận"), CB("Chế biến"), DGH("Đang giao hàng"), GH("Giao hàng"), HT("hoàn thành"), HD("Hủy đơn");

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
