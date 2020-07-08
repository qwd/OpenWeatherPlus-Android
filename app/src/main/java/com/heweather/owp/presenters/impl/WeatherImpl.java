package com.heweather.owp.presenters.impl;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;

import com.heweather.owp.presenters.WeatherInterface;
import com.heweather.owp.presenters.WeatherPresenters;
import com.heweather.owp.utils.ContentUtil;
import com.heweather.owp.utils.SpUtils;

import interfaces.heweather.com.interfacesmodule.bean.WarningBean;
import interfaces.heweather.com.interfacesmodule.bean.air.AirNowBean;
import interfaces.heweather.com.interfacesmodule.bean.base.Code;
import interfaces.heweather.com.interfacesmodule.bean.base.Lang;
import interfaces.heweather.com.interfacesmodule.bean.base.Mode;
import interfaces.heweather.com.interfacesmodule.bean.base.Range;
import interfaces.heweather.com.interfacesmodule.bean.base.Unit;
import interfaces.heweather.com.interfacesmodule.bean.geo.GeoBean;
import interfaces.heweather.com.interfacesmodule.bean.weather.WeatherDailyBean;
import interfaces.heweather.com.interfacesmodule.bean.weather.WeatherHourlyBean;
import interfaces.heweather.com.interfacesmodule.bean.weather.WeatherNowBean;
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
            lang = Lang.EN;
        } else {
            lang = Lang.ZH_HANS;
        }
        unit = Unit.METRIC;
    }

    @Override
    public void getWeatherNow(String location) {
        HeWeather.getWeatherNow(context, location, lang, unit, new HeWeather.OnResultWeatherNowListener() {
            @Override
            public void onError(Throwable throwable) {
                WeatherNowBean weatherNow = SpUtils.getBean(context, "weatherNow", WeatherNowBean.class);
                weatherInterface.getWeatherNow(weatherNow);
            }

            @Override
            public void onSuccess(WeatherNowBean weatherNowBean) {
                if (Code.OK.getCode().equalsIgnoreCase(weatherNowBean.getCode())) {
                    weatherInterface.getWeatherNow(weatherNowBean);
                    SpUtils.saveBean(context, "weatherNow", weatherNowBean);
                }
            }

        });

    }


    @Override
    public void getWeatherForecast(final String location) {

        HeWeather.getWeather3D(context, location, lang, unit, new HeWeather.OnResultWeatherDailyListener() {
            @Override
            public void onError(Throwable throwable) {
                Log.i("sky", "getWeatherForecast onError: ");
                WeatherDailyBean weatherForecast = SpUtils.getBean(context, "weatherForecast", WeatherDailyBean.class);
                weatherInterface.getWeatherForecast(weatherForecast);
                getAirForecast(location);
            }

            @Override
            public void onSuccess(WeatherDailyBean weatherDailyBean) {
                if (Code.OK.getCode().equalsIgnoreCase(weatherDailyBean.getCode())) {
                    weatherInterface.getWeatherForecast(weatherDailyBean);
                    getAirForecast(location);
                    SpUtils.saveBean(context, "weatherForecast", weatherDailyBean);
                }
            }

        });
    }

    @Override
    public void getWarning(String location) {
        HeWeather.getWarning(context, location, lang, new HeWeather.OnResultWarningListener() {
            @Override
            public void onError(Throwable throwable) {
                weatherInterface.getWarning(null);
                Log.i("sky", "getWarning onError: " + throwable);
            }

            @Override
            public void onSuccess(WarningBean warningBean) {
                if (Code.OK.getCode().equalsIgnoreCase(warningBean.getCode())) {
                    if (warningBean.getBeanBaseList() != null && warningBean.getBeanBaseList().size() > 0) {
                        weatherInterface.getWarning(warningBean.getBeanBaseList().get(0));
                        SpUtils.saveBean(context, "alarm", warningBean);
                    }
                }
            }

        });
    }

    @Override
    public void getAirNow(final String location) {
        HeWeather.getAirNow(context, location, lang, new HeWeather.OnResultAirNowListener() {
            @Override
            public void onError(Throwable throwable) {
                Log.i("sky", "getAirNow onError: ");
                getParentAir(location);
            }

            @Override
            public void onSuccess(AirNowBean airNowBean) {
                if (Code.OK.getCode().equalsIgnoreCase(airNowBean.getCode())) {
                    weatherInterface.getAirNow(airNowBean);
                    SpUtils.saveBean(context, "airNow", airNowBean);
                }
            }
        });
    }

    private void getParentAir(String location) {
        HeWeather.getGeoCityLookup(context, location, Mode.FUZZY, Range.WORLD, 3, lang, new HeWeather.OnResultGeoListener() {
            @Override
            public void onError(Throwable throwable) {

            }

            @Override
            public void onSuccess(GeoBean geoBean) {
                String parentCity = geoBean.getLocationBean().get(0).getAdm2();
                if (TextUtils.isEmpty(parentCity)) {
                    parentCity = geoBean.getLocationBean().get(0).getAdm1();
                }
                HeWeather.getAirNow(context, parentCity, lang, new HeWeather.OnResultAirNowListener() {
                    @Override
                    public void onError(Throwable throwable) {
                        weatherInterface.getAirNow(null);
                    }

                    @Override
                    public void onSuccess(AirNowBean airNow) {
                        if (Code.OK.getCode().equalsIgnoreCase(airNow.getCode())) {
                            weatherInterface.getAirNow(airNow);
                        }
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
        HeWeather.getWeather24Hourly(context, location, lang, unit, new HeWeather.OnResultWeatherHourlyListener() {
            @Override
            public void onError(Throwable throwable) {
                Log.i("sky", "getWeatherHourly onError: getWeatherHourly");
            }

            @Override
            public void onSuccess(WeatherHourlyBean weatherHourlyBean) {
                if (Code.OK.getCode().equalsIgnoreCase(weatherHourlyBean.getCode())) {
                    weatherInterface.getWeatherHourly(weatherHourlyBean);
                    SpUtils.saveBean(context, "weatherHourly", weatherHourlyBean);
                }
            }
        });
    }

}
