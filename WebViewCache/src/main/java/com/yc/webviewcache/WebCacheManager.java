package com.yc.webviewcache;

import android.app.Application;

import java.io.File;

public class WebCacheManager {



    /**
     * 初始化缓存
     * @param application                       上下文
     */
    public static void initCache(Application application , String path){
        if (path==null || path.length()==0){
            path = "YcCacheWebView";
        }
        //1.创建委托对象
        WebViewCacheDelegate webViewCacheDelegate = WebViewCacheDelegate.getInstance();
        //2.创建调用处理器对象，实现类
        WebViewCacheWrapper.Builder builder = new WebViewCacheWrapper.Builder(application);
        //设置缓存路径，默认getCacheDir，名称CacheWebViewCache
        builder.setCachePath(new File(application.getCacheDir().toString(),path))
                //设置缓存大小，默认100M
                .setCacheSize(1024*1024*100)
                //设置本地路径
                //.setAssetsDir("yc")
                //设置http请求链接超时，默认20秒
                .setConnectTimeoutSecond(20)
                //设置http请求链接读取超时，默认20秒
                .setReadTimeoutSecond(20)
                //设置缓存为正常模式，默认模式为强制缓存静态资源
                .setCacheType(WebCacheType.FORCE);
        webViewCacheDelegate.init(builder);
    }



}
