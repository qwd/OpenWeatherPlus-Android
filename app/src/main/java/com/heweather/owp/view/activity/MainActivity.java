package com.heweather.owp.view.activity;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.heweather.owp.dataInterface.DataInterface;
import com.heweather.owp.dataInterface.DataUtil;
import com.heweather.owp.R;
import com.heweather.owp.adapter.ViewPagerAdapter;
import com.heweather.owp.bean.CityBean;
import com.heweather.owp.bean.CityBeanList;
import com.heweather.owp.utils.ContentUtil;
import com.heweather.owp.utils.DisplayUtil;
import com.heweather.owp.utils.IconUtils;
import com.heweather.owp.utils.SpUtils;
import com.heweather.owp.view.fragment.WeatherFragment;
import com.heweather.owp.view.window.LocListWindow;

import org.joda.time.DateTime;

import java.util.ArrayList;
import java.util.List;

import interfaces.heweather.com.interfacesmodule.bean.Lang;
import interfaces.heweather.com.interfacesmodule.bean.basic.Basic;
import interfaces.heweather.com.interfacesmodule.bean.search.Search;
import interfaces.heweather.com.interfacesmodule.bean.weather.now.Now;
import interfaces.heweather.com.interfacesmodule.bean.weather.now.NowBase;
import interfaces.heweather.com.interfacesmodule.view.HeWeather;

import static android.view.ViewGroup.LayoutParams.MATCH_PARENT;

public class MainActivity extends BaseActivity implements View.OnClickListener, DataInterface {
    private List<Fragment> fragments;
    private List<String> locaitons;
    private List<String> locaitonsEn;
    private List<String> cityIds;
    private ViewPager viewPager;
    private LinearLayout llRound;
    private int mNum = 0;
    private TextView tvLocation;
    private ImageView ivLoc;
    CityBeanList cityBeanList = new CityBeanList();
    private ImageView ivBack;
    private String condCode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Window window = getWindow();
        //透明状态栏
        window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        viewPager = findViewById(R.id.view_pager);
        llRound = findViewById(R.id.ll_round);
        tvLocation = findViewById(R.id.tv_location);
        ivLoc = findViewById(R.id.iv_loc);
        ivBack = findViewById(R.id.iv_main_back);
        RelativeLayout rvTitle = findViewById(R.id.rv_title);
        initFragments(true);
        ImageView ivSet = findViewById(R.id.iv_set);
        ImageView ivAdd = findViewById(R.id.iv_add_city);
        ivSet.setOnClickListener(this);
        ivAdd.setOnClickListener(this);
        setMargins(viewPager, 0, getStatusBarHeight(this) + DisplayUtil.dip2px(this, 52), 0, 0);
        setMargins(rvTitle, 0, getStatusBarHeight(this), 0, 0);

    }

    //声明AMapLocationClient类对象
    public AMapLocationClient mLocationClient = null;
    //声明定位回调监听器
    public AMapLocationListener mLocationListener = new AMapLocationListener() {
        @Override
        public void onLocationChanged(AMapLocation aMapLocation) {
            if (aMapLocation.getErrorCode() == 0) {
                ContentUtil.NOW_LON = aMapLocation.getLongitude();
                ContentUtil.NOW_LAT = aMapLocation.getLatitude();
                getNowCity(true);
                mLocationClient.onDestroy();
            } else {
                if (ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.ACCESS_FINE_LOCATION)
                        != PackageManager.PERMISSION_GRANTED || ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE)
                        != PackageManager.PERMISSION_GRANTED || ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.READ_PHONE_STATE)
                        != PackageManager.PERMISSION_GRANTED) {
                    // 没有权限
                    View view = LayoutInflater.from(MainActivity.this).inflate(R.layout.pop_loc_list, null);
                    LocListWindow locListWindow = new LocListWindow(view, MATCH_PARENT, MATCH_PARENT, MainActivity.this);
                    locListWindow.show();
                    locListWindow.showAtLocation(tvLocation, Gravity.CENTER, 0, 0);
                    if (ContentUtil.FIRST_OPEN) {
                        ContentUtil.FIRST_OPEN = false;
                        SpUtils.putBoolean(MainActivity.this, "first_open", false);
                    }
                }
                getNowCity(true);
                mLocationClient.onDestroy();
            }
        }
    };

    /**
     * 兼容全面屏的状态栏高度
     */
    public void setMargins(View view, int l, int t, int r, int b) {
        if (view.getLayoutParams() instanceof ViewGroup.MarginLayoutParams) {
            ViewGroup.MarginLayoutParams p = (ViewGroup.MarginLayoutParams) view.getLayoutParams();
            p.setMargins(l, t, r, b);
            view.requestLayout();
        }
    }

    /**
     * 获取状态栏高度
     */
    private static int getStatusBarHeight(Context context) {
        int resourceId = context.getResources().getIdentifier("status_bar_height", "dimen", "android");
        return context.getResources().getDimensionPixelSize(resourceId);
    }

    private void initFragments(boolean first) {
        cityBeanList = SpUtils.getBean(MainActivity.this, "cityBean", CityBeanList.class);
        CityBeanList cityBeanEn = SpUtils.getBean(MainActivity.this, "cityBeanEn", CityBeanList.class);
        CityBeanList cityBean = SpUtils.getBean(MainActivity.this, "cityBean", CityBeanList.class);
        locaitonsEn = new ArrayList<>();
        locaitons = new ArrayList<>();
        if (cityBeanEn != null) {
            for (CityBean city : cityBeanEn.getCityBeans()) {
                String cityName = city.getCityName();
                locaitonsEn.add(cityName);
            }
        }
        if (cityBean != null) {
            for (CityBean city : cityBean.getCityBeans()) {
                String cityName = city.getCityName();
                locaitons.add(cityName);
            }
        }
        cityIds = new ArrayList<>();
        fragments = new ArrayList<>();
        if (first) {
            initLocation();
        } else {
            getNowCity(false);
        }

    }

    private void initLocation() {
        //初始化定位
        mLocationClient = new AMapLocationClient(getApplicationContext());
        //设置定位回调监听

        //声明AMapLocationClientOption对象
        AMapLocationClientOption mLocationOption = new AMapLocationClientOption();
        //设置定位模式为AMapLocationMode.Hight_Accuracy，高精度模式。
        mLocationOption.setLocationMode(AMapLocationClientOption.AMapLocationMode.Hight_Accuracy);
        //设置定位间隔,单位毫秒,默认为2000ms，最低1000ms。
        mLocationOption.setInterval(10000);
        //单位是毫秒，默认30000毫秒，建议超时时间不要低于8000毫秒。
        mLocationOption.setHttpTimeOut(20000);
        mLocationClient.setLocationListener(mLocationListener);
        //给定位客户端对象设置定位参数
        mLocationClient.setLocationOption(mLocationOption);
        //启动定位
        mLocationClient.startLocation();
    }

    private void getNowCity(final boolean first) {
        Lang lang;
        if (ContentUtil.APP_SETTING_LANG.equals("en") || ContentUtil.APP_SETTING_LANG.equals("sys") && ContentUtil.SYS_LANG.equals("en")) {
            lang = Lang.ENGLISH;
        } else {
            lang = Lang.CHINESE_SIMPLIFIED;
        }
        HeWeather.getSearch(this, ContentUtil.NOW_LON + "," + ContentUtil.NOW_LAT, "cn,overseas", 3, lang, new HeWeather.OnResultSearchBeansListener() {
            @Override
            public void onError(Throwable throwable) {
                List<CityBean> cityBeans = new ArrayList<>();
                CityBean cityBean = new CityBean();
                cityBean.setCityName("北京");
                cityBean.setCityId("CN101010100");
                cityBeans.add(cityBean);
                getData(cityBeans, first);
            }

            @Override
            public void onSuccess(Search search) {
                Basic basic = search.getBasic().get(0);
                String cid = basic.getCid();
                String location = basic.getLocation();
                if (first) {
                    ContentUtil.NOW_CITY_ID = cid;
                    ContentUtil.NOW_CITY_NAME = location;
                }

                List<CityBean> cityBeans = new ArrayList<>();
                CityBean cityBean = new CityBean();
                cityBean.setCityName(location);
                cityBean.setCityId(cid);

                locaitons.add(0, location);
                locaitonsEn.add(0, location);
                if (cityBeanList != null && cityBeanList.getCityBeans() != null && cityBeanList.getCityBeans().size() > 0) {
                    cityBeans = cityBeanList.getCityBeans();
                    cityBeans.add(0, cityBean);
                } else {
                    cityBeans.add(cityBean);
                }
                tvLocation.setText(location);
                getData(cityBeans, first);
            }
        });
    }

    private void getNow(String location, final boolean nowCity) {
        HeWeather.getSearch(this, location, "cn,overseas", 3, Lang.CHINESE_SIMPLIFIED, new HeWeather.OnResultSearchBeansListener() {
            @Override
            public void onError(Throwable throwable) {

            }

            @Override
            public void onSuccess(Search search) {
                Basic basic = search.getBasic().get(0);
                String cid = basic.getCid();
                String location = basic.getLocation();
                if (nowCity) {
                    ContentUtil.NOW_CITY_ID = cid;
                    ContentUtil.NOW_CITY_NAME = location;
                    if (cityIds != null && cityIds.size() > 0) {
                        cityIds.add(0, cid);
                        cityIds.remove(1);
                    }
                }
                HeWeather.getWeatherNow(MainActivity.this, cid, new HeWeather.OnResultWeatherNowBeanListener() {
                    @Override
                    public void onError(Throwable throwable) {

                    }

                    @Override
                    public void onSuccess(List<Now> list) {
                        NowBase now = list.get(0).getNow();
                        condCode = now.getCond_code();
                        DateTime nowTime = DateTime.now();
                        int hourOfDay = nowTime.getHourOfDay();
                        if (hourOfDay > 6 && hourOfDay < 19) {
                            ivBack.setImageResource(IconUtils.getDayBack(condCode));
                        } else {
                            ivBack.setImageResource(IconUtils.getNightBack(condCode));
                        }
                    }
                });
            }
        });
    }

    /**
     * 获取数据
     */
    private void getData(List<CityBean> cityBeans, boolean first) {
        fragments = new ArrayList<>();
        llRound.removeAllViews();
        for (CityBean city : cityBeans) {
            String cityId = city.getCityId();
            cityIds.add(cityId);
            WeatherFragment weatherFragment = WeatherFragment.newInstance(cityId);
            fragments.add(weatherFragment);
        }
        if (cityIds.get(0).equalsIgnoreCase(ContentUtil.NOW_CITY_ID)) {
            ivLoc.setVisibility(View.VISIBLE);
        } else {
            ivLoc.setVisibility(View.INVISIBLE);
        }
        View view;
        for (int i = 0; i < fragments.size(); i++) {
            //创建底部指示器(小圆点)
            view = new View(MainActivity.this);
            view.setBackgroundResource(R.drawable.background);
            view.setEnabled(false);
            //设置宽高
            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(DisplayUtil.dip2px(this, 4), DisplayUtil.dip2px(this, 4));
            //设置间隔
            if (fragments.get(i) != fragments.get(0)) {
                layoutParams.leftMargin = 10;
            }
            //添加到LinearLayout
            llRound.addView(view, layoutParams);
        }
        viewPager.setAdapter(new ViewPagerAdapter(getSupportFragmentManager(), fragments));
        //第一次显示小白点
        llRound.getChildAt(0).setEnabled(true);
        mNum = 0;
        if (fragments.size() == 1) {
            llRound.setVisibility(View.GONE);
        } else {
            llRound.setVisibility(View.VISIBLE);
        }
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int i, float v, int i1) {

            }

            @Override
            public void onPageSelected(int i) {
                if (cityIds.get(i).equalsIgnoreCase(ContentUtil.NOW_CITY_ID)) {
                    ivLoc.setVisibility(View.VISIBLE);
                } else {
                    ivLoc.setVisibility(View.INVISIBLE);
                }
                llRound.getChildAt(mNum).setEnabled(false);
                llRound.getChildAt(i).setEnabled(true);
                mNum = i;
                tvLocation.setText(locaitons.get(i));
                if (ContentUtil.SYS_LANG.equalsIgnoreCase("en")) {
                    tvLocation.setText(locaitonsEn.get(i));
                }
            }

            @Override
            public void onPageScrollStateChanged(int i) {

            }
        });
        if (!first && fragments.size() > 1) {
            viewPager.setCurrentItem(1);
            getNow(cityIds.get(1), false);
        } else {
            viewPager.setCurrentItem(0);
            getNow(ContentUtil.NOW_LON + "," + ContentUtil.NOW_LAT, true);
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_set:
                startActivity(new Intent(this, SettingActivity.class));
                break;
            case R.id.iv_add_city:
                startActivity(new Intent(this, SearchActivity.class));
                break;
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        DataUtil.setDataInterface(this);
        if (!ContentUtil.APP_PRI_TESI.equalsIgnoreCase(ContentUtil.APP_SETTING_TESI)) {
            if (fragments != null && fragments.size() > 0) {
                for (Fragment fragment : fragments) {
                    WeatherFragment weatherFragment = (WeatherFragment) fragment;
                    weatherFragment.changeTextSize();
                }
            }
            if ("small".equalsIgnoreCase(ContentUtil.APP_SETTING_TESI)) {
                tvLocation.setTextSize(15);
            } else if ("large".equalsIgnoreCase(ContentUtil.APP_SETTING_TESI)) {
                tvLocation.setTextSize(17);
            } else {
                tvLocation.setTextSize(16);
            }
            ContentUtil.APP_PRI_TESI = ContentUtil.APP_SETTING_TESI;
        }
        if (ContentUtil.CHANGE_LANG) {
            if (ContentUtil.SYS_LANG.equalsIgnoreCase("en")) {
                changeLang(Lang.ENGLISH);
            } else {
                changeLang(Lang.CHINESE_SIMPLIFIED);
            }
            ContentUtil.CHANGE_LANG = false;
        }
        if (ContentUtil.CITY_CHANGE) {
            initFragments(true);
            ContentUtil.CITY_CHANGE = false;
        }
        if (ContentUtil.UNIT_CHANGE) {
            for (Fragment fragment : fragments) {
                WeatherFragment weatherFragment = (WeatherFragment) fragment;
                weatherFragment.changeUnit();
            }
            ContentUtil.UNIT_CHANGE = false;
        }
    }

    @Override
    public void setCid(String cid) {
        initFragments(false);
    }

    @Override
    public void deleteID(int index) {
        initFragments(true);
    }

    @Override
    public void changeBack(String condCode) {
        DateTime nowTime = DateTime.now();
        int hourOfDay = nowTime.getHourOfDay();
        if (hourOfDay > 6 && hourOfDay < 19) {
            ivBack.setImageResource(IconUtils.getDayBack(condCode));
        } else {
            ivBack.setImageResource(IconUtils.getNightBack(condCode));
        }
    }

    private void changeLang(final Lang lang) {
        HeWeather.getSearch(this, ContentUtil.NOW_LON + "," + ContentUtil.NOW_LAT, "cn,overseas", 3, lang, new HeWeather.OnResultSearchBeansListener() {
            @Override
            public void onError(Throwable throwable) {
            }

            @Override
            public void onSuccess(Search search) {
                Basic basic = search.getBasic().get(0);
                String location = basic.getLocation();

                if (lang == Lang.ENGLISH) {
                    locaitonsEn.remove(0);
                    locaitonsEn.add(0, location);
                    tvLocation.setText(locaitonsEn.get(mNum));
                }else if (lang == Lang.CHINESE_SIMPLIFIED){
                    locaitons.remove(0);
                    locaitons.add(0, location);
                    tvLocation.setText(locaitons.get(mNum));
                }
            }
        });
    }
}
