package com.njau.mobileplayer.init;

import org.xutils.BuildConfig;
import org.xutils.x;

import com.iflytek.cloud.SpeechConstant;
import com.iflytek.cloud.SpeechUtility;

import android.app.Application;

/**
 * ���ڼ���xUtilsʱ��ʼ��Application��
 * ��ʼ���ƴ�Ѷ��
 * @author zhangcan
 *
 */
public class MyApplication extends Application {

	@Override
	public void onCreate() {
		super.onCreate();
		//��ʼ��xUtils
		x.Ext.init(this);
	    x.Ext.setDebug(BuildConfig.DEBUG); // �Ƿ����debug��־, ����debug��Ӱ������.
	    
	    //��ʼ���ƴ�Ѷ��
	    // ����12345678���滻��������� APPID�������ַ�� http://www.xfyun.cn
	    // �����ڡ� =���� appid ֮�����������ַ�����ת���
	    SpeechUtility.createUtility(this, SpeechConstant.APPID +"=58e3669e");
	}
}
