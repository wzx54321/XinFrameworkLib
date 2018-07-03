package xin.framework.utils.android.Loger;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Environment;

import java.io.File;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.lang.Thread.UncaughtExceptionHandler;
import java.text.SimpleDateFormat;
import java.util.Date;

import xin.framework.configs.FileConfig;
import xin.framework.utils.common.utils.DateUtil;
import xin.framework.utils.common.utils.io.FileUtil;

/**
 * 描述： 未捕获异常
 *
 * @Author xin
 * @since JDK1.8
 */
@SuppressLint("SimpleDateFormat")
public class CrashLogger implements
        UncaughtExceptionHandler {

    private Context mContext;

    public CrashLogger(Context mContext) {
        super();
        this.mContext = mContext;
    }

    @SuppressLint("SimpleDateFormat")
    @Override
    public void uncaughtException(Thread thread,
                                  Throwable ex) {
        File file;
        try {
            if (isSdCardAvailable()) {
                file = new File(FileUtil.getPublicDir(FileConfig.DIR_CRASH),
                        "crash-app-" + new SimpleDateFormat(DateUtil.Format.CN_DEFAULT_FORMAT).format(new Date()) + FileConfig.FILE_NAME_EXTENSION_LOG);
            } else {// Android/data/
                file = new File(FileUtil.getDiskCacheDir(mContext,
                        "/crash"),
                        "crash-app-" + new SimpleDateFormat(DateUtil.Format.CN_DEFAULT_FORMAT).format(new Date()) + FileConfig.FILE_NAME_EXTENSION_LOG);
            }
            StringWriter sw = new StringWriter();
            PrintWriter pw = new PrintWriter(sw);
            ex.printStackTrace(pw);
            String error = sw.toString();
            Log.e("------------------------------------uncaughtException ------------------------------------:/n",
                    error + "\n ----------------------------------------------------------------------------------");


            FileUtil.writeStringToFile(file,
                    error,
                    false);
            Thread.getDefaultUncaughtExceptionHandler()
                    .uncaughtException(thread,
                            ex);
        } catch (Exception e) {
            Log.e(e, "", "");
        }
    }


    /**
     * is sd card available.
     *
     * @return true if available
     */
    private boolean isSdCardAvailable() {
        return Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState());
    }
}
