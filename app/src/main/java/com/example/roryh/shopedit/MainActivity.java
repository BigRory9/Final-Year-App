package com.example.roryh.shopedit;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    Product products[];
    TextView m_response;
    static Cart m_cart;

    //
    LinearLayout mparent;

    LayoutInflater layoutInflater;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mparent = findViewById(R.id.parent);
        layoutInflater = (LayoutInflater)getSystemService(LAYOUT_INFLATER_SERVICE);



        for(int i =0; i<2; i++) {
            View myview = layoutInflater.inflate(R.layout.myrow, null, false);

            mparent.addView(myview);

        }
    }
}
