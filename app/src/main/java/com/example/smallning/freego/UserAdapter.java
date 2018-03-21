package com.example.smallning.freego;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.amap.api.maps.model.Circle;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * Created by Smallning on 2018/3/20.
 */

public class UserAdapter extends RecyclerView.Adapter<UserAdapter.ViewHolder> {

    private List<User> usersList;
    private List<Bitmap> portraitList = new ArrayList<>();
    Activity activity;

    class ViewHolder extends RecyclerView.ViewHolder {
        CircleImageView portrait;
        TextView name;
        TextView motto;

        public ViewHolder(View view) {
            super(view);
            portrait = view.findViewById(R.id.portrait);
            name = view.findViewById(R.id.name);
            motto = view.findViewById(R.id.motto);
        }
    }

    public UserAdapter(List<User> list,Activity activity) {
        usersList = list;
        this.activity = activity;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.user_list,parent,false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        final User singleUser = usersList.get(position);
        holder.name.setText(singleUser.getName());
        holder.motto.setText(singleUser.getMotto());
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    OkHttpClient okHttpClient = new OkHttpClient();
                    RequestBody requestBody = new FormBody.Builder()
                            .add("Account",singleUser.getAccount())
                            .build();
                    Request request = new Request.Builder().post(requestBody).url("http://106.15.201.54:8080/Freego/searchFriends").build();
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
        return usersList.size();
    }

    public void cleanBitmap() {
        for(Bitmap needClean : portraitList) {
            needClean.recycle();
        }
    }
}
