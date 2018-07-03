package xin.framework.base.fragment;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.trello.rxlifecycle2.components.support.RxFragment;

import xin.framework.base.common.IVDelegate;
import xin.framework.base.common.VDelegateImpl;
import xin.framework.base.fragment.utils.TransactionRecord;
import xin.framework.mvp.mvp.IPresent;
import xin.framework.mvp.mvp.IView;
import xin.framework.utils.android.view.compatibility.title.StatusBarUtil;

/**
 * fragment基类
 * @param <P>
 */
public abstract class XinFragment<P extends IPresent> extends RxFragment implements IView<P> {
    private P p;
    @Nullable
    protected View mRootView;
    private IVDelegate delegateHandler;
    TransactionRecord mTransactionRecord;


    /**
     * 自动适配状态title的padding
     *
     * @param titleView
     */
    public void setTitleViewPadding(View titleView) {

        StatusBarUtil.setPaddingSmart(getContext(), titleView);
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        // 创建统一处理
        delegateHandler = VDelegateImpl.onCreate(activity);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle bundle = getArguments();
        if (bundle != null && bundle.size() > 0) {
            initVariables(bundle);
        }

    }

    @Override
    public void initData(Bundle savedInstanceState) {
        // 什么都没做
    }

    /**
     * 初始化Bundle传的数据
     */
    protected abstract void initVariables(Bundle bundle);


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (mRootView == null && getLayoutId() > 0) {
            mRootView = inflater.inflate(getLayoutId(), null);
        } else {
            ViewGroup viewGroup = (ViewGroup) mRootView.getParent();
            if (viewGroup != null) {
                viewGroup.removeView(mRootView);
            }
        }
        isFirstLoad = true;
        bindUI(mRootView);
        isPrepared = true;
        lazyLoad();
        return mRootView;
    }


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

    }


    @Override
    public void onDetach() {
        super.onDetach();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();

        if (getP() != null) {
            getP().detachV();
        }
        p = null;
        isPrepared = false;
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        delegateHandler = null;
    }

    protected P getP() {
        if (p == null) {
            p = newP();
            if (p != null) {
                p.attachV(this);
            }
        }
        return p;
    }

    public IVDelegate getDelegateHandler() {
        return delegateHandler;
    }

    public TransactionRecord getmTransactionRecord() {
        return mTransactionRecord;
    }

    public void setmTransactionRecord(TransactionRecord mTransactionRecord) {
        this.mTransactionRecord = mTransactionRecord;
    }


    //***************************************懒加载处理*********************************************************************//

    /**
     * 是否可见状态
     */
    private boolean isFragmentVisible;
    /**
     * 标志位，View已经初始化完成。
     */
    private boolean isPrepared;
    /**
     * 是否第一次加载
     */
    private boolean isFirstLoad = true;
    /**
     * <pre>
     * 默认是懒加载,如果设置为true会
     * 忽略isFirstLoad的值，强制刷新数据，但仍要Visible & Prepared
     * 一般用于PagerAdapter需要刷新各个子Fragment的场景
     * 不要new 新的 PagerAdapter 而采取reset数据的方式
     * 所以要求Fragment重新走 {@link XinFragment#bindDataFirst()}
     * 故使用 {@link XinFragment#setNotLazyMode(boolean)}
     * </pre>
     */
    private boolean notLazyMode = false;


    /**
     * 如果是与ViewPager一起使用，调用的是setUserVisibleHint
     *
     * @param isVisibleToUser 是否显示出来了
     */
    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (getUserVisibleHint()) {
            onVisible();
        } else {
            onInvisible();
        }
    }


    /**
     * 如果是通过FragmentTransaction的show和hide的方法来控制显示，调用的是onHiddenChanged.
     * 若是初始就show的Fragment 为了触发该事件 需要先hide再show
     *
     * @param hidden hidden True if the fragment is now hidden, false if it is not
     *               visible.
     */
    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if (!hidden) {
            onVisible();
        } else {
            onInvisible();
        }
    }


    protected void onVisible() {
        isFragmentVisible = true;
        lazyLoad();
    }

    protected void onInvisible() {
        isFragmentVisible = false;
    }


    /**
     * 要实现延迟加载Fragment内容,需要在 onCreateView
     * isPrepared = true;
     */
    protected void lazyLoad() {
        if (isPrepared() && isFragmentVisible()) {
            if (notLazyMode || isFirstLoad()) {
                notLazyMode = false;
                isFirstLoad = false;
                bindDataFirst();
            }
        }
    }

    protected abstract void bindDataFirst();

    public boolean isPrepared() {
        return isPrepared;
    }

    /**
     * 忽略isFirstLoad的值，强制刷新数据，但仍要Visible & Prepared
     */
    public void setNotLazyMode(boolean notLazyMode) {
        this.notLazyMode = notLazyMode;
    }

    public boolean isFirstLoad() {
        return isFirstLoad;
    }

    public boolean isFragmentVisible() {
        return isFragmentVisible;
    }
}
