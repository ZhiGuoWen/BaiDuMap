package com.wenzhiguo.baidumap;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;
import android.widget.ZoomControls;

import com.baidu.mapapi.SDKInitializer;
import com.baidu.mapapi.cloud.CloudListener;
import com.baidu.mapapi.cloud.CloudManager;
import com.baidu.mapapi.cloud.CloudPoiInfo;
import com.baidu.mapapi.cloud.CloudRgcResult;
import com.baidu.mapapi.cloud.CloudSearchResult;
import com.baidu.mapapi.cloud.DetailSearchResult;
import com.baidu.mapapi.cloud.NearbySearchInfo;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.MapStatus;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.map.OverlayOptions;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.model.LatLngBounds;
/**图片适配
 * 屏幕适配
 * 代码适配
 * 权重适配
 * 百分比适配
 */
public class MainActivity extends AppCompatActivity implements View.OnClickListener, CloudListener {
    //八维坐标116.306416,40.04919,设置值的时候,参1纬度,参2精度,正好与搜索的相反
    LatLng mLatLng = new LatLng(40.04919, 116.306416);
    //移动到的位置114.551841,35.666831
    LatLng latLng = new LatLng(35.666831, 114.551841);
    private MapView mMapView;
    private Button mCondensation;
    private Button mOverLook;
    private Button mMoving;
    private BaiduMap mBaiduMap;
    private Button mMagnifying;
    private Button mCommon;
    private Button mTraffic;
    private Button mSatellite;
    private Button mFood;
    private Button mJump;
    private MapStatus mMapStatus;
    private MapStatusUpdate msu;
    int overlookAngle = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //在使用SDK各组件之前初始化context信息，传入ApplicationContext
        //注意该方法要再setContentView方法之前实现
        SDKInitializer.initialize(getApplicationContext());
        setContentView(R.layout.activity_main);
        //获取地图控件引用
        initView();
        // 隐藏logo
        View child = mMapView.getChildAt(1);
        if (child != null && (child instanceof ImageView || child instanceof ZoomControls)) {
            child.setVisibility(View.INVISIBLE);
        }
        //定义地图状态 zoom是缩放级别值越大比例越小(所在层级)
        MapStatus mMapStatus = new MapStatus.Builder()
                .target(mLatLng)
                .zoom(15)
                .build();
        //定义MapStatusUpdate对象，以便描述地图状态将要发生的变化
        MapStatusUpdate mMapStatusUpdate = MapStatusUpdateFactory.newMapStatus(mMapStatus);
        //改变地图状态
        mBaiduMap.setMapStatus(mMapStatusUpdate);
        //云检索
        CloudManager.getInstance().init(this);
        double v = Math.sqrt(1080 * 1080) + (1920 * 1920) / 5.5;
        Toast.makeText(this, "计算值"+v, Toast.LENGTH_SHORT).show();
    }

    private void initView() {
        mMapView = (MapView) findViewById(R.id.mapview);
        //缩放
        mCondensation = (Button) findViewById(R.id.condensation);
        //放大
        mMagnifying = (Button) findViewById(R.id.magnifying);
        //俯视
        mOverLook = (Button) findViewById(R.id.overlook);
        //移动
        mMoving = (Button) findViewById(R.id.moving);
        //普通
        mCommon = (Button) findViewById(R.id.common);
        //卫星
        mSatellite = (Button) findViewById(R.id.satellite);
        //交通
        mTraffic = (Button) findViewById(R.id.traffic);
        //美食
        mFood = (Button) findViewById(R.id.food);
        //跳转
        mJump = (Button) findViewById(R.id.jump);
        //初始化地图
        mBaiduMap = mMapView.getMap();
        //监听
        mMagnifying.setOnClickListener(this);
        mCondensation.setOnClickListener(this);
        mMoving.setOnClickListener(this);
        mOverLook.setOnClickListener(this);
        mCommon.setOnClickListener(this);
        mSatellite.setOnClickListener(this);
        mTraffic.setOnClickListener(this);
        mFood.setOnClickListener(this);
        mJump.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            //放大
            case R.id.magnifying:
                //最大级别21
                float maxZoomLevel = mBaiduMap.getMaxZoomLevel();
                MapStatusUpdate zoomOut = MapStatusUpdateFactory.zoomOut();
                mBaiduMap.setMapStatus(zoomOut);
                break;
            //缩放
            case R.id.condensation:
                //最小级别3
                float minZoomLevel = mBaiduMap.getMinZoomLevel();
                MapStatusUpdate zoomIn = MapStatusUpdateFactory.zoomIn();
                mBaiduMap.setMapStatus(zoomIn);
                break;
            //俯视
            case R.id.overlook:
                mMapStatus = new MapStatus.Builder(mBaiduMap.getMapStatus()).overlook(overlookAngle -= 3).build();
                msu = MapStatusUpdateFactory.newMapStatus(mMapStatus);
                mBaiduMap.animateMapStatus(msu);
                break;
            //移动
            case R.id.moving:
                //定义地图状态
                MapStatus build = new MapStatus
                        .Builder()
                        .target(latLng)//设置移动的距离
                        .zoom(20)//放大地图
                        .build();
                //描述即将发生的状态
                MapStatusUpdate mapStatusUpdate = MapStatusUpdateFactory.newMapStatus(build);
                mBaiduMap.animateMapStatus(mapStatusUpdate, 3000);
                //改变状态
                mBaiduMap.setMapStatus(mapStatusUpdate);
                break;
            //普通
            case R.id.common:
                mBaiduMap.setMapType(BaiduMap.MAP_TYPE_NORMAL);
                mBaiduMap.setTrafficEnabled(false);
                break;
            //卫星
            case R.id.satellite:
                mBaiduMap.setMapType(BaiduMap.MAP_TYPE_SATELLITE);
                mBaiduMap.setTrafficEnabled(false);
                break;
            //交通
            case R.id.traffic:
                mBaiduMap.setTrafficEnabled(true);
                break;
            //美食
            case R.id.food:
                //创建百度地图特有的容器NearbySearchInfo,用来放请求我们虎鲸平台的数据
                NearbySearchInfo info = new NearbySearchInfo();
                //服务器的AK值,注意必须是字符串
                info.ak = "NTh9Ek3R3iGkf1dvNnwX7WfLyrPLlMGM";
                //百度地图自定义数据库的KEY值,int型
                info.geoTableId = 167910;
                //设置搜索的最大半径,int型
                info.radius = 3000000;
                //确定搜索中心的坐标,字符串,注意这里就是经度在前,维度在后
                info.location = "116.391801,39.913782";
                //把设置好的数据装入容器中
                CloudManager.getInstance().nearbySearch(info);
                break;
            //跳转
            case R.id.jump:
                Intent intent = new Intent(this,Main2Activity.class);
                startActivity(intent);
                break;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //在activity执行onDestroy时执行mMapView.onDestroy()，实现地图生命周期管理
        mMapView.onDestroy();
        //释放云检索资源
        CloudManager.getInstance().destroy();
    }

    @Override
    protected void onResume() {
        super.onResume();
        //在activity执行onResume时执行mMapView. onResume ()，实现地图生命周期管理
        mMapView.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        //在activity执行onPause时执行mMapView. onPause ()，实现地图生命周期管理
        mMapView.onPause();
    }

    @Override
    public void onGetSearchResult(CloudSearchResult result, int i) {
        //处理从服务器得到的结果前,先判断结果对象是否存在,结果里面的表对象是否存在,结果里面的表是否有内容
        if (result != null && result.poiList != null && result.poiList.size() > 0) {
            //画画前,先把黑板擦干净,所以在地图上面定位表示前,先把地图清空
            mBaiduMap.clear();
            //创建定位标识的图片对象,使用Bitmap工厂从资料里面获取图片资源,建立对象.
            BitmapDescriptor bd = BitmapDescriptorFactory.fromResource(R.drawable.icon_gcoding);
            //建立定位点对象
            LatLngBounds.Builder builder = new LatLngBounds.Builder();
            //建立点对象
            LatLng ll;
            //使用高级for循环,把从服务器得到的json结果位置拿出来,在添加图片,成为标识显示在地图上
            for (CloudPoiInfo info : result.poiList) {
                //从表中拿出维度和经度,设置给点对象
                ll = new LatLng(info.latitude, info.longitude);
                //添加坐标图片的对象给MarkerOptions,传图片BitmapDescriptor参数,和点LatLng参数
                OverlayOptions oo = new MarkerOptions().icon(bd).position(ll);
                mBaiduMap.addOverlay(oo);
                builder.include(ll);
            }
        }

    }

    @Override
    public void onGetDetailSearchResult(DetailSearchResult detailSearchResult, int i) {

    }

    @Override
    public void onGetCloudRgcResult(CloudRgcResult cloudRgcResult, int i) {

    }
}
