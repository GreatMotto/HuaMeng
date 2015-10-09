package com.bm.hm.http;

import java.lang.ref.SoftReference;
import java.util.LinkedHashMap;

import android.graphics.Bitmap;

import com.android.volley.toolbox.ImageLoader.ImageCache;

/**
 * @description bitmap软引用
 * @author guoky@bluemobi.sh.cn
 * 
 * @time 2014-12-23下午3:01:47
 */
public class BitmapSoftRefCache implements ImageCache {

    private static final String TAG = "BitmapSoftRefCache";

    private LinkedHashMap<String, SoftReference<Bitmap>> map;

    public BitmapSoftRefCache() {
        map = new LinkedHashMap<String, SoftReference<Bitmap>>();
    }

    /**
     * 从软引用集合中得到Bitmap对象
     */
    @Override
    public Bitmap getBitmap(String url) {
        Bitmap bitmap = null;
        SoftReference<Bitmap> softRef = map.get(url);
        if (softRef != null) {
            bitmap = softRef.get();
            if (bitmap == null) {
                map.remove(url); // 从map中移除
            }
            else {
            }
        }
        return bitmap;
    }

    /**
     * 从软引用集合中添加bitmap对象
     */
    @Override
    public void putBitmap(String url, Bitmap bitmap) {
        SoftReference<Bitmap> softRef = new SoftReference<Bitmap>(bitmap);
        map.put(url, softRef);
    }

}