package com.heweather.owp.view.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.heweather.owp.R;
import com.heweather.owp.utils.ContentUtil;
import com.heweather.owp.utils.SpUtils;

import java.util.ArrayList;
import java.util.List;

public class SettingActivity extends BaseActivity implements View.OnClickListener {

    private TextView tvSys;
    private TextView tvChinese;
    private TextView tvEnglish;
    private TextView tvHua;
    private TextView tvShe;
    private TextView tvSmall;
    private TextView tvMid;
    private TextView tvLarge;
    private ImageView ivSys;
    private ImageView ivChinese;
    private ImageView ivEnglish;
    private ImageView ivHua;
    private ImageView ivShe;
    private ImageView ivSmall;
    private ImageView ivMid;
    private ImageView ivLarge;
    private TextView tvControl;
    private TextView tvAbout;
    private TextView tvLangTitle;
    private TextView tvUnitTitle;
    private TextView tvSizeTitle;
    private List<TextView> tvList = new ArrayList<>();
    private String preSize = "mid";
    private TextView tvSetTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        initView();
    }

    private void initView() {
        tvList = new ArrayList<>();
        String language = SpUtils.getString(this, "language", "sys");
        String unit = SpUtils.getString(this, "unit", "she");
        String size = SpUtils.getString(this, "size", "mid");
        ImageView ivBack = findViewById(R.id.iv_setting_back);
        RelativeLayout rvControl = findViewById(R.id.rv_control);
        RelativeLayout rvSys = findViewById(R.id.rv_system_language);
        RelativeLayout rvChinese = findViewById(R.id.rv_chinese_language);
        RelativeLayout rvEnglish = findViewById(R.id.rv_english_language);
        RelativeLayout rvShe = findViewById(R.id.rv_she);
        RelativeLayout rvHua = findViewById(R.id.rv_hua);
        RelativeLayout rvSmall = findViewById(R.id.rv_small);
        RelativeLayout rvMid = findViewById(R.id.rv_mid);
        RelativeLayout rvLarge = findViewById(R.id.rv_large);
        RelativeLayout rvAbout = findViewById(R.id.rv_about);
        tvSetTitle = findViewById(R.id.tv_setting_title);
        tvControl = findViewById(R.id.tv_control);
        tvAbout = findViewById(R.id.tv_about);
        tvLangTitle = findViewById(R.id.tv_choose_lang);
        tvUnitTitle = findViewById(R.id.tv_choose_unit);
        tvSizeTitle = findViewById(R.id.tv_choose_size);
        tvSys = findViewById(R.id.tv_sys_right);
        tvChinese = findViewById(R.id.tv_ch_right);
        tvEnglish = findViewById(R.id.tv_en_right);
        tvHua = findViewById(R.id.tv_hua_right);
        tvShe = findViewById(R.id.tv_she_right);
        tvSmall = findViewById(R.id.tv_small_right);
        tvMid = findViewById(R.id.tv_mid_right);
        tvLarge = findViewById(R.id.tv_large_right);
        ivSys = findViewById(R.id.iv_sys_right);
        ivChinese = findViewById(R.id.iv_ch_right);
        ivEnglish = findViewById(R.id.iv_en_right);
        ivHua = findViewById(R.id.iv_hua_right);
        ivShe = findViewById(R.id.iv_she_right);
        ivSmall = findViewById(R.id.iv_small_right);
        ivMid = findViewById(R.id.iv_mid_right);
        ivLarge = findViewById(R.id.iv_large_right);
        tvList.add(tvControl);
        tvList.add(tvAbout);
        tvList.add(tvLangTitle);
        tvList.add(tvUnitTitle);
        tvList.add(tvSizeTitle);
        tvList.add(tvSys);
        tvList.add(tvChinese);
        tvList.add(tvEnglish);
        tvList.add(tvHua);
        tvList.add(tvShe);
        tvList.add(tvSmall);
        tvList.add(tvMid);
        tvList.add(tvLarge);
        rvControl.setOnClickListener(this);
        rvChinese.setOnClickListener(this);
        rvSys.setOnClickListener(this);
        rvEnglish.setOnClickListener(this);
        rvShe.setOnClickListener(this);
        rvHua.setOnClickListener(this);
        rvSmall.setOnClickListener(this);
        rvMid.setOnClickListener(this);
        rvLarge.setOnClickListener(this);
        rvAbout.setOnClickListener(this);
        ivBack.setOnClickListener(this);
        switch (language) {
            case "sys":
                chooseView(tvSys, ivSys);
                unChooseView(tvChinese, ivChinese);
                unChooseView(tvEnglish, ivEnglish);
                break;
            case "zh":
                chooseView(tvChinese, ivChinese);
                unChooseView(tvSys, ivSys);
                unChooseView(tvEnglish, ivEnglish);
                break;
            case "en":
                chooseView(tvEnglish, ivEnglish);
                unChooseView(tvSys, ivSys);
                unChooseView(tvChinese, ivChinese);
                break;
        }
        switch (unit) {
            case "she":
                chooseView(tvShe, ivShe);
                unChooseView(tvHua, ivHua);
                break;
            case "hua":
                chooseView(tvHua, ivHua);
                unChooseView(tvShe, ivShe);
                break;
        }
        switch (size) {
            case "small":
                preSize = "small";
                chooseView(tvSmall, ivSmall);
                unChooseView(tvMid, ivMid);
                unChooseView(tvLarge, ivLarge);
                break;
            case "mid":
                preSize = "mid";
                chooseView(tvMid, ivMid);
                unChooseView(tvSmall, ivSmall);
                unChooseView(tvLarge, ivLarge);
                break;
            case "large":
                preSize = "large";
                chooseView(tvLarge, ivLarge);
                unChooseView(tvSmall, ivSmall);
                unChooseView(tvMid, ivMid);
                break;
        }
        ContentUtil.APP_PRI_TESI = preSize;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_setting_back:
                onBackPressed();
                break;
            case R.id.rv_control:
                startActivity(new Intent(this, ControlCityActivity.class));
                break;
            case R.id.rv_system_language:
                SpUtils.putString(this, "language", "sys");
                ContentUtil.APP_SETTING_LANG = "sys";
                chooseView(tvSys, ivSys);
                unChooseView(tvChinese, ivChinese);
                unChooseView(tvEnglish, ivEnglish);
                changeLang();
                break;
            case R.id.rv_chinese_language:
                SpUtils.putString(this, "language", "zh");
                ContentUtil.APP_SETTING_LANG = "zh";
                chooseView(tvChinese, ivChinese);
                unChooseView(tvSys, ivSys);
                unChooseView(tvEnglish, ivEnglish);
                changeLang();
                break;
            case R.id.rv_english_language:
                SpUtils.putString(this, "language", "en");
                ContentUtil.APP_SETTING_LANG = "en";
                chooseView(tvEnglish, ivEnglish);
                unChooseView(tvSys, ivSys);
                unChooseView(tvChinese, ivChinese);
                changeLang();
                break;
            case R.id.rv_she:
                ContentUtil.UNIT_CHANGE = true;
                SpUtils.putString(this, "unit", "she");
                ContentUtil.APP_SETTING_UNIT = "she";
                chooseView(tvShe, ivShe);
                unChooseView(tvHua, ivHua);
                break;
            case R.id.rv_hua:
                ContentUtil.UNIT_CHANGE = true;
                ContentUtil.APP_SETTING_UNIT = "hua";
                SpUtils.putString(this, "unit", "hua");
                chooseView(tvHua, ivHua);
                unChooseView(tvShe, ivShe);
                break;
            case R.id.rv_small:
                SpUtils.putString(this, "size", "small");
                chooseView(tvSmall, ivSmall);
                unChooseView(tvMid, ivMid);
                unChooseView(tvLarge, ivLarge);
                if (preSize.equalsIgnoreCase("mid")) {
                    midSmall(tvList);
                } else if (preSize.equalsIgnoreCase("large")) {
                    largeSmall(tvList);
                }
                ContentUtil.APP_PRI_TESI = preSize;
                preSize = "small";
                ContentUtil.APP_SETTING_TESI = "small";
                break;
            case R.id.rv_mid:
                SpUtils.putString(this, "size", "mid");
                chooseView(tvMid, ivMid);
                unChooseView(tvSmall, ivSmall);
                unChooseView(tvLarge, ivLarge);
                if (preSize.equalsIgnoreCase("small")) {
                    smallMid(tvList);
                } else if (preSize.equalsIgnoreCase("large")) {
                    largeMid(tvList);
                }
                ContentUtil.APP_PRI_TESI = preSize;
                preSize = "mid";
                ContentUtil.APP_SETTING_TESI = "mid";
                break;
            case R.id.rv_large:
                SpUtils.putString(this, "size", "large");
                chooseView(tvLarge, ivLarge);
                unChooseView(tvSmall, ivSmall);
                unChooseView(tvMid, ivMid);
                if (preSize.equalsIgnoreCase("small")) {
                    smallLarge(tvList);
                } else if (preSize.equalsIgnoreCase("mid")) {
                    midLarge(tvList);
                }
                ContentUtil.APP_PRI_TESI = preSize;
                preSize = "large";
                ContentUtil.APP_SETTING_TESI = "large";
                break;
            case R.id.rv_about:
                startActivity(new Intent(this, AboutActivity.class));
                break;
        }

    }

    private void chooseView(TextView textView, ImageView imageView) {
        textView.setTextColor(getResources().getColor(R.color.color_4a4a4a));
        imageView.setVisibility(View.VISIBLE);
    }

    private void unChooseView(TextView textView, ImageView imageView) {
        textView.setTextColor(getResources().getColor(R.color.color_a4a4a4));
        imageView.setVisibility(View.GONE);
    }

    private void changeLang() {
        ContentUtil.CHANGE_LANG = true;
        tvControl.setText(R.string.control_city);
        tvLangTitle.setText(R.string.choose_lang);
        tvUnitTitle.setText(R.string.choose_unit);
        tvSizeTitle.setText(R.string.choose_text_size);
        tvSys.setText(R.string.system_lang);
        tvChinese.setText(R.string.chinese);
        tvEnglish.setText(R.string.english);
        tvShe.setText(R.string.she);
        tvHua.setText(R.string.hua);
        tvSmall.setText(R.string.small);
        tvMid.setText(R.string.mid);
        tvLarge.setText(R.string.large);
        tvAbout.setText(R.string.about);
        tvSetTitle.setText(R.string.setting);
    }


}
