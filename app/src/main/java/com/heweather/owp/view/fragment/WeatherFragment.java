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

import interfaces.heweather.com.interfacesmodule.bean.air.forecast.AirForecast;
import interfaces.heweather.com.interfacesmodule.bean.air.now.AirNow;
import interfaces.heweather.com.interfacesmodule.bean.air.now.AirNowCity;
import interfaces.heweather.com.interfacesmodule.bean.alarm.Alarm;
import interfaces.heweather.com.interfacesmodule.bean.alarm.AlarmBase;
import interfaces.heweather.com.interfacesmodule.bean.weather.forecast.Forecast;
import interfaces.heweather.com.interfacesmodule.bean.weather.forecast.ForecastBase;
import interfaces.heweather.com.interfacesmodule.bean.weather.hourly.Hourly;
import interfaces.heweather.com.interfacesmodule.bean.weather.hourly.HourlyBase;
import interfaces.heweather.com.interfacesmodule.bean.weather.now.Now;
import interfaces.heweather.com.interfacesmodule.bean.weather.now.NowBase;

public class WeatherFragment extends Fragment implements WeatherInterface {
    private static final String PARAM = "LOCATION";
    List<ScrollWatcher> watcherList;
    private List<TextView> tvWeekList = new ArrayList<>();
    private List<ImageView> ivDayList = new ArrayList<>();
    private List<ImageView> ivNightList = new ArrayList<>();
    private List<TextView> tvMinList = new ArrayList<>();
    private List<TextView> tvMaxList = new ArrayList<>();
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
    private String tz = "-8.0";
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
    private Forecast weatherForecastBean;
    private Hourly weatherHourlyBean;
    private String nowTmp;
    private String location;
    private String language;
    private ImageView ivBack;
    private String condCode;
    private ImageView ivLine;
    private GridLayout gridAir;
    private SwipeRefreshLayout swipeRefreshLayout;
    private TextView tvWeek1;
    private TextView tvAlarm;

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
        tvWeekList = new ArrayList<>();
        ivDayList = new ArrayList<>();
        ivNightList = new ArrayList<>();
        tvMinList = new ArrayList<>();
        tvMaxList = new ArrayList<>();
        tvCond = view.findViewById(R.id.tv_today_cond);
        tvTmp = view.findViewById(R.id.tv_today_tmp);
        textViewList.add(tvTmp);
        ivBack = view.findViewById(R.id.iv_back);
        ivLine = view.findViewById(R.id.iv_line2);
        gridAir = view.findViewById(R.id.grid_air);

        tvWeek1 = view.findViewById(R.id.tv_week1);
        TextView tvWeek2 = view.findViewById(R.id.tv_week2);
        TextView tvWeek3 = view.findViewById(R.id.tv_week3);
        TextView tvWeek4 = view.findViewById(R.id.tv_week4);
        TextView tvWeek5 = view.findViewById(R.id.tv_week5);
        tvWeekList.add(tvWeek1);
        tvWeekList.add(tvWeek2);
        tvWeekList.add(tvWeek3);
        tvWeekList.add(tvWeek4);
        tvWeekList.add(tvWeek5);
        setWeeks(tvWeekList);
        ImageView iv1Day = view.findViewById(R.id.iv_1_day);
        ImageView iv2Day = view.findViewById(R.id.iv_2_day);
        ImageView iv3Day = view.findViewById(R.id.iv_3_day);
        ImageView iv4Day = view.findViewById(R.id.iv_4_day);
        ImageView iv5Day = view.findViewById(R.id.iv_5_day);
        ivDayList.add(iv1Day);
        ivDayList.add(iv2Day);
        ivDayList.add(iv3Day);
        ivDayList.add(iv4Day);
        ivDayList.add(iv5Day);

        ImageView iv1Night = view.findViewById(R.id.iv_1_night);
        ImageView iv2Night = view.findViewById(R.id.iv_2_night);
        ImageView iv3Night = view.findViewById(R.id.iv_3_night);
        ImageView iv4Night = view.findViewById(R.id.iv_4_night);
        ImageView iv5Night = view.findViewById(R.id.iv_5_night);
        ivNightList.add(iv1Night);
        ivNightList.add(iv2Night);
        ivNightList.add(iv3Night);
        ivNightList.add(iv4Night);
        ivNightList.add(iv5Night);

        TextView tv1MinTemp = view.findViewById(R.id.tv_1_min);
        TextView tv2MinTemp = view.findViewById(R.id.tv_2_min);
        TextView tv3MinTemp = view.findViewById(R.id.tv_3_min);
        TextView tv4MinTemp = view.findViewById(R.id.tv_4_min);
        TextView tv5MinTemp = view.findViewById(R.id.tv_5_min);
        tvMinList.add(tv1MinTemp);
        tvMinList.add(tv2MinTemp);
        tvMinList.add(tv3MinTemp);
        tvMinList.add(tv4MinTemp);
        tvMinList.add(tv5MinTemp);

        TextView tv1MaxTemp = view.findViewById(R.id.tv_1_max);
        TextView tv2MaxTemp = view.findViewById(R.id.tv_2_max);
        TextView tv3MaxTemp = view.findViewById(R.id.tv_3_max);
        TextView tv4MaxTemp = view.findViewById(R.id.tv_4_max);
        TextView tv5MaxTemp = view.findViewById(R.id.tv_5_max);
        tvMaxList.add(tv1MaxTemp);
        tvMaxList.add(tv2MaxTemp);
        tvMaxList.add(tv3MaxTemp);
        tvMaxList.add(tv4MaxTemp);
        tvMaxList.add(tv5MaxTemp);

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

    private void setWeeks(List<TextView> tvWeekList) {
        DateTime now = DateTime.now();
        tvWeek1.setText(getString(R.string.today));
        for (int i = 1; i < tvWeekList.size(); i++) {
            tvWeekList.get(i).setText(getWeek(now.plusDays(i).getDayOfWeek()));
        }
    }

    private void initData(String location) {
        WeatherImpl weatherImpl = new WeatherImpl(this.getActivity(), this);
        weatherImpl.getWeatherHourly(location);
        weatherImpl.getAirForecast(location);
        weatherImpl.getAirNow(location);
        weatherImpl.getAlarm(location);
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
        if (!ContentUtil.APP_PRI_TESI.equalsIgnoreCase(ContentUtil.APP_SETTING_TESI)) {
            switch (ContentUtil.APP_PRI_TESI) {
                case "small":
                    if ("mid".equalsIgnoreCase(ContentUtil.APP_SETTING_TESI)) {
                        smallMid(textViewList);
                        smallMid(tvMaxList);
                        smallMid(tvMinList);
                        smallMid(tvWeekList);
                    } else if ("large".equalsIgnoreCase(ContentUtil.APP_SETTING_TESI)) {
                        smallLarge(textViewList);
                        smallLarge(tvMaxList);
                        smallLarge(tvMinList);
                        smallLarge(tvWeekList);
                    }
                    break;
                case "mid":
                    if ("small".equalsIgnoreCase(ContentUtil.APP_SETTING_TESI)) {
                        midSmall(textViewList);
                        midSmall(tvMaxList);
                        midSmall(tvMinList);
                        midSmall(tvWeekList);
                    } else if ("large".equalsIgnoreCase(ContentUtil.APP_SETTING_TESI)) {
                        midLarge(textViewList);
                        midLarge(tvMaxList);
                        midLarge(tvMinList);
                        midLarge(tvWeekList);
                    }
                    break;
                case "large":
                    if ("small".equalsIgnoreCase(ContentUtil.APP_SETTING_TESI)) {
                        largeSmall(textViewList);
                        largeSmall(tvMaxList);
                        largeSmall(tvMinList);
                        largeSmall(tvWeekList);
                    } else if ("mid".equalsIgnoreCase(ContentUtil.APP_SETTING_TESI)) {
                        largeMid(textViewList);
                        largeMid(tvMaxList);
                        largeMid(tvMinList);
                        largeMid(tvWeekList);
                    }
                    break;
            }
        }
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void getWeatherNow(Now bean) {
        if (bean != null && bean.getNow() != null) {
            NowBase now = bean.getNow();
            String rain = now.getPcpn();
            String hum = now.getHum();
            String pres = now.getPres();
            String vis = now.getVis();
            String windDir = now.getWind_dir();
            String windSc = now.getWind_sc();
            String condTxt = now.getCond_txt();
            condCode = now.getCond_code();
            nowTmp = now.getTmp();
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
    public void getWeatherForecast(Forecast bean) {
        if (bean != null && bean.getDaily_forecast() != null) {
            weatherForecastBean = bean;
            DateTime now = DateTime.now(DateTimeZone.UTC);
            tz = bean.getBasic().getTz();
            float a = Float.valueOf(tz);
            float minute = a * 60;
            now = now.plusMinutes(((int) minute));
            currentTime = now.toString("HH:mm");
            List<ForecastBase> daily_forecast = bean.getDaily_forecast();
            for (int i = 0; i < 5; i++) {
                ForecastBase forecastBase = daily_forecast.get(i);
                String condCodeD = forecastBase.getCond_code_d();
                String condCodeN = forecastBase.getCond_code_n();
                String tmpMin = forecastBase.getTmp_min();
                String tmpMax = forecastBase.getTmp_max();
                if (ContentUtil.APP_SETTING_UNIT.equals("hua")) {
                    tmpMax = String.valueOf(TransUnitUtil.getF(tmpMax));
                    tmpMin = String.valueOf(TransUnitUtil.getF(tmpMin));
                }
                if (i == 0) {
                    sunrise = forecastBase.getSr();
                    sunset = forecastBase.getSs();
                    moonRise = forecastBase.getMr();
                    moonSet = forecastBase.getMs();
                    sunView.setTimes(sunrise, sunset, currentTime);
                    moonView.setTimes(moonRise, moonSet, currentTime);
                    todayMaxTmp = tmpMax;
                    todayMinTmp = tmpMin;
                    tvTodayMax.setText(tmpMax + "°");
                    tvTodayMin.setText(tmpMin + "°");
                    ivTodayDay.setImageResource(IconUtils.getDayIconDark(condCodeD));
                    ivTodayNight.setImageResource(IconUtils.getNightIconDark(condCodeN));
                }
                ivDayList.get(i).setImageResource(IconUtils.getDayIconDark(condCodeD));
                ivNightList.get(i).setImageResource(IconUtils.getNightIconDark(condCodeN));
                tvMaxList.get(i).setText(tmpMax + "°");
                tvMinList.get(i).setText(tmpMin + "°");
            }
        }
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void getAlarm(Alarm bean) {
        if (bean != null && bean.getAlarm().size() > 0 && bean.getAlarm().get(0) != null) {
            tvAlarm.setVisibility(View.VISIBLE);
            AlarmBase alarmBase = bean.getAlarm().get(0);
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
    public void getAirNow(AirNow bean) {
        if (bean != null && bean.getAir_now_city() != null) {
            ivLine.setVisibility(View.VISIBLE);
            gridAir.setVisibility(View.VISIBLE);
            rvAir.setVisibility(View.VISIBLE);
            tvAirTitle.setVisibility(View.VISIBLE);
            AirNowCity airNowCity = bean.getAir_now_city();
            String qlty = airNowCity.getQlty();
            String aqi = airNowCity.getAqi();
            String pm25 = airNowCity.getPm25();
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

    @Override
    public void getAirForecast(AirForecast bean) {

    }

    @SuppressLint("SetTextI18n")
    @Override
    public void getWeatherHourly(Hourly bean) {
        if (bean != null && bean.getHourly() != null) {
            weatherHourlyBean = bean;
            List<HourlyBase> hourlyWeatherList = bean.getHourly();
            List<HourlyBase> data = new ArrayList<>();
            if (hourlyWeatherList.size() > 23) {
                for (int i = 0; i < 24; i++) {
                    data.add(hourlyWeatherList.get(i));
                    String condCode = data.get(i).getCond_code();
                    String time = data.get(i).getTime();
                    time = time.substring(time.length() - 5, time.length() - 3);
                    int hourNow = Integer.parseInt(time);
                    if (hourNow >= 6 && hourNow <= 19) {
                        data.get(i).setCond_code(condCode + "d");
                    } else {
                        data.get(i).setCond_code(condCode + "n");
                    }
                }
            } else {
                for (int i = 0; i < hourlyWeatherList.size(); i++) {
                    data.add(hourlyWeatherList.get(i));
                    String condCode = data.get(i).getCond_code();
                    String time = data.get(i).getTime();
                    time = time.substring(time.length() - 5, time.length() - 3);
                    int hourNow = Integer.parseInt(time);
                    if (hourNow >= 6 && hourNow <= 19) {
                        data.get(i).setCond_code(condCode + "d");
                    } else {
                        data.get(i).setCond_code(condCode + "n");
                    }
                }
            }

            int minTmp = Integer.parseInt(data.get(0).getTmp());
            int maxTmp = minTmp;
            for (int i = 0; i < data.size(); i++) {
                int tmp = Integer.parseInt(data.get(i).getTmp());
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
        setWeeks(tvWeekList);
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
