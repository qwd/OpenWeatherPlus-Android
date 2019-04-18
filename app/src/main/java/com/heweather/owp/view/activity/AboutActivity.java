package com.heweather.owp.view.activity;

import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.heweather.owp.R;

public class AboutActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);
        findViewById(R.id.iv_about_back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        TextView tvVersion = findViewById(R.id.tv_version_num);
        tvVersion.setText(getVersionName(this));
    }

    /**
     * 获取versionName
     *
     * @param context 上下文
     * @return 版本号
     */
    private static String getVersionName(Context context) {
        String versionName = "";
        try {
            versionName = context.getPackageManager().
                    getPackageInfo(context.getPackageName(), 0).versionName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return "v" + versionName;
    }

}
