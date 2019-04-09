package com.heweather.owp.bean;

public class ReminderCache {
    /**
     * cityId, true, triggerTime, event, nowCity, deleteId
     */
    String cityId;
    String triggerTime;
    String event;
    boolean nowCity;
    String deleteId;

    public String getCityId() {
        return cityId;
    }

    public void setCityId(String cityId) {
        this.cityId = cityId;
    }

    public String getTriggerTime() {
        return triggerTime;
    }

    public void setTriggerTime(String triggerTime) {
        this.triggerTime = triggerTime;
    }

    public String getEvent() {
        return event;
    }

    public void setEvent(String event) {
        this.event = event;
    }

    public boolean isNowCity() {
        return nowCity;
    }

    public void setNowCity(boolean nowCity) {
        this.nowCity = nowCity;
    }

    public String getDeleteId() {
        return deleteId;
    }

    public void setDeleteId(String deleteId) {
        this.deleteId = deleteId;
    }
}
