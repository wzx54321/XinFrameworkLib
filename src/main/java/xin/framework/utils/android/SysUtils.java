package xin.framework.utils.android;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.v4.app.ActivityCompat;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.lang.reflect.Method;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.util.Enumeration;
import java.util.List;
import java.util.UUID;

import xin.framework.utils.android.Loger.Log;

/**
 * 系统相关工具
 * <p>
 * <p>
 * 邮箱：ittfxin@126.com
 * <p>
 * https://github.com/wzx54321/XinFrameworkLib
 */
public class SysUtils {

    /**
     * 获取手机ip地址
     *
     * @return ip地址
     */
    public static String getPhoneIp() {
        try {
            for (Enumeration<NetworkInterface> en = NetworkInterface.getNetworkInterfaces(); en
                    .hasMoreElements(); ) {
                NetworkInterface anInterface = en.nextElement();
                for (Enumeration<InetAddress> enumIpAddr = anInterface.getInetAddresses(); enumIpAddr
                        .hasMoreElements(); ) {
                    InetAddress inetAddress = enumIpAddr.nextElement();
                    if (!inetAddress.isLoopbackAddress() && inetAddress instanceof Inet4Address) {
                        Log.i("IP info:" + inetAddress.getHostAddress(), "");
                        return inetAddress.getHostAddress();
                    }
                }
            }
        } catch (Exception e) {
            Log.printStackTrace(e);
        }
        return "";
    }

    /**
     * 获取设备唯一标识
     *
     * @param ctx 上下文
     * @return 设备唯一标识
     */
    @SuppressLint("HardwareIds")
    public static String getDeviceId(Context ctx) {
        String deviceId1;
        String deviceId2;
        TelephonyManager telephonyManager;
        try {
            telephonyManager = (TelephonyManager) ctx.getSystemService(Context.TELEPHONY_SERVICE);
            // MTK平台获取方式，如果不是此平台，则是高通平台
            deviceId1 = getOperatorBySlot(telephonyManager, "getDeviceIdGemini", 0);
            deviceId2 = getOperatorBySlot(telephonyManager, "getDeviceIdGemini", 1);
            // 如果获取MTK平台失败，则获取高通平台的
            if (TextUtils.isEmpty(deviceId1) && TextUtils.isEmpty(deviceId2)) {
                deviceId1 = getOperatorBySlot(telephonyManager, "getDeviceId", 0);
                deviceId2 = getOperatorBySlot(telephonyManager, "getDeviceId", 1);
            }
            // 优先卡1，如果卡1获取不到，则用卡2，都获取不到，则用设备id,最后考虑自动生成
            if (!TextUtils.isEmpty(deviceId1)) {
                return deviceId1;
            } else if (!TextUtils.isEmpty(deviceId2)) {
                return deviceId2;
            } else {
                if (telephonyManager != null) {

                    // 需要获取权限
                    if (ActivityCompat.checkSelfPermission(ctx, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
                        deviceId1 = telephonyManager.getDeviceId();

                        if (TextUtils.isEmpty(deviceId1)) {
                            deviceId1 = SysUtils.hasO() ? Build.getSerial() : Build.SERIAL;

                        }
                    }
                }
                if (!TextUtils.isEmpty(deviceId1)
                        && !TextUtils.equals(deviceId1, "000000000000000") && !TextUtils.equals(deviceId1, "9774d56d682e549c")/*厂商定制系统的Bug*/) {
                    return deviceId1;
                }
            }
        } catch (Exception e2) {
            Log.printStackTrace(e2);
        }
        return UUID.randomUUID().toString();
    }


    /**
     * 获取版本号
     */

    public static String getVersion(Context context) {
        String name = "3.0";
        try {
            PackageInfo pi = context.getPackageManager().getPackageInfo(context.getPackageName(),
                    0);
            return pi.versionName;
        } catch (PackageManager.NameNotFoundException e) {
            return name;
        }
    }

    /**
     * 获取build号
     */
    public static String getVersionCode(Context context) throws PackageManager.NameNotFoundException {

        PackageInfo pi = context.getPackageManager().getPackageInfo(context.getPackageName(),
                0);
        return pi.versionCode + "";
    }

    /**
     * 获取设备名称
     */
    @SuppressWarnings("SameReturnValue")
    public static String getDeviceName() {
        return Build.MODEL;
    }


    /**
     * 判断是否大于Honeycomb 3.0
     */
    @SuppressLint("ObsoleteSdkInt")
    public static boolean hasHoneycomb() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB;
    }


    /**
     * 判断是否大于JELLY_BEAN_MR1 4.2
     */
    public static boolean hasJellyBeanMr1() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1;
    }

    /**
     * 判断是否大于KITKAT 4.4
     */
    public static boolean hasKitKat() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT;
    }


    /**
     * 判断是否大于LOLLIPOP 5.0
     */
    public static boolean hasLollipop() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP;
    }

    /**
     * 判断是否大于M 6.0
     */
    public static boolean hasM() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.M;
    }

    /**
     * 判断是否大于M 7.0
     */
    public static boolean hasNougat() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.N;
    }

    /**
     * 判断是否大于M 8.0
     */
    public static boolean hasO() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.O;
    }

    /**
     * 获取当前进程名字
     */
    public static String getCurProcessName(Context ctx) {
        int pid = android.os.Process.myPid();
        ActivityManager activityManager = (ActivityManager) ctx.getSystemService(Context.ACTIVITY_SERVICE);
        if (activityManager == null || activityManager.getRunningAppProcesses() == null) {
            return null;
        }
        for (ActivityManager.RunningAppProcessInfo processInfo : activityManager.getRunningAppProcesses()) {
            if (processInfo.pid == pid) {
                return processInfo.processName;
            }
        }
        return null;
    }


    /**
     * 获取进程号对应的进程名
     *
     * @param pid 进程号
     * @return 进程名
     */
    public static String getProcessName(int pid) {
        BufferedReader reader = null;
        try {
            reader = new BufferedReader(new FileReader("/proc/" + pid + "/cmdline"));
            String processName = reader.readLine();
            if (!TextUtils.isEmpty(processName)) {
                processName = processName.trim();
            }
            return processName;
        } catch (Throwable throwable) {
            Log.printStackTrace(throwable);
        } finally {
            try {
                if (reader != null) {
                    reader.close();
                }
            } catch (IOException exception) {
                Log.printStackTrace(exception);
            }
        }
        return null;
    }

    /**
     * whether this process is named with processName
     *
     * @param context
     * @param processName
     * @return <ul>
     * return whether this process is named with processName
     * <li>if context is null, return false</li>
     * <li>if {@link ActivityManager#getRunningAppProcesses()} is null, return false</li>
     * <li>if one process of {@link ActivityManager#getRunningAppProcesses()} is equal to processName, return
     * true, otherwise return false</li>
     * </ul>
     */
    public static boolean isNamedProcess(Context context, String processName) {
        if (context == null) {
            return false;
        }

        int pid = android.os.Process.myPid();
        ActivityManager manager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningAppProcessInfo> processInfoList = manager == null ? null : manager.getRunningAppProcesses();
        if (processInfoList == null) {
            return true;
        }

        for (ActivityManager.RunningAppProcessInfo processInfo : manager.getRunningAppProcesses()) {
            if (processInfo.pid == pid && processInfo.processName.equals(processName)) {
                return true;
            }
        }
        return false;
    }


    private static String getOperatorBySlot(TelephonyManager telephony, String predictedMethodName,
                                            int slotID) {
        if (telephony == null) {
            return null;
        }
        String inumeric = null;
        try {
            Class<?> telephonyClass = Class.forName(telephony.getClass().getName());
            Class<?>[] parameter = new Class[1];
            parameter[0] = int.class;
            Method getSimID = telephonyClass.getMethod(predictedMethodName, parameter);
            Object[] obParameter = new Object[1];
            obParameter[0] = slotID;
            Object ob_phone = getSimID.invoke(telephony, obParameter);
            if (ob_phone != null) {
                inumeric = ob_phone.toString();
            }
        } catch (Exception e) {
            //   Log.printStackTrace(e);
        }
        return inumeric;
    }

    public static void dismissKeyBoard(Context context) {
        View view = ((Activity) context).getWindow().peekDecorView();
        if (view != null) {
            InputMethodManager inputManger = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
            inputManger.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }


    /**
     * 启动应用入口Activity
     *
     * @param activity    从activity启动
     * @param packageName 启动activity的包名
     */
    public static void openLaunchApp(Activity activity, String packageName) {
        Intent intent = activity.getPackageManager().getLaunchIntentForPackage(packageName);
        activity.startActivity(intent);
    }


    /**
     * 隐藏虚拟按键，并且全屏
     */
    public static void hideBottomUIMenu(Activity activity) {
        //隐藏虚拟按键，并且全屏
        if ( Build.VERSION.SDK_INT < 19) { // lower api
            View v = activity.getWindow().getDecorView();
            v.setSystemUiVisibility(View.GONE);
        } else {
            //for new api versions.
            View decorView = activity.getWindow().getDecorView();
            int uiOptions = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                    | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY | View.SYSTEM_UI_FLAG_FULLSCREEN;
            decorView.setSystemUiVisibility(uiOptions);
        }

    }

    /**
     * 隐藏虚拟按键，并且全屏
     */
    public static void hideBottomUIMenu(View view) {
        //隐藏虚拟按键，并且全屏
        if ( Build.VERSION.SDK_INT < 19) { // lower api
            view.setSystemUiVisibility(View.GONE);
        } else {
            //for new api versions.

            int uiOptions = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                    | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY | View.SYSTEM_UI_FLAG_FULLSCREEN;
            view.setSystemUiVisibility(uiOptions);
        }

    }
}