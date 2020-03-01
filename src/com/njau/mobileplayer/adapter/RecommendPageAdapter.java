package com.njau.mobileplayer.adapter;

import java.util.List;

import org.xutils.x;
import org.xutils.common.util.DensityUtil;

import pl.droidsonroids.gif.GifImageView;

import android.content.Context;
import android.net.Uri;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.VideoView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.njau.mobileplayer.R;
import com.njau.mobileplayer.entity.RecommendData.ListEntity;
import com.njau.mobileplayer.entity.RecommendData.ListEntity.TopCommentsEntity;
import com.njau.mobileplayer.utils.StringUtils;
import com.njau.mobileplayer.utils.Utils;

/**
 * RecommendPageAdapterҳ���������
 * 
 * @author zhangcan
 * 
 */
public class RecommendPageAdapter extends BaseAdapter {

	private Context context;
	private List<ListEntity> datas;

	/**
	 * ��Ƶ
	 */
	private static final int TYPE_VIDEO = 0;

	/**
	 * ͼƬ
	 */
	private static final int TYPE_IMAGE = 1;

	/**
	 * ����
	 */
	private static final int TYPE_TEXT = 2;

	/**
	 * GIFͼƬ
	 */
	private static final int TYPE_GIF = 3;

	/**
	 * ����ƹ�
	 */
	private static final int TYPE_AD = 4;

	private TopCommentAdapter commentAdapter;

	public RecommendPageAdapter(Context context, List<ListEntity> datas) {
		this.context = context;
		this.datas = datas;
	}

	@Override
	public int getCount() {
		return datas.size();
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

	/**
	 * �õ����͵�����
	 */
	@Override
	public int getViewTypeCount() {
		return 5;
	}

	/**
	 * ����λ�õõ���Ӧ������
	 */
	@Override
	public int getItemViewType(int position) {
		ListEntity listEntity = datas.get(position);
		int entityType = -1;
		String type = listEntity.getType();// �õ� image,video,text,gif,ad
		if ("image".equals(type)) {
			entityType = TYPE_IMAGE;
		} else if ("video".equals(type)) {
			entityType = TYPE_VIDEO;
		} else if ("text".equals(type)) {
			entityType = TYPE_TEXT;
		} else if ("gif".equals(type)) {
			entityType = TYPE_GIF;
		} else {
			entityType = TYPE_AD;
		}

		return entityType;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder viewHolder;
		int entityType = getItemViewType(position);
		// convertView���� ���ڽ�֮ǰ���غõĲ��ֽ��л��棬 �Ա�֮����Խ������ã��������
		if (null == convertView) {// ��ʼ��
			viewHolder = new ViewHolder();// �ؼ���ʵ�����Ž�viewHolder��

			// ʵ�������еĿؼ�
			convertView = initView(convertView, entityType, viewHolder);

			// ʵ������������
			initCommonView(convertView, entityType, viewHolder);
			// ��������
			convertView.setTag(viewHolder);
		} else {// ��ȡTag
			viewHolder = (ViewHolder) convertView.getTag();
		}

		// //����positionλ�����õ���Ӧ�б������
		ListEntity listEntity = datas.get(position);
		// ������
		bindData(entityType, viewHolder, listEntity);

		return convertView;
	}

	/**
	 * ������
	 * 
	 * @param entityType
	 * @param viewHolder
	 * @param listEntity
	 */
	private void bindData(int entityType, ViewHolder viewHolder,
			ListEntity listEntity) {
		switch (entityType) {
		case TYPE_VIDEO:// ��Ƶ
			bindCommonData(viewHolder, listEntity);
			// ��һ����������Ƶ���ŵ�ַ���ڶ�����������ʾ����ĵ�ַ�����������Ǳ���
			// Log.e("jcv_videoplayer.setVideoURI",
			// listEntity.getVideo().getVideo().get(0));
			viewHolder.jcv_videoplayer.setVideoURI(Uri.parse(listEntity
					.getVideo().getVideo().get(0)));
			viewHolder.tv_play_nums.setText(listEntity.getVideo()
					.getPlaycount() + "�β���");
			viewHolder.tv_video_duration.setText(StringUtils
					.stringForTime(listEntity.getVideo().getDuration() * 1000)
					+ "");

			break;
		case TYPE_IMAGE:// ͼƬ
			bindCommonData(viewHolder, listEntity);
			viewHolder.iv_image_icon.setImageResource(R.drawable.bg_item);
			int height = listEntity.getImage().getHeight() <= DensityUtil
					.getScreenHeight() * 0.75 ? listEntity.getImage()
					.getHeight() : (int) (DensityUtil.getScreenHeight() * 0.75);

			LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
					DensityUtil.getScreenWidth(), height);
			viewHolder.iv_image_icon.setLayoutParams(params);
			if (listEntity.getImage() != null
					&& listEntity.getImage().getBig() != null
					&& listEntity.getImage().getBig().size() > 0) {
//				x.image().bind(viewHolder.iv_image_icon, listEntity.getImage().getBig().get(0));
				Glide.with(context).load(listEntity.getImage().getBig().get(0))
						.placeholder(R.drawable.bg_item)
						.error(R.drawable.bg_item)
						.diskCacheStrategy(DiskCacheStrategy.ALL)
						.into(viewHolder.iv_image_icon);
			}
			break;
		case TYPE_TEXT:// ����
			bindCommonData(viewHolder, listEntity);
			break;
		case TYPE_GIF:// gif
			bindCommonData(viewHolder, listEntity);
			System.out.println("listEntity.getGif().getImages().get(0)"
					+ listEntity.getGif().getImages().get(0));
			// x.image().bind(viewHolder.iv_image_gif,
			// listEntity.getGif().getImages().get(0));
			Glide.with(context).load(listEntity.getGif().getImages().get(0))
					.diskCacheStrategy(DiskCacheStrategy.SOURCE)
					.into(viewHolder.iv_image_gif);
			break;
		case TYPE_AD:// ������
			break;
		}

		// �����ı�
		viewHolder.tv_context.setText(listEntity.getText());
	}

	/**
	 * ��ʼ����������ͼ
	 * 
	 * @param convertView
	 * @param entityType
	 * @param viewHolder
	 */
	private void initCommonView(View convertView, int entityType,
			ViewHolder viewHolder) {
		// ʵ������������
		switch (entityType) {
		case TYPE_VIDEO:// ��Ƶ
		case TYPE_IMAGE:// ͼƬ
		case TYPE_TEXT:// ����
		case TYPE_GIF:// gif
			// ���س�����沿�ֵĹ���������ͼ
			viewHolder.iv_headpic = (ImageView) convertView
					.findViewById(R.id.iv_headpic);
			viewHolder.tv_name = (TextView) convertView
					.findViewById(R.id.tv_name);
			viewHolder.tv_time_refresh = (TextView) convertView
					.findViewById(R.id.tv_time_refresh);
			viewHolder.iv_right_more = (ImageView) convertView
					.findViewById(R.id.iv_right_more);
			// bottom
			viewHolder.iv_video_kind = (ImageView) convertView
					.findViewById(R.id.iv_video_kind);
			viewHolder.tv_video_kind_text = (TextView) convertView
					.findViewById(R.id.tv_video_kind_text);
			viewHolder.tv_shenhe_ding_number = (TextView) convertView
					.findViewById(R.id.tv_shenhe_ding_number);
			viewHolder.tv_shenhe_cai_number = (TextView) convertView
					.findViewById(R.id.tv_shenhe_cai_number);
			viewHolder.tv_posts_number = (TextView) convertView
					.findViewById(R.id.tv_posts_number);
			viewHolder.ll_download = (LinearLayout) convertView
					.findViewById(R.id.ll_download);

			viewHolder.comment_listview = (ListView) convertView
					.findViewById(R.id.comment_listview);
			break;
		}

		// �м乫������ -���еĶ���
		viewHolder.tv_context = (TextView) convertView
				.findViewById(R.id.tv_context);

	}

	/**
	 * ������ʵ�������еĿռ�
	 * 
	 * @param convertView
	 * @param entityType
	 * @param viewHolder
	 * @return
	 */
	private View initView(View convertView, int entityType,
			ViewHolder viewHolder) {
		switch (entityType) {
		case TYPE_VIDEO:// ��Ƶ
			convertView = View.inflate(context, R.layout.all_video_item, null);
			//
			viewHolder.tv_play_nums = (TextView) convertView
					.findViewById(R.id.tv_play_nums);
			viewHolder.tv_video_duration = (TextView) convertView
					.findViewById(R.id.tv_video_duration);
			viewHolder.jcv_videoplayer = (VideoView) convertView
					.findViewById(R.id.jcv_videoplayer);
			break;
		case TYPE_IMAGE:// ͼƬ
			convertView = View.inflate(context, R.layout.all_image_item, null);
			viewHolder.iv_image_icon = (ImageView) convertView
					.findViewById(R.id.iv_image_icon);
			break;
		case TYPE_TEXT:// �ı�
			convertView = View.inflate(context, R.layout.all_text_item, null);

			break;
		case TYPE_GIF:// GIF����
			convertView = View.inflate(context, R.layout.all_gif_item, null);
			viewHolder.iv_image_gif = (GifImageView) convertView
					.findViewById(R.id.iv_image_gif);
			break;
		case TYPE_AD:// ����ƹ���
			convertView = View.inflate(context, R.layout.all_ad_item, null);
			viewHolder.btn_install = (Button) convertView
					.findViewById(R.id.btn_install);
			viewHolder.iv_image_icon = (ImageView) convertView
					.findViewById(R.id.iv_image_icon);
			break;
		}

		return convertView;
	}

	/**
	 * �󶨹�����������
	 * 
	 * @param viewHolder
	 * @param mediaItem
	 */
	private void bindCommonData(ViewHolder viewHolder, ListEntity mediaItem) {
		if (mediaItem.getU() != null && mediaItem.getU().getHeader() != null
				&& mediaItem.getU().getHeader().get(0) != null) {
			x.image().bind(viewHolder.iv_headpic,
					mediaItem.getU().getHeader().get(0));
		}
		if (mediaItem.getU() != null && mediaItem.getU().getName() != null) {
			viewHolder.tv_name.setText(mediaItem.getU().getName() + "");
		}

		viewHolder.tv_time_refresh.setText(mediaItem.getPasstime());

		// ���ñ�ǩ
		List<ListEntity.TagsEntity> tagsEntities = mediaItem.getTags();
		if (tagsEntities != null && tagsEntities.size() > 0) {
			StringBuffer buffer = new StringBuffer();
			for (int i = 0; i < tagsEntities.size(); i++) {
				buffer.append(tagsEntities.get(i).getName() + " ");
			}
			viewHolder.tv_video_kind_text.setText(buffer.toString());
		}

		// ���õ��ޣ���,ת��
		viewHolder.tv_shenhe_ding_number.setText(mediaItem.getUp());
		viewHolder.tv_shenhe_cai_number.setText(mediaItem.getDown() + "");
		viewHolder.tv_posts_number.setText(mediaItem.getForward() + "");

		// ���������б�����
		List<TopCommentsEntity> commentsEntities = mediaItem.getTop_comments();
		if (commentsEntities != null && commentsEntities.size() > 0) {
			this.commentAdapter = new TopCommentAdapter(context,
					commentsEntities);
			viewHolder.comment_listview.setAdapter(commentAdapter);

			// ������ListView��Adapter�󣬸���ListView������Ŀ���¼���ListView�ĸ߶�
			Utils.setListViewHeightBasedOnChildren(viewHolder.comment_listview);
		} else {// û������
			viewHolder.comment_listview.setVisibility(View.GONE);

		}
	}

	/**
	 * ���ڶԿؼ���ʵ�����л���
	 * 
	 * @author zhangcan
	 * 
	 */
	static class ViewHolder {
		// user_info
		ImageView iv_headpic;
		TextView tv_name;
		TextView tv_time_refresh;
		ImageView iv_right_more;
		// bottom
		ImageView iv_video_kind;
		TextView tv_video_kind_text;
		TextView tv_shenhe_ding_number;
		TextView tv_shenhe_cai_number;
		TextView tv_posts_number;
		LinearLayout ll_download;

		// �м乫������ -���еĶ���
		TextView tv_context;

		// Video
		// TextView tv_context;
		TextView tv_play_nums;
		TextView tv_video_duration;

		/* JCVideoPlayer jcv_videoplayer; */
		VideoView jcv_videoplayer;

		// Image
		ImageView iv_image_icon;
		// TextView tv_context;

		// Text
		// TextView tv_context;

		// Gif------->��Դ Android-gif-drawable
		GifImageView iv_image_gif;
		// TextView tv_context;

		// ����ƹ�
		Button btn_install;
		// TextView iv_image_icon;
		// TextView tv_context;

		ListView comment_listview;

	}

}
