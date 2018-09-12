package xin.framework.app;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;

import xin.framework.utils.android.Loger.Log;
import xin.framework.utils.android.SysUtils;

/**
 * 作者：xin on 2018/9/12 10:18
 * <p>
 * 邮箱：ittfxin@126.com
 * <p>
 * 应用重启使用
 */
public class AppRestartBridge extends Activity {


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SysUtils.openLaunchApp(this, getPackageName());
        Log.i("AppRestartBridge----> 启动");
        finish();
        android.os.Process.killProcess(android.os.Process.myPid());
        System.exit(10);
    }


}
