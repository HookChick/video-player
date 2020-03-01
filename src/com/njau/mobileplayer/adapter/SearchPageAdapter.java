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
import com.njau.mobileplayer.entity.SearchBean.ItemData;

/**
 * SearchPageAdapterҳ���������
 * @author zhangcan
 *
 */
public class SearchPageAdapter extends BaseAdapter{

	private Context context;
	private List<ItemData> items;
	
	public SearchPageAdapter(Context context, List<ItemData> items){
		this.context = context;
		this.items = items;
	}
	@Override
	public int getCount() {
		return items.size();
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
		ItemData itemData = items.get(position);
		x.image().bind(viewHolder.netvideo_img, itemData.getItemImage().getImgUrl1());//����ͼƬ��ַ
		viewHolder.netvideo_name.setText(itemData.getItemTitle());
		viewHolder.netvideo_desc.setText(itemData.getKeywords());
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
