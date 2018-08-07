package xin.framework.base.fragment.utils;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentHack;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;

import java.util.List;

import xin.framework.base.fragment.XinFragment;
import xin.framework.utils.android.Loger.Log;

/**
 * 作者：xin on 2018/7/2 0002 09:29
 * <p>
 * 邮箱：ittfxin@126.com
 * <p>
 * Fragment工具
 */

@SuppressWarnings("unchecked")
public class XinFragmentUtils {
    // Fragment容器布局ID参数
    static final String ARG_CONTAINER = "arg_container";
    static final String ARG_REPLACE = "arg_replace";
    static final String ARG_IS_SHARED_ELEMENT = "arg_is_shared_element";
    static final String ARG_CUSTOM_END_ANIM = "arg_custom_end_anim";
    static final String ARG_ROOT_STATUS = "arg_root_status";

    static final int TYPE_ADD = 0;
    static final int TYPE_REPLACE = 10;
    static final int TYPE_ADD_RESULT = 2;
    static final int TYPE_ADD_WITHOUT_HIDE = 3;
    static final int TYPE_REPLACE_DO_NOT_BACK = 14;

    static final int STATUS_UN_ROOT = 0;
    static final int STATUS_ROOT_ANIM_DISABLE = 1;
    static final int STATUS_ROOT_ANIM_ENABLE = 2;


    private FragmentManager fragmentManager;

    public XinFragmentUtils(FragmentActivity activity) {
        fragmentManager = activity.getSupportFragmentManager();
    }

    public XinFragmentUtils(Fragment fragment) {
        fragmentManager = fragment.getChildFragmentManager();
    }

    /**
     * 加载根Fragment, 即Activity内的第一个Fragment 或 Fragment内的第一个子Fragment
     */
    public void loadRootFragment(int containerId, XinFragment toFragment) {
        // addToBackStack true   allowAnimation false
        bindContainerId(containerId, toFragment);
        start(null, toFragment,
                toFragment.getClass().getName(),
                false,
                false,
                TYPE_REPLACE);
    }

    /**
     * 加载多个同级根Fragment,  QQ主页的场景
     */
    public void loadMultipleRootFragment(int containerId, int showPosition, List<XinFragment> fragments) {
        loadMultipleRootFragment(containerId, showPosition, fragments.toArray(new XinFragment[]{}));

    }

    /**
     * 加载多个同级根Fragment,  QQ主页的场景
     */
    public void loadMultipleRootFragment(int containerId, int showPosition, XinFragment... toFragments) {
        FragmentTransaction ft = fragmentManager.beginTransaction();
        int len = toFragments.length;
        Fragment to;
        for (int i = 0; i < len; i++) {
            to = toFragments[i];
            Bundle args = getArguments(to);
            args.putInt(ARG_ROOT_STATUS, STATUS_ROOT_ANIM_DISABLE);
            bindContainerId(containerId, to);
            String toName = to.getClass().getName();
            ft.add(containerId, to, toName);
            if (i != showPosition) {
                ft.hide(to);
            } else {
                ft.show(to);
            }
        }

        supportCommit(fragmentManager, ft);
        HideShowFragment(toFragments[showPosition]);
    }

    /**
     * show一个Fragment,hide其他同栈所有Fragment
     * 使用该方法时，要确保同级栈内无多余的Fragment,(只有通过loadMultipleRootFragment()载入的Fragment)
     * <p>
     * 建议使用更明确的{@link #showHideFragment(XinFragment, XinFragment)}
     *
     * @param showFragment 需要show的Fragment
     */
    public void showHideFragment(XinFragment showFragment) {
        showHideFragment(showFragment, null);
    }

    /**
     * 先隐藏再显示
     *
     */
    private void HideShowFragment(XinFragment xinFragment) {


        fragmentManager.beginTransaction().hide(xinFragment).commit();
        fragmentManager.beginTransaction().show(xinFragment).commit();
    }


    /**
     * show一个Fragment,hide一个Fragment ; 主要用于类似微信主页那种 切换tab的情况
     */
    @SuppressWarnings({"unchecked", "SameParameterValue"})
    public void showHideFragment(XinFragment showFragment, XinFragment hideFragment) {
        if (showFragment == hideFragment) return;
        FragmentTransaction ft = fragmentManager.beginTransaction().show(showFragment);
        if (hideFragment == null) {
            List<Fragment> fragmentList = FragmentHack.getActiveFragments(fragmentManager);
            if (fragmentList != null) {
                for (Fragment fragment : fragmentList) {
                    if (fragment != null && fragment != showFragment) {
                        ft.hide(fragment);
                    }
                }
            }
        } else {
            ft.hide(hideFragment);
        }
        supportCommit(fragmentManager, ft);
    }


    @SuppressWarnings("SameParameterValue")
    private void start(XinFragment fromFragment, XinFragment toFragment,
                       String toFragmentTag,
                       boolean unAddToBackStack,
                       boolean allowRootFragmentAnim,
                       int type) {
        FragmentTransaction ft = fragmentManager.beginTransaction();
        boolean addMode = (type == TYPE_ADD || type == TYPE_ADD_RESULT || type == TYPE_ADD_WITHOUT_HIDE);
        Bundle args = getArguments(toFragment);
        args.putBoolean(ARG_REPLACE, !addMode);
        TransactionRecord record = toFragment.getTransactionRecord();

        if (record != null && record.sharedElementList != null && !record.sharedElementList.isEmpty()) {
            args.putBoolean(ARG_IS_SHARED_ELEMENT, true);
            for (TransactionRecord.SharedElement item : record.sharedElementList) {
                ft.addSharedElement(item.sharedElement, item.sharedName);
            }
        } else {
            if (addMode) { // Replace mode forbidden animation, the replace animations exist overlapping Bug on support-v4.
                if (record != null && record.targetFragmentEnter != Integer.MIN_VALUE) {
                    ft.setCustomAnimations(record.targetFragmentEnter, record.currentFragmentPopExit,
                            record.currentFragmentPopEnter, record.targetFragmentExit);
                    args.putInt(ARG_CUSTOM_END_ANIM, record.targetFragmentEnter);
                } else {
                    ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
                }
            } else {
                args.putInt(ARG_ROOT_STATUS, STATUS_ROOT_ANIM_DISABLE);
            }
        }

        if (fromFragment == null) {
            ft.replace(args.getInt(ARG_CONTAINER), toFragment, toFragmentTag);
            if (!addMode) {
                ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
                args.putInt(ARG_ROOT_STATUS, allowRootFragmentAnim ?
                        STATUS_ROOT_ANIM_ENABLE : STATUS_ROOT_ANIM_DISABLE);
            }
        } else {
            Bundle agsFrom = getArguments(fromFragment);
            int fromContainerId = agsFrom.containsKey(ARG_CONTAINER)
                    ? agsFrom.getInt(ARG_CONTAINER) : args.getInt(ARG_CONTAINER);

            if (addMode) {
                ft.add(fromContainerId, toFragment, toFragmentTag);
                if (type != TYPE_ADD_WITHOUT_HIDE) {
                    ft.hide(fromFragment);
                }
            } else {
                ft.replace(fromContainerId, toFragment, toFragmentTag);
            }
        }

        if (!unAddToBackStack && type != TYPE_REPLACE_DO_NOT_BACK) {
            ft.addToBackStack(toFragmentTag);
        }
        supportCommit(fragmentManager, ft);
    }

    private void supportCommit(FragmentManager fragmentManager, FragmentTransaction transaction) {
        boolean stateSaved = FragmentHack.isStateSaved(fragmentManager);

        if (stateSaved) {
            transaction.commitAllowingStateLoss();
            IllegalStateException e = new IllegalStateException("Can not perform this action after onSaveInstanceState!");
            Log.e(e, "Please beginTransaction in onPostResume() after the Activity returns!");
        } else {
            transaction.commit();
        }
    }

    private void bindContainerId(int containerId, Fragment fragment) {
        Bundle args = getArguments(fragment);
        args.putInt(ARG_CONTAINER, containerId);
    }

    private Bundle getArguments(Fragment fragment) {

        Bundle bundle = fragment.getArguments();
        if (bundle == null) {
            bundle = new Bundle();
            fragment.setArguments(bundle);
        }
        return bundle;

    }

    /**
     * 如果Activity中使用多个Fragment并添加了BackStack，使用back键可以使用这个方法
     *
     * @param activity
     */
    public void onBackPress(Activity activity) {
        if (fragmentManager.getBackStackEntryCount() > 1) {
            fragmentManager.popBackStack();
        } else {
            activity.finish();
        }
    }


    /**
     * 获取目标Fragment的前一个 Fragment
     *

     */
    public XinFragment getTopFragment(FragmentManager fragmentManager, int containerId) {
        List<XinFragment> fragmentList = FragmentHack.getActiveFragments(fragmentManager);
        if (fragmentList == null) return null;

        for (int i = fragmentList.size() - 1; i >= 0; i--) {
            XinFragment fragment = fragmentList.get(i);
            if (containerId == 0) return fragment;

            if (containerId == getArguments(fragment).getInt(ARG_CONTAINER)) {
                return fragment;
            }

        }
        return null;
    }


    /**
     * 获取目标Fragment的前一个XinFragment
     *
     * @param fragment 目标Fragment
     */
    @SuppressWarnings("unchecked")
    public static XinFragment getPreFragment(Fragment fragment) {
        FragmentManager fragmentManager = fragment.getFragmentManager();
        if (fragmentManager == null) return null;

        @SuppressWarnings("unchecked") List<Fragment> fragmentList = FragmentHack.getActiveFragments(fragmentManager);
        if (fragmentList == null) return null;

        int index = fragmentList.indexOf(fragment);
        for (int i = index - 1; i >= 0; i--) {
            Fragment preFragment = fragmentList.get(i);
            if (preFragment instanceof XinFragment) {
                return (XinFragment) preFragment;
            }
        }
        return null;
    }

}
