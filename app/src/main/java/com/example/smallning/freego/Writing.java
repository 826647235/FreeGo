package com.example.smallning.freego;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.amap.api.maps.model.Text;
import com.amap.api.services.cloud.CloudImage;

import de.hdodenhof.circleimageview.CircleImageView;

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
    private CommunityShow communityShow;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_writing);
        communityShow = (CommunityShow)getIntent().getSerializableExtra("CommunityShow");
        portrait = (CircleImageView)findViewById(R.id.portrait);
        name = (TextView)findViewById(R.id.name);
        date = (TextView)findViewById(R.id.date);
        content = (TextView)findViewById(R.id.content);
        collection = (LinearLayout)findViewById(R.id.collection);
        comment = (LinearLayout)findViewById(R.id.comment);
        like = (LinearLayout)findViewById(R.id.like);
        likeNum = (TextView)findViewById(R.id.likenum);
        pictureView = (RecyclerView)findViewById(R.id.pictureView);

        portrait.setImageBitmap(communityShow.getPortrait());
        name.setText(communityShow.getName());
        date.setText(communityShow.getDate());
        content.setText(communityShow.getContent());
        likeNum.setText(communityShow.getLikeNum());

        LinearLayoutManager layoutManager=new LinearLayoutManager(Writing.this);
        layoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        pictureView.setLayoutManager(layoutManager);
        PictureAdapter adapter=new PictureAdapter(communityShow.getPictureList());
        pictureView.setAdapter(adapter);

        portrait.setOnClickListener(this);
        name.setOnClickListener(this);
        date.setOnClickListener(this);
        content.setOnClickListener(this);
        collection.setOnClickListener(this);
        comment.setOnClickListener(this);
        like.setOnClickListener(this);
        likeNum.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.portrait:
                break;
            case R.id.name:
                break;
            case R.id.collection:
                ClickMethod.collection(((GlobalVariable)getApplication()).getName(),communityShow.getId());
                break;
            case R.id.comment:
                break;
            case R.id.like:
                ClickMethod.like(communityShow.getId());
                break;
            default:
                break;
        }
    }
}
