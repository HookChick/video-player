package com.njau.mobileplayer.utils;

import java.net.HttpURLConnection;
import java.net.URL;

import com.njau.mobileplayer.entity.User;

import android.annotation.SuppressLint;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.TrafficStats;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListAdapter;
import android.widget.ListView;

/**
 * 工具类
 * 
 * @author zhangcan
 * 
 */
@SuppressLint("DefaultLocale")
public final class Utils {

	/**
	 * 判断是否有网络
	 * @param context
	 * @return
	 */
	public static boolean hasNetwork(Context context) {
		ConnectivityManager connectivityManager = (ConnectivityManager) context
		        .getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo info = connectivityManager.getActiveNetworkInfo(); 
		if (info != null && info.isAvailable()) { 
			 return true ; 
		} 
		return false;
	}

	/**
	 * 软件中登录用户的当前user对象
	 */
	public static User currentUser = null;
	
	/**
	 * 判断视频是否是来源于网络的视频资源
	 * 
	 * @param uri
	 * @return
	 */
	public static boolean isFromNetVideo(String uri) {
		boolean isNet = false;
		if (uri.toLowerCase().startsWith("http")
				|| uri.toLowerCase().startsWith("rtsp")) {
			isNet = true;
		}
		return isNet;
	}

	private long lastTotalRxBytes = 0;
	private long lastTimeStamp = 0;

	/**
	 * 得到网络速度值， 每隔一个时间段就去获取这个时间段获取到的网络数据的大小，然后通过计算获得网速值
	 * 
	 * @param context
	 * @return
	 */
	public String getNetSpeed(Context context) {
		String netSpeed = "0 Kb/s";
		long nowTotalRxBytes = TrafficStats.getUidRxBytes(context
				.getApplicationInfo().uid) == TrafficStats.UNSUPPORTED ? 0
				: (TrafficStats.getTotalRxBytes() / 1024);// 转为KB
		long nowTimeStamp = System.currentTimeMillis();
		long speed = ((nowTotalRxBytes - lastTotalRxBytes) * 1000 / (nowTimeStamp - lastTimeStamp));// 毫秒转换

		lastTimeStamp = nowTimeStamp;
		lastTotalRxBytes = nowTotalRxBytes;

		netSpeed = String.valueOf(speed) + " kb/s";
		return netSpeed;
	}

	/**
	 * 在设置完ListView的Adapter后，根据ListView的子项目重新计算ListView的高度，
	 * 然后把高度再作为LayoutParams设置给ListView，这样它的高度就正确了
	 * 
	 * @param listView
	 */
	public static void setListViewHeightBasedOnChildren(ListView listView) {
		ListAdapter listAdapter = listView.getAdapter();
		if (listAdapter == null) {
			return;
		}
		int totalHeight = 0;
		for (int i = 0; i < listAdapter.getCount(); i++) {
			View listItem = listAdapter.getView(i, null, listView);
			listItem.measure(0, 0);
			totalHeight += listItem.getMeasuredHeight();
		}

		ViewGroup.LayoutParams params = listView.getLayoutParams();
		params.height = totalHeight
				+ (listView.getDividerHeight() * (listAdapter.getCount() - 1));
		listView.setLayoutParams(params);
	}

	/**
	 * 获取一个 Android 同服务器连接的HttpURLConnection对象
	 * @param url  传过来的url请求路径
	 * @return
	 */
	public static HttpURLConnection getHttpURLConnection12(String url) {
		HttpURLConnection connection = null;

		try {
			URL urll = new URL(url); // 声明一个URL
			connection = (HttpURLConnection) urll.openConnection(); // 打开该URL连接
			connection.setRequestMethod("GET");// 设置请求方法，GET，我们这里用GET
			connection.setConnectTimeout(5000); // 设置连接建立的超时时间
			connection.setReadTimeout(5000); // 设置网络报文收发超时时间
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return connection;
	}

}
