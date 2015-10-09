package com.bm.hm.abroad;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.bm.hm.R;
import com.bm.hm.base.BaseActivity;

/**
 * Created by liuz on 2015/4/23.
 */
public class AbroadMessageJoin extends BaseActivity {

    private TextView tv_result;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ac_join_true);

        initView();
    }

    private void initView() {
        initTitleBarWithBack("我要留学");

        tv_result = (TextView) findViewById(R.id.tv_result);
        if(getIntent().getIntExtra("result",0)==1){
            tv_result.setText("您已经参加过这个问卷调查");
        }else{
            tv_result.setText("感谢您答完问卷调查");
        }
    }

}
