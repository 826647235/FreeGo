package com.example.smallning.freego;


import android.app.Activity;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import java.util.List;

/**
 * Created by Smallning on 2018/3/12.
 */

public class SceneryAdapter extends RecyclerView.Adapter<SceneryAdapter.ViewHolder> {

    private List<Scenery> sceneryList;
    private Activity activity;
    private int width;

    class ViewHolder extends RecyclerView.ViewHolder {
        View sceneryView;
        ImageView picture;
        TextView province;

        public ViewHolder(View view) {
            super(view);
            sceneryView = view;
            picture = view.findViewById(R.id.picture);
            province = view.findViewById(R.id.province);
        }
    }

    public SceneryAdapter(List<Scenery> list, Activity activity, int width) {
        sceneryList = list;
        this.activity = activity;
        this.width = width;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.scenery_list,parent,false);
        final ViewHolder viewHolder = new ViewHolder(view);
        ViewGroup.LayoutParams layoutParams = viewHolder.picture.getLayoutParams();
        layoutParams.width = width/2-10;
        layoutParams.height = (int)(layoutParams.width*0.7);
        viewHolder.picture.setLayoutParams(layoutParams);

        viewHolder.sceneryView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int position = viewHolder.getAdapterPosition();
                Scenery scenery = sceneryList.get(position);
                Intent intent = new Intent(activity,SceneryWebView.class);
                intent.putExtra("Url",scenery.getUrl());
                activity.startActivity(intent);
            }
        });
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Scenery scenery = sceneryList.get(position);
        holder.picture.setBackgroundResource(scenery.getPicture());
        holder.province.setText(scenery.getProvince());
    }

    @Override
    public int getItemCount() {
        return sceneryList.size();
    }
}
