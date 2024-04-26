package com.joshy.cart2go.backend;

import okhttp3.*;

import java.io.File;
import java.io.IOException;

public class ImageUploader {

    public void uploadImage(String name, String imagePath) {
        OkHttpClient client = new OkHttpClient().newBuilder()
                .build();
        MediaType mediaType = MediaType.parse("multipart/form-data");
        RequestBody body = new MultipartBody.Builder().setType(MultipartBody.FORM)
                .addFormDataPart("name", name)
                .addFormDataPart("image", "image",
                        RequestBody.create(MediaType.parse("application/octet-stream"),
                                new File(imagePath)))
                .build();
        Request request = new Request.Builder()
                .url("https://c2g.dev/upload")
                .method("POST", body)
                .addHeader("Content-Type", "multipart/form-data")
                .build();
        try {
            Response response = client.newCall(request).execute();
            System.out.println(response.body().string());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

