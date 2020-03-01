package com.njau.mobileplayer.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.ViewGroup;
import android.widget.VideoView;

/**
 * 自定义的VideoView实现视频播放进而能够实现视频全屏和默认的切换
 * @author zhangcan
 *
 */
public class MyVideoView extends VideoView {

	public MyVideoView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		// TODO Auto-generated constructor stub
	}

	public MyVideoView(Context context, AttributeSet attrs) {
		this(context, attrs,0);
	}

	/**
	 * 代码调用
	 * @param context
	 */
	public MyVideoView(Context context) {
		this(context,null);
	}
	
	/**
	 * 测量的方法
	 */
	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);
		setMeasuredDimension(widthMeasureSpec,heightMeasureSpec);
	}
	
	/**
	 * 设置视频的宽和高
	 * @param videoWidth
	 * @param videoHeight
	 * @author zhangcan
	 */
	public void setVideoSize(int videoWidth , int videoHeight){
		ViewGroup.LayoutParams params = this.getLayoutParams();
		params.width = videoWidth;
		params.height = videoHeight;
		setLayoutParams(params);
	}

}
