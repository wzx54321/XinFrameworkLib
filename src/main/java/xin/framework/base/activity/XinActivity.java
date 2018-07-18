package xin.framework.base.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;

import com.trello.rxlifecycle2.components.support.RxAppCompatActivity;

import xin.framework.base.common.IVDelegate;
import xin.framework.base.common.VDelegateImpl;
import xin.framework.mvp.mvp.IPresent;
import xin.framework.mvp.mvp.IView;
import xin.framework.utils.android.view.compatibility.title.StatusBarUtil;

/**
 * activity基类
 * <p>
 * 邮箱：ittfxin@126.com
 * <p>
 * https://github.com/wzx54321/XinFrameworkLib
 * <p>
 *
 * @param <P>
 */
public abstract class XinActivity<P extends IPresent> extends RxAppCompatActivity implements IView<P> {

    private P p;
    private IVDelegate delegateHandler;


    @Override
    public void onAttachedToWindow() {
        super.onAttachedToWindow();
        // 默认使用沉浸式的状态栏
        StatusBarUtil.immersive(getWindow());
    }


    /**
     * 设置状态栏字体和图标颜色并实现沉浸式
     */
    public void setStatusDarkMode() {
        StatusBarUtil.darkMode(this);
    }


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // 创建统一处理
        delegateHandler = VDelegateImpl.onCreate(this);

        prepareData();
        if (getLayoutId() > 0) {
            View rootView = LayoutInflater.from(this).inflate(getLayoutId(), null, false);
            setContentView(rootView);
            bindUI(rootView);
        }
        initData(savedInstanceState);

    }

    /**
     * 在UI之前准备数据
     */
    public void prepareData() {
    }


    @Override
    public void bindUI(View rootView) {
        //unBinder = KnifeKit.bind(this);
    }


    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }


    @SuppressWarnings("unchecked")
    protected P getP() {
        if (p == null) {
            p = newP();
            if (p != null) {
                p.attachV(this);
            }
        }
        return p;
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();

        if (getP() != null) {
            getP().detachV();
        }
        p = null;
        delegateHandler = null;
    }


    public IVDelegate getDelegateHandler() {
        return delegateHandler;
    }

}
