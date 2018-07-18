package xin.framework.widget.navigation;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;

/**
 * 作者：xin on 2018/7/18 16:01
 * <p>
 * 邮箱：ittfxin@126.com
 */
public class ViewPagerSlide extends ViewPager {
    public ViewPagerSlide(@NonNull Context context) {
        super(context);
    }

    public ViewPagerSlide(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }


    //是否可以进行滑动
    private boolean isSlide = true;

    public void setSlide(boolean slide) {
        isSlide = slide;
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        return isSlide;
    }
}
