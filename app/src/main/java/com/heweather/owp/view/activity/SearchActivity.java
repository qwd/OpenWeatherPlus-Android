package com.heweather.owp.view.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.heweather.owp.R;
import com.heweather.owp.adapter.SearchAdapter;
import com.heweather.owp.bean.CityBean;
import com.heweather.owp.utils.ContentUtil;

import java.util.ArrayList;
import java.util.List;

import interfaces.heweather.com.interfacesmodule.bean.base.Code;
import interfaces.heweather.com.interfacesmodule.bean.base.Lang;
import interfaces.heweather.com.interfacesmodule.bean.base.Mode;
import interfaces.heweather.com.interfacesmodule.bean.base.Range;
import interfaces.heweather.com.interfacesmodule.bean.geo.GeoBean;
import interfaces.heweather.com.interfacesmodule.view.HeWeather;

public class SearchActivity extends AppCompatActivity implements View.OnClickListener {

    private AutoCompleteTextView etSearch;
    private LinearLayout llHistory;
    private RecyclerView rvSearch;
    private Lang lang;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        if (ContentUtil.APP_SETTING_LANG.equals("en") || ContentUtil.APP_SETTING_LANG.equals("sys") && ContentUtil.SYS_LANG.equals("en")) {
            lang = Lang.EN;
        } else {
            lang = Lang.ZH_HANS;
        }
        initView();
        initSearch();
    }

    private void initView() {
        ImageView ivBack = findViewById(R.id.iv_search_back);
        etSearch = findViewById(R.id.et_search);
        llHistory = findViewById(R.id.ll_history);
        rvSearch = findViewById(R.id.recycle_search);
        LinearLayoutManager sevenManager = new LinearLayoutManager(this);
        sevenManager.setOrientation(LinearLayoutManager.VERTICAL);
        rvSearch.setLayoutManager(sevenManager);
        ivBack.setOnClickListener(this);
    }

    private void initSearch() {
        etSearch.setThreshold(1);
        //编辑框输入监听
        etSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String searchResult = etSearch.getText().toString();
                if (!TextUtils.isEmpty(searchResult)) {
                    llHistory.setVisibility(View.GONE);
                    rvSearch.setVisibility(View.VISIBLE);
                    getSearchResult(searchResult);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_search_back:
                onBackPressed();
                break;
        }
    }

    private void getSearchResult(String location) {
        HeWeather.getGeoCityLookup(this, location, Mode.FUZZY, Range.WORLD, 10, lang, new HeWeather.OnResultGeoListener() {
            @Override
            public void onError(Throwable throwable) {
                GeoBean search = new GeoBean();
                search.setStatus("noData");
            }

            @Override
            public void onSuccess(GeoBean search) {
                if (search.getStatus().equals(Code.OK.getCode())) {
                    final List<GeoBean.LocationBean> basic = search.getLocationBean();
                    List<CityBean> data = new ArrayList<>();

                    if (basic != null && basic.size() > 0) {
                        if (data.size() > 0) {
                            data.clear();
                        }
                        for (int i = 0; i < basic.size(); i++) {
                            GeoBean.LocationBean basicData = basic.get(i);
                            String parentCity = basicData.getAdm2();
                            String adminArea = basicData.getAdm1();
                            String cnty = basicData.getCountry();
                            if (TextUtils.isEmpty(parentCity)) {
                                parentCity = adminArea;
                            }
                            if (TextUtils.isEmpty(adminArea)) {
                                parentCity = cnty;
                            }
                            CityBean cityBean = new CityBean();
                            cityBean.setCityName(parentCity + " - " + basicData.getName());
                            cityBean.setCityId(basicData.getId());
                            cityBean.setCnty(cnty);
                            cityBean.setAdminArea(adminArea);
                            data.add(cityBean);
                        }

                        SearchAdapter searchAdapter = new SearchAdapter(SearchActivity.this, data, etSearch.getText().toString(), true);
                        rvSearch.setAdapter(searchAdapter);
                        searchAdapter.notifyDataSetChanged();
                    }
                }
            }
        });
    }

}
