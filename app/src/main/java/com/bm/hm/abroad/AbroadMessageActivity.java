package com.bm.hm.abroad;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.bm.hm.R;
import com.bm.hm.base.BaseActivity;
import com.bm.hm.constant.Constant;
import com.bm.hm.util.SharedPreferencesUtils;

/**
 * Created by liuz on 2015/4/23.
 */
public class AbroadMessageActivity extends BaseActivity {

    private EditText et_abroad_name, et_abr_school, et_abr_phone, et_abr_email;
    private Button btn_abr_next;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ac_abroad_message);

        initView();
    }

    private void initView() {
        initTitleBarWithBack("我要留学");
        //姓名
        et_abroad_name = (EditText) findViewById(R.id.et_abroad_name);
        //毕业学校
        et_abr_school = (EditText) findViewById(R.id.et_abr_school);
        //电话
        et_abr_phone = (EditText) findViewById(R.id.et_abr_phone);
        //邮件
        et_abr_email = (EditText) findViewById(R.id.et_abr_email);
        //下一步
        btn_abr_next = (Button) findViewById(R.id.btn_abr_next);
        btn_abr_next.setOnClickListener(this);

        et_abroad_name.setText(SharedPreferencesUtils.getInstance().getString(Constant.NAME));
        et_abr_email.setText(SharedPreferencesUtils.getInstance().getString(Constant.EMAIL));
        et_abr_phone.setText(SharedPreferencesUtils.getInstance().getString(Constant.MOBILE));

    }


    @Override
    public void onClick(View v) {
        super.onClick(v);
        // TODO Auto-generated method stub
        switch (v.getId()) {
            case R.id.btn_abr_next:
                Intent intent = new Intent(getApplicationContext(),QuestionActivity.class);
                intent.putExtra("name",et_abroad_name.getText().toString().trim());
                intent.putExtra("school",et_abr_school.getText().toString().trim());
                intent.putExtra("mobile",et_abr_phone.getText().toString().trim());
                intent.putExtra("mail",et_abr_email.getText().toString().trim());
                startActivity(intent);
                break;

            default:
                break;
        }
    }


}
