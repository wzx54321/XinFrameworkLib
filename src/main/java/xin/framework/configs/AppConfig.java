package xin.framework.configs;

import android.content.Context;
import android.text.TextUtils;

import xin.framework.content.SPManager;
import xin.framework.utils.android.SysUtils;


/**
 * Description :App配置
 * Created by xin on 2017/8/22 0022.
 */

public class AppConfig {
    public static void setDeviceId(Context app) {
        if (TextUtils.isEmpty(SPManager.getInstance().getDeviceId())) {
            SPManager.getInstance().putDeviceId(SysUtils.getDeviceId(app));
        }
    }


}
