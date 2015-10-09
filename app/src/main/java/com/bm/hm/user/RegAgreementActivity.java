package com.bm.hm.user;

import android.os.Bundle;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.bm.hm.R;
import com.bm.hm.base.BaseActivity;

/**
 * Created by liuz on 2015/4/21.
 */
public class RegAgreementActivity extends BaseActivity {
    private WebView webView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ac_reg_agreement);

        initView();
    }

    private void initView() {
        initTitleBarWithBack("注册协议");

        initWebView("file:///android_asset/register.htm");
    }

    private void initWebView(String url){
        webView = (WebView) findViewById(R.id.webView);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.getSettings().setPluginState(WebSettings.PluginState.ON);
        webView.getSettings().setUseWideViewPort(true);
//
//        // 设置可以支持缩放
//        webView.getSettings().setSupportZoom(true);
//        // 设置出现缩放工具
//        webView.getSettings().setBuiltInZoomControls(true);
//        // 扩大比例的缩放
//        webView.getSettings().setUseWideViewPort(true);
        // 自适应屏幕
//        webView.getSettings().setLayoutAlgorithm(
//                WebSettings.LayoutAlgorithm.SINGLE_COLUMN);
//        webView.getSettings().setLoadWithOverviewMode(true);

        webView.loadUrl(url);

        webView.setWebViewClient(new WebViewClient() {
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                // 重写此方法表明点击网页里面的链接还是在当前的webview里跳转，不跳到浏览器那边
                view.loadUrl(url);
                return true;
            }
        });
    }
}
