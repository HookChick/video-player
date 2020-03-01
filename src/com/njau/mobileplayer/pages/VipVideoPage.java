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
 * VIPר����Ƶ����
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
	 * ���һ��һ����Ƶ�ļ�������Ϣ��ɵĶ���ļ���
	 */
	private List<VipVideoItem> vipVideoItems;

	private VipVideoPageAdapter vipVideoPageAdapter;

	/**
	 * �ж��Ƿ񸶿true��ʾ������
	 */
	private boolean isPayed;
	
	/**
	 * ����Ƿ񸶿��Url
	 */
	private String checkPayUrl;
	
	/**
	 * �û��������Ƶ��
	 */
	private VipVideoItem item;
	
	public VipVideoPage(Context context) {
		super(context);
	}

	@Override
	public View initView() {
		View view = View.inflate(context, R.layout.vipvideo_page, null);
		// ��һ��������ǰ���󣬵ڶ�����ʵ�����Ĳ���
		// ������Ͱ����view�Ĳ���ͨ�������ܴ��ݸ�������࣬�Ϳ���ʵ������
		x.view().inject(this, view);
		// ���õ���¼�
		vipvideo_listview.setOnItemClickListener(new MyOnItemClickListener());

		return view;
	}

	/**
	 * ����listView�ĵ���¼�
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
				//��¼�ˣ���ȥ����������û��Ը���Ƶ��û�и���
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
				//��ת��¼����
				Intent intent = new Intent(context,LoginActivity.class);
				context.startActivity(intent);
			}
		}

	}

	/**
	 * �ͷ����ͨѶ������Ƿ񸶿�
	 * @param url ���ݵĵ�ַ
	 */
	private void requestCheckPayedorNot() {
		//����ͨѶ�������µ��߳�
		new Thread(new Runnable() {
			
			@Override
			public void run() {
				HttpURLConnection httpURLConnection = null;
				BufferedReader bufferedReader = null;
				try {
					URL urll = new URL(checkPayUrl); // ����һ��URL
					httpURLConnection = (HttpURLConnection) urll.openConnection(); // �򿪸�URL����
					httpURLConnection.setRequestMethod("GET");// �������󷽷���GET������������GET
					httpURLConnection.setConnectTimeout(5000); // �������ӽ����ĳ�ʱʱ��
					httpURLConnection.setReadTimeout(5000); // �������籨���շ���ʱʱ��
					InputStream inputStream = httpURLConnection.getInputStream();
					bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
					StringBuffer buffer = new StringBuffer();
					String line = null;
					while((line = bufferedReader.readLine())!=null){
						buffer.append(line);
					}
					//������Ϣ
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
	 * ������Ϣ
	 */
	private Handler handler = new Handler(){
		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case 1:
				String responsestr = msg.obj.toString();
				Log.e("responsestr", responsestr);
				if(responsestr!= null&& "true".equals(responsestr)){
					//�����ˣ����Բ��Ÿ���Ƶ
					//�����ˣ�ֱ�Ӳ��Ÿ���Ƶ
					// ��Ҫ������Ƶ
					Log.e("responsestr", responsestr);
					Intent intent = new Intent(context,
							MySystemDefinedVideoPlayer.class);
					Bundle bundle = new Bundle();
					List<VipVideoItem> vipItems = new ArrayList<VipVideoItem>();
					vipItems.add(item);
					bundle.putSerializable("videolist",(ArrayList<VipVideoItem>)vipItems);// ������Ƶ�Ķ���
					intent.putExtras(bundle);
					//			intent.putExtra("position", position);
					context.startActivity(intent);
				}else{
					//û�и���
					//û�и���,������ά��
					Intent intent = new Intent(context,
							QRImageActivity.class);
					Bundle bundle = new Bundle();
					bundle.putSerializable("vipVideoItem", item);//����һ����Ƶ
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
	 * ����������˵�����
	 */
	private void getDataFromServer() {
		// ��һ������������Ƶ��URL
		RequestParams params = new RequestParams(VIP_SERVER_URL);
		// ���������������̣߳����ǻص���ʱ���ڲ���װ�ˣ�ת��Ϊ���̣߳������ǵĽ������ݷǳ��죬�����̼߳���
		x.http().get(params, new Callback.CommonCallback<String>() {

			@Override
			public void onCancelled(CancelledException arg0) {
				Log.e("onCancelled", "onCancelled");
			}

			@Override
			public void onError(Throwable arg0, boolean arg1) {
				Log.e("onError", "����ʧ��" + arg0.getMessage());
			}

			@Override
			public void onFinished() {
				Log.e("onFinished", "�������");
			}

			@Override
			public void onSuccess(String result) {
				Log.e("onSuccess", "����ɹ�");
				// ���߳�
				// ��������
				processDataFromServer(result);
			}

		});
	}

	/**
	 * �����ӷ�������õ�����
	 * 
	 * @param result
	 */
	private void processDataFromServer(String json) {
		vipVideoItems = parseJSON(json);
		if (null != vipVideoItems && vipVideoItems.size() > 0) {
			// �����磬����������
			vipVideoPageAdapter = new VipVideoPageAdapter(context,
					vipVideoItems);
			vipvideo_listview.setAdapter(vipVideoPageAdapter);
			// �ı�Ӱ��
			viptv_nonet.setVisibility(View.GONE);

		} else {
			// û�����磬�ı���ʾ
			viptv_nonet.setText("û������..");
			viptv_nonet.setVisibility(View.VISIBLE);
		}
		// progressӰ��
		vippb_loading.setVisibility(View.GONE);

	}

	/**
	 * ����Json����
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
				String id = jsonObjectItem.optString("id");// ��Ƶ��id
				videoItem.setId(id);
				String videoName = jsonObjectItem.optString("videoname");// ��Ƶ������
				videoItem.setVideoname(videoName);
				String imgUrl = jsonObjectItem.optString("descimg");// ��Ƶ��ͼƬ��ַ
				videoItem.setDescimg(imgUrl);
				String desc = jsonObjectItem.optString("videodesc");// ��Ƶ��������Ϣ
				videoItem.setVideodesc(desc);
				String videosrc = jsonObjectItem.optString("videosrc");// ��Ƶ�Ĳ��ŵ�ַ
				videoItem.setVideosrc(videosrc);
				float needmoney = (float) jsonObjectItem.optDouble("needmoney");// ��Ƶ������Ҫ���ķ���
				videoItem.setNeedmoney(needmoney);
				videos.add(videoItem);
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}

		return videos;
	}

}
