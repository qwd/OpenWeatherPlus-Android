package com.heweather.owp.presenters;

import interfaces.heweather.com.interfacesmodule.bean.WarningBean;
import interfaces.heweather.com.interfacesmodule.bean.air.AirNowBean;
import interfaces.heweather.com.interfacesmodule.bean.weather.WeatherBean;
import interfaces.heweather.com.interfacesmodule.bean.weather.WeatherDailyBean;
import interfaces.heweather.com.interfacesmodule.bean.weather.WeatherHourlyBean;
import interfaces.heweather.com.interfacesmodule.bean.weather.WeatherNowBean;

/**
 * Created by niuchong on 2018/5/17.
 */

public interface WeatherInterface {
    /**
     * 实况天气
     */
    void getWeatherNow(WeatherNowBean bean);

    /**
     * 3-7天天气预报
     */
    void getWeatherForecast(WeatherDailyBean bean);

    /**
     * 灾害天气预警
     */
    void getWarning(WarningBean.WarningBeanBase bean);

    /**
     * 空气实况
     */
    void getAirNow(AirNowBean bean);

    /**
     * 空气预报
     */
//    void getAirForecast(AirForecast bean);


    /**
     * 逐小时预报
     */
    void getWeatherHourly(WeatherHourlyBean bean);

}
