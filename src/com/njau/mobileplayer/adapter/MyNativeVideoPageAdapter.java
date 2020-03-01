package com.njau.mobileplayer.adapter;

import java.util.List;

import android.content.Context;
import android.text.format.Formatter;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.njau.mobileplayer.R;
import com.njau.mobileplayer.entity.VideoItem;
import com.njau.mobileplayer.utils.StringUtils;

/**
 * NativeVideoPage页面的适配器
 * @author zhangcan
 *
 */
public class MyNativeVideoPageAdapter extends BaseAdapter{

	private Context context;
	private List<VideoItem> videoItems;
	
	public MyNativeVideoPageAdapter(Context context, List<VideoItem> videoItems){
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
			convertView = View.inflate(context, R.layout.video_item_page, null);
			viewHolder = new ViewHolder();//控件的实例都放进viewHolder里
			viewHolder.video_icon = (ImageView) convertView.findViewById(R.id.video_icon);
			viewHolder.video_name = (TextView) convertView.findViewById(R.id.video_name);
			viewHolder.video_time = (TextView) convertView.findViewById(R.id.video_time);
			viewHolder.video_size = (TextView) convertView.findViewById(R.id.video_size);
			//关联数据
			convertView.setTag(viewHolder);
		}else{
			viewHolder = (ViewHolder) convertView.getTag();
		}
		
		//根据position位置来得到对应列表的数据
		VideoItem videoItem = videoItems.get(position);
		viewHolder.video_name.setText(videoItem.getVideoName());
		viewHolder.video_time.setText(StringUtils.stringForTime((int)videoItem.getDuration()));
		viewHolder.video_size.setText(Formatter.formatFileSize(context, videoItem.getSize()));
		return convertView;
	}
	
	/**
	 * 用于对控件的实例进行缓存
	 * @author zhangcan
	 *
	 */
	static class ViewHolder{
		ImageView video_icon;
		TextView video_name;
		TextView video_time;
		TextView video_size;
	}
	
}
