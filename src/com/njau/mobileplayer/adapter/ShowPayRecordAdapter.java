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
import com.njau.mobileplayer.entity.PayItem;
import com.njau.mobileplayer.entity.SearchBean.ItemData;

/**
 * SearchPageAdapterҳ���������
 * @author zhangcan
 *
 */
public class ShowPayRecordAdapter extends BaseAdapter{

	private Context context;
	private List<PayItem> payItems;
	
	public ShowPayRecordAdapter(Context context, List<PayItem> payItems){
		this.context = context;
		this.payItems = payItems;
	}
	@Override
	public int getCount() {
		return payItems.size();
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
			convertView = View.inflate(context, R.layout.pay_record_item, null);
			viewHolder = new ViewHolder();//�ؼ���ʵ�����Ž�viewHolder��
			viewHolder.pay_record_name = (TextView) convertView.findViewById(R.id.pay_record_name);
			viewHolder.pay_record_desc = (TextView) convertView.findViewById(R.id.pay_record_desc);
			viewHolder.pay_record_money = (TextView) convertView.findViewById(R.id.pay_record_money);
			viewHolder.pay_record_time = (TextView) convertView.findViewById(R.id.pay_record_time);
			//��������
			convertView.setTag(viewHolder);
		}else{
			viewHolder = (ViewHolder) convertView.getTag();
		}
		
		//����positionλ�����õ���Ӧ�б������
		PayItem payItem = payItems.get(position);
		viewHolder.pay_record_name.setText(payItem.getVideo().getVideoname());
		viewHolder.pay_record_desc.setText(payItem.getVideo().getVideodesc());
		viewHolder.pay_record_money.setText("��"+payItem.getVideo().getNeedmoney());
		viewHolder.pay_record_time.setText(payItem.getPaytime()+"");
		return convertView;
	}
	
	/**
	 * ���ڶԿؼ���ʵ�����л���
	 * @author zhangcan
	 *
	 */
	static class ViewHolder{
		TextView pay_record_name;
		TextView pay_record_desc;
		TextView pay_record_money;
		TextView pay_record_time;
	}
	
}
