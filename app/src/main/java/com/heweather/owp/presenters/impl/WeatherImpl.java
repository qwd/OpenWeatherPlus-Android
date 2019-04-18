package com.heweather.owp.presenters.impl;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;

import com.heweather.owp.presenters.WeatherInterface;
import com.heweather.owp.presenters.WeatherPresenters;
import com.heweather.owp.utils.ContentUtil;
import com.heweather.owp.utils.SpUtils;

import java.util.List;

import interfaces.heweather.com.interfacesmodule.bean.Lang;
import interfaces.heweather.com.interfacesmodule.bean.Unit;
import interfaces.heweather.com.interfacesmodule.bean.air.now.AirNow;
import interfaces.heweather.com.interfacesmodule.bean.alarm.Alarm;
import interfaces.heweather.com.interfacesmodule.bean.search.Search;
import interfaces.heweather.com.interfacesmodule.bean.weather.forecast.Forecast;
import interfaces.heweather.com.interfacesmodule.bean.weather.hourly.Hourly;
import interfaces.heweather.com.interfacesmodule.bean.weather.now.Now;
import interfaces.heweather.com.interfacesmodule.view.HeWeather;

/**
 * Created by niuchong on 2018/5/17.
 */

public class WeatherImpl implements WeatherPresenters {

    private Context context;
    private WeatherInterface weatherInterface;
    private String TAG = "sky";
    private Lang lang;
    private Unit unit;


    public WeatherImpl(Context context, WeatherInterface weatherInterface) {
        this.context = context;
        this.weatherInterface = weatherInterface;
        if (ContentUtil.APP_SETTING_LANG.equals("en") || ContentUtil.APP_SETTING_LANG.equals("sys") && ContentUtil.SYS_LANG.equals("en")) {
            lang = Lang.ENGLISH;
        } else {
            lang = Lang.CHINESE_SIMPLIFIED;
        }
        unit = Unit.METRIC;
    }

    @Override
    public void getWeatherNow(String location) {
        HeWeather.getWeatherNow(context, location, lang, unit, new HeWeather.OnResultWeatherNowBeanListener() {
            @Override
            public void onError(Throwable throwable) {
                Now weatherNow = SpUtils.getBean(context, "weatherNow", Now.class);
                weatherInterface.getWeatherNow(weatherNow);
            }

            @Override
            public void onSuccess(List<Now> list) {
                weatherInterface.getWeatherNow(list.get(0));
                SpUtils.saveBean(context, "weatherNow", list.get(0));
            }
        });

    }


    @Override
    public void getWeatherForecast(final String location) {
        HeWeather.getWeatherForecast(context, location, lang, unit, new HeWeather.OnResultWeatherForecastBeanListener() {
            @Override
            public void onError(Throwable throwable) {
                Log.i("sky", "getWeatherForecast onError: ");
                Forecast weatherForecast = SpUtils.getBean(context, "weatherForecast", Forecast.class);
                weatherInterface.getWeatherForecast(weatherForecast);
                getAirForecast(location);
            }

            @Override
            public void onSuccess(List<Forecast> list) {
                weatherInterface.getWeatherForecast(list.get(0));
                getAirForecast(location);
                SpUtils.saveBean(context, "weatherForecast", list.get(0));

            }
        });
    }

    @Override
    public void getAlarm(String location) {
        HeWeather.getAlarm(context, location, lang, unit, new HeWeather.OnResultAlarmBeansListener() {
            @Override
            public void onError(Throwable throwable) {
                weatherInterface.getAlarm(null);
                Log.i("sky", "getAlarm onError: " + throwable);
            }

            @Override
            public void onSuccess(List<Alarm> list) {
                weatherInterface.getAlarm(list.get(0));
                SpUtils.saveBean(context, "alarm", list.get(0));
            }
        });
    }

    @Override
    public void getAirNow(final String location) {
        HeWeather.getAirNow(context, location, lang, unit, new HeWeather.OnResultAirNowBeansListener() {
            @Override
            public void onError(Throwable throwable) {
                Log.i("sky", "getAirNow onError: ");
                getParentAir(location);
            }

            @Override
            public void onSuccess(List<AirNow> list) {
                AirNow airNow = list.get(0);
                weatherInterface.getAirNow(airNow);
                SpUtils.saveBean(context, "airNow", airNow);
            }
        });
    }

    private void getParentAir(String location) {
        HeWeather.getSearch(context, location, "cn,overseas", 3, lang, new HeWeather.OnResultSearchBeansListener() {
            @Override
            public void onError(Throwable throwable) {

            }

            @Override
            public void onSuccess(Search search) {
                String parentCity = search.getBasic().get(0).getParent_city();
                if (TextUtils.isEmpty(parentCity)) {
                    parentCity = search.getBasic().get(0).getAdmin_area();
                }
                HeWeather.getAirNow(context, parentCity, lang, unit, new HeWeather.OnResultAirNowBeansListener() {
                    @Override
                    public void onError(Throwable throwable) {
                        weatherInterface.getAirNow(null);
                    }

                    @Override
                    public void onSuccess(List<AirNow> list) {
                        AirNow airNow = list.get(0);
                        weatherInterface.getAirNow(airNow);
                    }
                });
            }
        });
    }

    @Override
    public void getAirForecast(String location) {
//        HeWeather.getAirForecast(context, location, lang, unit, new HeWeather.OnResultAirForecastBeansListener() {
//            @Override
//            public void onError(Throwable throwable) {
//                Log.i(TAG, "getAirForecast onError: ");
//                AirForecast airForecast = SpUtils.getBean(context, "airForecast", AirForecast.class);
//                weatherInterface.getAirForecast(airForecast);
//            }
//
//            @Override
//            public void onSuccess(List<AirForecast> list) {
//                weatherInterface.getAirForecast(list.get(0));
//                SpUtils.saveBean(context, "airForecast", list.get(0));
//
//            }
//        });
    }


    @Override
    public void getWeatherHourly(String location) {
        HeWeather.getWeatherHourly(context, location, lang, unit, new HeWeather.OnResultWeatherHourlyBeanListener() {
            @Override
            public void onError(Throwable throwable) {
                Log.i("sky", "getWeatherHourly onError: getWeatherHourly");
            }

            @Override
            public void onSuccess(List<Hourly> list) {
                weatherInterface.getWeatherHourly(list.get(0));
                SpUtils.saveBean(context, "weatherHourly", list.get(0));
            }
        });
    }

}
