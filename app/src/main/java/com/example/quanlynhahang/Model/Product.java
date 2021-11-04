package com.example.quanlynhahang.Model;

public class Product {
    private int id,price,img;
    private String name;

    public Product() {
    }

    public Product(int id,String name, int price, int img ) {
        this.id = id;
        this.price = price;
        this.img = img;
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public int getImg() {
        return img;
    }

    public void setImg(int img) {
        this.img = img;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
