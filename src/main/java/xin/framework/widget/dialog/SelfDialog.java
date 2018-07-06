package xin.framework.widget.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import xin.framework.R;


/**
 * 使用方法：
 * final SelfDialog selfDialog = new SelfDialog(MainActivity.this);
 * selfDialog.setTitle("提示");
 * selfDialog.setMessage("确定退出应用?");
 * selfDialog.setYesOnclickListener("确定", new SelfDialog.onYesOnclickListener() {
 *
 * @Override public void onYesClick() {
 * Toast.makeText(MainActivity.this, "点击了--确定--按钮", Toast.LENGTH_LONG).show();
 * selfDialog.dismiss();
 * }
 * });
 * selfDialog.setNoOnclickListener("取消", new SelfDialog.onNoOnclickListener() {
 * @Override public void onNoClick() {
 * Toast.makeText(MainActivity.this, "点击了--取消--按钮", Toast.LENGTH_LONG).show();
 * selfDialog.dismiss();
 * }
 * });
 * selfDialog.show();
 */

public class SelfDialog extends Dialog {
    private Button yes;//确定按钮

    private Button no;//取消按钮

    private TextView titleTv;//消息标题文本

    private TextView messageTv;//消息提示文本

    private String titleStr;//从外界设置的title文本

    private String messageStr;//从外界设置的消息文本
    //确定文本和取消文本的显示内容

    private String yesStr, noStr;

    private onNoOnclickListener noOnclickListener;//取消按钮被点击了的监听器

    private onYesOnclickListener yesOnclickListener;//确定按钮被点击了的监听器

    /**
     * 29.     * 设置取消按钮的显示内容和监听
     * 30.     *
     * 31.     * @param str
     * 32.     * @param onNoOnclickListener
     * 33.
     */


    public SelfDialog(Context context) {
        super(context, R.style.dialog_normal);

    }

    /**
     * 42.     * 设置确定按钮的显示内容和监听
     * 43.     *
     * 44.     * @param str
     * 45.     * @param onYesOnclickListener
     * 46.
     */


    public void setNoOnclickListener(String str, onNoOnclickListener onNoOnclickListener) {
        if (str != null) {
            noStr = str;

        }
        this.noOnclickListener = onNoOnclickListener;

    }


    public void setYesOnclickListener(String str, onYesOnclickListener onYesOnclickListener) {
        if (str != null) {
            yesStr = str;

        }
        this.yesOnclickListener = onYesOnclickListener;

    }


    @Override


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.common_dialog_layout);
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
        yes = findViewById(R.id.yes);
        no = findViewById(R.id.no);
        titleTv = findViewById(R.id.title);
        messageTv = findViewById(R.id.message);

    }

    /**
     * 99.     * 初始化界面控件的显示数据
     * 100.
     */


    private void initData() {
        //如果用户自定了title和message
        if (titleStr != null) {
            titleTv.setText(titleStr);

        }
        if (messageStr != null) {
            messageTv.setText(messageStr);

        }
        //如果设置按钮的文字
        if (yesStr != null) {
            yes.setText(yesStr);

        }
        if (noStr != null) {
            no.setText(noStr);

        }

    }

    /**
     * 119.     * 初始化界面控件
     * 120.
     */

    private void initEvent() {
        //设置确定按钮被点击后，向外界提供监听
        yes.setOnClickListener(new View.OnClickListener() {
            @Override


            public void onClick(View v) {
                if (yesOnclickListener != null) {
                    yesOnclickListener.onYesClick();

                }

            }

        });
        //设置取消按钮被点击后，向外界提供监听
        no.setOnClickListener(new View.OnClickListener() {
            @Override

            public void onClick(View v) {
                if (noOnclickListener != null) {
                    noOnclickListener.onNoClick();

                }

            }
        });

    }

    /**
     * 129.     * 从外界Activity为Dialog设置标题
     * 130.     *
     * 131.     * @param title
     * 132.
     */


    public void setTitle(String title) {
        titleStr = title;

    }


    /**
     * 从外界Activity为Dialog设置dialog的message
     * 1     *
     * .     * @param message
     * 1
     */
    public void setMessage(String message) {
        messageStr = message;
    }

    /**
     * 设置确定按钮和取消被点击的接口
     */
    public interface onYesOnclickListener {
        void onYesClick();
    }

    public interface onNoOnclickListener {
        void onNoClick();
    }
}
