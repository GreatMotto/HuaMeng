package com.bm.hm.view;

import android.os.Bundle;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.bm.hm.R;
import com.bm.hm.base.BaseActivity;

/**
 * Created by chenb on 2015/5/27.
 */
public class WebViewActivity extends BaseActivity{
    private WebView agreement_content;
    private String title;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ac_agreement);
        init();
    }

    private void init() {
        title = "";
        title = getIntent().getStringExtra("flag");
        String url = getIntent().getStringExtra("url");
        initTitleBar(title);
//        agreement_content = (WebView) findViewById(R.id.agreement_content);

        agreement_content.loadUrl(url);

        agreement_content.getSettings().setJavaScriptEnabled(true);
        agreement_content.setWebViewClient(new HelloWebViewClient());
    }

    private class HelloWebViewClient extends WebViewClient {

        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            view.loadUrl(url);
            return true;
        }
    }
}
