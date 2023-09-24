package com.nsb.restaurant.model;

import java.io.Serializable;

public class FoodModel implements Serializable {
    String id, name, price, image;
    int numOfFood;
    String saleOff, priceSaleOff;

    public FoodModel() {
    }

    public FoodModel(String id, int numOfFood) {
        this.id = id;
        this.numOfFood = numOfFood;
    }

    public FoodModel(String id, String name, String price, String image, int numOfFood) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.image = image;
        this.numOfFood = numOfFood;
    }

    public FoodModel(String id, String name, String price, String image) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.image = image;
    }

    public FoodModel(String id, String name, String image) {
        this.id = id;
        this.name = name;
        this.image = image;
    }

    public FoodModel(String name, String price, int numOfFood) {
        this.name = name;
        this.price = price;
        this.numOfFood = numOfFood;
    }

    public FoodModel(String id, String name, String price, String image, String saleOff, String priceSaleOff) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.image = image;
        this.saleOff = saleOff;
        this.priceSaleOff = priceSaleOff;
    }

    public String getSaleOff() {
        return saleOff;
    }

    public void setSaleOff(String saleOff) {
        this.saleOff = saleOff;
    }

    public String getPriceSaleOff() {
        return priceSaleOff;
    }

    public void setPriceSaleOff(String priceSaleOff) {
        this.priceSaleOff = priceSaleOff;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String ten) {
        this.name = ten;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String gia) {
        this.price = gia;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public int getNumOfFood() {
        return numOfFood;
    }

    public void setNumOfFood(int numOfFood) {
        this.numOfFood = numOfFood;
    }

    @Override
    public String toString() {
        return "FoodModel{" +
                "name='" + name + '\'' +
                ", price='" + price + '\'' +
                ", numOfFood=" + numOfFood +
                '}';
    }
}
