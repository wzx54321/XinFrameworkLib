package xin.framework.configs;

import android.app.Activity;
import android.os.Environment;
import com.yanzhenjie.permission.Action;
import com.yanzhenjie.permission.AndPermission;
import com.yanzhenjie.permission.Permission;
import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import xin.framework.utils.common.utils.io.FileUtil;
import xin.framework.utils.common.utils.io.SdCardUtil;

import java.io.File;
import java.util.List;

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


        AndPermission.with(activity).runtime().permission(Permission.Group.STORAGE)
                .onDenied(new Action<List<String>>() {
                    @Override
                    public void onAction(List<String> data) {
                        // 没有创建权限
                        if (mOnFileCreatedListener != null)
                            mOnFileCreatedListener.onFailure();
                    }
                }).onGranted(new Action<List<String>>() {


            @Override
            public void onAction(List<String> data) {
                createParentDir();
            }
        }).start();


    }

    private static void createParentDir() {
        Observable.create(new ObservableOnSubscribe<Object>() {
            @Override
            public void subscribe(ObservableEmitter<Object> emitter) throws Exception {
                boolean isCreated = false;
                if (SdCardUtil.isSdCardAvailable()) {// 创建外置根目录,.nomedia文件
                    isCreated = FileUtil.createNewFileAndParentDir(new File(getPublicDir(DIR_PUBLIC_ROOT),
                            ".nomedia"));
                }
                if (isCreated)
                    emitter.onNext(new Object());
                emitter.onComplete();
            }
        }).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<Object>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(Object o) {
                        if (mOnFileCreatedListener != null)
                            mOnFileCreatedListener.onCreated();
                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onComplete() {
                        if (mOnFileCreatedListener != null)
                            mOnFileCreatedListener.onFailure();
                    }
                });

        new Thread(new Runnable() {

            @Override
            public void run() {


            }
        }).start();


    }

    static OnFileCreatedListener mOnFileCreatedListener;

    public interface OnFileCreatedListener {
        void onCreated();

        void onFailure();
    }


}
