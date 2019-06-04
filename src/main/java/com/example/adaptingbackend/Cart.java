package com.example.adaptingbackend;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

import static com.example.adaptingbackend.MainShop.mCartItemCount;

public class Cart {

    private Map<Product, Integer> m_cart;
    private double m_value =0;

    private Cart(){
        m_cart= new LinkedHashMap<>();
    }

    void addToCart(Product product){
        if(m_cart.containsKey(product))
            m_cart.put(product,m_cart.get(product) + 1);
        else
            m_cart.put(product,1);

        m_value += product.get_value();
    }

    void removeFromCart(Product product) {
        mCartItemCount= mCartItemCount-m_cart.get(product).intValue();
        m_value -= product.get_value()*m_cart.get(product).intValue();
        m_cart.remove(product);

    }

    void removeOneFromCart(Product product){
        if(m_cart.get(product)!=1) {
            m_cart.put(product, m_cart.get(product) - 1);
        }
        else {
            m_cart.remove(product);
        }
        m_value -= product.get_value();
    }

    int getQuantity(Product product)
    {
        return m_cart.get(product);
    }

    Set getProducts()
    {
        return m_cart.keySet();
    }


    void empty(){
        m_cart.clear();
        m_value=0;
        mCartItemCount=0;
    }

    double getValue()
    {
        return m_value;
    }

    int getSize()
    {
        return m_cart.size();
    }

    public Map<Product, Integer> getCart(){
        return m_cart;
    }

}