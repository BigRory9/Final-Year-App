package com.example.adaptingbackend;

import android.annotation.TargetApi;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.adaptingbackend.Database.Database;
import com.example.adaptingbackend.R;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.encoder.QRCode;
import com.journeyapps.barcodescanner.BarcodeEncoder;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;


public class QrCode extends AppCompatActivity {

    private ImageView imageView;
    private String value = "Order Information\n";
    private String orderID = "87", name;

    private static final String TAG = "MainActivity";

    private static final int ERROR_DIALOG_REQUEST = 9001;

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qr_code);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        List<Order> m_cart = new Database(this).getCarts();
//        Set<Product> products = cart.getProducts();
        Iterator iterator = m_cart.iterator();
        String pin = generatePIN();
        System.out.print(pin);


        while (iterator.hasNext()) {
            Order order = (Order) iterator.next();

            // makes it only add the product once
            if (!value.contains(order.getProductName())) {
                BackgroundWorker backgroundWorker = new BackgroundWorker(this);
                String type = "order";



                backgroundWorker.execute(type, order.getProduct_id(), order.getQuantity(), SharedPrefManager.getOrderID(this), pin);
                value = value + "\nProduct Name : " + order.getProductName() + " \nAmount = " + order.getQuantity() + "\n";
            }

        }
        new Connection().execute(pin);


        new Database(this).clearCart();
    }

    public void homepage(View v) {
        Toast toast = Toast.makeText(getApplicationContext(), "This is a message displayed in a Toast " + orderID, Toast.LENGTH_SHORT);
        toast.show();
        startActivity(new Intent(this, MainShop.class));
    }

    public String generatePIN() {

        //generate a 4 digit integer 1000 <10000
        int randomPIN = (int) (Math.random() * 9000) + 1000;

        return Integer.toString(randomPIN);
    }

    private class Connection extends AsyncTask {



        @Override
        protected Object doInBackground(Object[] objects) {
                String pin =objects[0].toString();
                String email = SharedPrefManager.getEmail(getApplicationContext());
                Mail m = new Mail("gigzeaze@gmail.com", "Gigzeaze123!");

                String[] toArr = {email};
                m.setTo(toArr);
                m.setFrom("gigzeaze@gmail.com");
                m.setSubject("This is an email sent by GigzEaze to collect your items");
                m.setBody("The code you will need to collect your order is "+pin);

                try {
                    m.send();
                } catch (Exception e) {

                    Log.e("MailApp", "Could not send email", e);
                }
                return null;
            }
        }

}