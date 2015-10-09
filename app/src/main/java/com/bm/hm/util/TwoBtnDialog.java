package com.bm.hm.util;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.bm.hm.R;

public class TwoBtnDialog extends Dialog {

    private Context mContext;
    private TextView tv_content;
    private TextView btn_left, btn_right;
    private String mcontent;

    /**
     * 按钮点击事件 默认关闭dialog
     */
    private View.OnClickListener mOnClickListener = new View.OnClickListener()
    {

        @Override
        public void onClick(View v) {
            dismiss();
        }
    };

    public TwoBtnDialog(Context context, String content,
            View.OnClickListener onClickListener) {
        super(context, R.style.Theme_dialog);
        mContext = context;
        mcontent = content;
        if (null != onClickListener) {
            mOnClickListener = onClickListener;
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_two_btn);
        tv_content = (TextView) findViewById(R.id.tv_content);
        btn_left = (TextView) findViewById(R.id.btn_left);
        btn_right = (TextView) findViewById(R.id.btn_right);
        btn_left.setOnClickListener(mOnClickListener);
        btn_right.setOnClickListener(mOnClickListener);

        tv_content.setText(mcontent);
    }

    public void show() {
        super.show();
    }

}
