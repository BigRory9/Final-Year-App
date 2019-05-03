package com.example.adaptingbackend;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.craftman.cardform.CardForm;
import com.craftman.cardform.OnPayBtnClickListner;
import com.example.adaptingbackend.Database.Database;
import com.stripe.android.Stripe;
import com.stripe.android.TokenCallback;
import com.stripe.android.model.Token;
import com.stripe.android.model.Card;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;


public class CardActivity extends AppCompatActivity {
    String name;
    int orderID;
    int userID;
    String email;
    String value = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_card_activity);
        email =SharedPrefManager.getEmail(this);
        JSONBackgroundWorker backgroundWorker = new JSONBackgroundWorker();
        backgroundWorker.execute();

    }

    public void start() {
        //email =SharedPrefManager.getEmail(this);
        Toast toast = Toast.makeText(getApplicationContext(),"Users Email = "+
                SharedPrefManager.getEmail(this), Toast.LENGTH_SHORT);
        toast.show();
        CardForm cardForm = (CardForm) findViewById(R.id.cardform);


        TextView txt = (TextView) findViewById(R.id.payment_amount);
        Button btnPay = (Button) findViewById(R.id.btn_pay);
        btnPay.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
        String price = new Database(this).getPrice();
        txt.setText("€ " + price);
        btnPay.setText(String.format("Payer %s", txt.getText()));
        SharedPrefManager.getEmail(this);
        name = SharedPrefManager.getEmail(this);
        final Intent intent = new Intent(this, MainShop.class);


        cardForm.setPayBtnClickListner(new OnPayBtnClickListner() {
            @Override
            public void onClick(com.craftman.cardform.Card card) {
                pay(card);
            }
        });
    }


    public void pay(com.craftman.cardform.Card card) {
        final Intent intent = new Intent(this, QrCode.class);
        Stripe stripe = new Stripe(getApplicationContext(), "pk_test_6ypfwwRrX7SPmxZNpY6Fl49M");
        Toast.makeText(CardActivity.this, String.format("Exp: %d/%d", card.getExpMonth(), card.getExpYear()), Toast.LENGTH_SHORT).show();

        Card card1 = new Card(
                card.getNumber(),
                card.getExpMonth(),
                card.getExpYear(),
                card.getCVC()
        );
        card1.validateCVC();
        card1.validateNumber();


        if (card1.validateCard() == false) {
            Toast.makeText(CardActivity.this, "PROBLEM", Toast.LENGTH_SHORT).show();
        } else {

            Toast.makeText(CardActivity.this, "IMO"+card1.getCVC(), Toast.LENGTH_SHORT).show();
            stripe.createToken(card1, new TokenCallback() {


                @Override
                public void onError(Exception error) {
                    //Error
                    Toast.makeText(getApplicationContext(), "ERROR", Toast.LENGTH_LONG).show();
                }

                @Override
                public void onSuccess(Token token) {
                    //send token to server
                    Toast.makeText(getApplicationContext(), "Succesfully created a token", Toast.LENGTH_LONG).show();       // < this toast works, so my token is fine

                    BackgroundWorker databaseTask = new BackgroundWorker(getApplicationContext());
                    //does this need to be what they purchsed
                    databaseTask.execute("SEND TOKEN", token.getId(), String.valueOf(orderID),"drinks/food",String.valueOf(userID));
                    SharedPrefManager.saveOrderID(String.valueOf(orderID), getApplicationContext());

                    startActivity(intent);

                }

            });

        }

    }

    public void parseJSON(String JSON_STRING) {
      //  Toast.makeText(getApplicationContext(), "Displaying Json " + JSON_STRING, Toast.LENGTH_SHORT).show();

        try {

            JSONObject jsonObject = new JSONObject(JSON_STRING);
            JSONArray jsonArray = jsonObject.getJSONArray("server_response");
            int count = 0;
            String order_id;
            String user_id;
            String food;
            boolean type = true;
           // Toast.makeText(getApplicationContext(), "Displaying Json " + jsonArray.length(), Toast.LENGTH_SHORT).show();
            while (count < jsonArray.length()) {
                JSONObject JO = jsonArray.getJSONObject(count);

                if(count ==0) {
                    order_id = JO.getString("order_id");
                    orderID = Integer.parseInt(order_id);
                }

                if(count ==1) {
                    user_id = JO.getString("user_id");
                    userID = Integer.parseInt(user_id);
                }

                Toast.makeText(getApplicationContext(), "HELLO", Toast.LENGTH_SHORT).show();

                count++;

            }
            start();
        } catch (JSONException e) {
            e.printStackTrace();
        }
//        Toast.makeText(getApplicationContext(), "This is a message displayed in a Toast " + orderID, Toast.LENGTH_SHORT).show();
    }

    public class JSONBackgroundWorker extends AsyncTask<Void, Void, String> {
        String JSON_URL;
        String JSON_STRING;

        @Override
        protected void onPreExecute() {
//            JSON_URL = "http://10.0.2.2/createOrder.php/?email="+name;

            JSON_URL = "http://10.0.2.2/createOrder.php?email="+email;
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
