package com.njau.mobileplayer.activity;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.njau.mobileplayer.R;
import com.njau.mobileplayer.entity.PayItem;
import com.njau.mobileplayer.entity.PayItem.Video;
import com.njau.mobileplayer.utils.Constant;
import com.njau.mobileplayer.utils.Utils;

/**
 * 展示用户信息的界面
 * 
 * @author zhangcan
 * 
 */
public class ShowUserInfoActivity extends Activity implements OnClickListener {

	private TextView info_back_btn;
	private TextView info_user_name;
	private TextView user_balance_btn;
	private TextView user_balance;
	private TextView user_pay_recored_btn;
	private TextView user_pay_recored;
	
	private TextView user_vip_btn;

	private Button exit_user_btn;
	
//	private ListView pay_recored_listview;
//	private ShowPayRecordAdapter adapter;

	/**
	 * 查看用户余额的链接
	 */
	private String showBalanceUrl;
	
	/**
	 * 查看购买记录的url
	 */
	private String showPayItemRecordUrl;

	/**
	 * 初始化以及设置监听事件
	 */
	private void initViews() {
		info_back_btn = (TextView) findViewById(R.id.info_back_btn);
		info_user_name = (TextView) findViewById(R.id.info_user_name);
		user_balance_btn = (TextView) findViewById(R.id.user_balance_btn);
		user_balance = (TextView) findViewById(R.id.user_balance);
		user_pay_recored_btn = (TextView) findViewById(R.id.user_pay_recored_btn);
		user_pay_recored = (TextView) findViewById(R.id.user_pay_recored);
		exit_user_btn = (Button) findViewById(R.id.exit_user_btn);
//		pay_recored_listview = (ListView) findViewById(R.id.pay_recored_listview);
		user_vip_btn = (TextView) findViewById(R.id.user_vip_btn);
		user_balance.setVisibility(View.GONE);
		user_pay_recored.setVisibility(View.GONE);
//		pay_recored_listview.setVisibility(View.GONE);

		info_back_btn.setOnClickListener(this);
		user_balance_btn.setOnClickListener(this);
		user_pay_recored_btn.setOnClickListener(this);
		exit_user_btn.setOnClickListener(this);
		user_vip_btn.setOnClickListener(this);
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_show_user_info);
		initViews();

		info_user_name.setText(Utils.currentUser.getUsername());

	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.info_back_btn:
			// 个人信息界面顶部的返回按钮
			this.finish();
			break;
			
		case R.id.user_balance_btn:
			// 个人信息界面显示个人余额按钮
			// Toast.makeText(this, "显示余额按钮", Toast.LENGTH_SHORT).show();
			showBalanceUrl = Constant.URL_IP
					+ "videoServlet?method=showBalance&userid="
					+ Utils.currentUser.getUid();
			requestShowBalance();
			break;
			
		case R.id.user_pay_recored_btn:
			// 个人信息界面显示购买记录按钮
//			Toast.makeText(this, "显示购买记录", Toast.LENGTH_SHORT).show();
			showPayItemRecordUrl = Constant.URL_IP
					+ "videoServlet?method=showPayItem&userid="
					+ Utils.currentUser.getUid();
			requestShowPayItem();
			break;
			
		case R.id.exit_user_btn:
			// 个人信息界面底部退出个人账户按钮
			// Toast.makeText(this, "退出个人账户", Toast.LENGTH_SHORT).show();
			this.finish();
			Utils.currentUser = null;
			break;
			
		case R.id.user_vip_btn:
			//进入会员中心按钮
			Intent intent = new Intent(this,ChongZhiActivity.class);
			startActivity(intent);
			break;

		}
	}

	/**
	 * 查看用户的购买记录
	 */
	private void requestShowPayItem() {
		new Thread(new Runnable() {
			
			@Override
			public void run() {
				HttpURLConnection httpURLConnection = null;
				BufferedReader bufferedReader = null;
				try {
					URL urll = new URL(showPayItemRecordUrl); // 声明一个URL
					httpURLConnection = (HttpURLConnection) urll.openConnection(); // 打开该URL连接
					httpURLConnection.setRequestMethod("GET");// 设置请求方法，GET，我们这里用GET
					httpURLConnection.setConnectTimeout(5000); // 设置连接建立的超时时间
					httpURLConnection.setReadTimeout(5000); // 设置网络报文收发超时时间
					bufferedReader = new BufferedReader(new InputStreamReader(httpURLConnection.getInputStream()));
					String line = null;
					StringBuffer stringBuffer = new StringBuffer();
					while ((line = bufferedReader.readLine())!= null) {
						stringBuffer.append(line);
					}
					//转编码
					String content = URLDecoder.decode(stringBuffer.toString(), "UTF-8");
					
					Message message = new Message();
					message.what = 2;
					message.obj = content;
					handler.sendMessage(message);
				} catch (IOException e) {
					e.printStackTrace();
				}finally{
					if(bufferedReader!=null){
						try {
							bufferedReader.close();
						} catch (IOException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
					if(httpURLConnection!=null){
						httpURLConnection.disconnect();
					}
				}
			}
		}).start();
	}

	/**
	 * 查看用户余额，需要开启多线程与服务器进行通信
	 */
	private void requestShowBalance() {
		new Thread(new Runnable() {

			@Override
			public void run() {
				HttpURLConnection httpURLConnection = null;
				BufferedReader bufferedReader = null;
				try {
					URL urll = new URL(showBalanceUrl); // 声明一个URL
					httpURLConnection = (HttpURLConnection) urll.openConnection(); // 打开该URL连接
					httpURLConnection.setRequestMethod("GET");// 设置请求方法，GET，我们这里用GET
					httpURLConnection.setConnectTimeout(5000); // 设置连接建立的超时时间
					httpURLConnection.setReadTimeout(5000); // 设置网络报文收发超时时间
					InputStream inputStream = httpURLConnection
							.getInputStream();
					bufferedReader = new BufferedReader(
							new InputStreamReader(inputStream));
					StringBuffer stringBuffer = new StringBuffer();
					String line = null;
					while ((line = bufferedReader.readLine()) != null) {
						stringBuffer.append(line);
					}

					Message message = new Message();
					message.what = 1;
					message.obj = stringBuffer.toString();
					handler.sendMessage(message);
				} catch (IOException e) {
					e.printStackTrace();
				}finally{
					if(bufferedReader!=null){
						try {
							bufferedReader.close();
						} catch (IOException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
					if(httpURLConnection!=null){
						httpURLConnection.disconnect();
					}
				}
			}
		}).start();
	}

	/**
	 * 消息处理
	 */
	private Handler handler = new Handler() {
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case 1:// 显示余额
				String content = msg.obj.toString();
				if (null != content) {
					user_balance.setText("￥" + content);
					user_balance.setVisibility(View.VISIBLE);
				}else{
					Toast.makeText(ShowUserInfoActivity.this, "链接服务器失败", Toast.LENGTH_SHORT).show();
				}
				break;

			case 2://查看用户购买记录
				String responseStr = msg.obj.toString();
				if(responseStr != null &&"[]".equals(responseStr)){
//					Log.e("responseStr", responseStr);
					user_pay_recored.setText("查无记录");
//					pay_recored_listview.setVisibility(View.GONE);
					user_pay_recored.setVisibility(View.VISIBLE);
				}else if(responseStr != null && !"[]".equals(responseStr)){
//					Log.e("responseStr", responseStr);
					List<PayItem> payItems = parsejson(responseStr);
					if(payItems!= null && payItems.size()>0){
						StringBuffer buffer = new StringBuffer();
						for(PayItem pay : payItems){
							buffer.append("电影："+pay.getVideo().getVideoname()+" 金额：￥"+pay.getVideo().getNeedmoney()+"\n 时间："+pay.getPaytime());
							buffer.append("\n");
						}
						user_pay_recored.setText(buffer.toString());
						user_pay_recored.setVisibility(View.VISIBLE);
					}
//					adapter = new ShowPayRecordAdapter(ShowUserInfoActivity.this, payItems);
//					Log.e("payItems", payItems+"");
//					pay_recored_listview.setAdapter(adapter);
//					pay_recored_listview.setVisibility(View.VISIBLE);
					
				}else{
					Toast.makeText(ShowUserInfoActivity.this, "链接服务器失败", Toast.LENGTH_SHORT).show();
				}
				break;
			}
		}

	};
	
//[{"payid":1,"userid":1,"paytype":true,"paytime":"Apr 17, 2017 10:51:10 PM","video":{"id":"222","videoname":"青云志第二季02","videosrc":"http://192.168.23.1:8080/02.mp4","videodesc":"小凡能否找到灵石","descimg":"http://192.168.23.1:8080/2245631.png","needmoney":2.0}},
//{"payid":2,"userid":1,"paytype":true,"paytime":"Apr 17, 2017 10:57:22 PM","video":{"id":"333","videoname":"青云志第二季03","videosrc":"http://192.168.23.1:8080/03.mp4","videodesc":"剧情如何发展","descimg":"http://192.168.23.1:8080/2245631.png","needmoney":3.0}}]

	/**
	 * 解析json字符串得到一个PayItem集合
	 * @param responseStr
	 * @return
	 */
	private List<PayItem> parsejson(String responseStr) {
		List<PayItem> items = new ArrayList<PayItem>();
		try {
			JSONArray array = new JSONArray(responseStr);
			for(int i=0;i<array.length();i++){
				PayItem item = new PayItem();
				JSONObject jsonObject = array.optJSONObject(i);
				int payid = jsonObject.optInt("payid");
				item.setPayid(payid);
				int userid = jsonObject.optInt("userid");
				item.setUserid(userid);
				boolean paytype = jsonObject.optBoolean("paytype");
				item.setPaytype(paytype);
				String paytime = jsonObject.optString("paytime");
				item.setPaytime(paytime);
				JSONObject videoJSONObject = jsonObject.optJSONObject("video");
				PayItem.Video video = new Video();
				String id = videoJSONObject.optString("id");
				video.setId(id);
				String videoname = videoJSONObject.optString("videoname");
				video.setVideoname(videoname);
				String videosrc = videoJSONObject.optString("videosrc");
				video.setVideosrc(videosrc);
				String videodesc = videoJSONObject.optString("videodesc");
				video.setVideodesc(videodesc);
				String descimg = videoJSONObject.optString("descimg");
				video.setDescimg(descimg);
				float needmoney = (float) videoJSONObject.optDouble("needmoney");
				video.setNeedmoney(needmoney);
				item.setVideo(video);
				
				items.add(item);
				
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
		
		return items;
	}
}
