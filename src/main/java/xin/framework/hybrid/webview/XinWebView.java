package xin.framework.hybrid.webview;

import android.content.Context;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.view.ViewGroup;
import android.webkit.WebView;

import okhttp3.internal.Util;
import xin.framework.hybrid.bean.WebPostParams;
import xin.framework.hybrid.model.WebModel;
import xin.framework.utils.android.Loger.Log;
import xin.framework.utils.common.utils.HttpUtils;
import xin.framework.utils.common.utils.io.StringCodingUtils;

/**
 * Description :
 * Created by 王照鑫 on 2017/11/2 0002.
 */

public class XinWebView extends WebView {


    private ViewGroup mViewGroup;
    private boolean mIsUsed;

    public XinWebView(Context context) {
        super(context);
    }

    public XinWebView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public XinWebView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }


    @Override
    public boolean dispatchKeyEvent(KeyEvent event) {
        if (event.getKeyCode() == KeyEvent.KEYCODE_BACK) {
            if (onBackClickListener != null) {
                return onBackClickListener.onKeyBack() || super.dispatchKeyEvent(event);
            }
        }
        return super.dispatchKeyEvent(event);
    }



    private onBackClickListener onBackClickListener;


    public void setOnBackClickListener(XinWebView.onBackClickListener onBackClickListener) {
        this.onBackClickListener = onBackClickListener;
    }

    public void setParentViewGroup(ViewGroup view, ViewGroup.LayoutParams webParams) {

        if (mIsUsed) {
            if (mViewGroup != null) {
                mViewGroup.removeView(this);
            }
        }
        this.mViewGroup = view;

        view.addView(this, webParams);
    }


    public ViewGroup getParentViewGroup() {
        return mViewGroup;
    }

    public void setIsUsed(boolean isUsed) {
        mIsUsed = isUsed;

    }

    public interface onBackClickListener {
        boolean onKeyBack();
    }

    @Override
    public void loadDataWithBaseURL(String baseUrl, String data, String mimeType, String encoding, String historyUrl) {
        super.loadDataWithBaseURL(baseUrl, data, mimeType, encoding, historyUrl);
    }


    @Override
    public void loadUrl(String url) {
        super.loadUrl(url);
    }


    public void postUrl(String url, @NonNull WebPostParams<String, String> params) {

        String paramStr = HttpUtils.createParams(url, params);

        if (!TextUtils.isEmpty(paramStr)) {

            Log.i("webView地址:  " + url + "\n" + "参数: " + paramStr);
            postUrl(url, StringCodingUtils.getBytes(paramStr, Util.UTF_8));//这种写法可以正确解码

        }
    }


    public void setXinWebViewClient(WebModel.XinWebViewClient client) {
        super.setWebViewClient(client);

    }

    public void recycle() {


        setWebViewClient(null);

        setWebChromeClient(null);
        setOnBackClickListener(null);
        super.destroy();

        if (mViewGroup != null) {
            if (mViewGroup.indexOfChild(this) != -1) {
                mViewGroup.removeView(this);
                mViewGroup = null;

                mIsUsed = false;
            }
        }
    }
}
