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
 * NetVideoPageAdapter页面的适配器
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
		//convertView参数 用于将之前加载好的布局进行缓存， 以便之后可以进行重用，提高性能
		if(null == convertView){
			convertView = View.inflate(context, R.layout.vipvideo_item_page, null);
			viewHolder = new ViewHolder();//控件的实例都放进viewHolder里
			viewHolder.vipvideo_img = (ImageView) convertView.findViewById(R.id.vipvideo_img);
			viewHolder.vipvideo_name = (TextView) convertView.findViewById(R.id.vipvideo_name);
			viewHolder.vipvideo_desc = (TextView) convertView.findViewById(R.id.vipvideo_desc);
			viewHolder.vip_pay_needmoney = (TextView) convertView.findViewById(R.id.vip_pay_needmoney);
			//关联数据
			convertView.setTag(viewHolder);
		}else{
			viewHolder = (ViewHolder) convertView.getTag();
		}
		
		//根据position位置来得到对应列表的数据
		VipVideoItem videoItem = vipVideoItems.get(position);
		x.image().bind(viewHolder.vipvideo_img, videoItem.getDescimg());//设置图片地址
		viewHolder.vipvideo_name.setText(videoItem.getVideoname());
		viewHolder.vipvideo_desc.setText(videoItem.getVideodesc());
		viewHolder.vip_pay_needmoney.setText("￥"+videoItem.getNeedmoney());
		return convertView;
		
	}
	
	/**
	 * 用于对控件的实例进行缓存
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
