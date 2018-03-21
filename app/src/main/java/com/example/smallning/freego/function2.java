package com.example.smallning.freego;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;

import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class function2 extends AppCompatActivity {

    ImageView picturev;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_function2);
        picturev = (ImageView)findViewById(R.id.picture) ;

        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    OkHttpClient okHttpClient = new OkHttpClient();
                    Request request = new Request.Builder().url("http://106.15.201.54:8080/Freego/getPicture").build();
                    Response response = okHttpClient.newCall(request).execute();
                    final byte[] picture = response.body().bytes();
                    final Bitmap bitmap = BitmapFactory.decodeByteArray(picture, 0, picture.length);
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            picturev.setImageBitmap(bitmap);
                        }
                    });
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }
}
