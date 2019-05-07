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

import com.example.adaptingbackend.Database.Database;
import com.example.adaptingbackend.R;
import com.google.zxing.qrcode.encoder.QRCode;

import java.util.Iterator;
import java.util.List;
import java.util.Set;

import static com.example.adaptingbackend.MainShop.mCartItemCount;


public class ViewCart extends AppCompatActivity {
  LinearLayout mparent;
  LayoutInflater layoutInflater;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_view_cart);

    List<Order> m_cart = new Database(this).getCarts();
    if (m_cart.size() < 1) {
      Toast toast = Toast.makeText(getApplicationContext(), "ERROR: Cart is Empty", Toast.LENGTH_SHORT);
      toast.show();
      Intent intent = new Intent(this, MainShop.class);
      startActivity(intent);

    }
    mparent = findViewById(R.id.mparent);
    layoutInflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
//        Set<Product> products = cart.getProducts();

    TextView welcome = (TextView) findViewById(R.id.welcome);
    SharedPrefManager.getEmail(this);
    String name = SharedPrefManager.getEmail(this);
    welcome.setText("Welcome "+name+" this is your cart");

    Iterator iterator = m_cart.iterator();
    while (iterator.hasNext()) {
      Order order = (Order) iterator.next();
      View myview = layoutInflater.inflate(R.layout.cartrow, null, false);
      mparent.addView(myview);
      //set the views details to the products details



//            ImageView imageView = (ImageView) myview.findViewById(R.id.image);
//            imageView.setImageResource(product.getImageName());

      TextView textView = (TextView) myview.findViewById(R.id.name);
      textView.setText(order.getProductName());
//
      TextView priceView = (TextView) myview.findViewById(R.id.price);
      priceView.setText(order.getPrice());
//
//            TextView descView = (TextView) myview.findViewById(R.id.desc);
//            descView.setText(product.getDescription());
//
      TextView quaView = (TextView) myview.findViewById(R.id.qty);
      quaView.setText(order.getQuantity());
////
      TextView valueView = (TextView) findViewById(R.id.response);
      String price = new Database(this).getPrice();
      valueView.setText("Total Cart Value = € "+price);
//
      Button btn = (Button) myview.findViewById(R.id.btnRemove);
      ImageButton btnAdd = (ImageButton) myview.findViewById(R.id.btnAdd);
      ImageButton btnRemove = (ImageButton) myview.findViewById(R.id.btnRemoveOne);

      btn.setTag(order.getProductName());
      btnAdd.setTag(order.getProductName());
      btnRemove.setTag(order.getProductName());

      btn.setTag(order.getProductName());
      btnAdd.setTag(order.getProductName());
      btnRemove.setTag(order.getProductName());


    }

  }

  public void deleteProdcut(View v) {
    Button button = v.findViewById(R.id.btnRemove);
    String product = (String) button.getTag();


    new Database(this).deleteProduct(product);
    Toast toast = Toast.makeText(getApplicationContext(), "A " + product + " been removed from your cart", Toast.LENGTH_SHORT);
    toast.show();
    Intent intent = new Intent(this, ViewCart.class);
    startActivity(intent);
  }

  public void deleteOneProduct(View v) {
    ImageButton button = v.findViewById(R.id.btnRemoveOne);
    String product = (String) button.getTag();

    new Database(this).minusOneToProduct(product);
    Toast toast = Toast.makeText(getApplicationContext(), "A " + product + " been taken from your cart", Toast.LENGTH_SHORT);
    toast.show();
    Intent intent = new Intent(this, ViewCart.class);
    startActivity(intent);
  }

  public void addOneProduct(View v) {
    ImageButton button = v.findViewById(R.id.btnAdd);
    String product = (String) button.getTag();

    new Database(this).addOneToProduct(product);
    Toast toast = Toast.makeText(getApplicationContext(), "A " + product + " been added to your cart", Toast.LENGTH_SHORT);
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