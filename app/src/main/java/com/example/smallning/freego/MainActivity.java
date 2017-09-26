package com.example.smallning.freego;


import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{

    //我改了些东西

    private Button mainButton;
    private Button mapButton;
    private Button societyButton;
    private Button optionButton;

    private Toolbar toolbar;

    private MainFragment mainFragment;
    private MapFragment mapFragment;
    private SocietyFragment societyFragment;
    private OptionFragment optionFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        toolbar=(Toolbar)findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //底栏按钮
        mainButton=(Button)findViewById(R.id.mainButton);
        mapButton=(Button)findViewById(R.id.mapButton);
        societyButton=(Button)findViewById(R.id.societyButton);
        optionButton=(Button)findViewById(R.id.optionButton);
        //注册按钮
        mainButton.setOnClickListener(this);
        mapButton.setOnClickListener(this);
        societyButton.setOnClickListener(this);
        optionButton.setOnClickListener(this);

        mainFragment=new MainFragment();
        mapFragment=new MapFragment();
        societyFragment=new SocietyFragment();
        optionFragment=new OptionFragment();


    }
    //重写onCreateOptionsMenu方法
    //加入menu模块
    public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu.nav_menu,menu);
        return true;
    }

    //menu响应模块
    public boolean onOptionsItemSelected(MenuItem item){
        switch (item.getItemId()){
            case R.id.item1:
                Toast.makeText(this,"You clicked item1",Toast.LENGTH_LONG).show();
                break;
            case R.id.item2:
                Toast.makeText(this,"You clicked item2",Toast.LENGTH_LONG).show();
                break;
            case R.id.item3:
                Toast.makeText(this,"You clicked item3",Toast.LENGTH_LONG).show();
                break;
            case R.id.item4:
                Toast.makeText(this,"You clicked item4",Toast.LENGTH_LONG).show();
                break;
            default:;
        }
        return true;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.mainButton:
                showFragment(mainFragment);
                break;
            case R.id.mapButton:
                showFragment(mapFragment);
                break;
            case R.id.societyButton:
                showFragment(societyFragment);
                break;
            case R.id.optionButton:
                showFragment(optionFragment);
                break;
            default:
                break;
        }
    }
    private void showFragment(Fragment fragment) {
        FragmentManager manager=getSupportFragmentManager();
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

}
