package com.heweather.owp.adapter;

import android.annotation.SuppressLint;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.Adapter;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.TextUtils;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.heweather.owp.dataInterface.DataUtil;
import com.heweather.owp.R;
import com.heweather.owp.bean.CityBean;
import com.heweather.owp.bean.CityBeanList;
import com.heweather.owp.utils.ContentUtil;
import com.heweather.owp.utils.SpUtils;
import com.heweather.owp.view.activity.SearchActivity;

import java.util.ArrayList;
import java.util.List;

import interfaces.heweather.com.interfacesmodule.bean.Lang;
import interfaces.heweather.com.interfacesmodule.bean.basic.Basic;
import interfaces.heweather.com.interfacesmodule.bean.search.Search;
import interfaces.heweather.com.interfacesmodule.view.HeWeather;

/**
 * 最近搜索
 */
public class SearchAdapter extends Adapter<RecyclerView.ViewHolder> {

    private List<CityBean> data;
    private SearchActivity activity;
    private String searchText;
    private Lang lang;
    private CityBeanList cityBeanList = new CityBeanList();
    private boolean isSearching;

    public SearchAdapter(SearchActivity activity, List<CityBean> data, String searchText, boolean isSearching) {
        this.activity = activity;
        this.data = data;
        this.searchText = searchText;
        this.isSearching = isSearching;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        if (ContentUtil.APP_SETTING_LANG.equals("en") || ContentUtil.APP_SETTING_LANG.equals("sys") && ContentUtil.SYS_LANG.equals("en")) {
            lang = Lang.ENGLISH;
        } else {
            lang = Lang.CHINESE_SIMPLIFIED;
        }
        View view;
        if (isSearching) {
            view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_searching, viewGroup, false);
        } else {
            view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_search_history, viewGroup, false);
        }
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder myViewHolder, @SuppressLint("RecyclerView") final int i) {
        MyViewHolder viewHolder = (MyViewHolder) myViewHolder;
        View itemView = viewHolder.itemView;
        String name = data.get(i).getCityName();
        int x = name.indexOf("-");
        String parentCity = name.substring(0, x);
        String location = name.substring(x + 1);

        String cityName = location + "，" + parentCity + "，" + data.get(i).getAdminArea() + "，" + data.get(i).getCnty();
        if (TextUtils.isEmpty(data.get(i).getAdminArea())) {
            cityName = location + "，" + parentCity + "，" + data.get(i).getCnty();
        }
        if (!TextUtils.isEmpty(cityName)) {
            viewHolder.tvCity.setText(cityName);
            if (cityName.contains(searchText)) {
                int index = cityName.indexOf(searchText);
                //创建一个 SpannableString对象
                SpannableString sp = new SpannableString(cityName);
                //设置高亮样式一
                sp.setSpan(new ForegroundColorSpan(activity.getResources().getColor(R.color.light_text_color)), index, index + searchText.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                viewHolder.tvCity.setText(sp);
            }
        }

        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String cid = data.get(i).getCityId();
                if (lang.equals(Lang.CHINESE_SIMPLIFIED)) {
                    saveData(Lang.ENGLISH, "cityBeanEn", cid);
                    saveBean("cityBean", cid, i);
                } else {
                    saveData(Lang.CHINESE_SIMPLIFIED, "cityBean", cid);
                    saveBean("cityBeanEn", cid, i);
                }

            }
        });
    }


    private void saveBean(final String key, String cid, int x) {
        List<CityBean> citys = new ArrayList<>();
        cityBeanList = SpUtils.getBean(activity, key, CityBeanList.class);
        if (cityBeanList != null && cityBeanList.getCityBeans() != null) {
            citys = cityBeanList.getCityBeans();
        }
        for (int i = 0; i < citys.size(); i++) {
            if (citys.get(i).getCityId().equals(cid)) {
                citys.remove(i);
            }
        }
        if (citys.size() == 10) {
            citys.remove(9);
        }
        citys.add(0, data.get(x));
        CityBeanList cityBeans = new CityBeanList();
        cityBeans.setCityBeans(citys);
        SpUtils.saveBean(activity, key, cityBeans);

    }

    private void saveData(Lang lang, final String key, final String cid) {
        HeWeather.getSearch(activity, cid, "cn,overseas", 1, lang, new HeWeather.OnResultSearchBeansListener() {
            @Override
            public void onError(Throwable throwable) {
                Log.i("sky", "onError: ");
                activity.onBackPressed();
            }

            @Override
            public void onSuccess(Search search) {
                List<CityBean> citys = new ArrayList<>();
                if (!search.getStatus().equals("unknown city") && !search.getStatus().equals("noData")) {
                    List<Basic> basic = search.getBasic();
                    Basic basicData = basic.get(0);
                    String parentCity = basicData.getParent_city();
                    String adminArea = basicData.getAdmin_area();
                    String cnty = basicData.getCnty();
                    if (TextUtils.isEmpty(parentCity)) {
                        parentCity = adminArea;
                    }
                    if (TextUtils.isEmpty(adminArea)) {
                        parentCity = cnty;
                    }
                    CityBean cityBean = new CityBean();
                    cityBean.setCityName(parentCity + " - " + basicData.getLocation());
                    cityBean.setCityId(basicData.getCid());
                    cityBean.setCnty(cnty);
                    cityBean.setAdminArea(adminArea);

                    cityBeanList = SpUtils.getBean(activity, key, CityBeanList.class);
                    if (cityBeanList != null && cityBeanList.getCityBeans() != null) {
                        citys = cityBeanList.getCityBeans();
                    }
                    for (int i = 0; i < citys.size(); i++) {
                        if (citys.get(i).getCityId().equals(cid)) {
                            citys.remove(i);
                        }
                    }
                    if (citys.size() == 10) {
                        citys.remove(9);
                    }
                    citys.add(0, cityBean);
                    CityBeanList cityBeans = new CityBeanList();
                    cityBeans.setCityBeans(citys);
                    SpUtils.saveBean(activity, key, cityBeans);
                    DataUtil.setCid(cid);
                    activity.onBackPressed();
                }
            }
        });
    }


    @Override
    public int getItemCount() {
        return data.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {

        private final TextView tvCity;

        MyViewHolder(@NonNull View itemView) {
            super(itemView);
            tvCity = itemView.findViewById(R.id.tv_item_history_city);

        }
    }
}
