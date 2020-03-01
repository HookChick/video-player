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
 * 推荐模块评论列表的适配器
 * @author zhangcan
 *
 */
public class TopCommentAdapter extends BaseAdapter {

	private Context context;
	//评论的数据
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
		//convertView参数 用于将之前加载好的布局进行缓存， 以便之后可以进行重用，提高性能
		if(null == convertView){
			convertView = View.inflate(context, R.layout.comment_item, null);
			viewHolder = new ViewHolder();//控件的实例都放进viewHolder里
			viewHolder.comment_headpic = (ImageView) convertView.findViewById(R.id.comment_headpic);
			viewHolder.comment_name = (TextView) convertView.findViewById(R.id.comment_name);
			viewHolder.comment_content = (TextView) convertView.findViewById(R.id.comment_content);
			//关联数据
			convertView.setTag(viewHolder);
		}else{
			viewHolder = (ViewHolder) convertView.getTag();
		}
		
		//根据position位置来得到对应列表的数据
		TopCommentsEntity commentsEntity = commentsEntities.get(position);
		x.image().bind(viewHolder.comment_headpic, commentsEntity.getU().getHeader().get(0));//设置图片地址
		viewHolder.comment_name.setText(commentsEntity.getU().getName() +":");
		viewHolder.comment_content.setText(commentsEntity.getContent());
		return convertView;
	}
	
	/**
	 * 用于对控件的实例进行缓存
	 * @author zhangcan
	 *
	 */
	static class ViewHolder{
		ImageView comment_headpic;//评论者头像
		TextView comment_name;//评论者名字
		TextView comment_content;//评论内容
	}

}
