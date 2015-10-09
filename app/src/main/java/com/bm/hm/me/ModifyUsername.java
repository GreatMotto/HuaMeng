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
import com.bm.hm.util.SharedPreferencesUtils;
import com.bm.hm.util.ToastUtil;

import java.util.HashMap;

/**
 * Created by liuz on 2015/4/24.
 */
public class ModifyUsername extends BaseActivity {

    private EditText et_modify_username;
    private Button btn_modify_name_sure;
    private String nikeName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ac_modify_username);

        initView();
    }

    private void initView() {
        initTitleBarWithBack("昵称");
        //昵称
        et_modify_username = (EditText) findViewById(R.id.et_modify_username);
        //确定
        btn_modify_name_sure = (Button) findViewById(R.id.btn_modify_name_sure);
        btn_modify_name_sure.setOnClickListener(this);

        nikeName = getIntent().getStringExtra("nickname");
        et_modify_username.setText(nikeName);
}

    private Boolean canModifyName() {
        if (TextUtils.isEmpty(et_modify_username.getText().toString().trim())) {
            DialogUtils.dialogToast("昵称不能为空", ModifyUsername.this);
//            ToastUtil.showToast(getApplicationContext(), "新密码不能为空", Toast.LENGTH_SHORT);
            return false;
        }
        if ((et_modify_username.getText().toString().trim().length()) < 3) {
            DialogUtils.dialogToast("昵称不符合规范,请输入3-8个字符", ModifyUsername.this);
//            ToastUtil.showToast(getApplicationContext(), "新密码不符合规范,请输入6-16个字符", Toast.LENGTH_SHORT);
            return false;
        }
        if ((et_modify_username.getText().toString().trim().length()) > 8) {
            DialogUtils.dialogToast("昵称不符合规范,请输入3-8个字符", ModifyUsername.this);
//            ToastUtil.showToast(getApplicationContext(), "新密码不符合规范,请输入6-16个字符", Toast.LENGTH_SHORT);
            return false;
        }
        return true;
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        // TODO Auto-generated method stub
        switch (v.getId()) {
            case R.id.btn_modify_name_sure:
                if (canModifyName()) {
                    if (nikeName.equals(et_modify_username.getText().toString().trim())) {
                        finish();
                    } else {
                        nicknameRequest();
                    }
                }
                break;

            default:
                break;
        }
    }

    /**
     * @Description 修改昵称  请求后台数据
     */
    private void nicknameRequest() {
        HashMap<String, String> param = new HashMap<String, String>();
        param.put("userId", String.valueOf(SharedPreferencesUtils.getInstance().getInt(Constant.USER_ID)));
        param.put("nickname", et_modify_username.getText().toString().trim());
        HttpVolleyRequest<BaseData> request = new HttpVolleyRequest<BaseData>(this);
        request.HttpVolleyRequestPost(Urls.UPDATE_USERINFO, param, BaseData.class, User.class,
                modifyNameSuccessListener(), null);
    }

    //回调成功
    private Response.Listener<BaseData> modifyNameSuccessListener() {
        return new Response.Listener<BaseData>() {

            @Override
            public void onResponse(BaseData response) {
                ToastUtil.showToast(getApplicationContext(), "昵称修改成功", Toast.LENGTH_SHORT);
                getIntent().putExtra("nickname", et_modify_username.getText().toString().trim());
                setResult(5, getIntent());

                SharedPreferencesUtils.getInstance().putString(Constant.NICKNAME, et_modify_username.getText().toString().trim()); //昵称
                finish();
            }
        };
    }
}
