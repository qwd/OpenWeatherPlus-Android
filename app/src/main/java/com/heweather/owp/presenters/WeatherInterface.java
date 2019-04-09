package com.heweather.owp.presenters;

import interfaces.heweather.com.interfacesmodule.bean.air.forecast.AirForecast;
import interfaces.heweather.com.interfacesmodule.bean.air.now.AirNow;
import interfaces.heweather.com.interfacesmodule.bean.alarm.Alarm;
import interfaces.heweather.com.interfacesmodule.bean.weather.Weather;
import interfaces.heweather.com.interfacesmodule.bean.weather.forecast.Forecast;
import interfaces.heweather.com.interfacesmodule.bean.weather.hourly.Hourly;
import interfaces.heweather.com.interfacesmodule.bean.weather.now.Now;

/**
 * Created by niuchong on 2018/5/17.
 */

public interface WeatherInterface {
    /**
     * 实况天气
     */
    void getWeatherNow(Now bean);

    /**
     * 3-7天天气预报
     */
    void getWeatherForecast(Forecast bean);

    /**
     * 灾害天气预警
     */
    void getAlarm(Alarm bean);

    /**
     * 空气实况
     */
    void getAirNow(AirNow bean);

    /**
     * 空气预报
     */
    void getAirForecast(AirForecast bean);


    /**
     * 逐小时预报
     */
    void getWeatherHourly(Hourly bean);

}
