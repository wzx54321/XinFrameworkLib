package xin.framework.utils.android.view;

import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListAdapter;
import android.widget.ListView;

/**
 * Created by xin
 * 用于查找View使用替代findViewById
 * <p>
 * 邮箱：ittfxin@126.com
 * <p>
 * https://github.com/wzx54321/XinFrameworkLib
 */
@SuppressWarnings("unchecked")
public class ViewFinder {

    public static <T extends View> T find(View view, int id) {
        return (T) view.findViewById(id);
    }

    public static <T extends View> T find(Activity activity, int id) {
        return (T) activity.findViewById(id);
    }

    @SuppressWarnings("RedundantThrows")
    public static <T extends View> T getView(View view) throws Exception {
        return (T) view;
    }
    /**
     * 动态设置ListView的高度
     * @param listView ListView
     */
    public static void setListViewHeightBasedOnChildren(ListView listView) {
        if(listView == null) return;

        ListAdapter listAdapter = listView.getAdapter();
        if (listAdapter == null) {
            // pre-condition
            return;
        }

        int totalHeight = 0;
        for (int i = 0; i < listAdapter.getCount(); i++) {
            View listItem = listAdapter.getView(i, null, listView);
            listItem.measure(0, 0);
            totalHeight += listItem.getMeasuredHeight();
        }

        ViewGroup.LayoutParams params = listView.getLayoutParams();
        params.height = totalHeight + (listView.getDividerHeight() * (listAdapter.getCount() - 1));
        listView.setLayoutParams(params);
    }



}
