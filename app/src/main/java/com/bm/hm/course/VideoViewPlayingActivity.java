package com.bm.hm.course;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.media.AudioManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;
import android.os.Message;
import android.os.PowerManager;
import android.os.PowerManager.WakeLock;
import android.os.Process;
import android.provider.Settings;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Response;
import com.baidu.cyberplayer.core.BVideoView;
import com.baidu.cyberplayer.core.BVideoView.OnCompletionListener;
import com.baidu.cyberplayer.core.BVideoView.OnErrorListener;
import com.baidu.cyberplayer.core.BVideoView.OnInfoListener;
import com.baidu.cyberplayer.core.BVideoView.OnPlayingBufferCacheListener;
import com.baidu.cyberplayer.core.BVideoView.OnPreparedListener;
import com.baidu.cyberplayer.dlna.IDLNAServiceProvider;
import com.bm.hm.R;
import com.bm.hm.base.HMApplication;
import com.bm.hm.bean.Course;
import com.bm.hm.bean.SightTime;
import com.bm.hm.bean.Video;
import com.bm.hm.constant.Constant;
import com.bm.hm.constant.Urls;
import com.bm.hm.http.BaseData;
import com.bm.hm.http.HttpVolleyRequest;
import com.bm.hm.pay.OrderActivity;
import com.bm.hm.user.LoginActivity;
import com.bm.hm.util.CommentUtils;
import com.bm.hm.util.ScreenUtil;
import com.bm.hm.util.SharedPreferencesUtils;
import com.bm.hm.util.ToastUtil;
import com.google.gson.Gson;
import com.xckevin.download.DownloadListener;
import com.xckevin.download.DownloadTask;
import com.xckevin.download.util.RandomAccessFileUtil;

import java.io.IOException;
import java.io.Serializable;
import java.util.HashMap;
import java.util.List;

public class VideoViewPlayingActivity extends Activity implements OnPreparedListener, OnCompletionListener,
        OnErrorListener, OnInfoListener, OnPlayingBufferCacheListener, IDLNAServiceProvider.IGetVolumeCallBack, IDLNAServiceProvider.ISetVolumeCallBack {

    /**
     * 您的ak
     */
    private String AK = "SEvC7Nx0LW9Rir5D0ng2mHvr";
    /**
     * 您的sk的前16位
     */
    private String SK = "8YLsuM5G6w4eRR1q";

    /**
     * 记录播放位置
     */
    private int mLastPos = 0;

    /**
     * 播放状态
     */
    private enum PLAYER_STATUS {
        PLAYER_IDLE, PLAYER_PREPARING, PLAYER_PREPARED,
    }

    private PLAYER_STATUS mPlayerStatus = PLAYER_STATUS.PLAYER_IDLE;

    private BVideoView mVV = null;
    private ImageButton mPlaybtn = null;//开始、暂停
    //	private ImageButton mPrebtn = null;//后退
//	private ImageButton mForwardbtn = null;//前进
    private LinearLayout mController = null;//控制区域
    private SeekBar mProgress = null;//播放进度
    private TextView mDuration = null;//总的播放时长
    private TextView mCurrPostion = null;//当前播放时长
    private TextView tv_title;//视频名称

    private TextView tv_tishi;//视频的免费提示

    private ImageButton muteButton;//音量
    private ImageView iv_cache;//加入缓存
    private ImageView iv_light;
    private SeekBar seekbar_volume;
    private SeekBar seekbar_light;

    //视频列表
    private List<Video> videoList;
    private View titlebar;
    private PopupWindow popupWindow;
    private ListView lv_video_list;
    private VideoListAdapter adapter;
    private int index;
    private Course course;

    private String mVideoSource = null;//视频路径
    private EventHandler mEventHandler;
    private HandlerThread mHandlerThread;

    private final Object SYNC_Playing = new Object();
    private WakeLock mWakeLock = null;
    private static final String POWER_LOCK = "VideoViewPlayingActivity";
    private boolean mIsHwDecode = false;

    private final int EVENT_PLAY = 0;
    private final int UPDATE_VOLUME = 203;
    private final int UI_EVENT_UPDATE_CURRPOSITION = 1;
    private long mTouchTime;
    private boolean barShow = true;

    private AudioManager audioManager;
    private int curVolume;
    private int maxVolume;

    private AlertDialog alertDialog;
    private int totalSecond;
    private String isBuy;
    private int userId;

    @Override
    public void onGetVolume(boolean isSucess, int result, int errCode,
                            String errDesc) {
        // TODO Auto-generated method stub
        if (isSucess) {
            Message msg = new Message();
            msg.what = UPDATE_VOLUME;
            msg.arg1 = result;
            mUIHandler.sendMessage(msg);
        }
    }

    @Override
    public void onSetVolume(boolean b, int i, String s) {

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.controllerplaying);

        audioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
        curVolume = audioManager.getStreamVolume(AudioManager.STREAM_MUSIC);
        maxVolume = audioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC);

        videoList = (List<Video>) getIntent().getSerializableExtra("videoList");
        index = getIntent().getIntExtra("position", 0);
        course = (Course) getIntent().getSerializableExtra("course");

        isBuy = getIntent().getStringExtra("isBuy");

        for (Video video : videoList) {
            video.setSelect(false);
        }

        PowerManager pm = (PowerManager) getSystemService(Context.POWER_SERVICE);
        mWakeLock = pm.newWakeLock(PowerManager.FULL_WAKE_LOCK | PowerManager.ON_AFTER_RELEASE, POWER_LOCK);

        mIsHwDecode = getIntent().getBooleanExtra("isHW", false);
        Uri uriPath = getIntent().getData();
        if (null != uriPath) {
            String scheme = uriPath.getScheme();
            if (null != scheme) {
                mVideoSource = uriPath.toString();
            } else {
                mVideoSource = uriPath.getPath();
            }
        }
        setLocalPath();

        initUI();

        /**
         * 开启后台事件处理线程
         */
        mHandlerThread = new HandlerThread("event handler thread",
                Process.THREAD_PRIORITY_BACKGROUND);
        mHandlerThread.start();
        mEventHandler = new EventHandler(mHandlerThread.getLooper());
    }

    /**
     * 初始化界面
     */
    private void initUI() {
        initTitle();
        initPopwindow();

        mPlaybtn = (ImageButton) findViewById(R.id.play_btn);
//		mPrebtn = (ImageButton)findViewById(R.id.pre_btn);
//		mForwardbtn = (ImageButton)findViewById(R.id.next_btn);
        mController = (LinearLayout) findViewById(R.id.controlbar);

        mProgress = (SeekBar) findViewById(R.id.media_progress);
        mDuration = (TextView) findViewById(R.id.time_total);
        mCurrPostion = (TextView) findViewById(R.id.time_current);

        tv_tishi=(TextView)findViewById(R.id.tv_tishi);

        registerCallbackForControl();

        /**
         * 设置ak及sk的前16位
         */
        BVideoView.setAKSK(AK, SK);

        /**
         *获取BVideoView对象
         */
        mVV = (BVideoView) findViewById(R.id.video_view);

        /**
         * 注册listener
         */
        mVV.setOnPreparedListener(this);
        mVV.setOnCompletionListener(this);
        mVV.setOnErrorListener(this);
        mVV.setOnInfoListener(this);

        /**
         * 设置解码模式
         */
        mVV.setDecodeMode(mIsHwDecode ? BVideoView.DECODE_HW : BVideoView.DECODE_SW);

        ScreenUtil.setScreenMode(VideoViewPlayingActivity.this, Settings.System.SCREEN_BRIGHTNESS_MODE_MANUAL);
        seekbar_light = (SeekBar) findViewById(R.id.seekbar_light);
        //亮度
        iv_light = (ImageView) findViewById(R.id.iv_light);
        iv_light.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (seekbar_light.getVisibility() == View.VISIBLE) {
                    seekbar_light.setVisibility(View.GONE);
                } else {
                    seekbar_light.setVisibility(View.VISIBLE);
                    ScreenUtil.saveScreenBrightness(VideoViewPlayingActivity.this, ScreenUtil.getScreenBrightness(VideoViewPlayingActivity.this));
                    seekbar_light.setProgress(ScreenUtil.getScreenBrightness(VideoViewPlayingActivity.this) / 100);
                }
            }
        });

        seekbar_light.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                ScreenUtil.setScreenBrightness(VideoViewPlayingActivity.this, seekbar_light.getProgress());
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        //音量
        muteButton = (ImageButton) findViewById(R.id.volume);
        muteButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (seekbar_volume.getVisibility() == View.VISIBLE) {
                    seekbar_volume.setVisibility(View.GONE);
                } else {
                    seekbar_volume.setVisibility(View.VISIBLE);
                }
            }
        });
        seekbar_volume = (SeekBar) findViewById(R.id.seekbar_volume);
        seekbar_volume.setMax(maxVolume);
        seekbar_volume.setProgress(curVolume);
        audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, curVolume,
                AudioManager.FLAG_PLAY_SOUND);
        seekbar_volume.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, progress,
                        AudioManager.FLAG_PLAY_SOUND);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        //加入缓存
        iv_cache = (ImageView) findViewById(R.id.iv_cache);
        iv_cache.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())) {
                    com.xckevin.download.CourseInfo courseInfo = new com.xckevin.download.CourseInfo();
                    courseInfo.setId(course.id);
                    courseInfo.setName(course.name);
                    courseInfo.setImagePath(course.image.path);
                    Gson gson = new Gson();
                    courseInfo.setCourseInfo(gson.toJson(course));

                    Video video = videoList.get(index);
                    DownloadTask task = new DownloadTask();
                    task.setUrl(video.video.path);
                    task.setName(video.name);
                    task.setId(String.valueOf(video.id));
                    task.setCourseId(String.valueOf(course.id));
                    task.setStreamingPath(video.video.streamingPath);
                    HMApplication.downloadMgr.addDownloadTask(task, mListener, courseInfo);

                    ToastUtil.showToast(getApplicationContext(), "已经加入缓存列表", Toast.LENGTH_SHORT);
                } else {
                    ToastUtil.showToast(getApplicationContext(), "该手机无SD卡，无法下载", Toast.LENGTH_SHORT);
                }
            }
        });

    }

    private void initTitle() {
        titlebar = findViewById(R.id.titlebar);

        ImageView iv_back = (ImageView) findViewById(R.id.iv_back);
        iv_back.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        ImageView iv_right = (ImageView) findViewById(R.id.iv_right);
        iv_right.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                popupWindow.showAsDropDown(titlebar, CommentUtils.getWidth(getApplicationContext()) - popupWindow.getWidth(), 0);
            }
        });

        tv_title = (TextView) findViewById(R.id.tv_title);
        tv_title.setText(videoList.get(index).name);
    }

    private void initPopwindow() {
        View pop_view = View.inflate(getApplicationContext(), R.layout.pop_video_list, null);

        lv_video_list = (ListView) pop_view.findViewById(R.id.lv_video_list);
        lv_video_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                index = position;
                setSelect();
                tv_title.setText(videoList.get(position).name);

                mVideoSource = videoList.get(position).video.streamingPath;
                setLocalPath();

                if (mPlayerStatus != PLAYER_STATUS.PLAYER_IDLE) {
                    mVV.stopPlayback();
                }

                /**
                 * 发起一次新的播放任务
                 */
                if (mEventHandler.hasMessages(EVENT_PLAY))
                    mEventHandler.removeMessages(EVENT_PLAY);
                mEventHandler.sendEmptyMessage(EVENT_PLAY);
            }
        });

        setSelect();
        adapter = new VideoListAdapter(getApplicationContext(), videoList);
        lv_video_list.setAdapter(adapter);

//		mController.measure(0,0);
//		int plusHeight = mController.getMeasuredHeight()+45;
//		int height = this.getResources().getDisplayMetrics().heightPixels-CommentUtils.dip2px(getApplicationContext(), plusHeight);
//		ViewGroup.LayoutParams params = lv_video_list.getLayoutParams();
//		params.height = height;
//		lv_video_list.setLayoutParams(params);
        popupWindow = new PopupWindow(pop_view, CommentUtils.getWidth(this) / 3, ViewGroup.LayoutParams.WRAP_CONTENT);
        popupWindow.setFocusable(true);
        popupWindow.setBackgroundDrawable(new BitmapDrawable());
    }

    private void setSelect() {
        for (int i = 0; i < videoList.size(); i++) {
            if (i == index) {
                videoList.get(i).setSelect(true);
            } else {
                videoList.get(i).setSelect(false);
            }
        }
        if (adapter != null) {
            adapter.notifyDataSetChanged();
        }
    }

    /**
     * 为控件注册回调处理函数
     */
    private void registerCallbackForControl() {
        mPlaybtn.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                // TODO Auto-generated method stub

                if (mVV.isPlaying()) {
//					mPlaybtn.setImageResource(R.drawable.play_btn_style);
                    mPlaybtn.setImageResource(R.mipmap.video_start);
                    /**
                     * 暂停播放
                     */
                    mVV.pause();
                } else {
//					mPlaybtn.setImageResource(R.drawable.pause_btn_style);
                    mPlaybtn.setImageResource(R.mipmap.video_pause);
                    /**
                     * 继续播放
                     */
                    mVV.resume();
                }

            }
        });

        /**
         * 实现切换示例
         */
//		mPrebtn.setOnClickListener(new OnClickListener() {
//			public void onClick(View v) {
//				/**
//				 * 如果已经播放了，等待上一次播放结束
//				 */
//				if(mPlayerStatus != PLAYER_STATUS.PLAYER_IDLE){
//					mVV.stopPlayback();
//				}
//
//				/**
//				 * 发起一次新的播放任务
//				 */
//				if(mEventHandler.hasMessages(EVENT_PLAY))
//					mEventHandler.removeMessages(EVENT_PLAY);
//				mEventHandler.sendEmptyMessage(EVENT_PLAY);
//			}
//		});
//
//
//		mForwardbtn.setOnClickListener(new OnClickListener() {
//			public void onClick(View v) {
//				// TODO Auto-generated method stub
//
//			}
//		});

        OnSeekBarChangeListener osbc1 = new OnSeekBarChangeListener() {
            public void onProgressChanged(SeekBar seekBar, int progress,
                                          boolean fromUser) {
                // TODO Auto-generated method stub
                //Log.v(TAG, "progress: " + progress);
                updateTextViewWithTimeFormat(mCurrPostion, progress);
            }

            public void onStartTrackingTouch(SeekBar seekBar) {
                // TODO Auto-generated method stub
                /**
                 * SeekBar开始seek时停止更新
                 */
                mUIHandler.removeMessages(UI_EVENT_UPDATE_CURRPOSITION);
            }

            public void onStopTrackingTouch(SeekBar seekBar) {
                // TODO Auto-generated method stub
                int iseekPos = seekBar.getProgress();
                /**
                 * SeekBark完成seek时执行seekTo操作并更新界面
                 *
                 */
                mVV.seekTo(iseekPos);
                mUIHandler.sendEmptyMessage(UI_EVENT_UPDATE_CURRPOSITION);
            }
        };
        mProgress.setOnSeekBarChangeListener(osbc1);
    }

    private void updateTextViewWithTimeFormat(TextView view, int second) {
        int hh = second / 3600;
        int mm = second % 3600 / 60;
        int ss = second % 60;
        String strTemp = null;
        if (0 != hh) {
            strTemp = String.format("%02d:%02d:%02d", hh, mm, ss);
        } else {
            strTemp = String.format("%02d:%02d", mm, ss);
        }
        view.setText(strTemp);
    }

    @Override
    protected void onPause() {
        // TODO Auto-generated method stub
        super.onPause();
        /**
         * 在停止播放前 你可以先记录当前播放的位置,以便以后可以续播
         */
        if (mPlayerStatus == PLAYER_STATUS.PLAYER_PREPARED) {
            mLastPos = mVV.getCurrentPosition();
            mVV.stopPlayback();
        }

        setLocalPath();

    }

    @Override
    protected void onResume() {
        // TODO Auto-generated method stub
        super.onResume();
        userId = SharedPreferencesUtils.getInstance().getInt(Constant.USER_ID);
        if (null != mWakeLock && (!mWakeLock.isHeld())) {
            mWakeLock.acquire();
        }

        getData();

    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {

        // TODO Auto-generated method stub
        if (event.getAction() == MotionEvent.ACTION_DOWN)
            mTouchTime = System.currentTimeMillis();
        else if (event.getAction() == MotionEvent.ACTION_UP) {
            long time = System.currentTimeMillis() - mTouchTime;
            if (time < 400) {
                updateControlBar(!barShow);
            }
        }

        return true;
    }

    public void updateControlBar(boolean show) {
        if (show) {
            mController.setVisibility(View.VISIBLE);
            titlebar.setVisibility(View.VISIBLE);

        } else {
            mController.setVisibility(View.GONE);
            titlebar.setVisibility(View.GONE);
            seekbar_light.setVisibility(View.INVISIBLE);
            seekbar_volume.setVisibility(View.INVISIBLE);
        }
        barShow = show;
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        /**
         * 退出后台事件处理线程
         */
        mHandlerThread.quit();
    }

    @Override
    public boolean onInfo(int what, int extra) {
        // TODO Auto-generated method stub
        switch (what) {
            /**
             * 开始缓冲
             */
            case BVideoView.MEDIA_INFO_BUFFERING_START:
                break;
            /**
             * 结束缓冲
             */
            case BVideoView.MEDIA_INFO_BUFFERING_END:
                break;
            default:
                break;
        }
        return true;
    }

    /**
     * 当前缓冲的百分比， 可以配合onInfo中的开始缓冲和结束缓冲来显示百分比到界面
     */
    @Override
    public void onPlayingBufferCache(int percent) {
        // TODO Auto-generated method stub

    }

    /**
     * 播放出错
     */
    @Override
    public boolean onError(int what, int extra) {
        // TODO Auto-generated method stub
        synchronized (SYNC_Playing) {
            SYNC_Playing.notify();
        }
        mPlayerStatus = PLAYER_STATUS.PLAYER_IDLE;
        mUIHandler.removeMessages(UI_EVENT_UPDATE_CURRPOSITION);
        return true;
    }

    /**
     * 播放完成
     */
    @Override
    public void onCompletion() {
        // TODO Auto-generated method stub
        synchronized (SYNC_Playing) {
            SYNC_Playing.notify();
        }
        mPlayerStatus = PLAYER_STATUS.PLAYER_IDLE;
        mUIHandler.removeMessages(UI_EVENT_UPDATE_CURRPOSITION);
    }

    /**
     * 准备播放就绪
     */
    @Override
    public void onPrepared() {
        // TODO Auto-generated method stub
        mPlayerStatus = PLAYER_STATUS.PLAYER_PREPARED;
        mUIHandler.sendEmptyMessage(UI_EVENT_UPDATE_CURRPOSITION);
    }

    DownloadListener mListener = new DownloadListener() {
        @Override
        public void onDownloadStart(DownloadTask task) {

        }

        @Override
        public void onDownloadUpdated(DownloadTask task, long finishedSize, long trafficSpeed) {

        }

        @Override
        public void onDownloadPaused(DownloadTask task) {

        }

        @Override
        public void onDownloadResumed(DownloadTask task) {

        }

        @Override
        public void onDownloadSuccessed(DownloadTask task) {

        }

        @Override
        public void onDownloadCanceled(DownloadTask task) {

        }

        @Override
        public void onDownloadFailed(DownloadTask task) {

        }

        @Override
        public void onDownloadRetry(DownloadTask task) {

        }
    };

    private void setLocalPath() {
        if (Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())) {
            DownloadTask task = HMApplication.downloadMgr.findDownloadTaskById(String.valueOf(videoList.get(index).id));

            if (task != null && task.getStatus() == DownloadTask.STATUS_FINISHED) {
                coding(task.getDownloadSavePath());
                mVideoSource = task.getDownloadSavePath();
            }
        }
    }

    private void coding(String path) {
        try {
            RandomAccessFileUtil util = new RandomAccessFileUtil(path);
            util.openFile();
            util.coding();
            util.closeFile();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    class EventHandler extends Handler {
        public EventHandler(Looper looper) {
            super(looper);
        }

        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case EVENT_PLAY:
                    /**
                     * 如果已经播放了，等待上一次播放结束
                     */

                    if (mPlayerStatus != PLAYER_STATUS.PLAYER_IDLE) {
                        synchronized (SYNC_Playing) {
                            try {
                                SYNC_Playing.wait();
                            } catch (InterruptedException e) {
                                // TODO Auto-generated catch block
                                e.printStackTrace();
                            }
                        }
                    }

                    /**
                     * 设置播放url
                     */
                    mVV.setVideoPath(mVideoSource);


                    /**
                     * 续播，如果需要如此
                     */
                    if (mLastPos > 0) {

                        mVV.seekTo(mLastPos);
                        mLastPos = 0;
                    }

                    /**
                     * 显示或者隐藏缓冲提示
                     */
                    mVV.showCacheInfo(true);

                    /**
                     * 开始播放
                     */
                    Log.e("path:", mVideoSource);
                    mVV.start();
                    mPlayerStatus = PLAYER_STATUS.PLAYER_PREPARING;
                    break;
                default:
                    break;
            }
        }
    }

    Handler mUIHandler = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                /**
                 * 更新进度及时间
                 */
                case UI_EVENT_UPDATE_CURRPOSITION:
                    int currPosition = mVV.getCurrentPosition();
                    int duration = mVV.getDuration();

                    updateTextViewWithTimeFormat(mCurrPostion, currPosition);
                    updateTextViewWithTimeFormat(mDuration, duration);
                    mProgress.setMax(duration);
                    mProgress.setProgress(currPosition);

                    mUIHandler.sendEmptyMessageDelayed(UI_EVENT_UPDATE_CURRPOSITION, 200);

                    if(isBuy.equals("1") && currPosition>=totalSecond){
                        showToast(course.score);
                        mLastPos = mVV.getCurrentPosition();
                        mVV.stopPlayback();
                        mController.setVisibility(View.GONE);
                        titlebar.setVisibility(View.GONE);
                        seekbar_light.setVisibility(View.INVISIBLE);
                        seekbar_volume.setVisibility(View.INVISIBLE);
                    }
                    break;
                case UPDATE_VOLUME:
                    seekbar_volume.setProgress((int) (msg.arg1 / 10));
                    break;
                default:
                    break;
            }
        }
    };


    private void getData() {
        HashMap<String, String> param = new HashMap<String, String>();

        HttpVolleyRequest<BaseData> request = new HttpVolleyRequest<BaseData>(this);
        request.HttpVolleyRequestPost(Urls.GET_SIGHT_TIME, param, BaseData.class, SightTime.class,
                getDataSuccessListener(), null);

        if(isBuy.equals("1")) {
            tv_tishi.setVisibility(View.VISIBLE);
        }
    }

    public Response.Listener<BaseData> getDataSuccessListener() {
        return new Response.Listener<BaseData>() {

            @Override
            public void onResponse(BaseData response) {
                totalSecond = response.data.sightTime.hour*60*60+response.data.sightTime.minute*60+response.data.sightTime.second;

                tv_tishi.setText( "可免费观看"+totalSecond+"秒，付费后观看全过程");
//
                Handler handler=new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        tv_tishi.setVisibility(View.GONE);
                    }
                },2000);

                /**
                 * 发起一次播放任务,当然您不一定要在这发起
                 */
                mEventHandler.sendEmptyMessage(EVENT_PLAY);
            }
        };
    }

    private void showToast(int score) {
        AlertDialog.Builder builder = new Builder(this);
        alertDialog = builder.create();
        alertDialog.show();
        Window window = alertDialog.getWindow();
        window.setContentView(R.layout.login_toast);
        TextView tv_queding = (TextView) window.findViewById(R.id.tv_queding);
        TextView tv_quxiao = (TextView) window.findViewById(R.id.tv_quxiao);
        TextView tv_score = (TextView) window.findViewById(R.id.tv_score);
        tv_score.setText(String.valueOf(score));
        TextView tv_login = (TextView) window.findViewById(R.id.tv_login);
        tv_login.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(VideoViewPlayingActivity.this, LoginActivity.class);
                intent.putExtra("from",3);
                startActivity(intent);
            }
        });
        tv_queding.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                if(userId!=0) {
                    Intent intent = new Intent(getApplicationContext(), OrderActivity.class);
                    intent.putExtra("course", (Serializable) course);
                    startActivity(intent);
                }else{
                    Intent intent = new Intent(VideoViewPlayingActivity.this, LoginActivity.class);
                    intent.putExtra("course", (Serializable) course);
                    intent.putExtra("from",5);
                    startActivity(intent);
                }
            }
        });
        tv_quxiao.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}
