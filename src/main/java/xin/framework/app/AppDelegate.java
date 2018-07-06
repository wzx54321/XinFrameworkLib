package xin.framework.app;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.Application;
import android.content.Context;
import android.os.Bundle;

import xin.framework.BuildConfig;
import xin.framework.configs.AppConfig;
import xin.framework.configs.CrashReportConfig;
import xin.framework.configs.DBConfig;
import xin.framework.configs.FileConfig;
import xin.framework.imageloader.base.GlideApp;
import xin.framework.utils.android.ContextUtils;
import xin.framework.utils.android.Loger.Log;
import xin.framework.utils.android.Loger.LogLevel;
import xin.framework.utils.android.Loger.MemoryLog;
import xin.framework.utils.android.ScreenUtils;

/**
 * 作者：xin on 2018/6/7 0007 16:42
 * <p>
 * 邮箱：ittfxin@126.com
 */

class AppDelegate implements Application.ActivityLifecycleCallbacks {
    private App app;

    private static int appCreateCount;

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
        // init  CrashReport
        CrashReportConfig.init(app);

        // 生命周期
        app.registerActivityLifecycleCallbacks(this);


        // 屏幕宽高
        ScreenUtils.init(app);

    }


    @Override
    public void onActivityCreated(Activity activity, Bundle savedInstanceState) {
        if (MemoryLog.DEBUG_MEMORY) {// 堆栈和内存使用log
            MemoryLog.printMemory(activity.getClass().getName() + "-->onCreate");
        }


        if (appCreateCount == 0) {
            // 配置文件系统
            new FileConfig().init(activity, new FileConfig.OnFileCreatedListener() {
                @Override
                public void onCreated() {
                    // init  CrashReport
                    CrashReportConfig.init(app);
                }

                @Override
                public void onFailure() {
                }
            });


        }
        appCreateCount++;
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
}
