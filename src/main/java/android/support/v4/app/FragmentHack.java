package android.support.v4.app;

import android.util.SparseArray;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * 作者：xin on 2018/7/2 0002 15:54
 * <p>
 * 邮箱：ittfxin@126.com
 */

public class FragmentHack {
    private static boolean sSupportLessThan25dot4 = false;

    static {
        Field[] fields = FragmentManagerImpl.class.getDeclaredFields();
        for (Field field : fields) {
            if (field.getName().equals("mAvailIndices")) {
                sSupportLessThan25dot4 = true;
                break;
            }
        }
    }

    public static boolean isStateSaved(FragmentManager fragmentManager) {
        if (!(fragmentManager instanceof FragmentManagerImpl))
            return false;
        try {
            FragmentManagerImpl fragmentManagerImpl = (FragmentManagerImpl) fragmentManager;
            return fragmentManagerImpl.mStateSaved;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public static List getActiveFragments(FragmentManager fragmentManager) {
        if (!(fragmentManager instanceof FragmentManagerImpl))
            return Collections.EMPTY_LIST;

        // For pre-25.4.0
        if (sSupportLessThan25dot4) return fragmentManager.getFragments();

        // For compat 25.4.0+
        try {
            FragmentManagerImpl fragmentManagerImpl = (FragmentManagerImpl) fragmentManager;
            // Since v4-25.4.0，mActive: ArrayList -> SparseArray
            return getActiveList(fragmentManagerImpl.mActive);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return fragmentManager.getFragments();

    }

    private static List getActiveList(SparseArray<Fragment> active) {
        if (active == null) {
            return Collections.EMPTY_LIST;
        }

        final int count = active.size();
        ArrayList<Fragment> fragments = new ArrayList<>(count);
        for (int i = 0; i < count; i++) {
            fragments.add(active.valueAt(i));
        }
        return fragments;

    }
}
