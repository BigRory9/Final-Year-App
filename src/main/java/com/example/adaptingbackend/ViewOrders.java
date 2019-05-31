package com.example.adaptingbackend;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.http.HttpClient;
import com.amazonaws.http.HttpResponse;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.GetObjectRequest;
import com.amazonaws.services.s3.model.S3Object;

import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.io.Writer;
import java.lang.reflect.Array;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class ViewOrders extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {


    private LinearLayout mparent;
    private ArrayList<ReadOrdersFromDB> combinedOrders = new ArrayList<ReadOrdersFromDB>();
    private ArrayList<ReadOrdersFromDB> orderList = new ArrayList<ReadOrdersFromDB>();
    private ArrayList<Product> products = new ArrayList<Product>();
    private JSONObject jsonObject;
    private JSONArray jsonArray;
    private LayoutInflater layoutInflater;
    private RecyclerView recyclerView;
    private String id;
    private DrawerLayout drawer;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_orders);
        id = SharedPrefManager.getUserID(this);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        mparent = findViewById(R.id.mparent);
        layoutInflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);

        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                com.example.adaptingbackend.ViewOrders.this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(com.example.adaptingbackend.ViewOrders.this);

        new com.example.adaptingbackend.ViewOrders.JSONBackgroundWorkerProducts().execute();

    }

    public void startUp() {
         OrderAdapter adapter = new OrderAdapter(this, orderList, products);
        System.out.print("HELLO");
        //setting adapter to recyclerview
         recyclerView.setAdapter(adapter);
    }

    public boolean onNavigationItemSelected(MenuItem item) {
        int id = item.getItemId();
        int count = 0;

        if (id == R.id.nav_first_layout) {
            Toast.makeText(this, "Purchase food and drinks",
                    Toast.LENGTH_LONG).show();
            Intent i = new Intent(this, MainShop.class);
            startActivity(i);
        } else if (id == R.id.nav_second_layout) {
            Toast.makeText(this, "Closing Drawer",
                    Toast.LENGTH_LONG).show();
            drawer.closeDrawers();
        } else if (id == R.id.logout) {
            String email = SharedPrefManager.getEmail(this);
            Toast.makeText(this, "Logging out now  user " + email, Toast.LENGTH_LONG).show();
            SharedPrefManager.saveEmail("", this);
            SharedPrefManager.saveOrderID("", this);
            SharedPrefManager.saveUserID("", this);
            Intent i = new Intent(this, MainActivity.class);
            startActivity(i);
        }


        return false;
    }


    public ArrayList<ReadOrdersFromDB> parseJSON(String JSON_STRING) {


        try {
            JSONObject jsonObject = new JSONObject(JSON_STRING);
            JSONArray jsonArray = jsonObject.getJSONArray("server_response");
            int count = 0;
            String id, product_id, productQuantity, code;
            boolean type = true;
            while (count < jsonArray.length()) {
                JSONObject JO = jsonArray.getJSONObject(count);
                id = JO.getString("order_ID");
                product_id = JO.getString("product_ID");

                productQuantity = JO.getString("productQuantity");

                code= JO.getString("code");

                ArrayList<String> prod = new ArrayList<String>();

                ArrayList<String> quantity = new ArrayList<String>();
                ReadOrdersFromDB order = new ReadOrdersFromDB(id, prod, quantity,code);
                boolean found = false;
                int num = 0;
                for (int i = 0; i < orderList.size(); i++) {
                    if (id.equals(orderList.get(i).getOrder_id())) {
                        found = true;
                        num = i;
                        break;
                    }
                }
                if (orderList.size() == 0) {
                    prod.add(product_id);
                    quantity.add(productQuantity);
                    orderList.add(order);
                } else if (found == true) {
                    orderList.get(num).getProduct_id().add(product_id);
                    orderList.get(num).getProductQuantity().add(productQuantity);
                    found = false;
                } else {
                    prod.add(product_id);
                    quantity.add(productQuantity);
                    orderList.add(order);
                }


                count++;
            }


            startUp();
//                return ticketList;
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return null;
    }

    public class JSONBackgroundWorker extends AsyncTask<Void, Void, String> {
        String JSON_URL;
        String JSON_STRING;

//        ArrayList<Product> list = new ArrayList<Product>();

        @Override
        protected void onPreExecute() {
            JSON_URL = "http://147.252.148.154/getUsersOrders.php?id=" + id;
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

            orderList = parseJSON(result);


        }
    }

    public ArrayList<Product> parseJSONProducts(String JSON_STRING) {
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

                products.add(prod);


                count++;
            }

            new com.example.adaptingbackend.ViewOrders.JSONBackgroundWorker().execute();
            return products;
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }

    public class JSONBackgroundWorkerProducts extends AsyncTask<Void, Void, String> {
        String JSON_URL;
        String JSON_STRING;
        JSONObject jsonObject;
        JSONArray jsonArray;
//        ArrayList<Product> list = new ArrayList<Product>();

        @Override
        protected void onPreExecute() {
            JSON_URL = "http://147.252.148.154/display_products.php";
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

            products = parseJSONProducts(result);

        }
    }



}



