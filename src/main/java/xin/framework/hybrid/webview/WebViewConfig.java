package xin.framework.hybrid.webview;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.MutableContextWrapper;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;

import java.io.File;

import xin.framework.configs.FileConfig;
import xin.framework.hybrid.download.WebDownLoadListener;
import xin.framework.hybrid.model.CookiesHandler;
import xin.framework.network.Network;
import xin.framework.utils.android.Loger.Log;
import xin.framework.utils.android.SysUtils;
import xin.framework.utils.common.utils.io.FileUtil;

/**
 * Description :WebView Config
 * Created by 王照鑫 on 2017/11/1 0001.
 */

public class WebViewConfig implements IWebViewInit {


    private XinWebView mWebView;

    /**
     * 是否允许打电话
     */
    public static final boolean TEL_ENABLE = true;
    /**
     * 是否允许发邮件
     */
    public static final boolean MAIL_ENABLE = true;
    /**
     * 是否允许发短信
     */
    public static final boolean SMS_ENABLE = true;
    /**
     * 是否允许下载
     */
    public static final boolean DOWNLOAD_ENABLE = false;


    private WebSettings mSettings;


    private WebViewConfig() {

    }

    public static WebViewConfig getInstance() {
        return Holder.sWebViewConfig;
    }


    private static class Holder {
        protected static WebViewConfig sWebViewConfig = new WebViewConfig();
    }

    @Override
    public boolean init() {


        createWebView();
        doConfig();


        return true;
    }


    /**
     * 使用webView
     */
    @Override
    public XinWebView useWebView(Context context) {
        mWebView = WebViewCache.getInstance().useWebView(context);
        if (mWebView == null) {

            mWebView = reuseWebView(context);

        }


        return mWebView;
    }

    private XinWebView reuseWebView(Context context) {

        createWebView();
        doConfig();
        if (mWebView != null) {
            ((MutableContextWrapper) mWebView.getContext()).setBaseContext(context);

            mWebView.setIsUsed(true);
        }
        return mWebView;
    }


    /**
     * 不再使用重创建
     */
    @Override
    public void resetWebView() {
        WebViewCache.getInstance().resetWebView();
        mWebView = null;
        createWebView();
        doConfig();
    }


    private void createWebView() {
        if (mWebView == null)
            mWebView = WebViewCache.getInstance().initWebView();
        if (mWebView != null) {
            Log.i("webview 初始化成功");
        }

    }

    private void doConfig() {
        if (mWebView == null)
            return;


        mSettings = initWebSettings(mWebView);
        CookiesHandler.initCookiesManager(mWebView.getContext());

        if (DOWNLOAD_ENABLE)
            mWebView.setDownloadListener(new WebDownLoadListener(mWebView.getContext()));
    }



    @Override
    public WebSettings initWebSettings(WebView view) {

        // 根据需求执行配置

        WebSettings setting = view.getSettings();
        // 适配
        setting.setSupportZoom(true);
        setting.setTextZoom(100);
        setting.setBuiltInZoomControls(true);
        setting.setDisplayZoomControls(false);// 隐藏缩放按钮
        setting.setUseWideViewPort(true);
        setting.setLoadWithOverviewMode(true);
        if (SysUtils.hasKitKat()) {

            setting.setLayoutAlgorithm(android.webkit.WebSettings.LayoutAlgorithm.SINGLE_COLUMN);
        } else {
            setting.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.NORMAL);
        }

        // 存储
        // setting.setSavePassword(true);
        setting.setSaveFormData(true);
        setting.setAllowFileAccess(true);//允许加载本地文件html  file协议
        setting.setDefaultTextEncodingName("UTF-8");
        setting.setDomStorageEnabled(true);
        setting.setDatabaseEnabled(true);
        setting.setAppCacheEnabled(true);
        // 定位
        setting.setGeolocationEnabled(true);// 启用地理定位
        String dir = getCacheDir(view.getContext());
        // 设置H5缓存
        setting.setAppCachePath(dir);

        if (!SysUtils.hasKitKat()) {
            //设置数据库路径  api19 已经废弃,这里只针对 webkit 起作用
            setting.setGeolocationDatabasePath(dir);
            setting.setDatabasePath(dir);
            //缓存文件最大值
            setting.setAppCacheMaxSize(Long.MAX_VALUE);
        }

        //根据cache-control获取数据。
        setting.setCacheMode(android.webkit.WebSettings.LOAD_DEFAULT);


        if (SysUtils.hasKitKat()) {
            setting.setLoadsImagesAutomatically(true);//图片自动缩放 打开
        } else {
            setting.setLoadsImagesAutomatically(false);//图片自动缩放 关闭
        }

        setting.setSupportMultipleWindows(false);
        setting.setBlockNetworkImage(false);//是否阻塞加载网络图片  协议http or https

        setting.setNeedInitialFocus(true);
        setting.setJavaScriptCanOpenWindowsAutomatically(true);
        setting.setDefaultFontSize(16);
        setting.setMinimumFontSize(12);//设置 WebView 支持的最小字体大小，默认为 8
        setting.setJavaScriptEnabled(true);
        setting.setJavaScriptCanOpenWindowsAutomatically(true);
        setting.setAllowUniversalAccessFromFileURLs(true);
        if (SysUtils.hasLollipop()) {
            //适配5.0不允许http和https混合使用情况
            setting.setMixedContentMode(android.webkit.WebSettings.MIXED_CONTENT_ALWAYS_ALLOW);
            view.setLayerType(View.LAYER_TYPE_HARDWARE, null);
        } else if (SysUtils.hasKitKat()) {
            view.setLayerType(View.LAYER_TYPE_HARDWARE, null);
        } else if (!SysUtils.hasKitKat()) {
            view.setLayerType(View.LAYER_TYPE_SOFTWARE, null);
        }
        view.setScrollBarStyle(WebView.SCROLLBARS_INSIDE_INSET);
        view.setSaveEnabled(true);
        //  view.setKeepScreenOn(true);




     /* TODO 还没有使用sonic的接口   // add java script interface
         // sonic 使用的
        view.removeJavascriptInterface("searchBoxJavaBridge_");
        intent.putExtra(SonicJavaScriptInterface.PARAM_LOAD_URL_TIME, System.currentTimeMillis());
        view.addJavascriptInterface(new SonicJavaScriptInterface(sonicSessionClient, intent), "sonic");*/


        return setting;
    }

    @Override
    public String getCacheDir(Context context) {

        return context.getCacheDir().getAbsolutePath() + File.separator + FileConfig.DIR_WEB_CACHE;
    }

    @Override
    public File getSonicCacheDir(Context context) {
        String dir = context.getFilesDir().getAbsolutePath() + File.separator + FileConfig.DIR_WEB_SONIC_CACHE;
        File file = new File(dir);


        FileUtil.createDir(file);

        return file;
    }


    @SuppressLint("JavascriptInterface")
    @Override
    public void addJavascriptInterface(Object object, String name) {


        mWebView.addJavascriptInterface(object, name);

    }


    @Override
    public void clearWebCache(boolean mIsWebViewInit) {
        WebViewCache.getInstance().clearWebCache(mIsWebViewInit);
    }

    @Override
    public void checkCacheMode() {
        if ( Network.isAvailable(mWebView.getContext())) {
            //根据cache-control获取数据。
            getSettings().setCacheMode(android.webkit.WebSettings.LOAD_DEFAULT);
        } else {
            //没网，则从本地获取，即离线加载
            getSettings().setCacheMode(android.webkit.WebSettings.LOAD_CACHE_ELSE_NETWORK);
        }

    }





    public WebSettings getSettings() {
        return mSettings;
    }




}
