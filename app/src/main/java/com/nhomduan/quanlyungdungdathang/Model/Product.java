package com.nhomduan.quanlyungdungdathang.Model;

import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;
import java.util.List;

public class Product implements Serializable, Parcelable {
    private String name;
    private String image;
    private String bao_quan;
    private String thong_tin_bao_quan;
    private String mota;
    private String loai_sp;
    private int thoiGianCheBien;
    private int gia_ban;
    private float khuyen_mai;
    private int rate;
    private int so_luong_da_ban;

    public Product() {
    }

    protected Product(Parcel in) {
        name = in.readString();
        image = in.readString();
        bao_quan = in.readString();
        thong_tin_bao_quan = in.readString();
        mota = in.readString();
        loai_sp = in.readString();
        thoiGianCheBien = in.readInt();
        gia_ban = in.readInt();
        khuyen_mai = in.readFloat();
        rate = in.readInt();
        so_luong_da_ban = in.readInt();
    }

    public static final Creator<Product> CREATOR = new Creator<Product>() {
        @Override
        public Product createFromParcel(Parcel in) {
            return new Product(in);
        }

        @Override
        public Product[] newArray(int size) {
            return new Product[size];
        }
    };

    public int getThoiGianCheBien() {
        return thoiGianCheBien;
    }

    public void setThoiGianCheBien(int thoiGianCheBien) {
        this.thoiGianCheBien = thoiGianCheBien;
    }

    public void setGia_ban(int gia_ban) {
        this.gia_ban = gia_ban;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
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

    public int getGia_ban() {
        return gia_ban;
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

    public int getSo_luong_da_ban() {
        return so_luong_da_ban;
    }

    public void setSo_luong_da_ban(int so_luong_da_ban) {
        this.so_luong_da_ban = so_luong_da_ban;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(name);
        parcel.writeString(image);
        parcel.writeString(bao_quan);
        parcel.writeString(thong_tin_bao_quan);
        parcel.writeString(mota);
        parcel.writeString(loai_sp);
        parcel.writeInt(thoiGianCheBien);
        parcel.writeInt(gia_ban);
        parcel.writeFloat(khuyen_mai);
        parcel.writeInt(rate);
        parcel.writeInt(so_luong_da_ban);
    }
}
