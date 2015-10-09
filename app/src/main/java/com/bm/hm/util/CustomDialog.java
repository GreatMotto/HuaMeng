package com.bm.hm.util;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.view.ViewGroup.LayoutParams;

import com.bm.hm.R;

/**
 * Created by liuz on 2015/4/24.
 */
public class CustomDialog extends Dialog {

    public CustomDialog(Context context) {
        super(context);
    }

    public CustomDialog(Context context, int theme) {
        super(context, theme);
    }

    public static class Builder {
        private Context context; //上下文对象
        private String title; //对话框标题
        private String message; //对话框内容
        private String confirm_btnText; //按钮名称“确定”
        private String cancel_btnText; //按钮名称“取消”
        private View contentView; //对话框中间加载的其他布局界面
        /*按钮坚挺事件*/
        private DialogInterface.OnClickListener confirm_btnClickListener;
        private DialogInterface.OnClickListener cancel_btnClickListener;

        public Builder(Context context) {
            this.context = context;
        }

        /*设置对话框信息*/
        public Builder setMessage(String message) {
            this.message = message;
            return this;
        }

        /**
         * Set the Dialog message from resource
         *
         * @param
         * @return
         */
        public Builder setMessage(int message) {
            this.message = (String) context.getText(message);
            return this;
        }

        /**
         * Set the Dialog title from resource
         *
         * @param title
         * @return
         */
        public Builder setTitle(int title) {
            this.title = (String) context.getText(title);
            return this;
        }

        /**
         * Set the Dialog title from String
         *
         * @param title
         * @return
         */
        public Builder setTitle(String title) {
            this.title = title;
            return this;
        }

        /**
         * 设置对话框界面
         *
         * @param v View
         * @return
         */
        public Builder setContentView(View v) {
            this.contentView = v;
            return this;
        }

        /**
         * Set the positive button resource and it's listener
         *
         * @param confirm_btnText
         * @return
         */
        public Builder setPositiveButton(int confirm_btnText,
                                         DialogInterface.OnClickListener listener) {
            this.confirm_btnText = (String) context
                    .getText(confirm_btnText);
            this.confirm_btnClickListener = listener;
            return this;
        }

        /**
         * Set the positive button and it's listener
         *
         * @param confirm_btnText
         * @return
         */
        public Builder setPositiveButton(String confirm_btnText,
                                         DialogInterface.OnClickListener listener) {
            this.confirm_btnText = confirm_btnText;
            this.confirm_btnClickListener = listener;
            return this;
        }

        /**
         * Set the negative button resource and it's listener
         *
         * @param
         * @return
         */
        public Builder setNegativeButton(int cancel_btnText,
                                         DialogInterface.OnClickListener listener) {
            this.cancel_btnText = (String) context
                    .getText(cancel_btnText);
            this.cancel_btnClickListener = listener;
            return this;
        }

        /**
         * Set the negative button and it's listener
         *
         * @param
         * @return
         */
        public Builder setNegativeButton(String cancel_btnText,
                                         DialogInterface.OnClickListener listener) {
            this.cancel_btnText = cancel_btnText;
            this.cancel_btnClickListener = listener;
            return this;
        }

        public CustomDialog create() {
            LayoutInflater inflater = (LayoutInflater) context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            // instantiate the dialog with the custom Theme
            final CustomDialog dialog = new CustomDialog(context, R.style.mystyle);
            View layout = inflater.inflate(R.layout.customdialog, null);
            dialog.addContentView(layout, new LayoutParams(
                    LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
            // set the dialog title
            ((TextView) layout.findViewById(R.id.title)).setText(title);
            ((TextView) layout.findViewById(R.id.title)).getPaint().setFakeBoldText(true);
            ;
            // set the confirm button
            if (confirm_btnText != null) {
                ((Button) layout.findViewById(R.id.confirm_btn))
                        .setOnClickListener(new View.OnClickListener() {
                            public void onClick(View v) {
                                dialog.cancel();
                            }
                        });
            }
            // set the content message
            if (message != null) {
                ((TextView) layout.findViewById(R.id.message)).setText(message);
            }
            dialog.setContentView(layout);
            return dialog;
        }

    }
}