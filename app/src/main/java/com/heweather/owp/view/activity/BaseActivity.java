package com.heweather.owp.view.activity;

import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;
import android.util.TypedValue;
import android.widget.TextView;

import com.heweather.owp.utils.ContentUtil;

import java.util.List;
import java.util.Locale;

public class BaseActivity extends FragmentActivity {


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
    }

    @Override
    public Resources getResources() {
        // 本地语言设置
        String language = Locale.getDefault().getLanguage();
        Resources resources = super.getResources();
        Configuration configuration = resources.getConfiguration();
        if (ContentUtil.APP_SETTING_LANG.equals("en")) {
            configuration.locale = Locale.ENGLISH;// 英语
            ContentUtil.SYS_LANG = "en";
        } else {
            if (ContentUtil.APP_SETTING_LANG.equals("sys") && !"zh".equals(language)) {
                configuration.locale = Locale.ENGLISH;// 英语
                ContentUtil.SYS_LANG = "en";
            } else {
                configuration.locale = Locale.SIMPLIFIED_CHINESE;// 简体中文
                ContentUtil.SYS_LANG = "zh";
            }
        }

        switch (ContentUtil.APP_SETTING_TESI) {
            case "small":
                configuration.fontScale = 0.8f;
                break;
            case "mid":
                configuration.fontScale = 1f;
                break;
            case "large":
                configuration.fontScale = 1.1f;
                break;
        }
        resources.updateConfiguration(configuration, resources.getDisplayMetrics());
        return resources;

    }

    @Override
    protected void onResume() {
        super.onResume();

    }

    public void smallLarge(List<TextView> tvList) {
        for (TextView textView : tvList) {
            float textSize = textView.getTextSize();
            textView.setTextSize(TypedValue.COMPLEX_UNIT_PX, textSize * 11 / 8);
        }
    }

    public void smallMid(List<TextView> tvList) {
        for (TextView textView : tvList) {
            float textSize = textView.getTextSize();
            textView.setTextSize(TypedValue.COMPLEX_UNIT_PX, textSize * 5 / 4);
        }
    }

    public void midSmall(List<TextView> tvList) {
        for (TextView textView : tvList) {
            float textSize = textView.getTextSize();
            textView.setTextSize(TypedValue.COMPLEX_UNIT_PX, textSize * 4 / 5);
        }
    }

    public void midLarge(List<TextView> tvList) {
        for (TextView textView : tvList) {
            float textSize = textView.getTextSize();
            textView.setTextSize(TypedValue.COMPLEX_UNIT_PX, textSize * 11 / 10);
        }
    }

    public void largeSmall(List<TextView> tvList) {
        for (TextView textView : tvList) {
            float textSize = textView.getTextSize();
            textView.setTextSize(TypedValue.COMPLEX_UNIT_PX, textSize * 8 / 11);
        }
    }

    public void largeMid(List<TextView> tvList) {
        for (TextView textView : tvList) {
            float textSize = textView.getTextSize();
            textView.setTextSize(TypedValue.COMPLEX_UNIT_PX, textSize * 10 / 11);
        }
    }

}
