package com.njau.mobileplayer.init;

import org.xutils.BuildConfig;
import org.xutils.x;

import com.iflytek.cloud.SpeechConstant;
import com.iflytek.cloud.SpeechUtility;

import android.app.Application;

/**
 * 用于集成xUtils时初始化Application类
 * 初始化科大讯飞
 * @author zhangcan
 *
 */
public class MyApplication extends Application {

	@Override
	public void onCreate() {
		super.onCreate();
		//初始化xUtils
		x.Ext.init(this);
	    x.Ext.setDebug(BuildConfig.DEBUG); // 是否输出debug日志, 开启debug会影响性能.
	    
	    //初始化科大讯飞
	    // 将“12345678”替换成您申请的 APPID，申请地址： http://www.xfyun.cn
	    // 请勿在“ =”与 appid 之间添加任务空字符或者转义符
	    SpeechUtility.createUtility(this, SpeechConstant.APPID +"=58e3669e");
	}
}
