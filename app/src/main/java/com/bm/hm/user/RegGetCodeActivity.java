package com.bm.hm.user;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Response;
import com.bm.hm.R;
import com.bm.hm.base.BaseActivity;
import com.bm.hm.constant.Urls;
import com.bm.hm.http.BaseData;
import com.bm.hm.http.HttpVolleyRequest;
import com.bm.hm.util.DialogUtils;
import com.bm.hm.util.TextUtil;
import com.bm.hm.util.ToastUtil;

import java.util.HashMap;

/**
 * Created by liuz on 2015/4/21.
 * 注册获取验证码
 */
public class RegGetCodeActivity extends BaseActivity {

    private EditText et_reg_phone, et_reg_message;
    private TextView tv_reg_get_code, tv_reg_Agreement_name;
    private CheckBox cb_select;
    private Button btn_next;
    private Boolean CheckFlag = true;
    private String mobile;
    private String code;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ac_register_getcode);

        initView();
    }

    private void initView() {
        initTitleBarWithBack("注册");
        //手机号
        et_reg_phone = (EditText) findViewById(R.id.et_reg_phone);
        //短信验证码
        et_reg_message = (EditText) findViewById(R.id.et_reg_message);
        //获取验证码按钮
        tv_reg_get_code = (TextView) findViewById(R.id.tv_reg_get_code);
        //复选框
        cb_select = (CheckBox) findViewById(R.id.cb_select);
        //协议
        tv_reg_Agreement_name = (TextView) findViewById(R.id.tv_reg_Agreement_name);
        //下一步按钮
        btn_next = (Button) findViewById(R.id.btn_next);
        TextUtil.setPintLine(getApplicationContext(), tv_reg_Agreement_name);

        btn_next.setOnClickListener(this);
        tv_reg_Agreement_name.setOnClickListener(this);
        tv_reg_get_code.setOnClickListener(this);
        texst(et_reg_phone);
        cb_select.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    CheckFlag = true;
                } else {
                    CheckFlag = false;
                }
            }
        });

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
                if (!et_reg_phone.getText().toString().matches(telRegex)) {
//                    ToastUtil.showToast(getApplicationContext(), "请输入正确的手机号", Toast.LENGTH_SHORT);
                    tv_reg_get_code.setClickable(false);
                    tv_reg_get_code.setTextColor(getResources().getColor(R.color.item_content_color));
                } else {
                    tv_reg_get_code.setClickable(true);
                    tv_reg_get_code.setTextColor(getResources().getColor(R.color.orange));

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
            tv_reg_get_code.setClickable(false);
            tv_reg_get_code.setTextColor(getResources().getColor(R.color.item_content_color));
            tv_reg_get_code.setText("获取验证码(" + millisUntilFinished / 1000 + "S)");
        }

        @Override
        public void onFinish() {
            tv_reg_get_code.setText("获取验证码(60S)");
            tv_reg_get_code.setTextColor(getResources().getColor(R.color.orange));
            tv_reg_get_code.setClickable(true);
        }

    }


    /**
     * @param
     * @return 说明返回值含义
     * @Description 前端判断能否注册
     */
    private boolean canGetCode() {
        // 手机号码正则判断
        String telRegex = "^(1([34578][0-9]))\\d{8}$";
        if (TextUtils.isEmpty(et_reg_phone.getText().toString().trim())) {
            ToastUtil.showToast(getApplicationContext(), "手机号码不能为空", Toast.LENGTH_SHORT);
            return false;
        } else if (!et_reg_phone.getText().toString().matches(telRegex)) {
            ToastUtil.showToast(getApplicationContext(), "请输入正确的手机号", Toast.LENGTH_SHORT);
            tv_reg_get_code.setClickable(false);
            tv_reg_get_code.setTextColor(getResources().getColor(R.color.item_content_color));
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
        mobile = et_reg_phone.getText().toString().trim();
        String telRegex = "^(1([34578][0-9]))\\d{8}$";
        if (TextUtils.isEmpty(mobile)) {
            ToastUtil.showToast(getApplicationContext(), "手机号码不能为空", Toast.LENGTH_SHORT);
            return false;
        } else if (!mobile.matches(telRegex)) {
            ToastUtil.showToast(getApplicationContext(), "请输入正确的手机号", Toast.LENGTH_SHORT);
            tv_reg_get_code.setClickable(false);
            tv_reg_get_code.setTextColor(getResources().getColor(R.color.item_content_color));
            return false;
        }
        return true;

    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        // TODO Auto-generated method stub
        switch (v.getId()) {
            case R.id.btn_next:
                if (canGetCode()) {
                    if (CheckFlag) {
                        code = et_reg_message.getText().toString().trim();
                        checkCode(mobile, code);
                    } else {
                        ToastUtil.showToast(getApplicationContext(), "你还没同意华盟教育注册协议", Toast.LENGTH_SHORT);
                    }
                    break;
                }
                break;
            case R.id.tv_reg_Agreement_name:
                gotoOtherActivity(RegAgreementActivity.class);
                break;
            case R.id.tv_reg_get_code:
                if (canSendCode()) {
                    sendCode(mobile);
                    break;
                }

                break;

            default:
                break;
        }
    }

    private void sendCode(String mobile) {
        HashMap<String, String> param = new HashMap<String, String>();
        param.put("mobile", mobile);
        param.put("sign", "");
        HttpVolleyRequest<BaseData> request = new HttpVolleyRequest<BaseData>(this);
        request.HttpVolleyRequestPost(Urls.SEND_CODE, param, BaseData.class, null,
                sendCodeSuccessListener(), null,1);

    }

    public Response.Listener<BaseData> sendCodeSuccessListener() {
        return new Response.Listener<BaseData>() {

            @Override
            public void onResponse(BaseData response) {
                DialogUtils.cancleProgressDialog();
                if(response.status.equals("1")){
                    ToastUtil.showToast(getApplicationContext(), "验证码发送失败", Toast.LENGTH_SHORT);
                }else{
                    ToastUtil.showToast(getApplicationContext(), "验证码发送成功", Toast.LENGTH_SHORT);
                    TimeCount tc = new TimeCount(60000, 1000);
                    tc.start();
                }
            }
        };
    }

    private void checkCode(String mobile, String code) {
        HashMap<String, String> param = new HashMap<String, String>();
        param.put("mobile", mobile);
        param.put("code", code);
        HttpVolleyRequest<BaseData> request = new HttpVolleyRequest<BaseData>(this);
        request.HttpVolleyRequestPost(Urls.CHECK_CODE, param, BaseData.class, null,
                checkCodeSuccessListener(), null);

    }

    public Response.Listener<BaseData> checkCodeSuccessListener() {
        return new Response.Listener<BaseData>() {

            @Override
            public void onResponse(BaseData response) {
                DialogUtils.cancleProgressDialog();
                Intent intent = new Intent(getApplicationContext(), RegisterActivity.class);
                intent.putExtra("mobile", mobile);
                intent.putExtra("code", code);
                startActivity(intent);
            }
        };
    }
}
