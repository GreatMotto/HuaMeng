package com.bm.hm.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import com.bm.hm.R;

/**
 * 左右滑动小圆点的生成
 */
public class PageIndicatorView extends View {

    private String TAG = "PageIndicatorView";
    private int mCurrentPage = -1;
    private int mTotalPage = 0;
    private int align = 0;

    public int getAlign() {
        return align;
    }

    public void setAlign(int align) {
        this.align = align;
    }

    int imageId;

    public PageIndicatorView(Context context) {
        super(context);
    }

    public PageIndicatorView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public void setTotalPage(int nPageNum) {
        mTotalPage = nPageNum;
        if (mCurrentPage >= mTotalPage)
            mCurrentPage = mTotalPage - 1;
    }

    public int getCurrentPage() {
        return mCurrentPage;
    }

    public void setCurrentPage(int nPageIndex) {
        if (nPageIndex < 0 || nPageIndex >= mTotalPage)
            return;

        if (mCurrentPage != nPageIndex) {
            mCurrentPage = nPageIndex;
            this.invalidate();
        }
    }

    public void setImageId(int imageId) {
        this.imageId = imageId;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        Log.d(TAG, "on draw...");
        Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG | Paint.FILTER_BITMAP_FLAG);
        paint.setStyle(Paint.Style.FILL);
        paint.setColor(Color.BLACK);

        Rect r = new Rect();
        this.getDrawingRect(r);

        int iconWidth = 10;
        int iconHeight = 10;
        int space = 12;

        int x,y = 0;
        if(align == 0){
            x = (r.width() - (iconWidth * mTotalPage + space * (mTotalPage - 1))) / 2;
            y = (r.height() - iconHeight) / 2;
        }else{
            x = r.width() - (iconWidth * mTotalPage + space * (mTotalPage - 1));
            y = (r.height() - iconHeight) / 2;
        }

        for (int i = 0; i < mTotalPage; i++) {

            int resid = R.mipmap.banner_unfocus;

            if (i == mCurrentPage) {
                resid = imageId;
            }

            Rect r1 = new Rect();
            r1.left = x;
            r1.top = y;
            r1.right = x + iconWidth;
            r1.bottom = y + iconHeight;

            Bitmap bmp = BitmapFactory.decodeResource(getResources(), resid);
            canvas.drawBitmap(bmp, null, r1, paint);

            x += iconWidth + space;

        }

    }

}
