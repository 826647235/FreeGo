package com.example.smallning.freego;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.SystemClock;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.jwenfeng.library.pulltorefresh.BaseRefreshListener;
import com.jwenfeng.library.pulltorefresh.PullToRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadMoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;


import java.util.ArrayList;
import java.util.List;

import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

import static android.app.Activity.RESULT_OK;

/**
 * Created by Smallning on 2017/9/6.
 */

public class SocietyFragment extends Fragment{

    private RefreshLayout refreshLayout;
    private RecyclerView recyclerView;
    private FloatingActionButton sendMessage;

    List<CommunityShow> communityList = new ArrayList<>();
    CommunityAdapter adapter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_society, container, false);
        refreshLayout = view.findViewById(R.id.refresh);
        sendMessage = view.findViewById(R.id.sendMessage);
        sendMessage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(),SendCommunityMessage.class);
                getActivity().startActivityForResult(intent,3);
            }
        });


        Toolbar toolbar = view.findViewById(R.id.toolbar);
        toolbar.setTitle("");
        ((AppCompatActivity)getActivity()).setSupportActionBar(toolbar);
        setHasOptionsMenu(true);
        TextView textView = new TextView(getContext());
        textView.setText("社区");
        textView.setTextSize(20);
        textView.setTextColor(getResources().getColor(R.color.white));
        Toolbar.LayoutParams params = new Toolbar.LayoutParams(Toolbar.LayoutParams.WRAP_CONTENT, Toolbar.LayoutParams.WRAP_CONTENT);
        params.gravity = Gravity.CENTER_HORIZONTAL;
        toolbar.addView(textView, params);



        recyclerView = view.findViewById(R.id.recyclerView);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);


        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    communityList.addAll(getMessage("new",0));
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            adapter = new CommunityAdapter(communityList,getActivity());
                            recyclerView.setAdapter(adapter);
                        }
                    });

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();


        refreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(@NonNull RefreshLayout refreshLayout) {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            communityList.addAll(0,getMessage("new",communityList.get(0).getId()));
                            getActivity().runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    adapter.notifyDataSetChanged();
                                }
                            });
                        } catch (Exception e) {
                            e.getMessage();
                        }
                    }
                }).start();
                refreshLayout.finishRefresh(2000);
            }
        });

        refreshLayout.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            communityList.addAll(getMessage("old",communityList.get(communityList.size()-1).getId()));
                            getActivity().runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    adapter.notifyDataSetChanged();
                                }
                            });
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }).start();
                refreshLayout.finishLoadMore(2000);
            }
        });
        return view;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 1) {
            if (resultCode == RESULT_OK) {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            communityList.addAll(0,getMessage("new",communityList.get(0).getId()));
                            adapter.notifyDataSetChanged();
                        } catch (Exception e) {
                            e.getMessage();
                        }
                    }
                }).start();
            }
        }
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.toolbar_social,menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Intent intent;
        switch (item.getItemId()) {
            case R.id.addFriend:
                intent = new Intent(getContext(),SearchFriend.class);
                startActivity(intent);
                break;
            case R.id.myCollection:
                intent = new Intent(getContext(),MyCollection.class);
                startActivity(intent);
                break;
            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private List<CommunityShow> getMessage (String state,int position) throws Exception {
        OkHttpClient okHttpClient = new OkHttpClient();
        Log.e("HHHHH",state + position);
        RequestBody requestBody = new FormBody.Builder()
                .add("State",state)
                .add("Position",String.valueOf(position))
                .add("Name",((GlobalVariable)getActivity().getApplication()).getName())
                .build();
        Request request = new Request.Builder().post(requestBody).url("http://106.15.201.54:8080/Freego/getCommunity").build();
        Response response = okHttpClient.newCall(request).execute();
        String communityMessage = response.body().string().toString();
        Log.e("HHHHH",communityMessage);
        return handleMessage(communityMessage);
    }

    public static List<CommunityShow> handleMessage (String communityMessage) {
        List<CommunityShow> midList = new ArrayList<>();
        communityMessage = communityMessage.substring(1, communityMessage.length() - 6);
        String[] messageList = communityMessage.split("@@@, ");
        for (String oneMessage : messageList) {
            CommunityShow communityItem = new CommunityShow();
            String[] splitMessage = oneMessage.split("##,##");
            communityItem.setId(Integer.parseInt(splitMessage[0]));
            communityItem.setName(splitMessage[1]);
            communityItem.setDate(splitMessage[2]);
            communityItem.setContent(splitMessage[3]);
            communityItem.setLikeNum(Integer.parseInt(splitMessage[4]));
            communityItem.setPictureNum(Integer.parseInt(splitMessage[5]));
            communityItem.setIsCollect(Boolean.parseBoolean(splitMessage[6]));
            communityItem.setIsLike(Boolean.parseBoolean(splitMessage[7]));
            Log.e("HHHHH",splitMessage[0]+splitMessage[6]+splitMessage[7]);
            midList.add(communityItem);
        }
        return midList;
    }
}