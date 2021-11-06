package com.nhomduan.quanlyungdungdathang.Model;

import java.util.List;

public class Product {
    private int price;
    private String name;
    private String main_img;
    private List<String> detain_img;
    private String bao_quan;
    private String thong_tin_bao_quan;
    private String mota;
    private String loai_sp;
    private String gia_ban;
    private float khuyen_mai;
    private int rate;

    public Product() {
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMain_img() {
        return main_img;
    }

    public void setMain_img(String main_img) {
        this.main_img = main_img;
    }

    public List<String> getDetain_img() {
        return detain_img;
    }

    public void setDetain_img(List<String> detain_img) {
        this.detain_img = detain_img;
    }

    public String getBao_quan() {
        return bao_quan;
    }

    public void setBao_quan(String bao_quan) {
        this.bao_quan = bao_quan;
    }

    public String getThong_tin_bao_quan() {
        return thong_tin_bao_quan;
    }

    public void setThong_tin_bao_quan(String thong_tin_bao_quan) {
        this.thong_tin_bao_quan = thong_tin_bao_quan;
    }

    public String getMota() {
        return mota;
    }

    public void setMota(String mota) {
        this.mota = mota;
    }

    public String getLoai_sp() {
        return loai_sp;
    }

    public void setLoai_sp(String loai_sp) {
        this.loai_sp = loai_sp;
    }

    public String getGia_ban() {
        return gia_ban;
    }

    public void setGia_ban(String gia_ban) {
        this.gia_ban = gia_ban;
    }

    public float getKhuyen_mai() {
        return khuyen_mai;
    }

    public void setKhuyen_mai(float khuyen_mai) {
        this.khuyen_mai = khuyen_mai;
    }

    public int getRate() {
        return rate;
    }

    public void setRate(int rate) {
        this.rate = rate;
    }
}
