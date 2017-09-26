package com.example.smallning.freego;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
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

public class RegisterActivity extends AppCompatActivity implements View.OnClickListener{

    private EditText account;
    private EditText password;
    private EditText password2;
    private Button register;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        account=(EditText)findViewById(R.id.account);
        password=(EditText)findViewById(R.id.password);
        password2=(EditText)findViewById(R.id.password2);
        register=(Button)findViewById(R.id.register);

        register.setOnClickListener(this);


    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.register:
                if(TextUtils.isEmpty(account.getText())||TextUtils.isEmpty(password.getText())||TextUtils.isEmpty(password2.getText())) {
                    Toast.makeText(RegisterActivity.this,"请输入正确的用户名或密码",Toast.LENGTH_SHORT).show();
                } else if(password.getText().toString()!=password2.getText().toString()) {
                    Toast.makeText(RegisterActivity.this,"两次密码不匹配",Toast.LENGTH_SHORT).show();
                } else {
                    Register();
                }

                break;
        }
    }

    private void Register() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    OkHttpClient client=new OkHttpClient();
                    RequestBody requestBody=new FormBody.Builder()
                            .add("Account",account.getText().toString())
                            .add("Password",password.getText().toString())
                            .build();
                    Request request=new Request.Builder().url("http://10.0.2.2:8080/hhh").post(requestBody).build();
                    Response response=client.newCall(request).execute();
                    if(response.body().string()=="")
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }
}
