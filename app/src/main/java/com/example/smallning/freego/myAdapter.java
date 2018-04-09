package com.example.smallning.freego;

import android.app.Activity;
import android.app.Fragment;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import me.nereo.multi_image_selector.MultiImageSelectorActivity;

import static android.app.Activity.RESULT_FIRST_USER;

/**
 * Created by Smallning on 2017/9/6.
 */

public class myAdapter extends RecyclerView.Adapter<myAdapter.ViewHolder> {

    private List<Item> itemList;
    OptionFragment fragment;

    class ViewHolder extends RecyclerView.ViewHolder {
        View view;
        ImageView imageView;
        TextView textView;

        public ViewHolder(View view) {
            super(view);
            this.view=view;
            imageView=view.findViewById(R.id.imageView);
            textView=view.findViewById(R.id.textView);
        }
    }

    public myAdapter(List<Item> items,OptionFragment fragment) {
        itemList=items;
        this.fragment = fragment;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.recyclerview_layout,parent,false);
        ViewHolder viewHolder=new ViewHolder(view);
        return viewHolder;

    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        final Item item=itemList.get(position);
        holder.imageView.setImageResource(item.getImageId());
        holder.textView.setText(item.getName());
        holder.view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=null;
                switch (item.getName()) {
                    case "寻觅":
                        intent=new Intent(view.getContext(),function1.class);
                        break;
                    case "云相册":
                        intent=new Intent(view.getContext(),MyPhoto.class);
                        break;
                    case "附近的人":
                        intent=new Intent(view.getContext(),function3.class);
                        break;
                    case "照片压缩":
                        ArrayList<String> a = new ArrayList<String>();
                        intent = new Intent(fragment.getActivity(), MultiImageSelectorActivity.class);
                        intent.putExtra(MultiImageSelectorActivity.EXTRA_SHOW_CAMERA, true);
                        intent.putExtra(MultiImageSelectorActivity.EXTRA_SELECT_COUNT, 9);
                        intent.putExtra(MultiImageSelectorActivity.EXTRA_SELECT_MODE, MultiImageSelectorActivity.MODE_MULTI);
                        intent.putStringArrayListExtra(MultiImageSelectorActivity.EXTRA_DEFAULT_SELECTED_LIST, a);
                        fragment.startActivityForResult(intent,RESULT_FIRST_USER);
                        break;
                    default:
                        break;
                }
                view.getContext().startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return itemList.size();
    }
}
