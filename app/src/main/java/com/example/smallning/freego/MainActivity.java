package com.example.smallning.freego;


import android.Manifest;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import id.zelory.compressor.Compressor;
import me.nereo.multi_image_selector.MultiImageSelectorActivity;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{


    private LinearLayout main;
    private LinearLayout map;
    private LinearLayout society;
    private LinearLayout option;
    private ImageView mainIcon;
    private ImageView mapIcon;
    private ImageView societyIcon;
    private ImageView optionIcon;
    private NavigationView navigationView;
    private CircleImageView portrait;
    private TextView headerName;
    private TextView headerAccount;

    private MainFragment mainFragment;
    private MapFragment mapFragment;
    private SocietyFragment societyFragment;
    private OptionFragment optionFragment;

    private final int MAIN = 0;
    private final int MAP = 1;
    private final int SOCIETY = 2;
    private final int OPTION = 3;
    private int state = MAIN;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        navigationView = (NavigationView)findViewById(R.id.nav);
        View headerView = navigationView.getHeaderView(0);
        portrait = headerView.findViewById(R.id.portrait);
        headerName = headerView.findViewById(R.id.name);
        headerAccount = headerView.findViewById(R.id.account);

        portrait.setOnClickListener(this);

        SharedPreferences login = getSharedPreferences("Login",MODE_PRIVATE);
        final String account = login.getString("Account",null);
        if(account == null) {
            Intent intent = new Intent(MainActivity.this, LoginActivity.class);
            startActivityForResult(intent,1);
        } else {
            final String password = login.getString("Password","");
            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        OkHttpClient okHttpClient = new OkHttpClient();
                        RequestBody requestBody = new FormBody.Builder()
                                .add("Account", account)
                                .add("Password", password)
                                .build();
                        Request request = new Request.Builder().url("http://106.15.201.54:8080/Freego/login").post(requestBody).build();
                        Response response = okHttpClient.newCall(request).execute();
                        String text = response.body().string().toString();
                        if(!text.equals("")) {
                            GlobalVariable globalVariable = (GlobalVariable)getApplication();
                            globalVariable.setAccount(account);
                            globalVariable.setName(text);
                            SharedPreferences sharedPreferences = getSharedPreferences("Login", MODE_PRIVATE);
                            SharedPreferences.Editor editor = sharedPreferences.edit();
                            editor.putString("Account", account);
                            editor.putString("Password", password);
                            getHeaderMessage(text);
                        } else {
                           startLogin("密码错误请重新登录");
                        }
                    } catch (Exception e) {
                        startLogin("网络故障");
                    }
                }
            }).start();

        }

        main = (LinearLayout)findViewById(R.id.main);
        map = (LinearLayout)findViewById(R.id.map);
        society = (LinearLayout)findViewById(R.id.society);
        option = (LinearLayout)findViewById(R.id.option);
        mainIcon = (ImageView)findViewById(R.id.mainIcon);
        mapIcon = (ImageView)findViewById(R.id.mapIcon);
        societyIcon = (ImageView)findViewById(R.id.societyIcon);
        optionIcon = (ImageView)findViewById(R.id.optionIcon);

        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                Intent intent = null ;
                switch (item.getItemId()) {
                    case R.id.item1:
                        intent = new Intent(MainActivity.this,MyFriends.class);
                        break;
                    case R.id.item2:
                        intent = new Intent(MainActivity.this,MyCollection.class);
                        break;
                    case R.id.item3:
                        intent = new Intent(MainActivity.this,MyMessage.class);
                        break;
                    case R.id.item4:
                        intent = new Intent(MainActivity.this,MyPhoto.class);
                        break;
                    default:
                        break;
                }
                startActivity(intent);
                return false;
            }
        });
        main.setOnClickListener(this);
        map.setOnClickListener(this);
        society.setOnClickListener(this);
        option.setOnClickListener(this);
        mainFragment = new MainFragment();
        mapFragment = new MapFragment();
        societyFragment = new SocietyFragment();
        optionFragment = new OptionFragment();

        showFragment(mainFragment);


    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.main:
                state = MAIN;
                checkClick();
                showFragment(mainFragment);
                break;
            case R.id.map:
                state = MAP;
                checkClick();
                showFragment(mapFragment);
                break;
            case R.id.society:
                state = SOCIETY;
                checkClick();
                showFragment(societyFragment);
                break;
            case R.id.option:
                state = OPTION;
                checkClick();
                showFragment(optionFragment);
                break;
            case R.id.portrait:
                Intent intent = new Intent(MainActivity.this,LoginActivity.class);
                startActivityForResult(intent,1);
            default:
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case 1:
                if (resultCode == RESULT_OK) {
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                getHeaderMessage(((GlobalVariable)getApplication()).getName());
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    }).start();
                } else {
                    finish();
                }
                break;
            case 2:
                if (resultCode == RESULT_OK) {
                    List<String> path = data.getStringArrayListExtra(MultiImageSelectorActivity.EXTRA_RESULT);
                    try {
                        for (String p : path) {
                            File file = new File(p);
                            new Compressor(this)
                                    .setMaxWidth(640)
                                    .setMaxHeight(480)
                                    .setQuality(75)
                                    .setCompressFormat(Bitmap.CompressFormat.WEBP)
                                    .setDestinationDirectoryPath(Environment.getExternalStoragePublicDirectory(
                                            Environment.DIRECTORY_PICTURES).getAbsolutePath())
                                    .compressToFile(file);
                        }
                        Toast.makeText(MainActivity.this, "压缩成功\n请到Pictures目录查看", Toast.LENGTH_SHORT).show();
                    } catch (Exception e) {
                        Toast.makeText(MainActivity.this, "压缩失败\n格式不支持", Toast.LENGTH_SHORT).show();
                        e.printStackTrace();
                    }
                }
                break;
            case 3:
                if (resultCode == RESULT_OK) {
                    societyFragment.onActivityResult(1, resultCode, data);
                }
                break;
            default:
                break;
        }
    }

    private void startLogin(final String state) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(MainActivity.this,state,Toast.LENGTH_SHORT).show();
            }
        });
        Intent intent = new Intent(MainActivity.this,LoginActivity.class);
        startActivityForResult(intent,1);
    }

    private void getHeaderMessage(String name) throws Exception {
        OkHttpClient okHttpClient = new OkHttpClient();
        RequestBody body = new FormBody.Builder().add("Name",name).build();
        Request req =  new Request.Builder().post(body).url("http://106.15.201.54:8080/Freego/getPortrait").build();
        Response res = okHttpClient.newCall(req).execute();
        byte[] picture = res.body().bytes();
        final Bitmap bitmap = BitmapFactory.decodeByteArray(picture, 0, picture.length);
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                portrait.setImageBitmap(bitmap);
                headerAccount.setText(((GlobalVariable)getApplication()).getAccount());
                headerName.setText(((GlobalVariable)getApplication()).getName());
            }
        });
    }

    private void showFragment(Fragment fragment) {
        FragmentManager manager = getSupportFragmentManager();
        FragmentTransaction transaction=manager.beginTransaction();
        if(!fragment.isAdded())  {
            transaction.add(R.id.fragmentPosition,fragment);
        }
        hideFragment();
        transaction.show(fragment);
        transaction.commit();
    }

    private void hideFragment(){
        FragmentTransaction transaction=getSupportFragmentManager().beginTransaction();
        transaction.hide(mainFragment).hide(mapFragment).hide(societyFragment).hide(optionFragment);
        transaction.commit();
    }

    private void checkClick() {
        switch(state) {
            case MAIN:
                mainIcon.setImageBitmap(BitmapFactory.decodeResource(getResources(), R.mipmap.main_click));
                mapIcon.setImageBitmap(BitmapFactory.decodeResource(getResources(), R.mipmap.map));
                societyIcon.setImageBitmap(BitmapFactory.decodeResource(getResources(), R.mipmap.society));
                optionIcon.setImageBitmap(BitmapFactory.decodeResource(getResources(), R.mipmap.option));
                break;
            case MAP:
                mainIcon.setImageBitmap(BitmapFactory.decodeResource(getResources(), R.mipmap.main));
                mapIcon.setImageBitmap(BitmapFactory.decodeResource(getResources(), R.mipmap.map_click));
                societyIcon.setImageBitmap(BitmapFactory.decodeResource(getResources(), R.mipmap.society));
                optionIcon.setImageBitmap(BitmapFactory.decodeResource(getResources(), R.mipmap.option));
                break;
            case SOCIETY:
                mainIcon.setImageBitmap(BitmapFactory.decodeResource(getResources(), R.mipmap.main));
                mapIcon.setImageBitmap(BitmapFactory.decodeResource(getResources(), R.mipmap.map));
                societyIcon.setImageBitmap(BitmapFactory.decodeResource(getResources(), R.mipmap.society_click));
                optionIcon.setImageBitmap(BitmapFactory.decodeResource(getResources(), R.mipmap.option));
                break;
            case OPTION:
                mainIcon.setImageBitmap(BitmapFactory.decodeResource(getResources(), R.mipmap.main));
                mapIcon.setImageBitmap(BitmapFactory.decodeResource(getResources(), R.mipmap.map));
                societyIcon.setImageBitmap(BitmapFactory.decodeResource(getResources(), R.mipmap.society));
                optionIcon.setImageBitmap(BitmapFactory.decodeResource(getResources(), R.mipmap.option_click));
                break;
            default:
                break;

        }
    }
}
