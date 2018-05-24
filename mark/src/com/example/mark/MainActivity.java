package com.example.mark;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationClientOption.AMapLocationMode;
import com.amap.api.location.AMapLocationListener;
import com.amap.api.maps.AMap;
import com.amap.api.maps.CameraUpdateFactory;
import com.amap.api.maps.LocationSource;
import com.amap.api.maps.MapView;
import com.amap.api.maps.UiSettings;
import com.amap.api.services.help.Inputtips;
import com.amap.api.services.help.Inputtips.InputtipsListener;
import com.amap.api.services.help.InputtipsQuery;
import com.amap.api.services.help.Tip;
import com.tencent.mm.opensdk.modelmsg.SendMessageToWX;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;

import android.app.Activity;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

/*
 * 使用高徳地图的地理围栏功能构建地图
 * 地理围栏相当于在地图上根据一个规则（比如，某坐标半径50米以内）
 * 来标记出一个区域（可自定义该地理围栏名称），
 * 可以监听一些行为（比如，进入该区域），通过广播告知监听的行为发生。
 */
/**
 * 1、增加地理围栏；

2、初始化围栏广播接收；

3、初始化定位；

4、获取定位信息；

5、销毁定位和移除地理围栏
 * @author lijinzhi
 *
 */
/**
 * 关于所用的IntentFilter
 * Intent是信息的载体，用它可以去请求组件做相应的操作
 *   Intent解析机制主要是通过查找已注册在AndroidManifest.xml中的
 *   所有IntentFilter及其中定义的Intent，最终找到匹配的Intent。
 *   在这个解析过程中，Android是通过Intent的action、type、category
 *   这三个属性来进行匹配判断的。
 *   还有显示引用。即将IntentFilter实例化
 *   
 * @author lijinzhi
 *
 */
public class MainActivity extends Activity implements OnClickListener,
		LocationSource, AMapLocationListener, TextWatcher, InputtipsListener
{

	private MapView mapView;
	private AMap aMap;

	private Button normal_but,sate_but,night_but,place_but,circle_but,line_but;
	private ImageButton share_but,road_but,traffic_but,search_but,navi_but,location_but,soundauto_but,weather_but,guide_but;
	private EditText editText;

	boolean traffic_flag = true;
	boolean navi_flag = false;
	boolean olddriver_flag = false;
	boolean soundauto_flag = false;
    String city;
	UiSettings mUiSettings;
	MarkerDo markerDo;
	double location_s;
	double location_e;
	FindRoad findRoad = new FindRoad();
	OldDriver oldDriver = new OldDriver();
	boolean flag_auto = false;
	  IntentFilter fliter ;
	private PendingIntent mPendingIntent = null;
	/**
	 * PendingIntent可以认为是对Intent的包装，
	 * 实际上就是，供当前App或之外的其他App调用，
	 * 而常见的是供外部App使用，外部App执行这个 PendingIntent时，
	 * 间接地调用里面的Intent,即外部App延时执行PendingIntent中
	 * 描述的Intent及其最终行为,PendingIntent主要持有的信息是它所
	 * 包装的Intent和当前App Context，即使当前App已经不存在了，
	 * 也能通过存在于PendingIntent里的 Context来执行Intent。
	 * example:你虽然想启动另一个B activity，可是你并不想马上跳转到Bactivity
	 * 页面，你想静等5分钟之后再跳转到Bactivity，
	 * 那么你可以通过PendingIntent来实现
	 * PendingIntent可以包装第1步中的intent，
	 * 然后通过AlarmManager这个定时器，
	 * 定制5分钟之后启PendingIntent，
	 */
	private ListView minputlist;
	//定义接收广播的action字符串  
	public static final String GEOFENCE_BROADCAST_ACTION = "com.location.apis.geofencedemo.broadcast";
	private OnLocationChangedListener mListener;
	private AMapLocationClient mlocationClient;
	private AMapLocationClientOption mLocationOption;
	
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);  
		mapView = (MapView) findViewById(R.id.map);
		mapView.onCreate(savedInstanceState);
		initMap();
		initUI();
	    
		//Log.i("tag", "ZoomPosition = " + aMap.getUiSettings().getZoomPosition());
	}

	public void initMap()
	{
		// TODO Auto-generated method stub
		
		aMap = mapView.getMap();
		aMap.setLocationSource(this);
		//设置缩放按钮的位置，高德地图缩放控件默认显示在地图右下角，需要调整位置。 
		 aMap.getUiSettings().setZoomPosition(1);//AMapOptions.ZOOM_POSITION_RIGHT_CENTER为右侧中间位置
		 //UIsettings.setLogoPosition(AMapOptions)来设置LOGO的位置
		 
		 aMap.setLocationSource(this);//设置定位监听
		 //aMap.getUiSettings().setMyLocationButtonEnabled(true);// 是否显示定位按钮
		 aMap.setMyLocationEnabled(true);// 是否可触发定位并显示定位层
	     aMap.getUiSettings().setCompassEnabled(true);  //指南针可见,此方法设置是否显示和使用指南针
	     markerDo = new MarkerDo();
	     markerDo.doMarker(aMap, this);
	     aMap.moveCamera(CameraUpdateFactory.zoomTo(15));
	     /**
	      * 即虽然定位在此处但仍可移动屏幕内容
	      * 并且CameraUpdateFactory.zoomTo(15)为改变缩放级别为一个定值，
	      * 同时保持其他相同的属性，此方法方法返回一个 CameraUpdate 对象，
	      * 使用 AMap.moveCamera(CameraUpdate update) 方法
	      * 更新可视区域显示在地图上。
	      * 高德地图 Android SDK 允许在屏幕上切换显示的地图区域。
	      * 这是通过改变可视区域的位置实现的。
	      * 改变可视区域的位置，需要明确想要把可视区域移动到哪里。
	      * 可使用 CameraUpdateFactory 创建不同类型的 CameraUpdate。
	      */
	     
	     //地理围栏
	    fliter = new IntentFilter(
					ConnectivityManager.CONNECTIVITY_ACTION);
	    //用广播ConnectivityManager.CONNECTIVITY_ACTION监听网络变化
	    /*
	     * ConnectivityManager作为安卓网络连接管理类，主要功能如下：
         1. 通知应用网络状态的改变，发送广播 ACTION:CONNECTIVITY_ACTION
         2. WiFi，GPRS等网络的连接管理(是否可用，连接状态等)
         3. 提供了一种api来让应用去请求或是选择网络来进行数据的传输
	     */
			fliter.addAction(GEOFENCE_BROADCAST_ACTION);
			
	     Intent intent = new Intent(GEOFENCE_BROADCAST_ACTION);
	     mPendingIntent = PendingIntent.getBroadcast(getApplicationContext(), 0,
					intent, 0);
	     /**
	      * PendingIntent.getBroadcast(context, num,intent,PendingIntent.FLAG_UPDATE_CURRENT); 
	      *FLAG_CANCEL_CURRENT为0代表该PendingIntent不带数据
	      *FLAG_UPDATE_CURRENT会更新之前PendingIntent的消息
	      *此方法是方法从系统取得一个用于
	      *向BroadcastReceiver的Intent广播的PendingIntent对象
	      *当requestCode值一样时，后面的就会对之前的消息起作用，后加进去的Intent会把前面的盖掉
	      *当同时有多个通知的时候，可通过不同requestCode参数来互相区别，
	      */
	     
	    
	}

	private void initUI()
	{
		
		// TODO Auto-generated method stub
		normal_but = (Button) findViewById(R.id.normal);
		sate_but = (Button) findViewById(R.id.sate);
		night_but = (Button) findViewById(R.id.night);
		share_but = (ImageButton) findViewById(R.id.share);
		road_but = (ImageButton) findViewById(R.id.road);
		traffic_but = (ImageButton) findViewById(R.id.traffic);
		search_but = (ImageButton) findViewById(R.id.search);
		weather_but=(ImageButton)findViewById(R.id.weather);
		guide_but=(ImageButton)findViewById(R.id.guide);
		navi_but = (ImageButton) findViewById(R.id.navi);
		location_but = (ImageButton) findViewById(R.id.location);
		soundauto_but = (ImageButton) findViewById(R.id.sound);
		editText = (EditText) findViewById(R.id.edittext);
		minputlist = (ListView)findViewById(R.id.inputlist);
		
		
		editText.addTextChangedListener(this);
		normal_but.setBackgroundColor(getResources().getColor(R.color.normal));
		sate_but.setBackgroundColor(getResources().getColor(R.color.selt));
		night_but.setBackgroundColor(getResources().getColor(R.color.night));
		
		//设置按钮透明度
		normal_but.getBackground().setAlpha(100);
		sate_but.getBackground().setAlpha(100);
		night_but.getBackground().setAlpha(100);
		share_but.getBackground().setAlpha(150);
		road_but.getBackground().setAlpha(150);
		navi_but.getBackground().setAlpha(150);		
		traffic_but.getBackground().setAlpha(150);
		search_but.getBackground().setAlpha(150);
		weather_but.getBackground().setAlpha(150);
		location_but.getBackground().setAlpha(150);
		soundauto_but.getBackground().setAlpha(150);
		
		

		normal_but.setOnClickListener(this);
		sate_but.setOnClickListener(this);
		night_but.setOnClickListener(this);
		traffic_but.setOnClickListener(this);
		navi_but.setOnClickListener(this);
		search_but.setOnClickListener(this);
		location_but.setOnClickListener(this);
		weather_but.setOnClickListener(this);
		guide_but.setOnClickListener(this);
		road_but.setOnClickListener(this);
		share_but.setOnClickListener(this);
		soundauto_but.setOnClickListener(this);
		
	
	}

	@Override
	protected void onResume()
	{
		// TODO Auto-generated method stub
		super.onResume();
		mapView.onResume();
	}

	@Override
	protected void onPause()
	{
		// TODO Auto-generated method stub
		super.onPause();
		mapView.onPause();
	}

	@Override
	protected void onDestroy()
	{
		// TODO Auto-generated method stub
		super.onDestroy();
		mapView.onDestroy();
	}

	@Override
	public void onClick(View v)
	{
		// TODO Auto-generated method stub
		switch (v.getId())
		{
		case R.id.normal:
			aMap.setMapType(AMap.MAP_TYPE_NORMAL);
			break;
		case R.id.sate:
			aMap.setMapType(AMap.MAP_TYPE_SATELLITE);
			break;
		case R.id.night:
			aMap.setMapType(AMap.MAP_TYPE_NIGHT);
			break;
		case R.id.traffic:
			if (traffic_flag == false)
			{
				aMap.setTrafficEnabled(false);
				traffic_flag = true;
			} else
			{
				aMap.setTrafficEnabled(true);
				traffic_flag = false;
			}
			break;
			
		case R.id.location:
			 Toast.makeText(MainActivity.this, "定位", Toast.LENGTH_SHORT).show();
			 aMap.moveCamera(CameraUpdateFactory.zoomTo(15));
			 aMap.setMyLocationType(AMap.LOCATION_TYPE_LOCATE);
			break;
			
		case R.id.navi:
			if (navi_flag == false)
			{	
				if (markerDo.getMarkPosition() == null)
				{
					Toast.makeText(MainActivity.this, "请选择你要去的地方！", Toast.LENGTH_SHORT).show();
				}else {
					findRoad.setPosition(markerDo.getMarkPosition(),location_s,location_e);
					findRoad.doFindRoad(this, aMap);
					aMap.setMyLocationType(AMap.LOCATION_TYPE_MAP_ROTATE);
					navi_flag = true;
				}
				
				
			}else {
				aMap.setMyLocationType(AMap.LOCATION_TYPE_LOCATE);
				aMap.clear();
				initMap();
				navi_flag = false;
			}
			break;
		case R.id.search:
			Search search = new Search();
			search.doSearchQuery(editText,this,aMap);
			break;
		case R.id.weather:
			Bundle bundle1=new Bundle();
			bundle1.putString("cityname",city);
			Intent intent1=new Intent(this,Weather.class);
			intent1.putExtras(bundle1);
			startActivity(intent1);
			break;
		case R.id.guide:
			if (navi_flag == false)
			{	
				if (markerDo.getMarkPosition() == null)
				{
					Toast.makeText(MainActivity.this, "请选择你要去的地方！", Toast.LENGTH_SHORT).show();
				}else {
				
			Bundle bundle2=new Bundle();
			double e_lat=markerDo.getMarkPosition().latitude;
			double e_lon=markerDo.getMarkPosition().longitude;
			bundle2.putDouble("lon", location_e);//纬度
			bundle2.putDouble("lat",location_s);//经度
			bundle2.putDouble("e_lat",e_lat);
			bundle2.putDouble("e_lon",e_lon);
			Intent intent2=new Intent(this,Autoguide.class);
			intent2.putExtras(bundle2);
			startActivity(intent2);
				}
				}else {
					aMap.setMyLocationType(AMap.LOCATION_TYPE_LOCATE);
					aMap.clear();
					initMap();
					navi_flag = false;
				}
				break;
		case R.id.road:
			if (navi_flag == false)
			{
				oldDriver.doTripRoad(this, aMap);
				navi_flag = true;
			}else {
				aMap.clear();
				initMap();
				navi_flag = false;
			}
			
			break;
		/*case R.id.sound:
			if (soundauto_flag == false)
			{
				Toast.makeText(MainActivity.this, "自动语音导游模式", Toast.LENGTH_SHORT).show();
				soundauto_but.setImageResource(R.drawable.sound);
				registerReceiver(mGeoFenceReceiver, fliter);
				
				soundauto_flag = true;
			}else {
				Toast.makeText(MainActivity.this, "关闭自动导游模式", Toast.LENGTH_SHORT).show();
				soundauto_but.setImageResource(R.drawable.nosound);
				unregisterReceiver(mGeoFenceReceiver);
				soundauto_flag = false;
				Intent intent2 = new Intent(this,VideoService.class);
				 intent2.putExtra("playing", true);
				 this.stopService(intent2);
			}
			break;
		*/
		case R.id.share:
			
			/*if (markerDo.getMarkPosition() == null)
			{
		Toast.makeText(this, "请先选择你的导航路线再进行分享！", Toast.LENGTH_SHORT);
			}else {*/
			
			//创建微信api并注册到微信,通过WXAPIFactory工厂获取IWXAPI实例
	        Constants.wx_api = WXAPIFactory.createWXAPI(MainActivity.this, Constants.APP_ID, true);
	        Constants.wx_api.registerApp(Constants.APP_ID);//将应用的appId注册到微信
	        //发起登录请求
         //   final SendAuth.Req req = new SendAuth.Req();
          //  req.scope = "snsapi_userinfo";
          //  req.state = "wechat_sdk_demo_test";
	        //分享朋友圈请求
	        SendMessageToWX.Req req = new SendMessageToWX.Req();  
	        req.transaction = String.valueOf(System.currentTimeMillis());  
            Constants.wx_api.sendReq(req);     //sendReq是第三方app主动发送消息给微信，
            //发送完成之后会切回到第三方app界面。       
            req.scene=SendMessageToWX.Req.WXSceneTimeline;
            //SendMessageToWX.Req的scene成员，如果scene填WXSceneSession，
            //那么消息会发送至微信的会话内。如果scene填WXSceneTimeline
            //（微信4.2以上支持，如果需要检查微信版本支持API的情况， 
            //可调用IWXAPI的getWXAppSupportAPI方法,0x21020001及以上支持
            //发送朋友圈），那么消息会发送至朋友圈。scene默认值为WXSceneSession。
            
				/*Share share = new Share();
				share.setPosition(this, aMap, markerDo.getMarkPosition(), location_s, location_e);
				share.shareNavi();
				share.showShare();
				}*/
			
			
			break;

	}
	}

	@Override
	public void activate(OnLocationChangedListener arg0)
	{
		// TODO Auto-generated method stub
		mListener = arg0;
		if (mlocationClient == null)
		{
			mlocationClient = new AMapLocationClient(this);
			mLocationOption = new AMapLocationClientOption();
			// 设置定位监听
			mlocationClient.setLocationListener(this);
			//设置定位间隔,单位毫秒,默认为2000ms
			mLocationOption.setInterval(2000);
			// 设置为高精度定位模式
			mLocationOption.setLocationMode(AMapLocationMode.Hight_Accuracy);
			// 设置定位参数
			mlocationClient.setLocationOption(mLocationOption);
			// 此方法为每隔固定时间会发起一次定位请求，为了减少电量消耗或网络流量消耗，
			// 注意设置合适的定位时间的间隔（最小间隔支持为2000ms），并且在合适时间调用stopLocation()方法来取消定位请求
			// 在定位结束后，在合适的生命周期调用onDestroy()方法
			// 在单次定位情况下，定位无论成功与否，都无需调用stopLocation()方法移除请求，定位sdk内部会移除
		mlocationClient.startLocation();
		}
		
	}
	
	

	@Override
	public void deactivate()
	{
		// TODO Auto-generated method stub 
		mListener = null;
		if (mlocationClient != null)
		{
			mlocationClient.stopLocation();
			mlocationClient.onDestroy();
		}
		mlocationClient = null;
	}

	@Override
	public void onLocationChanged(AMapLocation amapLocation)
	{
		// TODO Auto-generated method stub
		if (mListener != null && amapLocation != null) {
			if (amapLocation != null
					&& amapLocation.getErrorCode() == 0) {
				//mLocationErrText.setVisibility(View.GONE);
				mListener.onLocationChanged(amapLocation);// 显示系统小蓝点
				location_s = amapLocation.getLatitude();
				location_e = amapLocation.getLongitude();
				city=amapLocation.getCity();
				mlocationClient.addGeoFenceAlert("apollo", 32.10777867, 118.93012404, 1000, -1, mPendingIntent);
			} else {
				String errText = "定位失败," + amapLocation.getErrorCode()+ ": " + amapLocation.getErrorInfo();
				Log.e("AmapErr",errText);
				//mLocationErrText.setVisibility(View.VISIBLE);
				//mLocationErrText.setText(errText);
			}
		}
		 
	}
	
	
		BroadcastReceiver mGeoFenceReceiver = new BroadcastReceiver() {
			@Override
			public void onReceive(Context context, Intent intent) {
				// 接收广播
				if (intent.getAction().equals(GEOFENCE_BROADCAST_ACTION)) {
					Bundle bundle = intent.getExtras();
					// 根据广播的event来确定是在区域内还是在区域外
					int status = bundle.getInt("event");
					String geoFenceId = bundle.getString("fenceid");
					if (status == 1) {
						// 进入围栏区域
						// 可以自定义提醒方式,示例中使用的是文字方式
						Toast.makeText(context, "进入围栏区域", Toast.LENGTH_SHORT).show();
						
							Intent intent2 = new Intent(context,VideoService.class);
							intent2.putExtra("place", "apollo");
							intent2.putExtra("playing", true);
							context.startService(intent2);
					} else if (status == 2) {
						// 离开围栏区域
						// 可以自定义提醒方式,示例中使用的是文字方式
						//Toast.makeText(context, "离开围栏区域", Toast.LENGTH_SHORT).show();
						Intent intent2 = new Intent(context,VideoService.class);
						 intent2.putExtra("playing", true);
						 context.stopService(intent2);
					}
				}
			}

			
		};

		@Override
		public void onGetInputtips(final List<Tip> tipList, int rCode) {
			// TODO Auto-generated method stub
			if (rCode == 1000) {
	            List<HashMap<String, String>> listString = new ArrayList<HashMap<String, String>>();
	            if(tipList != null) {
	            	int size = tipList.size();
					for (int i = 0; i < size; i++) {
						Tip tip = tipList.get(i);
						if(tip != null) {
							HashMap<String, String> map = new HashMap<String, String>();
							map.put("name", tipList.get(i).getName());
							map.put("address", tipList.get(i).getDistrict());
							listString.add(map);
						}
					}
					SimpleAdapter aAdapter = new SimpleAdapter(getApplicationContext(), listString, R.layout.item,
							new String[]{"name", "address"}, new int[]{R.id.poi_field_id, R.id.poi_value_id});

					minputlist.setAdapter(aAdapter);
					aAdapter.notifyDataSetChanged();
				}

	        } else {
	        	Toast.makeText(this.getApplicationContext(), rCode, Toast.LENGTH_SHORT).show();
			}
			
		}

		@Override
		public void afterTextChanged(Editable arg0) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void beforeTextChanged(CharSequence arg0, int arg1, int arg2,
				int arg3) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void onTextChanged(CharSequence s, int start, int before, int count) {
			// TODO Auto-generated method stub
			String newText = s.toString().trim();
	        InputtipsQuery inputquery = new InputtipsQuery(newText, city);
	        inputquery.setCityLimit(true);
	        Inputtips inputTips = new Inputtips(MainActivity.this, inputquery);
	        inputTips.setInputtipsListener(this);
	        inputTips.requestInputtipsAsyn();
		}

	
	
		
	
	
}
