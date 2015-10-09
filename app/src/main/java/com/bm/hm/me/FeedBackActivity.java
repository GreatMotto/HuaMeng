package com.bm.hm.me;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.Gravity;
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
import com.bm.hm.util.SharedPreferencesUtils;

import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by chenb on 2015/4/28.
 */
public class FeedBackActivity extends BaseActivity {
    private EditText et_feedback_email, et_feedback_jianyi;
    private Button btn_feedback;
    //设置的键值名为email
    private String email;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ac_feedback);

        initView();
    }

    private void initView() {
        initTitleBarWithBack("意见反馈");
        //获取邮箱
        email = getIntent().getStringExtra("email");
        et_feedback_email = (EditText) findViewById(R.id.et_feedback_email);
        et_feedback_jianyi = (EditText) findViewById(R.id.et_feedback_jianyi);

        btn_feedback = (Button) findViewById(R.id.btn_feedback);
        //光标到当前邮箱末尾
        if (!TextUtils.isEmpty(email)) {
            et_feedback_email.setText(email);
            et_feedback_email.setSelection(et_feedback_email.getText().length());
        }
        btn_feedback.setOnClickListener(this);

    }

    public void onClick(View v) {
        super.onClick(v);
        // TODO Auto-generated method stub
        switch (v.getId()) {
            case R.id.btn_feedback:
                if (isOk()) {
                    feedBackRequest();
                }
                break;

            default:
                break;

        }
    }

    private boolean isOk() {
        Pattern pattern = Pattern.compile("^\\s*\\w+(?:\\.{0,1}[\\w-]+)*@[a-zA-Z0-9]+(?:[-.][a-zA-Z0-9]+)*\\.[a-zA-Z]+\\s*$");
        Matcher mathcer = pattern.matcher(et_feedback_email.getText().toString().trim());
        if (TextUtils.isEmpty(et_feedback_email.getText().toString().trim())) {
            Toast.makeText(this, "邮箱不能为空", Toast.LENGTH_SHORT).show();
            return false;
        } else {
            if (!mathcer.find()) {
                Toast.makeText(this, "邮箱格式不正确！", Toast.LENGTH_SHORT).show();
                return false;
            }
        }
        if (TextUtils.isEmpty(et_feedback_jianyi.getText().toString().trim())) {
            Toast.makeText(this, "反馈内容不能为空", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }


    /**
     * @Description 意见反馈  请求后台数据
     */
    private void feedBackRequest() {
        HashMap<String, String> param = new HashMap<String, String>();
        param.put("userId", String.valueOf(SharedPreferencesUtils.getInstance().getInt(Constant.USER_ID)));
        param.put("email", SharedPreferencesUtils.getInstance().getString(Constant.EMAIL));
        param.put("content", et_feedback_jianyi.getText().toString().trim());
        HttpVolleyRequest<BaseData> request = new HttpVolleyRequest<BaseData>(this);
        request.HttpVolleyRequestPost(Urls.FEEDBACK, param, BaseData.class, User.class,
                feedBackSuccessListener(), null);
    }

    private Response.Listener<BaseData> feedBackSuccessListener() {
        return new Response.Listener<BaseData>() {

            @Override
            public void onResponse(BaseData baseData) {
                Toast toast = Toast.makeText(getApplicationContext(), "            " + "提交成功\n感谢您提出的宝贵意见！", Toast.LENGTH_SHORT);
                toast.setGravity(Gravity.CENTER, 0, 0);
                toast.show();
                finish();
            }
        };
    }
}
