package com.nhomduan.quanlyungdungdathang.Model;

import java.util.HashMap;
import java.util.Map;

public class LoaiSP {

    private String id;
    private String name;
    private String hinhanh;

    public LoaiSP() {
    }


    public LoaiSP(String id, String name, String hinhanh) {
        this.id = id;
        this.name = name;
        this.hinhanh = hinhanh;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getHinhanh() {
        return hinhanh;
    }

    public void setHinhanh(String hinhanh) {
        this.hinhanh = hinhanh;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Map<String, Object> toMap() {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("id", id);
        map.put("name", name);
        map.put("hinhanh", hinhanh);
        return map;
    }
}
