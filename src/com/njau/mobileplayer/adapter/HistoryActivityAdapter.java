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
 * ��ʷ����ҳ���������
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
		//convertView���� ���ڽ�֮ǰ���غõĲ��ֽ��л��棬 �Ա�֮����Խ������ã��������
		if(null == convertView){
			convertView = View.inflate(context, R.layout.historyvideo_item_page, null);
			viewHolder = new ViewHolder();//�ؼ���ʵ�����Ž�viewHolder��
			viewHolder.historyvideo_img = (ImageView) convertView.findViewById(R.id.historyvideo_img);
			viewHolder.historyvideo_name = (TextView) convertView.findViewById(R.id.historyvideo_name);
			viewHolder.historyvideo_time = (TextView) convertView.findViewById(R.id.historyvideo_time);
			//��������
			convertView.setTag(viewHolder);
		}else{
			viewHolder = (ViewHolder) convertView.getTag();
		}
		
		//����positionλ�����õ���Ӧ�б������
		VideoItem videoItem = videoItems.get(position);
		x.image().bind(viewHolder.historyvideo_img, videoItem.getImgUrl());//����ͼƬ��ַ
		viewHolder.historyvideo_name.setText(videoItem.getVideoName());
		viewHolder.historyvideo_time.setText(videoItem.getSummary());
		
		return convertView;
	}
	
	/**
	 * ���ڶԿؼ���ʵ�����л���
	 * @author zhangcan
	 *
	 */
	static class ViewHolder{
		ImageView historyvideo_img;
		TextView historyvideo_name;
		TextView historyvideo_time;
	}
	
}
