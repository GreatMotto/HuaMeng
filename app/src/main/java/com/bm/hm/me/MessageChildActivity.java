package com.bm.hm.me;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import com.bm.hm.R;
import com.bm.hm.base.BaseActivity;

/**
 * Created by chenb on 2015/5/5.
 * 全体通知
 */
public class MessageChildActivity extends BaseActivity {
    private TextView tv_message;
    private  String content;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ac_inform);
        Intent intent=getIntent();
        content=intent.getStringExtra("content");
        initView();
    }

    private void initView() {
        initTitleBarWithBack("通知");
        tv_message=(TextView)findViewById(R.id.tv_message);
        tv_message.setText(content);
    }
}
