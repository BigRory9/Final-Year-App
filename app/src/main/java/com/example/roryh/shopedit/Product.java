package com.example.roryh.shopedit;

public class Product {

    String m_name;
    double m_value;

    public Product(String m_name, double m_value) {
        this.m_name = m_name;
        this.m_value = m_value;
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
}
