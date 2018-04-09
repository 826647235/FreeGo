package com.example.smallning.freego;

import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class Register extends AppCompatActivity implements View.OnClickListener{

    private final int NETWORK_ERROR = 0;
    private final int MESSAGE_REPEAT = 1;
    private final int REGISTER_SUCCEED = 2;

    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case NETWORK_ERROR:
                    Toast.makeText(Register.this,"网络故障",Toast.LENGTH_SHORT).show();
                    break;
                case MESSAGE_REPEAT:
                    Toast.makeText(Register.this,"昵称重复",Toast.LENGTH_SHORT).show();
                    break;
                case REGISTER_SUCCEED:
                    Toast.makeText(Register.this,"注册成功",Toast.LENGTH_SHORT).show();
                    GlobalVariable globalVariable = (GlobalVariable)getApplication();
                    globalVariable.setAccount(account);
                    globalVariable.setName(nameText);
                    SharedPreferences sharedPreferences = getSharedPreferences("Login", MODE_PRIVATE);
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putString("Account", account);
                    editor.putString("Password",password);
                    editor.commit();
                    setResult(RESULT_OK);
                    finish();
                    break;
                default:
                    break;
            }
        }
    };

    private EditText name;
    private EditText age;
    private ImageView man;
    private ImageView woman;
    private EditText motto;
    private Button ok;
    private final int BLANK = 0;
    private final int MAN = 1;
    private final int WOMAN = 2;
    private int state = BLANK;
    private String account;
    private String password;
    private String nameText;
    private String ageText;
    private String mottoText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        account = getIntent().getStringExtra("Account");
        password = getIntent().getStringExtra("Password");

        name = (EditText)findViewById(R.id.name);
        age = (EditText)findViewById(R.id.age);
        man = (ImageView)findViewById(R.id.man);
        woman = (ImageView)findViewById(R.id.woman);
        motto = (EditText)findViewById(R.id.motto);
        ok = (Button)findViewById(R.id.ok);

        man.setOnClickListener(this);
        woman.setOnClickListener(this);
        ok.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.man:
                state = MAN;
                sex_click(state);
                break;
            case R.id.woman:
                state = WOMAN;
                sex_click(state);
                break;
            case R.id.ok:
                nameText = name.getText().toString();
                ageText = age.getText().toString();
                mottoText = motto.getText().toString();

                if(nameText.equals("") || ageText.equals("") || mottoText.equals("") || state == BLANK) {
                    Toast.makeText(Register.this,"请填写全部信息",Toast.LENGTH_SHORT).show();
                } else {
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                String sex;
                                if(state == MAN) {
                                    sex = "男";
                                 } else {
                                    sex = "女";
                                 }
                                OkHttpClient okHttpClient = new OkHttpClient();
                                RequestBody requestBody = new FormBody.Builder()
                                        .add("Account",account)
                                        .add("Password",password)
                                        .add("Name",nameText)
                                        .add("Age",ageText)
                                        .add("Sex",sex)
                                        .add("Motto",mottoText)
                                        .build();
                                Request request = new Request.Builder().post(requestBody).url("http://106.15.201.54:8080/Freego/register").build();
                                Response response = okHttpClient.newCall(request).execute();
                                Message message = new Message();
                                if(response.body().string().toString().equals("true")) {
                                    message.what = REGISTER_SUCCEED;
                                } else {
                                    message.what = MESSAGE_REPEAT;
                                }
                                handler.sendMessage(message);
                            } catch (Exception e) {
                                Message message = new Message();
                                message.what = NETWORK_ERROR;
                                handler.sendMessage(message);
                            }
                        }
                    }).start();
                }
                break;
            default:
                break;
        }
    }

    private void sex_click(int state) {
        switch (state) {
            case MAN:
                man.setBackgroundColor(Color.parseColor("#60000000"));
                woman.setBackgroundColor(Color.parseColor("#00000000"));
                break;
            case WOMAN:
                woman.setBackgroundColor(Color.parseColor("#60000000"));
                man.setBackgroundColor(Color.parseColor("#00000000"));
                break;
            default:
                break;
        }
    }

}
