package com.example.smallning.freego;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
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
 * Created by Smallning on 2018/4/2.
 */

public class FriendAdapter extends RecyclerView.Adapter<FriendAdapter.ViewHolder> {
    private List<User> friendsList;
    private List<Bitmap> portraitList = new ArrayList<>();
    Activity activity;

    class ViewHolder extends RecyclerView.ViewHolder {
        CircleImageView portrait;
        TextView name;
        TextView account;
        TextView age;
        ImageView sexIcon;
        TextView motto;
        LinearLayout chat;
        View whole;

        public ViewHolder(View view) {
            super(view);
            portrait = view.findViewById(R.id.portrait);
            name = view.findViewById(R.id.name);
            account = view.findViewById(R.id.account);
            age = view.findViewById(R.id.age);
            sexIcon = view.findViewById(R.id.sexIcon);
            motto = view.findViewById(R.id.motto);
            chat = view.findViewById(R.id.chat);
            whole = view;
        }
    }

    public FriendAdapter(List<User> list,Activity activity) {
        friendsList = list;
        this.activity = activity;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.friend_list,parent,false);
        final ViewHolder viewHolder = new ViewHolder(view);
        viewHolder.chat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int position = viewHolder.getAdapterPosition();
                User friend = friendsList.get(position);
                Intent intent = new Intent(activity,Chat.class);
                intent.putExtra("Name",friend.getName());
                activity.startActivity(intent);
            }
        });
        viewHolder.whole.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int position = viewHolder.getAdapterPosition();
                User friend = friendsList.get(position);
                Intent intent = new Intent(activity,Information.class);
                intent.putExtra("Name",friend.getName());
                activity.startActivity(intent);
            }
        });
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        final User singleFriend = friendsList.get(position);
        holder.name.setText(singleFriend.getName());
        holder.account.setText(singleFriend.getAccount());
        if(singleFriend.getSex().equals("男")) {
            Resources res = activity.getResources();
            Bitmap man= BitmapFactory.decodeResource(res, R.mipmap.man);
            holder.sexIcon.setImageBitmap(man);
        } else {
            Resources res = activity.getResources();
            Bitmap woman= BitmapFactory.decodeResource(res, R.mipmap.woman);
            holder.sexIcon.setImageBitmap(woman);
        }
        holder.age.setText(singleFriend.getAge() + "岁");
        holder.motto.setText(singleFriend.getMotto());
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    OkHttpClient okHttpClient = new OkHttpClient();
                    RequestBody requestBody = new FormBody.Builder()
                            .add("Name",singleFriend.getName())
                            .build();
                    Request request = new Request.Builder().post(requestBody).url("http://106.15.201.54:8080/Freego/getPortrait").build();
                    Response response = okHttpClient.newCall(request).execute();
                    final byte[] picture = response.body().bytes();
                    final Bitmap bitmap = BitmapFactory.decodeByteArray(picture, 0, picture.length);
                    portraitList.add(bitmap);
                    activity.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            holder.portrait.setImageBitmap(bitmap);
                        }
                    });
                } catch ( Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    @Override
    public int getItemCount() {
        return friendsList.size();
    }

    public void cleanBitmap() {
        for(Bitmap needClean : portraitList) {
            needClean.recycle();
        }
    }
}
