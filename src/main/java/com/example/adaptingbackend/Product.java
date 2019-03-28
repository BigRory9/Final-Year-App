package com.example.adaptingbackend;

public class Product {
    String id;
    String m_name;
    double m_value;
    int imageName;
    String description;
    boolean food;

    public boolean isFood() {
        return food;
    }

    public void setFood(boolean food) {
        this.food = food;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Product(String id, String m_name, double m_value, String description,boolean food) {
        this.id = id;
        this.m_name = m_name;
        this.m_value = m_value;
        this.description = description;
        this.food = food;
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

