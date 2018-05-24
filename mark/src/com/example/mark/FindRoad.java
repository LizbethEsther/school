package com.example.mark;

import java.util.ArrayList;
import java.util.List;

import javax.security.auth.PrivateCredentialPermission;

import android.content.Context;
import android.view.View;
import android.widget.Toast;

import com.amap.api.maps.AMap;
import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.overlay.WalkRouteOverlay;
import com.amap.api.maps.overlay.DrivingRouteOverlay;
import com.amap.api.services.core.LatLonPoint;
import com.amap.api.services.route.BusRouteResult;
import com.amap.api.services.route.DrivePath;
import com.amap.api.services.route.DriveRouteResult;
import com.amap.api.services.route.RouteSearch;
import com.amap.api.services.route.RouteSearch.DriveRouteQuery;
import com.amap.api.services.route.RouteSearch.OnRouteSearchListener;
import com.amap.api.services.route.RouteSearch.WalkRouteQuery;
import com.amap.api.services.route.WalkPath;
import com.amap.api.services.route.WalkRouteResult;
import com.autonavi.aps.amapapi.model.AmapLoc;


/**
 * 在 RouteSearch.OnRouteSearchListener 接口回调方法
 *  void onWalkRouteSearched(WalkRouteResult walkRouteResult,int rCode) 
 *  处理步行规划路径结果。返回的信息中您可以获得路段的距离、
 *  步行的预计时间、步行路段的坐标点、步行路段的道路名称、
 *  导航主要操作等信息
 * @author lijinzhi
 *
 */
public class FindRoad implements OnRouteSearchListener
{
	private LatLonPoint mStartPoint ;//起点，
	private LatLonPoint mEndPoint;//终点，
	private RouteSearch mRouteSearch;
	private WalkRouteResult mWalkRouteResult;//通过 WalkRouteQuery 设置搜索条件
	
	Context context;
	AMap aMap;
	
	public void setPosition(LatLng endlatLng , double location_s ,double location_e)
	{
		mStartPoint = new LatLonPoint(location_s, location_e);
		mEndPoint = new LatLonPoint(endlatLng.latitude, endlatLng.longitude);
	}
	
	public void doFindRoad(Context context ,AMap aMap)
	{
		this.context = context;
		this.aMap = aMap;
		mRouteSearch = new RouteSearch(context);
		mRouteSearch.setRouteSearchListener(this);
		searchRouteResult(3, RouteSearch.WalkDefault);
	}
	

	
	public void searchRouteResult(int routeType, int mode)
	{	
		
	
			if (mEndPoint == null) {
				Toast.makeText(context, "请选择你要去的景点", Toast.LENGTH_SHORT).show();
			}
			final RouteSearch.FromAndTo fromAndTo = new RouteSearch.FromAndTo(
					mStartPoint, mEndPoint);
			/*初始化query对象，fromAndTo是包含起终点信息，walkMode是步行路径规划的模式
			有两种模式：RouteSearch.WALK_DEFAULT 和 RouteSearch.WALK_MULTI_PATH。
			*/WalkRouteQuery query = new WalkRouteQuery(fromAndTo, mode);
			mRouteSearch.calculateWalkRouteAsyn(query);// 异步路径规划步行模式查询
	/**
	 * 使用类 RouteSearch 的 calculateWalkRouteAsyn(WalkRouteQuery query) 
	 * 方法进行步行规划路径计算。
	 */
	}

	@Override
	public void onWalkRouteSearched(WalkRouteResult result, int errorCode)
	{
	/**
	 * 1）可以在回调中解析result，获取骑行的路径。
       2）result.getPaths()可以获取到 WalkPath 列表，。
      3）返回结果成功或者失败的响应码。1000为成功，其他为失败
	 */
	//	dissmissProgressDialog();
		aMap.clear();// 清理地图上的所有覆盖物
		if (errorCode == 1000) {
			if (result != null && result.getPaths() != null) {
				if (result.getPaths().size() > 0) {
					mWalkRouteResult = result;
					final WalkPath walkPath = mWalkRouteResult.getPaths()
							.get(0);
					
					WalkRouteOverlay walkRouteOverlay = new WalkRouteOverlay(
							context, aMap, walkPath,
							mWalkRouteResult.getStartPos(),
							mWalkRouteResult.getTargetPos());
					walkRouteOverlay.removeFromMap();
					walkRouteOverlay.addToMap();
					walkRouteOverlay.zoomToSpan();
			
				} else if (result != null && result.getPaths() == null) {
					Toast.makeText(context, "没有查询到结果", Toast.LENGTH_SHORT).show();
				}

			} else {
				Toast.makeText(context, "没有查询到结果", Toast.LENGTH_SHORT).show();
			}
		} else {
			Toast.makeText(context, "查询错误", Toast.LENGTH_SHORT).show();
		}
	}
	
	
	

	@Override
	public void onBusRouteSearched(BusRouteResult arg0, int arg1)
	{
		// 公交车路线
		
	}

	@Override
	public void onDriveRouteSearched(DriveRouteResult result, int errorCode)
	{
	//汽车出行路线	
	}
}
