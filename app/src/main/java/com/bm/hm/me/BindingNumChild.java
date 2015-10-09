package com.bm.hm.me;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bm.hm.R;
import com.bm.hm.base.BaseActivity;

/**
 * Created by chenb on 2015/4/29.
 */
public class BindingNumChild extends BaseActivity {

    private Button btn_binding_goto;
    private ImageView img_binding;
    private TextView tv_binding, tv_binding_def;
    private int status;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ac_binding_num_child);

        initView();
    }

    private void initView() {
        initTitleBarWithBack("学号");
        img_binding = (ImageView) findViewById(R.id.img_binding);
        tv_binding = (TextView) findViewById(R.id.tv_binding);
        tv_binding_def = (TextView) findViewById(R.id.tv_binding_def);
        btn_binding_goto = (Button) findViewById(R.id.btn_binding_goto);
        btn_binding_goto.setOnClickListener(this);

        status = getIntent().getIntExtra("status", 0);
        if (status == 1) {
            img_binding.setImageResource(R.mipmap.binding_def);
            tv_binding.setText("您的姓名或手机号不正确");
            tv_binding_def.setVisibility(View.VISIBLE);
            btn_binding_goto.setText("继续绑定");
        } else {
            img_binding.setImageResource(R.mipmap.binding_suc);
            tv_binding.setText("恭喜你，绑定成功");
            tv_binding_def.setVisibility(View.INVISIBLE);
            btn_binding_goto.setText("返回个人中心");
        }
    }

    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()) {
            case R.id.btn_binding_goto:
                if (status == 2) {
//                    gotoOtherActivity(MyCenterMessage.class);
                    finish();
                } else {
                    gotoOtherActivity(BindingNumActivity.class);
                    finish();
                }
                break;

            default:
                break;

        }

    }

}
