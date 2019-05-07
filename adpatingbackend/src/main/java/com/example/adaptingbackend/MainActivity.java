package com.example.adaptingbackend;


import android.content.Intent;
import android.os.Bundle;

import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.example.adaptingbackend.Database.Database;

public class MainActivity extends AppCompatActivity {

    EditText UsernameEt, PasswordEt;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        UsernameEt = (EditText) findViewById(R.id.username);
        PasswordEt = (EditText) findViewById(R.id.password);
        new Database(this).clearCart();
    }

    public void onLogin(View view) {
        String email = UsernameEt.getText().toString();
        String password = PasswordEt.getText().toString();
        String type = "login";

        SharedPrefManager.saveEmail(email, this);
        //save ID
        Toast toast = Toast.makeText(getApplicationContext(),
                SharedPrefManager.getEmail(this), Toast.LENGTH_SHORT);
        toast.show();
        BackgroundWorker backgroundWorker = new BackgroundWorker(this);
        backgroundWorker.execute(type, email, password);


    }



    public void OpenReg(View view) {
        startActivity(new Intent(this, Register.class));
    }


}
