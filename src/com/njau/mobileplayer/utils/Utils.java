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
 * ������
 * 
 * @author zhangcan
 * 
 */
@SuppressLint("DefaultLocale")
public final class Utils {

	/**
	 * �ж��Ƿ�������
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
	 * ����е�¼�û��ĵ�ǰuser����
	 */
	public static User currentUser = null;
	
	/**
	 * �ж���Ƶ�Ƿ�����Դ���������Ƶ��Դ
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
	 * �õ������ٶ�ֵ�� ÿ��һ��ʱ��ξ�ȥ��ȡ���ʱ��λ�ȡ�����������ݵĴ�С��Ȼ��ͨ������������ֵ
	 * 
	 * @param context
	 * @return
	 */
	public String getNetSpeed(Context context) {
		String netSpeed = "0 Kb/s";
		long nowTotalRxBytes = TrafficStats.getUidRxBytes(context
				.getApplicationInfo().uid) == TrafficStats.UNSUPPORTED ? 0
				: (TrafficStats.getTotalRxBytes() / 1024);// תΪKB
		long nowTimeStamp = System.currentTimeMillis();
		long speed = ((nowTotalRxBytes - lastTotalRxBytes) * 1000 / (nowTimeStamp - lastTimeStamp));// ����ת��

		lastTimeStamp = nowTimeStamp;
		lastTotalRxBytes = nowTotalRxBytes;

		netSpeed = String.valueOf(speed) + " kb/s";
		return netSpeed;
	}

	/**
	 * ��������ListView��Adapter�󣬸���ListView������Ŀ���¼���ListView�ĸ߶ȣ�
	 * Ȼ��Ѹ߶�����ΪLayoutParams���ø�ListView���������ĸ߶Ⱦ���ȷ��
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
	 * ��ȡһ�� Android ͬ���������ӵ�HttpURLConnection����
	 * @param url  ��������url����·��
	 * @return
	 */
	public static HttpURLConnection getHttpURLConnection12(String url) {
		HttpURLConnection connection = null;

		try {
			URL urll = new URL(url); // ����һ��URL
			connection = (HttpURLConnection) urll.openConnection(); // �򿪸�URL����
			connection.setRequestMethod("GET");// �������󷽷���GET������������GET
			connection.setConnectTimeout(5000); // �������ӽ����ĳ�ʱʱ��
			connection.setReadTimeout(5000); // �������籨���շ���ʱʱ��
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return connection;
	}

}
