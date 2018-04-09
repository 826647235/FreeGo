package com.example.smallning.freego;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;


import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class Writing extends AppCompatActivity implements View.OnClickListener{

    private CircleImageView portrait;
    private TextView name;
    private TextView date;
    private TextView content;
    private LinearLayout collection;
    private LinearLayout comment;
    private LinearLayout like;
    private TextView likeNum;
    private RecyclerView pictureView;
    private RecyclerView commentView;
    private TextView loadMore;
    private List<Comment> commentList = new ArrayList<>();
    private CommentAdapter commentAdapter;
    private RelativeLayout sendComment;
    private ImageView send;
    private EditText commentContent;
    private List<Bitmap> pictureList = new ArrayList<>();
    private int Id;
    private String itemName;
    private int pictureNum;
    private int likeNumber;
    private PictureAdapter pictureAdapter;
    private ImageView collectIcon;
    private ImageView likeIcon;
    private TextView commentNumText;
    private Bitmap needClear = null;
    private int commentNum;
    private TextView collectionText;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_writing);
        portrait = (CircleImageView)findViewById(R.id.portrait);
        name = (TextView)findViewById(R.id.name);
        date = (TextView)findViewById(R.id.date);
        content = (TextView)findViewById(R.id.content);
        collection = (LinearLayout)findViewById(R.id.collection);
        comment = (LinearLayout)findViewById(R.id.comment);
        like = (LinearLayout)findViewById(R.id.like);
        likeNum = (TextView)findViewById(R.id.likeNum);
        pictureView = (RecyclerView)findViewById(R.id.pictureView);
        commentView = (RecyclerView)findViewById(R.id.commentList);
        loadMore = (TextView)findViewById(R.id.loadMore);
        sendComment = (RelativeLayout)findViewById(R.id.sendComment);
        send = (ImageView)findViewById(R.id.send);
        commentContent = (EditText)findViewById(R.id.commentContent);
        collectIcon = (ImageView)findViewById(R.id.collectIcon);
        likeIcon = (ImageView)findViewById(R.id.likeIcon);
        commentNumText = (TextView)findViewById(R.id.commentNum);
        collectionText = (TextView)findViewById(R.id.collectionText);

        final Intent intent = getIntent();
        Id = intent.getIntExtra("Id",0);
        itemName = intent.getStringExtra("Name");
        pictureNum = intent.getIntExtra("PictureNum",0);
        likeNumber = intent.getIntExtra("LikeNum",0);

        if(intent.getBooleanExtra("IsLike",false)) {
            likeIcon.setImageResource(R.mipmap.like_click);
        }

        if(intent.getBooleanExtra("IsCollect",false)) {
            collectIcon.setImageResource(R.mipmap.collect_click);
            collectionText.setText("已收藏");
        }


        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    OkHttpClient okHttpClient = new OkHttpClient();
                    RequestBody body = new FormBody.Builder()
                            .add("Name",intent.getStringExtra("Name"))
                            .build();
                    Request request = new Request.Builder().post(body).url("http://106.15.201.54:8080/Freego/getPortrait").build();
                    Response response = okHttpClient.newCall(request).execute();
                    byte[] picture = response.body().bytes();
                    final Bitmap bitmap = BitmapFactory.decodeByteArray(picture, 0, picture.length);
                    needClear = bitmap;
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            portrait.setImageBitmap(bitmap);
                        }
                    });
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();

        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    OkHttpClient okHttpClient = new OkHttpClient();
                    RequestBody body = new FormBody.Builder()
                            .add("Id",String.valueOf(Id))
                            .build();
                    Request request = new Request.Builder().post(body).url("http://106.15.201.54:8080/Freego/getCommentNum").build();
                    Response response = okHttpClient.newCall(request).execute();
                    final String message = response.body().string().toString();
                    commentNum = Integer.parseInt(message);
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            commentNumText.setText(String.valueOf(commentNum));
                        }
                    });
                    if(commentNum > 0) {
                        getComment(Id,"new",0);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();
        name.setText(intent.getStringExtra("Name"));
        date.setText(intent.getStringExtra("Date"));
        content.setText(intent.getStringExtra("Content"));
        likeNum.setText(String.valueOf(likeNumber));

        if(pictureNum > 0) {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        OkHttpClient okHttpClient = new OkHttpClient();
                        int Count = 1;
                        while (true) {
                            RequestBody requestBody = new FormBody.Builder()
                                    .add("Id", String.valueOf(Id))
                                    .add("Count", String.valueOf(Count))
                                    .build();
                            Request request = new Request.Builder().post(requestBody).url("http://106.15.201.54:8080/Freego/getPicture").build();
                            Response response = okHttpClient.newCall(request).execute();
                            byte[] picture = response.body().bytes();
                            Bitmap bitmap = BitmapFactory.decodeByteArray(picture, 0, picture.length);
                            pictureList.add(bitmap);
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    pictureAdapter.notifyDataSetChanged();
                                }
                            });
                            if (Count == pictureNum) {
                                break;
                            }
                            Count++;
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }).start();


            LinearLayoutManager layoutManager1=new LinearLayoutManager(Writing.this);
            layoutManager1.setOrientation(LinearLayoutManager.HORIZONTAL);
            pictureView.setLayoutManager(layoutManager1);
            pictureAdapter=new PictureAdapter(pictureList);
            pictureView.setAdapter(pictureAdapter);
        }

        LinearLayoutManager layoutManager2=new LinearLayoutManager(Writing.this);
        layoutManager2.setOrientation(LinearLayoutManager.VERTICAL);
        commentView.setLayoutManager(layoutManager2);
        commentAdapter = new CommentAdapter(commentList,Writing.this);
        commentView.setAdapter(commentAdapter);
        portrait.setOnClickListener(this);
        name.setOnClickListener(this);
        date.setOnClickListener(this);
        content.setOnClickListener(this);
        collection.setOnClickListener(this);
        comment.setOnClickListener(this);
        like.setOnClickListener(this);
        likeNum.setOnClickListener(this);
        loadMore.setOnClickListener(this);
        send.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.portrait:
                startInformation(itemName);
                break;
            case R.id.name:
                startInformation(itemName);
                break;
            case R.id.collection:
                if(getIntent().getBooleanExtra("IsCollect",false)) {
                    Toast.makeText(Writing.this,"已经收藏了",Toast.LENGTH_SHORT).show();
                } else {
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                OkHttpClient okHttpClient = new OkHttpClient();
                                RequestBody requestBody = new FormBody.Builder()
                                        .add("Name", ((GlobalVariable)getApplication()).getName())
                                        .add("Id", String.valueOf(Id))
                                        .build();
                                Request request = new Request.Builder().post(requestBody).url("http://106.15.201.54:8080/Freego/collection").build();
                                Response response = okHttpClient.newCall(request).execute();
                                final String resBody = response.body().string().toString();
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        if (resBody.equals("true")) {
                                            Toast.makeText(Writing.this, "收藏成功", Toast.LENGTH_SHORT).show();
                                        } else {
                                            Toast.makeText(Writing.this, "网络故障", Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                });
                            } catch (Exception e) {
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        Toast.makeText(Writing.this, "网络故障", Toast.LENGTH_SHORT).show();
                                    }
                                });
                                e.printStackTrace();
                            }
                        }
                    }).start();
                }
                break;
            case R.id.comment:
                sendComment.clearAnimation();
                sendComment.setVisibility(View.VISIBLE);
                break;
            case R.id.like:
                if(getIntent().getBooleanExtra("IsLike",false)) {
                    Toast.makeText(Writing.this,"已经赞过了",Toast.LENGTH_SHORT).show();
                } else {
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                OkHttpClient okHttpClient = new OkHttpClient();
                                RequestBody requestBody = new FormBody.Builder()
                                        .add("Id",String.valueOf(Id))
                                        .build();
                                Request request = new Request.Builder().post(requestBody).url("http://106.15.201.54:8080/Freego/like").build();
                                Response response = okHttpClient.newCall(request).execute();
                                if(response.body().string().toString().equals("true")) {
                                    likeIcon.setImageResource(R.mipmap.like_click);
                                    likeNum.setText(Integer.valueOf(likeNumber+1));
                                    like.setClickable(false);
                                } else {
                                    runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            Toast.makeText(Writing.this, "网络故障", Toast.LENGTH_SHORT).show();
                                        }
                                    });
                                }
                            } catch (Exception e) {
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        Toast.makeText(Writing.this, "网络故障", Toast.LENGTH_SHORT).show();
                                    }
                                });
                                e.printStackTrace();
                            }
                        }
                    }).start();
                    likeNum.setText(String.valueOf(likeNumber+1));
                    like.setClickable(false);
                }
                break;
            case R.id.loadMore:
                if(commentList.size() == 0) {
                    getComment(Id,"new",0);
                } else {
                    getComment(Id,"old",commentList.get(commentList.size()-1).getId());
                }
                break;
            case R.id.send:
                if(commentContent.getText() == null ||commentContent.getText().equals("")) {
                    Toast.makeText(Writing.this,"请输入内容",Toast.LENGTH_SHORT).show();
                } else {
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            try{
                                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy年MM月dd日 HH:mm:ss");
                                Date date = new Date(System.currentTimeMillis());
                                String dateString = simpleDateFormat.format(date);
                                OkHttpClient okHttpClient = new OkHttpClient();
                                RequestBody requestBody = new FormBody.Builder()
                                        .add("Id",String.valueOf(Id))
                                        .add("Name",((GlobalVariable)getApplication()).getName())
                                        .add("Date",dateString)
                                        .add("Content",commentContent.getText().toString())
                                        .build();
                                Request request = new Request.Builder().post(requestBody).url("http://106.15.201.54:8080/Freego/saveComment").build();
                                okHttpClient.newCall(request).execute();
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    }).start();
                    sendComment.setVisibility(View.GONE);
                }
                break;
            default:
                break;
        }
    }

    private void getComment(final int Id, final String state, final int position) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    OkHttpClient okHttpClient = new OkHttpClient();
                    RequestBody requestBody = new FormBody.Builder()
                            .add("Id", String.valueOf(Id))
                            .add("State", state)
                            .add("Position", String.valueOf(position))
                            .build();
                    Request request = new Request.Builder().post(requestBody).url("http://106.15.201.54:8080/Freego/getComment").build();
                    Response response = okHttpClient.newCall(request).execute();
                    String commentMessage = response.body().string().toString();
                    commentList.addAll(handleCommentMessage(commentMessage));
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            commentAdapter.notifyDataSetChanged();
                        }
                    });
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    private List<Comment> handleCommentMessage(String commentMessage) {
        List<Comment> midList = new ArrayList<>();
        commentMessage = commentMessage.substring(1, commentMessage.length() - 6);
        String[] messageList = commentMessage.split("@@@, ");
        for (String oneMessage : messageList) {
            Comment comment = new Comment();
            String[] splitMessage = oneMessage.split("##,##");
            comment.setId(Integer.parseInt(splitMessage[0]));
            comment.setName(splitMessage[1]);
            comment.setDate(splitMessage[2]);
            comment.setContent(splitMessage[3]);
            comment.setLikeNum(Integer.parseInt(splitMessage[4]));
            midList.add(comment);
        }
        return midList;
    }

    private void startInformation(String name) {
        Intent intent = new Intent(Writing.this,Information.class);
        intent.putExtra("Name",name);
        startActivity(intent);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(needClear != null) {
            needClear.recycle();
        }
        for(Bitmap clear : pictureList) {
            clear.recycle();
        }
    }
}
