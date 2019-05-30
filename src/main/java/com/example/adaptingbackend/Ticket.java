package com.example.adaptingbackend;

import android.content.pm.InstrumentationInfo;

public class Ticket {


    private String name;
    private String id;
    private String arena;
    private String date;
    private double price;
    private String time;
    private String longitude;
    private String latitude;

    public Ticket(String id, String name, String arena, String date, double price, String time, String longitude, String latitude) {
        this.id = id;
        this.name = name;
        this.arena = arena;
        this.date = date;
        this.price = price;
        this.time = time;
        this.longitude = longitude;
        this.latitude = latitude;
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getArena() {
        return arena;
    }

    public void setArena(String arena) {
        this.arena = arena;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }
}