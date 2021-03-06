package com.example.smallning.freego;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
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

    private Button loginChange;
    private Button registerChange;
    private Button ok;
    private EditText account;
    private EditText password;
    private EditText surePassword;
    private View line;
    private final int LOGIN = 0;
    private final int REGISTER = 1;
    private int state = 0;
    private final int LOGIN_SUCCEED = 2;
    private final int LOGIN_FAILED = 3;
    private final int NETWORK_ERROR =4;
    private final int REGISTER_SUCCEED = 5;
    private final int REGISTER_FAILED = 6;
    private String accountText;
    private String passwordText;

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case LOGIN_SUCCEED:
                    Toast.makeText(LoginActivity.this,"登陆成功",Toast.LENGTH_SHORT).show();
                    setResult(RESULT_OK);
                    finish();
                    break;
                case LOGIN_FAILED:
                    Toast.makeText(LoginActivity.this,"账号或密码错误",Toast.LENGTH_SHORT).show();
                    break;
                case REGISTER_SUCCEED:
                    Intent intent = new Intent(LoginActivity.this,Register.class);
                    intent.putExtra("Account",accountText);
                    intent.putExtra("Password",passwordText);
                    startActivityForResult(intent,1);
                    break;
                case REGISTER_FAILED:
                    Toast.makeText(LoginActivity.this,"该账号已存在",Toast.LENGTH_SHORT).show();
                    break;
                case NETWORK_ERROR:
                    Toast.makeText(LoginActivity.this,"网络故障",Toast.LENGTH_SHORT).show();
                    break;
                default:
                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        loginChange = (Button) findViewById(R.id.loginChange);
        registerChange = (Button) findViewById(R.id.registerChange);
        ok = (Button) findViewById(R.id.ok);
        account = (EditText) findViewById(R.id.account);
        password = (EditText) findViewById(R.id.password);
        surePassword = (EditText) findViewById(R.id.surePassword);
        line = findViewById(R.id.line);

        SharedPreferences login = getSharedPreferences("Login",MODE_PRIVATE);
        String acc = login.getString("Account",null);
        if(acc != null) {
            account.setText(acc);
            password.setText(login.getString("Password",null));
        }
        loginChange.setOnClickListener(this);
        registerChange.setOnClickListener(this);
        ok.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.loginChange:
                if (state != LOGIN) {
                    surePassword.setVisibility(View.GONE);
                    line.setVisibility(View.GONE);
                    loginChange.setBackgroundColor(getResources().getColor((R.color.transparentBlack)));
                    registerChange.setBackgroundColor(getResources().getColor((R.color.transparentWhite)));
                    ok.setText("登录");
                    state = LOGIN;
                }
                break;
            case R.id.registerChange:
                if (state != REGISTER) {
                    surePassword.setVisibility(View.VISIBLE);
                    line.setVisibility(View.VISIBLE);
                    loginChange.setBackgroundColor(getResources().getColor((R.color.transparentWhite)));
                    registerChange.setBackgroundColor(getResources().getColor((R.color.transparentBlack)));
                    ok.setText("注册");
                    state = REGISTER;
                }
                break;
            case R.id.ok:
                if (state == LOGIN) {
                    accountText = account.getText().toString();
                    passwordText = password.getText().toString();
                    if (accountText.equals("") || passwordText.equals("")) {
                        Toast.makeText(LoginActivity.this, "账号和密码不能为空", Toast.LENGTH_SHORT).show();
                    } else {
                        Login();
                    }
                } else if (state == REGISTER) {
                    accountText = account.getText().toString();
                    passwordText = password.getText().toString();
                    String surePas = surePassword.getText().toString();
                    if (accountText.equals("") || passwordText.equals("") || surePas.equals("")) {
                        Toast.makeText(LoginActivity.this, "不能为空", Toast.LENGTH_SHORT).show();
                    } else if (!passwordText.equals(surePas)) {
                        Toast.makeText(LoginActivity.this, "两次输入密码不一致", Toast.LENGTH_SHORT).show();
                    } else {
                        Register();
                    }
                }
                break;
            default:
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode == 1) {
            if (resultCode == RESULT_OK) {
                setResult(RESULT_OK);
                finish();
            }
        }
    }

    private void Login() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    OkHttpClient client = new OkHttpClient();
                    RequestBody requestBody = new FormBody.Builder()
                            .add("Account", accountText)
                            .add("Password", passwordText)
                            .build();
                    Request request = new Request.Builder().url("http://106.15.201.54:8080/Freego/login").post(requestBody).build();
                    Response response = client.newCall(request).execute();
                    Message message = new Message();
                    String text = response.body().string().toString();
                    if (!text.equals("")) {
                        GlobalVariable globalVariable = (GlobalVariable)getApplication();
                        globalVariable.setAccount(accountText);
                        globalVariable.setName(text);
                        SharedPreferences sharedPreferences = getSharedPreferences("Login", MODE_PRIVATE);
                        SharedPreferences.Editor editor = sharedPreferences.edit();
                        editor.putString("Account", accountText);
                        editor.putString("Password", passwordText);
                        editor.commit();
                        message.what = LOGIN_SUCCEED;
                    } else {
                        message.what = LOGIN_FAILED;
                    }
                    handler.sendMessage(message);
                } catch (Exception e) {
                    e.printStackTrace();
                    Message message = new Message();
                    message.what = NETWORK_ERROR;
                    handler.sendMessage(message);
                }
            }
        }).start();
    }

    private void Register() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    OkHttpClient client = new OkHttpClient();
                    RequestBody requestBody = new FormBody.Builder()
                            .add("Account", accountText)
                            .add("Password", passwordText)
                            .build();
                    Request request = new Request.Builder().url("http://106.15.201.54:8080/Freego/checkRepeat").post(requestBody).build();
                    final Response response = client.newCall(request).execute();
                    Message message = new Message();
                    if (response.body().string().toString().equals("true")) {
                        message.what = REGISTER_SUCCEED;
                    } else {
                        message.what = REGISTER_FAILED;
                    }
                    handler.sendMessage(message);
                } catch (Exception e) {
                    e.printStackTrace();
                    Message message = new Message();
                    message.what = NETWORK_ERROR;
                    handler.sendMessage(message);
                }
            }
        }).start();
    }
}

