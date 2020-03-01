package com.njau.mobileplayer.pages;

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
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.TextView;

import com.njau.mobileplayer.R;
import com.njau.mobileplayer.activity.AboutVideoActivity;
import com.njau.mobileplayer.adapter.NetVideoPageAdapter;
import com.njau.mobileplayer.base.BasePage;
import com.njau.mobileplayer.entity.VideoItem;
import com.njau.mobileplayer.utils.Constant;
import com.njau.mobileplayer.utils.StringUtils;
import com.njau.mobileplayer.xlistview.XListView;

/**
 * ������Ƶ����
 * 
 * @author zhangcan
 * 
 */
public class NetVideoPage extends BasePage  {

	@ViewInject(R.id.netvideo_listview)
	private XListView netvideo_listview;

	@ViewInject(R.id.tv_nonet)
	private TextView tv_nonet;

	@ViewInject(R.id.pb_loading)
	private ProgressBar pb_loading;
	
	@ViewInject(R.id.id_gallery_type)
	private RadioGroup id_gallery_type;//���������͵İ�ť��
	
	@ViewInject(R.id.radioButtonall)
	private RadioButton radioButtonall;
	@ViewInject(R.id.radioButton1)
	private RadioButton radioButton1;
	@ViewInject(R.id.radioButton2)
	private RadioButton radioButton2;
	@ViewInject(R.id.radioButton3)
	private RadioButton radioButton3;
	@ViewInject(R.id.radioButton4)
	private RadioButton radioButton4;
	@ViewInject(R.id.radioButton6)
	private RadioButton radioButton6;
	@ViewInject(R.id.radioButton7)
	private RadioButton radioButton7;
	@ViewInject(R.id.radioButton8)
	private RadioButton radioButton8;
	@ViewInject(R.id.radioButton9)
	private RadioButton radioButton9;
	@ViewInject(R.id.radioButton10)
	private RadioButton radioButton10;
	@ViewInject(R.id.radioButton11)
	private RadioButton radioButton11;
	@ViewInject(R.id.radioButton13)
	private RadioButton radioButton13;
	@ViewInject(R.id.radioButton14)
	private RadioButton radioButton14;
	

	/**
	 * ���һ��һ����Ƶ�ļ�������Ϣ��ɵĶ���ļ���
	 */
	private List<VideoItem> videoItemsAll;

	/**
	 * ������Ƶ��������
	 */
	private NetVideoPageAdapter netVideoPageAdapter;

	/**
	 * �Ƿ���ظ�����
	 */
	private boolean isLoadMore = false;
	
    
	public NetVideoPage(Context context) {
		super(context);
		
	}

	/**
	 * ��ʾ�����������ݼ���
	 */
	private List<VideoItem> videoItems;
	
	/**
	 * ��ǰѡ�е�����
	 */
	private String currentSelectType;
	
	@Override
	public View initView() {
		View view = View.inflate(context, R.layout.netvideo_page, null);
		// ��һ��������ǰ���󣬵ڶ�����ʵ�����Ĳ���
		// ������Ͱ����view�Ĳ���ͨ�������ܴ��ݸ�������࣬�Ϳ���ʵ������
		x.view().inject(this, view);

		
		// ���õ���¼�
		netvideo_listview.setOnItemClickListener(new MyOnItemClickListener());

		netvideo_listview.setPullLoadEnable(true);
		netvideo_listview.setXListViewListener(new MyIXListViewListener());

		//���ð�ťRadioGroup �ĵ�������¼�
		id_gallery_type.setOnCheckedChangeListener(new MyOnCheckedChangeListener());
		id_gallery_type.check(R.id.radioButtonall);//Ĭ��ѡ�б�����Ƶ
		
		return view;
	}

	/**
	 * �ڸ���������ʵ��RadioGroup�����¼����ڲ���
	 * @author zhangcan
	 *
	 */
	private class MyOnCheckedChangeListener implements OnCheckedChangeListener{

		@Override
		public void onCheckedChanged(RadioGroup group, int checkedId) {
			String text = "";
			switch (checkedId) {
			default://ȫ��
				videoItems = videoItemsAll;
				currentSelectType = "all";
				if (null != videoItems && videoItems.size() > 0) {
					netVideoPageAdapter = new NetVideoPageAdapter(context, videoItems);
					netvideo_listview.setAdapter(netVideoPageAdapter);
					tv_nonet.setVisibility(View.GONE);
					
				} else {
					// û�����磬�ı���ʾ
					tv_nonet.setVisibility(View.VISIBLE);
				}
				// progressӰ��
				pb_loading.setVisibility(View.GONE);
				break;
			case R.id.radioButtonall://ȫ��
				videoItems = videoItemsAll;
				currentSelectType = "all";
				if (null != videoItems && videoItems.size() > 0) {
					netVideoPageAdapter = new NetVideoPageAdapter(context, videoItems);
					netvideo_listview.setAdapter(netVideoPageAdapter);
					tv_nonet.setVisibility(View.GONE);
					
				} else {
					// û�����磬�ı���ʾ
					tv_nonet.setVisibility(View.VISIBLE);
				}
				// progressӰ��
				pb_loading.setVisibility(View.GONE);
				break;
				
			case R.id.radioButton1://����
				text = radioButton1.getText().toString().trim();
				currentSelectType = text;
				videoItems = getCurrentItemDatas(text);
				netVideoPageAdapter = new NetVideoPageAdapter(context, videoItems);
				netvideo_listview.setAdapter(netVideoPageAdapter);
				tv_nonet.setVisibility(View.GONE);
				break;
			case R.id.radioButton2://ð��
				text = radioButton2.getText().toString().trim();
				currentSelectType = text;
				videoItems = getCurrentItemDatas(text);
//				netVideoPageAdapter.notifyDataSetChanged();
				netVideoPageAdapter = new NetVideoPageAdapter(context, videoItems);
				netvideo_listview.setAdapter(netVideoPageAdapter);
				tv_nonet.setVisibility(View.GONE);
				break;
			case R.id.radioButton3://ϲ��
				text = radioButton3.getText().toString().trim();
				currentSelectType = text;
				videoItems = getCurrentItemDatas(text);
				netVideoPageAdapter = new NetVideoPageAdapter(context, videoItems);
				netvideo_listview.setAdapter(netVideoPageAdapter);
				tv_nonet.setVisibility(View.GONE);
				break;
			case R.id.radioButton4://��ͥ
				text = radioButton4.getText().toString().trim();
				currentSelectType = text;
				videoItems = getCurrentItemDatas(text);
				netVideoPageAdapter = new NetVideoPageAdapter(context, videoItems);
				netvideo_listview.setAdapter(netVideoPageAdapter);
				tv_nonet.setVisibility(View.GONE);
				break;
			case R.id.radioButton6://����
				text = radioButton6.getText().toString().trim();
				currentSelectType = text;
				videoItems = getCurrentItemDatas(text);
				netVideoPageAdapter = new NetVideoPageAdapter(context, videoItems);
				netvideo_listview.setAdapter(netVideoPageAdapter);
				tv_nonet.setVisibility(View.GONE);
				break;
			case R.id.radioButton7://���
				text = radioButton7.getText().toString().trim();
				currentSelectType = text;
				videoItems = getCurrentItemDatas(text);
				netVideoPageAdapter = new NetVideoPageAdapter(context, videoItems);
				netvideo_listview.setAdapter(netVideoPageAdapter);
				tv_nonet.setVisibility(View.GONE);
				break;
			case R.id.radioButton8://�ֲ�
				text = radioButton8.getText().toString().trim();
				currentSelectType = text;
				videoItems = getCurrentItemDatas(text);
				netVideoPageAdapter = new NetVideoPageAdapter(context, videoItems);
				netvideo_listview.setAdapter(netVideoPageAdapter);
				tv_nonet.setVisibility(View.GONE);
				break;
			case R.id.radioButton9://�ƻ�
				text = radioButton9.getText().toString().trim();
				currentSelectType = text;
				videoItems = getCurrentItemDatas(text);
				netVideoPageAdapter = new NetVideoPageAdapter(context, videoItems);
				netvideo_listview.setAdapter(netVideoPageAdapter);
				tv_nonet.setVisibility(View.GONE);
				break;
			case R.id.radioButton10://���
				text = radioButton10.getText().toString().trim();
				currentSelectType = text;
				videoItems = getCurrentItemDatas(text);
				netVideoPageAdapter = new NetVideoPageAdapter(context, videoItems);
				netvideo_listview.setAdapter(netVideoPageAdapter);
				tv_nonet.setVisibility(View.GONE);
				break;
			case R.id.radioButton11://����
				text = radioButton11.getText().toString().trim();
				currentSelectType = text;
				videoItems = getCurrentItemDatas(text);
				netVideoPageAdapter = new NetVideoPageAdapter(context, videoItems);
				netvideo_listview.setAdapter(netVideoPageAdapter);
				tv_nonet.setVisibility(View.GONE);
				break;
			case R.id.radioButton13://����
				text = radioButton13.getText().toString().trim();
				currentSelectType = text;
				videoItems = getCurrentItemDatas(text);
				netVideoPageAdapter = new NetVideoPageAdapter(context, videoItems);
				netvideo_listview.setAdapter(netVideoPageAdapter);
				tv_nonet.setVisibility(View.GONE);
				break;
			case R.id.radioButton14://����
				text = radioButton14.getText().toString().trim();
				currentSelectType = text;
				videoItems = getCurrentItemDatas(text);
				netVideoPageAdapter = new NetVideoPageAdapter(context, videoItems);
				netvideo_listview.setAdapter(netVideoPageAdapter);
				tv_nonet.setVisibility(View.GONE);
				break;
				
			}
		}
		
	}
	
	/**
	 * ����ѡ��ķ����ѡ���ȡ��ص�����
	 * @param text ѡ��ķ����ֵ
	 * @return
	 */
	public List<VideoItem> getCurrentItemDatas(String text) {
		if("all".equals(text)){ //���ȫ���򷵻�����
			return this.videoItemsAll;
		}
		List<VideoItem> items = new ArrayList<VideoItem>();
		for(VideoItem item : this.videoItemsAll){
			List<String> typeString = item.getType();
			if(typeString.contains(text)){
				items.add(item);
			}
		}
		return items;
	}
	
	/**
	 * ����ѡ��ķ����ѡ��Ӹ��������ݼ��������ȡ���ݼ���
	 * @param itemLists  VideoItem�����ݼ���
	 * @param text  ѡ��ķ����ֵ
	 * @return
	 */
	public List<VideoItem> getCurrentItemDatas(List<VideoItem> itemLists,String text) {
		
		if("all".equals(text)){//�����ȫ���򷵻ش��������
			return itemLists;
		}
		List<VideoItem> items = new ArrayList<VideoItem>();
		for(VideoItem item : itemLists){
			List<String> typeString = item.getType();
			if(typeString.contains(text)){
				items.add(item);
			}
		}
		return items;
	}
	
	/**
	 * xListView�ļ����¼�
	 * 
	 * @author zhangcan
	 * 
	 */
	class MyIXListViewListener implements XListView.IXListViewListener {

		@Override
		public void onRefresh() {
			// �ٴ���������
			// ��Ҫ���¸�������
			getDataFromNet();
		}

		@Override
		public void onLoadMore() {
			// ���ظ���
			getMoreDataFromNet();
		}

	}

	@Override
	public void initData() {
		super.initData();
		String catchData = StringUtils.getString(context, Constant.NET_URI);
		if(null != catchData && !"".equals(catchData)){//��������ݲ�������Խ�������
			processDataFromNet(catchData);
		}
		getDataFromNet();
	}


	/**
	 * ���ظ������ݣ�ͬ��ȥ������������
	 */
	private void getMoreDataFromNet() {
		// ��һ��������Ƶ��URL
		RequestParams params = new RequestParams(Constant.NET_URI);
		// ���������������̣߳����ǻص���ʱ���ڲ���װ�ˣ�ת��Ϊ���̣߳������ǵĽ������ݷǳ��죬�����̼߳���
		x.http().get(params, new Callback.CommonCallback<String>() {

			@Override
			public void onCancelled(CancelledException arg0) {
				Log.e("onCancelled", "onCancelled");
				isLoadMore = false;//����ȡ����
			}

			@Override
			public void onError(Throwable arg0, boolean arg1) {
				Log.e("onError", "����ʧ��" + arg0.getMessage());
				isLoadMore = false;//����ʧ��

			}

			@Override
			public void onFinished() {
				Log.e("onFinished", "�������");
				isLoadMore = false;//�������
			}

			@Override
			public void onSuccess(String result) {
				 Log.e("onSuccess","�����ɹ�");
				isLoadMore = true;//���سɹ�
				// ���߳�
				processDataFromNet(result);
			}

		});
	}

	/**
	 * ������������
	 */
	private void getDataFromNet() {
		// ��һ��������Ƶ��URL
		RequestParams params = new RequestParams(Constant.NET_URI);
		// ���������������̣߳����ǻص���ʱ���ڲ���װ�ˣ�ת��Ϊ���̣߳������ǵĽ������ݷǳ��죬�����̼߳���
		x.http().get(params, new Callback.CommonCallback<String>() {

			@Override
			public void onCancelled(CancelledException arg0) {
				Log.e("onCancelled", "onCancelled");
			}

			@Override
			public void onError(Throwable arg0, boolean arg1) {
				Log.e("onError", "����ʧ��" + arg0.getMessage());
//				tv_nonet.setVisibility(View.VISIBLE);
//				pb_loading.setVisibility(View.GONE);
			}

			@Override
			public void onFinished() {
				Log.e("onFinished", "�������");
			}

			@Override
			public void onSuccess(String result) {
				 Log.e("onSuccess","�����ɹ�");
				// ���߳�
				//�ȱ��滺�������
				StringUtils.putString(context, Constant.NET_URI, result);
				//�ٽ�������
				processDataFromNet(result);
			}

		});
	}

	/**
	 * ֹͣˢ��ֹͣ���ظ���
	 */
	private void onLoad() {
		netvideo_listview.stopRefresh();
		netvideo_listview.stopLoadMore();
		netvideo_listview.setRefreshTime(StringUtils.getSystemTime());
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

			Intent intent = new Intent(context,
					AboutVideoActivity.class);
			Bundle bundle = new Bundle();
			bundle.putSerializable("videoitemclick",
					videoItems.get(position -1));// ���ݵ�ǰ��Ƶ
			intent.putExtras(bundle);
			context.startActivity(intent);

		}
	}

	/**
	 * �����ӷ���˻������
	 * 
	 * @param result
	 */
	private void processDataFromNet(String json) {

		if(!isLoadMore){
			//���û�м��ظ�������
			videoItemsAll = parseJSON(json);
			videoItems = getCurrentItemDatas(currentSelectType);
			if (null != videoItems && videoItems.size() > 0) {
				
				// �����磬����������
				netVideoPageAdapter = new NetVideoPageAdapter(context, videoItems);
				netvideo_listview.setAdapter(netVideoPageAdapter);
				onLoad();
				//�ı�Ӱ��
				tv_nonet.setVisibility(View.GONE);
				
			} else {
				// û�����磬�ı���ʾ
				tv_nonet.setVisibility(View.VISIBLE);
			}
			// progressӰ��
			pb_loading.setVisibility(View.GONE);
		}else{
			//���ظ��������
			//Ҫ�ѵõ��ĸ����������ӵ�ԭ���ļ�����
			isLoadMore = false;//��ԭ
			List<VideoItem> moreItems = parseJSON(json);
			videoItems.addAll(getCurrentItemDatas(moreItems, currentSelectType));//������½���������
			
			netVideoPageAdapter.notifyDataSetChanged();
			onLoad();
		}
	}

	/**
	 * ����JSON���� ��ϵͳ�Ľӿڽ���
	 * 
	 * @param json
	 * @return
	 */
	private List<VideoItem> parseJSON(String json) {
		List<VideoItem> videos = new ArrayList<VideoItem>();

		try {
			JSONObject jsonObject = new JSONObject(json);
			JSONArray array = jsonObject.optJSONArray("trailers");
			for (int i = 0; i < array.length(); i++) {
				VideoItem videoItem = new VideoItem();
				JSONObject jsonObjectItem = (JSONObject) array.opt(i);
				String videoName = jsonObjectItem.optString("movieName");// ��Ƶ������
				videoItem.setVideoName(videoName);
				String imgUrl = jsonObjectItem.optString("coverImg");// ��Ƶ��ͼƬ��ַ
				videoItem.setImgUrl(imgUrl);
				String desc = jsonObjectItem.optString("videoTitle");// ��Ƶ��������Ϣ
				videoItem.setDescription(desc);
				String summary = jsonObjectItem.optString("summary"); //��Ƶ��Ҫ
				videoItem.setSummary(summary);
				int videoLength = jsonObjectItem.optInt("videoLength"); //��Ƶʱ��
				videoItem.setDuration(videoLength);
				float rating = (float) jsonObjectItem.optDouble("rating");
				videoItem.setRating(rating);
				String videoUrl = jsonObjectItem.optString("hightUrl");// ������Ƶ�Ĳ��ŵ�ַ
				videoItem.setData(videoUrl);
				 
				JSONArray arrayType = (JSONArray) jsonObjectItem.get("type");
				List<String> types = new ArrayList<String>();
				for(int j = 0;j<arrayType.length();j++){
					String type = (String) arrayType.opt(j);
					types.add(type);
				}
				
				videoItem.setType(types);
				
				videos.add(videoItem);
			}
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return videos;
	}
	
}
