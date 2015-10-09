package com.bm.hm.user;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Response;
import com.bm.hm.R;
import com.bm.hm.base.BaseActivity;
import com.bm.hm.bean.User;
import com.bm.hm.constant.Constant;
import com.bm.hm.constant.Urls;
import com.bm.hm.home.MainActivity;
import com.bm.hm.http.BaseData;
import com.bm.hm.http.HttpVolleyRequest;
import com.bm.hm.util.DialogUtils;
import com.bm.hm.util.MD5Util;
import com.bm.hm.util.SharedPreferencesUtils;
import com.bm.hm.util.ToastUtil;

import java.util.HashMap;

/**
 * Created by liuz on 2015/4/22.
 */
public class FindPasswordActivity extends BaseActivity {

    private EditText et_find_phone, et_reg_message;
    private TextView tv_find_get_code;
    private Button btn_sure;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ac_find_pass);

        initView();
    }

    private void initView() {
        initTitleBarWithBack("找回密码");
        //手机号
        et_find_phone = (EditText) findViewById(R.id.et_find_phone);
        //验证码
        et_reg_message = (EditText) findViewById(R.id.et_reg_message);
        //验证码按钮
        tv_find_get_code = (TextView) findViewById(R.id.tv_find_get_code);
        //确定
        btn_sure = (Button) findViewById(R.id.btn_sure);

        tv_find_get_code.setOnClickListener(this);
        btn_sure.setOnClickListener(this);
        texst(et_find_phone);
    }

    /**
     * 动态监听手机号
     *
     * @param mEditText
     */
    private void texst(EditText mEditText) {
        mEditText.addTextChangedListener(new TextWatcher() {

            @Override
            public void afterTextChanged(Editable s) {
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before,
                                      int count) {
                // 手机号码正则判断
                String telRegex = "^(1([34578][0-9]))\\d{8}$";
                if (!et_find_phone.getText().toString().matches(telRegex)) {
//                    ToastUtil.showToast(getApplicationContext(), "请输入正确的手机号", Toast.LENGTH_SHORT);
                    tv_find_get_code.setClickable(false);
                    tv_find_get_code.setTextColor(getResources().getColor(R.color.item_content_color));
                } else {
                    tv_find_get_code.setClickable(true);
                    tv_find_get_code.setTextColor(getResources().getColor(R.color.orange));

                }
            }

        });
    }

    public class TimeCount extends CountDownTimer {

        public TimeCount(long millisInFuture, long countDownInterval) {
            super(millisInFuture, countDownInterval);
        }

        @Override
        public void onTick(long millisUntilFinished) {
            tv_find_get_code.setClickable(false);
            tv_find_get_code.setTextColor(getResources().getColor(R.color.item_content_color));
            tv_find_get_code.setText("获取验证码(" + millisUntilFinished / 1000 + ")");
        }

        @Override
        public void onFinish() {
            tv_find_get_code.setText("获取验证码");
            tv_find_get_code.setClickable(true);
            tv_find_get_code.setTextColor(getResources().getColor(R.color.orange));
        }

    }

    /**
     * @param
     * @return 说明返回值含义
     * @Description 前端判断能否登录
     */
    private boolean canFind() {
        // 手机号码正则判断
        String telRegex = "^(1([34578][0-9]))\\d{8}$";
        if (TextUtils.isEmpty(et_find_phone.getText().toString().trim())) {
            ToastUtil.showToast(getApplicationContext(), "手机号码不能为空", Toast.LENGTH_SHORT);
            return false;
        } else if (!et_find_phone.getText().toString().matches(telRegex)) {
            ToastUtil.showToast(getApplicationContext(), "请输入正确的手机号", Toast.LENGTH_SHORT);
            return false;
        }
        if (TextUtils.isEmpty(et_reg_message.getText().toString().trim())) {
            ToastUtil.showToast(getApplicationContext(), "验证码不能为空", Toast.LENGTH_SHORT);
            return false;
        }
        return true;
    }

    /**
     * 判断是否能发送消息
     *
     * @return
     */
    private boolean canSendCode() {
        // 获取编辑框电话号码
        String phoneNumber = et_find_phone.getText().toString().trim();
        String telRegex = "^(1([34578][0-9]))\\d{8}$";
        if (TextUtils.isEmpty(phoneNumber)) {
            ToastUtil.showToast(getApplicationContext(), "手机号码不能为空", Toast.LENGTH_SHORT);
            return false;
        } else if (!phoneNumber.matches(telRegex)) {
            ToastUtil.showToast(getApplicationContext(), "请输入正确的手机号", Toast.LENGTH_SHORT);
            tv_find_get_code.setClickable(false);
            tv_find_get_code.setTextColor(getResources().getColor(R.color.item_content_color));
            return false;
        }
        return true;

    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        // TODO Auto-generated method stub
        switch (v.getId()) {
            case R.id.btn_sure:
                if (canFind()) {
                    checkCode();
                }
                break;
            case R.id.tv_find_get_code:
                if (canSendCode()) {
                    sendCode();
                    TimeCount tc = new TimeCount(60000, 1000);
                    tc.start();
                    break;
                }
                break;

            default:
                break;
        }
    }

    private void sendCode() {
        HashMap<String, String> param = new HashMap<String, String>();
        param.put("mobile", et_find_phone.getText().toString().trim());
        param.put("sign", "findpassword");
        HttpVolleyRequest<BaseData> request = new HttpVolleyRequest<BaseData>(this);
        request.HttpVolleyRequestPost(Urls.SEND_CODE, param, BaseData.class, null,
                sendCodeSuccessListener(), null);

    }

    public Response.Listener<BaseData> sendCodeSuccessListener() {
        return new Response.Listener<BaseData>() {

            @Override
            public void onResponse(BaseData response) {
                DialogUtils.cancleProgressDialog();
                ToastUtil.showToast(getApplicationContext(), "验证码发送成功", Toast.LENGTH_SHORT);
                TimeCount tc = new TimeCount(60000, 1000);
                tc.start();
            }
        };
    }

    private void checkCode() {
        HashMap<String, String> param = new HashMap<String, String>();
        param.put("mobile", et_find_phone.getText().toString().trim());
        param.put("code", et_reg_message.getText().toString().trim());
        HttpVolleyRequest<BaseData> request = new HttpVolleyRequest<BaseData>(this);
        request.HttpVolleyRequestPost(Urls.PASSWORD_CHECK_CODE, param, BaseData.class, null,
                checkCodeSuccessListener(), null);

    }

    public Response.Listener<BaseData> checkCodeSuccessListener() {
        return new Response.Listener<BaseData>() {

            @Override
            public void onResponse(BaseData response) {
                DialogUtils.cancleProgressDialog();
                Intent intent = new Intent(getApplicationContext(), ChangPasswordActivity.class);
                intent.putExtra("mobile", et_find_phone.getText().toString().trim());
                intent.putExtra("code", et_reg_message.getText().toString().trim());
                startActivity(intent);
            }
        };
    }
}
