package com.bm.hm.util;

import android.app.Dialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.bm.hm.R;

/**
 * 
 * @Description loading
 * @author
 * @time 2014-11-12 下午3:53:11
 */
public class DialogUtils {

    public static Dialog mProgressDialog;
    public static View view;

    /**
     * 
     * @deprecated 显示进度框
     * @param mProgressDialog
     * @param message
     * @param context
     */
    public static void showProgressDialog(String message, Context context) {
        view = LayoutInflater.from(context).inflate(R.layout.comm_popw_view, null);
        TextView title_view = (TextView) view.findViewById(R.id.title);
        title_view.setText(message);

        mProgressDialog = new Dialog(context, R.style.MyDialog);
        mProgressDialog.setContentView(view);
        mProgressDialog.setCanceledOnTouchOutside(false);
        mProgressDialog.show();
    }

    /**
     * 
     * @deprecated 取消加载框
     * @param mProgressDialog
     * @param message
     * @param context
     */
    public static void cancleProgressDialog() {
        if (mProgressDialog != null)
            mProgressDialog.cancel();
    }

    /**
     * 提示语弹出框
     * @param message
     * @param mContext
     */
    public static void dialogToast(String message,Context mContext) {
        CustomDialog.Builder builder = new CustomDialog.Builder(mContext);
        builder.setTitle(R.string.prompt);
        builder.setMessage(message);
        builder.setPositiveButton(R.string.confirm, null);
        builder.create().show();
    }
}