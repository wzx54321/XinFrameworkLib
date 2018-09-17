package xin.framework.mvp;

import android.os.Bundle;
import android.view.View;

/**
 * V层
 *
 * @param <P>
 */
public interface IView<P> {


    /**
     * 如果使用了三方注解绑定UI的SDK可在该方法内调用
     *
     * @param rootView 外部父布局
     */
    void bindUI(View rootView);

    /**
     * 初始化数据
     */
    void initData(Bundle savedInstanceState);


    /**
     * @return 初始化布局的ID
     */
    int getLayoutId();


    /**
     * @return 绑定的Presenter
     */
    P newP();
}
