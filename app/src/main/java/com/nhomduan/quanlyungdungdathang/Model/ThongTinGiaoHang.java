package com.nhomduan.quanlyungdungdathang.Model;

public class ThongTinGiaoHang {
    private String diaChi;
    private String hoTen;
    private String phone_number;

    public ThongTinGiaoHang() {
    }

    public ThongTinGiaoHang(String diaChi, String hoTen, String phone_number) {
        this.diaChi = diaChi;
        this.hoTen = hoTen;
        this.phone_number = phone_number;
    }

    public String getDiaChi() {
        return diaChi;
    }

    public void setDiaChi(String diaChi) {
        this.diaChi = diaChi;
    }

    public String getHoTen() {
        return hoTen;
    }

    public void setHoTen(String hoTen) {
        this.hoTen = hoTen;
    }

    public String getPhone_number() {
        return phone_number;
    }

    public void setPhone_number(String phone_number) {
        this.phone_number = phone_number;
    }
}
