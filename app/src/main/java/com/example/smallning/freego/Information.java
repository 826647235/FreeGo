package com.example.smallning.freego;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.amap.api.maps.model.Text;

import java.util.List;

import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class Information extends AppCompatActivity {

    private String getName;
    private ImageView portrait;
    private TextView name;
    private TextView motto;
    private TextView addFriend;
    private ImageView sexIcon;
    private ImageView photo1;
    private ImageView photo2;
    private ImageView photo3;
    private CardView cPhoto1;
    private CardView cPhoto2;
    private CardView cPhoto3;
    private int photoNum;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_information);
        getName = getIntent().getStringExtra("Name");
        portrait = (ImageView)findViewById(R.id.portrait);
        name = (TextView)findViewById(R.id.name);
        motto = (TextView)findViewById(R.id.motto);
        sexIcon = (ImageView)findViewById(R.id.sexIcon);
        photo1 = (ImageView)findViewById(R.id.photo1);
        photo2 = (ImageView)findViewById(R.id.photo1);
        photo3 = (ImageView)findViewById(R.id.photo1);
        cPhoto1 = (CardView)findViewById(R.id.cPhoto1);
        cPhoto2 = (CardView)findViewById(R.id.cPhoto2);
        cPhoto3 = (CardView)findViewById(R.id.cPhoto3);
        addFriend = (TextView)findViewById(R.id.addFriend);

        addFriend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            OkHttpClient okHttpClient = new OkHttpClient();
                            RequestBody requestBody = new FormBody.Builder()
                                    .add("Name", getName)
                                    .build();
                            Request request = new Request.Builder().post(requestBody).url("http://106.15.201.54:8080/Freego/addFriend").build();
                            Response response = okHttpClient.newCall(request).execute();
                            if (response.body().string().toString().equals("true")) {
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        Toast.makeText(Information.this, "关注成功", Toast.LENGTH_SHORT).show();
                                    }
                                });
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }).start();
            }
        });

        name.setText(getName);
        final Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    OkHttpClient okHttpClient = new OkHttpClient();
                    RequestBody requestBody = new FormBody.Builder()
                            .add("Name",getName)
                            .build();
                    Request request = new Request.Builder().post(requestBody).url("http://106.15.201.54:8080/Freego/getUser").build();
                    Response response = okHttpClient.newCall(request).execute();
                    String userMessage = response.body().string().toString();
                    String[] splitMessage = userMessage.split("##,##");
                    if(splitMessage[0].equals("男")) {
                        sexIcon.setImageResource(R.mipmap.man);
                    } else {
                        sexIcon.setImageResource(R.mipmap.woman);
                    }
                    motto.setText(splitMessage[1]);
                    photoNum = Integer.valueOf(splitMessage[1]);
                } catch ( Exception e) {
                    e.printStackTrace();
                }
            }
        });
        thread.start();
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    OkHttpClient okHttpClient = new OkHttpClient();
                    RequestBody requestBody = new FormBody.Builder()
                            .add("Name", getName)
                            .build();
                    Request request = new Request.Builder().post(requestBody).url("http://106.15.201.54:8080/Freego/getPortrait").build();
                    Response response = okHttpClient.newCall(request).execute();
                    byte[] picture = response.body().bytes();
                    final Bitmap bitmap = BitmapFactory.decodeByteArray(picture, 0, picture.length);
                    portrait.setImageBitmap(bitmap);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();
        try {
            thread.join();
        } catch (Exception e) {
            e.printStackTrace();
        }
        if(photoNum > 0 && photoNum <= 3) {
            switch (photoNum) {
                case 3:
                    getPhoto(3);
                case 2:
                    getPhoto(2);
                case 1:
                    getPhoto(1);
                    break;
                default:
                    break;
            }
        } else if(photoNum > 3) {
            getPhoto(1);
            getPhoto(2);
            getPhoto(3);
        }
    }

    private void getPhoto(final int number) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    OkHttpClient okHttpClient = new OkHttpClient();
                    RequestBody requestBody = new FormBody.Builder()
                            .add("Name", getName)
                            .build();
                    Request request = new Request.Builder().post(requestBody).url("http://106.15.201.54:8080/Freego/getPhoto").build();
                    Response response = okHttpClient.newCall(request).execute();
                    byte[] picture = response.body().bytes();
                    final Bitmap bitmap = BitmapFactory.decodeByteArray(picture, 0, picture.length);
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            switch (number) {
                                case 1:
                                    photo1.setImageBitmap(bitmap);
                                    cPhoto1.setVisibility(View.VISIBLE);
                                    break;
                                case 2:
                                    photo2.setImageBitmap(bitmap);
                                    cPhoto2.setVisibility(View.VISIBLE);
                                    break;
                                case 3:
                                    photo3.setImageBitmap(bitmap);
                                    cPhoto3.setVisibility(View.VISIBLE);
                                    break;
                                default:
                                    break;
                            }
                        }
                    });
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }
}
