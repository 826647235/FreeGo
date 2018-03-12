package com.example.smallning.freego;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import me.nereo.multi_image_selector.MultiImageSelectorActivity;
import okhttp3.FormBody;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import top.zibin.luban.Luban;
import top.zibin.luban.OnCompressListener;

public class SendCommunityMessage extends AppCompatActivity {

    private FloatingActionButton addPicture;
    private EditText content;
    private RecyclerView pictureView;
    private List<Bitmap> pictureList = new ArrayList<>();
    private List<File> pictureFileList = new ArrayList<>();
    private int pictureNum = 0;
    private PictureAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_send_community_message);
        addPicture = (FloatingActionButton)findViewById(R.id.addPicture);
        content = (EditText)findViewById(R.id.content);
        pictureView = (RecyclerView)findViewById(R.id.pictureView);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("");
        setSupportActionBar(toolbar);

        LinearLayoutManager layoutManager=new LinearLayoutManager(SendCommunityMessage.this);
        layoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        pictureView.setLayoutManager(layoutManager);
        adapter=new PictureAdapter(pictureList);
        pictureView.setAdapter(adapter);

        addPicture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ArrayList<String> picturePathList = new ArrayList<>();
                Intent intent = new Intent(SendCommunityMessage.this, MultiImageSelectorActivity.class);
                intent.putExtra(MultiImageSelectorActivity.EXTRA_SHOW_CAMERA, true);
                intent.putExtra(MultiImageSelectorActivity.EXTRA_SELECT_COUNT, 20);
                intent.putExtra(MultiImageSelectorActivity.EXTRA_SELECT_MODE, MultiImageSelectorActivity.MODE_MULTI);
                intent.putStringArrayListExtra(MultiImageSelectorActivity.EXTRA_DEFAULT_SELECTED_LIST, picturePathList);
                startActivityForResult(intent, 1);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == 1){
            if(resultCode == RESULT_OK){
                List<String> path = data.getStringArrayListExtra(MultiImageSelectorActivity.EXTRA_RESULT);
                pictureNum += path.size();
                for(final String p : path) {
                    File file = new File(p);
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
                                    pictureList.add(BitmapFactory.decodeFile(file.getPath()));
                                    pictureView.setVisibility(View.VISIBLE);
                                    adapter.notifyDataSetChanged();
                                    pictureFileList.add(file);
                                }
                                @Override
                                public void onError(Throwable e) {
                                    // TODO 当压缩过程出现问题时调用
                                }
                            }).launch();
                }
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.toolbar_send, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.send:
                final String message = content.getText().toString();
                if((message.equals("") || message == null) && pictureNum == 0 ) {
                    Toast.makeText(SendCommunityMessage.this,"动态不能为空",Toast.LENGTH_SHORT);
                } else {
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                Date date = new Date(System.currentTimeMillis());
                                OkHttpClient okHttpClient = new OkHttpClient();
                                RequestBody contentBody = new FormBody.Builder()
                                        .add("Name",((GlobalVariable)getApplication()).getName())
                                        .add("Content",message)
                                        .add("Date",date.toString())
                                        .add("PictureNum",String.valueOf(pictureNum))
                                        .build();
                                Request contentRequest = new Request.Builder()
                                        .post(contentBody)
                                        .url("http://106.15.201.54:8080/Freego/community")
                                        .build();
                                Response response = okHttpClient.newCall(contentRequest).execute();
                                String Id = response.body().toString();
                                while(pictureNum != pictureFileList.size()) {
                                }
                                for (File picture : pictureFileList) {
                                    okHttpClient = new OkHttpClient();
                                    MultipartBody.Builder builder = new MultipartBody.Builder();
                                    builder.addFormDataPart("img",Id + '_' + picture.getName(),RequestBody.create(null,picture));
                                    RequestBody requestBody = builder.build();
                                    Request request = new Request.Builder()
                                            .post(requestBody)
                                            .url("http://106.15.201.54:8080/Freego/savePicture")
                                            .build();
                                   okHttpClient.newCall(request).execute();
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    }).start();
                }
                break;
            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }


//    public class PictureAdapter extends RecyclerView.Adapter<PictureAdapter.ViewHolder> {
//
//        private List<Bitmap> pictureList;
//
//        class ViewHolder extends RecyclerView.ViewHolder {
//            ImageView picture;
//
//            public ViewHolder(View view) {
//                super(view);
//                picture = view.findViewById(R.id.picture);
//            }
//        }
//
//        public PictureAdapter(List<Bitmap> list) {
//            pictureList = list;
//        }
//
//        @Override
//        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
//            View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.picture_list,parent,false);
//            ViewHolder viewHolder = new ViewHolder(view);
//            return viewHolder;
//        }
//
//        @Override
//        public void onBindViewHolder(ViewHolder holder, int position) {
//            Bitmap singlePicture = pictureList.get(position);
//            holder.picture.setImageBitmap(singlePicture);
//        }
//
//        @Override
//        public int getItemCount() {
//            return pictureList.size();
//        }
//    }
}
