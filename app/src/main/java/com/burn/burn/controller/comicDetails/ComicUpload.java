package com.burn.burn.controller.comicDetails;

/**
 * Created by MichaelButnaru on 28/08/2018.
 */

public class ComicUpload {

    private String name;
    private double price;
    private String location;
    private String imageUrl;

    public ComicUpload(){

    }

    public ComicUpload(String name, double price, String location, String imageUrl) {
        this.name = name;
        this.price = price;
        this.location = location;
        this.imageUrl = imageUrl;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }
}
