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
 * NativeVideoPageҳ���������
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
		//convertView���� ���ڽ�֮ǰ���غõĲ��ֽ��л��棬 �Ա�֮����Խ������ã��������
		if(null == convertView){
			convertView = View.inflate(context, R.layout.video_item_page, null);
			viewHolder = new ViewHolder();//�ؼ���ʵ�����Ž�viewHolder��
			viewHolder.video_icon = (ImageView) convertView.findViewById(R.id.video_icon);
			viewHolder.video_name = (TextView) convertView.findViewById(R.id.video_name);
			viewHolder.video_time = (TextView) convertView.findViewById(R.id.video_time);
			viewHolder.video_size = (TextView) convertView.findViewById(R.id.video_size);
			//��������
			convertView.setTag(viewHolder);
		}else{
			viewHolder = (ViewHolder) convertView.getTag();
		}
		
		//����positionλ�����õ���Ӧ�б������
		VideoItem videoItem = videoItems.get(position);
		viewHolder.video_name.setText(videoItem.getVideoName());
		viewHolder.video_time.setText(StringUtils.stringForTime((int)videoItem.getDuration()));
		viewHolder.video_size.setText(Formatter.formatFileSize(context, videoItem.getSize()));
		return convertView;
	}
	
	/**
	 * ���ڶԿؼ���ʵ�����л���
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
