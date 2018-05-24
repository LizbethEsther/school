package com.example.mark;

import java.util.List;

import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.widget.EditText;
import android.widget.Toast;

import com.amap.api.maps.AMap;
import com.amap.api.maps.overlay.PoiOverlay;
import com.amap.api.services.core.PoiItem;
import com.amap.api.services.core.SuggestionCity;
import com.amap.api.services.poisearch.PoiResult;
import com.amap.api.services.poisearch.PoiSearch;
import com.amap.api.services.poisearch.PoiSearch.OnPoiSearchListener;
import com.amap.api.services.poisearch.PoiSearch.Query;

/**
 * POI指point of interest,兴趣点
 * 在地图表达中，一个 POI 可代表一栋大厦、一家商铺、一处景点等等。
 * 通过POI搜索，完成找餐馆、找景点、找厕所等等的功能。
 * @author lijinzhi
 *搜索方法。执行搜索方法，里面调用的数据时高德内置的搜索库。
 */
//实现关键字检索的步骤如下：
//1、继承 OnPoiSearchListener 监听。
//2、构造 PoiSearch.Query 对象，
//通过 PoiSearch.Query(String query, String ctgr, String city) 设置搜索条件
public class Search implements  OnPoiSearchListener, TextWatcher
{
	String keyWord ;
	private PoiResult poiResult;
	Query query;
	AMap aMap;
	Context context;

	protected void doSearchQuery(EditText editText,Context context ,AMap aMap) {
		this.aMap = aMap;
		this.context = context;
		Log.i("tag", "执行搜索方法、editText ==" + editText.getText().toString());
		keyWord = editText.getText().toString().trim();
		int currentPage = 0;
		query = new PoiSearch.Query(keyWord, "", "湘潭市");
		/*
		 * keyWord表示搜索字符串
		 * 第二个参数表示POI搜索类型，二者选填其一，
		 * 选用POI搜索类型时建议填写类型代码，码表可以参考下方（而非文字）
            cityCode（湘潭市）表示POI搜索区域，可以是城市编码也可以
            是城市名称，也可以传空字符串，
            空字符串代表全国在全国范围内进行搜索
		 */
		
		query.setPageSize(20);// 设置每页最多返回多少条poiitem
		query.setPageNum(currentPage);// 设置查第一页,从0开始
		PoiSearch poiSearch = new PoiSearch(context, query);//构造 PoiSearch 对象并设置监听
		poiSearch.setOnPoiSearchListener(this);
		poiSearch.searchPOIAsyn();// 异步搜索, searchPOIAsyn() 方法发送请求
		
	}



	@Override
	public void onPoiItemSearched(PoiItem arg0, int arg1)
	{
		// TODO Auto-generated method stub
		
	}



	/*通过回调接口 onPoiSearched 解析返回的结果，
	 * 将查询到的 POI 以绘制点的方式显示在地图上
	 * 
	 */
	/**
	 * 1）可以在回调中解析result，获取POI信息。
        2）result.getPois()可以获取到PoiItem列表，Poi详细信息可参考PoiItem类。
        3）若当前城市查询不到所需POI信息，
        可以通过result.getSearchSuggestionCitys()获取当前Poi搜索的建议城市。
        4）如果搜索关键字明显为误输入，
        则可通过result.getSearchSuggestionKeywords()方法得到搜索关键词建议。
        5）返回结果成功或者失败的响应码。1000为成功，其他为失败
	 */
	public void onPoiSearched(PoiResult result, int rCode)
	{
		// TODO Auto-generated method stub
		if (rCode == 1000)
		{
			if (result != null && result.getPois() != null)
			{
				if (result.getQuery().equals(query))
			{
				poiResult = result;
				List<PoiItem> poiItems = poiResult.getPois();
				List<SuggestionCity> suggestionCities = poiResult.getSearchSuggestionCitys();
				
				if (poiItems != null && poiItems.size() > 0) {
					aMap.clear();// 清理之前的图标
					PoiOverlay poiOverlay = new PoiOverlay(aMap, poiItems);
					poiOverlay.removeFromMap();
					poiOverlay.addToMap();
					poiOverlay.zoomToSpan();
				} else if (suggestionCities != null
						&& suggestionCities.size() > 0) {
					showSuggestCity(suggestionCities);
				} else {
					Toast.makeText(context, "没有搜索到相关地点！", Toast.LENGTH_SHORT).show();
				}
			}
		} else {
			Toast.makeText(context, "没有搜索到相关地点！", Toast.LENGTH_SHORT).show();
		}
	} else {
		Toast.makeText(context, rCode, Toast.LENGTH_SHORT).show();
	}
			
}
	
	
	private void showSuggestCity(List<SuggestionCity> cities) {
		String infomation = "推荐城市\n";
		for (int i = 0; i < cities.size(); i++) {
			infomation += "城市名称:" + cities.get(i).getCityName() + "城市区号:"
					+ cities.get(i).getCityCode() + "城市编码:"
					+ cities.get(i).getAdCode() + "\n";
		}
		Toast.makeText(context, infomation, Toast.LENGTH_SHORT).show();

	}



	@Override
	public void beforeTextChanged(CharSequence s, int start, int count,
			int after)
	{
	
		
	}



	@Override
	public void onTextChanged(CharSequence s, int start, int before, int count)
	{		
		String newText = s.toString().trim();	
	}



	@Override
	public void afterTextChanged(Editable s)
	{
		
	}
}
