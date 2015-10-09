package com.bm.hm.me;

import android.os.Bundle;

import com.bm.hm.R;
import com.bm.hm.base.BaseActivity;

/**
 * Created by chenb on 2015/4/28.
 */
public class ContactUsActivity extends BaseActivity {

    @Override
    public  void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ac_contact_us);
        initView();
    }

    private void initView() {
        initTitleBarWithBack("联系我们");
    }
}
