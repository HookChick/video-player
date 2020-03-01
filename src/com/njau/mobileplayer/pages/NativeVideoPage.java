package com.njau.mobileplayer.pages;

import java.util.ArrayList;
import java.util.List;

import android.annotation.SuppressLint;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.njau.mobileplayer.R;
import com.njau.mobileplayer.activity.MySystemDefinedVideoPlayer;
import com.njau.mobileplayer.adapter.MyNativeVideoPageAdapter;
import com.njau.mobileplayer.base.BasePage;
import com.njau.mobileplayer.entity.VideoItem;

/**
 * 本地视频界面
 * @author zhangcan
 *
 */
public class NativeVideoPage extends BasePage {
	
	private ListView listview;
	private TextView tv_nomiedia;
	private ProgressBar pb_loading;
	
	private MyNativeVideoPageAdapter myVideoPageAdapter;
	
	/**
	 * 存放一个一个视频文件基本信息组成的对象的集合
	 */
	private List<VideoItem> videoItems;

	public NativeVideoPage(Context context) {
		super(context);
	}

	@Override
	public View initView() {
		View view = View.inflate(context, R.layout.video_page, null);
		this.listview = (ListView) view.findViewById(R.id.listview);
		this.tv_nomiedia = (TextView) view.findViewById(R.id.tv_nomiedia);
		this.pb_loading = (ProgressBar) view.findViewById(R.id.pb_loading);
		
		//设置ListView 的Item点击事件、
		this.listview.setOnItemClickListener(new MyOnItemClickListener());
		return view;
	}
	
	/**
	 * 处理点击事件
	 * @author zhangcan
	 *
	 */
	class MyOnItemClickListener implements OnItemClickListener{

		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position,
				long id) {
//			VideoItem videoItem = videoItems.get(position);
//			Toast.makeText(context, "点击了"+videoItem.toString(), Toast.LENGTH_SHORT).show();
			//调用我自定义播放器,这里只穿了一个视频的播放地址
//			Intent intent = new Intent(context,MySystemDefinedVideoPlayer.class);
//			intent.setDataAndType(Uri.parse(videoItem.getData()), "video/*");
//			context.startActivity(intent);
			
			//需要传递视频列表过去，便于在播放界面播放上一个下一个视频
			Intent intent = new Intent(context,MySystemDefinedVideoPlayer.class);
			Bundle bundle = new Bundle();
			bundle.putSerializable("videolist", (ArrayList<VideoItem>)videoItems);//传递视频播放列表
			intent.putExtras(bundle);
			intent.putExtra("position", position);
			context.startActivity(intent);
			
		}
		
	}
	
	@Override
	public void initData() {
		super.initData();
		getVideoFromLocal();
	}

	@SuppressLint("HandlerLeak") private Handler handler = new Handler(){
		
		@Override
		public void handleMessage(android.os.Message msg) {
			super.handleMessage(msg);
			if(null != videoItems && videoItems.size() > 0){
				//有数据，设置适配器
				myVideoPageAdapter = new MyNativeVideoPageAdapter(context , videoItems);
				listview.setAdapter(myVideoPageAdapter);
				//文本影藏
				tv_nomiedia.setVisibility(View.GONE);
				
			}else{
				//没有数据，文本显示
				tv_nomiedia.setVisibility(View.VISIBLE);
			}
			//progress影藏
			pb_loading.setVisibility(View.GONE);
		};
	};
	
    private int i=0;
	/**
	 * 从本地的sdCard获取视频文件
	 * 这里从内容提供者中获取视频
	 */
	private void getVideoFromLocal() {
		
		//获取视频的操作是比较耗时的所以另外开启一个子线程来操作
		new Thread(){

			@Override
			public void run() {
				videoItems = new ArrayList<VideoItem>();
				ContentResolver contentResolver = context.getContentResolver();
				String[] objs = {
						MediaStore.Video.Media.DISPLAY_NAME,//视频文件在sdCard中的名称
						MediaStore.Video.Media.DURATION,//视频文件总时长
						MediaStore.Video.Media.SIZE,//视频文件大小
						MediaStore.Video.Media.DATA,//视频文件的绝对地址
						};
				Cursor cursor = contentResolver.query(MediaStore.Video.Media.EXTERNAL_CONTENT_URI, objs, null, null, null);
				
				if(cursor != null){
					while(cursor.moveToNext() && i<cursor.getCount()){
						i++;
						VideoItem videoItem = new VideoItem();
						String videoName = cursor.getString(0); //视频的名称
						long duration = cursor.getLong(1);//视频文件总时长
						long size = cursor.getLong(2);//视频文件大小
						String data = cursor.getString(3);//视频文件的绝对地址
						
						videoItem.setVideoName(videoName);
						videoItem.setDuration(duration);
						videoItem.setSize(size);
						videoItem.setData(data);
						videoItems.add(videoItem);
						Log.d("videoItemssize","size"+videoItems.size());
					}
					cursor.close();
					
				}
				Log.d("inMyVideoPageAdapterSize","size"+videoItems.size());
				//发消息
				handler.sendEmptyMessage(10);
			}
			
		}.start();
		i=0;
	}

}
