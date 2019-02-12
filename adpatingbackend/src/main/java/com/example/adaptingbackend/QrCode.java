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

import com.example.adaptingbackend.R;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
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
import java.util.Set;

import static com.example.adaptingbackend.MainShop.m_cart;

public class QrCode extends AppCompatActivity {

    private ImageView imageView;
    String value = "Order Information\n";
    String orderID, name;

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
        SharedPrefManager.getUsername(this);
        name = SharedPrefManager.getUsername(this);
        new JSONBackgroundWorker().execute();


    }

    public void start() {

        Cart cart = m_cart;
        Set<Product> products = cart.getProducts();
        Iterator iterator = products.iterator();


        while (iterator.hasNext()) {
            Product product = (Product) iterator.next();
//            Toast.makeText(this,
//                    m_order.getSize(), Toast.LENGTH_LONG).show();


            // makes it only add the product once
            if (!value.contains(product.get_name())) {
                //make a call to backgroundWorker which will have to take
                //User_id,and the ids of the products
                BackgroundWorker backgroundWorker = new BackgroundWorker(this);
                String type = "order";
                // I need the product Ids and product quantity and orderID

                Toast toast = Toast.makeText(getApplicationContext(), "This is a message displayed in a Toast "+name, Toast.LENGTH_SHORT); toast.show();

                backgroundWorker.execute(type, product.getId(), String.valueOf(m_cart.getQuantity(product)),orderID);
                value = value + "\nProduct Name : " + product.get_name() + " \nAmount = " + m_cart.getQuantity(product) + "\n";
            }

        }


        m_cart.empty();


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

    public ArrayList<Product> parseJSON(String JSON_STRING) {


        try {

            JSONObject jsonObject = new JSONObject(JSON_STRING);
            JSONArray jsonArray = jsonObject.getJSONArray("server_response");
            int count = 0;
            String id;
            String food;
            boolean type = true;

            while (count < jsonArray.length()) {
                JSONObject JO = jsonArray.getJSONObject(count);

                id = JO.getString("id");
                orderID = id;


                Toast.makeText(getApplicationContext(), "This is a message displayed in a Toast " + orderID, Toast.LENGTH_SHORT).show();
                
                count++;

            }
            start();
        } catch (JSONException e) {
            e.printStackTrace();
        }
//        Toast.makeText(getApplicationContext(), "This is a message displayed in a Toast " + orderID, Toast.LENGTH_SHORT).show();
        return null;
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

    public class JSONBackgroundWorker extends AsyncTask<Void, Void, String> {
        String JSON_URL;
        String JSON_STRING;

        @Override
        protected void onPreExecute() {
            JSON_URL = "http://10.0.2.2/createOrder.php/?email="+name;

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
//            Toast.makeText(getApplicationContext(), "This is a message displayed in a Toast" + result, Toast.LENGTH_SHORT).show();
            parseJSON(result);

        }
    }


}
