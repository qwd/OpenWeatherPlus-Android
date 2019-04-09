package com.heweather.owp.utils;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.util.DisplayMetrics;

import java.io.InputStreamReader;
import java.io.LineNumberReader;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Collections;
import java.util.Enumeration;
import java.util.List;


public class DeviceUtil {

    private static final boolean imeiFirst = true;

    /**
     * 1.优先CPU序列号
     * 2.检验版本：<=6.0 请求IMEI 为null->获取DevID
     * 3.>=6.0 有权限：请求IMEI 为null -> 获取DevId
     * 4.>=6.0 无权限：->DevId
     */
    public static String getCPUSerial(Context context) {
        String defaultCPUSerial = "0000000000000000";
        String str, strCPU, cpuAddress = defaultCPUSerial;
        try {
            if (imeiFirst) {
                cpuAddress = getDeviceId(context);
            }
            if (!imeiFirst || Build.UNKNOWN.equals(cpuAddress)) {
                //读取CPU信息
                Process pp = Runtime.getRuntime().exec("cat /proc/cpuinfo | grep Serial");
                InputStreamReader ir = new InputStreamReader(pp.getInputStream());
                LineNumberReader input = new LineNumberReader(ir);
                //查找CPU序列号
                for (int i = 1; i < 100; i++) {
                    str = input.readLine();
                    if (str != null) {
                        //查找到序列号所在行
                        if (str.contains("Serial")) {
                            //提取序列号
                            strCPU = str.substring(str.indexOf(":") + 1, str.length());
                            //去空格
                            cpuAddress = strCPU.trim();
                            break;
                        }
                    }
                }
            }

            if (defaultCPUSerial.equals(cpuAddress) || TextUtils.isEmpty(cpuAddress) || Build.UNKNOWN.equals(cpuAddress)) {
                if (imeiFirst) {
                    cpuAddress = getDevIDShort();
                } else {
                    cpuAddress = getDeviceId(context);
                    if (Build.UNKNOWN.equals(cpuAddress)) {
                        //没有IMEI
                        cpuAddress = getDevIDShort();
                    }
                }
            } else if (!imeiFirst) {
                cpuAddress = "A" + cpuAddress;
            }
        } catch (Exception ignored) {
        }
        return cpuAddress;
    }

    /**
     * 获取MAC wifi
     */
    @SuppressLint("HardwareIds")
    public static String getWiFiMAC(Context context) {
        String wifiMAC = null;

        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                try {
                    // 获得IpD地址
                    InetAddress ip = getLocalInetAddress();
                    byte[] b = NetworkInterface.getByInetAddress(ip).getHardwareAddress();
                    StringBuilder sb = new StringBuilder();
                    for (int i = 0; i < b.length; i++) {
                        if (i != 0) {
                            sb.append(':');
                        }
                        String str = Integer.toHexString(b[i] & 0xFF);
                        sb.append(str.length() == 1 ? 0 + str : str);
                    }
                    wifiMAC = sb.toString().toUpperCase();
                } catch (Exception ignored) {
                }
            } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                List<NetworkInterface> all = Collections.list(NetworkInterface.getNetworkInterfaces());
                for (NetworkInterface nif : all) {
                    if (!nif.getName().equalsIgnoreCase("wlan0")) {
                        continue;
                    }
                    byte[] macBytes = nif.getHardwareAddress();
                    if (macBytes != null) {
                        StringBuilder res1 = new StringBuilder();
                        for (byte b : macBytes) {
                            res1.append(String.format("%02X:", b));
                        }
                        if (res1.length() > 0) {
                            res1.deleteCharAt(res1.length() - 1);
                        }
                        wifiMAC = res1.toString();
                    }
                }
            } else {
                WifiManager wm = (WifiManager) context.getApplicationContext().getSystemService(Context.WIFI_SERVICE);
                if (wm != null) {
                    if (wm.getConnectionInfo() != null) {
                        wifiMAC = wm.getConnectionInfo().getMacAddress();
                    }
                }
            }
        } catch (Exception ignored) {
        }
        return object2String(wifiMAC, Build.UNKNOWN).replace(":", "");
    }

    /**
     * 获取Android_ID
     */
    public static String getAndroid_Id(Context context) {
        try {
            String androidId = Settings.System.getString(context.getContentResolver(), Settings.Secure.ANDROID_ID);
            return object2String(androidId, Build.UNKNOWN);
        } catch (Exception e) {
            return Build.UNKNOWN;
        }
    }

    /**
     * 获取Serial Number
     */
    public static String getSerialNumber() {
        String serial;
        try {
            serial = Build.class.getField("SERIAL").get(null).toString();
        } catch (Exception exception) {
            //serial需要一个初始化
            serial = "serial"; // 随便一个初始化
        }
        return object2String(serial, Build.UNKNOWN);
    }

    /**
     * 获取IMEI
     */
    @SuppressLint("HardwareIds")
    private static String getDeviceId(Context context) {
        try {
            TelephonyManager telephonyManager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
            if (ActivityCompat.checkSelfPermission(context, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
                return Build.UNKNOWN;
            } else if (telephonyManager != null) {
                String imei = telephonyManager.getDeviceId();
                if (TextUtils.isEmpty(imei)) {
                    return Build.UNKNOWN;
                }
                return "B" + imei;
            } else {
                return Build.UNKNOWN;
            }
        } catch (Exception e) {
            return Build.UNKNOWN;
        }
    }

    /**
     * 根据设备硬件信息生成15位串号
     * <p>
     * Build.BRAND				--设备品牌
     * • Build.CPU_ABI			--CPU指令集
     * • Build.DEVICE			    --设备参数
     * • Build.DISPLAY			--显示屏参数
     * • Build.HOST				--HOST
     * • Build.ID 				--修订版本列表
     * • Build.MANUFACTURER		--硬件制造商
     * • Build.MODEL				--版本
     * • Build.PRODUCT			--手机制造商
     * • Build.TAGS				--描述build的标签
     * • Build.TYPE				--builder类型
     * • Build.USER
     */
    private static String getDevIDShort() {
        return "C" + Build.BOARD.length() % 10 + Build.BRAND.length() % 10 + Build.CPU_ABI.length() % 10 + Build.DEVICE.length() % 10 + Build.DISPLAY.length() % 10 + Build.HOST.length() % 10 + Build.ID.length() % 10 + Build.MANUFACTURER.length() % 10 + Build.MODEL.length() % 10 + Build.PRODUCT.length() % 10 + Build.TAGS.length() % 10 + Build.TYPE.length() % 10 + Build.USER.length() % 10;
    }

    /**
     * 获取系统的其他信息，组成json
     */
    public static String getOther(Context context) {
        StringBuilder sb = new StringBuilder();
        sb.append("{");
        try {
            sb.append("\"kvn\":\"").append(BuildConfig.VERSION_NAME).append("\"").append(",");
            sb.append("\"kvc\":\"").append(BuildConfig.VERSION_CODE).append("\"").append(",");
            sb.append("\"sid\":\"").append(object2String(Build.ID)).append("\"").append(",");
            sb.append("\"sdv\":\"").append(object2String(Build.DEVICE)).append("\"").append(",");
            sb.append("\"sbr\":\"").append(object2String(Build.BRAND)).append("\"").append(",");
            sb.append("\"smo\":\"").append(object2String(Build.MODEL)).append("\"").append(",");
            sb.append("\"spd\":\"").append(object2String(Build.PRODUCT)).append("\"").append(",");
            sb.append("\"smf\":\"").append(object2String(Build.MANUFACTURER)).append("\"").append(",");
            sb.append("\"shw\":\"").append(object2String(Build.HARDWARE)).append("\"").append(",");
            sb.append("\"sfp\":\"").append(object2String(Build.FINGERPRINT)).append("\"").append(",");
            sb.append("\"stp\":\"").append(object2String(Build.TYPE)).append("\"").append(",");
            sb.append("\"shs\":\"").append(object2String(Build.HOST)).append("\"").append(",");
            sb.append("\"sbd\":\"").append(object2String(Build.BOARD)).append("\"").append(",");
            sb.append("\"stm\":\"").append(object2String(Build.TIME)).append("\"").append(",");
            sb.append("\"sca\":\"").append(object2String(Build.CPU_ABI)).append("\"").append(",");
            sb.append("\"sbl\":\"").append(object2String(Build.BOOTLOADER)).append("\"").append(",");
            sb.append("\"sdp\":\"").append(object2String(Build.DISPLAY)).append("\"").append(",");
            sb.append("\"stg\":\"").append(object2String(Build.TAGS)).append("\"").append(",");
            sb.append("\"svr\":\"").append(object2String(Build.VERSION.RELEASE)).append("\"").append(",");
            sb.append("\"svs\":\"").append(object2String(Build.VERSION.SDK_INT)).append("\"").append(",");
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                sb.append("\"svb\":\"").append(object2String(Build.VERSION.BASE_OS)).append("\"").append(",");
            }
            sb.append("\"svc\":\"").append(object2String(Build.VERSION.CODENAME)).append("\"").append(",");
            sb.append("\"svi\":\"").append(object2String(Build.VERSION.INCREMENTAL)).append("\"").append(",");
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                sb.append("\"svs\":\"").append(object2String(Build.VERSION.SECURITY_PATCH)).append("\"").append(",");
            }

            if (context != null) {
                TelephonyManager phone = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);

                sb.append("\"avc\":\"").append(context.getPackageName()).append("\"").append(",");
                sb.append("\"avn\":\"").append(getVersionName(context)).append("\"").append(",");
                sb.append("\"aan\":\"").append(getAppName(context)).append("\"").append(",");
                if (phone != null) {
                    if (ActivityCompat.checkSelfPermission(context, Manifest.permission.READ_PHONE_STATE) == PackageManager.PERMISSION_GRANTED) {
                        @SuppressLint("HardwareIds") String telephone = phone.getSimSerialNumber();
                        if (!TextUtils.isEmpty(telephone)) {
                            sb.append("\"psn\":\"").append(object2String(telephone)).append("\"").append(",");
                        }
                        @SuppressLint("HardwareIds") String subscriberId = phone.getSubscriberId();
                        if (!TextUtils.isEmpty(subscriberId)) {
                            sb.append("\"psi\":\"").append(object2String(subscriberId)).append("\"").append(",");
                        }
                    }
                    int phoneType = phone.getPhoneType();
                    sb.append("\"ppt\":\"").append(object2String(phoneType)).append("\"").append(",");
                    String simCountryIso = phone.getSimCountryIso();
                    if (!TextUtils.isEmpty(simCountryIso)) {
                        sb.append("\"psc\":\"").append(object2String(simCountryIso)).append("\"").append(",");
                    }
                    String simOperator = phone.getSimOperator();
                    if (!TextUtils.isEmpty(simOperator)) {
                        sb.append("\"pso\":\"").append(object2String(simOperator)).append("\"").append(",");
                    }
                    String simOperatorName = phone.getSimOperatorName();
                    if (!TextUtils.isEmpty(simOperatorName)) {
                        sb.append("\"psn\":\"").append(object2String(simOperatorName)).append("\"").append(",");
                    }
                }

                if (context.getResources() != null) {
                    DisplayMetrics metrics = context.getResources().getDisplayMetrics();
                    if (metrics != null) {
                        sb.append("\"dmd\":\"").append(object2String(metrics.density)).append("\"").append(",");
                        sb.append("\"ddp\":\"").append(object2String(metrics.densityDpi)).append("\"").append(",");
                        sb.append("\"dmx\":\"").append(object2String(metrics.xdpi)).append("\"").append(",");
                        sb.append("\"dmy\":\"").append(object2String(metrics.ydpi)).append("\"").append(",");
                        sb.append("\"dsd\":\"").append(object2String(metrics.scaledDensity)).append("\"").append(",");
                    }
                }
            }

            sb = sb.deleteCharAt(sb.length() - 1);
        } catch (Exception ignored) {
        }
        sb.append("}");
        return sb.toString();
    }

    private static String object2String(Object object) {
        return object2String(object, "");
    }

    private static String object2String(Object object, String defaultString) {
        if (object instanceof String) {
            return TextUtils.isEmpty((String) object) ? defaultString : ((String) object).trim();
        } else {
            return object == null ? defaultString : object.toString();
        }
    }

    /**
     * 获取移动设备本地IP
     */
    private static InetAddress getLocalInetAddress() {
        InetAddress ip = null;
        try {
            // 列举
            Enumeration<NetworkInterface> en_netInterface = NetworkInterface.getNetworkInterfaces();
            while (en_netInterface.hasMoreElements()) {
                NetworkInterface ni = en_netInterface.nextElement();
                Enumeration<InetAddress> en_ip = ni.getInetAddresses();
                while (en_ip.hasMoreElements()) {
                    ip = en_ip.nextElement();
                    if (!ip.isLoopbackAddress() && !ip.getHostAddress().contains(":")) {
                        break;
                    } else {
                        ip = null;
                    }
                }

                if (ip != null) {
                    break;
                }
            }
        } catch (SocketException ignored) {
        }
        return ip;
    }

    /**
     * 获取versionName
     *
     * @param context 上下文
     * @return
     */
    private static String getVersionName(Context context) {
        String versionName = "";
        try {
            versionName = context.getPackageManager().
                    getPackageInfo(context.getPackageName(), 0).versionName;
        } catch (PackageManager.NameNotFoundException ignored) {
        }
        return versionName;
    }

    private static synchronized String getAppName(Context context) {
        try {
            PackageManager packageManager = context.getPackageManager();
            PackageInfo packageInfo = packageManager.getPackageInfo(
                    context.getPackageName(), 0);
            int labelRes = packageInfo.applicationInfo.labelRes;
            return context.getResources().getString(labelRes);
        } catch (Exception ignored) {
        }
        return null;
    }

}
