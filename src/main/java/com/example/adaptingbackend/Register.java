package com.example.adaptingbackend;

import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.adaptingbackend.R;

import java.io.File;

public class Register extends AppCompatActivity {
    EditText username, email, password;
    String picturePath;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        username = (EditText) findViewById(R.id.username);
        email = (EditText) findViewById(R.id.email);
        password = (EditText) findViewById(R.id.password);
    }


    public void getPhotoFromGallery(View view) {
        Intent pickPhoto = new Intent(Intent.ACTION_PICK,
                android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(pickPhoto, 0);//one can be replaced with any action code
    }


    protected void onActivityResult(int requestCode, int resultCode, Intent imageReturnedIntent) {
        ImageView imageview = findViewById(R.id.imageView);
        super.onActivityResult(requestCode, resultCode, imageReturnedIntent);
        switch (requestCode) {
            case 0:
                if (resultCode == RESULT_OK) {

                    Uri selectedImage = imageReturnedIntent.getData();
                    String[] filePathColumn = {MediaStore.Images.Media.DATA};
                    Cursor cursor = getContentResolver().query(selectedImage,
                            filePathColumn, null, null, null);
                    // Move to first row
                    cursor.moveToFirst();

                    int columnIndex = cursor.getColumnIndex(filePathColumn[0]);

                     picturePath = cursor.getString(columnIndex);
                    File f = new File(picturePath);
                    String new_path = getPath(selectedImage);

                    String imageName = f.getName();
//                    new UploadImage().execute("dsouihagihbiDSOBFDPIBAOFDUAIUVSNDAOU", picturePath);
//                        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
//                        StrictMode.setThreadPolicy(policy);
//                        Uri selectedImage = imageReturnedIntent.getData();
//
//                        String filePath = getPath(selectedImage);
//                        InputStream is = this.getContentResolver().openInputStream(selectedImage);
//                        imageview.setImageURI(selectedImage);
//                        String imageName = "pleaseWork";
//                        AmazonS3Client s3Client = new AmazonS3Client(new BasicAWSCredentials("AKIAJVL5I336SYABBB4A", "I7gmPoB7tY5bUky5GjLsDijZucjLG/8sngV/UZg6"));
//                        System.out.print(is.toString());
//
//                        s3Client.putObject(new PutObjectRequest("gigzeaze", imageName, is, new ObjectMetadata()));
////                    PutObjectRequest por = new PutObjectRequest("gigzeaze", imageName, new java.io.File(selectedImage.getPath()));
////                    s3Client.putObject(por);

                }
            case 1:
                break;

        }
    }


    public void onReg(View view) {
        String str_username = username.getText().toString();
        String str_email = email.getText().toString();
        String str_password = password.getText().toString();
        String type = "register";

        if (!str_password.trim().equals("") && !str_email.trim().equals("") && !str_password.trim().equals("")) {
            if (android.util.Patterns.EMAIL_ADDRESS.matcher(str_email).matches()) {
                BackgroundWorker backgroundWorker = new BackgroundWorker(this);
                try {
//                    String encrptPassword = AESCrypt.encrypt(str_password);
                    String pass = BCrypt.hashpw(str_password, BCrypt.gensalt());
                    backgroundWorker.execute(type, str_username, str_email, pass);
                } catch (Exception e) {
                    e.printStackTrace();
                }

            } else {
                Toast toast = Toast.makeText(getApplicationContext(), "Email Must be Valid ", Toast.LENGTH_SHORT);
                toast.show();
            }
        } else {
            Toast toast = Toast.makeText(getApplicationContext(), "No Entry Can Be Empty ", Toast.LENGTH_SHORT);
            toast.show();
        }
        new UploadImage().execute("dsouihagihbiDSOBFDPIBAOFDUAIUVSNDAOU", picturePath);

    }

    public String getPath(Uri uri) {
        String[] projection = {MediaStore.Images.Media.DATA};
        Cursor cursor = managedQuery(uri, projection, null, null, null);
        startManagingCursor(cursor);
        int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        cursor.moveToFirst();
        return cursor.getString(column_index);
    }

    public void OpenLogin(View view) {
        startActivity(new Intent(this, MainActivity.class));
    }
}
