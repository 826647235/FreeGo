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
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.amap.api.maps.model.Text;
import com.jwenfeng.library.pulltorefresh.BaseRefreshListener;
import com.jwenfeng.library.pulltorefresh.PullToRefreshLayout;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import okhttp3.FormBody;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okio.BufferedSink;

/**
 * Created by Smallning on 2017/9/6.
 */

public class SocietyFragment extends Fragment{

    private PullToRefreshLayout pullToRefreshLayout;
    private RecyclerView recyclerView;
    private FloatingActionButton sendMessage;

    List<CommunityShow> communityList = new ArrayList<>();
    CommunityAdapter adapter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_society, container, false);
        pullToRefreshLayout = view.findViewById(R.id.refresh);
        sendMessage = view.findViewById(R.id.sendMessage);
        sendMessage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(),SendCommunityMessage.class);
                startActivity(intent);
            }
        });

        //Toolbar设置
        Toolbar toolbar = view.findViewById(R.id.toolbar);
        toolbar.setTitle("");
        ((AppCompatActivity)getActivity()).setSupportActionBar(toolbar);
        setHasOptionsMenu(true);
        TextView textView = new TextView(getContext());
        textView.setText("社区");
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




        pullToRefreshLayout.setRefreshListener(new BaseRefreshListener() {
            @Override
            public void refresh() {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(getContext(), "run", Toast.LENGTH_SHORT).show();
                        try {
                            communityList.addAll(0,getMessage("new",communityList.get(0).getId()));
                            adapter.notifyDataSetChanged();
                        } catch (Exception e) {
                            e.getMessage();
                        }
                        pullToRefreshLayout.finishRefresh();
                    }
                }, 2000);

            }

            @Override
            public void loadMore() {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(getContext(), "loadMore", Toast.LENGTH_SHORT).show();
                        try {
                            communityList.addAll(getMessage("old",communityList.get(communityList.size()-1).getId()));
                            adapter.notifyDataSetChanged();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        pullToRefreshLayout.finishLoadMore();
                    }
                }, 2000);
            }
        });
        return view;
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
                Toast.makeText(getContext(),"addFriend",Toast.LENGTH_SHORT).show();
                intent = new Intent(getContext(),SearchFriend.class);
                startActivity(intent);
                break;
            case R.id.myCollection:
                Toast.makeText(getContext(),"myCollection",Toast.LENGTH_SHORT).show();
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
        RequestBody requestBody = new FormBody.Builder()
                .add("State",state)
                .add("Position",String.valueOf(position))
                .build();
        Request request = new Request.Builder().post(requestBody).url("http://106.15.201.54:8080/Freego/getCommunity").build();
        Response response = okHttpClient.newCall(request).execute();
        String communityMessage = response.body().string().toString();
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
            midList.add(communityItem);
        }
        Log.e("hhhhh",midList.toString());
        return midList;
    }

//    public class CommunityAdapter extends RecyclerView.Adapter<CommunityAdapter.ViewHolder> {
//
//        private List<CommunityShow> communityList;
//
//        class ViewHolder extends RecyclerView.ViewHolder {
//
//            View communityView;
//            CircleImageView portrait;
//            TextView name;
//            TextView date;
//            TextView content;
//            TextView likeNum;
//            RecyclerView pictureView;
//
//            public ViewHolder(View view) {
//                super(view);
//                communityView = view;
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
//            final ViewHolder viewHolder = new ViewHolder(view);
//            viewHolder.communityView.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View view) {
//                    int position = viewHolder.getAdapterPosition();
//                    CommunityShow communityShow = communityList.get(position);
//                    Intent intent = new Intent(getContext(),Writing.class);
//                    Bundle bundle = new Bundle();
//                    bundle.putSerializable("CommunityShow",communityShow);
//                    intent.putExtras(bundle);
//                    startActivity(intent);
//                }
//            });
//
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
//                            getActivity().runOnUiThread(new Runnable() {
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
//            LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
//            layoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
//            holder.pictureView.setLayoutManager(layoutManager);
//            PictureAdapter adapter = new PictureAdapter(communityMessage.getPictureList());
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