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
 * NetVideoPageAdapter页面的适配器
 * @author zhangcan
 *
 */
public class NetVideoPageAdapter extends BaseAdapter{

	private Context context;
	private List<VideoItem> videoItems;
	
	public NetVideoPageAdapter(Context context, List<VideoItem> videoItems){
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
			convertView = View.inflate(context, R.layout.netvideo_item_page, null);
			viewHolder = new ViewHolder();//控件的实例都放进viewHolder里
			viewHolder.netvideo_img = (ImageView) convertView.findViewById(R.id.netvideo_img);
			viewHolder.netvideo_name = (TextView) convertView.findViewById(R.id.netvideo_name);
			viewHolder.netvideo_desc = (TextView) convertView.findViewById(R.id.netvideo_desc);
			//关联数据
			convertView.setTag(viewHolder);
		}else{
			viewHolder = (ViewHolder) convertView.getTag();
		}
		
		//根据position位置来得到对应列表的数据
		VideoItem videoItem = videoItems.get(position);
		x.image().bind(viewHolder.netvideo_img, videoItem.getImgUrl());//设置图片地址
		viewHolder.netvideo_name.setText(videoItem.getVideoName());
		viewHolder.netvideo_desc.setText(videoItem.getDescription());
		return convertView;
	}
	
	/**
	 * 用于对控件的实例进行缓存
	 * @author zhangcan
	 *
	 */
	static class ViewHolder{
		ImageView netvideo_img;
		TextView netvideo_name;
		TextView netvideo_desc;
	}
	
}
