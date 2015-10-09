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
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by liuz on 2015/4/24.
 */
public class ModifyEmailActivity extends BaseActivity {

    private EditText et_modify_email;
    private Button btn_modify_email_sure;
    private String email;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ac_modify_email);
        initView();
    }

    private void initView() {
        initTitleBarWithBack("邮箱");
        //邮箱
        et_modify_email = (EditText) findViewById(R.id.et_modify_email);
        //确定
        btn_modify_email_sure = (Button) findViewById(R.id.btn_modify_email_sure);
        btn_modify_email_sure.setOnClickListener(this);

        email = getIntent().getStringExtra("email");
        et_modify_email.setText(email);
    }

    private boolean canModifyEmail() {
        Pattern pattern = Pattern.compile("^\\s*\\w+(?:\\.{0,1}[\\w-]+)*@[a-zA-Z0-9]+(?:[-.][a-zA-Z0-9]+)*\\.[a-zA-Z]+\\s*$");
        Matcher mathcer = pattern.matcher(et_modify_email.getText().toString().trim());
        if (TextUtils.isEmpty(et_modify_email.getText().toString().trim())) {
            DialogUtils.dialogToast("邮箱不能为空", ModifyEmailActivity.this);
            return false;
        } else {
            if (!mathcer.find()) {
                DialogUtils.dialogToast("邮箱格式不正确，请重新输入", ModifyEmailActivity.this);
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
            case R.id.btn_modify_email_sure:
                if (canModifyEmail()) {
                    if (email.equals(et_modify_email.getText().toString().trim())) {
                        finish();
                    } else {
                        modeifyEmailRequest();
                    }
                }
                break;
            default:
                break;

        }
    }

    /**
     * 修改邮箱
     */
    private void modeifyEmailRequest() {
        HashMap<String, String> param = new HashMap<String, String>();
        param.put("userId", String.valueOf(SharedPreferencesUtils.getInstance().getInt(Constant.USER_ID)));
        param.put("email", et_modify_email.getText().toString().trim());
        HttpVolleyRequest<BaseData> request = new HttpVolleyRequest<BaseData>(this);
        request.HttpVolleyRequestPost(Urls.UPDATE_USERINFO, param, BaseData.class, User.class,
                modifyEmailSuccessListener(), null);
    }

    private Response.Listener<BaseData> modifyEmailSuccessListener() {
        return new Response.Listener<BaseData>() {

            @Override
            public void onResponse(BaseData baseData) {
                ToastUtil.showToast(getApplicationContext(), "邮箱修改成功", Toast.LENGTH_SHORT);
                getIntent().putExtra("email", et_modify_email.getText().toString().trim());
                SharedPreferencesUtils.getInstance().putString(Constant.EMAIL, et_modify_email.getText().toString().trim());   //邮箱
                setResult(6, getIntent());
                SharedPreferencesUtils.getInstance().putString(Constant.EMAIL, et_modify_email.getText().toString().trim()); //邮箱
                finish();
            }
        };
    }
}
