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
 * �� RouteSearch.OnRouteSearchListener �ӿڻص�����
 *  void onWalkRouteSearched(WalkRouteResult walkRouteResult,int rCode) 
 *  �����й滮·����������ص���Ϣ�������Ի��·�εľ��롢
 *  ���е�Ԥ��ʱ�䡢����·�ε�����㡢����·�εĵ�·���ơ�
 *  ������Ҫ��������Ϣ
 * @author lijinzhi
 *
 */
public class FindRoad implements OnRouteSearchListener
{
	private LatLonPoint mStartPoint ;//��㣬
	private LatLonPoint mEndPoint;//�յ㣬
	private RouteSearch mRouteSearch;
	private WalkRouteResult mWalkRouteResult;//ͨ�� WalkRouteQuery ������������
	
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
				Toast.makeText(context, "��ѡ����Ҫȥ�ľ���", Toast.LENGTH_SHORT).show();
			}
			final RouteSearch.FromAndTo fromAndTo = new RouteSearch.FromAndTo(
					mStartPoint, mEndPoint);
			/*��ʼ��query����fromAndTo�ǰ������յ���Ϣ��walkMode�ǲ���·���滮��ģʽ
			������ģʽ��RouteSearch.WALK_DEFAULT �� RouteSearch.WALK_MULTI_PATH��
			*/WalkRouteQuery query = new WalkRouteQuery(fromAndTo, mode);
			mRouteSearch.calculateWalkRouteAsyn(query);// �첽·���滮����ģʽ��ѯ
	/**
	 * ʹ���� RouteSearch �� calculateWalkRouteAsyn(WalkRouteQuery query) 
	 * �������в��й滮·�����㡣
	 */
	}

	@Override
	public void onWalkRouteSearched(WalkRouteResult result, int errorCode)
	{
	/**
	 * 1�������ڻص��н���result����ȡ���е�·����
       2��result.getPaths()���Ի�ȡ�� WalkPath �б���
      3�����ؽ���ɹ�����ʧ�ܵ���Ӧ�롣1000Ϊ�ɹ�������Ϊʧ��
	 */
	//	dissmissProgressDialog();
		aMap.clear();// �����ͼ�ϵ����и�����
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
					Toast.makeText(context, "û�в�ѯ�����", Toast.LENGTH_SHORT).show();
				}

			} else {
				Toast.makeText(context, "û�в�ѯ�����", Toast.LENGTH_SHORT).show();
			}
		} else {
			Toast.makeText(context, "��ѯ����", Toast.LENGTH_SHORT).show();
		}
	}
	
	
	

	@Override
	public void onBusRouteSearched(BusRouteResult arg0, int arg1)
	{
		// ������·��
		
	}

	@Override
	public void onDriveRouteSearched(DriveRouteResult result, int errorCode)
	{
	//��������·��	
	}
}
