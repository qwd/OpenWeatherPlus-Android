package com.heweather.owp.utils;

import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.FileCallBack;
import com.zhy.http.okhttp.callback.StringCallback;

import java.io.File;
import java.util.HashMap;

/**
 * 作者：niu on 2018/6/03
 */

public class AppNetConfig {

    /**
     * post请求
     *
     * @param url
     * @param params
     * @param headers  请求头是非必传字段，如果没有，设置null即可
     * @param callback
     */
    public static final void RequestPost(final String url, final HashMap<String, String> params,
                                         final HashMap<String, String> headers, final StringCallback callback) {
        if ( headers == null ) {
            OkHttpUtils.post().url(url).params(params).build().execute(callback);
        } else {
            OkHttpUtils.post().url(url).headers(headers).params(params).build().execute(callback);
        }
    }

    /**
     * post请求
     *
     * @param url
     * @param params
     * @param headers  请求头是非必传字段，如果没有，设置null即可
     * @param callback
     */
    public static final void RequestPost(final String url, final int writeTimeOut, final HashMap<String, String> params,
                                         final HashMap<String, String> headers, final StringCallback callback) {
        if ( headers == null ) {
            OkHttpUtils.post().url(url).params(params).build().readTimeOut(writeTimeOut).execute(callback);
        } else {
            OkHttpUtils.post().url(url).headers(headers).params(params).build().readTimeOut(writeTimeOut).execute(callback);
        }
    }

    /**
     * get请求
     *
     * @param url
     * @param callback
     */
    public static final void RequestGet(final String url, final StringCallback callback) {
        OkHttpUtils.get().url(url).build().execute(callback);
    }

    public static final void GetFile(final String url , final FileCallBack callBack){
        OkHttpUtils.get().url(url).build().execute(callBack);
    }

    /**
     * 上传单个文件
     *
     * @param name     文件标识
     * @param fileName 文件名 如：lyh.png
     * @param file     上传的文件
     * @param params   需要携带的参数
     * @param url      上传的目标地址
     * @param headers  需要携带的请求头
     * @param callback 请求结果的回调
     */
    public static final void RequestUpFile(final String name, final int writeTimeOut, final String fileName,
                                           final File file, final HashMap<String, String> params, final String url,
                                           final HashMap<String, String> headers, final StringCallback callback) {
        if ( headers == null ) {
            OkHttpUtils.post().addFile(name, fileName, file).params(params).url(url).build().writeTimeOut(writeTimeOut).execute(callback);
        } else {
            OkHttpUtils.post().addFile(name, fileName, file).params(params).headers(headers).url(url).build().writeTimeOut(writeTimeOut).execute(callback);
        }
    }

    /**
     * 上传多个文件
     *
     * @param key      本次上传的唯一标识
     * @param files    上传的文件，String类型的key为文件名， 如:lyh.png , File类型的value是上传的文件
     * @param params   需要携带的参数
     * @param url      上传的目标地址
     * @param headers  需要携带的请求头信息
     * @param callback 服务器响应结果的回调
     */
    public static final void RequestUpFiles(final String key, final HashMap<String, File> files,
                                            final HashMap<String, String> params, final String url,
                                            final HashMap<String, String> headers, final StringCallback callback) {
        if ( headers == null ) {
            OkHttpUtils.post().files(key, files).params(params).url(url).build().execute(callback);
        } else {
            OkHttpUtils.post().files(key, files).params(params).headers(headers).url(url).build().execute(callback);
        }
    }
}
