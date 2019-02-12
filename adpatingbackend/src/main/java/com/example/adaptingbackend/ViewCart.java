package com.example.adaptingbackend;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.adaptingbackend.R;
import com.google.zxing.qrcode.encoder.QRCode;

import java.util.Iterator;
import java.util.Set;

import static com.example.adaptingbackend.MainShop.mCartItemCount;
import static com.example.adaptingbackend.MainShop.m_cart;


public class ViewCart extends AppCompatActivity {
    LinearLayout mparent;
    LayoutInflater layoutInflater;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_cart);

        Cart cart = m_cart;
        if (m_cart.getProducts().size() < 1) {
            Toast toast = Toast.makeText(getApplicationContext(), "ERROR: Cart is Empty", Toast.LENGTH_SHORT);
            toast.show();
            Intent intent = new Intent(this, MainShop.class);
            startActivity(intent);

        }
        mparent = findViewById(R.id.mparent);
        layoutInflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
        Set<Product> products = cart.getProducts();

        TextView welcome = (TextView) findViewById(R.id.welcome);
        SharedPrefManager.getUsername(this);
        String name = SharedPrefManager.getUsername(this);
        welcome.setText("Welcome "+name+" this is your cart");

        Iterator iterator = products.iterator();
        while (iterator.hasNext()) {
            Product product = (Product) iterator.next();
            View myview = layoutInflater.inflate(R.layout.cartrow, null, false);
            mparent.addView(myview);
            //set the views details to the products details



            ImageView imageView = (ImageView) myview.findViewById(R.id.image);
            imageView.setImageResource(product.getImageName());

            TextView textView = (TextView) myview.findViewById(R.id.name);
            textView.setText(product.get_name());

            TextView priceView = (TextView) myview.findViewById(R.id.price);
            priceView.setText(Double.toString(product.get_value()));

            TextView descView = (TextView) myview.findViewById(R.id.desc);
            descView.setText(product.getDescription());

            TextView quaView = (TextView) myview.findViewById(R.id.qty);
            quaView.setText(Integer.toString(cart.getQuantity(product)));

            TextView valueView = (TextView) findViewById(R.id.response);
            String price =String.format("%.2f", cart.getValue());
            valueView.setText("Total Cart Value = $ "+price);

            Button btn = (Button) myview.findViewById(R.id.btnRemove);
            ImageButton btnAdd = (ImageButton) myview.findViewById(R.id.btnAdd);
            ImageButton btnRemove = (ImageButton) myview.findViewById(R.id.btnRemoveOne);

            btn.setTag(product);
            btnAdd.setTag(product);
            btnRemove.setTag(product);


        }

    }

    public void deleteProdcut(View v) {
        Button button = v.findViewById(R.id.btnRemove);
        Product product = (Product) button.getTag();


        m_cart.removeFromCart(product);
        Toast toast = Toast.makeText(getApplicationContext(), "A " + product.get_name() + " been removed from your cart", Toast.LENGTH_SHORT);
        toast.show();
        Intent intent = new Intent(this, ViewCart.class);
        startActivity(intent);
    }

    public void deleteOneProduct(View v) {
        ImageButton button = v.findViewById(R.id.btnRemoveOne);
        Product product = (Product) button.getTag();
//
        m_cart.removeOneFromCart(product);
        mCartItemCount = mCartItemCount - 1;
        Toast toast = Toast.makeText(getApplicationContext(), "A " + product.get_name() + " been removed from your cart", Toast.LENGTH_SHORT);
        toast.show();
        Intent intent = new Intent(this, ViewCart.class);
        startActivity(intent);
    }

    public void addOneProduct(View v) {
        ImageButton button = v.findViewById(R.id.btnAdd);
        Product product = (Product) button.getTag();
//
        m_cart.addToCart(product);
        mCartItemCount = mCartItemCount + 1;
        Toast toast = Toast.makeText(getApplicationContext(), "A " + product.get_name() + " been added to your cart", Toast.LENGTH_SHORT);
        toast.show();
        Intent intent = new Intent(this, ViewCart.class);
        startActivity(intent);
    }

    public void homepage(View v){
        Intent intent = new Intent(this, MainShop.class);
        startActivity(intent);
    }

    public void checkoutWithCard(View v){
        Intent intent = new Intent(this, CardActivity.class);
        startActivity(intent);
    }

}
