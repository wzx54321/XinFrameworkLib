package xin.framework.base.common;

import android.content.Context;
import android.widget.Toast;


/**
 * 处理生命周期的一些操作和View成相关接口
 * <p>
 * 如：统计、弹窗/tost
 * </P>
 */
public class VDelegateImpl implements IVDelegate {

    private Context context;

    private VDelegateImpl(Context context) {
        this.context = context;
    }

    public static IVDelegate onCreate(Context context) {
        return new VDelegateImpl(context);
    }




    @Override
    public void toastShort(String msg) {
        Toast.makeText(context, msg, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void toastLong(String msg) {
        Toast.makeText(context, msg, Toast.LENGTH_LONG).show();
    }
}
