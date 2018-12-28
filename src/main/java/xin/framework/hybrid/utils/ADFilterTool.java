package xin.framework.hybrid.utils;

import android.content.Context;
import android.content.res.Resources;

import xin.framework.R;

/**
 * 广告过滤
 */
public class ADFilterTool {
    public static String getClearAdDivJs(Context context) {
        StringBuilder js = new StringBuilder("javascript:");
        Resources res = context.getResources();
        String[] adDivs = res.getStringArray(R.array.adBlockDiv);
        for (int i = 0; i < adDivs.length; i++) {

            js.append("var adDiv").append(i).append("= document.getElementById('").append(adDivs[i]).append("');if(adDiv").append(i).append(" != null)adDiv").append(i).append(".parentNode.removeChild(adDiv").append(i).append(");");
        }
        return js.toString();
    }
}