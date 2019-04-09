package com.heweather.owp.bean;

import java.util.List;

public class PushBean {
    private String msg;
    private String status;
    private List<PushList> pushList;

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public List<PushList> getPushList() {
        return pushList;
    }

    public void setPushList(List<PushList> pushList) {
        this.pushList = pushList;
    }
}
