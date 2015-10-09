package com.bm.hm.me;

import android.os.Bundle;

import com.bm.hm.R;
import com.bm.hm.base.BaseActivity;

/**
 * Created by chenb on 2015/4/28. 关于我们
 */
public class AboutUsActivity extends BaseActivity{
    @Override

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ac_about_us);
        initView();
    }

   private void initView() {

       initTitleBarWithBack("关于我们");
    }
}
