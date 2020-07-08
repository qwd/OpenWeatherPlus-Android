package com.heweather.owp.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.Adapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.heweather.owp.R;
import com.heweather.owp.utils.ContentUtil;
import com.heweather.owp.utils.IconUtils;

import org.joda.time.DateTime;

import java.util.List;

import interfaces.heweather.com.interfacesmodule.bean.weather.WeatherDailyBean;

public class ForecastAdapter extends Adapter<ForecastAdapter.MyViewHolder> {

    private List<WeatherDailyBean.DailyBean> datas;
    private Context context;

    public ForecastAdapter(Context context, List<WeatherDailyBean.DailyBean> datas) {
        this.datas = datas;
        this.context = context;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_forecast, viewGroup, false);
        return new MyViewHolder(view);
    }

    public void refreshData(Context context, List<WeatherDailyBean.DailyBean> datas) {
        this.datas = datas;
        this.context = context;
        notifyDataSetChanged();
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull MyViewHolder myViewHolder, @SuppressLint("RecyclerView") final int i) {
        WeatherDailyBean.DailyBean forecastBase = datas.get(i);
        String condCodeD = forecastBase.getIconDay();
        String condCodeN = forecastBase.getIconNight();
        String tmpMin = forecastBase.getTempMin();
        String tmpMax = forecastBase.getTempMax();
        myViewHolder.tvMax.setText(tmpMax + "°");
        myViewHolder.tvMin.setText(tmpMin + "°");
        myViewHolder.ivDay.setImageResource(IconUtils.getDayIconDark(condCodeD));
        myViewHolder.ivNight.setImageResource(IconUtils.getDayIconDark(condCodeN));
        DateTime now = DateTime.now();
        myViewHolder.tvWeek.setText(getWeek(now.plusDays(i).getDayOfWeek()));
        myViewHolder.tvWeek.setTextColor(context.getResources().getColor(R.color.edit_hint_color));
        if (i == 0) {
            myViewHolder.tvWeek.setText(context.getString(R.string.today));
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


    @Override
    public int getItemCount() {
        return datas.size();
    }


    class MyViewHolder extends RecyclerView.ViewHolder {

        private final ImageView ivDay;
        private final ImageView ivNight;
        private final TextView tvWeek;
        private final TextView tvMin;
        private final TextView tvMax;

        MyViewHolder(@NonNull View itemView) {
            super(itemView);
            ivDay = itemView.findViewById(R.id.iv_day);
            ivNight = itemView.findViewById(R.id.iv_night);
            tvWeek = itemView.findViewById(R.id.tv_week);
            tvMin = itemView.findViewById(R.id.tv_min);
            tvMax = itemView.findViewById(R.id.tv_max);
        }
    }
}
