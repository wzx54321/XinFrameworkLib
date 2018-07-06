package xin.framework.widget.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import xin.framework.R;


/**
 * 使用方法：
 * final CustomDialog selfDialog = new CustomDialog(MainActivity.this);
 * <p>
 * selfDialog.setCustom(你的布局);
 * selfDialog.setYesOnclickListener("确定", new SelfDialog.onYesOnclickListener() {
 *
 * @Override public void onYesClick() {
 * Toast.makeText(MainActivity.this, "点击了--确定--按钮", Toast.LENGTH_LONG).show();
 * selfDialog.dismiss();
 * }
 * });
 * selfDialog.setNoOnclickListener("取消", new SelfDialog.onNoOnclickListener() {
 *  public void onNoClick() {
 * Toast.makeText(MainActivity.this, "点击了--取消--按钮", Toast.LENGTH_LONG).show();
 * CustomDialog.dismiss();
 * }
 * });
 * CustomDialog.show();
 */

public class CustomDialog extends Dialog {


    private View mContentView;


    private RelativeLayout contentRoot;

    /**
     * 29.     * 设置取消按钮的显示内容和监听
     * 30.     *
     * 31.     * @param str
     * 32.     * @param onNoOnclickListener
     * 33.
     */


    public CustomDialog(Context context) {
        super(context, R.style.dialog_normal);

    }


    @Override


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.common_dialog_custom_view_layout);
        //按空白处不能取消动画
        setCanceledOnTouchOutside(true);

        //初始化界面控件
        initView();
        //初始化界面数据
        initData();
        //初始化界面控件的事件
        initEvent();


    }

    /**
     * 75.     * 初始化界面的确定和取消监听器
     * 76.
     */

    private void initView() {

        contentRoot = findViewById(R.id.content_view_root);

    }

    /**
     * 99.     * 初始化界面控件的显示数据
     * 100.
     */


    private void initData() {


        if (mContentView != null) {
            RelativeLayout.LayoutParams params = new RelativeLayout.
                    LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT);
            contentRoot.addView(mContentView, params);

        }

    }

    /**
     * 119.     * 初始化界面控件
     * 120.
     */

    private void initEvent() {


    }


    public View getContentView() {
        return mContentView;
    }

    public void setCustom(View mContentView) {
        this.mContentView = mContentView;


    }
}
