package com.heweather.owp.view.fragment;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.heweather.owp.MyApplication;
import com.heweather.owp.R;
import com.heweather.owp.adapter.ForecastAdapter;
import com.heweather.owp.dataInterface.DataUtil;
import com.heweather.owp.presenters.WeatherInterface;
import com.heweather.owp.presenters.impl.WeatherImpl;
import com.heweather.owp.utils.ContentUtil;
import com.heweather.owp.utils.IconUtils;
import com.heweather.owp.utils.TransUnitUtil;
import com.heweather.owp.view.horizonview.HourlyForecastView;
import com.heweather.owp.view.horizonview.IndexHorizontalScrollView;
import com.heweather.owp.view.horizonview.ScrollWatched;
import com.heweather.owp.view.horizonview.ScrollWatcher;
import com.heweather.owp.view.skyview.SunView;

import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;

import java.util.ArrayList;
import java.util.List;

import interfaces.heweather.com.interfacesmodule.bean.WarningBean;
import interfaces.heweather.com.interfacesmodule.bean.air.AirNowBean;
import interfaces.heweather.com.interfacesmodule.bean.base.Lang;
import interfaces.heweather.com.interfacesmodule.bean.base.Mode;
import interfaces.heweather.com.interfacesmodule.bean.base.Range;
import interfaces.heweather.com.interfacesmodule.bean.geo.GeoBean;
import interfaces.heweather.com.interfacesmodule.bean.weather.WeatherDailyBean;
import interfaces.heweather.com.interfacesmodule.bean.weather.WeatherHourlyBean;
import interfaces.heweather.com.interfacesmodule.bean.weather.WeatherNowBean;
import interfaces.heweather.com.interfacesmodule.view.HeWeather;

public class WeatherFragment extends Fragment implements WeatherInterface {
    private static final String PARAM = "LOCATION";
    List<ScrollWatcher> watcherList;
    private List<TextView> textViewList = new ArrayList<>();
    private ImageView ivTodayDay;
    private ImageView ivTodayNight;
    private TextView tvTodayTitle;
    private TextView tvForecastTitle;
    private TextView tvTodayMin;
    private TextView tvTodayMax;
    private TextView tvTodayHum;
    private TextView tvTodayRain;
    private TextView tvTodayPressure;
    private TextView tvTodayVisible;
    private TextView tvWindDir;
    private TextView tvWindSc;
    private TextView tvMin;
    private TextView tvMax;
    private TextView tvRain;
    private TextView tvHum;
    private TextView tvPressure;
    private TextView tvVisible;
    private TextView tvAirTitle;
    private TextView tvAir;
    private TextView tvAirNum;
    private TextView tvTodayPm25;
    private TextView tvTodayPm10;
    private TextView tvTodaySo2;
    private TextView tvTodayNo2;
    private TextView tvTodayCo;
    private TextView tvTodayO3;
    private TextView tvSunTitle;
    private RelativeLayout rvAir;
    private HourlyForecastView hourlyForecastView;
    private ScrollWatched watched;
    private TextView tvLineMin;
    private TextView tvLineMax;
    private boolean isEn = false;
    private SunView sunView;
    private SunView moonView;
    private String tz = "+8.0";
    private String currentTime;
    private String sunrise;
    private String sunset;
    private String moonRise;
    private String moonSet;
    private boolean hasAni = false;
    private TextView tvCond;
    private TextView tvTmp;
    private View rootView;
    private String todayMaxTmp;
    private String todayMinTmp;
    private WeatherDailyBean weatherForecastBean;
    private WeatherHourlyBean weatherHourlyBean;
    private String nowTmp;
    private String location;
    private String language;
    private ImageView ivBack;
    private String condCode;
    private ImageView ivLine;
    private GridLayout gridAir;
    private SwipeRefreshLayout swipeRefreshLayout;
    private TextView tvAlarm;
    private RecyclerView rvForecast;
    private ForecastAdapter forecastAdapter;

    public static WeatherFragment newInstance(String cityId) {
        WeatherFragment fragment = new WeatherFragment();
        Bundle args = new Bundle();
        args.putString(PARAM, cityId);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        if (rootView == null) {
            rootView = inflater.inflate(R.layout.fragment_weather, container, false);
        }
        return rootView;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if (getArguments() != null) {
            isEn = ContentUtil.APP_SETTING_LANG.equals("en") || ContentUtil.APP_SETTING_LANG.equals("sys") && ContentUtil.SYS_LANG.equals("en");
            location = getArguments().getString(PARAM);
            initObserver();
            initView(view);
            Lang lang = Lang.ZH_HANS;
            if (isEn) {
                lang = Lang.EN;
            }

            initData(location);

        }
    }

    private void initView(View view) {
        language = ContentUtil.SYS_LANG;
        DateTime now = DateTime.now(DateTimeZone.UTC);
        float a = Float.valueOf(tz);
        float minute = a * 60;
        now = now.plusMinutes(((int) minute));
        currentTime = now.toString("HH:mm");
        tvCond = view.findViewById(R.id.tv_today_cond);
        tvTmp = view.findViewById(R.id.tv_today_tmp);
        textViewList.add(tvTmp);
        ivBack = view.findViewById(R.id.iv_back);
        ivLine = view.findViewById(R.id.iv_line2);
        gridAir = view.findViewById(R.id.grid_air);

        rvForecast = view.findViewById(R.id.rv_forecast);

        tvTodayTitle = view.findViewById(R.id.tv_today_title);
        tvForecastTitle = view.findViewById(R.id.tv_forecast_title);
        textViewList.add(tvTodayTitle);
        textViewList.add(tvForecastTitle);
        ivTodayDay = view.findViewById(R.id.iv_today_day);
        ivTodayNight = view.findViewById(R.id.iv_today_night);
        tvTodayMin = view.findViewById(R.id.tv_min_tmp);
        textViewList.add(tvTodayMin);
        tvTodayMax = view.findViewById(R.id.tv_max_tmp);
        textViewList.add(tvTodayMax);
        tvTodayHum = view.findViewById(R.id.tv_today_hum);
        textViewList.add(tvTodayHum);
        tvTodayRain = view.findViewById(R.id.tv_today_rain);
        textViewList.add(tvTodayRain);
        tvTodayPressure = view.findViewById(R.id.tv_today_pressure);
        textViewList.add(tvTodayPressure);
        tvTodayVisible = view.findViewById(R.id.tv_today_visible);
        textViewList.add(tvTodayVisible);
        tvWindDir = view.findViewById(R.id.tv_wind_dir);
        textViewList.add(tvWindDir);
        tvWindSc = view.findViewById(R.id.tv_wind_sc);
        textViewList.add(tvWindSc);

        tvMin = view.findViewById(R.id.tv_min);
        textViewList.add(tvMin);
        tvMax = view.findViewById(R.id.tv_max);
        textViewList.add(tvMax);
        tvRain = view.findViewById(R.id.tv_rain);
        textViewList.add(tvRain);
        tvHum = view.findViewById(R.id.tv_hum);
        textViewList.add(tvHum);
        tvPressure = view.findViewById(R.id.tv_pressure);
        textViewList.add(tvPressure);
        tvVisible = view.findViewById(R.id.tv_visible);
        textViewList.add(tvVisible);

        tvAirTitle = view.findViewById(R.id.air_title);
        textViewList.add(tvAirTitle);
        rvAir = view.findViewById(R.id.rv_air);
        tvAir = view.findViewById(R.id.tv_air);
        textViewList.add(tvAir);
        tvAirNum = view.findViewById(R.id.tv_air_num);
        textViewList.add(tvAirNum);

        TextView tvPm25 = view.findViewById(R.id.tv_pm25);
        textViewList.add(tvPm25);
        tvTodayPm25 = view.findViewById(R.id.tv_today_pm25);
        textViewList.add(tvTodayPm25);
        TextView tvPm10 = view.findViewById(R.id.tv_pm10);
        textViewList.add(tvPm10);
        tvTodayPm10 = view.findViewById(R.id.tv_today_pm10);
        textViewList.add(tvTodayPm10);
        TextView tvSo2 = view.findViewById(R.id.tv_so2);
        textViewList.add(tvSo2);
        tvTodaySo2 = view.findViewById(R.id.tv_today_so2);
        textViewList.add(tvTodaySo2);
        TextView tvNo2 = view.findViewById(R.id.tv_no2);
        textViewList.add(tvNo2);
        tvTodayNo2 = view.findViewById(R.id.tv_today_no2);
        textViewList.add(tvTodayNo2);
        TextView tvCo = view.findViewById(R.id.tv_co);
        textViewList.add(tvCo);
        tvTodayCo = view.findViewById(R.id.tv_today_co);
        textViewList.add(tvTodayCo);
        TextView tvO3 = view.findViewById(R.id.tv_o3);
        textViewList.add(tvO3);
        tvTodayO3 = view.findViewById(R.id.tv_today_o3);
        textViewList.add(tvTodayO3);
        tvLineMin = view.findViewById(R.id.tv_line_min_tmp);
        textViewList.add(tvLineMin);
        tvLineMax = view.findViewById(R.id.tv_line_max_tmp);
        textViewList.add(tvLineMax);
        tvAlarm = view.findViewById(R.id.tv_today_alarm);
        textViewList.add(tvAlarm);

        TextView tvFrom = view.findViewById(R.id.tv_from);
        tvFrom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startUri();
            }
        });

        tvSunTitle = view.findViewById(R.id.tv_sun_title);
        textViewList.add(tvSunTitle);
        sunView = view.findViewById(R.id.sun_view);
        moonView = view.findViewById(R.id.moon_view);

        IndexHorizontalScrollView horizontalScrollView = view.findViewById(R.id.hsv);
        hourlyForecastView = view.findViewById(R.id.hourly);
        horizontalScrollView.setToday24HourView(hourlyForecastView);

        watched.addWatcher(hourlyForecastView);

        //横向滚动监听
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            horizontalScrollView.setOnScrollChangeListener(new View.OnScrollChangeListener() {
                @Override
                public void onScrollChange(View v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
                    watched.notifyWatcher(scrollX);
                }
            });
        }

        swipeRefreshLayout = view.findViewById(R.id.swipe_layout);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                initData(location);
            }
        });

    }


    /**
     * 初始化横向滚动条的监听
     */
    private void initObserver() {
        watcherList = new ArrayList<>();
        watched = new ScrollWatched() {
            @Override
            public void addWatcher(ScrollWatcher watcher) {
                watcherList.add(watcher);
            }

            @Override
            public void removeWatcher(ScrollWatcher watcher) {
                watcherList.remove(watcher);
            }

            @Override
            public void notifyWatcher(int x) {
                for (ScrollWatcher watcher : watcherList) {
                    watcher.update(x);
                }
            }
        };
    }

    private void initData(String location) {
        WeatherImpl weatherImpl = new WeatherImpl(this.getActivity(), this);
        weatherImpl.getWeatherHourly(location);
        weatherImpl.getAirForecast(location);
        weatherImpl.getAirNow(location);
        weatherImpl.getWarning(location);
        weatherImpl.getWeatherForecast(location);
        weatherImpl.getWeatherNow(location);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onResume() {
        super.onResume();
        isEn = ContentUtil.APP_SETTING_LANG.equals("en") || ContentUtil.APP_SETTING_LANG.equals("sys") && ContentUtil.SYS_LANG.equals("en");
        if (!language.equalsIgnoreCase(ContentUtil.SYS_LANG)) {
            changeLang();
            language = ContentUtil.SYS_LANG;
        }

        if (!hasAni && !TextUtils.isEmpty(sunrise) && !TextUtils.isEmpty(sunset) && !TextUtils.isEmpty(moonRise) && !TextUtils.isEmpty(moonSet)) {
            DateTime now = DateTime.now(DateTimeZone.UTC);
            float a = Float.valueOf(tz);
            float minute = a * 60;
            now = now.plusMinutes(((int) minute));
            currentTime = now.toString("HH:mm");
            sunView.setTimes(sunrise, sunset, currentTime);
            moonView.setTimes(moonRise, moonSet, currentTime);
            hasAni = true;
        }
    }

    @SuppressLint("SetTextI18n")
    public void changeUnit() {
        if (ContentUtil.APP_SETTING_UNIT.equals("hua")) {
            tvTodayMax.setText(TransUnitUtil.getF(todayMaxTmp) + "°");
            tvTodayMin.setText(TransUnitUtil.getF(todayMinTmp) + "°");
            tvTmp.setText(TransUnitUtil.getF(nowTmp) + "°");
        } else {
            tvTodayMax.setText(todayMaxTmp + "°");
            tvTodayMin.setText(todayMinTmp + "°");
            tvTmp.setText(nowTmp + "°");
        }
        getWeatherHourly(weatherHourlyBean);
        getWeatherForecast(weatherForecastBean);
    }

    public void changeTextSize() {
        if (!TextUtils.isEmpty(sunrise) && !TextUtils.isEmpty(sunset) && !TextUtils.isEmpty(moonRise) && !TextUtils.isEmpty(moonSet)) {
            DateTime now = DateTime.now(DateTimeZone.UTC);
            float a = Float.valueOf(tz);
            float minute = a * 60;
            now = now.plusMinutes(((int) minute));
            currentTime = now.toString("HH:mm");
            sunView.setTimes(sunrise, sunset, currentTime);
            moonView.setTimes(moonRise, moonSet, currentTime);
            hasAni = true;
        }

        getWeatherForecast(weatherForecastBean);

        if (!ContentUtil.APP_PRI_TESI.equalsIgnoreCase(ContentUtil.APP_SETTING_TESI)) {
            switch (ContentUtil.APP_PRI_TESI) {
                case "small":
                    if ("mid".equalsIgnoreCase(ContentUtil.APP_SETTING_TESI)) {
                        smallMid(textViewList);
                    } else if ("large".equalsIgnoreCase(ContentUtil.APP_SETTING_TESI)) {

                        smallLarge(textViewList);
                    }
                    break;
                case "mid":
                    if ("small".equalsIgnoreCase(ContentUtil.APP_SETTING_TESI)) {
                        midSmall(textViewList);
                    } else if ("large".equalsIgnoreCase(ContentUtil.APP_SETTING_TESI)) {
                        midLarge(textViewList);
                    }
                    break;
                case "large":
                    if ("small".equalsIgnoreCase(ContentUtil.APP_SETTING_TESI)) {
                        largeSmall(textViewList);
                    } else if ("mid".equalsIgnoreCase(ContentUtil.APP_SETTING_TESI)) {
                        largeMid(textViewList);
                    }
                    break;
            }
        }
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void getWeatherNow(WeatherNowBean bean) {
        if (bean != null && bean.getNow() != null) {
            WeatherNowBean.NowBaseBean now = bean.getNow();
            String rain = now.getPrecip();
            String hum = now.getHumidity();
            String pres = now.getPressure();
            String vis = now.getVis();
            String windDir = now.getWindDir();
            String windSc = now.getWindScale();
            String condTxt = now.getText();
            condCode = now.getIcon();
            nowTmp = now.getTemp();
            tvCond.setText(condTxt);
            tvTmp.setText(nowTmp + "°");
            if (ContentUtil.APP_SETTING_UNIT.equals("hua")) {
                tvTmp.setText(TransUnitUtil.getF(nowTmp) + "°");
            }
            tvTodayRain.setText(rain + "mm");
            tvTodayPressure.setText(pres + "HPA");
            tvTodayHum.setText(hum + "%");
            tvTodayVisible.setText(vis + "KM");
            tvWindDir.setText(windDir);
            tvWindSc.setText(windSc + "级");
            DateTime nowTime = DateTime.now();
            int hourOfDay = nowTime.getHourOfDay();
            if (hourOfDay > 6 && hourOfDay < 19) {
                ivBack.setImageResource(IconUtils.getDayBack(condCode));
            } else {
                ivBack.setImageResource(IconUtils.getNightBack(condCode));
            }
            if (isEn) {
                tvWindSc.setText("Level" + windSc);
            }
            swipeRefreshLayout.setRefreshing(false);
        }
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void getWeatherForecast(WeatherDailyBean bean) {
        if (bean != null && bean.getDaily() != null) {
            weatherForecastBean = bean;
            DateTime now = DateTime.now(DateTimeZone.UTC);
//            tz = bean.getBasic().getTz();
            float a = Float.valueOf(tz);
            float minute = a * 60;
            now = now.plusMinutes(((int) minute));
            currentTime = now.toString("HH:mm");
            List<WeatherDailyBean.DailyBean> daily_forecast = bean.getDaily();

            WeatherDailyBean.DailyBean forecastBase = daily_forecast.get(0);
            String condCodeD = forecastBase.getIconDay();
            String condCodeN = forecastBase.getIconNight();
            String tmpMin = forecastBase.getTempMin();
            String tmpMax = forecastBase.getTempMax();
            sunrise = forecastBase.getSunrise();
            sunset = forecastBase.getSunset();
            moonRise = forecastBase.getMoonRise();
            moonSet = forecastBase.getMoonSet();
            sunView.setTimes(sunrise, sunset, currentTime);
            moonView.setTimes(moonRise, moonSet, currentTime);
            todayMaxTmp = tmpMax;
            todayMinTmp = tmpMin;
            tvTodayMax.setText(tmpMax + "°");
            tvTodayMin.setText(tmpMin + "°");
            ivTodayDay.setImageResource(IconUtils.getDayIconDark(condCodeD));
            ivTodayNight.setImageResource(IconUtils.getNightIconDark(condCodeN));

            if (forecastAdapter == null) {
                forecastAdapter = new ForecastAdapter(getActivity(), daily_forecast);
                rvForecast.setAdapter(forecastAdapter);
                LinearLayoutManager forecastManager = new LinearLayoutManager(getActivity());
                forecastManager.setOrientation(LinearLayoutManager.VERTICAL);
                rvForecast.setLayoutManager(forecastManager);
            } else {
                forecastAdapter.refreshData(getActivity(), daily_forecast);
            }

        }
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void getWarning(WarningBean.WarningBeanBase alarmBase) {
        if (alarmBase != null) {
            tvAlarm.setVisibility(View.VISIBLE);
            String level = alarmBase.getLevel();
            String type = alarmBase.getType();
            if (ContentUtil.SYS_LANG.equals("en")) {
                tvAlarm.setText(type);
            } else {
                tvAlarm.setText(type + "预警");
            }
            if (!TextUtils.isEmpty(level)) {
                switch (level) {
                    case "蓝色":
                    case "Blue":
                        tvAlarm.setBackground(getResources().getDrawable(R.drawable.shape_blue_alarm));
                        tvAlarm.setTextColor(getResources().getColor(R.color.white));
                        break;
                    case "黄色":
                    case "Yellow":
                        tvAlarm.setBackground(getResources().getDrawable(R.drawable.shape_yellow_alarm));
                        tvAlarm.setTextColor(getResources().getColor(R.color.white));
                        break;
                    case "橙色":
                    case "Orange":
                        tvAlarm.setBackground(getResources().getDrawable(R.drawable.shape_orange_alarm));
                        tvAlarm.setTextColor(getResources().getColor(R.color.white));
                        break;
                    case "红色":
                    case "Red":
                        tvAlarm.setBackground(getResources().getDrawable(R.drawable.shape_red_alarm));
                        tvAlarm.setTextColor(getResources().getColor(R.color.white));
                        break;
                    case "白色":
                    case "White":
                        tvAlarm.setBackground(getResources().getDrawable(R.drawable.shape_white_alarm));
                        tvAlarm.setTextColor(getResources().getColor(R.color.black));
                        break;
                }
            }
        } else {
            tvAlarm.setVisibility(View.GONE);
        }
    }

    @Override
    public void getAirNow(AirNowBean bean) {
        if (bean != null && bean.getNow() != null) {
            ivLine.setVisibility(View.VISIBLE);
            gridAir.setVisibility(View.VISIBLE);
            rvAir.setVisibility(View.VISIBLE);
            tvAirTitle.setVisibility(View.VISIBLE);
            AirNowBean.NowBean airNowCity = bean.getNow();
            String qlty = airNowCity.getCategory();
            String aqi = airNowCity.getAqi();
            String pm25 = airNowCity.getPm2p5();
            String pm10 = airNowCity.getPm10();
            String so2 = airNowCity.getSo2();
            String no2 = airNowCity.getNo2();
            String co = airNowCity.getCo();
            String o3 = airNowCity.getO3();
            tvAir.setText(qlty);
            tvAirNum.setText(aqi);
            tvTodayPm25.setText(pm25);
            tvTodayPm10.setText(pm10);
            tvTodaySo2.setText(so2);
            tvTodayNo2.setText(no2);
            tvTodayCo.setText(co);
            tvTodayO3.setText(o3);
            rvAir.setBackground(getAirBackground(aqi));
        } else {
            ivLine.setVisibility(View.GONE);
            gridAir.setVisibility(View.GONE);
            rvAir.setVisibility(View.GONE);
            tvAirTitle.setVisibility(View.GONE);
        }
    }

    private Drawable getAirBackground(String aqi) {
        int num = Integer.parseInt(aqi);
        if (getActivity() != null) {
            if (num <= 50) {
                return getActivity().getResources().getDrawable(R.drawable.shape_aqi_excellent);
            } else if (num <= 100) {
                return getActivity().getResources().getDrawable(R.drawable.shape_aqi_good);
            } else if (num <= 150) {
                return getActivity().getResources().getDrawable(R.drawable.shape_aqi_low);
            } else if (num <= 200) {
                return getActivity().getResources().getDrawable(R.drawable.shape_aqi_mid);
            } else if (num <= 300) {
                return getActivity().getResources().getDrawable(R.drawable.shape_aqi_bad);
            } else {
                return getActivity().getResources().getDrawable(R.drawable.shape_aqi_serious);
            }
        } else {
            return MyApplication.getContext().getResources().getDrawable(R.drawable.shape_aqi_excellent);
        }
    }


    @SuppressLint("SetTextI18n")
    @Override
    public void getWeatherHourly(WeatherHourlyBean bean) {
        if (bean != null && bean.getHourly() != null) {
            weatherHourlyBean = bean;
            List<WeatherHourlyBean.HourlyBean> hourlyWeatherList = bean.getHourly();
            List<WeatherHourlyBean.HourlyBean> data = new ArrayList<>();
            if (hourlyWeatherList.size() > 23) {
                for (int i = 0; i < 24; i++) {
                    data.add(hourlyWeatherList.get(i));
                    String condCode = data.get(i).getIcon();
                    String time = data.get(i).getFxTime();
                    time = time.substring(time.length() - 11, time.length() - 9);
                    int hourNow = Integer.parseInt(time);
                    if (hourNow >= 6 && hourNow <= 19) {
                        data.get(i).setIcon(condCode + "d");
                    } else {
                        data.get(i).setIcon(condCode + "n");
                    }
                }
            } else {
                for (int i = 0; i < hourlyWeatherList.size(); i++) {
                    data.add(hourlyWeatherList.get(i));
                    String condCode = data.get(i).getIcon();
                    String time = data.get(i).getFxTime();
                    time = time.substring(time.length() - 11, time.length() - 9);
                    int hourNow = Integer.parseInt(time);
                    if (hourNow >= 6 && hourNow <= 19) {
                        data.get(i).setIcon(condCode + "d");
                    } else {
                        data.get(i).setIcon(condCode + "n");
                    }
                }
            }

            int minTmp = Integer.parseInt(data.get(0).getTemp());
            int maxTmp = minTmp;
            for (int i = 0; i < data.size(); i++) {
                int tmp = Integer.parseInt(data.get(i).getTemp());
                minTmp = Math.min(tmp, minTmp);
                maxTmp = Math.max(tmp, maxTmp);
            }
            //设置当天的最高最低温度
            hourlyForecastView.setHighestTemp(maxTmp);
            hourlyForecastView.setLowestTemp(minTmp);
            if (maxTmp == minTmp) {
                hourlyForecastView.setLowestTemp(minTmp - 1);
            }
            hourlyForecastView.initData(data);
            tvLineMax.setText(maxTmp + "°");
            tvLineMin.setText(minTmp + "°");
            if (ContentUtil.APP_SETTING_UNIT.equals("hua")) {
                tvLineMax.setText(TransUnitUtil.getF(String.valueOf(maxTmp)) + "°");
                tvLineMin.setText(TransUnitUtil.getF(String.valueOf(minTmp)) + "°");
            }
        }
    }


    /**
     * 获取星期
     *
     * @param num 0-6
     * @return 星期
     */
    private String getWeek(int num) {
        String week = " ";
        if (ContentUtil.APP_SETTING_LANG.equals("en") || ContentUtil.APP_SETTING_LANG.equals("sys") && ContentUtil.SYS_LANG.equals("en")) {
            switch (num) {
                case 1:
                    week = "Mon";
                    break;
                case 2:
                    week = "Tues";
                    break;
                case 3:
                    week = "Wed";
                    break;
                case 4:
                    week = "Thur";
                    break;
                case 5:
                    week = "Fri";
                    break;
                case 6:
                    week = "Sat";
                    break;
                case 7:
                    week = "Sun";
                    break;
            }
        } else {
            switch (num) {
                case 1:
                    week = "周一";
                    break;
                case 2:
                    week = "周二";
                    break;
                case 3:
                    week = "周三";
                    break;
                case 4:
                    week = "周四";
                    break;
                case 5:
                    week = "周五";
                    break;
                case 6:
                    week = "周六";
                    break;
                case 7:
                    week = "周日";
                    break;
            }
        }
        return week;
    }


    private void smallLarge(List<TextView> tvList) {
        for (TextView textView : tvList) {
            float textSize = textView.getTextSize();
            textView.setTextSize(TypedValue.COMPLEX_UNIT_PX, textSize * 11 / 8);
        }
    }

    private void smallMid(List<TextView> tvList) {
        for (TextView textView : tvList) {
            float textSize = textView.getTextSize();
            textView.setTextSize(TypedValue.COMPLEX_UNIT_PX, textSize * 5 / 4);
        }
    }

    private void midSmall(List<TextView> tvList) {
        for (TextView textView : tvList) {
            float textSize = textView.getTextSize();
            textView.setTextSize(TypedValue.COMPLEX_UNIT_PX, textSize * 4 / 5);
        }
    }

    private void midLarge(List<TextView> tvList) {
        for (TextView textView : tvList) {
            float textSize = textView.getTextSize();
            textView.setTextSize(TypedValue.COMPLEX_UNIT_PX, textSize * 11 / 10);
        }
    }

    private void largeSmall(List<TextView> tvList) {
        for (TextView textView : tvList) {
            float textSize = textView.getTextSize();
            textView.setTextSize(TypedValue.COMPLEX_UNIT_PX, textSize * 8 / 11);
        }
    }

    private void largeMid(List<TextView> tvList) {
        for (TextView textView : tvList) {
            float textSize = textView.getTextSize();
            textView.setTextSize(TypedValue.COMPLEX_UNIT_PX, textSize * 10 / 11);
        }
    }

    private void changeLang() {
        initData(location);
//        setWeeks(tvWeekList);
        if (forecastAdapter != null) {
            forecastAdapter.notifyDataSetChanged();
        }
        tvTodayTitle.setText(R.string.today_title);
        tvForecastTitle.setText(R.string.forecast);
        tvMin.setText(R.string.min_tmp);
        tvMax.setText(R.string.max_tmp);
        tvHum.setText(R.string.hum);
        tvRain.setText(R.string.rainfall);
        tvPressure.setText(R.string.pressure);
        tvVisible.setText(R.string.visible);
        tvAirTitle.setText(R.string.air_quality);
        tvSunTitle.setText(R.string.sun_moon);
        if (!TextUtils.isEmpty(sunrise) && !TextUtils.isEmpty(sunset) && !TextUtils.isEmpty(moonRise) && !TextUtils.isEmpty(moonSet)) {
            DateTime now = DateTime.now(DateTimeZone.UTC);
            float a = Float.valueOf(tz);
            float minute = a * 60;
            now = now.plusMinutes(((int) minute));
            currentTime = now.toString("HH:mm");
            sunView.setTimes(sunrise, sunset, currentTime);
            moonView.setTimes(moonRise, moonSet, currentTime);
            hasAni = true;
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (rootView != null && rootView.getParent() != null) {
            ((ViewGroup) rootView.getParent()).removeView(rootView);
        }
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser && !TextUtils.isEmpty(condCode)) {
            DataUtil.changeBack(condCode);
        }
        if (!hasAni && !TextUtils.isEmpty(sunrise) && !TextUtils.isEmpty(sunset) && !TextUtils.isEmpty(moonRise) && !TextUtils.isEmpty(moonSet)) {
            DateTime now = DateTime.now(DateTimeZone.UTC);
            float a = Float.valueOf(tz);
            float minute = a * 60;
            now = now.plusMinutes(((int) minute));
            currentTime = now.toString("HH:mm");
            sunView.setTimes(sunrise, sunset, currentTime);
            moonView.setTimes(moonRise, moonSet, currentTime);
            hasAni = true;
        }
    }

    private void startUri() {
        Uri uri = Uri.parse("https://www.heweather.com");
        Intent intent = new Intent(Intent.ACTION_VIEW, uri);
        startActivity(intent);
    }
}
