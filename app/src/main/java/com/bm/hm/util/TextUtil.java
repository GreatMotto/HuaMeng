package com.bm.hm.util;

import android.content.Context;
import android.graphics.Paint;
import android.widget.TextView;

import com.bm.hm.R;

/**
 * Created by fengwh on 2015/4/24.
 */
public class TextUtil {

    /**
     * TextView 的下划线
     *
     * @param tv
     */
    public static void setPintLine(Context context,TextView tv) {
        tv.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG);//下划线
        tv.setTextColor(context.getResources().getColor(R.color.web_blue));
    }
}
