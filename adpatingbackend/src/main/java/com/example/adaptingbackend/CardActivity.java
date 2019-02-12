package com.example.adaptingbackend;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.craftman.cardform.CardForm;
import com.craftman.cardform.OnPayBtnClickListner;
import com.stripe.android.Stripe;
import com.stripe.android.TokenCallback;
import com.stripe.android.model.Token;
import com.stripe.android.model.Card;

import static com.example.adaptingbackend.MainShop.m_cart;

public class CardActivity extends AppCompatActivity {
    String name;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_card_activity);

        CardForm cardForm = (CardForm) findViewById(R.id.cardform);


        TextView txt = (TextView) findViewById(R.id.payment_amount);
        Button btnPay = (Button) findViewById(R.id.btn_pay);
        btnPay.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
        String price =String.format("%.2f", m_cart.getValue());
        txt.setText("$ "+price);
        btnPay.setText(String.format("Payer %s",txt.getText()));
        SharedPrefManager.getUsername(this);
        name = SharedPrefManager.getUsername(this);
        final Intent intent=new Intent(this,QrCode.class);



        cardForm.setPayBtnClickListner(new OnPayBtnClickListner() {
            @Override
            public void onClick(com.craftman.cardform.Card card) {
                    pay(card);
            }
        });
    }



    public void pay(com.craftman.cardform.Card card) {
        final Intent intent=new Intent(this,QrCode.class);
        Stripe stripe = new Stripe(getApplicationContext(), "pk_test_6ypfwwRrX7SPmxZNpY6Fl49M");
        Toast.makeText(CardActivity.this, String.format("Exp: %d/%d",card.getExpMonth(),card.getExpYear()),Toast.LENGTH_SHORT).show();

        Card card1 = new Card(
                card.getNumber(),
                card.getExpMonth(),
                card.getExpYear(),
                card.getCVC()
        );
        card1.validateCVC();
        card1.validateNumber();


        if(card.validateCard()==false) {

        }
        else {


            stripe.createToken(card1, new TokenCallback() {

                @Override
                public void onError(Exception error) {
                    //Error
                    Toast.makeText(getApplicationContext(), error.getLocalizedMessage(), Toast.LENGTH_LONG).show();
                }

                @Override
                public void onSuccess(Token token) {
                    //send token to server
                    Toast.makeText(getApplicationContext(), "Succesfully created a token", Toast.LENGTH_LONG).show();       // < this toast works, so my token is fine

                    BackgroundWorker databaseTask = new BackgroundWorker(getApplicationContext());
                    //username, order_id
                    databaseTask.execute("SEND TOKEN", token.getId());
                    startActivity(intent);

                }

            });

        }

    }
}
