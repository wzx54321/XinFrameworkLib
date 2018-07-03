package xin.framework.content;

import android.annotation.SuppressLint;
import android.text.TextUtils;

import java.util.UUID;

import xin.framework.utils.android.ContextUtils;

/**
 * Description :SharedPreferences
 * Created by xin on 2017/5/16 0016.
 */

public class SPManager implements SharedPreferencesKeys {


    private DataKeeper mDk;
    private static SPManager spManager;

    private SPManager() {
        mDk = new DataKeeper(ContextUtils.getContext(), spFileName);
    }

    public static SPManager getInstance() {
        if (spManager == null) {
            synchronized (SPManager.class) {
                if (spManager == null) {
                    spManager = new SPManager();
                }
            }

        }

        return spManager;
    }


    /**
     * 获取设备ID
     *
     * @return 设备ID
     */
    public String getDeviceId() {
        return mDk.get(KEY_PHONE_DEVICE_ID, "");
    }

    /**
     * 保存设备ID
     *
     * @param deviceId 设备ID
     */
    @SuppressLint("HardwareIds")
    public boolean putDeviceId(String deviceId) {

        if (TextUtils.isEmpty(mDk.get(KEY_PHONE_DEVICE_ID, ""))) {
            // 某些设备在应用启动时会提示获取手机识别码安全限制，此时如果禁止权限则deviceId为空，无法进行登录,所以这种情况下取SERIAL作为设备唯一标识
            if (TextUtils.isEmpty(deviceId)) {
                deviceId = android.os.Build.SERIAL != null ? android.os.Build.SERIAL : UUID.randomUUID().toString();
            }
            return mDk.put(KEY_PHONE_DEVICE_ID, deviceId).commit();

        }
        return false;

    }


    /**
     * 登录的UID
     *
     * @return UserID
     */
    public String getUserID() {
        return mDk.get(KEY_USER_ID, "");
    }


    /**
     * 存UserID
     *
     * @param uid 用户ID
     */
    public boolean putUserId(String uid) {
        return mDk.put(KEY_USER_ID, uid).commit();
    }

    /**
     * @return 获取userTOKEN
     */
    public String getUserToken() {
        return mDk.get(KEY_USER_TOKEN, "");
    }


    /***
     *
     * @param token 登录使用的token
     */
    public boolean putUserToken(String token) {
        return mDk.put(KEY_USER_TOKEN, token).commit();
    }


    /**
     * 存手势密码
     *
     * @param encryptPwd
     * @return
     */
    public void   putPatternPSW(String encryptPwd) {

         mDk.put(KEY_GESTURE_PWD, encryptPwd).commit();

    }


    /**
     * 手势密码
     */
    public String getPatternPSW() {
        return mDk.get(KEY_GESTURE_PWD, "");
    }


    /**
     * 指纹密码
     *
     * @return
     */
    public void setHasFingerPrint(boolean isSet) {
          mDk.put(KEY_HAS_FINGERPRINT, isSet).apply();
    }

    /**
     * 指纹密码
     *
     * @return
     */
    public boolean getHasFingerPrint() {
        return mDk.get(KEY_HAS_FINGERPRINT, false);
    }
}
