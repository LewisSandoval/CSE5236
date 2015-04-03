package com.example.art.the_restaurant_guru;

import com.google.android.gms.maps.model.LatLng;

/**
 * Created by art on 4/1/2015.
 */
public class Restaurant {
    private int _id;
    private String name;
    private String address;
    private String price;
    private String foodType;
    private String rating;
    private String distance;
    private LatLng location;


    public void RestaurantCreateNewRep(int id,String name,String address,String price,String foodType,String rating,String distance, LatLng location){
        this._id = id;
        this.name = name;
        this.address = address;
        this.price = price;
        this.foodType = foodType;
        this.rating = rating;
        this.distance = distance;
        this.location = location;
    }
    public int get_id(){ return _id; }
    public void set_id(int id){ this._id = id; }

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

    public LatLng getLocation(){ return location;}
    public void setLocation(LatLng location){ this.location = location; }


}

