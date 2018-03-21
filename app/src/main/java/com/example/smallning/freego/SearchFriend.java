package com.example.smallning.freego;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;

import java.util.ArrayList;
import java.util.List;

import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class SearchFriend extends AppCompatActivity {

    private EditText searchMessage;
    private RecyclerView userView;
    private List<User> userList = new ArrayList<>();
    private UserAdapter adapter = new UserAdapter(userList,SearchFriend.this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_friend);
        searchMessage = (EditText)findViewById(R.id.searchMessage);
        userView = (RecyclerView)findViewById(R.id.usersView);

        LinearLayoutManager layoutManager = new LinearLayoutManager(SearchFriend.this);
        userView.setLayoutManager(layoutManager);
        userView.setAdapter(adapter);

        searchMessage.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(final Editable editable) {
                adapter.cleanBitmap();
                userList.clear();
                if (editable.length() != 0) {
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                OkHttpClient okHttpClient = new OkHttpClient();
                                RequestBody requestBody = new FormBody.Builder()
                                        .add("Message",editable.toString())
                                        .build();
                                Request request = new Request.Builder().post(requestBody).url("http://106.15.201.54:8080/Freego/searchFriends").build();
                                Response response = okHttpClient.newCall(request).execute();
                                String usersMessage = response.body().string().toString();
                                String[] messageList = usersMessage.split("@@@, ");
                                for (String oneMessage : messageList) {
                                    User user = new User();
                                    String[] splitMessage = oneMessage.split("##,##");
                                    user.setAccount(splitMessage[0]);
                                    user.setName(splitMessage[1]);
                                    user.setSex(splitMessage[2]);
                                    user.setMotto(splitMessage[3]);
                                    userList.add(user);
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
                } else {
                    adapter.cleanBitmap();
                    userList.clear();
                    adapter.notifyDataSetChanged();
                }
            }
        });
    }
}
