package com.example.smallning.freego;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

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
        ImageView collectionIcon;
        ImageView likeIcon;
        TextView collectionState;

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
            collectionIcon = view.findViewById(R.id.collectionIcon);
            likeIcon = view.findViewById(R.id.likeIcon);
            collectionState = view.findViewById(R.id.collectionState);
        }
    }



    public CommunityAdapter(List<CommunityShow> items, Activity activity) {
        communityList = items;
        this.activity = activity;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.community_list,parent,false);
        final ViewHolder viewHolder = new ViewHolder(view);

        viewHolder.communityView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int position = viewHolder.getAdapterPosition();
                CommunityShow communityShow = communityList.get(position);
                startNew("Message",communityShow);
            }
        });
        viewHolder.portrait.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int position = viewHolder.getAdapterPosition();
                Log.e("HHHHH",position + "");
                CommunityShow communityShow = communityList.get(position);
                startInformation(communityShow.getName());
            }
        });
        viewHolder.name.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int position = viewHolder.getAdapterPosition();
                CommunityShow communityShow = communityList.get(position);
                startInformation(communityShow.getName());
            }
        });

        viewHolder.collection.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int position = viewHolder.getAdapterPosition();
                final CommunityShow communityShow = communityList.get(position);
                if (communityShow.getIsCollect()) {
                    Toast.makeText(activity, "已经收藏了", Toast.LENGTH_SHORT).show();
                } else {
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                OkHttpClient okHttpClient = new OkHttpClient();
                                RequestBody requestBody = new FormBody.Builder()
                                        .add("Name", ((GlobalVariable) activity.getApplication()).getName())
                                        .add("Id", String.valueOf(communityShow.getId()))
                                        .build();
                                Request request = new Request.Builder().post(requestBody).url("http://106.15.201.54:8080/Freego/collection").build();
                                Response response = okHttpClient.newCall(request).execute();
                                final String resBody = response.body().string().toString();
                                activity.runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        if (resBody.equals("true")) {
                                            Toast.makeText(activity, "收藏成功", Toast.LENGTH_SHORT).show();
                                            viewHolder.collectionIcon.setImageResource(R.mipmap.collect_click);
                                            viewHolder.collectionState.setText("已收藏");
                                            communityShow.setIsCollect(true);
                                        } else {
                                            Toast.makeText(activity, "网络故障", Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                });
                            } catch (Exception e) {
                                activity.runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        Toast.makeText(activity, "网络故障", Toast.LENGTH_SHORT).show();
                                    }
                                });
                                e.printStackTrace();
                            }
                        }
                    }).start();
                }
            }
        });
        viewHolder.comment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int position = viewHolder.getAdapterPosition();
                CommunityShow communityShow = communityList.get(position);
                startNew("Comment",communityShow);
            }
        });

        viewHolder.like.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int position = viewHolder.getAdapterPosition();
                final CommunityShow communityShow = communityList.get(position);
                if(communityShow.getIsLike()) {
                    Toast.makeText(activity, "已经赞过了", Toast.LENGTH_SHORT).show();
                } else {
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                OkHttpClient okHttpClient = new OkHttpClient();
                                RequestBody requestBody = new FormBody.Builder()
                                        .add("Name", ((GlobalVariable) activity.getApplication()).getName())
                                        .add("Id",String.valueOf(communityShow.getId()))
                                        .build();
                                Request request = new Request.Builder().post(requestBody).url("http://106.15.201.54:8080/Freego/like").build();
                                Response response = okHttpClient.newCall(request).execute();
                                if(response.body().string().toString().equals("true")) {
                                    activity.runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            viewHolder.likeIcon.setImageResource(R.mipmap.like_click);
                                            communityShow.setLikeNum(communityShow.getLikeNum()+1);
                                            viewHolder.likeNum.setText(String.valueOf(communityShow.getLikeNum()));
                                            communityShow.setIsLike(true);
                                            Toast.makeText(activity, "点赞成功", Toast.LENGTH_SHORT).show();
                                        }
                                    });
                                } else {
                                    activity.runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            Toast.makeText(activity, "网络故障", Toast.LENGTH_SHORT).show();
                                        }
                                    });
                                }
                            } catch (Exception e) {
                                activity.runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        Toast.makeText(activity, "网络故障", Toast.LENGTH_SHORT).show();
                                    }
                                });
                                e.printStackTrace();
                            }
                        }
                    }).start();
                }
            }
        });
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final CommunityAdapter.ViewHolder holder, int position) {
        final CommunityShow communityMessage = communityList.get(position);
        if(communityMessage.getIsLike()) {
            holder.likeIcon.setImageResource(R.mipmap.like_click);
        } else {
            holder.likeIcon.setImageResource(R.mipmap.like);
        }
        if (communityMessage.getPortrait() == null) {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        OkHttpClient okHttpClient = new OkHttpClient();
                        RequestBody body = new FormBody.Builder()
                                .add("Name",communityMessage.getName())
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
        if(!communityMessage.getIsLike()) {
            holder.likeIcon.setImageResource(R.mipmap.like);
        } else {
            holder.likeIcon.setImageResource(R.mipmap.like_click);
        }

        if(!communityMessage.getIsLike()) {
            holder.collectionIcon.setImageResource(R.mipmap.collect);
            holder.collectionState.setText("收藏");
        } else {
            holder.collectionIcon.setImageResource(R.mipmap.collect_click);
            holder.collectionState.setText("已收藏");
        }

        LinearLayoutManager layoutManager = new LinearLayoutManager(activity);
        layoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        holder.pictureView.setLayoutManager(layoutManager);

        if (communityMessage.getPictureNum() != 0) {
            if (communityMessage.getPictureList() == null) {
                communityMessage.setPictureList(new ArrayList<Bitmap>());
                final PictureAdapter adapter = new PictureAdapter(communityMessage.getPictureList());
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            OkHttpClient okHttpClient = new OkHttpClient();
                            int Count = 1;
                            while (true) {
                                Log.e("HHHHHHH","Count is :  "+ Count);
                                RequestBody requestBody = new FormBody.Builder()
                                        .add("Id", String.valueOf(communityMessage.getId()))
                                        .add("Count", String.valueOf(Count))
                                        .build();
                                Request request = new Request.Builder().post(requestBody).url("http://106.15.201.54:8080/Freego/getPicture").build();
                                Response response = okHttpClient.newCall(request).execute();
                                byte[] picture = response.body().bytes();
                                Bitmap bitmap = BitmapFactory.decodeByteArray(picture, 0, picture.length);
                                communityMessage.getPictureList().add(bitmap);
                                activity.runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        adapter.notifyDataSetChanged();
                                    }
                                });
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
                holder.pictureView.setAdapter(adapter);
            } else {
                PictureAdapter adapter = new PictureAdapter(communityMessage.getPictureList());
                holder.pictureView.setAdapter(adapter);
            }
        } else {
            if(communityMessage.getPictureList() == null) {
                communityMessage.setPictureList(new ArrayList<Bitmap>());
                PictureAdapter adapter = new PictureAdapter(communityMessage.getPictureList());
                holder.pictureView.setAdapter(adapter);
            } else {
                PictureAdapter adapter = new PictureAdapter(communityMessage.getPictureList());
                holder.pictureView.setAdapter(adapter);
            }
        }
    }
    @Override
    public int getItemCount() {
        return communityList.size();
    }

    private void startNew(String state,CommunityShow communityShow) {
        Intent intent = new Intent(activity,Writing.class);
        intent.putExtra("Id",communityShow.getId());
        intent.putExtra("Name",communityShow.getName());
        intent.putExtra("Date",communityShow.getDate());
        intent.putExtra("Content",communityShow.getContent());
        intent.putExtra("LikeNum",communityShow.getLikeNum());
        intent.putExtra("PictureNum",communityShow.getPictureNum());
        intent.putExtra("IsLike",communityShow.getIsLike());
        intent.putExtra("IsCollect",communityShow.getIsCollect());
        intent.putExtra("State",state);
        activity.startActivity(intent);
    }

    private void startInformation(String name) {
        Intent intent = new Intent(activity,Information.class);
        intent.putExtra("Name",name);
        activity.startActivity(intent);
    }
}
