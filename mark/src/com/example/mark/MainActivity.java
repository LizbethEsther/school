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
 * ʹ�øߏԵ�ͼ�ĵ���Χ�����ܹ�����ͼ
 * ����Χ���൱���ڵ�ͼ�ϸ���һ�����򣨱��磬ĳ����뾶50�����ڣ�
 * ����ǳ�һ�����򣨿��Զ���õ���Χ�����ƣ���
 * ���Լ���һЩ��Ϊ�����磬��������򣩣�ͨ���㲥��֪��������Ϊ������
 */
/**
 * 1�����ӵ���Χ����

2����ʼ��Χ���㲥���գ�

3����ʼ����λ��

4����ȡ��λ��Ϣ��

5�����ٶ�λ���Ƴ�����Χ��
 * @author lijinzhi
 *
 */
/**
 * �������õ�IntentFilter
 * Intent����Ϣ�����壬��������ȥ�����������Ӧ�Ĳ���
 *   Intent����������Ҫ��ͨ��������ע����AndroidManifest.xml�е�
 *   ����IntentFilter�����ж����Intent�������ҵ�ƥ���Intent��
 *   ��������������У�Android��ͨ��Intent��action��type��category
 *   ����������������ƥ���жϵġ�
 *   ������ʾ���á�����IntentFilterʵ����
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
	 * PendingIntent������Ϊ�Ƕ�Intent�İ�װ��
	 * ʵ���Ͼ��ǣ�����ǰApp��֮�������App���ã�
	 * ���������ǹ��ⲿAppʹ�ã��ⲿAppִ����� PendingIntentʱ��
	 * ��ӵص��������Intent,���ⲿApp��ʱִ��PendingIntent��
	 * ������Intent����������Ϊ,PendingIntent��Ҫ���е���Ϣ������
	 * ��װ��Intent�͵�ǰApp Context����ʹ��ǰApp�Ѿ��������ˣ�
	 * Ҳ��ͨ��������PendingIntent��� Context��ִ��Intent��
	 * example:����Ȼ��������һ��B activity�������㲢����������ת��Bactivity
	 * ҳ�棬���뾲��5����֮������ת��Bactivity��
	 * ��ô�����ͨ��PendingIntent��ʵ��
	 * PendingIntent���԰�װ��1���е�intent��
	 * Ȼ��ͨ��AlarmManager�����ʱ����
	 * ����5����֮����PendingIntent��
	 */
	private ListView minputlist;
	//������չ㲥��action�ַ���  
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
		//�������Ű�ť��λ�ã��ߵµ�ͼ���ſؼ�Ĭ����ʾ�ڵ�ͼ���½ǣ���Ҫ����λ�á� 
		 aMap.getUiSettings().setZoomPosition(1);//AMapOptions.ZOOM_POSITION_RIGHT_CENTERΪ�Ҳ��м�λ��
		 //UIsettings.setLogoPosition(AMapOptions)������LOGO��λ��
		 
		 aMap.setLocationSource(this);//���ö�λ����
		 //aMap.getUiSettings().setMyLocationButtonEnabled(true);// �Ƿ���ʾ��λ��ť
		 aMap.setMyLocationEnabled(true);// �Ƿ�ɴ�����λ����ʾ��λ��
	     aMap.getUiSettings().setCompassEnabled(true);  //ָ����ɼ�,�˷��������Ƿ���ʾ��ʹ��ָ����
	     markerDo = new MarkerDo();
	     markerDo.doMarker(aMap, this);
	     aMap.moveCamera(CameraUpdateFactory.zoomTo(15));
	     /**
	      * ����Ȼ��λ�ڴ˴����Կ��ƶ���Ļ����
	      * ����CameraUpdateFactory.zoomTo(15)Ϊ�ı����ż���Ϊһ����ֵ��
	      * ͬʱ����������ͬ�����ԣ��˷�����������һ�� CameraUpdate ����
	      * ʹ�� AMap.moveCamera(CameraUpdate update) ����
	      * ���¿���������ʾ�ڵ�ͼ�ϡ�
	      * �ߵµ�ͼ Android SDK ��������Ļ���л���ʾ�ĵ�ͼ����
	      * ����ͨ���ı���������λ��ʵ�ֵġ�
	      * �ı���������λ�ã���Ҫ��ȷ��Ҫ�ѿ��������ƶ������
	      * ��ʹ�� CameraUpdateFactory ������ͬ���͵� CameraUpdate��
	      */
	     
	     //����Χ��
	    fliter = new IntentFilter(
					ConnectivityManager.CONNECTIVITY_ACTION);
	    //�ù㲥ConnectivityManager.CONNECTIVITY_ACTION��������仯
	    /*
	     * ConnectivityManager��Ϊ��׿�������ӹ����࣬��Ҫ�������£�
         1. ֪ͨӦ������״̬�ĸı䣬���͹㲥 ACTION:CONNECTIVITY_ACTION
         2. WiFi��GPRS����������ӹ���(�Ƿ���ã�����״̬��)
         3. �ṩ��һ��api����Ӧ��ȥ�������ѡ���������������ݵĴ���
	     */
			fliter.addAction(GEOFENCE_BROADCAST_ACTION);
			
	     Intent intent = new Intent(GEOFENCE_BROADCAST_ACTION);
	     mPendingIntent = PendingIntent.getBroadcast(getApplicationContext(), 0,
					intent, 0);
	     /**
	      * PendingIntent.getBroadcast(context, num,intent,PendingIntent.FLAG_UPDATE_CURRENT); 
	      *FLAG_CANCEL_CURRENTΪ0�����PendingIntent��������
	      *FLAG_UPDATE_CURRENT�����֮ǰPendingIntent����Ϣ
	      *�˷����Ƿ�����ϵͳȡ��һ������
	      *��BroadcastReceiver��Intent�㲥��PendingIntent����
	      *��requestCodeֵһ��ʱ������ľͻ��֮ǰ����Ϣ�����ã���ӽ�ȥ��Intent���ǰ��ĸǵ�
	      *��ͬʱ�ж��֪ͨ��ʱ�򣬿�ͨ����ͬrequestCode��������������
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
		
		//���ð�ť͸����
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
			 Toast.makeText(MainActivity.this, "��λ", Toast.LENGTH_SHORT).show();
			 aMap.moveCamera(CameraUpdateFactory.zoomTo(15));
			 aMap.setMyLocationType(AMap.LOCATION_TYPE_LOCATE);
			break;
			
		case R.id.navi:
			if (navi_flag == false)
			{	
				if (markerDo.getMarkPosition() == null)
				{
					Toast.makeText(MainActivity.this, "��ѡ����Ҫȥ�ĵط���", Toast.LENGTH_SHORT).show();
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
					Toast.makeText(MainActivity.this, "��ѡ����Ҫȥ�ĵط���", Toast.LENGTH_SHORT).show();
				}else {
				
			Bundle bundle2=new Bundle();
			double e_lat=markerDo.getMarkPosition().latitude;
			double e_lon=markerDo.getMarkPosition().longitude;
			bundle2.putDouble("lon", location_e);//γ��
			bundle2.putDouble("lat",location_s);//����
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
				Toast.makeText(MainActivity.this, "�Զ���������ģʽ", Toast.LENGTH_SHORT).show();
				soundauto_but.setImageResource(R.drawable.sound);
				registerReceiver(mGeoFenceReceiver, fliter);
				
				soundauto_flag = true;
			}else {
				Toast.makeText(MainActivity.this, "�ر��Զ�����ģʽ", Toast.LENGTH_SHORT).show();
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
		Toast.makeText(this, "����ѡ����ĵ���·���ٽ��з���", Toast.LENGTH_SHORT);
			}else {*/
			
			//����΢��api��ע�ᵽ΢��,ͨ��WXAPIFactory������ȡIWXAPIʵ��
	        Constants.wx_api = WXAPIFactory.createWXAPI(MainActivity.this, Constants.APP_ID, true);
	        Constants.wx_api.registerApp(Constants.APP_ID);//��Ӧ�õ�appIdע�ᵽ΢��
	        //�����¼����
         //   final SendAuth.Req req = new SendAuth.Req();
          //  req.scope = "snsapi_userinfo";
          //  req.state = "wechat_sdk_demo_test";
	        //��������Ȧ����
	        SendMessageToWX.Req req = new SendMessageToWX.Req();  
	        req.transaction = String.valueOf(System.currentTimeMillis());  
            Constants.wx_api.sendReq(req);     //sendReq�ǵ�����app����������Ϣ��΢�ţ�
            //�������֮����лص�������app���档       
            req.scene=SendMessageToWX.Req.WXSceneTimeline;
            //SendMessageToWX.Req��scene��Ա�����scene��WXSceneSession��
            //��ô��Ϣ�ᷢ����΢�ŵĻỰ�ڡ����scene��WXSceneTimeline
            //��΢��4.2����֧�֣������Ҫ���΢�Ű汾֧��API������� 
            //�ɵ���IWXAPI��getWXAppSupportAPI����,0x21020001������֧��
            //��������Ȧ������ô��Ϣ�ᷢ��������Ȧ��sceneĬ��ֵΪWXSceneSession��
            
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
			// ���ö�λ����
			mlocationClient.setLocationListener(this);
			//���ö�λ���,��λ����,Ĭ��Ϊ2000ms
			mLocationOption.setInterval(2000);
			// ����Ϊ�߾��ȶ�λģʽ
			mLocationOption.setLocationMode(AMapLocationMode.Hight_Accuracy);
			// ���ö�λ����
			mlocationClient.setLocationOption(mLocationOption);
			// �˷���Ϊÿ���̶�ʱ��ᷢ��һ�ζ�λ����Ϊ�˼��ٵ������Ļ������������ģ�
			// ע�����ú��ʵĶ�λʱ��ļ������С���֧��Ϊ2000ms���������ں���ʱ�����stopLocation()������ȡ����λ����
			// �ڶ�λ�������ں��ʵ��������ڵ���onDestroy()����
			// �ڵ��ζ�λ����£���λ���۳ɹ���񣬶��������stopLocation()�����Ƴ����󣬶�λsdk�ڲ����Ƴ�
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
				mListener.onLocationChanged(amapLocation);// ��ʾϵͳС����
				location_s = amapLocation.getLatitude();
				location_e = amapLocation.getLongitude();
				city=amapLocation.getCity();
				mlocationClient.addGeoFenceAlert("apollo", 32.10777867, 118.93012404, 1000, -1, mPendingIntent);
			} else {
				String errText = "��λʧ��," + amapLocation.getErrorCode()+ ": " + amapLocation.getErrorInfo();
				Log.e("AmapErr",errText);
				//mLocationErrText.setVisibility(View.VISIBLE);
				//mLocationErrText.setText(errText);
			}
		}
		 
	}
	
	
		BroadcastReceiver mGeoFenceReceiver = new BroadcastReceiver() {
			@Override
			public void onReceive(Context context, Intent intent) {
				// ���չ㲥
				if (intent.getAction().equals(GEOFENCE_BROADCAST_ACTION)) {
					Bundle bundle = intent.getExtras();
					// ���ݹ㲥��event��ȷ�����������ڻ�����������
					int status = bundle.getInt("event");
					String geoFenceId = bundle.getString("fenceid");
					if (status == 1) {
						// ����Χ������
						// �����Զ������ѷ�ʽ,ʾ����ʹ�õ������ַ�ʽ
						Toast.makeText(context, "����Χ������", Toast.LENGTH_SHORT).show();
						
							Intent intent2 = new Intent(context,VideoService.class);
							intent2.putExtra("place", "apollo");
							intent2.putExtra("playing", true);
							context.startService(intent2);
					} else if (status == 2) {
						// �뿪Χ������
						// �����Զ������ѷ�ʽ,ʾ����ʹ�õ������ַ�ʽ
						//Toast.makeText(context, "�뿪Χ������", Toast.LENGTH_SHORT).show();
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
