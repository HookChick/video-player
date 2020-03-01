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
 * ������Ƶ����
 * @author zhangcan
 *
 */
public class NativeVideoPage extends BasePage {
	
	private ListView listview;
	private TextView tv_nomiedia;
	private ProgressBar pb_loading;
	
	private MyNativeVideoPageAdapter myVideoPageAdapter;
	
	/**
	 * ���һ��һ����Ƶ�ļ�������Ϣ��ɵĶ���ļ���
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
		
		//����ListView ��Item����¼���
		this.listview.setOnItemClickListener(new MyOnItemClickListener());
		return view;
	}
	
	/**
	 * �������¼�
	 * @author zhangcan
	 *
	 */
	class MyOnItemClickListener implements OnItemClickListener{

		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position,
				long id) {
//			VideoItem videoItem = videoItems.get(position);
//			Toast.makeText(context, "�����"+videoItem.toString(), Toast.LENGTH_SHORT).show();
			//�������Զ��岥����,����ֻ����һ����Ƶ�Ĳ��ŵ�ַ
//			Intent intent = new Intent(context,MySystemDefinedVideoPlayer.class);
//			intent.setDataAndType(Uri.parse(videoItem.getData()), "video/*");
//			context.startActivity(intent);
			
			//��Ҫ������Ƶ�б��ȥ�������ڲ��Ž��沥����һ����һ����Ƶ
			Intent intent = new Intent(context,MySystemDefinedVideoPlayer.class);
			Bundle bundle = new Bundle();
			bundle.putSerializable("videolist", (ArrayList<VideoItem>)videoItems);//������Ƶ�����б�
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
				//�����ݣ�����������
				myVideoPageAdapter = new MyNativeVideoPageAdapter(context , videoItems);
				listview.setAdapter(myVideoPageAdapter);
				//�ı�Ӱ��
				tv_nomiedia.setVisibility(View.GONE);
				
			}else{
				//û�����ݣ��ı���ʾ
				tv_nomiedia.setVisibility(View.VISIBLE);
			}
			//progressӰ��
			pb_loading.setVisibility(View.GONE);
		};
	};
	
    private int i=0;
	/**
	 * �ӱ��ص�sdCard��ȡ��Ƶ�ļ�
	 * ����������ṩ���л�ȡ��Ƶ
	 */
	private void getVideoFromLocal() {
		
		//��ȡ��Ƶ�Ĳ����ǱȽϺ�ʱ���������⿪��һ�����߳�������
		new Thread(){

			@Override
			public void run() {
				videoItems = new ArrayList<VideoItem>();
				ContentResolver contentResolver = context.getContentResolver();
				String[] objs = {
						MediaStore.Video.Media.DISPLAY_NAME,//��Ƶ�ļ���sdCard�е�����
						MediaStore.Video.Media.DURATION,//��Ƶ�ļ���ʱ��
						MediaStore.Video.Media.SIZE,//��Ƶ�ļ���С
						MediaStore.Video.Media.DATA,//��Ƶ�ļ��ľ��Ե�ַ
						};
				Cursor cursor = contentResolver.query(MediaStore.Video.Media.EXTERNAL_CONTENT_URI, objs, null, null, null);
				
				if(cursor != null){
					while(cursor.moveToNext() && i<cursor.getCount()){
						i++;
						VideoItem videoItem = new VideoItem();
						String videoName = cursor.getString(0); //��Ƶ������
						long duration = cursor.getLong(1);//��Ƶ�ļ���ʱ��
						long size = cursor.getLong(2);//��Ƶ�ļ���С
						String data = cursor.getString(3);//��Ƶ�ļ��ľ��Ե�ַ
						
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
				//����Ϣ
				handler.sendEmptyMessage(10);
			}
			
		}.start();
		i=0;
	}

}
