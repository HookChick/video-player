package com.njau.mobileplayer.activity;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.app.SearchManager;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.TextView;

import com.iflytek.cloud.record.c;
import com.njau.mobileplayer.R;
import com.njau.mobileplayer.adapter.HistoryActivityAdapter;
import com.njau.mobileplayer.db.SQLiteHelper;
import com.njau.mobileplayer.entity.VideoItem;

/**
 * 历史播放列表页面
 * @author zhangcan
 *
 */
public class HistoryActivity extends Activity implements OnClickListener {

	private ListView historyvideo_listview;
	private TextView history_back_btn;
	private TextView clear_history_btn;
	private TextView tv_norecord;
	
	private List<VideoItem> videos;
	private HistoryActivityAdapter historyActivityAdapter;
	
	Cursor cursor=null;//用来存储查询的数据  
	SQLiteHelper helper=null;
	
	private void initViews() {
		historyvideo_listview = (ListView) findViewById(R.id.historyvideo_listview);
		tv_norecord = (TextView) findViewById(R.id.tv_norecord);
		history_back_btn = (TextView) findViewById(R.id.history_back_btn);
		clear_history_btn = (TextView) findViewById(R.id.clear_history_btn);
		
		helper = new SQLiteHelper(this);
		
		history_back_btn.setOnClickListener(this);
		clear_history_btn.setOnClickListener(this);
	}
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_history);
		initViews();
		
		setAdapt();
		
		historyvideo_listview.setOnItemClickListener(new MyOnItemClickListener());
	}
	



	/**
	 * 设置历史播放记录数据
	 */
	private void setAdapt() {
		videos = this.getDataFromDataBase();
		if(videos!=null && videos.size()>0){
			//有数据设置适配器
			historyActivityAdapter = new HistoryActivityAdapter(this, videos);
			historyvideo_listview.setAdapter(historyActivityAdapter);
			
			tv_norecord.setVisibility(View.GONE);
		}else{
			//没有数据文本显示
			tv_norecord.setVisibility(View.VISIBLE);
		}
	}
	
	/**
	 * 历史列表的点击事件
	 * @author zhangcan
	 *
	 */
	class MyOnItemClickListener implements OnItemClickListener{

		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position,
				long id) {
			List<VideoItem> clickitems = new ArrayList<VideoItem>();
			clickitems.add(videos.get(position));
			Intent intent = new Intent(HistoryActivity.this, MySystemDefinedVideoPlayer.class);
			Bundle bundle = new Bundle();
			bundle.putSerializable("videolist",
					(ArrayList<VideoItem>) clickitems);// 传递视频
			intent.putExtras(bundle);
			HistoryActivity.this.startActivity(intent);
			HistoryActivity.this.finish();
		}
		
	}

	/**
	 * 从数据库获取所有的数据
	 */
	private List<VideoItem> getDataFromDataBase() {
		List<VideoItem> videos = new ArrayList<VideoItem>();
		cursor = helper.getAll();
//		 _id, _VIDEONAME, _playedtimestring,_imgurl,_url
		while(cursor.moveToNext()){
			VideoItem item = new VideoItem();
			int _id = cursor.getInt(cursor.getColumnIndex("_id")); 
			item.setId(_id);
		    String _VIDEONAME = cursor.getString(cursor.getColumnIndex("_VIDEONAME")); 
		    item.setVideoName(_VIDEONAME);
		    String _playedtimestring = cursor.getString(cursor.getColumnIndex("_playedtimestring")); 
		    item.setSummary(_playedtimestring);
		    String _imgurl = cursor.getString(cursor.getColumnIndex("_imgurl")); 
		    item.setImgUrl(_imgurl);
		    String _url = cursor.getString(cursor.getColumnIndex("_url")); 
		    item.setData(_url);
		    videos.add(item);
		}
		
		return videos;
	}


	@Override
	public void onClick(View v) {
		if(v== history_back_btn){
			this.finish();
			
		}
		if(v == clear_history_btn){
			helper.deleteAll();
			videos.clear();
			historyActivityAdapter.notifyDataSetChanged();
			setAdapt();
		}
	}
	
}
