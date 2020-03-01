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
 * 网络视频界面
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
	private RadioGroup id_gallery_type;//顶部的类型的按钮组
	
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
	 * 存放一个一个视频文件基本信息组成的对象的集合
	 */
	private List<VideoItem> videoItemsAll;

	/**
	 * 网络视频的适配器
	 */
	private NetVideoPageAdapter netVideoPageAdapter;

	/**
	 * 是否加载更多了
	 */
	private boolean isLoadMore = false;
	
    
	public NetVideoPage(Context context) {
		super(context);
		
	}

	/**
	 * 显示适配器的数据集合
	 */
	private List<VideoItem> videoItems;
	
	/**
	 * 当前选中的类型
	 */
	private String currentSelectType;
	
	@Override
	public View initView() {
		View view = View.inflate(context, R.layout.netvideo_page, null);
		// 第一个参数当前对象，第二个是实例化的布局
		// 有这个就把这个view的布局通过这个框架传递给了这个类，就可以实例化了
		x.view().inject(this, view);

		
		// 设置点击事件
		netvideo_listview.setOnItemClickListener(new MyOnItemClickListener());

		netvideo_listview.setPullLoadEnable(true);
		netvideo_listview.setXListViewListener(new MyIXListViewListener());

		//设置按钮RadioGroup 的点击监听事件
		id_gallery_type.setOnCheckedChangeListener(new MyOnCheckedChangeListener());
		id_gallery_type.check(R.id.radioButtonall);//默认选中本地视频
		
		return view;
	}

	/**
	 * 在该类中用来实现RadioGroup监听事件的内部类
	 * @author zhangcan
	 *
	 */
	private class MyOnCheckedChangeListener implements OnCheckedChangeListener{

		@Override
		public void onCheckedChanged(RadioGroup group, int checkedId) {
			String text = "";
			switch (checkedId) {
			default://全部
				videoItems = videoItemsAll;
				currentSelectType = "all";
				if (null != videoItems && videoItems.size() > 0) {
					netVideoPageAdapter = new NetVideoPageAdapter(context, videoItems);
					netvideo_listview.setAdapter(netVideoPageAdapter);
					tv_nonet.setVisibility(View.GONE);
					
				} else {
					// 没有网络，文本显示
					tv_nonet.setVisibility(View.VISIBLE);
				}
				// progress影藏
				pb_loading.setVisibility(View.GONE);
				break;
			case R.id.radioButtonall://全部
				videoItems = videoItemsAll;
				currentSelectType = "all";
				if (null != videoItems && videoItems.size() > 0) {
					netVideoPageAdapter = new NetVideoPageAdapter(context, videoItems);
					netvideo_listview.setAdapter(netVideoPageAdapter);
					tv_nonet.setVisibility(View.GONE);
					
				} else {
					// 没有网络，文本显示
					tv_nonet.setVisibility(View.VISIBLE);
				}
				// progress影藏
				pb_loading.setVisibility(View.GONE);
				break;
				
			case R.id.radioButton1://动画
				text = radioButton1.getText().toString().trim();
				currentSelectType = text;
				videoItems = getCurrentItemDatas(text);
				netVideoPageAdapter = new NetVideoPageAdapter(context, videoItems);
				netvideo_listview.setAdapter(netVideoPageAdapter);
				tv_nonet.setVisibility(View.GONE);
				break;
			case R.id.radioButton2://冒险
				text = radioButton2.getText().toString().trim();
				currentSelectType = text;
				videoItems = getCurrentItemDatas(text);
//				netVideoPageAdapter.notifyDataSetChanged();
				netVideoPageAdapter = new NetVideoPageAdapter(context, videoItems);
				netvideo_listview.setAdapter(netVideoPageAdapter);
				tv_nonet.setVisibility(View.GONE);
				break;
			case R.id.radioButton3://喜剧
				text = radioButton3.getText().toString().trim();
				currentSelectType = text;
				videoItems = getCurrentItemDatas(text);
				netVideoPageAdapter = new NetVideoPageAdapter(context, videoItems);
				netvideo_listview.setAdapter(netVideoPageAdapter);
				tv_nonet.setVisibility(View.GONE);
				break;
			case R.id.radioButton4://家庭
				text = radioButton4.getText().toString().trim();
				currentSelectType = text;
				videoItems = getCurrentItemDatas(text);
				netVideoPageAdapter = new NetVideoPageAdapter(context, videoItems);
				netvideo_listview.setAdapter(netVideoPageAdapter);
				tv_nonet.setVisibility(View.GONE);
				break;
			case R.id.radioButton6://动作
				text = radioButton6.getText().toString().trim();
				currentSelectType = text;
				videoItems = getCurrentItemDatas(text);
				netVideoPageAdapter = new NetVideoPageAdapter(context, videoItems);
				netvideo_listview.setAdapter(netVideoPageAdapter);
				tv_nonet.setVisibility(View.GONE);
				break;
			case R.id.radioButton7://奇幻
				text = radioButton7.getText().toString().trim();
				currentSelectType = text;
				videoItems = getCurrentItemDatas(text);
				netVideoPageAdapter = new NetVideoPageAdapter(context, videoItems);
				netvideo_listview.setAdapter(netVideoPageAdapter);
				tv_nonet.setVisibility(View.GONE);
				break;
			case R.id.radioButton8://恐怖
				text = radioButton8.getText().toString().trim();
				currentSelectType = text;
				videoItems = getCurrentItemDatas(text);
				netVideoPageAdapter = new NetVideoPageAdapter(context, videoItems);
				netvideo_listview.setAdapter(netVideoPageAdapter);
				tv_nonet.setVisibility(View.GONE);
				break;
			case R.id.radioButton9://科幻
				text = radioButton9.getText().toString().trim();
				currentSelectType = text;
				videoItems = getCurrentItemDatas(text);
				netVideoPageAdapter = new NetVideoPageAdapter(context, videoItems);
				netvideo_listview.setAdapter(netVideoPageAdapter);
				tv_nonet.setVisibility(View.GONE);
				break;
			case R.id.radioButton10://惊悚
				text = radioButton10.getText().toString().trim();
				currentSelectType = text;
				videoItems = getCurrentItemDatas(text);
				netVideoPageAdapter = new NetVideoPageAdapter(context, videoItems);
				netvideo_listview.setAdapter(netVideoPageAdapter);
				tv_nonet.setVisibility(View.GONE);
				break;
			case R.id.radioButton11://剧情
				text = radioButton11.getText().toString().trim();
				currentSelectType = text;
				videoItems = getCurrentItemDatas(text);
				netVideoPageAdapter = new NetVideoPageAdapter(context, videoItems);
				netvideo_listview.setAdapter(netVideoPageAdapter);
				tv_nonet.setVisibility(View.GONE);
				break;
			case R.id.radioButton13://悬疑
				text = radioButton13.getText().toString().trim();
				currentSelectType = text;
				videoItems = getCurrentItemDatas(text);
				netVideoPageAdapter = new NetVideoPageAdapter(context, videoItems);
				netvideo_listview.setAdapter(netVideoPageAdapter);
				tv_nonet.setVisibility(View.GONE);
				break;
			case R.id.radioButton14://犯罪
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
	 * 根据选择的分类的选项获取相关的数据
	 * @param text 选择的分类的值
	 * @return
	 */
	public List<VideoItem> getCurrentItemDatas(String text) {
		if("all".equals(text)){ //如果全部则返回数据
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
	 * 根据选择的分类的选项从给定的数据集合里面获取数据集合
	 * @param itemLists  VideoItem的数据集合
	 * @param text  选择的分类的值
	 * @return
	 */
	public List<VideoItem> getCurrentItemDatas(List<VideoItem> itemLists,String text) {
		
		if("all".equals(text)){//如果是全部则返回传入的数据
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
	 * xListView的监听事件
	 * 
	 * @author zhangcan
	 * 
	 */
	class MyIXListViewListener implements XListView.IXListViewListener {

		@Override
		public void onRefresh() {
			// 再次联网请求
			// 需要重新覆盖数据
			getDataFromNet();
		}

		@Override
		public void onLoadMore() {
			// 加载更多
			getMoreDataFromNet();
		}

	}

	@Override
	public void initData() {
		super.initData();
		String catchData = StringUtils.getString(context, Constant.NET_URI);
		if(null != catchData && !"".equals(catchData)){//缓存的数据不空则可以解析数据
			processDataFromNet(catchData);
		}
		getDataFromNet();
	}


	/**
	 * 加载更多数据，同样去网络联网请求
	 */
	private void getMoreDataFromNet() {
		// 传一个网络视频的URL
		RequestParams params = new RequestParams(Constant.NET_URI);
		// 联网请求是在子线程，但是回调的时候内部封装了，转化为主线程，而我们的解析数据非常快，在主线程即可
		x.http().get(params, new Callback.CommonCallback<String>() {

			@Override
			public void onCancelled(CancelledException arg0) {
				Log.e("onCancelled", "onCancelled");
				isLoadMore = false;//加载取消了
			}

			@Override
			public void onError(Throwable arg0, boolean arg1) {
				Log.e("onError", "联网失败" + arg0.getMessage());
				isLoadMore = false;//加载失败

			}

			@Override
			public void onFinished() {
				Log.e("onFinished", "联网完成");
				isLoadMore = false;//加载完成
			}

			@Override
			public void onSuccess(String result) {
				 Log.e("onSuccess","联网成功");
				isLoadMore = true;//加载成功
				// 主线程
				processDataFromNet(result);
			}

		});
	}

	/**
	 * 联网请求数据
	 */
	private void getDataFromNet() {
		// 传一个网络视频的URL
		RequestParams params = new RequestParams(Constant.NET_URI);
		// 联网请求是在子线程，但是回调的时候内部封装了，转化为主线程，而我们的解析数据非常快，在主线程即可
		x.http().get(params, new Callback.CommonCallback<String>() {

			@Override
			public void onCancelled(CancelledException arg0) {
				Log.e("onCancelled", "onCancelled");
			}

			@Override
			public void onError(Throwable arg0, boolean arg1) {
				Log.e("onError", "联网失败" + arg0.getMessage());
//				tv_nonet.setVisibility(View.VISIBLE);
//				pb_loading.setVisibility(View.GONE);
			}

			@Override
			public void onFinished() {
				Log.e("onFinished", "联网完成");
			}

			@Override
			public void onSuccess(String result) {
				 Log.e("onSuccess","联网成功");
				// 主线程
				//先保存缓存的数据
				StringUtils.putString(context, Constant.NET_URI, result);
				//再解析数据
				processDataFromNet(result);
			}

		});
	}

	/**
	 * 停止刷新停止加载更多
	 */
	private void onLoad() {
		netvideo_listview.stopRefresh();
		netvideo_listview.stopLoadMore();
		netvideo_listview.setRefreshTime(StringUtils.getSystemTime());
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

			Intent intent = new Intent(context,
					AboutVideoActivity.class);
			Bundle bundle = new Bundle();
			bundle.putSerializable("videoitemclick",
					videoItems.get(position -1));// 传递当前视频
			intent.putExtras(bundle);
			context.startActivity(intent);

		}
	}

	/**
	 * 解析从服务端获的数据
	 * 
	 * @param result
	 */
	private void processDataFromNet(String json) {

		if(!isLoadMore){
			//如果没有加载更多数据
			videoItemsAll = parseJSON(json);
			videoItems = getCurrentItemDatas(currentSelectType);
			if (null != videoItems && videoItems.size() > 0) {
				
				// 有网络，设置适配器
				netVideoPageAdapter = new NetVideoPageAdapter(context, videoItems);
				netvideo_listview.setAdapter(netVideoPageAdapter);
				onLoad();
				//文本影藏
				tv_nonet.setVisibility(View.GONE);
				
			} else {
				// 没有网络，文本显示
				tv_nonet.setVisibility(View.VISIBLE);
			}
			// progress影藏
			pb_loading.setVisibility(View.GONE);
		}else{
			//加载更多的数据
			//要把得到的更多的数据添加到原来的集合中
			isLoadMore = false;//还原
			List<VideoItem> moreItems = parseJSON(json);
			videoItems.addAll(getCurrentItemDatas(moreItems, currentSelectType));//添加重新解析的数据
			
			netVideoPageAdapter.notifyDataSetChanged();
			onLoad();
		}
	}

	/**
	 * 解析JSON数据 用系统的接口解析
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
				String videoName = jsonObjectItem.optString("movieName");// 视频的名称
				videoItem.setVideoName(videoName);
				String imgUrl = jsonObjectItem.optString("coverImg");// 视频的图片地址
				videoItem.setImgUrl(imgUrl);
				String desc = jsonObjectItem.optString("videoTitle");// 视频的描述信息
				videoItem.setDescription(desc);
				String summary = jsonObjectItem.optString("summary"); //视频概要
				videoItem.setSummary(summary);
				int videoLength = jsonObjectItem.optInt("videoLength"); //视频时长
				videoItem.setDuration(videoLength);
				float rating = (float) jsonObjectItem.optDouble("rating");
				videoItem.setRating(rating);
				String videoUrl = jsonObjectItem.optString("hightUrl");// 高清视频的播放地址
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
