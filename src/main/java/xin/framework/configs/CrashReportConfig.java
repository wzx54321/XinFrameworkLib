package xin.framework.configs;

import android.content.Context;

import xin.framework.utils.android.ContextUtils;
import xin.framework.utils.android.Loger.CrashLogger;


/**
 * Description :Crash报告配置
 * Created by xin on 2017/8/17 0017.
 */

public class CrashReportConfig {


    public static void init(Context app) {

        Thread.currentThread()
                .setUncaughtExceptionHandler(new CrashLogger(ContextUtils.getContext()));

    }
}
