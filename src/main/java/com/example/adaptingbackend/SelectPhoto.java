package com.example.adaptingbackend;

import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.adaptingbackend.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

public class SelectPhoto extends AppCompatActivity {
    String picturePath,email,user_id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        email = SharedPrefManager.getEmail(this);
        new SelectPhoto.JSONgetUserId().execute();


    }
    public void startUp(){
        setContentView(R.layout.activity_select_photo);
    }


    public void getPhotoFromGallery(View view) {
        Intent pickPhoto = new Intent(Intent.ACTION_PICK,
                android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(pickPhoto, 0);//one can be replaced with any action code
    }

    public void confirmPhoto(View view){
        new UploadImage().execute("Image Number "+user_id, picturePath);
        Intent i = new Intent(this, MainShop.class);
        startActivity(i);
    }
    protected void onActivityResult(int requestCode, int resultCode, Intent imageReturnedIntent) {
        ImageView imageview = findViewById(R.id.imageView);
        TextView textView = findViewById(R.id.textView);
        TextView textView1 = findViewById(R.id.textView1);
        super.onActivityResult(requestCode, resultCode, imageReturnedIntent);
        switch (requestCode) {
            case 0:
                if (resultCode == RESULT_OK) {

                    Uri selectedImage = imageReturnedIntent.getData();
                    imageview.setImageURI(selectedImage);
//                    textView.setText("");
//                    textView1.setText("");
                    String[] filePathColumn = {MediaStore.Images.Media.DATA};
                    Cursor cursor = getContentResolver().query(selectedImage,
                            filePathColumn, null, null, null);
                    // Move to first row

                    cursor.moveToFirst();

                    int columnIndex = cursor.getColumnIndex(filePathColumn[0]);

                    picturePath = cursor.getString(columnIndex);
                    File f = new File(picturePath);
//                    String new_path = getPath(selectedImage);
//
//                    String imageName = f.getName();

                }
            case 1:
                break;

        }
    }

    public String getPath(Uri uri) {
        String[] projection = {MediaStore.Images.Media.DATA};
        Cursor cursor = managedQuery(uri, projection, null, null, null);
        startManagingCursor(cursor);
        int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        cursor.moveToFirst();
        return cursor.getString(column_index);
    }

    public ArrayList<Product> parseJSON(String JSON_STRING) {

            try {
                JSONObject jsonObject = new JSONObject(JSON_STRING);
                JSONArray jsonArray = jsonObject.getJSONArray("server_response");
                int count = 0;
                boolean type = true;
                while (count < jsonArray.length()) {
                    JSONObject JO = jsonArray.getJSONObject(count);
                    user_id = JO.getString("id");
                    Toast.makeText(this, "Your ID " + user_id,
                            Toast.LENGTH_SHORT).show();

                    //
                    count++;
                }
                     startUp();
                return null;
            } catch (JSONException e) {
                e.printStackTrace();
            }


        return null;
    }

    public class JSONgetUserId extends AsyncTask<String, Void, String> {
        String JSON_URL;
        String JSON_STRING;
        JSONObject jsonObject;
        JSONArray jsonArray;
//        ArrayList<Product> list = new ArrayList<Product>();

        @Override
        protected void onPreExecute() {
//            JSON_URL = "http://147.252.148.154/getUsersid.php?email=" + email;
            JSON_URL = "http://192.168.1.120/getUsersid.php?email=" + email;
        }

        @Override
        protected String doInBackground(String... params) {
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
            parseJSON(result);

        }
    }
}
