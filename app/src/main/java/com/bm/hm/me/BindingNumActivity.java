package com.bm.hm.me;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.Response;
import com.bm.hm.R;
import com.bm.hm.base.BaseActivity;
import com.bm.hm.base.HMApplication;
import com.bm.hm.bean.UserStudentNum;
import com.bm.hm.constant.Constant;
import com.bm.hm.constant.Urls;
import com.bm.hm.http.BaseData;
import com.bm.hm.http.HttpVolleyRequest;
import com.bm.hm.util.SharedPreferencesUtils;
import com.bm.hm.util.ToastUtil;

import java.util.HashMap;

/**
 * Created by chenb on 2015/4/28.
 */
public class BindingNumActivity extends BaseActivity {

    private EditText  et_binding_num;
    private  EditText et_binding_name;
    private Button btn_binding_num;
    private  String number,name;


    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ac_binding_num);

        inieView();
        initdata();

    }

    private void initdata() {
        number=getIntent().getStringExtra("number");
        et_binding_num.setText(number);
        name=getIntent().getStringExtra("name");
        et_binding_name.setText(name);
    }

    private void inieView() {
           initTitleBarWithBack("学号");

        //学号
        et_binding_num=(EditText)findViewById(R.id.et_binding_num);
        et_binding_name=(EditText)findViewById(R.id.et_binding_name);

        //绑定
        btn_binding_num=(Button)findViewById(R.id.btn_binding_num);
        btn_binding_num.setOnClickListener(this);

    }


    public void onClick(View v){
        super.onClick(v);
        switch (v.getId()){
            case  R.id.btn_binding_num:
                if (canBinding()) {
                    bindStudentNumberRequest();
                }
            break;

            default:
                break;

        }
    }

    private boolean canBinding(){
        if(TextUtils.isEmpty(et_binding_num.getText().toString().trim())){
            ToastUtil.showToast(getApplicationContext(), "学号不能为空", Toast.LENGTH_SHORT);
             return  false;
        }
        if(TextUtils.isEmpty(et_binding_name.getText().toString().trim())){
            ToastUtil.showToast(getApplicationContext(), "姓名不能为空", Toast.LENGTH_SHORT);
            return false;
        }
        return true;
    }
    /**
     *
     * @Description 绑定学号 请求后台数据
     */
    private  void bindStudentNumberRequest(){
        HashMap<String, String> param = new HashMap<String,String>();
        param.put("userId",String.valueOf(SharedPreferencesUtils.getInstance().getInt(Constant.USER_ID)));
        param.put("number",et_binding_num.getText().toString().trim());
        param.put("name",  et_binding_name.getText().toString().trim());

        HttpVolleyRequest<BaseData> request = new HttpVolleyRequest<BaseData>(this);
        request.HttpVolleyRequestPost(Urls.BIND_NUM, param, BaseData.class, UserStudentNum.class,
                bindnumSuccessListener(), null,1);
    }
    private Response.Listener<BaseData> bindnumSuccessListener() {
        return new Response.Listener<BaseData>() {

            @Override
            public void onResponse(BaseData result) {
                if (result.status.equals("1")){
                    Intent intent = new Intent(getApplicationContext(),BindingNumChild.class);
                    intent.putExtra("status",1);
                    startActivity(intent);
                }else {
                    HMApplication.getInstance().isOK=true;
                    Intent intent = new Intent(getApplicationContext(),BindingNumChild.class);
                    intent.putExtra("status",2);
                    SharedPreferencesUtils.getInstance().putString(Constant.STUDENTNUMBER,et_binding_num.getText().toString().trim());//学号
                    SharedPreferencesUtils.getInstance().putString(Constant.NAME,et_binding_name.getText().toString().trim()); //姓名

                    startActivity(intent);
                }
                finish();
            }
        };
    }
    
}
