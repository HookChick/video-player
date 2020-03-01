package com.njau.mobileplayer.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.MotionEvent;
import android.view.Window;

import com.njau.mobileplayer.R;

/**
 * ��ʼ����ҳ��
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
        //��֤������̻߳������߳���ִ��
        handler.postDelayed(new Runnable() {
			
			@Override
			public void run() {
				//2����ִ��
				startMainActivity();
			}

		}, 2000);
    }
    
    /**
     * ����ף�ҳ�棬�رյ�ǰ�
     */
    private void startMainActivity() {
    	Intent intent = new Intent(this,MainActivity.class);
    	this.startActivity(intent);
    	this.finish();
	}
    
    /**
     * �û������Ļ��������������
     */
    @Override
    public boolean onTouchEvent(MotionEvent event) {
    	startMainActivity();
    	return super.onTouchEvent(event);
    }
    
    @Override
    protected void onDestroy() {
    	//�����е���Ϣ������ص��Ƴ�����֤������ִ��
    	handler.removeCallbacksAndMessages(null);
    	super.onDestroy();
    }
}
