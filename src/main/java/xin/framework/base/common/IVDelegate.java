package xin.framework.base.common;

/**
 * 处理生命周期的一些操作和View成相关接口,自行定制添加
 * <p>
 * 如： 弹窗/tost
 * </P>
 */
public interface IVDelegate {


    void toastShort(String msg);

    void toastLong(String msg);
}
