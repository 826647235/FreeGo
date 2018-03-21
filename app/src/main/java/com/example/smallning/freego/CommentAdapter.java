package com.example.smallning.freego;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

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
public class CommentAdapter extends RecyclerView.Adapter<CommentAdapter.ViewHolder> {

    private List<Comment> commentList;
    private Activity activity;

    class ViewHolder extends RecyclerView.ViewHolder {
        CircleImageView portrait;
        TextView name;
        TextView date;
        TextView content;
        TextView likeNum;
        LinearLayout like;
        ImageView likeIcon;


        public ViewHolder(View view) {
            super(view);
            portrait = view.findViewById(R.id.portrait);
            name = view.findViewById(R.id.name);
            date = view.findViewById(R.id.date);
            content = view.findViewById(R.id.content);
            likeNum = view.findViewById(R.id.likenum);
            like = view.findViewById(R.id.like);
            likeIcon = view.findViewById(R.id.likeIcon);
        }
    }

    public CommentAdapter(List<Comment> list,Activity activity) {
        commentList = list;
        this.activity = activity;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.comment_list,parent,false);
        final ViewHolder viewHolder = new ViewHolder(view);

        viewHolder.portrait.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int position = viewHolder.getAdapterPosition();
                Comment comment = commentList.get(position);
                startInformation(comment.getName());
            }
        });
        viewHolder.name.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int position = viewHolder.getAdapterPosition();
                Comment comment = commentList.get(position);
                startInformation(comment.getName());
            }
        });
        viewHolder.like.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                       try {
                           int position = viewHolder.getAdapterPosition();
                           Comment comment = commentList.get(position);
                           OkHttpClient okHttpClient = new OkHttpClient();
                           RequestBody requestBody = new FormBody.Builder().add("Id",String.valueOf(comment.getId())).build();
                           Request request = new Request.Builder().post(requestBody).url("http://106.15.201.54:8080/Freego/likeComment").build();
                           Response response = okHttpClient.newCall(request).execute();
                           if(response.body().string().toString().equals("true")) {
                               viewHolder.likeIcon.setImageResource(R.mipmap.like_click);
                               viewHolder.likeNum.setText(Integer.valueOf(comment.getLikeNum()+1));
                               viewHolder.like.setClickable(false);
                               comment.setIsLike(true);
                           }
                       } catch (Exception e) {
                           e.printStackTrace();
                       }
                    }
                }).start();
            }
        });
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        final Comment singleComment = commentList.get(position);
        holder.name.setText(singleComment.getName());
        holder.date.setText(singleComment.getDate());
        holder.likeNum.setText(singleComment.getLikeNum());
        holder.content.setText(singleComment.getContent());
        if(!singleComment.getIsLike()) {
            holder.likeIcon.setImageResource(R.mipmap.like);
            holder.like.setClickable(true);
        } else {
            holder.likeIcon.setImageResource(R.mipmap.like_click);
            holder.like.setClickable(false);
        }
        if (singleComment.getPortrait() == null) {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        OkHttpClient okHttpClient = new OkHttpClient();
                        RequestBody body = new FormBody.Builder()
                                .add("name",singleComment.getName())
                                .build();
                        Request request = new Request.Builder().post(body).url("http://106.15.201.54:8080/Freego/getPortrait").build();
                        Response response = okHttpClient.newCall(request).execute();
                        byte[] picture = response.body().bytes();
                        Bitmap bitmap = BitmapFactory.decodeByteArray(picture, 0, picture.length);
                        singleComment.setPortrait(bitmap);
                        activity.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                holder.portrait.setImageBitmap(singleComment.getPortrait());
                            }
                        });

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }).start();
        } else {
            holder.portrait.setImageBitmap(singleComment.getPortrait());
        }
    }

    @Override
    public int getItemCount() {
        return commentList.size();
    }

    private void startInformation(String name) {
        Intent intent = new Intent(activity,Information.class);
        intent.putExtra("Name",name);
        activity.startActivity(intent);
    }
}
