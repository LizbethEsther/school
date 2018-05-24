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
 * POIָpoint of interest,��Ȥ��
 * �ڵ�ͼ����У�һ�� POI �ɴ���һ�����á�һ�����̡�һ������ȵȡ�
 * ͨ��POI����������Ҳ͹ݡ��Ҿ��㡢�Ҳ����ȵȵĹ��ܡ�
 * @author lijinzhi
 *����������ִ������������������õ�����ʱ�ߵ����õ������⡣
 */
//ʵ�ֹؼ��ּ����Ĳ������£�
//1���̳� OnPoiSearchListener ������
//2������ PoiSearch.Query ����
//ͨ�� PoiSearch.Query(String query, String ctgr, String city) ������������
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
		Log.i("tag", "ִ������������editText ==" + editText.getText().toString());
		keyWord = editText.getText().toString().trim();
		int currentPage = 0;
		query = new PoiSearch.Query(keyWord, "", "��̶��");
		/*
		 * keyWord��ʾ�����ַ���
		 * �ڶ���������ʾPOI�������ͣ�����ѡ����һ��
		 * ѡ��POI��������ʱ������д���ʹ��룬�����Բο��·����������֣�
            cityCode����̶�У���ʾPOI�������򣬿����ǳ��б���Ҳ����
            �ǳ������ƣ�Ҳ���Դ����ַ�����
            ���ַ�������ȫ����ȫ����Χ�ڽ�������
		 */
		
		query.setPageSize(20);// ����ÿҳ��෵�ض�����poiitem
		query.setPageNum(currentPage);// ���ò��һҳ,��0��ʼ
		PoiSearch poiSearch = new PoiSearch(context, query);//���� PoiSearch �������ü���
		poiSearch.setOnPoiSearchListener(this);
		poiSearch.searchPOIAsyn();// �첽����, searchPOIAsyn() ������������
		
	}



	@Override
	public void onPoiItemSearched(PoiItem arg0, int arg1)
	{
		// TODO Auto-generated method stub
		
	}



	/*ͨ���ص��ӿ� onPoiSearched �������صĽ����
	 * ����ѯ���� POI �Ի��Ƶ�ķ�ʽ��ʾ�ڵ�ͼ��
	 * 
	 */
	/**
	 * 1�������ڻص��н���result����ȡPOI��Ϣ��
        2��result.getPois()���Ի�ȡ��PoiItem�б�Poi��ϸ��Ϣ�ɲο�PoiItem�ࡣ
        3������ǰ���в�ѯ��������POI��Ϣ��
        ����ͨ��result.getSearchSuggestionCitys()��ȡ��ǰPoi�����Ľ�����С�
        4����������ؼ�������Ϊ�����룬
        ���ͨ��result.getSearchSuggestionKeywords()�����õ������ؼ��ʽ��顣
        5�����ؽ���ɹ�����ʧ�ܵ���Ӧ�롣1000Ϊ�ɹ�������Ϊʧ��
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
					aMap.clear();// ����֮ǰ��ͼ��
					PoiOverlay poiOverlay = new PoiOverlay(aMap, poiItems);
					poiOverlay.removeFromMap();
					poiOverlay.addToMap();
					poiOverlay.zoomToSpan();
				} else if (suggestionCities != null
						&& suggestionCities.size() > 0) {
					showSuggestCity(suggestionCities);
				} else {
					Toast.makeText(context, "û����������صص㣡", Toast.LENGTH_SHORT).show();
				}
			}
		} else {
			Toast.makeText(context, "û����������صص㣡", Toast.LENGTH_SHORT).show();
		}
	} else {
		Toast.makeText(context, rCode, Toast.LENGTH_SHORT).show();
	}
			
}
	
	
	private void showSuggestCity(List<SuggestionCity> cities) {
		String infomation = "�Ƽ�����\n";
		for (int i = 0; i < cities.size(); i++) {
			infomation += "��������:" + cities.get(i).getCityName() + "��������:"
					+ cities.get(i).getCityCode() + "���б���:"
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
