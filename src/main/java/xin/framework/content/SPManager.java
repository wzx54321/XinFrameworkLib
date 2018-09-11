package xin.framework.content;

import android.annotation.SuppressLint;

import xin.framework.utils.android.ContextUtils;

/**
 * Description :SharedPreferences
 * Created by xin on 2017/5/16 0016.
 * <p>
 * 邮箱：ittfxin@126.com
 * <p>
 * https://github.com/wzx54321/XinFrameworkLib
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
    @SuppressWarnings("UnusedReturnValue")
    @SuppressLint("HardwareIds")
    public boolean putDeviceId(String deviceId) {
        return mDk.put(KEY_PHONE_DEVICE_ID, deviceId).commit();


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
    public void putPatternPSW(String encryptPwd) {

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
