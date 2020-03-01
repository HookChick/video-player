package com.njau.mobileplayer.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.ViewGroup;
import android.widget.VideoView;

/**
 * �Զ����VideoViewʵ����Ƶ���Ž����ܹ�ʵ����Ƶȫ����Ĭ�ϵ��л�
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
	 * �������
	 * @param context
	 */
	public MyVideoView(Context context) {
		this(context,null);
	}
	
	/**
	 * �����ķ���
	 */
	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);
		setMeasuredDimension(widthMeasureSpec,heightMeasureSpec);
	}
	
	/**
	 * ������Ƶ�Ŀ�͸�
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
