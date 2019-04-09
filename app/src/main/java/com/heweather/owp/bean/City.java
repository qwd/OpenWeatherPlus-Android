package com.heweather.owp.bean;

import java.util.List;

public class City {
    /**
     * cityList : [{"appUserId":4,"authorId":0,"authorName":" 系统 ","authorTime":"2018-12-25 15:55:51","basic":{"cid":"CN101010300","location":"朝阳","parent_city":"北京","admin_area":"北京","cnty":"中国","lat":"39.92148972","lon":"116.48641205"},"cityId":"CN101010300","display":1,"id":74,"recordStatus":0}]
     * msg : 获取成功
     * status : success
     */

    private String msg;
    private String status;
    private List<CityListBean> cityList;

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

    public List<CityListBean> getCityList() {
        return cityList;
    }

    public void setCityList(List<CityListBean> cityList) {
        this.cityList = cityList;
    }

    public static class CityListBean {
        /**
         * appUserId : 4
         * authorId : 0
         * authorName :  系统
         * authorTime : 2018-12-25 15:55:51
         * basic : {"cid":"CN101010300","location":"朝阳","parent_city":"北京","admin_area":"北京","cnty":"中国","lat":"39.92148972","lon":"116.48641205"}
         * cityId : CN101010300
         * display : 1
         * id : 74
         * recordStatus : 0
         */

        private int appUserId;
        private int authorId;
        private String authorName;
        private String authorTime;
        private BasicBean basic;
        private String cityId;
        private int display;
        private int id;
        private int recordStatus;
        private int orderby;

        public int getOrderby() {
            return orderby;
        }

        public void setOrderby(int orderby) {
            this.orderby = orderby;
        }

        public int getAppUserId() {
            return appUserId;
        }

        public void setAppUserId(int appUserId) {
            this.appUserId = appUserId;
        }

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

        public BasicBean getBasic() {
            return basic;
        }

        public void setBasic(BasicBean basic) {
            this.basic = basic;
        }

        public String getCityId() {
            return cityId;
        }

        public void setCityId(String cityId) {
            this.cityId = cityId;
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

        public int getRecordStatus() {
            return recordStatus;
        }

        public void setRecordStatus(int recordStatus) {
            this.recordStatus = recordStatus;
        }

        public static class BasicBean {
            /**
             * cid : CN101010300
             * location : 朝阳
             * parent_city : 北京
             * admin_area : 北京
             * cnty : 中国
             * lat : 39.92148972
             * lon : 116.48641205
             */

            private String cid;
            private String location;
            private String parent_city;
            private String admin_area;
            private String cnty;
            private String lat;
            private String lon;

            public String getCid() {
                return cid;
            }

            public void setCid(String cid) {
                this.cid = cid;
            }

            public String getLocation() {
                return location;
            }

            public void setLocation(String location) {
                this.location = location;
            }

            public String getParent_city() {
                return parent_city;
            }

            public void setParent_city(String parent_city) {
                this.parent_city = parent_city;
            }

            public String getAdmin_area() {
                return admin_area;
            }

            public void setAdmin_area(String admin_area) {
                this.admin_area = admin_area;
            }

            public String getCnty() {
                return cnty;
            }

            public void setCnty(String cnty) {
                this.cnty = cnty;
            }

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
        }
    }
}
