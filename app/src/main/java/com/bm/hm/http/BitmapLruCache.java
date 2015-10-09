package com.bm.hm.http;

import android.graphics.Bitmap;
import android.support.v4.util.LruCache;

import com.android.volley.toolbox.ImageLoader.ImageCache;

public class BitmapLruCache extends LruCache<String, Bitmap> implements ImageCache {

    private static final String TAG = "BitmapLruCache";

    private BitmapSoftRefCache softRefCache;

    public BitmapLruCache(int maxSize) {
        super(maxSize);
        softRefCache = new BitmapSoftRefCache();
    }

    @Override
    protected int sizeOf(String key, Bitmap value) {
        return value.getRowBytes() * value.getHeight();
    }

    @Override
    protected void entryRemoved(boolean evicted, String key, Bitmap oldValue, Bitmap newValue) {
        if (evicted) {
            // 将被挤出的bitmap对象，添加至软引用BitmapSoftRefCache
            softRefCache.putBitmap(key, oldValue);
        }
    }

    /**
     * 得到缓存对象
     */
    @Override
    public Bitmap getBitmap(String url) {
        Bitmap bitmap = get(url);
        // 如果bitmap为null，尝试从软引用缓存中查找
        if (bitmap == null) {
            bitmap = softRefCache.getBitmap(url);
        }
        else {
        }
        return bitmap;
    }

    /**
     * 添加缓存对象
     */
    @Override
    public void putBitmap(String url, Bitmap bitmap) {
        put(url, bitmap);
    }

}