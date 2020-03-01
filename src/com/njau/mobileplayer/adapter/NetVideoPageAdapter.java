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
 * NetVideoPageAdapterҳ���������
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
		//convertView���� ���ڽ�֮ǰ���غõĲ��ֽ��л��棬 �Ա�֮����Խ������ã��������
		if(null == convertView){
			convertView = View.inflate(context, R.layout.netvideo_item_page, null);
			viewHolder = new ViewHolder();//�ؼ���ʵ�����Ž�viewHolder��
			viewHolder.netvideo_img = (ImageView) convertView.findViewById(R.id.netvideo_img);
			viewHolder.netvideo_name = (TextView) convertView.findViewById(R.id.netvideo_name);
			viewHolder.netvideo_desc = (TextView) convertView.findViewById(R.id.netvideo_desc);
			//��������
			convertView.setTag(viewHolder);
		}else{
			viewHolder = (ViewHolder) convertView.getTag();
		}
		
		//����positionλ�����õ���Ӧ�б������
		VideoItem videoItem = videoItems.get(position);
		x.image().bind(viewHolder.netvideo_img, videoItem.getImgUrl());//����ͼƬ��ַ
		viewHolder.netvideo_name.setText(videoItem.getVideoName());
		viewHolder.netvideo_desc.setText(videoItem.getDescription());
		return convertView;
	}
	
	/**
	 * ���ڶԿؼ���ʵ�����л���
	 * @author zhangcan
	 *
	 */
	static class ViewHolder{
		ImageView netvideo_img;
		TextView netvideo_name;
		TextView netvideo_desc;
	}
	
}
