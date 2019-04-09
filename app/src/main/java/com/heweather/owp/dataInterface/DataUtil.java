package com.heweather.owp.dataInterface;

public class DataUtil {
    private static DataInterface dataInterface;

    public static void setDataInterface(DataInterface inter) {
        dataInterface = inter;
    }

    public static void setCid(String cid) {
        if (dataInterface != null) {
            dataInterface.setCid(cid);
        }
    }
    public static void deleteId(int index){
        if (dataInterface != null) {
            dataInterface.deleteID(index);
        }
    }
    public static void changeBack(String condCode){
        if (dataInterface!=null){
            dataInterface.changeBack(condCode);
        }
    }

}
