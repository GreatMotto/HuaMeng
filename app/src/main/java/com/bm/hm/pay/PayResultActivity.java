package com.bm.hm.pay;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.bm.hm.R;
import com.bm.hm.base.BaseActivity;
import com.bm.hm.bean.Course;
import com.bm.hm.course.CourseInfoActivity;
import com.bm.hm.home.MainActivity;

public class PayResultActivity extends BaseActivity {

    private Button btn_first, btn_second;
        private TextView tv_pay_result;
        private Course course;

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.ac_recharge_result);

            initView();
        }

    private void initView() {
        course = (Course) getIntent().getSerializableExtra("course");

        initTitleBarWithBack("收银台");

        tv_pay_result = (TextView) findViewById(R.id.tv_pay_result);

        btn_first = (Button) findViewById(R.id.btn_first);
        btn_first.setOnClickListener(this);

        btn_second = (Button) findViewById(R.id.btn_second);
        btn_second.setOnClickListener(this);

        if(getIntent().getStringExtra("result").equals("success")){
            tv_pay_result.setText("恭喜您，付款成功!");
            tv_pay_result.setCompoundDrawablesWithIntrinsicBounds(null, getResources().getDrawable(R.mipmap.pay_succeed), null, null);

            btn_first.setText("查看课程");
            btn_first.setTag("1");

            btn_second.setText("继续购物");
            btn_second.setTag("1");
        }else{
            tv_pay_result.setText("很抱歉，支付失败！");
            tv_pay_result.setCompoundDrawablesWithIntrinsicBounds(null, getResources().getDrawable(R.mipmap.pay_failure), null, null);

            btn_first.setText("继续支付");
            btn_first.setTag("2");

            btn_second.setText("取消支付");
            btn_second.setTag("2");
        }

        btn_first = (Button) findViewById(R.id.btn_first);
        btn_first.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()) {
            case R.id.btn_first:
                if(btn_first.getTag().toString().equals("1")){
                    Intent intent = new Intent(getApplicationContext(), CourseInfoActivity.class);
                    intent.putExtra("course",course);
                    startActivity(intent);
                }else{
                    Intent intent = new Intent(getApplicationContext(), OrderActivity.class);
                    intent.putExtra("course",course);
                    startActivity(intent);
                }
                finish();
                break;
            case R.id.btn_second:
                if(btn_second.getTag().toString().equals("1")){
                    Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                    intent.putExtra("tag",0);
                    startActivity(intent);
                }else{
                    Intent intent = new Intent(getApplicationContext(), CourseInfoActivity.class);
                    intent.putExtra("course",course);
                    startActivity(intent);
                }
                finish();
                break;
            default:
                break;
        }
    }
}
