package com.example.mark;

import android.graphics.Color;

import com.amap.api.maps.AMap;
import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.Polyline;
import com.amap.api.maps.model.PolylineOptions;

//单纯的把推荐的几个景点连接起来，形成一条路径规划
public class Line
{
	Polyline polyline;
	public void showline(AMap aMap)
	{
		polyline = aMap.addPolyline((new PolylineOptions())
				.add(new LatLng(27.901998,112.9219),
					 new LatLng(27.5452,112.5422),
					 new LatLng(27.6005,112.6209),
					 new LatLng(27.6911,112.6544),
					 new LatLng(27.7015,112.7346),
					 new LatLng(27.8327,112.85345))
				.width(10).geodesic(true).color(Color.BLACK));
	}
	
	
}
