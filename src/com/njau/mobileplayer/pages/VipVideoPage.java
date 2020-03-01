package com.njau.mobileplayer.pages;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.x;
import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.view.annotation.ViewInject;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.njau.mobileplayer.R;
import com.njau.mobileplayer.activity.LoginActivity;
import com.njau.mobileplayer.activity.MySystemDefinedVideoPlayer;
import com.njau.mobileplayer.activity.QRImageActivity;
import com.njau.mobileplayer.adapter.VipVideoPageAdapter;
import com.njau.mobileplayer.base.BasePage;
import com.njau.mobileplayer.entity.VideoItem;
import com.njau.mobileplayer.entity.VipVideoItem;
import com.njau.mobileplayer.utils.Constant;
import com.njau.mobileplayer.utils.Utils;

/**
 * VIP专区视频界面
 * 
 * @author zhangcan
 * 
 */
public class VipVideoPage extends BasePage {

	private static final String VIP_SERVER_URL = "http://192.168.23.1:8080/playerservice/videoServlet?method=getAllVideos";

	@ViewInject(R.id.vipvideo_listview)
	private ListView vipvideo_listview;

	@ViewInject(R.id.viptv_nonet)
	private TextView viptv_nonet;

	@ViewInject(R.id.vippb_loading)
	private ProgressBar vippb_loading;

	/**
	 * 存放一个一个视频文件基本信息组成的对象的集合
	 */
	private List<VipVideoItem> vipVideoItems;

	private VipVideoPageAdapter vipVideoPageAdapter;

	/**
	 * 判断是否付款，true表示付款了
	 */
	private boolean isPayed;
	
	/**
	 * 检查是否付款的Url
	 */
	private String checkPayUrl;
	
	/**
	 * 用户点击的视频项
	 */
	private VipVideoItem item;
	
	public VipVideoPage(Context context) {
		super(context);
	}

	@Override
	public View initView() {
		View view = View.inflate(context, R.layout.vipvideo_page, null);
		// 第一个参数当前对象，第二个是实例化的布局
		// 有这个就把这个view的布局通过这个框架传递给了这个类，就可以实例化了
		x.view().inject(this, view);
		// 设置点击事件
		vipvideo_listview.setOnItemClickListener(new MyOnItemClickListener());

		return view;
	}

	/**
	 * 处理listView的点击事件
	 * 
	 * @author zhangcan
	 * 
	 */
	class MyOnItemClickListener implements OnItemClickListener {

		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position,
				long id) {
			if(Utils.currentUser != null){
				item = vipVideoItems.get(position);
				//登录了，就去服务器检查用户对该视频有没有付款
				checkPayUrl = Constant.URL_IP + "videoServlet?method=checkPayed&userid="
						+ Utils.currentUser.getUid() + "&videoid="
						+ item.getId();
				
				requestCheckPayedorNot();
//				Log.e("isPayed", isPayed+"");
//				if(!isPayed){
//					
//					
//				}else{
//					
//				}
				
			}else{
				//跳转登录界面
				Intent intent = new Intent(context,LoginActivity.class);
				context.startActivity(intent);
			}
		}

	}

	/**
	 * 和服务端通讯，检查是否付款
	 * @param url 传递的地址
	 */
	private void requestCheckPayedorNot() {
		//网络通讯，开启新的线程
		new Thread(new Runnable() {
			
			@Override
			public void run() {
				HttpURLConnection httpURLConnection = null;
				BufferedReader bufferedReader = null;
				try {
					URL urll = new URL(checkPayUrl); // 声明一个URL
					httpURLConnection = (HttpURLConnection) urll.openConnection(); // 打开该URL连接
					httpURLConnection.setRequestMethod("GET");// 设置请求方法，GET，我们这里用GET
					httpURLConnection.setConnectTimeout(5000); // 设置连接建立的超时时间
					httpURLConnection.setReadTimeout(5000); // 设置网络报文收发超时时间
					InputStream inputStream = httpURLConnection.getInputStream();
					bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
					StringBuffer buffer = new StringBuffer();
					String line = null;
					while((line = bufferedReader.readLine())!=null){
						buffer.append(line);
					}
					//发送消息
					Message message = new Message();
					message.what = 1;
					message.obj = buffer.toString();
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
	 * 处理消息
	 */
	private Handler handler = new Handler(){
		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case 1:
				String responsestr = msg.obj.toString();
				Log.e("responsestr", responsestr);
				if(responsestr!= null&& "true".equals(responsestr)){
					//付款了，可以播放该视频
					//付款了，直接播放该视频
					// 需要传递视频
					Log.e("responsestr", responsestr);
					Intent intent = new Intent(context,
							MySystemDefinedVideoPlayer.class);
					Bundle bundle = new Bundle();
					List<VipVideoItem> vipItems = new ArrayList<VipVideoItem>();
					vipItems.add(item);
					bundle.putSerializable("videolist",(ArrayList<VipVideoItem>)vipItems);// 传递视频的对象
					intent.putExtras(bundle);
					//			intent.putExtra("position", position);
					context.startActivity(intent);
				}else{
					//没有付款
					//没有付款,弹出二维码
					Intent intent = new Intent(context,
							QRImageActivity.class);
					Bundle bundle = new Bundle();
					bundle.putSerializable("vipVideoItem", item);//传递一个视频
					intent.putExtras(bundle);
					context.startActivity(intent);
				}
				break;

			}
		};
	};
	
	
	@Override
	public void initData() {
		super.initData();
		getDataFromServer();
	}

	/**
	 * 请求服务器端的数据
	 */
	private void getDataFromServer() {
		// 传一个服务器端视频的URL
		RequestParams params = new RequestParams(VIP_SERVER_URL);
		// 请求数据是在子线程，但是回调的时候内部封装了，转化为主线程，而我们的解析数据非常快，在主线程即可
		x.http().get(params, new Callback.CommonCallback<String>() {

			@Override
			public void onCancelled(CancelledException arg0) {
				Log.e("onCancelled", "onCancelled");
			}

			@Override
			public void onError(Throwable arg0, boolean arg1) {
				Log.e("onError", "请求失败" + arg0.getMessage());
			}

			@Override
			public void onFinished() {
				Log.e("onFinished", "请求完成");
			}

			@Override
			public void onSuccess(String result) {
				Log.e("onSuccess", "请求成功");
				// 主线程
				// 解析数据
				processDataFromServer(result);
			}

		});
	}

	/**
	 * 解析从服务器获得的数据
	 * 
	 * @param result
	 */
	private void processDataFromServer(String json) {
		vipVideoItems = parseJSON(json);
		if (null != vipVideoItems && vipVideoItems.size() > 0) {
			// 有网络，设置适配器
			vipVideoPageAdapter = new VipVideoPageAdapter(context,
					vipVideoItems);
			vipvideo_listview.setAdapter(vipVideoPageAdapter);
			// 文本影藏
			viptv_nonet.setVisibility(View.GONE);

		} else {
			// 没有网络，文本显示
			viptv_nonet.setText("没有数据..");
			viptv_nonet.setVisibility(View.VISIBLE);
		}
		// progress影藏
		vippb_loading.setVisibility(View.GONE);

	}

	/**
	 * 解析Json数据
	 * 
	 * @param json
	 * @return
	 */
	private List<VipVideoItem> parseJSON(String json) {
		List<VipVideoItem> videos = new ArrayList<VipVideoItem>();

		try {
			JSONArray array = new JSONArray(json);
			for (int i = 0; i < array.length(); i++) {
				VipVideoItem videoItem = new VipVideoItem();
				JSONObject jsonObjectItem = (JSONObject) array.opt(i);
				String id = jsonObjectItem.optString("id");// 视频的id
				videoItem.setId(id);
				String videoName = jsonObjectItem.optString("videoname");// 视频的名称
				videoItem.setVideoname(videoName);
				String imgUrl = jsonObjectItem.optString("descimg");// 视频的图片地址
				videoItem.setDescimg(imgUrl);
				String desc = jsonObjectItem.optString("videodesc");// 视频的描述信息
				videoItem.setVideodesc(desc);
				String videosrc = jsonObjectItem.optString("videosrc");// 视频的播放地址
				videoItem.setVideosrc(videosrc);
				float needmoney = (float) jsonObjectItem.optDouble("needmoney");// 视频播放需要付的费用
				videoItem.setNeedmoney(needmoney);
				videos.add(videoItem);
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}

		return videos;
	}

}
