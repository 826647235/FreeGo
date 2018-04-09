package com.example.smallning.freego;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import id.zelory.compressor.Compressor;
import me.nereo.multi_image_selector.MultiImageSelectorActivity;

import static android.app.Activity.RESULT_OK;

/**
 * Created by Smallning on 2017/9/6.
 */

public class OptionFragment extends Fragment implements View.OnClickListener {

    private CardView seek;
    private CardView photo;
    private CardView nearPeople;
    private CardView compress;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fragment_option,container,false);

        seek = view.findViewById(R.id.seek);
        photo = view.findViewById(R.id.photo);
        nearPeople = view.findViewById(R.id.near_people);
        compress = view.findViewById(R.id.compress);

        seek.setOnClickListener(this);
        photo.setOnClickListener(this);
        nearPeople.setOnClickListener(this);
        compress.setOnClickListener(this);
        return view;
    }

    @Override
    public void onClick(View view) {
        Intent intent;
        switch (view.getId()) {
            case R.id.seek:
                intent=new Intent(view.getContext(),function1.class);
                break;
            case R.id.photo:
                intent=new Intent(view.getContext(),MyPhoto.class);
                startActivity(intent);
                break;
            case R.id.near_people:
                intent=new Intent(view.getContext(),function3.class);
                break;
            case R.id.compress:
                ArrayList<String> a = new ArrayList<>();
                intent = new Intent(getActivity(), MultiImageSelectorActivity.class);
                intent.putExtra(MultiImageSelectorActivity.EXTRA_SHOW_CAMERA, true);
                intent.putExtra(MultiImageSelectorActivity.EXTRA_SELECT_COUNT, 9);
                intent.putExtra(MultiImageSelectorActivity.EXTRA_SELECT_MODE, MultiImageSelectorActivity.MODE_MULTI);
                intent.putStringArrayListExtra(MultiImageSelectorActivity.EXTRA_DEFAULT_SELECTED_LIST, a);
                getActivity().startActivityForResult(intent,2);
                break;
            default:
                break;
        }
    }
}
