package com.njau.mobileplayer.utils;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Formatter;
import java.util.Locale;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;

/**
 * ����ת�������ڰ�ʱ��ת���ɹ̶���ʽ
 * @author zhangcan
 *
 */
public final class StringUtils {

	private static StringBuilder mFormatBuilder = new StringBuilder();;
	private static Formatter mFormatter;

	/**
	 * �Ѻ���ת���ɣ�1:20:30������ʽ
	 * @param timeMs
	 * @return
	 */
	public static String stringForTime(int timeMs) {
		
		mFormatter = new Formatter(mFormatBuilder, Locale.getDefault());
		
		int totalSeconds = timeMs / 1000;
		int seconds = totalSeconds % 60;

		int minutes = (totalSeconds / 60) % 60;

		int hours = totalSeconds / 3600;

		mFormatBuilder.setLength(0);
		if (hours > 0) {
			return mFormatter.format("%d:%02d:%02d", hours, minutes, seconds)
					.toString();
		} else {
			return mFormatter.format("%02d:%02d", minutes, seconds).toString();
		}
	}
	
	/**
	 * ��ȡϵͳʱ��
	 * @return
	 */
	@SuppressLint("SimpleDateFormat") public static String getSystemTime(){
		SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");
		return dateFormat.format(new Date());
	}
	
	/**
	 * ʹ�� SharedPreferences ����������
	 * @param context
	 * @param key
	 * @param values
	 */
	public static void putString(Context context,String key,String values){
		SharedPreferences sharedPreferences = context.getSharedPreferences("njau_edu", Context.MODE_PRIVATE);
		SharedPreferences.Editor editor = sharedPreferences.edit();
		editor.putString(key, values);
		editor.commit();
	}

	/**
	 * ��ȡ����Ļ�������
	 * @param context
	 * @param key
	 * @return
	 */
	public static String getString(Context context,String key){
		SharedPreferences sharedPreferences = context.getSharedPreferences("njau_edu", Context.MODE_PRIVATE);
		return sharedPreferences.getString(key, "");
	}
	
	
}
