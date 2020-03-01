package com.njau.mobileplayer.adapter;

import java.util.List;

import org.xutils.x;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.njau.mobileplayer.R;
import com.njau.mobileplayer.entity.VideoItem;

/**
 * 历史播放页面的适配器
 * @author zhangcan
 *
 */
public class HistoryActivityAdapter extends BaseAdapter{

	private Context context;
	private List<VideoItem> videoItems;
	
	public HistoryActivityAdapter(Context context, List<VideoItem> videoItems){
		this.context = context;
		this.videoItems = videoItems;
	}
	@Override
	public int getCount() {
		return videoItems.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder viewHolder;
		//convertView参数 用于将之前加载好的布局进行缓存， 以便之后可以进行重用，提高性能
		if(null == convertView){
			convertView = View.inflate(context, R.layout.historyvideo_item_page, null);
			viewHolder = new ViewHolder();//控件的实例都放进viewHolder里
			viewHolder.historyvideo_img = (ImageView) convertView.findViewById(R.id.historyvideo_img);
			viewHolder.historyvideo_name = (TextView) convertView.findViewById(R.id.historyvideo_name);
			viewHolder.historyvideo_time = (TextView) convertView.findViewById(R.id.historyvideo_time);
			//关联数据
			convertView.setTag(viewHolder);
		}else{
			viewHolder = (ViewHolder) convertView.getTag();
		}
		
		//根据position位置来得到对应列表的数据
		VideoItem videoItem = videoItems.get(position);
		x.image().bind(viewHolder.historyvideo_img, videoItem.getImgUrl());//设置图片地址
		viewHolder.historyvideo_name.setText(videoItem.getVideoName());
		viewHolder.historyvideo_time.setText(videoItem.getSummary());
		
		return convertView;
	}
	
	/**
	 * 用于对控件的实例进行缓存
	 * @author zhangcan
	 *
	 */
	static class ViewHolder{
		ImageView historyvideo_img;
		TextView historyvideo_name;
		TextView historyvideo_time;
	}
	
}
