package xin.framework.hybrid.presenter;

import android.graphics.Bitmap;
import android.text.TextUtils;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebView;

import xin.framework.hybrid.bean.WebOpenInfo;
import xin.framework.hybrid.bean.WebPostParams;
import xin.framework.hybrid.fragment.CommonWebFragment;
import xin.framework.hybrid.model.WebModel;
import xin.framework.mvp.PresentImpl;
import xin.framework.utils.android.Loger.Log;

/**
 * Description : webview使用的presenter
 * Created by 王照鑫 on 2017/11/2 0002.
 */

public class WebPresenter extends PresentImpl<CommonWebFragment> {


    private WebModel model;


    public WebPresenter() {
        model = new WebModel();
    }

    public void onStart() {
        model = new WebModel();

    }


    public void onDestroy() {
        model.onDestroy();

    }


    public void setWebOpenInfo(WebOpenInfo info) {
        WebOpenInfo mWebOpenInfo = info;


    }


    public void setWebViewClient() {


        getV().setWebViewClient(new WebModel.XinWebViewClient() {
            @Override
            public void onTitleSet(String titleText) {
                getV().setTitleText(titleText);
            }


            @Override
            public void setTitleStyles() {

                getV().setTitleStyles();

            }


            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                super.onPageStarted(view, url, favicon);
            }


        });

    }


    public void setWebChromeClient() {
        getV().setWebChromeClient(new WebModel.XinWebChromeClient() {
            @Override
            public void onTitleSet(String titleText) {
                getV().setTitleText(titleText);
            }


            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                super.onProgressChanged(view, newProgress);
                getV().onProgressChanged(view, newProgress);

            }


            @Override
            public void onShowCustomView(View view, WebChromeClient.CustomViewCallback callback) {
                super.onShowCustomView(view, callback);
                getV().onShowCustomView(view, callback);
            }


            @Override
            public void onHideCustomView() {
                super.onHideCustomView();

                getV().onHideCustomView();
            }
        });

    }


    public void openBrowser(WebOpenInfo webOpenInfo) {
        String url = webOpenInfo.getUrl();
        String htmlContent = webOpenInfo.getHtmlContent();
        WebPostParams<String, String> params = webOpenInfo.getParams();
        if (TextUtils.isEmpty(htmlContent)) {
            if (TextUtils.isEmpty(url)) {
                throw new NullPointerException("url 不可以问空值");
            }
            if (params == null || params.isEmpty()) {
                getV().loadUrl(url);
            } else {
                getV().postUrl(url, params);
                Log.d("XinWebView访问地址:" + url + "\n" + "参数：" + params.toString());
            }
        } else {
            getV().loadData(htmlContent);
            Log.d("XinWebView访问内容:" + htmlContent);
        }
    }


}
