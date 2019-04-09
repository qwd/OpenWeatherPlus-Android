package com.heweather.owp.bean;

import java.util.List;

public class SettingBean {
    private String msg;
    private String status;
    private List<ExtendListBean> extendList;

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

    public List<ExtendListBean> getExtendList() {
        return extendList;
    }

    public void setExtendList(List<ExtendListBean> extendList) {
        this.extendList = extendList;
    }

    public static class ExtendListBean {
        /**
         * authorId : 0
         * authorName :  系统
         * authorTime : 2019-02-22 18:00:15
         * display : 1
         * id : 1
         * k : 1
         * recordStatus : 0
         * token : AkqUTcgsck8qjWJaJgUnBqeBRcIF0ApS1bBvkyn6PP32
         * v : 2
         */

        private int authorId;
        private String authorName;
        private String authorTime;
        private int display;
        private int id;
        private String k;
        private int recordStatus;
        private String token;
        private String v;

        public int getAuthorId() {
            return authorId;
        }

        public void setAuthorId(int authorId) {
            this.authorId = authorId;
        }

        public String getAuthorName() {
            return authorName;
        }

        public void setAuthorName(String authorName) {
            this.authorName = authorName;
        }

        public String getAuthorTime() {
            return authorTime;
        }

        public void setAuthorTime(String authorTime) {
            this.authorTime = authorTime;
        }

        public int getDisplay() {
            return display;
        }

        public void setDisplay(int display) {
            this.display = display;
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getK() {
            return k;
        }

        public void setK(String k) {
            this.k = k;
        }

        public int getRecordStatus() {
            return recordStatus;
        }

        public void setRecordStatus(int recordStatus) {
            this.recordStatus = recordStatus;
        }

        public String getToken() {
            return token;
        }

        public void setToken(String token) {
            this.token = token;
        }

        public String getV() {
            return v;
        }

        public void setV(String v) {
            this.v = v;
        }
    }
}
