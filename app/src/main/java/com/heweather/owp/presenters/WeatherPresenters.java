package com.heweather.owp.presenters;

/**
 * Created by niuchong on 2018/5/17.
 */

public interface WeatherPresenters {

    /**
     * 实况天气
     */
    void getWeatherNow(String location);

    /**
     * 3-7天天气预报
     */
    void getWeatherForecast(String location);

    /**
     * 灾害天气预警
     */
    void getAlarm(String location);

    /**
     * 空气实况
     */
    void getAirNow(String location);

    /**
     * 空气预报
     */
    void getAirForecast(String location);

    /**
     * 逐小时预报
     */
    void getWeatherHourly(String location);

}
