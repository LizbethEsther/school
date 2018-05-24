package com.example.mark;

import com.amap.api.navi.AMapNavi;
import com.amap.api.navi.AMapNaviListener;
import com.amap.api.navi.AMapNaviView;
import com.amap.api.navi.AMapNaviViewListener;
import com.amap.api.navi.enums.NaviType;
import com.amap.api.navi.model.AMapLaneInfo;
import com.amap.api.navi.model.AMapNaviCross;
import com.amap.api.navi.model.AMapNaviInfo;
import com.amap.api.navi.model.AMapNaviLocation;
import com.amap.api.navi.model.AMapNaviTrafficFacilityInfo;
import com.amap.api.navi.model.AimLessModeCongestionInfo;
import com.amap.api.navi.model.AimLessModeStat;
import com.amap.api.navi.model.NaviInfo;
import com.amap.api.navi.model.NaviLatLng;
import com.autonavi.tbt.TrafficFacilityInfo;

import android.app.Activity;
import android.os.Bundle;

public class Autoguide extends Activity implements AMapNaviListener, AMapNaviViewListener{
	 protected AMapNaviView mAMapNaviView;
	 protected AMapNavi mAMapNavi;
	 private double s_lat;
	 private double s_lon;
	 private double e_lat;
	 private double e_lon;
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_guide);
		
		mAMapNaviView = (AMapNaviView) findViewById(R.id.auto);
	    mAMapNaviView.setAMapNaviViewListener(this);
	  mAMapNavi = AMapNavi.getInstance(getApplicationContext());
	  		//添加监听回调，用于处理算路成功
	  	mAMapNavi.addAMapNaviListener(this);
	    mAMapNaviView.onCreate(savedInstanceState);
	    Bundle bundle = this.getIntent().getExtras();
        s_lat= bundle.getDouble("lat");
        s_lon=bundle.getDouble("lon");
        e_lat=bundle.getDouble("e_lat");
        e_lon=bundle.getDouble("e_lon");
	}
	protected void onResume() {
	    super.onResume();
	    mAMapNaviView.onResume();
	}
	 
	@Override
	protected void onPause() {
	    super.onPause();
	    mAMapNaviView.onPause();
	}
	 
	@Override
	protected void onDestroy() {
	    super.onDestroy();
	    mAMapNaviView.onDestroy();
	}
	@Override
	public void onLockMap(boolean arg0) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public boolean onNaviBackClick() {
		// TODO Auto-generated method stub
		return false;
	}
	@Override
	public void onNaviCancel() {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void onNaviMapMode(int arg0) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void onNaviSetting() {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void onNaviTurnClick() {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void onNaviViewLoaded() {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void onNextRoadClick() {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void onScanViewButtonClick() {
		// TODO Auto-generated method stub
		
	}
	@Override
	@Deprecated
	public void OnUpdateTrafficFacility(AMapNaviTrafficFacilityInfo arg0) {
		// TODO Auto-generated method stub
		
	}
	@Override
	@Deprecated
	public void OnUpdateTrafficFacility(TrafficFacilityInfo arg0) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void OnUpdateTrafficFacility(AMapNaviTrafficFacilityInfo[] arg0) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void hideCross() {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void hideLaneInfo() {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void notifyParallelRoad(int arg0) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void onArriveDestination() {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void onArrivedWayPoint(int arg0) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void onCalculateMultipleRoutesSuccess(int[] arg0) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void onCalculateRouteFailure(int arg0) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void onCalculateRouteSuccess() {
		    mAMapNavi.startNavi(NaviType.GPS);
	}
	@Override
	public void onEndEmulatorNavi() {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void onGetNavigationText(int arg0, String arg1) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void onGpsOpenStatus(boolean arg0) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void onInitNaviFailure() {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void onInitNaviSuccess() {
	 mAMapNavi.calculateWalkRoute(new NaviLatLng(s_lat, s_lon), new NaviLatLng(e_lat, e_lon));
		 
	}
	@Override
	public void onLocationChange(AMapNaviLocation arg0) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void onNaviInfoUpdate(NaviInfo arg0) {
		// TODO Auto-generated method stub
		
	}
	@Override
	@Deprecated
	public void onNaviInfoUpdated(AMapNaviInfo arg0) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void onReCalculateRouteForTrafficJam() {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void onReCalculateRouteForYaw() {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void onStartNavi(int arg0) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void onTrafficStatusUpdate() {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void showCross(AMapNaviCross arg0) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void showLaneInfo(AMapLaneInfo[] arg0, byte[] arg1, byte[] arg2) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void updateAimlessModeCongestionInfo(AimLessModeCongestionInfo arg0) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void updateAimlessModeStatistics(AimLessModeStat arg0) {
		// TODO Auto-generated method stub
		
	}

}
