package com.example.smallning.freego;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.jwenfeng.library.pulltorefresh.BaseRefreshListener;
import com.jwenfeng.library.pulltorefresh.PullToRefreshLayout;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class MyCollection extends AppCompatActivity implements View.OnClickListener{

    private PullToRefreshLayout pullToRefreshLayout;
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
        toMyOwn = (Button)findViewById(R.id.myOwn);
        toMyCollection = (Button)findViewById(R.id.myCollection);
        pullToRefreshLayout = (PullToRefreshLayout)findViewById(R.id.refresh);
        recyclerView = (RecyclerView)findViewById(R.id.recyclerView);
        LinearLayoutManager layoutManager = new LinearLayoutManager(MyCollection.this);
        recyclerView.setLayoutManager(layoutManager);
        toMyOwn.setOnClickListener(this);
        toMyCollection.setOnClickListener(this);

        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    myOwnList.addAll(getMessage("myOwn",0));
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            adapter = new CommunityAdapter(myOwnList,MyCollection.this);
                            recyclerView.setAdapter(adapter);
                        }
                    });
                    myCollectionList.addAll(getMessage("myCollection",0));
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();

        pullToRefreshLayout.setRefreshListener(new BaseRefreshListener() {
            @Override
            public void refresh() {

            }

            @Override
            public void loadMore() {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(MyCollection.this, "loadMore", Toast.LENGTH_SHORT).show();
                        try {
                            pointList.addAll(getMessage(state,pointList.get(pointList.size()).getId()));
                            adapter.notifyDataSetChanged();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        pullToRefreshLayout.finishLoadMore();
                    }
                }, 2000);
            }
        });
    }

    @Override
    public void onClick(View view) {
        switch(view.getId()) {
            case R.id.myOwn:
                if(!state.equals("myOwn")) {
                    adapter = new CommunityAdapter(myOwnList,MyCollection.this);
                    recyclerView.setAdapter(adapter);
                }
                pointList = myOwnList;
                state = "myOwn";
                break;
            case R.id.myCollection:
                if(!state.equals("myCollection")) {
                    adapter = new CommunityAdapter(myCollectionList,MyCollection.this);
                    recyclerView.setAdapter(adapter);
                }
                pointList = myCollectionList;
                state = "myCollection";
                break;
            default:
                break;
        }
    }

    private List<CommunityShow> getMessage (String state,int position) throws Exception {
        OkHttpClient okHttpClient = new OkHttpClient();
        RequestBody requestBody = new FormBody.Builder()
                .add("State",state)
                .add("Position",String.valueOf(position))
                .add("Name",((GlobalVariable)getApplication()).getName())
                .build();
        Request request = new Request.Builder().post(requestBody).url("http://106.15.201.54:8080/Freego/getCollection").build();
        Response response = okHttpClient.newCall(request).execute();
        String communityMessage = response.body().string().toString();
        return SocietyFragment.handleMessage(communityMessage);
    }

//    public class CommunityAdapter extends RecyclerView.Adapter<CommunityAdapter.ViewHolder> {
//
//        private List<CommunityShow> communityList;
//
//        class ViewHolder extends RecyclerView.ViewHolder {
//            CircleImageView portrait;
//            TextView name;
//            TextView date;
//            TextView content;
//            TextView likeNum;
//            RecyclerView pictureView;
//
//            public ViewHolder(View view) {
//                super(view);
//                portrait = view.findViewById(R.id.portrait);
//                name = view.findViewById(R.id.name);
//                date = view.findViewById(R.id.date);
//                content = view.findViewById(R.id.content);
//                likeNum = view.findViewById(R.id.likenum);
//                pictureView = view.findViewById(R.id.pictureView);
//            }
//        }
//
//
//
//        public CommunityAdapter(List<CommunityShow> items) {
//            communityList = items;
//        }
//
//        @Override
//        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
//            View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.community_list,parent,false);
//            ViewHolder viewHolder = new ViewHolder(view);
//            return viewHolder;
//
//        }
//
//        @Override
//        public void onBindViewHolder(final ViewHolder holder, int position) {
//            final CommunityShow communityMessage = communityList.get(position);
//            if (communityMessage.getPortrait() == null) {
//                new Thread(new Runnable() {
//                    @Override
//                    public void run() {
//                        try {
//                            OkHttpClient okHttpClient = new OkHttpClient();
//                            RequestBody body = new FormBody.Builder()
//                                    .add("name",communityMessage.getName())
//                                    .build();
//                            Request request = new Request.Builder().post(body).url("http://106.15.201.54:8080/Freego/getPortrait").build();
//                            Response response = okHttpClient.newCall(request).execute();
//                            byte[] picture = response.body().bytes();
//                            Bitmap bitmap = BitmapFactory.decodeByteArray(picture, 0, picture.length);
//                            communityMessage.setPortrait(bitmap);
//                            runOnUiThread(new Runnable() {
//                                @Override
//                                public void run() {
//                                    holder.portrait.setImageBitmap(communityMessage.getPortrait());
//                                }
//                            });
//
//                        } catch (Exception e) {
//                            e.printStackTrace();
//                        }
//                    }
//                }).start();
//            } else {
//                holder.portrait.setImageBitmap(communityMessage.getPortrait());
//            }
//            holder.name.setText(communityMessage.getName());
//            holder.date.setText(communityMessage.getDate());
//            holder.content.setText(communityMessage.getContent());
//            holder.likeNum.setText(String.valueOf(communityMessage.getLikeNum()));
//            if (communityMessage.getPictureNum() != 0) {
//                if (communityMessage.getPictureList() == null) {
//                    communityMessage.setPictureList(new ArrayList<Bitmap>());
//                    new Thread(new Runnable() {
//                        @Override
//                        public void run() {
//                            try {
//                                OkHttpClient okHttpClient = new OkHttpClient();
//                                int Count = 1;
//                                while (true) {
//                                    RequestBody requestBody = new FormBody.Builder()
//                                            .add("Id", String.valueOf(communityMessage.getId()))
//                                            .add("Count", String.valueOf(Count))
//                                            .build();
//                                    Request request = new Request.Builder().post(requestBody).url("http://106.15.201.54:8080/Freego/getPicture").build();
//                                    Response response = okHttpClient.newCall(request).execute();
//                                    byte[] picture = response.body().bytes();
//                                    Bitmap bitmap = BitmapFactory.decodeByteArray(picture, 0, picture.length);
//                                    communityMessage.getPictureList().add(bitmap);
//                                    if (Count == communityMessage.getPictureNum()) {
//                                        break;
//                                    }
//                                    Count++;
//                                }
//                            } catch (Exception e) {
//                                e.printStackTrace();
//                            }
//                        }
//                    }).start();
//                }
//            } else {
//                if(communityMessage.getPictureList() == null) {
//                    communityMessage.setPictureList(new ArrayList<Bitmap>());
//                }
//            }
//            LinearLayoutManager layoutManager=new LinearLayoutManager(MyCollection.this);
//            layoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
//            holder.pictureView.setLayoutManager(layoutManager);
//            PictureAdapter adapter=new PictureAdapter(communityMessage.getPictureList());
//            holder.pictureView.setAdapter(adapter);
//        }
//        @Override
//        public int getItemCount() {
//            return communityList.size();
//        }
//    }
//
//    public class PictureAdapter extends RecyclerView.Adapter<PictureAdapter.ViewHolder> {
//
//        private List<Bitmap> pictureList;
//
//        class ViewHolder extends RecyclerView.ViewHolder {
//            ImageView picture;
//
//            public ViewHolder(View view) {
//                super(view);
//                picture = view.findViewById(R.id.picture);
//            }
//        }
//
//        public PictureAdapter(List<Bitmap> list) {
//            pictureList = list;
//        }
//
//        @Override
//        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
//            View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.picture_list,parent,false);
//            ViewHolder viewHolder = new ViewHolder(view);
//            return viewHolder;
//        }
//
//        @Override
//        public void onBindViewHolder(ViewHolder holder, int position) {
//            Bitmap singlePicture = pictureList.get(position);
//            holder.picture.setImageBitmap(singlePicture);
//        }
//
//        @Override
//        public int getItemCount() {
//            return pictureList.size();
//        }
//    }
}