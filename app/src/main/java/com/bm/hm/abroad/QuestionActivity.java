package com.bm.hm.abroad;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.Response;
import com.bm.hm.R;
import com.bm.hm.base.BaseActivity;
import com.bm.hm.bean.Question;
import com.bm.hm.constant.Constant;
import com.bm.hm.constant.Urls;
import com.bm.hm.http.BaseData;
import com.bm.hm.http.HttpVolleyRequest;
import com.bm.hm.util.SharedPreferencesUtils;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class QuestionActivity extends BaseActivity{

    private ImageView iv_resultA,iv_resultB,iv_resultC,iv_resultD;
    private TextView tv_question_name,tv_sum,tv_answerA,tv_answerB,tv_answerC,tv_answerD;
    private List<Question> list = new ArrayList<Question>();
    private int index = 0;
    private String answerInfo="";
    private String name,mobile,school,mail="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ac_question);

        initView();
        getAsk();
    }

    private void initView(){
        name = getIntent().getStringExtra("name");
        school = getIntent().getStringExtra("school");
        mobile = getIntent().getStringExtra("mobile");
        mail = getIntent().getStringExtra("mail");

        initTitleBarWithBack("我要留学");

        iv_resultA = (ImageView)findViewById(R.id.iv_resultA);
        iv_resultA.setOnClickListener(this);

        iv_resultB = (ImageView)findViewById(R.id.iv_resultB);
        iv_resultB.setOnClickListener(this);

        iv_resultC = (ImageView)findViewById(R.id.iv_resultC);
        iv_resultC.setOnClickListener(this);

        iv_resultD = (ImageView)findViewById(R.id.iv_resultD);
        iv_resultD.setOnClickListener(this);

        tv_question_name = (TextView) findViewById(R.id.tv_question_name);
        tv_sum = (TextView) findViewById(R.id.tv_sum);
        tv_answerA = (TextView) findViewById(R.id.tv_answerA);
        tv_answerB = (TextView) findViewById(R.id.tv_answerB);
        tv_answerC = (TextView) findViewById(R.id.tv_answerC);
        tv_answerD = (TextView) findViewById(R.id.tv_answerD);
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()){
            case R.id.iv_resultA:
                if(index+1==list.size()){
                    answerInfo+=(index+1)+",A";
                }else {
                    answerInfo += (index + 1) + ",A" + "@";
                }
                selectAnswer(1);
                break;
            case R.id.iv_resultB:
                if(index+1==list.size()){
                    answerInfo+=(index+1)+",B";
                }else {
                    answerInfo += (index + 1) + ",B" + "@";
                }
                selectAnswer(2);
                break;
            case R.id.iv_resultC:
                if(index+1==list.size()){
                    answerInfo+=(index+1)+",C";
                }else {
                    answerInfo += (index + 1) + ",C" + "@";
                }
                selectAnswer(3);
                break;
            case R.id.iv_resultD:
                if(index+1==list.size()){
                    answerInfo+=(index+1)+",D";
                }else {
                    answerInfo += (index + 1) + ",D" + "@";
                }
                selectAnswer(4);
                break;
        }
    }

    private void selectAnswer(int index){
        if(index == 1){
            iv_resultA.setBackgroundResource(R.mipmap.select);
            iv_resultB.setBackgroundResource(R.mipmap.unselect);
            iv_resultC.setBackgroundResource(R.mipmap.unselect);
            iv_resultD.setBackgroundResource(R.mipmap.unselect);
        }else if(index == 2){
            iv_resultA.setBackgroundResource(R.mipmap.unselect);
            iv_resultB.setBackgroundResource(R.mipmap.select);
            iv_resultC.setBackgroundResource(R.mipmap.unselect);
            iv_resultD.setBackgroundResource(R.mipmap.unselect);
        }else if(index == 3){
            iv_resultA.setBackgroundResource(R.mipmap.unselect);
            iv_resultB.setBackgroundResource(R.mipmap.unselect);
            iv_resultC.setBackgroundResource(R.mipmap.select);
            iv_resultD.setBackgroundResource(R.mipmap.unselect);
        }else if(index == 4){
            iv_resultA.setBackgroundResource(R.mipmap.unselect);
            iv_resultB.setBackgroundResource(R.mipmap.unselect);
            iv_resultC.setBackgroundResource(R.mipmap.unselect);
            iv_resultD.setBackgroundResource(R.mipmap.select);
        }else{
            iv_resultA.setBackgroundResource(R.mipmap.unselect);
            iv_resultB.setBackgroundResource(R.mipmap.unselect);
            iv_resultC.setBackgroundResource(R.mipmap.unselect);
            iv_resultD.setBackgroundResource(R.mipmap.unselect);
        }

        handler.sendEmptyMessageDelayed(0, 100);

    }

    private Handler handler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            if(index == list.size()-1) {
                submitAnswer();
            }else {
                index++;
                selectAnswer(5);
                setData();
            }
        }
    };

    private void getAsk() {
        HashMap<String, String> param = new HashMap<String, String>();
        HttpVolleyRequest<BaseData> request = new HttpVolleyRequest<BaseData>(this);
        request.HttpVolleyRequestPost(Urls.GET_ASK, param, BaseData.class, null,
                getAskListener(), null);

    }

    public Response.Listener<BaseData> getAskListener() {
        return new Response.Listener<BaseData>() {

            @Override
            public void onResponse(BaseData response) {
                list.addAll(response.data.questionList);
                setData();
            }
        };
    }

    public void setData(){
        tv_question_name.setText((index+1)+"/"+list.get(index).content);
        tv_sum.setText((index+1)+"/"+list.size());
        tv_answerA.setText("A."+list.get(index).answerA);
        tv_answerB.setText("B."+list.get(index).answerB);
        tv_answerC.setText("C."+list.get(index).answerC);
        tv_answerD.setText("D."+list.get(index).answerD);



    }

    private void submitAnswer() {
        HashMap<String, String> param = new HashMap<String, String>();
        param.put("userId", String.valueOf(SharedPreferencesUtils.getInstance().getInt(Constant.USER_ID)));
        param.put("userName", name);
        param.put("userSchool", school);
        param.put("userMobile", mobile);
        param.put("userEmail", mail);
        param.put("answerInfo", answerInfo);
        HttpVolleyRequest<BaseData> request = new HttpVolleyRequest<BaseData>(this);
        request.HttpVolleyRequestPost(Urls.SUBMIT_ANSWER, param, BaseData.class, null,
                submitAnswerListener(), null);

    }

    public Response.Listener<BaseData> submitAnswerListener() {
        return new Response.Listener<BaseData>() {

            @Override
            public void onResponse(BaseData response) {
                Intent intent = new Intent(getApplicationContext(), AbroadMessageJoin.class);
                intent.putExtra("result",2);
                startActivity(intent);
                finish();
            }
        };
    }
}
