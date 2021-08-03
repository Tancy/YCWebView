/*
Copyright 2017 yangchong211（github.com/yangchong211）

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
*/
package com.yc.webviewdns;

import android.content.Context;

import com.alibaba.sdk.android.httpdns.HttpDns;
import com.alibaba.sdk.android.httpdns.HttpDnsService;
import com.tencent.smtt.export.external.interfaces.WebResourceRequest;
import com.tencent.smtt.export.external.interfaces.WebResourceResponse;
import com.tencent.smtt.sdk.WebView;
import com.yc.webviewutils.X5WebUtils;
import com.ycbjie.webviewlib.client.JsX5WebViewClient;
import com.ycbjie.webviewlib.view.X5WebView;

/**
 * <pre>
 *     @author yangchong
 *     blog  : https://github.com/yangchong211
 *     time  : 2019/9/10
 *     desc  : 特别重要：主要把js的处理给分离到该类中
 *     revise: 如果要自定义WebViewClient必须要集成此类
 *             demo地址：https://github.com/yangchong211/YCWebView
 * </pre>
 */
public class DnsWebViewClient extends JsX5WebViewClient {

    private HttpDnsService httpDns ;
    private WebTlsHelper tlsHelper;

    /**
     * 初始化https+dns域名解析功能，如果没有初始化，则默认不使用
     * 初始化https+dns优化，目前已经集成阿里开源的httpdns库，已经非常稳定
     * 具体更加详细内容，可以参考阿里httpdns官方文档
     */
    private void initSetHttpDns() {
        if (X5WebUtils.isHttpDns){
            // 初始化http + dns
            httpDns = HttpDns.getService(X5WebUtils.getApplication(), X5WebUtils.accountID);
            // 预解析热点域名
            httpDns.setPreResolveHosts(X5WebUtils.host);
            // 允许过期IP以实现懒加载策略
            httpDns.setExpiredIPEnabled(true);
        }
    }

    public HttpDnsService getHttpDns() {
        if (httpDns==null){
            initSetHttpDns();
        }
        return httpDns;
    }

    /**
     * 构造方法
     *
     * @param webView 需要传进来webview
     * @param context 上下文
     */
    public DnsWebViewClient(X5WebView webView, Context context) {
        super(webView, context);
        if (X5WebUtils.isHttpDns && httpDns==null){
            httpDns = getHttpDns();
            tlsHelper = new WebTlsHelper(httpDns);
        }
    }


    /**
     * 此方法添加于API21，调用于非UI线程，拦截资源请求并返回数据，返回null时WebView将继续加载资源
     *
     * 其中 WebResourceRequest 封装了请求，WebResourceResponse 封装了响应
     * 封装了一个Web资源的响应信息，包含：响应数据流，编码，MIME类型，API21后添加了响应头，状态码与状态描述
     * @param webView                           view
     * @param webResourceRequest                request，添加于API21，封装了一个Web资源的请求信息，
     *                                          包含：请求地址，请求方法，请求头，是否主框架，是否用户点击，是否重定向
     * @return
     */
    @Override
    public WebResourceResponse shouldInterceptRequest(WebView webView, WebResourceRequest webResourceRequest) {
        if (tlsHelper!=null){
            return tlsHelper.shouldInterceptRequest(mWebView,webResourceRequest);
        }
        return super.shouldInterceptRequest(webView, webResourceRequest);
    }

}