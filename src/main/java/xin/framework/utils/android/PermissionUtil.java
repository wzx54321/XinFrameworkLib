package xin.framework.utils.android;

import android.Manifest;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;

import com.tbruyelle.rxpermissions2.RxPermissions;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;
import xin.framework.utils.android.Loger.Log;


/**
 * Created by jess on 17/10/2016 10:09
 * <P>
 *  modify by xin
 */

@SuppressWarnings("WeakerAccess")
public class PermissionUtil {
    public static final String TAG = "Permission";


    private PermissionUtil() {
    }

    public interface PermissionCallback {
        void onRequestPermissionSuccess();

        void onRequestPermissionFailure();

        void onRequestError();
    }


    public static void requestPermission(@NonNull final PermissionCallback requestPermission, RxPermissions rxPermissions, String... permissions) {
        if (permissions == null || permissions.length == 0) return;

        List<String> needRequest = new ArrayList<>();
        for (String permission : permissions) { //过滤调已经申请过的权限
            if (!rxPermissions.isGranted(permission)) {
                needRequest.add(permission);
            }
        }
        if (needRequest.size() == 0) {//全部权限都已经申请过，直接执行操作
            requestPermission.onRequestPermissionSuccess();
        } else {//没有申请过,则开始申请

            rxPermissions.request(needRequest.toArray(new String[needRequest.size()])).subscribe(new Observer<Boolean>() {
                @Override
                public void onSubscribe(Disposable d) {

                }

                @Override
                public void onNext(Boolean granted) {
                    if (granted) {
                        Log.d("Request permissions success");
                        requestPermission.onRequestPermissionSuccess();
                    } else {
                        Log.d("Request permissions failure");
                        requestPermission.onRequestPermissionFailure();
                    }
                }

                @Override
                public void onError(Throwable e) {
                    Log.d("Request permissions error");
                    requestPermission.onRequestError();
                }

                @Override
                public void onComplete() {
                }
            });
        }

    }


    /**
     * 请求摄像头权限
     */
    public static void camera(PermissionCallback requestPermission, RxPermissions rxPermissions
    ) {
        requestPermission(requestPermission, rxPermissions,
                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.CAMERA);
    }


    /**
     * 请求外部存储的权限
     * <p>
     * permission:android.permission.READ_EXTERNAL_STORAGE
     * <p>
     * permission:android.permission.WRITE_EXTERNAL_STORAGE
     */
    @SuppressWarnings("SameParameterValue")
    public static void externalStorage(PermissionCallback requestPermission, RxPermissions rxPermissions) {
        requestPermission(requestPermission, rxPermissions,
                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.READ_EXTERNAL_STORAGE);
    }





    /**
     * 短信所有权限
     * <p>
     * permission:android.permission.READ_SMS
     * <p>
     * permission:android.permission.RECEIVE_WAP_PUSH
     * <p>
     * permission:android.permission.RECEIVE_MMS
     * <p>
     * permission:android.permission.RECEIVE_SMS
     * <p>
     * permission:android.permission.SEND_SMS
     * <p>
     * permission:android.permission.READ_CELL_BROADCASTS
     * <p>
     */
    public static void SmsAll(PermissionCallback requestPermission, RxPermissions rxPermissions) {
        requestPermission(requestPermission, rxPermissions,
                Manifest.permission.SEND_SMS
        );
    }


    /**
     * 请求打电话权限
     */
    public static void callPhone(PermissionCallback requestPermission, RxPermissions rxPermissions) {
        requestPermission(requestPermission, rxPermissions, Manifest.permission.CALL_PHONE);
    }


    /**
     * 请求获取手机状态的权限
     */
    public static void readPhoneState(PermissionCallback requestPermission, RxPermissions rxPermissions) {
        requestPermission(requestPermission, rxPermissions,
                Manifest.permission.READ_PHONE_STATE);
    }


    /**
     * 联系人权限
     */
    public static void contacts(PermissionCallback requestPermission, RxPermissions rxPermissions) {
        requestPermission(requestPermission, rxPermissions,
                Manifest.permission.GET_ACCOUNTS);
    }

    /**
     * 日历权限
     */
    public static void calendar(PermissionCallback requestPermission, RxPermissions rxPermissions) {
        requestPermission(requestPermission, rxPermissions
                , Manifest.permission.READ_CALENDAR,
                Manifest.permission.WRITE_CALENDAR);
    }

    /**
     * 位置权限：
     * <p>
     * permission:android.permission.ACCESS_FINE_LOCATION
     * <p>
     * permission:android.permission.ACCESS_COARSE_LOCATION
     */
    public static void location(PermissionCallback requestPermission, RxPermissions rxPermissions) {
        requestPermission(requestPermission, rxPermissions,
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION);
    }


    /**
     * 传感器权限
     */
    @RequiresApi(api = Build.VERSION_CODES.KITKAT_WATCH)
    public static void sensors(PermissionCallback requestPermission, RxPermissions rxPermissions) {
        requestPermission(requestPermission, rxPermissions,
                Manifest.permission.BODY_SENSORS);

    }


    /**
     * 麦克风权限
     */
    public static void microphone(PermissionCallback requestPermission, RxPermissions rxPermissions) {
        requestPermission(requestPermission, rxPermissions,
                Manifest.permission.RECORD_AUDIO);
    }


    /**
     * 安装权限
     */
    @RequiresApi(api = Build.VERSION_CODES.M)
    public static void installApp(PermissionCallback requestPermission, RxPermissions rxPermissions) {
        requestPermission(requestPermission, rxPermissions,
                Manifest.permission.REQUEST_INSTALL_PACKAGES);
    }

}

