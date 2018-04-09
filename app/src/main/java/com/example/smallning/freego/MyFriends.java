package com.example.smallning.freego;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class MyFriends extends AppCompatActivity {

    private RecyclerView friendsView;
    private List<User> friendsList = new ArrayList<>();
    private FriendAdapter adapter = new FriendAdapter(friendsList,MyFriends.this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_friends);
        friendsView = (RecyclerView)findViewById(R.id.friendsView);

        LinearLayoutManager layoutManager = new LinearLayoutManager(MyFriends.this);
        friendsView.setLayoutManager(layoutManager);
        friendsView.setAdapter(adapter);

        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    OkHttpClient okHttpClient = new OkHttpClient();
                    RequestBody requestBody = new FormBody.Builder()
                            .add("Name",((GlobalVariable)getApplication()).getName())
                            .build();
                    Request request = new Request.Builder().post(requestBody).url("http://106.15.201.54:8080/Freego/getFriends").build();
                    Response response = okHttpClient.newCall(request).execute();
                    String friendsMessage = response.body().string().toString();
                    friendsMessage = friendsMessage.substring(1, friendsMessage.length() - 6);
                    String[] messageList = friendsMessage.split("@@@, ");
                    for (String oneMessage : messageList) {
                        User friend = new User();
                        String[] splitMessage = oneMessage.split("##,##");
                        friend.setAccount(splitMessage[0]);
                        friend.setName(splitMessage[1]);
                        friend.setAge(Integer.parseInt(splitMessage[2]));
                        friend.setSex(splitMessage[3]);
                        friend.setMotto(splitMessage[4]);
                        friendsList.add(friend);
                    }
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            adapter.notifyDataSetChanged();
                        }
                    });
                } catch ( Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();

    }


}
