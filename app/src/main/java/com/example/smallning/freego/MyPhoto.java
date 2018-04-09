package com.example.smallning.freego;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import id.zelory.compressor.Compressor;
import me.nereo.multi_image_selector.MultiImageSelectorActivity;
import okhttp3.FormBody;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class MyPhoto extends AppCompatActivity {

    private List<Bitmap> photoList = new ArrayList<>();
    private RecyclerView photoView;
    private PhotoAdapter adapter;
    private int photoNum;
    private TextView loadMore;
    private int upLoadNum;
    private final int SUCCEED = 0;
    private final int FALSE = 1;
    private final int NO_MORE = 2;
    private final int BLANK = 3;
    private int Count = 0;
    private int width;

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case SUCCEED:
                    Count++;
                    if(Count == upLoadNum) {
                        Toast.makeText(MyPhoto.this,"上传成功",Toast.LENGTH_SHORT).show();
                        Count = 0;
                        List<Bitmap> newPhotoList = new ArrayList<>();
                        adapter = new PhotoAdapter(newPhotoList,width);
                        adapter.notifyDataSetChanged();
                        for(Bitmap clear : photoList) {
                            clear.recycle();
                            Log.e("HHHHH","释放了一个");
                        }
                        photoList.clear();
                        getInitPhoto();
                    }
                    break;
                case FALSE:
                    Toast.makeText(MyPhoto.this,"网络故障",Toast.LENGTH_SHORT).show();
                    break;
                case NO_MORE:
                    Toast.makeText(MyPhoto.this,"没有更多照片了",Toast.LENGTH_SHORT).show();
                    break;
                case BLANK:
                    Toast.makeText(MyPhoto.this,"你的相册是空的\n赶快上传照片吧",Toast.LENGTH_SHORT).show();
                    break;
                default:
                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_photo);

        WindowManager wm = getWindowManager();
        width = wm.getDefaultDisplay().getWidth();

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("");
        MyPhoto.this.setSupportActionBar(toolbar);
        TextView textView = new TextView(MyPhoto.this);
        textView.setText("云相册");
        textView.setTextSize(20);
        textView.setTextColor(getResources().getColor(R.color.white));
        Toolbar.LayoutParams params = new Toolbar.LayoutParams(Toolbar.LayoutParams.WRAP_CONTENT, Toolbar.LayoutParams.WRAP_CONTENT);
        params.gravity = Gravity.CENTER_HORIZONTAL;
        toolbar.addView(textView, params);

        photoView = (RecyclerView)findViewById(R.id.photoView);
        loadMore = (TextView)findViewById(R.id.loadMore);

        getInitPhoto();
        StaggeredGridLayoutManager layoutManager = new StaggeredGridLayoutManager(3,StaggeredGridLayoutManager.VERTICAL);
        photoView.setLayoutManager(layoutManager);

        adapter = new PhotoAdapter(photoList,width);
        photoView.setAdapter(adapter);
        loadMore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        for(int j = 0; j < 6; j++)
                        {
                            if(photoNum == 0) {

                                break;
                            }
                            getPhoto();
                            photoNum--;
                        }
                }}).start();
            }
        });
    }

    private void getInitPhoto() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    OkHttpClient okHttpClient = new OkHttpClient();
                    RequestBody body = new FormBody.Builder()
                            .add("Name",((GlobalVariable)getApplication()).getName())
                            .build();
                    Request request = new Request.Builder().post(body).url("http://106.15.201.54:8080/Freego/getPhotoNum").build();
                    Response response = okHttpClient.newCall(request).execute();
                    String message = response.body().string().toString();
                    photoNum = Integer.parseInt(message);
                    if(photoNum == 0) {
                        Message msg = new Message();
                        msg.what = BLANK;
                        handler.sendMessage(msg);
                    } else {
                        for(int i = 0; i < 9; i++) {
                            if(photoNum == 0) {
                                break;
                            }
                            getPhoto();
                            photoNum--;
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();

    }

    private void getPhoto() {
        try {
            OkHttpClient okHttpClient = new OkHttpClient();
            RequestBody body = new FormBody.Builder()
                    .add("Name",((GlobalVariable)getApplication()).getName())
                    .add("Position",String.valueOf(photoNum))
                    .build();
            Request request = new Request.Builder().post(body).url("http://106.15.201.54:8080/Freego/getPhoto").build();
            Response response = okHttpClient.newCall(request).execute();
            byte[] picture = response.body().bytes();
            Bitmap bitmap = BitmapFactory.decodeByteArray(picture, 0, picture.length);
            photoList.add(bitmap);
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    adapter.notifyDataSetChanged();
                }
            });
        } catch (Exception e) {
            e.printStackTrace();}
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.toolbar_picture, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.add:
                ArrayList<String> a = new ArrayList<String>();
                Intent intent = new Intent(MyPhoto.this, MultiImageSelectorActivity.class);
                intent.putExtra(MultiImageSelectorActivity.EXTRA_SHOW_CAMERA, true);
                intent.putExtra(MultiImageSelectorActivity.EXTRA_SELECT_COUNT, 9);
                intent.putExtra(MultiImageSelectorActivity.EXTRA_SELECT_MODE, MultiImageSelectorActivity.MODE_MULTI);
                intent.putStringArrayListExtra(MultiImageSelectorActivity.EXTRA_DEFAULT_SELECTED_LIST, a);
                startActivityForResult(intent, 1);
        }
        return super.onOptionsItemSelected(item);
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 1) {
            if (resultCode == RESULT_OK) {
                List<String> path = data.getStringArrayListExtra(MultiImageSelectorActivity.EXTRA_RESULT);
                upLoadNum = path.size();
                for (String p : path) {
                    File file = new File(p);
                    try {
                        final File compressedImage = new Compressor(this)
                                .setMaxWidth(640)
                                .setMaxHeight(480)
                                .setQuality(75)
                                .setCompressFormat(Bitmap.CompressFormat.WEBP)
                                .setDestinationDirectoryPath(Environment.getExternalStoragePublicDirectory(
                                        Environment.DIRECTORY_PICTURES).getAbsolutePath())
                                .compressToFile(file);
                        new Thread(new Runnable() {
                            @Override
                            public void run() {
                                try {
                                    OkHttpClient okHttpClient = new OkHttpClient();
                                    MultipartBody.Builder builder = new MultipartBody.Builder();
                                    builder.addFormDataPart("img",((GlobalVariable)getApplication()).getName() + "_" + compressedImage.getName(), RequestBody.create(MediaType.parse("image/jpeg"),compressedImage));
                                    RequestBody requestBody = builder.build();
                                    Request request = new Request.Builder()
                                            .url("http://106.15.201.54:8080/Freego/savePhoto")
                                            .post(requestBody)
                                            .build();
                                    Response response = okHttpClient.newCall(request).execute();
                                    Message message = new Message();
                                    if(!response.body().string().toString().equals("true")) {
                                        message.what = FALSE;
                                    } else {
                                        message.what = SUCCEED;
                                    }
                                    handler.sendMessage(message);
                                } catch (Exception e) {
                                    Message message = new Message();
                                    message.what = FALSE;
                                    handler.sendMessage(message);
                                    e.printStackTrace();
                                }
                            }
                        }).start();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }
}
