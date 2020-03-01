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
import com.njau.mobileplayer.entity.VipVideoItem;

/**
 * NetVideoPageAdapterҳ���������
 * @author zhangcan
 *
 */
public class VipVideoPageAdapter extends BaseAdapter{

	private Context context;
	private List<VipVideoItem> vipVideoItems;
	
	public VipVideoPageAdapter(Context context, List<VipVideoItem> vipVideoItems){
		this.context = context;
		this.vipVideoItems = vipVideoItems;
	}
	@Override
	public int getCount() {
		return vipVideoItems.size();
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
			convertView = View.inflate(context, R.layout.vipvideo_item_page, null);
			viewHolder = new ViewHolder();//�ؼ���ʵ�����Ž�viewHolder��
			viewHolder.vipvideo_img = (ImageView) convertView.findViewById(R.id.vipvideo_img);
			viewHolder.vipvideo_name = (TextView) convertView.findViewById(R.id.vipvideo_name);
			viewHolder.vipvideo_desc = (TextView) convertView.findViewById(R.id.vipvideo_desc);
			viewHolder.vip_pay_needmoney = (TextView) convertView.findViewById(R.id.vip_pay_needmoney);
			//��������
			convertView.setTag(viewHolder);
		}else{
			viewHolder = (ViewHolder) convertView.getTag();
		}
		
		//����positionλ�����õ���Ӧ�б������
		VipVideoItem videoItem = vipVideoItems.get(position);
		x.image().bind(viewHolder.vipvideo_img, videoItem.getDescimg());//����ͼƬ��ַ
		viewHolder.vipvideo_name.setText(videoItem.getVideoname());
		viewHolder.vipvideo_desc.setText(videoItem.getVideodesc());
		viewHolder.vip_pay_needmoney.setText("��"+videoItem.getNeedmoney());
		return convertView;
		
	}
	
	/**
	 * ���ڶԿؼ���ʵ�����л���
	 * @author zhangcan
	 *
	 */
	static class ViewHolder{
		ImageView vipvideo_img;
		TextView vipvideo_name;
		TextView vipvideo_desc;
		
		TextView vip_pay_needmoney;
	}
	
}
