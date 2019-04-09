package com.heweather.owp.adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.Adapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.heweather.owp.dataInterface.DataUtil;
import com.heweather.owp.R;
import com.heweather.owp.bean.CityBean;
import com.heweather.owp.bean.CityBeanList;
import com.heweather.owp.utils.SpUtils;
import com.heweather.owp.view.activity.MainActivity;
import com.heweather.owp.view.window.LocListWindow;

import java.util.ArrayList;
import java.util.List;

public class LocLIstAdapter extends Adapter<RecyclerView.ViewHolder> {

    private List<CityBean> data;
    private LocListWindow locListWindow;
    private MainActivity activity;

    public LocLIstAdapter(LocListWindow locListWindow, List<CityBean> data, MainActivity context) {
        this.data = data;
        this.locListWindow = locListWindow;
        this.activity = context;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view;
        view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_search_favorite_light, viewGroup, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, final int i) {
        MyViewHolder myViewHolder = (MyViewHolder) viewHolder;
        myViewHolder.tvCity.setText(data.get(i).getCityName());
        myViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String city = data.get(i).getCityId();
                String cityName = data.get(i).getCityName();

                CityBean cityBean = new CityBean();
                cityBean.setCityName(cityName);
                cityBean.setCityId(city);
                CityBeanList cityBeans;
                List<CityBean> citys = new ArrayList<>();

                cityBeans = SpUtils.getBean(activity, "cityBean", CityBeanList.class);
                if (cityBeans != null && cityBeans.getCityBeans() != null) {
                    citys = cityBeans.getCityBeans();
                }else {
                    cityBeans = new CityBeanList();
                }
                citys.add(0, cityBean);
                cityBeans.setCityBeans(citys);
                SpUtils.saveBean(activity, "cityBean", cityBeans);
                SpUtils.saveBean(activity, "cityBeanEn", cityBeans);

                SpUtils.putString(activity, "lastLocation", city);
                DataUtil.setCid(city);
                locListWindow.dismiss();
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
            tvCity = itemView.findViewById(R.id.tv_item_favorite_city);

        }
    }
}
