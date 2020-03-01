package com.njau.mobileplayer.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.MotionEvent;
import android.view.Window;

import com.njau.mobileplayer.R;

/**
 * 初始加载页面
 * @author zhangcan
 *
 */
public class LaucherActivity extends Activity {

	private Handler handler = new Handler();
	
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_laucher);
        //保证这个子线程会在主线程中执行
        handler.postDelayed(new Runnable() {
			
			@Override
			public void run() {
				//2秒钟执行
				startMainActivity();
			}

		}, 2000);
    }
    
    /**
     * 启动祝活动页面，关闭当前活动
     */
    private void startMainActivity() {
    	Intent intent = new Intent(this,MainActivity.class);
    	this.startActivity(intent);
    	this.finish();
	}
    
    /**
     * 用户点击屏幕立即启动主界面
     */
    @Override
    public boolean onTouchEvent(MotionEvent event) {
    	startMainActivity();
    	return super.onTouchEvent(event);
    }
    
    @Override
    protected void onDestroy() {
    	//把所有的消息和任务回调移除，保证不会再执行
    	handler.removeCallbacksAndMessages(null);
    	super.onDestroy();
    }
}
