package com.example.smallning.freego;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * Created by Smallning on 2018/3/12.
 */

public class CommunityAdapter extends RecyclerView.Adapter<CommunityAdapter.ViewHolder> {

    private List<CommunityShow> communityList;
    private Activity activity;

    class ViewHolder extends RecyclerView.ViewHolder {
        View communityView;
        CircleImageView portrait;
        TextView name;
        TextView date;
        TextView content;
        TextView likeNum;
        RecyclerView pictureView;
        LinearLayout collection;
        LinearLayout comment;
        LinearLayout like;

        public ViewHolder(View view) {
            super(view);
            communityView = view;
            portrait = view.findViewById(R.id.portrait);
            name = view.findViewById(R.id.name);
            date = view.findViewById(R.id.date);
            content = view.findViewById(R.id.content);
            likeNum = view.findViewById(R.id.likenum);
            pictureView = view.findViewById(R.id.pictureView);
            collection = view.findViewById(R.id.collection);
            comment = view.findViewById(R.id.comment);
            like = view.findViewById(R.id.like);
        }
    }



    public CommunityAdapter(List<CommunityShow> items, Activity activity) {
        communityList = items;
        this.activity = activity;
    }

    @Override
    public CommunityAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.community_list,parent,false);
        final CommunityAdapter.ViewHolder viewHolder = new CommunityAdapter.ViewHolder(view);
        int position = viewHolder.getAdapterPosition();
        final CommunityShow communityShow = communityList.get(position);

        viewHolder.communityView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(activity,Writing.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable("CommunityShow",communityShow);
                intent.putExtras(bundle);
                activity.startActivity(intent);
            }
        });
        viewHolder.collection.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ClickMethod.collection(((GlobalVariable)activity.getApplication()).getName(),communityShow.getId());
            }
        });
        viewHolder.comment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
        viewHolder.like.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ClickMethod.like(communityShow.getId());
            }
        });
        return viewHolder;

    }

    @Override
    public void onBindViewHolder(final CommunityAdapter.ViewHolder holder, int position) {
        final CommunityShow communityMessage = communityList.get(position);
        if (communityMessage.getPortrait() == null) {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        OkHttpClient okHttpClient = new OkHttpClient();
                        RequestBody body = new FormBody.Builder()
                                .add("name",communityMessage.getName())
                                .build();
                        Request request = new Request.Builder().post(body).url("http://106.15.201.54:8080/Freego/getPortrait").build();
                        Response response = okHttpClient.newCall(request).execute();
                        byte[] picture = response.body().bytes();
                        Bitmap bitmap = BitmapFactory.decodeByteArray(picture, 0, picture.length);
                        communityMessage.setPortrait(bitmap);
                        activity.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                holder.portrait.setImageBitmap(communityMessage.getPortrait());
                            }
                        });

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }).start();
        } else {
            holder.portrait.setImageBitmap(communityMessage.getPortrait());
        }
        holder.name.setText(communityMessage.getName());
        holder.date.setText(communityMessage.getDate());
        holder.content.setText(communityMessage.getContent());
        holder.likeNum.setText(String.valueOf(communityMessage.getLikeNum()));
        if (communityMessage.getPictureNum() != 0) {
            if (communityMessage.getPictureList() == null) {
                communityMessage.setPictureList(new ArrayList<Bitmap>());
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            OkHttpClient okHttpClient = new OkHttpClient();
                            int Count = 1;
                            while (true) {
                                RequestBody requestBody = new FormBody.Builder()
                                        .add("Id", String.valueOf(communityMessage.getId()))
                                        .add("Count", String.valueOf(Count))
                                        .build();
                                Request request = new Request.Builder().post(requestBody).url("http://106.15.201.54:8080/Freego/getPicture").build();
                                Response response = okHttpClient.newCall(request).execute();
                                byte[] picture = response.body().bytes();
                                Bitmap bitmap = BitmapFactory.decodeByteArray(picture, 0, picture.length);
                                communityMessage.getPictureList().add(bitmap);
                                if (Count == communityMessage.getPictureNum()) {
                                    break;
                                }
                                Count++;
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }).start();
            }
        } else {
            if(communityMessage.getPictureList() == null) {
                communityMessage.setPictureList(new ArrayList<Bitmap>());
            }
        }
        LinearLayoutManager layoutManager = new LinearLayoutManager(activity);
        layoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        holder.pictureView.setLayoutManager(layoutManager);
        PictureAdapter adapter = new PictureAdapter(communityMessage.getPictureList());
        holder.pictureView.setAdapter(adapter);
    }
    @Override
    public int getItemCount() {
        return communityList.size();
    }
}
