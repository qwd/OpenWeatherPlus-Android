package com.heweather.owp.bean;

public class AttentionBeanBase {
    private String title;
    private String time;
    private String minTmp;
    private String maxTmp;
    private String aql;
    private String wind;
    private String type;
    private String cid;
    private String place;
    private String attentionType;
    private String deleteId;
    private boolean dataIsTrue;
    private boolean isNowCity;

    public String getPlace() {
        return place;
    }

    public void setPlace(String place) {
        this.place = place;
    }

    public String getDeleteId() {
        return deleteId;
    }

    public void setDeleteId(String deleteId) {
        this.deleteId = deleteId;
    }

    public boolean isNowCity() {
        return isNowCity;
    }

    public void setNowCity(boolean nowCity) {
        isNowCity = nowCity;
    }

    public String getAttentionType() {
        return attentionType;
    }

    public void setAttentionType(String attentionType) {
        this.attentionType = attentionType;
    }

    public String getCid() {
        return cid;
    }

    public void setCid(String cid) {
        this.cid = cid;
    }

    public boolean getDataIsTrue() {
        return dataIsTrue;
    }

    public void setDataIsTrue(boolean dataIsTrue) {
        this.dataIsTrue = dataIsTrue;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getMinTmp() {
        return minTmp;
    }

    public void setMinTmp(String minTmp) {
        this.minTmp = minTmp;
    }

    public String getMaxTmp() {
        return maxTmp;
    }

    public void setMaxTmp(String maxTmp) {
        this.maxTmp = maxTmp;
    }

    public String getAql() {
        return aql;
    }

    public void setAql(String aql) {
        this.aql = aql;
    }

    public String getWind() {
        return wind;
    }

    public void setWind(String wind) {
        this.wind = wind;
    }
}
