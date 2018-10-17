package xin.framework.app;

import android.app.Application;
import android.content.Context;

import xin.framework.utils.android.SysUtils;

/**
 * 作者：xin on 2018/6/7 0007 14:19
 * <p>
 * <p>
 * 邮箱：ittfxin@126.com
 * <p>
 * https://github.com/wzx54321/XinFrameworkLib
 * <p>
 * <p>
 * Application 基类
 */

public class App extends Application {

    private static AppDelegate mAppDelegate;

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);

    }


    @Override
    public void onCreate() {
        super.onCreate();

        // 判断是否是主进程
        if (!getApplicationInfo().packageName.equals(SysUtils.getCurProcessName(this)))
            return;

        mAppDelegate = new AppDelegate(this);
        mAppDelegate.onCreate();
    }


    @Override
    public void onLowMemory() {
        super.onLowMemory();
        if (mAppDelegate != null)
        mAppDelegate.onLowMemory();
    }


    @Override
    public void onTrimMemory(int level) {
        super.onTrimMemory(level);
        if (mAppDelegate != null)
        mAppDelegate.onTrimMemory(level);
    }

    /**
     * 退出应用
     */
    @SuppressWarnings("unused")
    public static void exitApp() {
        if (mAppDelegate != null)
            mAppDelegate.exit();
    }


    /**
     * 重启应用
     */
    public static void restart() {
        if (mAppDelegate != null)
            mAppDelegate.restartApp();
    }
}
