package com.njau.mobileplayer.activity;

import io.vov.vitamio.MediaPlayer;
import io.vov.vitamio.Vitamio;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.media.AudioManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.Vibrator;
import android.util.DisplayMetrics;
import android.view.GestureDetector;
import android.view.GestureDetector.SimpleOnGestureListener;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;
import android.widget.Toast;

import com.njau.mobileplayer.R;
import com.njau.mobileplayer.entity.VideoItem;
import com.njau.mobileplayer.entity.VipVideoItem;
import com.njau.mobileplayer.utils.Constant;
import com.njau.mobileplayer.utils.StringUtils;
import com.njau.mobileplayer.utils.Utils;
import com.njau.mobileplayer.view.VitamioVideoView;

/**
 * Vitamio的万能播放器
 * 
 * @author zhangcan
 * 
 */
public class VitamioVIdeoPlayer extends Activity implements
		OnClickListener {

	/**
	 * 视频播放的控制界面
	 */
	private VitamioVideoView vitamio_videoview;
	/**
	 * 传递过来的视频播放地址
	 */
	private Uri uri;
	/**
	 * 本地视频界面中传递过来的视频列表
	 */
	private ArrayList<Object> videoItems;
	/**
	 * 视频界面传递过来被点击的视频位置
	 */
	private int position;

	private RelativeLayout mediaController;
	private LinearLayout topController;
	private LinearLayout showStatusController;
	private TextView videoName;
	private ImageView systemBattery;
	private TextView systemTime;
	private LinearLayout voiceController;
	private Button btVoiceCloseoropen;
	private SeekBar seekbarVoice;
	private Button btSystemChangesystem;
	private LinearLayout bottomController;
	private LinearLayout bottomSeekbar;
	private TextView videoCurrentTime;
	private SeekBar seekbarVideo;
	private TextView videoDuration;
	private LinearLayout bottomVideoController;
	private Button bottomBtExit;
	private Button bottomBtVideoPre;
	private Button bottomBtVideoPauseandplay;
	private Button bottomBtVideoNext;
	private Button bottomBtVideoSwitchFullscreen;
	private TextView video_buffer_netspeed;
	private LinearLayout buffer_caton;
	private LinearLayout video_begin_loading;
	private TextView video_loading_netspeed;
	private Utils utils;

	/**
	 * 监听电量变化的广播
	 */
	private MyBroadcastReceiver myBroadcastReceiver;

	/**
	 * 手势识别器，实现用户和屏幕互动的各种事件
	 */
	private GestureDetector gestureDetector;

	/**
	 * 视频播放的控制面板是否显示 默认false即影藏
	 */
	private boolean isShowMediaController;

	/**
	 * 是否全屏的标志
	 */
	private boolean isFullScreen;

	/**
	 * 屏幕的宽
	 */
	private int screenWidth;

	/**
	 * 屏幕的高
	 */
	private int screenHeight;

	/**
	 * 视频真实的宽
	 */
	private int videoWidth;

	/**
	 * 视频真实的高
	 */
	private int videoHeight;

	/**
	 * 调用声音的系统服务
	 */
	private AudioManager audioManager;
	/**
	 * 当前的音量
	 */
	private int currentVoice;
	/**
	 * 最大的音量
	 */
	private int maxVoice;
	/**
	 * 是否是静音
	 */
	private boolean isMute = false;
	/**
	 * 是否来源于网络的Uri
	 */
	private boolean isNetUri;

	/**
	 * 根据布局文件在网站自动生成的实例化代码 Find the Views in the layout<br />
	 * <br />
	 * Auto-created on 2017-03-25 22:02:40 by Android Layout Finder
	 * (http://www.buzzingandroid.com/tools/android-layout-finder)
	 */
	private void findViews() {
		Vitamio.isInitialized(getApplicationContext());
		
		mediaController = (RelativeLayout) findViewById(R.id.media_controller);
		topController = (LinearLayout) findViewById(R.id.top_controller);
		showStatusController = (LinearLayout) findViewById(R.id.show_status_controller);
		videoName = (TextView) findViewById(R.id.video_name);
		systemBattery = (ImageView) findViewById(R.id.system_battery);
		systemTime = (TextView) findViewById(R.id.system_time);
		voiceController = (LinearLayout) findViewById(R.id.voice_controller);
		btVoiceCloseoropen = (Button) findViewById(R.id.bt_voice_closeoropen);
		seekbarVoice = (SeekBar) findViewById(R.id.seekbar_voice);
		btSystemChangesystem = (Button) findViewById(R.id.bt_system_changesystem);
		bottomController = (LinearLayout) findViewById(R.id.bottom_controller);
		bottomSeekbar = (LinearLayout) findViewById(R.id.bottom_seekbar);
		videoCurrentTime = (TextView) findViewById(R.id.video_current_time);
		seekbarVideo = (SeekBar) findViewById(R.id.seekbar_video);
		videoDuration = (TextView) findViewById(R.id.video_duration);
		bottomVideoController = (LinearLayout) findViewById(R.id.bottom_video_controller);
		bottomBtExit = (Button) findViewById(R.id.bottom_bt_exit);
		bottomBtVideoPre = (Button) findViewById(R.id.bottom_bt_video_pre);
		bottomBtVideoPauseandplay = (Button) findViewById(R.id.bottom_bt_video_pauseandplay);
		bottomBtVideoNext = (Button) findViewById(R.id.bottom_bt_video_next);
		bottomBtVideoSwitchFullscreen = (Button) findViewById(R.id.bottom_bt_video_switch_fullscreen);
		vitamio_videoview = (VitamioVideoView) findViewById(R.id.vitamio_videoview);
		video_buffer_netspeed = (TextView) findViewById(R.id.video_buffer_netspeed);
		buffer_caton = (LinearLayout) findViewById(R.id.buffer_caton);
		video_begin_loading = (LinearLayout) findViewById(R.id.video_begin_loading);
		video_loading_netspeed = (TextView) findViewById(R.id.video_loading_netspeed);
		utils = new Utils();

		gestureDetector = new GestureDetector(this,
				new MySimpleOnGestureListener());

		btVoiceCloseoropen.setOnClickListener(this);
		btSystemChangesystem.setOnClickListener(this);
		bottomBtExit.setOnClickListener(this);
		bottomBtVideoPre.setOnClickListener(this);
		bottomBtVideoPauseandplay.setOnClickListener(this);
		bottomBtVideoNext.setOnClickListener(this);
		bottomBtVideoSwitchFullscreen.setOnClickListener(this);
		
		//立即发送消息更新网络速度
		handler.sendEmptyMessage(Constant.SHOW_SPEED);
	}

	/**
	 * 按钮的点击事件 Handle button click events<br />
	 * <br />
	 * Auto-created on 2017-03-25 22:02:40 by Android Layout Finder
	 * (http://www.buzzingandroid.com/tools/android-layout-finder)
	 */
	@Override
	public void onClick(View v) {
		if (v == btVoiceCloseoropen) {
			isMute = !isMute;
			updateVoice(currentVoice, isMute);
		} else if (v == btSystemChangesystem) {
			// Handle clicks for btSystemChangesystem
		} else if (v == bottomBtExit) {
			this.finish();
		} else if (v == bottomBtVideoPre) {
			this.playPreVIdeo();
		} else if (v == bottomBtVideoPauseandplay) {
			if (this.vitamio_videoview.isPlaying()) {
				// 视频正在播放--设置暂停
				vitamio_videoview.pause();
				bottomBtVideoPauseandplay
						.setBackgroundResource(R.drawable.bottom_bt_video_start_selector);
			} else {
				// 视频暂停--设置播放
				vitamio_videoview.start();
				// 按钮设置--暂停
				bottomBtVideoPauseandplay
						.setBackgroundResource(R.drawable.bottom_bt_video_pause_selector);
			}
		} else if (v == bottomBtVideoNext) {
			this.playNextVideo();
		} else if (v == bottomBtVideoSwitchFullscreen) {
			if (isFullScreen) {
				// 设置默认
				setFullScreenAndDefault(Constant.DEFAULT_SCREEN);
			} else {
				// 设置全屏
				setFullScreenAndDefault(Constant.FULL_SCREEN);
			}
		}
		handler.removeMessages(Constant.HIDE_MEDIA_CONTROLLER);// 先移除隐藏屏幕控制面板消息
		handler.sendEmptyMessageDelayed(Constant.HIDE_MEDIA_CONTROLLER, 4000);// 发消息隐藏屏幕控制面板
	}

	/**
	 * 播放上一个视频
	 */
	private void playPreVIdeo() {
		if (videoItems != null && videoItems.size() > 0) {
			// 播放上一个
			this.position--;
			if (this.position >= 0) {
				video_begin_loading.setVisibility(View.VISIBLE);//设置加载页面可见
				Object obj = videoItems.get(position);
				if(obj instanceof VideoItem){
					VideoItem videoItem = (VideoItem) obj;
					this.videoName.setText(videoItem.getVideoName());// 设置视频的名称
					this.vitamio_videoview.setVideoPath(videoItem.getData());// 设置视频的播放地址
					isNetUri = Utils.isFromNetVideo(videoItem.getData());
				}
				// 设置按钮状态，可不可以点击
				this.setButtonState();
			}
		} else if (uri != null) {
			isNetUri = Utils.isFromNetVideo(uri.toString());
			// 设置按钮上一个下一个可不可以点击
			this.setButtonState();
		}
	}

	/**
	 * 播放下一个视频
	 */
	private void playNextVideo() {
		if (videoItems != null && videoItems.size() > 0) {
			// 播放下一个
			this.position++;
			if (this.position < videoItems.size()) {
				video_begin_loading.setVisibility(View.VISIBLE);//设置加载页面可见
				Object obj = videoItems.get(position);
				if(obj instanceof VideoItem){
					VideoItem videoItem = (VideoItem) obj;
					this.videoName.setText(videoItem.getVideoName());// 设置视频的名称
					this.vitamio_videoview.setVideoPath(videoItem.getData());// 设置视频的播放地址
					isNetUri = Utils.isFromNetVideo(videoItem.getData());
				}
				// 设置按钮状态，可不可以点击
				this.setButtonState();
			}
		} else if (uri != null) {
			isNetUri = Utils.isFromNetVideo(uri.toString());
			// 设置按钮上一个下一个可不可以点击
			this.setButtonState();
		}

	}

	/**
	 * 设置按钮状态，判断可不可以点击
	 */
	private void setButtonState() {
		if (null != videoItems && videoItems.size() > 0) {
			if (videoItems.size() == 1) {
				// 设置两个按钮不可点击
				this.bottomBtVideoPre
						.setBackgroundResource(R.drawable.btn_pre_gray);
				this.bottomBtVideoPre.setEnabled(false);
				this.bottomBtVideoNext
						.setBackgroundResource(R.drawable.btn_next_gray);
				this.bottomBtVideoNext.setEnabled(false);
			} else {
				if (position == 0) { // 上一个按钮不可以点击
					this.bottomBtVideoPre
							.setBackgroundResource(R.drawable.btn_pre_gray);
					this.bottomBtVideoPre.setEnabled(false);
					this.bottomBtVideoNext
							.setBackgroundResource(R.drawable.bottom_bt_video_next_selector);
					this.bottomBtVideoNext.setEnabled(true);
				} else if (position == videoItems.size() - 1) { // 下一个按钮不可以点击
					this.bottomBtVideoPre
							.setBackgroundResource(R.drawable.bottom_bt_video_pre_selector);
					this.bottomBtVideoPre.setEnabled(true);
					this.bottomBtVideoNext
							.setBackgroundResource(R.drawable.btn_next_gray);
					this.bottomBtVideoNext.setEnabled(false);
				} else {// 都可以点击
					this.bottomBtVideoPre
							.setBackgroundResource(R.drawable.bottom_bt_video_pre_selector);
					this.bottomBtVideoPre.setEnabled(true);
					this.bottomBtVideoNext
							.setBackgroundResource(R.drawable.bottom_bt_video_next_selector);
					this.bottomBtVideoNext.setEnabled(true);
				}
			}
		} else if (uri != null) {
			// 设置两个按钮不可点击
			this.bottomBtVideoPre
					.setBackgroundResource(R.drawable.btn_pre_gray);
			this.bottomBtVideoPre.setEnabled(false);
			this.bottomBtVideoNext
					.setBackgroundResource(R.drawable.btn_next_gray);
			this.bottomBtVideoNext.setEnabled(false);
		}
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_vitamio_video_player);

		this.findViews();
		this.setListener();

		// 设置音量及实现音量的变化
		this.setVoiceSeekBar();

		// 设置电量变化
		this.setRegisterBattery();
		// 从视频列表取数据
		this.getDataFromVideoPage();
		// 设置一个播放地址
		this.setData();

	}

	/**
	 * 初始化和音量相关的各种控件 同时设置刚开始播放时候的音量值
	 */
	private void setVoiceSeekBar() {
		audioManager = (AudioManager) getSystemService(AUDIO_SERVICE);
		currentVoice = audioManager.getStreamVolume(AudioManager.STREAM_MUSIC);
		maxVoice = audioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
		// 最大音量和seekBar关联
		seekbarVoice.setMax(maxVoice);
		// 设置当前进度
		seekbarVoice.setProgress(currentVoice);

	}

	/**
	 * 设置播放地址，及数据
	 */
	private void setData() {

		if (null != videoItems && videoItems.size() > 0) {
			Object obj = videoItems.get(position);
			if(obj instanceof VideoItem){
				VideoItem videoItem = (VideoItem) obj;
				this.videoName.setText(videoItem.getVideoName());// 设置视频的名称
				this.vitamio_videoview.setVideoPath(videoItem.getData());// 设置视频的播放地址
				//判断是否来源于网络
				isNetUri = Utils.isFromNetVideo(videoItem.getData());
				
			}else if(obj instanceof VipVideoItem){
				VipVideoItem videoItem = (VipVideoItem) obj;
				this.videoName.setText(videoItem.getVideoname());// 设置视频的名称
				this.vitamio_videoview.setVideoPath(videoItem.getVideosrc());// 设置视频的播放地址
				//判断是否来源于网络
				isNetUri = Utils.isFromNetVideo(videoItem.getVideosrc());
			}

		} else if (null != uri) {
			this.videoName.setText(uri.toString());// 设置视频的名称
			isNetUri = Utils.isFromNetVideo(uri.toString());
			this.vitamio_videoview.setVideoURI(uri);
		} else {
			Toast.makeText(VitamioVIdeoPlayer.this, "你没有传递数据",
					Toast.LENGTH_SHORT).show();
		}
		// 一进来就需要判断并且设置按钮状态
		this.setButtonState();
		// 获取屏幕的宽和高
		getScreenWidthHeight();
	}

	/**
	 * 获取屏幕的宽和高
	 */
	private void getScreenWidthHeight() {
		// 获取屏幕的宽和高
		DisplayMetrics outMetrics = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(outMetrics);
		screenWidth = outMetrics.widthPixels;
		screenHeight = outMetrics.heightPixels;
	}

	/**
	 * 从视频列表中取传递过来的数据
	 */
	@SuppressWarnings("unchecked")
	private void getDataFromVideoPage() {

		uri = getIntent().getData(); //通常来源文件夹和图片浏览器等
		videoItems = (ArrayList<Object>) getIntent().getSerializableExtra(
				"videolist");
		position = (int) getIntent().getIntExtra("position", 0);
	}

	/**
	 * 设置电量变化的广播
	 */
	private void setRegisterBattery() {
		// 注册电量广播
		myBroadcastReceiver = new MyBroadcastReceiver();
		IntentFilter intentFilter = new IntentFilter();
		// 当电量变化时发这个广播
		intentFilter.addAction(Intent.ACTION_BATTERY_CHANGED);
		registerReceiver(myBroadcastReceiver, intentFilter);
	}

	/**
	 * 获取电量变化的广播
	 * 
	 * @author zhangcan
	 * 
	 */
	class MyBroadcastReceiver extends BroadcastReceiver {

		@Override
		public void onReceive(Context context, Intent intent) {
			int level = intent.getIntExtra("level", 0); // 获取电量
			setBattery(level); // 设置电量变化
		}

	}

	/**
	 * 设置各种监听事件
	 */
	private void setListener() {
		// 设置准备好的监听
		vitamio_videoview.setOnPreparedListener(new MyOnPreparedListener());

		// 设置播放出错的监听
		vitamio_videoview.setOnErrorListener(new MyOnErrorListener());

		// 设置播放完成的监听
		vitamio_videoview.setOnCompletionListener(new MyOnCompletionListener());

		// 设置视频播放进度条的拖动监听
		seekbarVideo
				.setOnSeekBarChangeListener(new VideoOnSeekBarChangeListener());

		// 设置音量变化的一个监听
		seekbarVoice
				.setOnSeekBarChangeListener(new VoiceOnSeekBarChangeListener());
		
		//设置视频的监听卡，当视频没有缓冲到播放的地方就弹出一个圈圈即视频卡了
		vitamio_videoview.setOnInfoListener(new MyOnInfoListener());
	}

	/**
	 * 根据电量来设置电量变化的图标
	 * 
	 * @param level
	 */
	private void setBattery(int level) {
		if (level <= 0) {
			systemBattery.setImageResource(R.drawable.ic_battery_0);
		} else if (level <= 10) {
			systemBattery.setImageResource(R.drawable.ic_battery_10);
		} else if (level <= 20) {
			systemBattery.setImageResource(R.drawable.ic_battery_20);
		} else if (level <= 40) {
			systemBattery.setImageResource(R.drawable.ic_battery_40);
		} else if (level <= 60) {
			systemBattery.setImageResource(R.drawable.ic_battery_60);
		} else if (level <= 80) {
			systemBattery.setImageResource(R.drawable.ic_battery_80);
		} else if (level <= 100) {
			systemBattery.setImageResource(R.drawable.ic_battery_100);
		} else {
			systemBattery.setImageResource(R.drawable.ic_battery_100);
		}
	}

	/**
	 * 更改声音的变化，同时根据是否静音来判断设置为静音与否
	 * 
	 * @param progress
	 * @param isMute
	 *            true-->静音
	 */
	private void updateVoice(int progress, boolean isMute) {
		if (isMute) {
			// 静音
			seekbarVoice.setProgress(0);
			audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, 0, 0);
		} else {
			// 非静音
			seekbarVoice.setProgress(progress);
			audioManager
					.setStreamVolume(AudioManager.STREAM_MUSIC, progress, 0);
			currentVoice = progress;
		}
	}

	/**
	 * 设置视频的监听卡的事件，当视频没有缓冲到播放的地方就弹出一个圈圈即视频卡了
	 * @author zhangcan
	 *
	 */
	class MyOnInfoListener implements MediaPlayer.OnInfoListener{

		@Override
		public boolean onInfo(MediaPlayer mp, int what, int extra) {
			switch (what) {
			case MediaPlayer.MEDIA_INFO_BUFFERING_START:
				//视频卡了
//				Toast.makeText(MySystemDefinedVideoPlayer.this, "视频卡了", Toast.LENGTH_SHORT).show();
				buffer_caton.setVisibility(View.VISIBLE);
				break;
			case MediaPlayer.MEDIA_INFO_BUFFERING_END:
				//视频卡结束了，加载到播放的地方
//				Toast.makeText(MySystemDefinedVideoPlayer.this, "视频不卡了", Toast.LENGTH_SHORT).show();
				buffer_caton.setVisibility(View.GONE);
				break;

			}
			return true;
		}
		
	}
	
	/**
	 * 声音进度条改变的监听事件
	 * 
	 * @author zhangcan
	 * 
	 */
	class VoiceOnSeekBarChangeListener implements OnSeekBarChangeListener {

		@Override
		public void onProgressChanged(SeekBar seekBar, int progress,
				boolean fromUser) {
			if (fromUser) {
				if (progress > 0) {
					isMute = false;
				} else {
					isMute = true;
				}
				updateVoice(progress, isMute);
			}
		}

		@Override
		public void onStartTrackingTouch(SeekBar seekBar) {
			handler.removeMessages(Constant.HIDE_MEDIA_CONTROLLER);
		}

		@Override
		public void onStopTrackingTouch(SeekBar seekBar) {
			handler.sendEmptyMessageDelayed(Constant.HIDE_MEDIA_CONTROLLER,
					4000);
		}

	}

	/**
	 * 播放进度条改变的监听事件
	 * 
	 * @author zhangcan
	 * 
	 */
	class VideoOnSeekBarChangeListener implements OnSeekBarChangeListener {

		/**
		 * 当手指滑动时进度条变化，回调这个方法
		 * 
		 * @param fromUser
		 *            是用户引起的为true
		 */
		@Override
		public void onProgressChanged(SeekBar seekBar, int progress,
				boolean fromUser) {
			if (fromUser) {
				vitamio_videoview.seekTo(progress);
			}
		}

		/**
		 * 当手指触碰屏幕时回调这个方法
		 */
		@Override
		public void onStartTrackingTouch(SeekBar seekBar) {
			handler.removeMessages(Constant.HIDE_MEDIA_CONTROLLER);
		}

		/**
		 * 当手纸离开屏幕时回调这个方法
		 */
		@Override
		public void onStopTrackingTouch(SeekBar seekBar) {
			handler.sendEmptyMessageDelayed(Constant.HIDE_MEDIA_CONTROLLER,
					4000);
		}

	}

	/**
	 * 在里面定时每秒刷新进度，得到当前的进度，从而设置播放进度条
	 */
	private Handler handler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case Constant.PROGRESS: // 更新视频播放进度条和播放时间的消息
				// 1.得到当前的播放进度
				int currentPosition = (int) vitamio_videoview.getCurrentPosition();
				// 2.设置seekBar的播放进度，和当前播放的时间
				seekbarVideo.setProgress(currentPosition);
				videoCurrentTime.setText(StringUtils
						.stringForTime(currentPosition));
				// 设置当前系统时间
				systemTime.setText(new SimpleDateFormat("HH:mm:ss")
						.format(new Date()));
				
				//每秒缓冲进度的更新一次的一个算法
				if(isNetUri){
					//只有网络的资源才有缓冲效果
					int buffer = vitamio_videoview.getBufferPercentage();//0-100
					int totalBuffer =  buffer * seekbarVideo.getMax();
					//设置第二层的进度
					seekbarVideo.setSecondaryProgress(totalBuffer/100);
				}else{
					seekbarVideo.setSecondaryProgress(0);
				}
				
				// 3.每秒更新一次
				handler.removeMessages(Constant.PROGRESS);
				handler.sendEmptyMessageDelayed(Constant.PROGRESS, 1000);

				break;
				
			case Constant.HIDE_MEDIA_CONTROLLER: // 隐藏视频控制面板
				mediaController.setVisibility(View.GONE);
				isShowMediaController = false;

				break;
			case Constant.SHOW_SPEED://显示网速
				//1.得到网络速度
				String netSpeed = utils.getNetSpeed(VitamioVIdeoPlayer.this);
				
				//显示网络速
				video_loading_netspeed.setText("加载中..."+ netSpeed);
				video_buffer_netspeed.setText("缓冲中..."+ netSpeed);
				
				//2.每两秒调用一次
				handler.removeMessages(Constant.SHOW_SPEED);
				handler.sendEmptyMessageDelayed(Constant.SHOW_SPEED, 2000);
			}
		}
	};

	/**
	 * 实现视频准备好的监听事件
	 * 
	 * @author zhangcan
	 * 
	 */
	class MyOnPreparedListener implements MediaPlayer.OnPreparedListener {

		// 当底层解码准备好的时候
		@Override
		public void onPrepared(MediaPlayer mp) {
			vitamio_videoview.start(); // 开始播放
			// 隐藏播放视频的控制面板
			mediaController.setVisibility(View.GONE);
			isShowMediaController = false;
			// 获取视频真实的宽和高
			videoWidth = mp.getVideoWidth();
			videoHeight = mp.getVideoHeight();
			// 获取播放的总时长
			int duration = (int) vitamio_videoview.getDuration();
			seekbarVideo.setMax(duration);
			// 设置视频总时长
			videoDuration.setText(StringUtils.stringForTime(duration));
			// 发送更新的消息
			handler.sendEmptyMessage(Constant.PROGRESS);
			//设置默认屏幕
			setFullScreenAndDefault(Constant.DEFAULT_SCREEN);
			//把记载页面给消失掉
			video_begin_loading.setVisibility(View.GONE);
		}
	}

	/**
	 * 实现播放出错的监听
	 * 
	 * @author zhangcan
	 * 
	 */
	class MyOnErrorListener implements MediaPlayer.OnErrorListener {

		@Override
		public boolean onError(MediaPlayer mp, int what, int extra) {
			//播放出错的提示信息
			AlertDialog.Builder builder = new AlertDialog.Builder(VitamioVIdeoPlayer.this);
			builder.setTitle("提示");
			builder.setMessage("无法播放该视频!");
			builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
				
				@Override
				public void onClick(DialogInterface dialog, int which) {
					finish();
				}
			});
			builder.show();
			return true;
		}
	}

	/**
	 * 实现播放完成的监听
	 * 
	 * @author zhangcan
	 * 
	 */
	class MyOnCompletionListener implements MediaPlayer.OnCompletionListener {

		@Override
		public void onCompletion(MediaPlayer mp) {
			Toast.makeText(VitamioVIdeoPlayer.this, "万能播放器播放完成了",
					Toast.LENGTH_SHORT).show();
			playNextVideo();
		}
	}

	/**
	 * 自定义的手势识别器，实现用户和屏幕的交互事件 长按实现视频播放和暂停 单击实现视频控制面板的显示和隐藏 双击实现全屏和默认
	 * 
	 * @author zhangcan
	 * 
	 */
	class MySimpleOnGestureListener extends SimpleOnGestureListener {

		@Override
		public void onLongPress(MotionEvent e) {
			super.onLongPress(e);
			if (vitamio_videoview.isPlaying()) {
				// 视频正在播放--设置暂停
				vitamio_videoview.pause();
				bottomBtVideoPauseandplay
						.setBackgroundResource(R.drawable.bottom_bt_video_start_selector);
			} else {
				// 视频暂停--设置播放
				vitamio_videoview.start();
				// 按钮设置--暂停
				bottomBtVideoPauseandplay
						.setBackgroundResource(R.drawable.bottom_bt_video_pause_selector);
			}
		}

		@Override
		public boolean onDoubleTap(MotionEvent e) {
			// Toast.makeText(MySystemDefinedVideoPlayer.this, "被双击了",
			// Toast.LENGTH_SHORT).show();
			if (isFullScreen) {
				// 设置默认
				setFullScreenAndDefault(Constant.DEFAULT_SCREEN);
			} else {
				// 设置全屏
				setFullScreenAndDefault(Constant.FULL_SCREEN);
			}
			return super.onDoubleTap(e);
		}

		@Override
		public boolean onSingleTapConfirmed(MotionEvent e) {
			// Toast.makeText(MySystemDefinedVideoPlayer.this, "被单击了",
			// Toast.LENGTH_SHORT).show();
			if (!isShowMediaController) {
				mediaController.setVisibility(View.VISIBLE);
				isShowMediaController = true;
				// 若显示则发消息进行隐藏，延迟4s
				handler.sendEmptyMessageDelayed(Constant.HIDE_MEDIA_CONTROLLER,
						4000);
			} else {
				mediaController.setVisibility(View.GONE);
				isShowMediaController = false;
				// 移除隐藏控制面板的消息
				handler.removeMessages(Constant.HIDE_MEDIA_CONTROLLER);

			}
			return super.onSingleTapConfirmed(e);
		}

	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		// 把事件传递给手势识别器
		this.gestureDetector.onTouchEvent(event);

		this.handMoveChangeVoice(event);
		return super.onTouchEvent(event);
	}
	
	/**
	 * 监听物理键实现声音的变化，即手机的调节声音的按键
	 * @author zhangcan
	 */
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if(keyCode == KeyEvent.KEYCODE_VOLUME_DOWN){ //声音减小按键
			currentVoice --;
			updateVoice(currentVoice, false);
			handler.removeMessages(Constant.HIDE_MEDIA_CONTROLLER);
			handler.sendEmptyMessageDelayed(Constant.HIDE_MEDIA_CONTROLLER, 4000);
			return true;
		}else if(keyCode == KeyEvent.KEYCODE_VOLUME_UP){//声音增大按键
			currentVoice ++;
			updateVoice(currentVoice, false);
			handler.removeMessages(Constant.HIDE_MEDIA_CONTROLLER);
			handler.sendEmptyMessageDelayed(Constant.HIDE_MEDIA_CONTROLLER, 4000);
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}

	/**
	 * 手指开始滑动的位置
	 */
	private float startY;
	/**
	 * 屏幕的高
	 */
	private float touchScreenHeight;
	/**
	 * 当一按下时的音量
	 */
	private int moveVoice;
	
	/**
	 * 震动
	 */
	private Vibrator vibrator; 
	
	/**
	 * 手指上下滑动左边屏幕时改变亮度大小的算法，当屏幕达到最亮或最暗（20%）的时候，设备会震动
	 * 手指上下滑动右边屏幕时改变声音大小的算法
	 * 
	 * @param event
	 */
	private void handMoveChangeVoice(MotionEvent event) {
		// 手指上下滑动时改变音量
		switch (event.getAction()) {
		case MotionEvent.ACTION_DOWN: // 手指按下
			// 1.按下就需要记录值
			startY = event.getY();
			moveVoice = audioManager.getStreamVolume(AudioManager.STREAM_MUSIC);
			touchScreenHeight = Math.min(screenHeight, screenWidth);
			// 移除掉隐藏控制面板的消息
			handler.removeMessages(Constant.HIDE_MEDIA_CONTROLLER);
			break;
		case MotionEvent.ACTION_MOVE: // 手指滑动
			// 2.移动时记录相关值
			float endY = event.getY();
			float endX = event.getX();
			float distanceY = startY - endY;
			
			if(endX > screenWidth/2){
				//右边屏幕调节声音
				// 滑动屏幕的距离:总距离 = 改变声音:音量最大值
				// 改变声音 = （滑动屏幕的距离:总距离即屏幕高）*音量最大值
				float changeVoice = (distanceY / touchScreenHeight) * maxVoice;
				// 最终声音 = 原来的 +改变声音
				int finalVoice = (int) (moveVoice + changeVoice);
				finalVoice = (finalVoice <= maxVoice) ? finalVoice : maxVoice;
				finalVoice = (finalVoice >= 0) ? finalVoice : 0;
				if (changeVoice != 0) {
					isMute = false;
					updateVoice(finalVoice, isMute);
				}
			}else{
				//左边屏幕，调节亮度
				final double FLING_MIN_DISTANCE = 0.5;  
                final double FLING_MIN_VELOCITY = 0.5;  
                if (distanceY > FLING_MIN_DISTANCE  
                        && Math.abs(distanceY) > FLING_MIN_VELOCITY) {  
                    setBrightness(20);  
                }  
                if (distanceY < FLING_MIN_DISTANCE  
                        && Math.abs(distanceY) > FLING_MIN_VELOCITY) {  
                    setBrightness(-20);  
                }
			}
			
			break;
		case MotionEvent.ACTION_UP: // 手指抬起
			// 手指离开屏幕时重新发送影藏控制面板的消息
			handler.sendEmptyMessageDelayed(Constant.HIDE_MEDIA_CONTROLLER,
					4000);
			break;
		}
	}
	
	/**
	 * 设置屏幕亮度 lp = 0 全暗 ，lp= -1,根据系统设置， lp = 1; 最亮  
	 * @param brightness
	 */
    public void setBrightness(float brightness) {  
        WindowManager.LayoutParams lp = getWindow().getAttributes();  
        // if (lp.screenBrightness <= 0.1) {  
        // return;  
        // }  
        lp.screenBrightness = lp.screenBrightness + brightness / 255.0f;  
        if (lp.screenBrightness > 1) {  
            lp.screenBrightness = 1;  
            vibrator = (Vibrator) getSystemService(VIBRATOR_SERVICE);  
            long[] pattern = { 10, 200 }; // OFF/ON/OFF/ON...  
            vibrator.vibrate(pattern, -1);  
        } else if (lp.screenBrightness < 0.2) {  
            lp.screenBrightness = (float) 0.2;  
            vibrator = (Vibrator) getSystemService(VIBRATOR_SERVICE);  
            long[] pattern = { 10, 200 }; // OFF/ON/OFF/ON...  
            vibrator.vibrate(pattern, -1);  
        }  
        getWindow().setAttributes(lp);  
    }  

	@Override
	protected void onDestroy() {
		if (null != myBroadcastReceiver) {
			// 销毁动态注册的广播
			unregisterReceiver(myBroadcastReceiver);
			myBroadcastReceiver = null;
		}
		super.onDestroy();
	}

	/**
	 * 设置视频全屏或默认播放 设置默认播放时的一个算法
	 * 
	 * @param defaultScreen
	 */
	private void setFullScreenAndDefault(int defaultScreen) {
		switch (defaultScreen) {
		case Constant.FULL_SCREEN: // 全屏
			// 设置视频画面大小
			vitamio_videoview.setVideoSize(screenWidth, screenHeight);
			// 设置全屏按钮状态--为默认
			bottomBtVideoSwitchFullscreen
					.setBackgroundResource(R.drawable.bottom_bt_video_defaultscreen_selector);
			isFullScreen = true;
			break;

		case Constant.DEFAULT_SCREEN: // 默认
			// 设置视频画面大小
			int mVideoWidth = videoWidth; // 视频真实的宽
			int mVideoHeight = videoHeight;// 视频真实的高

			int width = screenWidth; // 屏幕的宽
			int height = screenHeight;// 屏幕的高

			if (mVideoWidth * height < width * mVideoHeight) {
				width = height * mVideoWidth / mVideoHeight;
			} else if (mVideoWidth * height > width * mVideoHeight) {
				height = width * mVideoHeight / mVideoWidth;
			}

			vitamio_videoview.setVideoSize(width, height);
			// 设置按钮状态--为全屏
			bottomBtVideoSwitchFullscreen
					.setBackgroundResource(R.drawable.bottom_bt_video_fullscreen_selector);
			isFullScreen = false;
			break;

		}
	}
}
