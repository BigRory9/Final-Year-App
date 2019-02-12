package com.example.myapplication;

import android.media.Image;

public class Product {

    String m_name;
    double m_value;
    int imageName;
    String description;

    public Product(String m_name, double m_value, int imageName, String description) {
        this.m_name = m_name;
        this.m_value = m_value;
        this.imageName = imageName;
        this.description = description;
    }

    public String get_name() {
        return m_name;
    }

    public void set_name(String m_name) {
        this.m_name = m_name;
    }

    public double get_value() {
        return m_value;
    }

    public void set_value(double m_value) {
        this.m_value = m_value;
    }

    public int getImageName() {
        return imageName;
    }

    public void setImageName(int imageName) {
        this.imageName = imageName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}

