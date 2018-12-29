package xin.framework.app;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import xin.framework.BuildConfig;
import xin.framework.configs.AppConfig;
import xin.framework.configs.CrashReportConfig;
import xin.framework.configs.DBConfig;
import xin.framework.configs.FileConfig;
import xin.framework.hybrid.webview.WebViewConfig;
import xin.framework.imageloader.base.GlideApp;
import xin.framework.utils.android.ContextUtils;
import xin.framework.utils.android.Loger.Log;
import xin.framework.utils.android.Loger.LogLevel;
import xin.framework.utils.android.Loger.MemoryLog;
import xin.framework.utils.android.ScreenUtils;

/**
 * 作者：xin on 2018/6/7 0007 16:42
 * <p>
 * <p>
 * 邮箱：ittfxin@126.com
 * <p>
 * https://github.com/wzx54321/XinFrameworkLib
 */

class AppDelegate implements Application.ActivityLifecycleCallbacks {
    private App app;

    private static int appCreateCount;

    /**
     * 是否初始化webview
     */
    private   boolean mIsWebViewInit;

    AppDelegate(App app) {
        this.app = app;
        ContextUtils.init(app);
    }


    void onCreate() {


        appCreateCount = 0;

        //  创建或更新数据库
        DBConfig.init(app);

        // Log 配置
        Log.init().logLevel(BuildConfig.DEBUG ? LogLevel.FULL : LogLevel.NONE);

        // 配置： ANR异常捕获 内存泄露捕获
       /* if (!LeakCanary.isInAnalyzerProcess(app)) {
            BlockCanary.install(app, new AppBlockCanaryContext()).start();
            LeakCanary.install(app);
        }*/


        //  Device ID
        AppConfig.setDeviceId(app);
        // 生命周期
        app.registerActivityLifecycleCallbacks(this);
        // 屏幕宽高
        ScreenUtils.init(app);
        // init  CrashReport
        CrashReportConfig.init(app);
        // 配置WebView,预先加载WEBVIEW提高反应速度，如果不使用weView可以忽略
        mIsWebViewInit = WebViewConfig.getInstance().init();
    }


    @Override
    public void onActivityCreated(Activity activity, Bundle savedInstanceState) {
        if (MemoryLog.DEBUG_MEMORY) {// 堆栈和内存使用log
            MemoryLog.printMemory(activity.getClass().getName() + "-->onCreate");
        }



    }

    @Override
    public void onActivityStarted(Activity activity) {

    }

    @Override
    public void onActivityResumed(Activity activity) {

    }

    @Override
    public void onActivityPaused(Activity activity) {

    }

    @Override
    public void onActivityStopped(Activity activity) {

    }

    @Override
    public void onActivitySaveInstanceState(Activity activity, Bundle outState) {

    }

    @Override
    public void onActivityDestroyed(Activity activity) {

    }

    void onLowMemory() {
        GlideApp.get(app).onLowMemory();
    }

    void onTrimMemory(int level) {
        GlideApp.get(app).onTrimMemory(level);
    }

    void exit() {
        try {


            ActivityManager activityMgr =
                    (ActivityManager) app.getSystemService(Context.ACTIVITY_SERVICE);
            if (activityMgr != null)
                activityMgr.killBackgroundProcesses(app.getPackageName());
            System.exit(0);
        } catch (Exception er) {
            Log.e(er, "exit app error");
        }

    }

    public void restartApp() {
        Intent intent =new Intent(app,AppRestartBridge.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK |Intent.FLAG_ACTIVITY_CLEAR_TASK );
        app.startActivity(intent);
    }
}
