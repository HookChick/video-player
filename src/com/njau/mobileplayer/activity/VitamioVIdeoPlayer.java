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
 * Vitamio�����ܲ�����
 * 
 * @author zhangcan
 * 
 */
public class VitamioVIdeoPlayer extends Activity implements
		OnClickListener {

	/**
	 * ��Ƶ���ŵĿ��ƽ���
	 */
	private VitamioVideoView vitamio_videoview;
	/**
	 * ���ݹ�������Ƶ���ŵ�ַ
	 */
	private Uri uri;
	/**
	 * ������Ƶ�����д��ݹ�������Ƶ�б�
	 */
	private ArrayList<Object> videoItems;
	/**
	 * ��Ƶ���洫�ݹ������������Ƶλ��
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
	 * ���������仯�Ĺ㲥
	 */
	private MyBroadcastReceiver myBroadcastReceiver;

	/**
	 * ����ʶ������ʵ���û�����Ļ�����ĸ����¼�
	 */
	private GestureDetector gestureDetector;

	/**
	 * ��Ƶ���ŵĿ�������Ƿ���ʾ Ĭ��false��Ӱ��
	 */
	private boolean isShowMediaController;

	/**
	 * �Ƿ�ȫ���ı�־
	 */
	private boolean isFullScreen;

	/**
	 * ��Ļ�Ŀ�
	 */
	private int screenWidth;

	/**
	 * ��Ļ�ĸ�
	 */
	private int screenHeight;

	/**
	 * ��Ƶ��ʵ�Ŀ�
	 */
	private int videoWidth;

	/**
	 * ��Ƶ��ʵ�ĸ�
	 */
	private int videoHeight;

	/**
	 * ����������ϵͳ����
	 */
	private AudioManager audioManager;
	/**
	 * ��ǰ������
	 */
	private int currentVoice;
	/**
	 * ��������
	 */
	private int maxVoice;
	/**
	 * �Ƿ��Ǿ���
	 */
	private boolean isMute = false;
	/**
	 * �Ƿ���Դ�������Uri
	 */
	private boolean isNetUri;

	/**
	 * ���ݲ����ļ�����վ�Զ����ɵ�ʵ�������� Find the Views in the layout<br />
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
		
		//����������Ϣ���������ٶ�
		handler.sendEmptyMessage(Constant.SHOW_SPEED);
	}

	/**
	 * ��ť�ĵ���¼� Handle button click events<br />
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
				// ��Ƶ���ڲ���--������ͣ
				vitamio_videoview.pause();
				bottomBtVideoPauseandplay
						.setBackgroundResource(R.drawable.bottom_bt_video_start_selector);
			} else {
				// ��Ƶ��ͣ--���ò���
				vitamio_videoview.start();
				// ��ť����--��ͣ
				bottomBtVideoPauseandplay
						.setBackgroundResource(R.drawable.bottom_bt_video_pause_selector);
			}
		} else if (v == bottomBtVideoNext) {
			this.playNextVideo();
		} else if (v == bottomBtVideoSwitchFullscreen) {
			if (isFullScreen) {
				// ����Ĭ��
				setFullScreenAndDefault(Constant.DEFAULT_SCREEN);
			} else {
				// ����ȫ��
				setFullScreenAndDefault(Constant.FULL_SCREEN);
			}
		}
		handler.removeMessages(Constant.HIDE_MEDIA_CONTROLLER);// ���Ƴ�������Ļ���������Ϣ
		handler.sendEmptyMessageDelayed(Constant.HIDE_MEDIA_CONTROLLER, 4000);// ����Ϣ������Ļ�������
	}

	/**
	 * ������һ����Ƶ
	 */
	private void playPreVIdeo() {
		if (videoItems != null && videoItems.size() > 0) {
			// ������һ��
			this.position--;
			if (this.position >= 0) {
				video_begin_loading.setVisibility(View.VISIBLE);//���ü���ҳ��ɼ�
				Object obj = videoItems.get(position);
				if(obj instanceof VideoItem){
					VideoItem videoItem = (VideoItem) obj;
					this.videoName.setText(videoItem.getVideoName());// ������Ƶ������
					this.vitamio_videoview.setVideoPath(videoItem.getData());// ������Ƶ�Ĳ��ŵ�ַ
					isNetUri = Utils.isFromNetVideo(videoItem.getData());
				}
				// ���ð�ť״̬���ɲ����Ե��
				this.setButtonState();
			}
		} else if (uri != null) {
			isNetUri = Utils.isFromNetVideo(uri.toString());
			// ���ð�ť��һ����һ���ɲ����Ե��
			this.setButtonState();
		}
	}

	/**
	 * ������һ����Ƶ
	 */
	private void playNextVideo() {
		if (videoItems != null && videoItems.size() > 0) {
			// ������һ��
			this.position++;
			if (this.position < videoItems.size()) {
				video_begin_loading.setVisibility(View.VISIBLE);//���ü���ҳ��ɼ�
				Object obj = videoItems.get(position);
				if(obj instanceof VideoItem){
					VideoItem videoItem = (VideoItem) obj;
					this.videoName.setText(videoItem.getVideoName());// ������Ƶ������
					this.vitamio_videoview.setVideoPath(videoItem.getData());// ������Ƶ�Ĳ��ŵ�ַ
					isNetUri = Utils.isFromNetVideo(videoItem.getData());
				}
				// ���ð�ť״̬���ɲ����Ե��
				this.setButtonState();
			}
		} else if (uri != null) {
			isNetUri = Utils.isFromNetVideo(uri.toString());
			// ���ð�ť��һ����һ���ɲ����Ե��
			this.setButtonState();
		}

	}

	/**
	 * ���ð�ť״̬���жϿɲ����Ե��
	 */
	private void setButtonState() {
		if (null != videoItems && videoItems.size() > 0) {
			if (videoItems.size() == 1) {
				// ����������ť���ɵ��
				this.bottomBtVideoPre
						.setBackgroundResource(R.drawable.btn_pre_gray);
				this.bottomBtVideoPre.setEnabled(false);
				this.bottomBtVideoNext
						.setBackgroundResource(R.drawable.btn_next_gray);
				this.bottomBtVideoNext.setEnabled(false);
			} else {
				if (position == 0) { // ��һ����ť�����Ե��
					this.bottomBtVideoPre
							.setBackgroundResource(R.drawable.btn_pre_gray);
					this.bottomBtVideoPre.setEnabled(false);
					this.bottomBtVideoNext
							.setBackgroundResource(R.drawable.bottom_bt_video_next_selector);
					this.bottomBtVideoNext.setEnabled(true);
				} else if (position == videoItems.size() - 1) { // ��һ����ť�����Ե��
					this.bottomBtVideoPre
							.setBackgroundResource(R.drawable.bottom_bt_video_pre_selector);
					this.bottomBtVideoPre.setEnabled(true);
					this.bottomBtVideoNext
							.setBackgroundResource(R.drawable.btn_next_gray);
					this.bottomBtVideoNext.setEnabled(false);
				} else {// �����Ե��
					this.bottomBtVideoPre
							.setBackgroundResource(R.drawable.bottom_bt_video_pre_selector);
					this.bottomBtVideoPre.setEnabled(true);
					this.bottomBtVideoNext
							.setBackgroundResource(R.drawable.bottom_bt_video_next_selector);
					this.bottomBtVideoNext.setEnabled(true);
				}
			}
		} else if (uri != null) {
			// ����������ť���ɵ��
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

		// ����������ʵ�������ı仯
		this.setVoiceSeekBar();

		// ���õ����仯
		this.setRegisterBattery();
		// ����Ƶ�б�ȡ����
		this.getDataFromVideoPage();
		// ����һ�����ŵ�ַ
		this.setData();

	}

	/**
	 * ��ʼ����������صĸ��ֿؼ� ͬʱ���øտ�ʼ����ʱ�������ֵ
	 */
	private void setVoiceSeekBar() {
		audioManager = (AudioManager) getSystemService(AUDIO_SERVICE);
		currentVoice = audioManager.getStreamVolume(AudioManager.STREAM_MUSIC);
		maxVoice = audioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
		// ���������seekBar����
		seekbarVoice.setMax(maxVoice);
		// ���õ�ǰ����
		seekbarVoice.setProgress(currentVoice);

	}

	/**
	 * ���ò��ŵ�ַ��������
	 */
	private void setData() {

		if (null != videoItems && videoItems.size() > 0) {
			Object obj = videoItems.get(position);
			if(obj instanceof VideoItem){
				VideoItem videoItem = (VideoItem) obj;
				this.videoName.setText(videoItem.getVideoName());// ������Ƶ������
				this.vitamio_videoview.setVideoPath(videoItem.getData());// ������Ƶ�Ĳ��ŵ�ַ
				//�ж��Ƿ���Դ������
				isNetUri = Utils.isFromNetVideo(videoItem.getData());
				
			}else if(obj instanceof VipVideoItem){
				VipVideoItem videoItem = (VipVideoItem) obj;
				this.videoName.setText(videoItem.getVideoname());// ������Ƶ������
				this.vitamio_videoview.setVideoPath(videoItem.getVideosrc());// ������Ƶ�Ĳ��ŵ�ַ
				//�ж��Ƿ���Դ������
				isNetUri = Utils.isFromNetVideo(videoItem.getVideosrc());
			}

		} else if (null != uri) {
			this.videoName.setText(uri.toString());// ������Ƶ������
			isNetUri = Utils.isFromNetVideo(uri.toString());
			this.vitamio_videoview.setVideoURI(uri);
		} else {
			Toast.makeText(VitamioVIdeoPlayer.this, "��û�д�������",
					Toast.LENGTH_SHORT).show();
		}
		// һ��������Ҫ�жϲ������ð�ť״̬
		this.setButtonState();
		// ��ȡ��Ļ�Ŀ�͸�
		getScreenWidthHeight();
	}

	/**
	 * ��ȡ��Ļ�Ŀ�͸�
	 */
	private void getScreenWidthHeight() {
		// ��ȡ��Ļ�Ŀ�͸�
		DisplayMetrics outMetrics = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(outMetrics);
		screenWidth = outMetrics.widthPixels;
		screenHeight = outMetrics.heightPixels;
	}

	/**
	 * ����Ƶ�б���ȡ���ݹ���������
	 */
	@SuppressWarnings("unchecked")
	private void getDataFromVideoPage() {

		uri = getIntent().getData(); //ͨ����Դ�ļ��к�ͼƬ�������
		videoItems = (ArrayList<Object>) getIntent().getSerializableExtra(
				"videolist");
		position = (int) getIntent().getIntExtra("position", 0);
	}

	/**
	 * ���õ����仯�Ĺ㲥
	 */
	private void setRegisterBattery() {
		// ע������㲥
		myBroadcastReceiver = new MyBroadcastReceiver();
		IntentFilter intentFilter = new IntentFilter();
		// �������仯ʱ������㲥
		intentFilter.addAction(Intent.ACTION_BATTERY_CHANGED);
		registerReceiver(myBroadcastReceiver, intentFilter);
	}

	/**
	 * ��ȡ�����仯�Ĺ㲥
	 * 
	 * @author zhangcan
	 * 
	 */
	class MyBroadcastReceiver extends BroadcastReceiver {

		@Override
		public void onReceive(Context context, Intent intent) {
			int level = intent.getIntExtra("level", 0); // ��ȡ����
			setBattery(level); // ���õ����仯
		}

	}

	/**
	 * ���ø��ּ����¼�
	 */
	private void setListener() {
		// ����׼���õļ���
		vitamio_videoview.setOnPreparedListener(new MyOnPreparedListener());

		// ���ò��ų���ļ���
		vitamio_videoview.setOnErrorListener(new MyOnErrorListener());

		// ���ò�����ɵļ���
		vitamio_videoview.setOnCompletionListener(new MyOnCompletionListener());

		// ������Ƶ���Ž��������϶�����
		seekbarVideo
				.setOnSeekBarChangeListener(new VideoOnSeekBarChangeListener());

		// ���������仯��һ������
		seekbarVoice
				.setOnSeekBarChangeListener(new VoiceOnSeekBarChangeListener());
		
		//������Ƶ�ļ�����������Ƶû�л��嵽���ŵĵط��͵���һ��ȦȦ����Ƶ����
		vitamio_videoview.setOnInfoListener(new MyOnInfoListener());
	}

	/**
	 * ���ݵ��������õ����仯��ͼ��
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
	 * ���������ı仯��ͬʱ�����Ƿ������ж�����Ϊ�������
	 * 
	 * @param progress
	 * @param isMute
	 *            true-->����
	 */
	private void updateVoice(int progress, boolean isMute) {
		if (isMute) {
			// ����
			seekbarVoice.setProgress(0);
			audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, 0, 0);
		} else {
			// �Ǿ���
			seekbarVoice.setProgress(progress);
			audioManager
					.setStreamVolume(AudioManager.STREAM_MUSIC, progress, 0);
			currentVoice = progress;
		}
	}

	/**
	 * ������Ƶ�ļ��������¼�������Ƶû�л��嵽���ŵĵط��͵���һ��ȦȦ����Ƶ����
	 * @author zhangcan
	 *
	 */
	class MyOnInfoListener implements MediaPlayer.OnInfoListener{

		@Override
		public boolean onInfo(MediaPlayer mp, int what, int extra) {
			switch (what) {
			case MediaPlayer.MEDIA_INFO_BUFFERING_START:
				//��Ƶ����
//				Toast.makeText(MySystemDefinedVideoPlayer.this, "��Ƶ����", Toast.LENGTH_SHORT).show();
				buffer_caton.setVisibility(View.VISIBLE);
				break;
			case MediaPlayer.MEDIA_INFO_BUFFERING_END:
				//��Ƶ�������ˣ����ص����ŵĵط�
//				Toast.makeText(MySystemDefinedVideoPlayer.this, "��Ƶ������", Toast.LENGTH_SHORT).show();
				buffer_caton.setVisibility(View.GONE);
				break;

			}
			return true;
		}
		
	}
	
	/**
	 * �����������ı�ļ����¼�
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
	 * ���Ž������ı�ļ����¼�
	 * 
	 * @author zhangcan
	 * 
	 */
	class VideoOnSeekBarChangeListener implements OnSeekBarChangeListener {

		/**
		 * ����ָ����ʱ�������仯���ص��������
		 * 
		 * @param fromUser
		 *            ���û������Ϊtrue
		 */
		@Override
		public void onProgressChanged(SeekBar seekBar, int progress,
				boolean fromUser) {
			if (fromUser) {
				vitamio_videoview.seekTo(progress);
			}
		}

		/**
		 * ����ָ������Ļʱ�ص��������
		 */
		@Override
		public void onStartTrackingTouch(SeekBar seekBar) {
			handler.removeMessages(Constant.HIDE_MEDIA_CONTROLLER);
		}

		/**
		 * ����ֽ�뿪��Ļʱ�ص��������
		 */
		@Override
		public void onStopTrackingTouch(SeekBar seekBar) {
			handler.sendEmptyMessageDelayed(Constant.HIDE_MEDIA_CONTROLLER,
					4000);
		}

	}

	/**
	 * �����涨ʱÿ��ˢ�½��ȣ��õ���ǰ�Ľ��ȣ��Ӷ����ò��Ž�����
	 */
	private Handler handler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case Constant.PROGRESS: // ������Ƶ���Ž������Ͳ���ʱ�����Ϣ
				// 1.�õ���ǰ�Ĳ��Ž���
				int currentPosition = (int) vitamio_videoview.getCurrentPosition();
				// 2.����seekBar�Ĳ��Ž��ȣ��͵�ǰ���ŵ�ʱ��
				seekbarVideo.setProgress(currentPosition);
				videoCurrentTime.setText(StringUtils
						.stringForTime(currentPosition));
				// ���õ�ǰϵͳʱ��
				systemTime.setText(new SimpleDateFormat("HH:mm:ss")
						.format(new Date()));
				
				//ÿ�뻺����ȵĸ���һ�ε�һ���㷨
				if(isNetUri){
					//ֻ���������Դ���л���Ч��
					int buffer = vitamio_videoview.getBufferPercentage();//0-100
					int totalBuffer =  buffer * seekbarVideo.getMax();
					//���õڶ���Ľ���
					seekbarVideo.setSecondaryProgress(totalBuffer/100);
				}else{
					seekbarVideo.setSecondaryProgress(0);
				}
				
				// 3.ÿ�����һ��
				handler.removeMessages(Constant.PROGRESS);
				handler.sendEmptyMessageDelayed(Constant.PROGRESS, 1000);

				break;
				
			case Constant.HIDE_MEDIA_CONTROLLER: // ������Ƶ�������
				mediaController.setVisibility(View.GONE);
				isShowMediaController = false;

				break;
			case Constant.SHOW_SPEED://��ʾ����
				//1.�õ������ٶ�
				String netSpeed = utils.getNetSpeed(VitamioVIdeoPlayer.this);
				
				//��ʾ������
				video_loading_netspeed.setText("������..."+ netSpeed);
				video_buffer_netspeed.setText("������..."+ netSpeed);
				
				//2.ÿ�������һ��
				handler.removeMessages(Constant.SHOW_SPEED);
				handler.sendEmptyMessageDelayed(Constant.SHOW_SPEED, 2000);
			}
		}
	};

	/**
	 * ʵ����Ƶ׼���õļ����¼�
	 * 
	 * @author zhangcan
	 * 
	 */
	class MyOnPreparedListener implements MediaPlayer.OnPreparedListener {

		// ���ײ����׼���õ�ʱ��
		@Override
		public void onPrepared(MediaPlayer mp) {
			vitamio_videoview.start(); // ��ʼ����
			// ���ز�����Ƶ�Ŀ������
			mediaController.setVisibility(View.GONE);
			isShowMediaController = false;
			// ��ȡ��Ƶ��ʵ�Ŀ�͸�
			videoWidth = mp.getVideoWidth();
			videoHeight = mp.getVideoHeight();
			// ��ȡ���ŵ���ʱ��
			int duration = (int) vitamio_videoview.getDuration();
			seekbarVideo.setMax(duration);
			// ������Ƶ��ʱ��
			videoDuration.setText(StringUtils.stringForTime(duration));
			// ���͸��µ���Ϣ
			handler.sendEmptyMessage(Constant.PROGRESS);
			//����Ĭ����Ļ
			setFullScreenAndDefault(Constant.DEFAULT_SCREEN);
			//�Ѽ���ҳ�����ʧ��
			video_begin_loading.setVisibility(View.GONE);
		}
	}

	/**
	 * ʵ�ֲ��ų���ļ���
	 * 
	 * @author zhangcan
	 * 
	 */
	class MyOnErrorListener implements MediaPlayer.OnErrorListener {

		@Override
		public boolean onError(MediaPlayer mp, int what, int extra) {
			//���ų������ʾ��Ϣ
			AlertDialog.Builder builder = new AlertDialog.Builder(VitamioVIdeoPlayer.this);
			builder.setTitle("��ʾ");
			builder.setMessage("�޷����Ÿ���Ƶ!");
			builder.setPositiveButton("ȷ��", new DialogInterface.OnClickListener() {
				
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
	 * ʵ�ֲ�����ɵļ���
	 * 
	 * @author zhangcan
	 * 
	 */
	class MyOnCompletionListener implements MediaPlayer.OnCompletionListener {

		@Override
		public void onCompletion(MediaPlayer mp) {
			Toast.makeText(VitamioVIdeoPlayer.this, "���ܲ��������������",
					Toast.LENGTH_SHORT).show();
			playNextVideo();
		}
	}

	/**
	 * �Զ��������ʶ������ʵ���û�����Ļ�Ľ����¼� ����ʵ����Ƶ���ź���ͣ ����ʵ����Ƶ����������ʾ������ ˫��ʵ��ȫ����Ĭ��
	 * 
	 * @author zhangcan
	 * 
	 */
	class MySimpleOnGestureListener extends SimpleOnGestureListener {

		@Override
		public void onLongPress(MotionEvent e) {
			super.onLongPress(e);
			if (vitamio_videoview.isPlaying()) {
				// ��Ƶ���ڲ���--������ͣ
				vitamio_videoview.pause();
				bottomBtVideoPauseandplay
						.setBackgroundResource(R.drawable.bottom_bt_video_start_selector);
			} else {
				// ��Ƶ��ͣ--���ò���
				vitamio_videoview.start();
				// ��ť����--��ͣ
				bottomBtVideoPauseandplay
						.setBackgroundResource(R.drawable.bottom_bt_video_pause_selector);
			}
		}

		@Override
		public boolean onDoubleTap(MotionEvent e) {
			// Toast.makeText(MySystemDefinedVideoPlayer.this, "��˫����",
			// Toast.LENGTH_SHORT).show();
			if (isFullScreen) {
				// ����Ĭ��
				setFullScreenAndDefault(Constant.DEFAULT_SCREEN);
			} else {
				// ����ȫ��
				setFullScreenAndDefault(Constant.FULL_SCREEN);
			}
			return super.onDoubleTap(e);
		}

		@Override
		public boolean onSingleTapConfirmed(MotionEvent e) {
			// Toast.makeText(MySystemDefinedVideoPlayer.this, "��������",
			// Toast.LENGTH_SHORT).show();
			if (!isShowMediaController) {
				mediaController.setVisibility(View.VISIBLE);
				isShowMediaController = true;
				// ����ʾ����Ϣ�������أ��ӳ�4s
				handler.sendEmptyMessageDelayed(Constant.HIDE_MEDIA_CONTROLLER,
						4000);
			} else {
				mediaController.setVisibility(View.GONE);
				isShowMediaController = false;
				// �Ƴ����ؿ���������Ϣ
				handler.removeMessages(Constant.HIDE_MEDIA_CONTROLLER);

			}
			return super.onSingleTapConfirmed(e);
		}

	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		// ���¼����ݸ�����ʶ����
		this.gestureDetector.onTouchEvent(event);

		this.handMoveChangeVoice(event);
		return super.onTouchEvent(event);
	}
	
	/**
	 * ���������ʵ�������ı仯�����ֻ��ĵ��������İ���
	 * @author zhangcan
	 */
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if(keyCode == KeyEvent.KEYCODE_VOLUME_DOWN){ //������С����
			currentVoice --;
			updateVoice(currentVoice, false);
			handler.removeMessages(Constant.HIDE_MEDIA_CONTROLLER);
			handler.sendEmptyMessageDelayed(Constant.HIDE_MEDIA_CONTROLLER, 4000);
			return true;
		}else if(keyCode == KeyEvent.KEYCODE_VOLUME_UP){//�������󰴼�
			currentVoice ++;
			updateVoice(currentVoice, false);
			handler.removeMessages(Constant.HIDE_MEDIA_CONTROLLER);
			handler.sendEmptyMessageDelayed(Constant.HIDE_MEDIA_CONTROLLER, 4000);
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}

	/**
	 * ��ָ��ʼ������λ��
	 */
	private float startY;
	/**
	 * ��Ļ�ĸ�
	 */
	private float touchScreenHeight;
	/**
	 * ��һ����ʱ������
	 */
	private int moveVoice;
	
	/**
	 * ��
	 */
	private Vibrator vibrator; 
	
	/**
	 * ��ָ���»��������Ļʱ�ı����ȴ�С���㷨������Ļ�ﵽ���������20%����ʱ���豸����
	 * ��ָ���»����ұ���Ļʱ�ı�������С���㷨
	 * 
	 * @param event
	 */
	private void handMoveChangeVoice(MotionEvent event) {
		// ��ָ���»���ʱ�ı�����
		switch (event.getAction()) {
		case MotionEvent.ACTION_DOWN: // ��ָ����
			// 1.���¾���Ҫ��¼ֵ
			startY = event.getY();
			moveVoice = audioManager.getStreamVolume(AudioManager.STREAM_MUSIC);
			touchScreenHeight = Math.min(screenHeight, screenWidth);
			// �Ƴ������ؿ���������Ϣ
			handler.removeMessages(Constant.HIDE_MEDIA_CONTROLLER);
			break;
		case MotionEvent.ACTION_MOVE: // ��ָ����
			// 2.�ƶ�ʱ��¼���ֵ
			float endY = event.getY();
			float endX = event.getX();
			float distanceY = startY - endY;
			
			if(endX > screenWidth/2){
				//�ұ���Ļ��������
				// ������Ļ�ľ���:�ܾ��� = �ı�����:�������ֵ
				// �ı����� = ��������Ļ�ľ���:�ܾ��뼴��Ļ�ߣ�*�������ֵ
				float changeVoice = (distanceY / touchScreenHeight) * maxVoice;
				// �������� = ԭ���� +�ı�����
				int finalVoice = (int) (moveVoice + changeVoice);
				finalVoice = (finalVoice <= maxVoice) ? finalVoice : maxVoice;
				finalVoice = (finalVoice >= 0) ? finalVoice : 0;
				if (changeVoice != 0) {
					isMute = false;
					updateVoice(finalVoice, isMute);
				}
			}else{
				//�����Ļ����������
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
		case MotionEvent.ACTION_UP: // ��ָ̧��
			// ��ָ�뿪��Ļʱ���·���Ӱ�ؿ���������Ϣ
			handler.sendEmptyMessageDelayed(Constant.HIDE_MEDIA_CONTROLLER,
					4000);
			break;
		}
	}
	
	/**
	 * ������Ļ���� lp = 0 ȫ�� ��lp= -1,����ϵͳ���ã� lp = 1; ����  
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
			// ���ٶ�̬ע��Ĺ㲥
			unregisterReceiver(myBroadcastReceiver);
			myBroadcastReceiver = null;
		}
		super.onDestroy();
	}

	/**
	 * ������Ƶȫ����Ĭ�ϲ��� ����Ĭ�ϲ���ʱ��һ���㷨
	 * 
	 * @param defaultScreen
	 */
	private void setFullScreenAndDefault(int defaultScreen) {
		switch (defaultScreen) {
		case Constant.FULL_SCREEN: // ȫ��
			// ������Ƶ�����С
			vitamio_videoview.setVideoSize(screenWidth, screenHeight);
			// ����ȫ����ť״̬--ΪĬ��
			bottomBtVideoSwitchFullscreen
					.setBackgroundResource(R.drawable.bottom_bt_video_defaultscreen_selector);
			isFullScreen = true;
			break;

		case Constant.DEFAULT_SCREEN: // Ĭ��
			// ������Ƶ�����С
			int mVideoWidth = videoWidth; // ��Ƶ��ʵ�Ŀ�
			int mVideoHeight = videoHeight;// ��Ƶ��ʵ�ĸ�

			int width = screenWidth; // ��Ļ�Ŀ�
			int height = screenHeight;// ��Ļ�ĸ�

			if (mVideoWidth * height < width * mVideoHeight) {
				width = height * mVideoWidth / mVideoHeight;
			} else if (mVideoWidth * height > width * mVideoHeight) {
				height = width * mVideoHeight / mVideoWidth;
			}

			vitamio_videoview.setVideoSize(width, height);
			// ���ð�ť״̬--Ϊȫ��
			bottomBtVideoSwitchFullscreen
					.setBackgroundResource(R.drawable.bottom_bt_video_fullscreen_selector);
			isFullScreen = false;
			break;

		}
	}
}
