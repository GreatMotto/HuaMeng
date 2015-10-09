package com.bm.hm.view;

import android.content.Context;
import android.view.View;
import android.webkit.WebView;

/**
 * Created by guoky on 2015/5/28.
 */
public class MyVebView extends WebView {
    public MyVebView(Context context) {
        super(context);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int mExpandSpec = View.MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE >> 2, MeasureSpec.AT_MOST);
        super.onMeasure(widthMeasureSpec, mExpandSpec);
    }
}
