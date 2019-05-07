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
    String value = "Order Information\n";
    String orderID = "87", name;

    private static final String TAG = "MainActivity";

    private static final int ERROR_DIALOG_REQUEST = 9001;

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qr_code);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        imageView = findViewById(R.id.imageView);
        List<Order> m_cart = new Database(this).getCarts();
//        Set<Product> products = cart.getProducts();
        Iterator iterator = m_cart.iterator();
        String pin = generatePIN();
        System.out.print(pin);


        while (iterator.hasNext()) {
            Order order = (Order) iterator.next();
//            Toast.makeText(this,
//                    m_order.getSize(), Toast.LENGTH_LONG).show();


            // makes it only add the product once
            if (!value.contains(order.getProductName())) {
                //make a call to backgroundWorker which will have to take
                //User_id,and the ids of the products
                BackgroundWorker backgroundWorker = new BackgroundWorker(this);
                String type = "order";
                // I need the product Ids and product quantity and orderID

                Toast toast = Toast.makeText(getApplicationContext(), "This is a message displayed in a Toast " + order.getProduct_id(), Toast.LENGTH_SHORT);
                toast.show();
                //get Product Id + product quantity
                // order.getProductName();


                backgroundWorker.execute(type, order.getProduct_id(), order.getQuantity(), SharedPrefManager.getOrderID(this), pin);
                value = value + "\nProduct Name : " + order.getProductName() + " \nAmount = " + order.getQuantity() + "\n";
            }

        }
        new Connection().execute(pin);


        new Database(this).clearCart();
//

        MultiFormatWriter multiFormatWriter = new MultiFormatWriter();
        try {
            BitMatrix bitMatrix = multiFormatWriter.encode(value, BarcodeFormat.QR_CODE, 500, 500);
            BarcodeEncoder barcodeEncoder = new BarcodeEncoder();
            Bitmap bitmap = barcodeEncoder.createBitmap(bitMatrix);
            imageView.setImageBitmap(bitmap);
        } catch (WriterException e) {
            e.printStackTrace();
        }

        if (isServicesOK()) {
            init();
        }


    }

    private void init() {
        Button btnMap = (Button) findViewById(R.id.btnMap);
        btnMap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(QrCode.this, MapActivity.class);
                startActivity(intent);
            }
        });
    }


    public boolean isServicesOK() {
        Log.d(TAG, "isServicesOK: checking google services version");

        int available = GoogleApiAvailability.getInstance().isGooglePlayServicesAvailable(QrCode.this);

        if (available == ConnectionResult.SUCCESS) {
            //everything is fine and the user can make map requests
            Log.d(TAG, "isServicesOK: Google Play Services is working");
            return true;
        } else if (GoogleApiAvailability.getInstance().isUserResolvableError(available)) {
            //an error occured but we can resolve it
            Log.d(TAG, "isServicesOK: an error occured but we can fix it");
            Dialog dialog = GoogleApiAvailability.getInstance().getErrorDialog(QrCode.this, available, ERROR_DIALOG_REQUEST);
            dialog.show();
        } else {
            Toast.makeText(this, "You can't make map requests", Toast.LENGTH_SHORT).show();
        }
        return false;
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
                Mail m = new Mail("rjharford@gmail.com", "dadadada1");

                String[] toArr = {"roryharford@live.ie"};
                m.setTo(toArr);
                m.setFrom("rjharford@gmail.com");
                m.setSubject("This is an email sent by GigzEaze to collect your items");
                m.setBody("The code you will need to collect your order is "+pin);

                try {
//                    m.addAttachment("/sdcard/filelocation");


                    if (m.send()) {
                        //  Toast.makeText(MainActivity.this, "Email was sent successfully.", Toast.LENGTH_LONG).show();
                    } else {
                        // Toast.makeText(MainActivity.this, "Email was not sent.", Toast.LENGTH_LONG).show();
                    }
                } catch (Exception e) {
                    //Toast.makeText(MailApp.this, "There was a problem sending the email.", Toast.LENGTH_LONG).show();
                    Log.e("MailApp", "Could not send email", e);
                }
                return null;
            }
        }

}