
package xin.framework.utils.common.utils;

import android.support.annotation.NonNull;
import android.text.TextUtils;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.List;
import java.util.Map;

import xin.framework.utils.android.Loger.Log;


public class HttpUtils {
    /**
     * 将传递进来的参数拼接成 url
     */
    public static String createUrlFromParams(String url, Map<String, List<String>> params) {
        try {
            StringBuilder sb = new StringBuilder();
            sb.append(url);
            if (url.indexOf('&') > 0 || url.indexOf('?') > 0) sb.append("&");
            else sb.append("?");
            for (Map.Entry<String, List<String>> urlParams : params.entrySet()) {
                List<String> urlValues = urlParams.getValue();
                for (String value : urlValues) {
                    //对参数进行 utf-8 编码,防止头信息传中文
                    String urlValue = URLEncoder.encode(value, "UTF-8");
                    sb.append(urlParams.getKey()).append("=").append(urlValue).append("&");
                }
            }
            sb.deleteCharAt(sb.length() - 1);
            return sb.toString();
        } catch (UnsupportedEncodingException e) {
            Log.e(e, "HttpUtils#createUrlFromParams fail");
        }
        return url;
    }


    /**
     * 将传递进来的参数拼接成  key 样式如：?data=sdsds&name=haha ...
     */
    public static String createParams(String url, @NonNull Map<String, String> params) {
        try {
            StringBuilder sb = new StringBuilder();
            if (url.indexOf('&') > 0 || url.indexOf('?') > 0) sb.append("&");
            else sb.append("?");
            for (Map.Entry<String, String> urlParams : params.entrySet()) {
                String urlValues = urlParams.getValue();
                //对参数进行 utf-8 编码,防止头信息传中文
                String urlValue = URLEncoder.encode(urlValues, "UTF-8");
                sb.append(urlParams.getKey()).append("=").append(urlValue).append("&");

            }
            sb.deleteCharAt(sb.length() - 1);
            return sb.toString();
        } catch (UnsupportedEncodingException e) {
            Log.e(e, "HttpUtils#createUrlFromParams fail");
        }
        return "";
    }







    /**
     * 通过 ‘？’ 和 ‘/’ 判断文件名
     */
    private static String getUrlFileName(String url) {
        String filename = null;
        String[] strings = url.split("/");
        for (String string : strings) {
            if (string.contains("?")) {
                int endIndex = string.indexOf("?");
                if (endIndex != -1) {
                    filename = string.substring(0, endIndex);
                    return filename;
                }
            }
        }
        if (strings.length > 0) {
            filename = strings[strings.length - 1];
        }
        return filename;
    }

    /**
     * 根据路径删除文件
     */
    public static boolean deleteFile(String path) {
        if (TextUtils.isEmpty(path)) return true;
        File file = new File(path);
        if (!file.exists()) return true;
        if (file.isFile()) {
            boolean delete = file.delete();
            Log.e("HttpUtils#" + "deleteFile:" + delete + " path:" + path);

            return delete;
        }
        return false;
    }



    public static <T> T checkNotNull(T object, String message) {
        if (object == null) {
            throw new NullPointerException(message);
        }
        return object;
    }

}
