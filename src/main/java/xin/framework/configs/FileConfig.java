package xin.framework.configs;

import android.app.Activity;
import android.os.Environment;

import com.tbruyelle.rxpermissions2.RxPermissions;

import java.io.File;

import xin.framework.utils.android.PermissionUtil;
import xin.framework.utils.android.SysUtils;
import xin.framework.utils.common.utils.io.FileUtil;
import xin.framework.utils.common.utils.io.SdCardUtil;

/**
 * Description : 文件路径配置
 * Created by xin on 2017/7/10 0010.
 * <p>
 * <p>
 * 邮箱：ittfxin@126.com
 * <p>
 * https://github.com/wzx54321/XinFrameworkLib
 */

@SuppressWarnings("WeakerAccess")
public class FileConfig {


    /**
     * 应用程序在SDCARD中的根目录，/mnt/sdcard/
     */
    public static String DIR_PUBLIC_ROOT = "xinFramework";

    /**
     * 崩溃日志存储目录
     */
    public static String DIR_CRASH = "crash";

    /**
     * 下载目录
     */
    public static String DIR_DOWNLOAD = "download";


    /**
     * webview 下载目录
     */
    public static String DIR_WEB_DOWNLOAD = "web_download";

    /**
     * webview 缓存
     */
    public static String DIR_WEB_CACHE = "web-cache";


    /**
     * webview 缓存
     */
    public static String DIR_WEB_SONIC_CACHE = "web-sonic-ache";


    /**
     * 日志文件名后缀
     */
    public static String FILE_NAME_EXTENSION_LOG = ".log";
    /**
     * jpg文件名后缀
     */
    public static String FILE_NAME_EXTENSION_JPG = ".jpg";
    /**
     * WEBP文件名后缀
     */
    public static String FILE_NAME_EXTENSION_WEBP = ".WEBP";

    /**
     * png文件名后缀
     */
    public static String FILE_NAME_EXTENSION_PNG = ".png";
    /**
     * apk文件名后缀
     */
    public static String FILE_NAME_EXTENSION_APK = ".apk";

    /**
     * json文件名后缀
     */
    public static String FILE_NAME_EXTENSION_JSON = ".json";

    /**
     * 获取 SDCARD上的存储根目录（如果登录过，会自动追加加userId的子目录）<br>
     * ，/mnt/sdcard/XinFramework/
     *
     * @param type
     * @return
     */
    public static File getPublicDir(String type) {
        if (DIR_PUBLIC_ROOT.equals(type)) {
            return Environment.getExternalStoragePublicDirectory(FileConfig.DIR_PUBLIC_ROOT);
        } else {
            return new File(Environment.getExternalStoragePublicDirectory(FileConfig.DIR_PUBLIC_ROOT),
                    type);
        }
    }


    public static File getPublicFile(String dirName, String fileName) {
        return new File(getPublicDir(dirName),
                fileName);
    }

    public void init(final Activity activity, OnFileCreatedListener onFileCreatedListener) {

        mOnFileCreatedListener = onFileCreatedListener;

        if (SysUtils.hasLollipop()) {

            // 权限处理
            PermissionUtil.externalStorage(new PermissionUtil.PermissionCallback() {
                @Override
                public void onRequestPermissionSuccess() {
                    createParentDir();
                    if (mOnFileCreatedListener != null)
                        mOnFileCreatedListener.onCreated();
                }

                @Override
                public void onRequestPermissionFailure() {
                    // 没有创建权限
                    if (mOnFileCreatedListener != null)
                        mOnFileCreatedListener.onFailure();
                }

                @Override
                public void onRequestError() {
                    // 没有创建权限
                    if (mOnFileCreatedListener != null)
                        mOnFileCreatedListener.onFailure();
                }
            }, new RxPermissions(activity));
        } else {
            createParentDir();
            if (mOnFileCreatedListener != null)
                mOnFileCreatedListener.onCreated();
        }


    }

    private static void createParentDir() {

        new Thread(new Runnable() {

            @Override
            public void run() {

                if (SdCardUtil.isSdCardAvailable()) {// 创建外置根目录,.nomedia文件
                    FileUtil.createNewFileAndParentDir(new File(getPublicDir(DIR_PUBLIC_ROOT),
                            ".nomedia"));
                }
            }
        }).start();


    }

    OnFileCreatedListener mOnFileCreatedListener;

    public interface OnFileCreatedListener {
        void onCreated();

        void onFailure();
    }


}
