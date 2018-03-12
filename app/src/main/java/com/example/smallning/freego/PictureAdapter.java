package com.example.smallning.freego;

import android.graphics.Bitmap;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import java.util.List;

/**
 * Created by Smallning on 2018/3/12.
 */

public class PictureAdapter extends RecyclerView.Adapter<PictureAdapter.ViewHolder> {

    private List<Bitmap> pictureList;

    class ViewHolder extends RecyclerView.ViewHolder {
        ImageView picture;

        public ViewHolder(View view) {
            super(view);
            picture = view.findViewById(R.id.picture);
        }
    }

    public PictureAdapter(List<Bitmap> list) {
        pictureList = list;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.picture_list,parent,false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Bitmap singlePicture = pictureList.get(position);
        holder.picture.setImageBitmap(singlePicture);
    }

    @Override
    public int getItemCount() {
        return pictureList.size();
    }
}