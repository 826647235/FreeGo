package com.example.smallning.freego;

import android.graphics.Bitmap;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import java.util.List;

/**
 * Created by Smallning on 2018/4/8.
 */

public class PhotoAdapter extends RecyclerView.Adapter<PhotoAdapter.ViewHolder>{

    private List<Bitmap> photoList;
    private int width;

    class ViewHolder extends RecyclerView.ViewHolder {
        ImageView photo;
        public ViewHolder(View view) {
            super(view);
            photo = view.findViewById(R.id.picture);
        }
    }

    public PhotoAdapter(List<Bitmap> list,int width) {
        photoList = list;
        this.width = width;
    }
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.picture_list,parent,false);
        ViewHolder viewHolder = new ViewHolder(view);
        ViewGroup.LayoutParams layoutParams = viewHolder.photo.getLayoutParams();
        layoutParams.width = width/3-10;
        layoutParams.height = layoutParams.width;
        viewHolder.photo.setLayoutParams(layoutParams);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Bitmap singlePicture = photoList.get(position);
        holder.photo.setImageBitmap(singlePicture);
    }

    @Override
    public int getItemCount() {
        return photoList.size();
    }
}
