package com.example.smallning.freego;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Smallning on 2017/9/6.
 */

public class MainFragment extends Fragment {

    List<Scenery> sceneryList = new ArrayList<>();
    RecyclerView sceneryView;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_main,container,false);

        sceneryView = view.findViewById(R.id.sceneryView);
        WindowManager wm = getActivity().getWindowManager();
        int width = wm.getDefaultDisplay().getWidth();

        init();
        StaggeredGridLayoutManager layoutManager = new StaggeredGridLayoutManager(2,StaggeredGridLayoutManager.VERTICAL);
        sceneryView.setLayoutManager(layoutManager);
        SceneryAdapter adapter = new SceneryAdapter(sceneryList,getActivity(),width);
        sceneryView.setAdapter(adapter);

        return view;
    }

    private void init() {
        sceneryList.add(new Scenery("北京",R.mipmap.beijing,"https://m.tuniu.com/g200/guide-0-0/"));
        sceneryList.add(new Scenery("上海",R.mipmap.shanghai,"https://m.tuniu.com/g2500/guide-0-0/"));
        sceneryList.add(new Scenery("天津",R.mipmap.tianjin,"https://m.tuniu.com/g3000/guide-0-0/"));
        sceneryList.add(new Scenery("重庆",R.mipmap.chongqing,"https://m.tuniu.com/g300/guide-0-0/"));
        sceneryList.add(new Scenery("河北",R.mipmap.hebei,"https://m.tuniu.com/g1000/guide-0-0/"));
        sceneryList.add(new Scenery("山东",R.mipmap.shandong,"https://m.tuniu.com/g2400/guide-0-0/"));
        sceneryList.add(new Scenery("辽宁",R.mipmap.liaoning,"https://m.tuniu.com/g1900/guide-0-0/"));
        sceneryList.add(new Scenery("黑龙江",R.mipmap.heilongjiang,"https://m.tuniu.com/g1100/guide-0-0/"));
        sceneryList.add(new Scenery("吉林",R.mipmap.jilin,"https://m.tuniu.com/g1808/guide-0-0/"));
        sceneryList.add(new Scenery("甘肃",R.mipmap.gansu,"https://m.tuniu.com/g500/guide-0-0/"));

        sceneryList.add(new Scenery("青海",R.mipmap.qinghai,"https://m.tuniu.com/g2300/guide-0-0/"));
        sceneryList.add(new Scenery("河南",R.mipmap.henan,"https://m.tuniu.com/g1200/guide-0-0/"));
        sceneryList.add(new Scenery("江苏",R.mipmap.jiangsu,"https://m.tuniu.com/g1600/guide-0-0/"));
        sceneryList.add(new Scenery("湖北",R.mipmap.hubei,"https://m.tuniu.com/g1400/guide-0-0/"));
        sceneryList.add(new Scenery("湖南",R.mipmap.hunan,"https://m.tuniu.com/g1500/guide-0-0/"));
        sceneryList.add(new Scenery("江西",R.mipmap.jiangxi,"https://m.tuniu.com/g1700/guide-0-0/"));
        sceneryList.add(new Scenery("浙江",R.mipmap.zhejiang,"https://m.tuniu.com/g3400/guide-0-0/"));
        sceneryList.add(new Scenery("广东",R.mipmap.guangdong,"https://m.tuniu.com/g600/guide-0-0/"));
        sceneryList.add(new Scenery("云南",R.mipmap.yunnan,"https://m.tuniu.com/g3300/guide-0-0/"));
        sceneryList.add(new Scenery("福建",R.mipmap.fujian,"https://m.tuniu.com/g400/guide-0-0/"));

        sceneryList.add(new Scenery("山西",R.mipmap.shanxi,"https://m.tuniu.com/g2600/guide-0-0/"));
        sceneryList.add(new Scenery("四川",R.mipmap.sichuan,"https://m.tuniu.com/g2800/guide-0-0/"));
        sceneryList.add(new Scenery("陕西",R.mipmap.shan3xi,"https://m.tuniu.com/g2700/guide-0-0/"));
        sceneryList.add(new Scenery("贵州",R.mipmap.guizhou,"https://m.tuniu.com/g800/guide-0-0/"));
        sceneryList.add(new Scenery("安徽",R.mipmap.anhui,"https://m.tuniu.com/g100/guide-0-0/"));
        sceneryList.add(new Scenery("海南",R.mipmap.hainan,"https://m.tuniu.com/g900/guide-0-0/"));
        sceneryList.add(new Scenery("广西",R.mipmap.guangxi,"https://m.tuniu.com/g700/guide-0-0/"));
        sceneryList.add(new Scenery("内蒙古",R.mipmap.neimenggu,"https://m.tuniu.com/g2100/guide-0-0/"));
        sceneryList.add(new Scenery("西藏",R.mipmap.xizang,"https://m.tuniu.com/g3200/guide-0-0/"));
        sceneryList.add(new Scenery("新疆",R.mipmap.xinjiang,"https://m.tuniu.com/g3100/guide-0-0/"));

        sceneryList.add(new Scenery("宁夏",R.mipmap.ningxia,"https://m.tuniu.com/g2200/guide-0-0/"));
        sceneryList.add(new Scenery("台湾",R.mipmap.taiwan,"https://m.tuniu.com/g2900/guide-0-0/"));
        sceneryList.add(new Scenery("香港",R.mipmap.xianggang,"https://m.tuniu.com/g1300/guide-0-0/"));
        sceneryList.add(new Scenery("澳门",R.mipmap.aomen,"https://m.tuniu.com/g2002/guide-0-0/"));
    }


}
