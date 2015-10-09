package com.bm.hm.me;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.Response;
import com.bm.hm.R;
import com.bm.hm.base.BaseActivity;
import com.bm.hm.bean.User;
import com.bm.hm.constant.Constant;
import com.bm.hm.constant.Urls;
import com.bm.hm.http.BaseData;
import com.bm.hm.http.HttpVolleyRequest;
import com.bm.hm.util.DialogUtils;
import com.bm.hm.util.MD5Util;
import com.bm.hm.util.SharedPreferencesUtils;
import com.bm.hm.util.ToastUtil;

import java.util.HashMap;

/**
 * Created by liuz on 2015/4/24.
 */
public class ModifyPasswordActivity extends BaseActivity {

    private EditText et_old_password, et_new_password, et_new_password_sure;
    private Button btn_modify_sure;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ac_modify_pass);

        initView();

    }

    private void initView() {
        initTitleBarWithBack("登录密码修改");

        //原始密码
        et_old_password = (EditText) findViewById(R.id.et_old_password);
        //新密码
        et_new_password = (EditText) findViewById(R.id.et_new_password);
        //再次输入新密码
        et_new_password_sure = (EditText) findViewById(R.id.et_new_password_sure);
        //确定
        btn_modify_sure = (Button) findViewById(R.id.btn_modify_sure);
        btn_modify_sure.setOnClickListener(this);

    }

    private Boolean canChange() {
        String old_pwd = et_old_password.getText().toString().trim();
        String new_pwd = et_new_password.getText().toString().trim();
        String again_pwd = et_new_password_sure.getText().toString().trim();

        if (TextUtils.isEmpty(old_pwd)) {
            DialogUtils.dialogToast("原始密码不能为空", ModifyPasswordActivity.this);
            return false;
        }
        if (TextUtils.isEmpty(new_pwd)) {
            DialogUtils.dialogToast("新密码不能为空", ModifyPasswordActivity.this);
            return false;
        }
        if (new_pwd.length() < 6 || new_pwd.length() > 16) {
            DialogUtils.dialogToast("新密码不符合规范,请输入6-16位新密码", ModifyPasswordActivity.this);
            return false;
        }
        if (TextUtils.isEmpty(again_pwd)) {
            DialogUtils.dialogToast("再次输入密码不能为空", ModifyPasswordActivity.this);
            return false;
        }
        if (!new_pwd.equals(again_pwd)) {
            DialogUtils.dialogToast("两次密码不一致,请核对后再输入", ModifyPasswordActivity.this);
            return false;
        }
        return true;
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        // TODO Auto-generated method stub
        switch (v.getId()) {
            case R.id.btn_modify_sure:
                if (canChange()) {
                    ModifyPasswordRequest();
                    ToastUtil.showToast(ModifyPasswordActivity.this, "密码修改成功", Toast.LENGTH_SHORT);
                    finish();
                }
                break;

            default:
                break;

        }
    }

    /**
     * @Description 修改密码  请求后台数据
     */
    private void ModifyPasswordRequest() {
        HashMap<String, String> param = new HashMap<String, String>();
        param.put("userId", String.valueOf(SharedPreferencesUtils.getInstance().getInt(Constant.USER_ID)));
        param.put("oldPassword", MD5Util.encodeByMD5(et_old_password.getText().toString().trim()));
        param.put("newPassword", MD5Util.encodeByMD5(et_new_password.getText().toString().trim()));
        HttpVolleyRequest<BaseData> request = new HttpVolleyRequest<BaseData>(this);
        request.HttpVolleyRequestPost(Urls.UPDATEPWD, param, BaseData.class, User.class,
                modifyPasSuccessListener(), null);
    }

    private Response.Listener<BaseData> modifyPasSuccessListener() {
        return new Response.Listener<BaseData>() {

            @Override
            public void onResponse(BaseData baseData) {
                ToastUtil.showToast(getApplicationContext(), "密码修改成功", Toast.LENGTH_SHORT);

            }
        };
    }
}
