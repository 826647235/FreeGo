package com.example.smallning.freego;


import android.Manifest;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.BitmapFactory;
import android.support.design.widget.NavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import de.hdodenhof.circleimageview.CircleImageView;

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
        main = (LinearLayout)findViewById(R.id.main);
        map = (LinearLayout)findViewById(R.id.map);
        society = (LinearLayout)findViewById(R.id.society);
        option = (LinearLayout)findViewById(R.id.option);
        mainIcon = (ImageView)findViewById(R.id.mainIcon);
        mapIcon = (ImageView)findViewById(R.id.mapIcon);
        societyIcon = (ImageView)findViewById(R.id.societyIcon);
        optionIcon = (ImageView)findViewById(R.id.optionIcon);
        main.setOnClickListener(this);
        map.setOnClickListener(this);
        society.setOnClickListener(this);
        option.setOnClickListener(this);
        mainFragment = new MainFragment();
        mapFragment = new MapFragment();
        societyFragment = new SocietyFragment();
        optionFragment = new OptionFragment();

        showFragment(mainFragment);

        View headerView = navigationView.getHeaderView(0);
        CircleImageView circleImageView = headerView.findViewById(R.id.portrait);
        circleImageView.setOnClickListener(this);
        headerView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(MainActivity.this,"hhh",Toast.LENGTH_SHORT).show();
            }
        });

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
                startActivity(intent);
            default:
                break;
        }
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
