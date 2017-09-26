package com.example.smallning.freego;

import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {

    private Toolbar toolbar;
    private EditText account;
    private EditText password;
    private Button login;
    private TextView register;
    private static final int LOGIN_SUCCEED=1;
    private static final int LOGIN_FAILED=2;

    private Handler handler=new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case LOGIN_SUCCEED:
                    Toast.makeText(LoginActivity.this,"登陆成功",Toast.LENGTH_SHORT).show();
                    break;
                case LOGIN_FAILED:
                    Toast.makeText(LoginActivity.this,"登录失败",Toast.LENGTH_SHORT).show();
                    break;
                default:
                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login1);
        toolbar = (Toolbar)findViewById(R.id.toolbar);
        account = (EditText)findViewById(R.id.account);
        password = (EditText)findViewById(R.id.password);
        login = (Button)findViewById(R.id.login);
        register=(TextView)findViewById(R.id.register);

        login.setOnClickListener(this);
        register.setOnClickListener(this);




        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);



    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.login:
                Login();
                break;
            case R.id.register:
                Intent intent=new Intent(LoginActivity.this,RegisterActivity.class);
                startActivity(intent);
        }
    }

    private void Login() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try{
                    OkHttpClient client=new OkHttpClient();
                    RequestBody requestBody=new FormBody.Builder()
                            .add("Account",account.getText().toString())
                            .add("Password",password.getText().toString()).build();
                    Request request=new Request.Builder().url("http://10.0.2.2:8080/hhh").post(requestBody).build();
                    Response response=client.newCall(request).execute();
                    Message message=new Message();
                    if(response.body().string()=="true") {
                        message.what=LOGIN_SUCCEED;
                    } else {
                        message.what=LOGIN_FAILED;
                    }
                    handler.sendMessage(message);
                }catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }
}

