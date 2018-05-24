package com.example.mark;


import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import com.amap.api.services.core.AMapException;
import com.amap.api.services.weather.LocalDayWeatherForecast;
import com.amap.api.services.weather.LocalWeatherForecast;
import com.amap.api.services.weather.LocalWeatherForecastResult;
import com.amap.api.services.weather.LocalWeatherLive;
import com.amap.api.services.weather.LocalWeatherLiveResult;
import com.amap.api.services.weather.WeatherSearch;
import com.amap.api.services.weather.WeatherSearch.OnWeatherSearchListener;
import com.amap.api.services.weather.WeatherSearchQuery;


import java.util.List;


public class Weather extends Activity implements OnWeatherSearchListener {
    private TextView forecasttv;
    private TextView reporttime1;
    private TextView reporttime2;
    private TextView weather;
    private TextView Temperature;
    private TextView wind;
    private TextView humidity;
    private WeatherSearchQuery mquery;
    private WeatherSearch mweathersearch;
    private LocalWeatherLive weatherlive;
    private LocalWeatherForecast weatherforecast;
    private List<LocalDayWeatherForecast> forecastlist = null;
    private String cityname;//���������ĳ��У�����д���ƻ�adcode��

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weather);
        Bundle bundle = this.getIntent().getExtras();
        cityname = bundle.getString("cityname");
        init(cityname);
        searchliveweather();
        searchforcastsweather();
    }

    private void init(String cityname) {
        TextView city = (TextView) findViewById(R.id.city);
        city.setText(cityname);
        forecasttv = (TextView) findViewById(R.id.forecast);
        reporttime1 = (TextView) findViewById(R.id.reporttime1);
        reporttime2 = (TextView) findViewById(R.id.reporttime2);
        weather = (TextView) findViewById(R.id.weather);
        Temperature = (TextView) findViewById(R.id.temp);
        wind = (TextView) findViewById(R.id.wind);
        humidity = (TextView) findViewById(R.id.humidity);
    }

    /**
     * Ԥ��������ѯ
     */
    private void searchforcastsweather() {
        mquery = new WeatherSearchQuery(cityname, WeatherSearchQuery.WEATHER_TYPE_FORECAST);//��������Ϊ���к��������ͣ�ʵʱ����Ϊ1������Ԥ��Ϊ2
        mweathersearch = new WeatherSearch(this);
        mweathersearch.setOnWeatherSearchListener(this);
        mweathersearch.setQuery(mquery);
        mweathersearch.searchWeatherAsyn(); //�첽����
    }

    /**
     * ʵʱ������ѯ
     */
    private void searchliveweather() {
        mquery = new WeatherSearchQuery(cityname, WeatherSearchQuery.WEATHER_TYPE_LIVE);//��������Ϊ���к��������ͣ�ʵʱ����Ϊ1������Ԥ��Ϊ2
        mweathersearch = new WeatherSearch(this);
        mweathersearch.setOnWeatherSearchListener(this);
        mweathersearch.setQuery(mquery);
        mweathersearch.searchWeatherAsyn(); //�첽����
    }

    /**
     * ʵʱ������ѯ�ص�
     */
    @Override
    public void onWeatherLiveSearched(LocalWeatherLiveResult weatherLiveResult, int rCode) {
        if (rCode == 1000) {
            if (weatherLiveResult != null && weatherLiveResult.getLiveResult() != null) {
                weatherlive = weatherLiveResult.getLiveResult();
                reporttime1.setText(weatherlive.getReportTime() + "����");
                weather.setText(weatherlive.getWeather());
                Temperature.setText(weatherlive.getTemperature() + "��");
                wind.setText(weatherlive.getWindDirection() + "��     " + weatherlive.getWindPower() + "��");
                humidity.setText("ʪ��         " + weatherlive.getHumidity() + "%");
            } else {
            	Toast.makeText(Weather.this, "ʧ��1", Toast.LENGTH_SHORT).show();
            }
        } else {
        	Toast.makeText(Weather.this, "ʧ��2", Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * ����Ԥ����ѯ����ص�
     */
    @Override
    public void onWeatherForecastSearched(
            LocalWeatherForecastResult weatherForecastResult, int rCode) {
        if (rCode == 1000) {
            if (weatherForecastResult != null && weatherForecastResult.getForecastResult() != null
                    && weatherForecastResult.getForecastResult().getWeatherForecast() != null
                    && weatherForecastResult.getForecastResult().getWeatherForecast().size() > 0) {
                weatherforecast = weatherForecastResult.getForecastResult();
                forecastlist = weatherforecast.getWeatherForecast();
                fillforecast();

            } else {
            	Toast.makeText(Weather.this, "ʧ��3", Toast.LENGTH_SHORT).show();
            }
        } else {
        	Toast.makeText(Weather.this, "ʧ��4", Toast.LENGTH_SHORT).show();
        }
    }

    private void fillforecast() {
        reporttime2.setText(weatherforecast.getReportTime() + "����");
        String forecast = "";
        for (int i = 0; i < forecastlist.size(); i++) {
            LocalDayWeatherForecast localdayweatherforecast = forecastlist.get(i);
            String week = null;
            switch (Integer.valueOf(localdayweatherforecast.getWeek())) {
                case 1:
                    week = "��һ";
                    break;
                case 2:
                    week = "�ܶ�";
                    break;
                case 3:
                    week = "����";
                    break;
                case 4:
                    week = "����";
                    break;
                case 5:
                    week = "����";
                    break;
                case 6:
                    week = "����";
                    break;
                case 7:
                    week = "����";
                    break;
                default:
                    break;
            }
            String temp = String.format("%-3s/%3s",
                    localdayweatherforecast.getDayTemp() + "��",
                    localdayweatherforecast.getNightTemp() + "��");
            String date = localdayweatherforecast.getDate();
            forecast += date + "  " + week + "                       " + temp + "\n\n";
        }
        forecasttv.setText(forecast);
    }
}