package com.example.smallning.freego;


import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.tencent.smtt.sdk.WebChromeClient;
import com.tencent.smtt.sdk.WebSettings;
import com.tencent.smtt.sdk.WebView;
import com.tencent.smtt.sdk.WebViewClient;


/**
 * Created by Smallning on 2017/9/5.
 */

public class MapFragment extends Fragment {


//    private MapView mapView;
//    private TextView textName;
//    private TextView textAddress;
//    private TextView textDistance;
//    private EditText searchMessage;
//    private ListView POIListView;
//
//    private Button button;
//
//    private View popup;
//    private AMap aMap;
//
//
//    private LatLng position;
//
//    private String locationCity;
//
//    boolean  isFirstLocation=true;

    private WebView webView;

    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_map, container, false);
        webView = view.findViewById(R.id.webView);
        webView.setWebViewClient(new WebViewClient());
        WebSettings webSettings = webView.getSettings();
        webSettings.setDatabaseEnabled(true);
        webSettings.setLoadWithOverviewMode(true);
        webSettings.setUseWideViewPort(true);
        String dir = getActivity().getApplicationContext().getDir("Database", Context.MODE_PRIVATE).getPath();
        webSettings.setJavaScriptEnabled(true);
        webSettings.setDomStorageEnabled(true);
        webSettings.setGeolocationDatabasePath(dir);
        webSettings.setAppCacheEnabled(true);
        webView.setWebChromeClient(new WebChromeClient());
        webView.setWebViewClient(new WebViewClient());
        int checkPermission = ContextCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_COARSE_LOCATION);
        if (checkPermission != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.ACCESS_COARSE_LOCATION}, 1);
            ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 2);
            webView.loadUrl("https://m.amap.com/");
        }
        webView.loadUrl("https://m.amap.com/");
        return view;
    }
}
//        mapView = view.findViewById(R.id.map);
//        textName = view.findViewById(R.id.name);
//        textAddress = view.findViewById(R.id.address);
//        textDistance = view.findViewById(R.id.distance);
//        searchMessage = view.findViewById(R.id.searchMessage);
//        POIListView = view.findViewById(R.id.POIListView);
//
//        button = view.findViewById(R.id.button);
//
//        searchMessage.setOnClickListener(this);
//        button.setOnClickListener(this);
//
//        popup = view.findViewById(R.id.popup);
//        mapView.onCreate(savedInstanceState);
//        init();
//        realizePosition();
//        realizedPOIClick();
//
//
//        return view;
//    }
//
//    //点击监听器
//    @Override
//    public void onClick(View view) {
//        switch (view.getId()) {
//            case R.id.button:
//                //button点击事件
//                break;
//            //EditView查询栏点击事件
//            case R.id.searchMessage:
//
//        }
//    }
//
//    //初始化操作
//    private void init() {
//        //定位动态授权
//        int checkPermission = ContextCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_COARSE_LOCATION);
//        if (checkPermission != PackageManager.PERMISSION_GRANTED) {
//            ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.ACCESS_COARSE_LOCATION}, 1);
//        }
//        aMap = mapView.getMap();
//        realizeUiSetting();
//
//        //地图加载监听器
//        aMap.setOnMapLoadedListener(new AMap.OnMapLoadedListener() {
//            @Override
//            public void onMapLoaded() {
//                //加载完成后设置界面初始缩放比例
//                aMap.moveCamera(CameraUpdateFactory.zoomTo(16));
//            }
//        });
//
//        //地图点击监听器
//        aMap.setOnMapClickListener(new AMap.OnMapClickListener() {
//            @Override
//            public void onMapClick(LatLng latLng) {
//                //点击后取消POI弹窗
//                popup.setVisibility(View.GONE);
//                textAddress.setText("");
//            }
//        });
//
//        //设置Edit文本改变监听器
//        searchMessage.addTextChangedListener(new TextWatcher() {
//            @Override
//            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
//
//            }
//            @Override
//            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
//
//            }
//
//            @Override
//            //当Text改变时触发
//            public void afterTextChanged(Editable editable) {
//                if (editable.length() != 0) {
//                    POIListView.setVisibility(View.VISIBLE);
//                    realizePOISearch(new PoiSearch.OnPoiSearchListener() {
//                        @Override
//                        public void onPoiSearched(PoiResult poiResult, int i) {
//                            List<POIMessage> POIMessageList = new ArrayList<>();
//                            List<PoiItem> items = poiResult.getPois();
//                            POIMessageAdapter adapter = new POIMessageAdapter(getActivity(),R.layout.poi_message,POIMessageList);
//                            for(PoiItem item:items) {
//                                String address;
//                                address = item.getProvinceName() + "-" + item.getCityName() + "-" + item.getAdName()
//                                        + "-" + item.getSnippet();
//                                POIMessage poiMessage = new POIMessage(item.getTitle(),address);
//                                POIMessageList.add(poiMessage);
//                            }
//                            POIListView.setAdapter(adapter);
//
//                        }
//
//                        @Override
//                        public void onPoiItemSearched(PoiItem poiItem, int i) {
//
//                        }
//                    }, editable.toString());
//                } else {
//                    POIListView.setVisibility(View.GONE);
//                }
//            }
//        });
//    }
//
//    //POI信息类，用于查询
//    private class POIMessage {
//        private String name;
//        private String address;
//
//        public POIMessage(String name,String address) {
//            this.name = name;
//            this.address = address;
//        }
//
//        public String getAddress() {
//            return address;
//        }
//
//        public String getName() {
//            return name;
//        }
//    }
//
//    private class POIMessageAdapter extends ArrayAdapter<POIMessage> {
//        private int layoutId;
//
//        public POIMessageAdapter(Context context, int layoutId, List<POIMessage> POIMessageList) {
//            super(context,layoutId,POIMessageList);
//            this.layoutId = layoutId;
//    }
//
//        @NonNull
//        @Override
//        public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
//            POIMessage poiMessage = getItem(position);
//            View view = LayoutInflater.from(getContext()).inflate(layoutId,parent,false);
//            TextView name= view.findViewById(R.id.nameOfPOIItem);
//            TextView address = view.findViewById(R.id.addressOfPOIItem);
//            name.setText(poiMessage.getName());
//            address.setText(poiMessage.getAddress());
//            return view;
//        }
//    }
//
//
//
//    //地图界面设置
//    private void realizeUiSetting() {
//            UiSettings settings = aMap.getUiSettings();
//        settings.setMyLocationButtonEnabled(true);
//    }
//
//    private void realizePosition() {
//        //设置定位蓝点
//        MyLocationStyle myLocationStyle = new MyLocationStyle();
//        myLocationStyle.interval(1000);
//        myLocationStyle.myLocationType(MyLocationStyle.LOCATION_TYPE_LOCATION_ROTATE_NO_CENTER);
//        myLocationStyle.strokeColor(Color.argb(0,0,0,0));
//        myLocationStyle.radiusFillColor(Color.argb(0,0,0,0));
//        aMap.setMyLocationStyle(myLocationStyle);
//        aMap.setMyLocationEnabled(true);
//
//        //设置定位监听器
//        aMap.setOnMyLocationChangeListener(new AMap.OnMyLocationChangeListener() {
//            @Override
//            public void onMyLocationChange(Location location) {
//                double lat = location.getLatitude();
//                double lng = location.getLongitude();
//                position = new LatLng(lat, lng);
//                if (isFirstLocation) {
//                    aMap. moveCamera(CameraUpdateFactory.changeLatLng(position));
//                    isFirstLocation = false;
//
//                    //获取所在城市
//                    GeocodeSearch geocodeSearch = new GeocodeSearch(getActivity());
//                    RegeocodeQuery query = new RegeocodeQuery(LatLngToLatLonPoint(position),100,GeocodeSearch.AMAP);
//                    geocodeSearch.getFromLocationAsyn(query);
//                    geocodeSearch.setOnGeocodeSearchListener(new GeocodeSearch.OnGeocodeSearchListener() {
//                        @Override
//                        public void onRegeocodeSearched(RegeocodeResult regeocodeResult, int i) {
//                            locationCity = regeocodeResult.getRegeocodeAddress().getCity();
//                        }
//
//                        @Override
//                        public void onGeocodeSearched(GeocodeResult geocodeResult, int i) {
//                        }
//                    });
//
//
//                }
//            }
//        });
//    }
//
//    //POI关键字查找
//    private void realizePOISearch(PoiSearch.OnPoiSearchListener listener,String... keyword) {
//        PoiSearch.Query query;
//        if(keyword.length==1) {
//            query = new PoiSearch.Query(keyword[0],"",locationCity);
//        } else {
//            query = new PoiSearch.Query(keyword[0],"",locationCity);
//        }
//        query.setPageSize(10);
//        query.setPageNum(3);
//        PoiSearch poiSearch = new PoiSearch(getActivity(),query);
//        poiSearch.setOnPoiSearchListener(listener);
//        poiSearch.searchPOIAsyn();
//}
//
//
//    //POI点击事件
//    private void realizedPOIClick() {
//        aMap.setOnPOIClickListener(new AMap.OnPOIClickListener() {
//            @Override
//            public void onPOIClick(Poi poi) {
//                String poiName = poi.getName();
//                LatLng poiPosition = poi.getCoordinate();
//                aMap.moveCamera(CameraUpdateFactory.changeLatLng(poiPosition));
//                textName.setText(poiName);
//
//                //计算距离
//                float distance = AMapUtils.calculateLineDistance(poiPosition,position);
//                if(distance>1000) {
//                    float dis = Math.round(distance / 100) / 10;
//                    textDistance.setText(dis + "km");
//                } else {
//                    int dis = Math.round(distance);
//                    textDistance.setText(dis + "m");
//                }
//                String poiId = poi.getPoiId();
//                PoiSearch poiSearch = new PoiSearch(getActivity(),null);
//                poiSearch.searchPOIIdAsyn(poiId);
//
//                //查找所点击的POI详细信息
//                poiSearch.setOnPoiSearchListener(new PoiSearch.OnPoiSearchListener() {
//                    @Override
//                    public void onPoiSearched(PoiResult poiResult, int i) {
//                    }
//
//                    @Override
//                    public void onPoiItemSearched(PoiItem poiItem, int i) {
//                        textAddress.setText(poiItem.getSnippet());
//
////                        poiItem.getAdCode();
////                        poiItem.getAdName();
////                        poiItem.getBusinessArea();
////                        poiItem.getCityCode();
////                        poiItem.getDirection();
////                        poiItem.getCityName();
////                        poiItem.getDistance();
////                        poiItem.getEmail();
////                        poiItem.getParkingType();
////                        poiItem.getPhotos();
////                        String message = poiItem.getPoiExtension().toString();
//                    }
//                });
//                popup.setVisibility(View.VISIBLE);
//            }
//        });
//    }
//
//    //得到屏幕中心点，转换为经纬度
//    private LatLng getScreenCentre() {
//        int left = mapView.getLeft();
//        int right = mapView.getRight();
//        int top = mapView.getTop();
//        int bottom = mapView.getBottom();
//        int x = (int)mapView.getX() + (right-left) / 2;
//        int y = (int)mapView.getY() + (bottom-top) / 2;
//        return aMap.getProjection().fromScreenLocation(new Point(x,y));
//    }
//
//    private LatLonPoint LatLngToLatLonPoint(LatLng latLng) {
//        return new LatLonPoint(latLng.latitude,latLng.longitude);
//    }
//
//    private LatLng LatLngToLatLonPoint(LatLonPoint latLonPoint) {
//        return new LatLng(latLonPoint.getLatitude(),latLonPoint.getLongitude());
//    }
//
//    @Override
//    public void onResume() {
//        super.onResume();
//        mapView.onResume();
//    }
//
//    @Override
//    public void onPause() {
//        super.onPause();
//        mapView.onPause();
//    }
//
//    @Override
//    public void onDestroy() {
//        super.onDestroy();
//        mapView.onDestroy();
//    }
//
//    @Override
//    public void onSaveInstanceState(Bundle outState) {
//        super.onSaveInstanceState(outState);
//        mapView.onSaveInstanceState(outState);
//    }
//}
