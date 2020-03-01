package com.njau.mobileplayer.adapter;

import java.util.List;

import org.xutils.x;

import com.njau.mobileplayer.R;
import com.njau.mobileplayer.adapter.NetVideoPageAdapter.ViewHolder;
import com.njau.mobileplayer.entity.VideoItem;
import com.njau.mobileplayer.entity.RecommendData.ListEntity.TopCommentsEntity;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * �Ƽ�ģ�������б��������
 * @author zhangcan
 *
 */
public class TopCommentAdapter extends BaseAdapter {

	private Context context;
	//���۵�����
	private List<TopCommentsEntity> commentsEntities;

	public TopCommentAdapter(Context context, List<TopCommentsEntity> commentsEntities){
		this.context = context;
		this.commentsEntities = commentsEntities;
	}
	
	@Override
	public int getCount() {
		return commentsEntities.size();
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
			convertView = View.inflate(context, R.layout.comment_item, null);
			viewHolder = new ViewHolder();//�ؼ���ʵ�����Ž�viewHolder��
			viewHolder.comment_headpic = (ImageView) convertView.findViewById(R.id.comment_headpic);
			viewHolder.comment_name = (TextView) convertView.findViewById(R.id.comment_name);
			viewHolder.comment_content = (TextView) convertView.findViewById(R.id.comment_content);
			//��������
			convertView.setTag(viewHolder);
		}else{
			viewHolder = (ViewHolder) convertView.getTag();
		}
		
		//����positionλ�����õ���Ӧ�б������
		TopCommentsEntity commentsEntity = commentsEntities.get(position);
		x.image().bind(viewHolder.comment_headpic, commentsEntity.getU().getHeader().get(0));//����ͼƬ��ַ
		viewHolder.comment_name.setText(commentsEntity.getU().getName() +":");
		viewHolder.comment_content.setText(commentsEntity.getContent());
		return convertView;
	}
	
	/**
	 * ���ڶԿؼ���ʵ�����л���
	 * @author zhangcan
	 *
	 */
	static class ViewHolder{
		ImageView comment_headpic;//������ͷ��
		TextView comment_name;//����������
		TextView comment_content;//��������
	}

}
