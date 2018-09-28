package xin.framework.utils.android.view;

import android.support.annotation.DrawableRes;

import xin.framework.utils.android.ContextUtils;

/**
 * 作者：xin on 2018/9/21 11:45
 * <p>
 * 邮箱：ittfxin@126.com
 */
public class ResFinder {


    /**
     * 获取
     * @param idName
     * @return
     */
    public static  @DrawableRes int findDrawableResByName(String idName){

        return ContextUtils.getContext().getResources().getIdentifier(idName,"drawable",ContextUtils.getContext().getPackageName());
    }
}
