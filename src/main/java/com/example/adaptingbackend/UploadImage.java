package com.example.adaptingbackend;
import android.os.AsyncTask;

import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.regions.Region;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.PutObjectRequest;

import java.io.File;

public class UploadImage extends AsyncTask<String, String, String> {
    private final String accessKey = "AKIAJVL5I336SYABBB4A";
    private final String secretKey = "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";
    private final String bucketName = "gigzeaze";

    @Override
    protected String doInBackground(String... params) {
        String fileName = params[0];
        String filePath = params[1];
        AmazonS3Client s3Client = new AmazonS3Client(new BasicAWSCredentials(accessKey, secretKey));
        PutObjectRequest por = new PutObjectRequest(bucketName, fileName, new File(filePath));
        s3Client.putObject(por);
        return "";
    }

    protected void onPostExecute(String feed) {

        return;
    }
}