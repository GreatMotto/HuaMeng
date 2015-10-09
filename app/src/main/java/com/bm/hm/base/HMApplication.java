package com.bm.hm.base;

import android.app.ActivityManager;
import android.app.Application;
import android.content.Context;
import android.os.Environment;
import android.text.TextUtils;
import android.util.Log;

import com.bm.hm.constant.Constant;
import com.bm.hm.download.IDCreate;
import com.bm.hm.http.RequestManager;
import com.bm.hm.util.DataCache;
import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;
import com.xckevin.download.DownloadConfig;
import com.xckevin.download.DownloadManager;
import com.xckevin.download.DownloadTask;

import java.io.File;
import java.util.List;

public class HMApplication extends Application {

    static HMApplication instance;
    public DataCache dataCache;
    private final static int RATE = 8; // 默认分配最大空间的几分之一
    public static String json;
    public boolean isOK;
    public static DownloadManager downloadMgr;

    public HMApplication() {
        instance = this;
    }

    public static synchronized HMApplication getInstance() {
        if (instance == null)
            instance = new HMApplication();
        return instance;
    }

    public void onCreate() {
        super.onCreate();
        init();
        initDownload();
    }

    private void init() {
        RequestManager.init(this);
        // 确定在LruCache中，分配缓存空间大小,默认程序分配最大空间的 1/8
        ActivityManager manager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
        int maxSize = manager.getMemoryClass() / RATE; // 比如 64M/8,单位为M
        initImageLoader();
        dataCache = DataCache.get(new File(Environment.getExternalStorageDirectory()
                + Constant.CACHE_JSON_DATA_PATH));
    }

    private void initImageLoader() {
        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(this)
                .threadPriority(Thread.NORM_PRIORITY - 2).denyCacheImageMultipleSizesInMemory()
                .diskCacheFileNameGenerator(new Md5FileNameGenerator())
                .diskCacheSize(50 * 1024 * 1024).tasksProcessingOrder(QueueProcessingType.LIFO)
                .writeDebugLogs().build();
        ImageLoader.getInstance().init(config);
    }

    public DataCache getCache() {
        return dataCache;
    }

    public static String getDeviceInfo(Context context) {
        try {
            org.json.JSONObject json = new org.json.JSONObject();
            android.telephony.TelephonyManager tm = (android.telephony.TelephonyManager) context
                    .getSystemService(Context.TELEPHONY_SERVICE);
            String device_id = tm.getDeviceId();
            android.net.wifi.WifiManager wifi = (android.net.wifi.WifiManager) context
                    .getSystemService(Context.WIFI_SERVICE);
            String mac = wifi.getConnectionInfo().getMacAddress();
            json.put("mac", mac);
            if (TextUtils.isEmpty(device_id)) {
                device_id = mac;
            }
            if (TextUtils.isEmpty(device_id)) {
                device_id = android.provider.Settings.Secure.getString(
                        context.getContentResolver(), android.provider.Settings.Secure.ANDROID_ID);
            }
            json.put("device_id", device_id);
            return json.toString();
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public DisplayImageOptions getOptionsCircle() {
        return new DisplayImageOptions.Builder().showImageOnLoading(0).showImageForEmptyUri(0).displayer(new RoundedBitmapDisplayer(360)).showImageOnFail(0).cacheInMemory(true).cacheOnDisk(true).considerExifParams(true).build();
    }

    public  void initDownload(){
        downloadMgr = DownloadManager.getInstance();

        DownloadConfig.Builder builder = new DownloadConfig.Builder(this);
        String downloadPath = null;
        if(Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())) {
            downloadPath = Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator + "hm"+File.separator+"video";
        } else {
            downloadPath = Environment.getDataDirectory().getAbsolutePath() + File.separator + "hm"+File.separator+"video";
        }
        File downloadFile = new File(downloadPath);
        if(!downloadFile.isDirectory() && !downloadFile.mkdirs()) {
            //throw new IllegalAccessError(" cannot create download folder");
            Log.e("download:","cannot create download folder");
        }
        builder.setDownloadSavePath(downloadPath);
        builder.setMaxDownloadThread(1);
        builder.setDownloadTaskIDCreator(new IDCreate());

        downloadMgr.init(builder.build());
    }

}
