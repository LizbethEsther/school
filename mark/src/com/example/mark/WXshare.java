package com.example.mark;

import android.content.Context;

import com.amap.api.maps.AMap;
import com.amap.api.maps.model.LatLng;
import com.amap.api.services.core.LatLonPoint;

public class WXshare {
	private LatLonPoint START ;
	private LatLonPoint END ;
	private Context context;
	private AMap mAMap;
	public void setPosition(Context context,AMap aMap ,LatLng endlatLng , double location_s ,double location_e)
	{
		START = new LatLonPoint(location_s, location_e);
		END = new LatLonPoint(endlatLng.latitude, endlatLng.longitude);
		this.context = context;
		this.mAMap = aMap;
	}
	 
      

}
