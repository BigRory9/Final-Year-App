package com.example.adaptingbackend;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
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

import com.example.adaptingbackend.Database.Database;
import com.example.adaptingbackend.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class MainShop extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    LinearLayout mparent, sparent;
    TextView m_response;
    String email, user_id;
    private DrawerLayout drawer;

    TextView textCartItemCount;
    public static int mCartItemCount = 0;


    LayoutInflater layoutInflater;

    int i = 0;

    JSONObject jsonObject;
    JSONArray jsonArray;

    ArrayList<Product> drinkList = new ArrayList<Product>();
    ArrayList<Product> foodList = new ArrayList<Product>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shop);

        email = SharedPrefManager.getEmail(this);
//        BackgroundWorker backgroundWorker = new BackgroundWorker(this);
//        backgroundWorker.execute("getUserId", email);
//
//        m_cart = new Cart();
//        m_response = (TextView) findViewById(R.id.response);


        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mparent = findViewById(R.id.mparent);
        sparent = findViewById(R.id.sparent);
        layoutInflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
        new JSONgetUserId().execute();


        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                MainShop.this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        new JSONBackgroundWorker().execute();


    }

    public boolean onNavigationItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.nav_first_layout) {
            Toast.makeText(this, "Closing Drawer",
                    Toast.LENGTH_LONG).show();
            drawer.closeDrawers();
        }
        else if (id == R.id.nav_view_orders) {
            Toast.makeText(this, "Attempting to view your Orders....",
                    Toast.LENGTH_LONG).show();
            Intent i = new Intent(this, ViewOrders.class);
            startActivity(i);
        }else if (id == R.id.nav_second_layout) {
            Toast.makeText(this, "Attempting to view your Tickets....",
                    Toast.LENGTH_LONG).show();
            Intent i = new Intent(this, ViewTickets.class);
            startActivity(i);
        } else if (id == R.id.logout) {
            Toast.makeText(this, "Logging out now  user "+email, Toast.LENGTH_LONG).show();
            SharedPrefManager.saveEmail("",this);
            SharedPrefManager.saveOrderID("",this);
            SharedPrefManager.saveUserID("",this);
            Intent i = new Intent(this, MainActivity.class);
            startActivity(i);
        }


        return false;
    }

    public void startUp() {
        String imageName = "";
        for (int i = 0; i < drinkList.size(); i++) {
            View myview = layoutInflater.inflate(R.layout.myrow, null, false);

            Toast.makeText(this, "Attempting to view your Tickets...." + user_id,
                    Toast.LENGTH_LONG).show();
            SharedPrefManager.saveUserID(user_id, this);


            mparent.addView(myview);
            Product product = drinkList.get(i);


            int res = getResources().getIdentifier(product.get_name().toLowerCase(), "drawable", this.getPackageName());
//            imageview = (ImageView) findViewById(R.id.imageView);
//            imageview.setImageResource(res);
            ImageView imageView = (ImageView) myview.findViewById(R.id.image);
            imageView.setImageResource(res);
//
            TextView textView = (TextView) myview.findViewById(R.id.name);
            textView.setText(drinkList.get(i).get_name());

            TextView priceView = (TextView) myview.findViewById(R.id.price);
            priceView.append(Double.toString(drinkList.get(i).get_value()));

            ImageButton button = (ImageButton) myview.findViewById(R.id.button);
            button.setTag(drinkList.get(i));


        }

        for (int i = 0; i < foodList.size(); i++) {
            View secondView = layoutInflater.inflate(R.layout.myrow, null, false);

            Product product = foodList.get(i);
            sparent.addView(secondView);
            String name = product.get_name();
            name = name.replace(" ", "");
            int res = getResources().getIdentifier(name.toLowerCase(), "drawable", this.getPackageName());
            ImageView imageView = (ImageView) secondView.findViewById(R.id.image);
            imageView.setImageResource(res);
//
            TextView textView = (TextView) secondView.findViewById(R.id.name);
            textView.setText(foodList.get(i).get_name());

            TextView priceView = (TextView) secondView.findViewById(R.id.price);
            priceView.append(Double.toString(foodList.get(i).get_value()));

            ImageButton button = (ImageButton) secondView.findViewById(R.id.button);
            button.setTag(foodList.get(i));


        }
    }


    public void addProduct(View v) {
        ImageButton button = v.findViewById(R.id.button);
        Product product = (Product) button.getTag();

        new Database(this).addToCart(new Order(product.get_name()
                , "1", String.valueOf(product.get_value()), product.getId()));
        i++;
        mCartItemCount = mCartItemCount + 1;
        setupBadge();

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
        int id = item.getItemId() + 1;

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


    public ArrayList<Product> parseJSON(String JSON_STRING, int amount) {

        if (amount == 0) {
            try {
                jsonObject = new JSONObject(JSON_STRING);
                jsonArray = jsonObject.getJSONArray("server_response");
                int count = 0;
                String id, name, value, desc;
                String food;
                boolean type = true;
                while (count < jsonArray.length()) {
                    JSONObject JO = jsonArray.getJSONObject(count);
                    id = JO.getString("id");
                    name = JO.getString("name");
                    value = JO.getString("value");
                    desc = JO.getString("description");
                    food = JO.getString("type");
                    if (food.equals("0")) {
                        type = false;

                    } else {
                        type = true;
                    }
                    double price = Double.parseDouble(value);

                    Product prod = new Product(id, name, price, desc, type);
                    if (prod.isFood() == false) {
                        drinkList.add(prod);
                    } else {
                        foodList.add(prod);
                    }

                    count++;
                }
                startUp();
                return drinkList;
            } catch (JSONException e) {
                e.printStackTrace();
            }
        } else if (amount == 1) {

            try {
                jsonObject = new JSONObject(JSON_STRING);
                jsonArray = jsonObject.getJSONArray("server_response");
                int count = 0;
                boolean type = true;
                while (count < jsonArray.length()) {
                    JSONObject JO = jsonArray.getJSONObject(count);
                    user_id = JO.getString("id");
                    Toast.makeText(this, "Your ID " + user_id,
                            Toast.LENGTH_SHORT).show();

                    //
                    count++;
                }
                //     startUp();
                return null;
            } catch (JSONException e) {
                e.printStackTrace();
            }

        }
        return null;
    }

    public class JSONBackgroundWorker extends AsyncTask<Void, Void, String> {
        String JSON_URL;
        String JSON_STRING;
        JSONObject jsonObject;
        JSONArray jsonArray;
//        ArrayList<Product> list = new ArrayList<Product>();

        @Override
        protected void onPreExecute() {
            JSON_URL = "http://10.0.2.2/display_products.php";
        }

        @Override
        protected String doInBackground(Void... params) {
            try {
                URL url = new URL(JSON_URL);
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                httpURLConnection.setRequestMethod("GET");
                InputStream inputStream = new BufferedInputStream(httpURLConnection.getInputStream());
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
                StringBuilder stringBuilder = new StringBuilder();

                while ((JSON_STRING = bufferedReader.readLine()) != null) {
                    stringBuilder.append(JSON_STRING + "\n");
                }
                bufferedReader.close();
                inputStream.close();
                httpURLConnection.disconnect();
                JSON_STRING = stringBuilder.toString().trim();
                return stringBuilder.toString().trim();
            } catch (Exception e) {
                e.printStackTrace();
            }
            return " ERROR";
        }

        @Override
        protected void onProgressUpdate(Void... values) {
            super.onProgressUpdate(values);
        }

        @Override
        protected void onPostExecute(String result) {

            drinkList = parseJSON(result, 0);

        }
    }

    public class JSONgetUserId extends AsyncTask<String, Void, String> {
        String JSON_URL;
        String JSON_STRING;
        JSONObject jsonObject;
        JSONArray jsonArray;
//        ArrayList<Product> list = new ArrayList<Product>();

        @Override
        protected void onPreExecute() {
            JSON_URL = "http://10.0.2.2/getUsersid.php?email=" + email;
        }

        @Override
        protected String doInBackground(String... params) {
            try {
                URL url = new URL(JSON_URL);
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                httpURLConnection.setRequestMethod("GET");
                InputStream inputStream = new BufferedInputStream(httpURLConnection.getInputStream());
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
                StringBuilder stringBuilder = new StringBuilder();


                while ((JSON_STRING = bufferedReader.readLine()) != null) {
                    stringBuilder.append(JSON_STRING + "\n");
                }
                bufferedReader.close();
                inputStream.close();
                httpURLConnection.disconnect();
                JSON_STRING = stringBuilder.toString().trim();
                return stringBuilder.toString().trim();
            } catch (Exception e) {
                e.printStackTrace();
            }
            return " ERROR";
        }

        @Override
        protected void onProgressUpdate(Void... values) {
            super.onProgressUpdate(values);
        }

        @Override
        protected void onPostExecute(String result) {
            parseJSON(result, 1);

        }
    }
}
//JSONgetUserId()