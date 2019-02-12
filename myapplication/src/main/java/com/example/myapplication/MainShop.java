package com.example.myapplication;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

public class MainShop extends AppCompatActivity {

    LinearLayout mparent, sparent;
    TextView m_response;

    TextView textCartItemCount;
    public static int mCartItemCount = 0;

    private Cart[] cart;
    private int anInt=0;

    LayoutInflater layoutInflater;

    static Cart m_cart = new Cart();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shop);
//
//        m_cart = new Cart();
//        m_response = (TextView) findViewById(R.id.response);


        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mparent = findViewById(R.id.mparent);
        sparent = findViewById(R.id.sparent);
        layoutInflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);

        int burgerID = getResources().getIdentifier("burger", "drawable", getPackageName());
        int wingsID = getResources().getIdentifier("wings", "drawable", getPackageName());
        int clubID = getResources().getIdentifier("club", "drawable", getPackageName());
        int soupID = getResources().getIdentifier("soup", "drawable", getPackageName());

        //Sides
        int chipID = getResources().getIdentifier("chips", "drawable", getPackageName());
        int sweetID = getResources().getIdentifier("sweet", "drawable", getPackageName());
        int mexicanID = getResources().getIdentifier("mexican", "drawable", getPackageName());
        int saladID = getResources().getIdentifier("salad", "drawable", getPackageName());
        Log.d("ADebugTag", "Burger Value: " + Float.toString(burgerID));
        Log.d("ADebugTag", "Wings Value: " + Float.toString(wingsID));
        Log.d("ADebugTag", "Club Value: " + Float.toString(clubID));
        Log.d("ADebugTag", "Soup Value: " + Float.toString(soupID));
        Product mains[] = {
                new Product("Burger", 20.20, burgerID,"Pure Irish Organic Quarter Pounder Beef Burgers are ground from selected organic beef cuts"),
                new Product("Wings", 15.00, wingsID,"Hefty classic organic buffalo wings"),
                new Product("Club Sandwich", 9.00, clubID,"Sambo"),
                new Product("Soup", 7.50, soupID,"Soup of the day")
        };

        Product sides[] = {
                new Product("Fries", 5.00, chipID,"Pure Irish Organic Chips"),
                new Product("Sweet Fries", 5.00, sweetID,"Hefty Special"),
                new Product("Mexixan Rice", 4.00, mexicanID,"Freshly Sourced Mexican Rice"),
                new Product("Salad", 6.50, saladID,"Juicy Salad")
        };





        for (int i = 0; i < mains.length; i++) {
            View myview = layoutInflater.inflate(R.layout.myrow, null, false);


            mparent.addView(myview);


            ImageView imageView = (ImageView) myview.findViewById(R.id.image);
            imageView.setImageResource(mains[i].getImageName());

            TextView textView = (TextView) myview.findViewById(R.id.name);
            textView.setText(mains[i].m_name);

            TextView priceView = (TextView) myview.findViewById(R.id.price);
            priceView.append(Double.toString(mains[i].m_value));

            ImageButton button = (ImageButton)  myview.findViewById(R.id.button);
            button.setTag(mains[i]);


        }

        for (int i = 0; i < sides.length; i++) {
            View secondView = layoutInflater.inflate(R.layout.myrow, null, false);


            sparent.addView(secondView);
            ImageView imageView = (ImageView) secondView.findViewById(R.id.image);
            imageView.setImageResource(sides[i].getImageName());

            TextView textView = (TextView) secondView.findViewById(R.id.name);
            textView.setText(sides[i].m_name);

            TextView priceView = (TextView) secondView.findViewById(R.id.price);
            priceView.append(Double.toString(sides[i].m_value));

            ImageButton button = (ImageButton)  secondView.findViewById(R.id.button);
            button.setTag(sides[i]);



        }



    }

    public void addProduct(View v) {
        ImageButton button = v.findViewById(R.id.button);
        Product product = (Product) button.getTag();
//
        m_cart.addToCart(product);
        mCartItemCount=mCartItemCount+1;
        setupBadge();
        Toast toast = Toast.makeText(getApplicationContext(), "Product "+product.get_name()+" has been added to your cart", Toast.LENGTH_SHORT); toast.show();
//        m_response.setText("Total cart value = $"+m_cart.getValue());

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);

        final MenuItem menuItem = menu.findItem(R.id.action_cart);

        View actionView = menuItem.getActionView();
        textCartItemCount = (TextView) actionView.findViewById(R.id.cart_badge);

        setupBadge();

        actionView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onOptionsItemSelected(menuItem);
            }
        });

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.

        switch (item.getItemId()) {

            case R.id.action_cart: {
                // Do something
                Intent intent = new Intent(this, ViewCart.class);
                startActivity(intent);
                return true;
            }
        }
        int id = item.getItemId()+1;

        return super.onOptionsItemSelected(item);
    }

    private void setupBadge() {

        if (textCartItemCount != null) {
            if (mCartItemCount == 0) {
                if (textCartItemCount.getVisibility() != View.GONE) {
                    textCartItemCount.setVisibility(View.GONE);
                }
            } else {
                textCartItemCount.setText(String.valueOf(Math.min(mCartItemCount, 99)));
                if (textCartItemCount.getVisibility() != View.VISIBLE) {
                    textCartItemCount.setVisibility(View.VISIBLE);
                }
            }
        }
    }

    public Cart[] getCart() {
        return cart;
    }

    public void addOrder(Cart cart) {
        this.cart[anInt]=cart;
        anInt++;

    }
}
