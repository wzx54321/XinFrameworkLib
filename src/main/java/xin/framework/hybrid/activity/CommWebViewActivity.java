package xin.framework.hybrid.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.View;

import xin.framework.R;
import xin.framework.base.activity.XinActivity;
import xin.framework.base.fragment.utils.XinFragmentUtils;
import xin.framework.hybrid.bean.WebOpenInfo;
import xin.framework.hybrid.fragment.CommonWebFragment;

/**
 * Description : 通用的Webview Activity
 * Created by 王照鑫 on 2017/11/3 0003.
 */

public class CommWebViewActivity extends XinActivity {


    public static final String WEB_OPEN_INFO = "WEB_OPEN_INFO";
    private XinFragmentUtils xinFragmentUtils;

    public static void launcher(Context context, @NonNull WebOpenInfo info) {
        Intent intent = new Intent(context, CommWebViewActivity.class);
        Bundle bundle = new Bundle();
        bundle.putSerializable(WEB_OPEN_INFO, info);
        intent.putExtras(bundle);
        if (!(context instanceof Activity))
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }


    @Override
    public int getLayoutId() {
        return R.layout.common_activity_web;
    }

    @Override
    public Object newP() {
        return null;
    }


    @Override
    public void bindUI(View rootView) {
    }

    @Override
    public void initData(Bundle savedInstanceState) {
        Bundle bundle = getIntent().getExtras();
        CommonWebFragment mWebFragment = null;
        if (bundle != null) {
            mWebFragment = CommonWebFragment.getInstance(bundle);
       
        xinFragmentUtils = new XinFragmentUtils(this);
        xinFragmentUtils.loadRootFragment(R.id.comm_root_web_activity, mWebFragment);
       }

    }

    @Override
    public void onBackPressed() {
        if (xinFragmentUtils != null)
            xinFragmentUtils.onBackPress(this);
    }
}
