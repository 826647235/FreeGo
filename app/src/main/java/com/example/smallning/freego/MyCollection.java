package com.example.smallning.freego;

import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.jwenfeng.library.pulltorefresh.BaseRefreshListener;
import com.jwenfeng.library.pulltorefresh.PullToRefreshLayout;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadMoreListener;

import java.util.ArrayList;
import java.util.List;

import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class MyCollection extends AppCompatActivity implements View.OnClickListener {

    private SmartRefreshLayout refreshLayout;
    private RecyclerView recyclerView;
    private List<CommunityShow> myOwnList = new ArrayList<>();
    private List<CommunityShow> myCollectionList = new ArrayList<>();
    private List<CommunityShow> pointList = myOwnList;
    private CommunityAdapter adapter;
    private Button toMyOwn;
    private Button toMyCollection;
    private String state = "myOwn";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_collection);
        toMyOwn = (Button) findViewById(R.id.myOwn);
        toMyCollection = (Button) findViewById(R.id.myCollection);
        refreshLayout = (SmartRefreshLayout) findViewById(R.id.refresh);
        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        LinearLayoutManager layoutManager = new LinearLayoutManager(MyCollection.this);
        recyclerView.setLayoutManager(layoutManager);
        toMyOwn.setOnClickListener(this);
        toMyCollection.setOnClickListener(this);

        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    myOwnList.addAll(getMessage("myOwn", 0));
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            adapter = new CommunityAdapter(pointList, MyCollection.this);
                            recyclerView.setAdapter(adapter);
                        }
                    });
                    myCollectionList.addAll(getMessage("myCollection", 0));
                    runOnUiThread(new Runnable() {
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

        refreshLayout.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            pointList.addAll(getMessage(state, pointList.get(pointList.size()).getId()));
                            runOnUiThread(new Runnable() {
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
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.myOwn:
                if (!state.equals("myOwn")) {
                    state = "myOwn";
                    pointList = myOwnList;
                    Log.e("HHHHH",state);
                    Log.e("HHHHH",pointList.size()+"");
                    recyclerView.removeAllViews();
                    adapter = new CommunityAdapter(pointList, MyCollection.this);
                    recyclerView.setAdapter(adapter);
                    toMyOwn.setBackgroundColor(getResources().getColor((R.color.transparentBlack)));
                    toMyCollection.setBackgroundColor(getResources().getColor((R.color.transparentWhite)));
                }
                break;
            case R.id.myCollection:
                if (!state.equals("myCollection")) {
                    state = "myCollection";
                    pointList = myCollectionList;
                    Log.e("HHHHH",state);
                    Log.e("HHHHH",pointList.size()+"");
                    recyclerView.removeAllViews();
                    adapter = new CommunityAdapter(pointList, MyCollection.this);
                    recyclerView.setAdapter(adapter);
                    adapter.notifyDataSetChanged();
                    toMyOwn.setBackgroundColor(getResources().getColor((R.color.transparentWhite)));
                    toMyCollection.setBackgroundColor(getResources().getColor((R.color.transparentBlack)));
                }

                break;
            default:
                break;
        }
    }

    private List<CommunityShow> getMessage(String state, int position) throws Exception {
        OkHttpClient okHttpClient = new OkHttpClient();
        RequestBody requestBody = new FormBody.Builder()
                .add("State", state)
                .add("Position", String.valueOf(position))
                .add("Name", ((GlobalVariable) getApplication()).getName())
                .build();
        Request request = new Request.Builder().post(requestBody).url("http://106.15.201.54:8080/Freego/getCollection").build();
        Response response = okHttpClient.newCall(request).execute();
        String communityMessage = response.body().string().toString();
        return SocietyFragment.handleMessage(communityMessage);
    }
}