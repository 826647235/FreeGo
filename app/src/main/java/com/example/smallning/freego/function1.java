package com.example.smallning.freego;

import android.content.Intent;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import id.zelory.compressor.Compressor;
import me.nereo.multi_image_selector.MultiImageSelectorActivity;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import top.zibin.luban.Luban;
import top.zibin.luban.OnCompressListener;

public class function1 extends AppCompatActivity {

    Button button;
    ImageView picture1;
    ImageView picture2;
    String path1;
    String path2;

    List<String> path = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_function1);
        button = (Button) findViewById(R.id.hahahaha);
        picture1 = (ImageView) findViewById(R.id.picture);
        picture2 = (ImageView) findViewById(R.id.picture2);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ArrayList<String> a = new ArrayList<String>();
                Intent intent = new Intent(function1.this, MultiImageSelectorActivity.class);
                intent.putExtra(MultiImageSelectorActivity.EXTRA_SHOW_CAMERA, true);
                intent.putExtra(MultiImageSelectorActivity.EXTRA_SELECT_COUNT, 9);
                intent.putExtra(MultiImageSelectorActivity.EXTRA_SELECT_MODE, MultiImageSelectorActivity.MODE_MULTI);
                intent.putStringArrayListExtra(MultiImageSelectorActivity.EXTRA_DEFAULT_SELECTED_LIST, a);
                startActivityForResult(intent, 1);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == 1){
            if(resultCode == RESULT_OK){
                path = data.getStringArrayListExtra(MultiImageSelectorActivity.EXTRA_RESULT);
                for(String p : path) {
                    File file = new File(p);
//                    try {
//                        final File compressedImage = new Compressor(this)
//                                .setMaxWidth(640)
//                                .setMaxHeight(480)
//                                .setQuality(75)
//                                .setCompressFormat(Bitmap.CompressFormat.WEBP)
//                                .setDestinationDirectoryPath(Environment.getExternalStoragePublicDirectory(
//                                        Environment.DIRECTORY_PICTURES).getAbsolutePath())
//                                .compressToFile(file);
//                        new Thread(new Runnable() {
//                            @Override
//                            public void run() {
//                                try {
//                                    OkHttpClient okHttpClient = new OkHttpClient();
//                                    MultipartBody.Builder builder = new MultipartBody.Builder();
//                                    builder.addFormDataPart("img",compressedImage.getName(),RequestBody.create(null,compressedImage));
//                                    RequestBody requestBody = builder.build();
//                                    Request request = new Request.Builder()
//                                            .url("http://106.15.201.54:8080/Freego/savePicture")
//                                            .post(requestBody)
//                                            .build();
//                                    Response response = okHttpClient.newCall(request).execute();
//                                    runOnUiThread(new Runnable() {
//                                        @Override
//                                        public void run() {
//                                            Toast.makeText(function1.this,"succeed",Toast.LENGTH_SHORT).show();
//                                        }
//                                    });
//                                } catch (Exception e) {
//                                    e.printStackTrace();
//                                }
//                            }
//                        }).start();
//                    } catch (Exception e) {
//                        e.printStackTrace();
//                    }

                    Luban.with(this)
                            .load(file)                                   // 传人要压缩的图片列表
                            .ignoreBy(100)                                  // 忽略不压缩图片的大小
                            .setCompressListener(new OnCompressListener() { //设置回调
                                @Override
                                public void onStart() {
                                    // TODO 压缩开始前调用，可以在方法内启动 loading UI
                                }

                                @Override
                                public void onSuccess(final File file) {
                                    new Thread(new Runnable() {
                                        @Override
                                        public void run() {
                                            try {
                                                OkHttpClient okHttpClient = new OkHttpClient();
                                                MultipartBody.Builder builder = new MultipartBody.Builder();
                                                builder.addFormDataPart("img",file.getName(),RequestBody.create(null,file));
                                                RequestBody requestBody = builder.build();
                                                Request request = new Request.Builder()
                                                        .url("http://106.15.201.54:8080/Freego/savePicture")
                                                        .post(requestBody)
                                                        .build();
                                                Response response = okHttpClient.newCall(request).execute();
                                            } catch (Exception e) {
                                                e.printStackTrace();
                                            }
                                        }
                                    }).start();
                                }

                                @Override
                                public void onError(Throwable e) {
                                    // TODO 当压缩过程出现问题时调用
                                }
                            }).launch();    //启动压缩
                }





//                new Thread(new Runnable() {
//                    @Override
//                    public void run() {
//                        try {
//                            OkHttpClient okHttpClient = new OkHttpClient();
//                            MultipartBody.Builder builder = new MultipartBody.Builder();
//                            for(String p : path) {
//                                File file = new File(p);
//
//                                builder.addFormDataPart("img",file.getName(),RequestBody.create(null,file));
//                            }
//
//                            RequestBody requestBody = builder.build();
//                            Request request = new Request.Builder()
//                                    .url("http://106.15.201.54:8080/Freego/savePicture")
//                                    .post(requestBody)
//                                    .build();
//                            Response response = okHttpClient.newCall(request).execute();
//
//                            byte[] picture = response.body().bytes();
//                            final Bitmap bitmap = BitmapFactory.decodeByteArray(picture, 0, picture.length);
//                            runOnUiThread(new Runnable() {
//                                @Override
//                                public void run() {
//                                    picture1.setImageBitmap(bitmap);
//                                }
//                            });
//                        } catch (Exception e) {
//                            e.printStackTrace();
//                        }
//                    }
//                }).start();

            }
        }
    }
}
