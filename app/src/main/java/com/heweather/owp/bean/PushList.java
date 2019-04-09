package com.heweather.owp.bean;

public class PushList {
    /**
     * id : 1
     * triggerTime 发生时间
     *               1,明天；2，后天
     * cityId : CN101010100
     * event : 事件类型
     *          0	雨雪天气
     *          1	温度骤变
     *          2	天气预警
     *          3	空气质量中度污染及以上
     * remindTime 提醒时间
     *          1，上午
     *          2，下午
     */

    private String id;
    private String triggerTime;
    private String cityId;
    private String event;
    private String remindTime;
    private String positionStatus;
    private String orderby;

    public String getOrderby() {
        return orderby;
    }

    public void setOrderby(String orderby) {
        this.orderby = orderby;
    }

    public String getPositionStatus() {
        return positionStatus;
    }

    public void setPositionStatus(String positionStatus) {
        this.positionStatus = positionStatus;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTriggerTime() {
        return triggerTime;
    }

    public void setTriggerTime(String triggerTime) {
        this.triggerTime = triggerTime;
    }

    public String getCityId() {
        return cityId;
    }

    public void setCityId(String cityId) {
        this.cityId = cityId;
    }

    public String getEvent() {
        return event;
    }

    public void setEvent(String event) {
        this.event = event;
    }

    public String getRemindTime() {
        return remindTime;
    }

    public void setRemindTime(String remindTime) {
        this.remindTime = remindTime;
    }
}
