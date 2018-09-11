package xin.framework.utils.android;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.support.v4.content.FileProvider;

import com.tbruyelle.rxpermissions2.RxPermissions;

import java.io.File;

/**
 * 作者：xin on 2018/9/11 11:37
 * <p>
 * 邮箱：ittfxin@126.com
 * <p>
 * <p>
 * .apk 操作相关方法
 * <p>
 * 8.0以上模拟器安装需要使用权限
 * <uses-permission android:name="android.permission.REQUEST_INSTALL_PACKAGES" />
 * </P>
 */
public class APkUtil {

    /**
     * 安装应用
     *
     * @param appFile  /storage/emulated/0/Android/data/<package_name>/files/apk/ 下的apk
     *                 <P>或根据需求自定义位置，但是要与xml/common_file_paths.xml的配置路径一致，请自行配置
     */
    public static boolean installApp(final Activity activity, final File appFile) {
        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) { //版本判断
                boolean b = activity.getApplication().getPackageManager().canRequestPackageInstalls();
                if (b) {
                    toInstallApp(activity.getApplicationContext(), appFile); //有权限就直接安装
                } else {

                    PermissionUtil.installApp(new PermissionUtil.PermissionCallback() {
                        @Override
                        public void onRequestPermissionSuccess() {
                            toInstallApp(activity.getApplicationContext(), appFile);
                        }

                        @Override
                        public void onRequestPermissionFailure() {

                        }

                        @Override
                        public void onRequestError() {

                        }
                    },new RxPermissions(activity) );


                }
            } else {
                toInstallApp(activity.getApplicationContext(), appFile);
            }
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    private static void toInstallApp(Context context, File appFile) {
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) { //此处涉及到的android7.0的适配
            Uri fileUri = FileProvider.getUriForFile(context, context.getApplicationContext().getPackageName() + ".fileProvider", appFile);
            intent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            intent.setDataAndType(fileUri, "application/vnd.android.package-archive");
        } else {
            intent.setDataAndType(Uri.fromFile(appFile), "application/vnd.android.package-archive");
        }
        if (context.getPackageManager().queryIntentActivities(intent, 0).size() > 0) {
            context.startActivity(intent);
        }
    }

}
