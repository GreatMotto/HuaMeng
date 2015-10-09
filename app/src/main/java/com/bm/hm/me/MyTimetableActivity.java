package com.bm.hm.me;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.FrameLayout;
import android.widget.ScrollView;
import android.widget.Toast;

import com.bm.hm.R;
import com.bm.hm.base.BaseActivity;
import com.bm.hm.constant.Constant;
import com.bm.hm.constant.Urls;
import com.bm.hm.pulltoref.PullToRefreshBase;
import com.bm.hm.pulltoref.PullToRefreshWebView;
import com.bm.hm.util.SharedPreferencesUtils;
import com.bm.hm.util.ToastUtil;

import java.util.HashMap;

/**
 * Created by chenb on 2015/5/28.
 */
public class MyTimetableActivity extends BaseActivity {
    protected WebView agreement_content;
    private String title;
    private ScrollView sc_mys;
    private PullToRefreshWebView rfl_timetable1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ac_agreement);
        initView();
        myTimetableRequest("");
    }

    private void initView() {
        initTitleBarWithBack("我的课表");
        sc_mys = (ScrollView) findViewById(R.id.sc_mys);

        String url = getIntent().getStringExtra("url");
        FrameLayout fll = (FrameLayout) findViewById(R.id.fll);
        rfl_timetable1 = new PullToRefreshWebView(this);
        rfl_timetable1.setMode(PullToRefreshBase.Mode.BOTH);
        agreement_content = rfl_timetable1.getRefreshableView();
        agreement_content.getSettings().setJavaScriptEnabled(true);
        agreement_content.setWebViewClient(new HelloWebViewClient());
        rfl_timetable1.setScrollBarStyle(View.SCROLLBARS_OUTSIDE_OVERLAY);
        fll.addView(rfl_timetable1);
        rfl_timetable1.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener<WebView>() {
            @Override
            public void onRefresh(PullToRefreshBase<WebView> refreshView) {
                if (rfl_timetable1.isHeaderShown()) {
                    if(isNetworkConnected(MyTimetableActivity.this)){
                        myTimetableRequest("down");
                    }else{
                        rfl_timetable1.onRefreshComplete();
                        ToastUtil.showToast(getApplicationContext(), "获取网络失败", Toast.LENGTH_SHORT);
                    }

                    Log.e("kebiao", "向下");

                } else {
                    if(isNetworkConnected(MyTimetableActivity.this)) {
                        myTimetableRequest("up");
                    }
                    else {
                        rfl_timetable1.onRefreshComplete();
                        ToastUtil.showToast(getApplicationContext(), "获取网络失败", Toast.LENGTH_SHORT);
                    }
                    Log.e("kebiao", "向上");
                }
            }
        });

    }

    private class HelloWebViewClient extends WebViewClient {

        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            view.loadUrl(url);
            return true;
        }

        @Override
        public boolean shouldOverrideKeyEvent(WebView view, KeyEvent event) {
            return super.shouldOverrideKeyEvent(view, event);
        }

        @Override
        public void onPageFinished(WebView view, String url) {
            super.onPageFinished(view, url);
            rfl_timetable1.onRefreshComplete();
            Log.e("结束", "结束");
        }

    }

    /**
     * 查询我的课表       请求后台数据
     */
    private void myTimetableRequest(String flag) {
        HashMap<String, String> param = new HashMap<String, String>();
        param.put("userId", String.valueOf(SharedPreferencesUtils.getInstance().getInt(Constant.USER_ID)));
        param.put("rollFlag", flag);
        Log.e("FLAG", String.valueOf(SharedPreferencesUtils.getInstance().getInt(Constant.USER_ID)) + flag + "哈哈");
        agreement_content.loadUrl(Urls.MYTIMETABLE + "?userId=" + String.valueOf(SharedPreferencesUtils.getInstance().getInt(Constant.USER_ID)) + "&rollFlag=" + flag);

    }

    public boolean isNetworkConnected(Context context) {
        if (context != null) {
            ConnectivityManager mConnectivityManager = (ConnectivityManager) context
                    .getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo mNetworkInfo = mConnectivityManager.getActiveNetworkInfo();
            if (mNetworkInfo != null) {
                return mNetworkInfo.isAvailable();
            }
        }
        return false;
    }
}
