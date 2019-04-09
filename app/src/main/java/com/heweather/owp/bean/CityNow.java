package com.heweather.owp.bean;

/**
 * Created by niuchong on 2018/6/3.
 */

public class CityNow {

    /**
     * lat : 39.90498734
     * lon : 116.4052887
     * city : 北京
     * location : 北京
     * province : 北京
     * tmp : 29
     * code : 104
     */

    private String lat;
    private String lon;
    private String city;
    private String location;
    private String province;
    private String tmp;
    private String code;

    public String getLat() {
        return lat;
    }

    public void setLat(String lat) {
        this.lat = lat;
    }

    public String getLon() {
        return lon;
    }

    public void setLon(String lon) {
        this.lon = lon;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        this.province = province;
    }

    public String getTmp() {
        return tmp;
    }

    public void setTmp(String tmp) {
        this.tmp = tmp;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }
}
