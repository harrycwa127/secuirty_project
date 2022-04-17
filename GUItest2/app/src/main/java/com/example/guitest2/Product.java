package com.example.guitest2;

public class Product {
    String description;
    String image;
    String prize;
    String title;

    public Product(){

    }

    public Product(String description, String imageString, String price, String title){
        this.description = description;
        this.image = imageString;
        this.prize = price;
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public String getImageString() {
        return image;
    }

    public String getPrize() {
        return prize;
    }

    public String getTitle() {
        return title;
    }
}
