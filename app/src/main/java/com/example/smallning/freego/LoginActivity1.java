package com.example.smallning.freego;

import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class LoginActivity1 extends AppCompatActivity implements View.OnClickListener {

    private Toolbar toolbar;
    private EditText account;
    private EditText password;
    private Button login;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login1);
        toolbar = (Toolbar)findViewById(R.id.toolbar);
        account = (EditText)findViewById(R.id.account);
        password = (EditText)findViewById(R.id.password);
        login = (Button)findViewById(R.id.login);

        login.setOnClickListener(this);





        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
    }

    @Override
    public void onClick(View view) {
        switch(view.getId()) {
            case R.id.login:
                Req
        }
    }
}

