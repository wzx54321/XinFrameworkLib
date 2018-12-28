package xin.framework.hybrid.fragment;

import android.content.res.Configuration;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import xin.framework.BuildConfig;
import xin.framework.R;
import xin.framework.base.fragment.XinFragment;
import xin.framework.hybrid.activity.CommWebViewActivity;
import xin.framework.hybrid.bean.WebOpenInfo;
import xin.framework.hybrid.bean.WebPostParams;
import xin.framework.hybrid.model.WebModel;
import xin.framework.hybrid.presenter.WebPresenter;
import xin.framework.hybrid.video.WebVideoDelegate;
import xin.framework.hybrid.webview.WebViewConfig;
import xin.framework.hybrid.webview.XinWebView;
import xin.framework.utils.android.Loger.Log;
import xin.framework.utils.android.view.ViewFinder;
import xin.framework.utils.android.view.compatibility.title.StatusBarUtil;


/**
 * Description : 通用的webview
 * Created by 王照鑫 on 2017/11/2 0002.
 */

public class CommonWebFragment extends XinFragment<WebPresenter> implements XinWebView.onBackClickListener {


    ProgressBar mProgressbar;

    LinearLayout mRootWebviewF;

    ImageView mBtnGoBack;

    ImageView mBtnClose;


    private XinWebView mWebView;
    private WebVideoDelegate mWebVideoDelegate;
    private TextView mTitle;


    public static CommonWebFragment getInstance(@NonNull Bundle bundle) {

        CommonWebFragment webFragment = new CommonWebFragment();
        webFragment.setArguments(bundle);

        return webFragment;

    }


    @Override
    public void bindUI(View rootView) {
        mWebView = WebViewConfig.getInstance().useWebView(getContext());
        LinearLayout.LayoutParams webParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT);
        mRootWebviewF = ViewFinder.find(rootView, R.id.root_webview_f);
        mWebView.setParentViewGroup(mRootWebviewF, webParams);
        mWebVideoDelegate = new WebVideoDelegate(getActivity(), mWebView);
        mWebView.setOnBackClickListener(this);
        mProgressbar = ViewFinder.find(rootView, R.id.progressbar_webview);
        if (BuildConfig.DEBUG)
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                mWebView.setWebContentsDebuggingEnabled(true);
            }
        StatusBarUtil.setPaddingSmart(getContext(), ViewFinder.find(rootView, R.id.title_root));

        mBtnGoBack = ViewFinder.find(rootView, R.id.app_back);
        mBtnGoBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!onKeyBack() && getActivity() != null) {
                    getActivity().finish();
                }

            }
        });


        mBtnClose = ViewFinder.find(rootView, R.id.app_close);
        mBtnClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().finish();
            }
        });
        mBtnClose.setVisibility(View.GONE);
        mTitle = ViewFinder.find(rootView, R.id.web_title);

    }


    @Override
    public void onResume() {
        super.onResume();
        if (mWebView != null)
            mWebView.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        mWebView.onPause();
    }


    @Override
    protected void initVariables(Bundle bundle) {

    }

    @Override
    public void onDestroy() {
        if (mWebView != null) {

            WebViewConfig.getInstance().resetWebView();
        }
        super.onDestroy();
    }

    @Override
    protected void bindDataFirst() {
        if (getArguments() != null) {
            WebOpenInfo mWebOpenInfo = (WebOpenInfo) getArguments().getSerializable(CommWebViewActivity.WEB_OPEN_INFO);
            getP().setWebOpenInfo(mWebOpenInfo);
            getP().setWebViewClient();
            getP().setWebChromeClient();
            getP().openBrowser(mWebOpenInfo);
        } else {
            throw new NullPointerException("Arguments is null");
        }
    }




    public void setWebViewClient(WebModel.XinWebViewClient client) {
        mWebView.setXinWebViewClient(client);
    }


    public void setWebChromeClient(WebChromeClient client) {
        mWebView.setWebChromeClient(client);
    }


    public void setTitleText(String titleText) {
        mTitle.setText(titleText);
    }


    public void onProgressChanged(WebView view, int newProgress) {
        if (newProgress < 100 && !mProgressbar.isShown()) {
            mProgressbar.setVisibility(View.VISIBLE);
        }

        mProgressbar.setProgress(newProgress);
        mProgressbar.postInvalidate();

        if (newProgress == 100) {
            mProgressbar.setVisibility(View.GONE);

        }
    }


    public void setTitleStyles() {
        // 处理title左边的view按钮显示效果
        if (mBtnClose == null)
            return;
        if (mWebView.canGoBack()) {
            mBtnClose.setVisibility(View.VISIBLE);
        } else {
            mBtnClose.setVisibility(View.INVISIBLE);
        }
    }


    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);

        try {
            super.onConfigurationChanged(newConfig);
            if (this.getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
                Log.v("onConfigurationChanged_ORIENTATION_LANDSCAPE");
            } else if (this.getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
                Log.v("onConfigurationChanged_ORIENTATION_PORTRAIT");
            }
        } catch (Exception ex) {
            Log.printStackTrace(ex);
        }
    }


    /**
     * post请求网页
     *
     * @param url    地址
     * @param params 参数
     */

    public void postUrl(String url, @NonNull WebPostParams<String, String> params) {
        mWebView.postUrl(url, params);
    }

    /**
     * 加载本地网页
     */

    public void loadData(String content) {
        mWebView.loadDataWithBaseURL(null, content, "text/html", "UTF-8", null);
    }


    public void loadUrl(String url) {

        mWebView.loadUrl(url);
    }


    public void onShowCustomView(View view, WebChromeClient.CustomViewCallback callback) {
        mWebVideoDelegate.onShowCustomView(view, callback);
    }




    public View getVideoLoadingProgressView() {
        return  mWebVideoDelegate.getVideoLoadingProgressView();
    }


    public void onHideCustomView() {
        mWebVideoDelegate.onHideCustomView();
    }


    public XinWebView getWebView() {
        return mWebView;
    }


    public boolean onBackPressedSupport() {
        return onKeyBack();
    }

    @Override
    public boolean onKeyBack() {
        if (this.mWebVideoDelegate != null && this.mWebVideoDelegate.event()) {
            return true;
        }

        if (mWebView != null && mWebView.canGoBack()) {
            mWebView.goBack();
            return true;
        }
        return false;
    }


    @Override
    public int getLayoutId() {
        return R.layout.common_fragment_web;
    }

    @Override
    public WebPresenter newP() {
        return new WebPresenter();
    }
}
