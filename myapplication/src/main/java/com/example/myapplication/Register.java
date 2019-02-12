package com.example.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class Register extends AppCompatActivity {
    EditText username, email,password;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        username = (EditText) findViewById(R.id.username);
        email = (EditText) findViewById(R.id.email);
        password = (EditText) findViewById(R.id.password);
    }

    public void onReg(View view){
        String str_username = username.getText().toString();
        String str_email = email.getText().toString();
        String str_password = password.getText().toString();
        String type="register";

        if(!str_password.trim().equals("") && !str_email.trim().equals("") && !str_password.trim().equals("")  ) {
            if(android.util.Patterns.EMAIL_ADDRESS.matcher(str_email).matches()) {
                BackgroundWorker backgroundWorker = new BackgroundWorker(this);
                backgroundWorker.execute(type, str_username, str_email, str_password);
            }
            else{
                Toast toast = Toast.makeText(getApplicationContext(), "Email Must be Valid ", Toast.LENGTH_SHORT); toast.show();
            }
        }
        else
        {
            Toast toast = Toast.makeText(getApplicationContext(), "No Entry Can Be Empty ", Toast.LENGTH_SHORT); toast.show();
        }

    }

    public void OpenLogin(View view){
        startActivity(new Intent(this,MainActivity.class));
    }
}
