package com.example.art.the_restaurant_guru;

/**
 * Created by art on 4/1/2015.
 */
public class Restaurant {
    private String name;
    private String address;
    private String price;
    private String foodType;
    private String rating;
    private String distance;

    public String getName(){
        return name;
    }
    public void setName(String name){
        this.name = name;
    }

    public String getAddress(){
        return address;
    }
    public void setAddress(String address){
        this.address = address;
    }

    public String getPrice(){
        return price;
    }
    public void setPrice(String price){
        this.price = price;
    }

    public String getFoodType(){
        return foodType;
    }
    public void setFoodType(String foodType){
        this.foodType = foodType;
    }

    public String getRating(){
        return rating;
    }
    public void setRating(String rating){
        this.rating = rating;
    }

    public String getDistance(){
        return distance;
    }

    public void setDistance(String distance) {
        this.distance = distance;
    }
}
