package com.bm.hm.user;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.Response;
import com.bm.hm.R;
import com.bm.hm.base.BaseActivity;
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
public class ChangPasswordActivity extends BaseActivity {

    private EditText et_chang_password, et_chang_password_sure;
    private Button btn_chang_sure;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ac_change_pass);

        initView();

    }

    private void initView() {
        initTitleBarWithBack("找回密码");
        //密码
        et_chang_password = (EditText) findViewById(R.id.et_chang_password);
        //再次确认密码
        et_chang_password_sure = (EditText) findViewById(R.id.et_chang_password_sure);
        //确定
        btn_chang_sure = (Button) findViewById(R.id.btn_chang_sure);
        btn_chang_sure.setOnClickListener(this);

        et_chang_password.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus)
                    if ((et_chang_password.getText().toString().trim().length()) < 6) {
                        ToastUtil.showToast(getApplicationContext(), "密码不符合规范,请输入6-16个字符", Toast.LENGTH_SHORT);
                    } else if ((et_chang_password.getText().toString().trim().length()) > 16) {
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
    private boolean canChang() {

        if (TextUtils.isEmpty(et_chang_password.getText().toString().trim())) {
            ToastUtil.showToast(getApplicationContext(), "密码不能为空", Toast.LENGTH_SHORT);
            return false;
        }
        if ((et_chang_password.getText().toString().trim().length()) < 6) {
            ToastUtil.showToast(getApplicationContext(), "密码不符合规范,请输入6-16个字符", Toast.LENGTH_SHORT);
            return false;
        }
        if ((et_chang_password.getText().toString().trim().length()) > 16) {
            ToastUtil.showToast(getApplicationContext(), "密码不符合规范,请输入6-16个字符", Toast.LENGTH_SHORT);
            return false;
        }
        if (TextUtils.isEmpty(et_chang_password_sure.getText().toString().trim())) {
            ToastUtil.showToast(getApplicationContext(), "密码不能为空", Toast.LENGTH_SHORT);
            return false;
        }
        if (!(et_chang_password.getText().toString()).equals(et_chang_password_sure.getText().toString())) {
            ToastUtil.showToast(getApplicationContext(), "两次密码不一致,请核对后再输入", Toast.LENGTH_SHORT);
            return false;
        }
        return true;
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        // TODO Auto-generated method stub
        switch (v.getId()) {
            case R.id.btn_chang_sure:
                if (canChang()) {
                    changePassword();
                }
                break;

            default:
                break;
        }
    }

    private void changePassword() {
        HashMap<String, String> param = new HashMap<String, String>();
        param.put("mobile", getIntent().getStringExtra("mobile"));
        param.put("newPassword", MD5Util.encodeByMD5(et_chang_password.getText().toString().trim()));
        param.put("code", getIntent().getStringExtra("code"));
        HttpVolleyRequest<BaseData> request = new HttpVolleyRequest<BaseData>(this);
        request.HttpVolleyRequestPost(Urls.CHANGE_PASSWORD, param, BaseData.class, null,
                changePasswordSuccessListener(), null);

    }

    public Response.Listener<BaseData> changePasswordSuccessListener() {
        return new Response.Listener<BaseData>() {

            @Override
            public void onResponse(BaseData response) {
                ToastUtil.showToast(getApplicationContext(), "密码修改成功", Toast.LENGTH_SHORT);
                if(SharedPreferencesUtils.getInstance().getBoolean(Constant.IS_REMEMBER_PASS)) {
                    SharedPreferencesUtils.getInstance().putString(Constant.USER_PASSWORD, et_chang_password.getText().toString().trim());
                }
                gotoOtherActivity(LoginActivity.class);
            }
        };
    }
}
