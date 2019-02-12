package com.example.myapplication;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.craftman.cardform.Card;
import com.craftman.cardform.CardForm;
import com.craftman.cardform.OnPayBtnClickListner;

import static com.example.myapplication.MainShop.m_cart;

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
            public void onClick(Card card) {
                Toast.makeText(CardActivity.this, String.format("Exp: %d/%d",card.getExpMonth(),card.getExpYear()),Toast.LENGTH_SHORT).show();
                startActivity(intent);
            }
        });

    }

//    public void checkout(View v){
//        Intent intent = new Intent(this, CardActivity.class);
//        startActivity(intent);
//    }
}
