package com.bm.hm.user;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.bm.hm.R;
import com.bm.hm.base.BaseActivity;
import com.bm.hm.constant.Urls;
import com.android.volley.Response.Listener;
import com.bm.hm.http.BaseData;
import com.bm.hm.http.HttpVolleyRequest;
import com.bm.hm.util.DialogUtils;
import com.bm.hm.util.MD5Util;
import com.bm.hm.util.ToastUtil;

import java.util.HashMap;

/**
 * Created by liuz on 2015/4/22.
 */
public class RegisterActivity extends BaseActivity {

    private EditText et_reg_username, et_reg_password, et_reg_password_sure, et_reg_username_phone;
    private Button btn_register;
    private String mobile,code;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ac_register);

        initView();
    }

    private void initView() {
        mobile = getIntent().getStringExtra("mobile");
        code = getIntent().getStringExtra("code");

        initTitleBarWithBack("注册");
        //昵称
        et_reg_username = (EditText) findViewById(R.id.et_reg_username);
        //密码
        et_reg_password = (EditText) findViewById(R.id.et_reg_password);
        //再次确认密码
        et_reg_password_sure = (EditText) findViewById(R.id.et_reg_password_sure);
        //推荐人手机
        et_reg_username_phone = (EditText) findViewById(R.id.et_reg_username_phone);
        //注册
        btn_register = (Button) findViewById(R.id.btn_register);

        btn_register.setOnClickListener(this);
        et_reg_password.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus)
                    if ((et_reg_password.getText().toString().trim().length()) < 6) {
                        ToastUtil.showToast(getApplicationContext(), "密码不符合规范,请输入6-16个字符", Toast.LENGTH_SHORT);
                    } else if ((et_reg_password.getText().toString().trim().length()) > 16) {
                        ToastUtil.showToast(getApplicationContext(), "密码不符合规范,请输入6-16个字符", Toast.LENGTH_SHORT);
                    }
            }
        });
    }


    /**
     * @param
     * @return 说明返回值含义
     * @Description 前端判断能否注册
     */
    private boolean canReg() {
        // 获取编辑框电话号码
        String phoneNumber = et_reg_username_phone.getText().toString().trim();
        String telRegex = "^(1([34578][0-9]))\\d{8}$";
        if (TextUtils.isEmpty(et_reg_username.getText().toString().trim())) {
            DialogUtils.dialogToast("昵称不能为空", RegisterActivity.this);
//            ToastUtil.showToast(getApplicationContext(), "昵称不能为空", Toast.LENGTH_SHORT);
            return false;
        }
        if ((et_reg_username.getText().toString().trim().length()) > 8) {
            DialogUtils.dialogToast("昵称不符合规范,请输入3-8个字符", RegisterActivity.this);
//            ToastUtil.showToast(getApplicationContext(), "昵称不符合规范,请输入3-8个字符", Toast.LENGTH_SHORT);
            return false;
        }
        if ((et_reg_username.getText().toString().trim().length()) < 3) {
            DialogUtils.dialogToast("昵称不符合规范,请输入3-8个字符", RegisterActivity.this);
//            ToastUtil.showToast(getApplicationContext(), "昵称不符合规范,请输入3-8个字符", Toast.LENGTH_SHORT);
            return false;
        }
        if (TextUtils.isEmpty(et_reg_password.getText().toString().trim())) {
            DialogUtils.dialogToast("密码不能为空", RegisterActivity.this);
//            ToastUtil.showToast(getApplicationContext(), "密码不能为空", Toast.LENGTH_SHORT);
            return false;
        }
        if ((et_reg_password.getText().toString().trim().length()) < 6) {
            DialogUtils.dialogToast("密码不符合规范,请输入6-16个字符", RegisterActivity.this);
//            ToastUtil.showToast(getApplicationContext(), "密码不符合规范,请输入6-16个字符", Toast.LENGTH_SHORT);
            return false;
        }
        if ((et_reg_password.getText().toString().trim().length()) > 16) {
            DialogUtils.dialogToast("密码不符合规范,请输入6-16个字符", RegisterActivity.this);
//            ToastUtil.showToast(getApplicationContext(), "密码不符合规范,请输入6-16个字符", Toast.LENGTH_SHORT);
            return false;
        }
        if (TextUtils.isEmpty(et_reg_password_sure.getText().toString().trim())) {
            DialogUtils.dialogToast("密码不能为空", RegisterActivity.this);
//            ToastUtil.showToast(getApplicationContext(), "密码不能为空", Toast.LENGTH_SHORT);
            return false;
        }
        if (!(et_reg_password.getText().toString()).equals(et_reg_password_sure.getText().toString())) {
            DialogUtils.dialogToast("两次密码不一致,请核对后再输入", RegisterActivity.this);
//            ToastUtil.showToast(getApplicationContext(), "两次密码不一致,请核对后再输入", Toast.LENGTH_SHORT);
            return false;
        }
        if (!TextUtils.isEmpty(et_reg_username_phone.getText().toString().trim())) {
            if (!phoneNumber.matches(telRegex)) {
                DialogUtils.dialogToast("请输入正确的手机号", RegisterActivity.this);
//                ToastUtil.showToast(getApplicationContext(), "请输入正确的手机号", Toast.LENGTH_SHORT);
                return false;
            }
        }
        return true;
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        // TODO Auto-generated method stub
        switch (v.getId()) {
            case R.id.btn_register:
                if (canReg()) {
                    String password = MD5Util.encodeByMD5(et_reg_password.getText().toString().trim());
                    String nickname = et_reg_username.getText().toString().trim();
                    String recommendMobile = et_reg_username_phone.getText().toString().trim();
                    register(mobile,code,password,nickname,recommendMobile);
                }
                break;

            default:
                break;
        }
    }

    private void register(String mobile, String code,String password, String nickname,String recommendMobile) {
        DialogUtils.showProgressDialog("正在提交", this);
        HashMap<String, String> param = new HashMap<String, String>();
        param.put("mobile", mobile);
        param.put("code", code);
        param.put("password", MD5Util.encodeByMD5(password));
        param.put("nickname", nickname);
        param.put("recommendMobile", recommendMobile);
        HttpVolleyRequest<BaseData> request = new HttpVolleyRequest<BaseData>(this);
        request.HttpVolleyRequestPost(Urls.REGISTER, param, BaseData.class, null,
                registSuccessListener(), null,1);

    }

    public Listener<BaseData> registSuccessListener() {
        return new Listener<BaseData>()
        {

            @Override
            public void onResponse(BaseData response) {
                DialogUtils.cancleProgressDialog();
                if(response.status.equals("1")){
                    ToastUtil.showToast(getApplicationContext(),"发送验证码失败",Toast.LENGTH_SHORT);
                }else{
                    ToastUtil.showToast(getApplicationContext(), "注册成功", Toast.LENGTH_SHORT);
                    startActivity(new Intent(getApplicationContext(),LoginActivity.class));
                }
            }
        };
    }

}
