package com.bm.hm.user;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Response;
import com.bm.hm.R;
import com.bm.hm.abroad.AbroadMessageActivity;
import com.bm.hm.abroad.AbroadMessageJoin;
import com.bm.hm.base.BaseActivity;
import com.bm.hm.bean.Course;
import com.bm.hm.bean.User;
import com.bm.hm.constant.Constant;
import com.bm.hm.constant.Urls;
import com.bm.hm.course.CourseInfoActivity;
import com.bm.hm.home.MainActivity;
import com.bm.hm.http.BaseData;
import com.bm.hm.http.HttpVolleyRequest;
import com.bm.hm.me.MessageActivity;
import com.bm.hm.pay.RechargeActivity;
import com.bm.hm.util.DialogUtils;
import com.bm.hm.util.MD5Util;
import com.bm.hm.util.SharedPreferencesUtils;
import com.bm.hm.util.TextUtil;
import com.bm.hm.util.ToastUtil;

import java.util.HashMap;

/**
 * Created by liuz on 2015/4/23.
 */
public class LoginActivity extends BaseActivity {

    private EditText et_log_username, et_log_password;
    private CheckBox cb_log_select;
    private TextView tv_log_forget_password;
    private Button btn_log_login, btn_log_register;
    private int from=0;
    private int tag = 0, flag = 0;
    private Course course;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ac_login);

        initView();
    }

    private void initView() {
        from = getIntent().getIntExtra("from",0);
        tag = getIntent().getIntExtra("tag",0);
        course = (Course) getIntent().getSerializableExtra("course");

        initTitleBarWithBack("登录");

        //账号
        et_log_username = (EditText) findViewById(R.id.et_log_username);
        //密码
        et_log_password = (EditText) findViewById(R.id.et_log_password);

        //忘记密码
        tv_log_forget_password = (TextView) findViewById(R.id.tv_log_forget_password);
        tv_log_forget_password.setOnClickListener(this);
        TextUtil.setPintLine(getApplicationContext(), tv_log_forget_password);
        //登录
        btn_log_login = (Button) findViewById(R.id.btn_log_login);
        btn_log_login.setOnClickListener(this);
        //注册
        btn_log_register = (Button) findViewById(R.id.btn_log_register);
        btn_log_register.setOnClickListener(this);
        //记住密码
        cb_log_select = (CheckBox) findViewById(R.id.cb_log_select);

        et_log_username.setText(SharedPreferencesUtils.getInstance().getString(Constant.USER_MOBILE_NAME));
        if (SharedPreferencesUtils.getInstance().getBoolean(Constant.IS_REMEMBER_PASS)) {
            cb_log_select.setChecked(true);
            et_log_password.setText(SharedPreferencesUtils.getInstance().getString(Constant.USER_PASSWORD));
        }
    }


    /**
     * 判断是否能登录
     *
     * @return
     */
    private boolean canLogin() {
        if (TextUtils.isEmpty(et_log_username.getText().toString().trim())) {
            DialogUtils.dialogToast("昵称不能为空", LoginActivity.this);
            return false;
        }
        if (TextUtils.isEmpty(et_log_password.getText().toString().trim())) {
            DialogUtils.dialogToast("密码不能为空", LoginActivity.this);
            return false;
        }
        if ((et_log_password.getText().toString().trim().length()) < 6) {
            DialogUtils.dialogToast("密码不符合规范,请输入6-16个字符", LoginActivity.this);
            return false;
        }
        if ((et_log_password.getText().toString().trim().length()) > 16) {
            DialogUtils.dialogToast("密码不符合规范,请输入6-16个字符", LoginActivity.this);
            return false;
        }
        return true;
    }


    @Override
    public void onClick(View v) {
        super.onClick(v);
        // TODO Auto-generated method stub
        switch (v.getId()) {
            case R.id.btn_log_login:
                if (canLogin()) {
                    login(et_log_username.getText().toString().trim(), et_log_password.getText().toString().trim());
                }
                break;
            case R.id.btn_log_register:
                gotoOtherActivity(RegGetCodeActivity.class);
                break;
            case R.id.tv_log_forget_password:
                gotoOtherActivity(FindPasswordActivity.class);
                break;

            default:
                break;
        }
    }

    private void login(String mobileOrNickname, String password) {
        DialogUtils.showProgressDialog("正在登录", this);
        HashMap<String, String> param = new HashMap<String, String>();
        param.put("mobileOrNickname", mobileOrNickname);
        param.put("password", MD5Util.encodeByMD5(password));
        HttpVolleyRequest<BaseData> request = new HttpVolleyRequest<BaseData>(this);
        request.HttpVolleyRequestPost(Urls.LOGIN, param, BaseData.class, User.class,
                loginSuccessListener(), null);

    }

    public Response.Listener<BaseData> loginSuccessListener() {
        return new Response.Listener<BaseData>() {

            @Override
            public void onResponse(BaseData response) {
                DialogUtils.cancleProgressDialog();

                SharedPreferencesUtils.getInstance().putInt(Constant.USER_ID, response.data.user.id);
                SharedPreferencesUtils.getInstance().putString(Constant.USER_MOBILE_NAME, et_log_username.getText().toString().trim());
                SharedPreferencesUtils.getInstance().putString(Constant.USER_PASSWORD,et_log_password.getText().toString().trim());
                SharedPreferencesUtils.getInstance().putString(Constant.PHOTO, response.data.user.head.path);  //图片路径
                SharedPreferencesUtils.getInstance().putString(Constant.NICKNAME, response.data.user.nickname); //昵称
                SharedPreferencesUtils.getInstance().putInt(Constant.SCORE, response.data.user.score);// 积分
                SharedPreferencesUtils.getInstance().putInt(Constant.SCALE, response.data.user.teacherLevel);  //等级
                SharedPreferencesUtils.getInstance().putString(Constant.ROLE, response.data.user.role);   //身份
                SharedPreferencesUtils.getInstance().putString(Constant.STUDENTNUMBER,response.data.user.studentNumber.number);//学号

                SharedPreferencesUtils.getInstance().putString(Constant.NAME, response.data.user.name);   //姓名
                SharedPreferencesUtils.getInstance().putString(Constant.EMAIL, response.data.user.email);   //邮箱
                SharedPreferencesUtils.getInstance().putString(Constant.MOBILE, response.data.user.mobile);   //手机

                if (cb_log_select.isChecked()) {
                    SharedPreferencesUtils.getInstance().putBoolean(Constant.IS_REMEMBER_PASS, true);
                }
                ToastUtil.showToast(getApplicationContext(), "登录成功", Toast.LENGTH_SHORT);
                switchToActivity();
                finish();
            }
        };
    }

    private void switchToActivity(){
        switch (from){
            case 0:
                Intent intent1 = new Intent(getApplicationContext(), MainActivity.class);
                intent1.putExtra("tag",tag);
                startActivity(intent1);
                break;
            case 1:
                joinAsk();
                break;
            case 2:
                Intent intent2 = new Intent(getApplicationContext(), RechargeActivity.class);
                intent2.putExtra("from",1);
                startActivity(intent2);
                break;
            case 3:
                Intent intent3 = new Intent(getApplicationContext(), CourseInfoActivity.class);
                intent3.putExtra("course",getIntent().getSerializableExtra("course"));
                startActivity(intent3);
                break;
            case 4:
                Intent intent4 = new Intent(getApplicationContext(), MessageActivity.class);
                startActivity(intent4);
                break;
            case 5:
                Intent intent5=new Intent (getApplicationContext(), CourseInfoActivity.class);
                intent5. putExtra("course", course);
                startActivity(intent5);
                break;

        }
    }

    private void joinAsk() {
        HashMap<String, String> param = new HashMap<String, String>();
        param.put("userId", String.valueOf(SharedPreferencesUtils.getInstance().getInt(Constant.USER_ID)));
        HttpVolleyRequest<BaseData> request = new HttpVolleyRequest<BaseData>(this);
        request.HttpVolleyRequestPost(Urls.JOIN_ASK, param, BaseData.class, null,
                joinAskListener(), null);

    }

    public Response.Listener<BaseData> joinAskListener() {
        return new Response.Listener<BaseData>() {

            @Override
            public void onResponse(BaseData response) {
                finish();
                if(response.data.isSubmit.equals("YES")){
                    Intent intent = new Intent(getApplicationContext(), AbroadMessageJoin.class);
                    intent.putExtra("result", 1);
                    startActivity(intent);
                }else{
                    startActivity(new Intent(getApplicationContext(), AbroadMessageActivity.class));
                }
                finish();
            }
        };
    }
}
