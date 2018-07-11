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
 * final CustomDialog customDialog = new CustomDialog(MainActivity.this);
 * <p>
 * customDialog.setCustom(你的布局);
 * customDialog.setYesOnclickListener("确定", new CustomDialog.onYesOnclickListener() {
 *
 * @Override public void onYesClick() {
 * Toast.makeText(MainActivity.this, "点击了--确定--按钮", Toast.LENGTH_LONG).show();
 * customDialog.dismiss();
 * }
 * });
 * customDialog.setNoOnclickListener("取消", new CustomDialog.onNoOnclickListener() {
 * public void onNoClick() {
 * Toast.makeText(MainActivity.this, "点击了--取消--按钮", Toast.LENGTH_LONG).show();
 * customDialog.dismiss();
 * }
 * });
 * customDialog.show();
 *
 * <p>
 * 邮箱：ittfxin@126.com
 * <p>
 * https://github.com/wzx54321/XinFrameworkLib
 */

public class CustomDialog extends Dialog {


    private View mContentView;


    private RelativeLayout contentRoot;

    /**
     * 设置取消按钮的显示内容和监听
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
     * 初始化界面的确定和取消监听器
     */

    private void initView() {

        contentRoot = findViewById(R.id.content_view_root);

    }

    /**
     * 初始化界面控件的显示数据
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
     * 初始化界面控件
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
