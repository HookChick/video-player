package com.njau.mobileplayer.activity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.x;
import org.xutils.common.Callback;
import org.xutils.http.RequestParams;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.njau.mobileplayer.R;
import com.njau.mobileplayer.db.SQLiteHelper;
import com.njau.mobileplayer.entity.VideoItem;
import com.njau.mobileplayer.utils.Constant;
import com.njau.mobileplayer.utils.StringUtils;
import com.njau.mobileplayer.utils.Utils;

/**
 * ��Ƶ����Ľ���
 * @author zhangcan
 *
 */
public class AboutVideoActivity extends Activity implements OnClickListener {

	private ImageView detail_video_img;
	private TextView detail_video_name;
	private RatingBar zhishu_rating_bar;
	private TextView detail_video_type;
	private TextView detail_video_duration;
	private TextView detail_video_title;
	private TextView detail_video_gaiyao;
	private GridView tuijian_gridview;
	private TextView norecommend_text;

	/**
	 * ��������Ƶҳ�洫�ݹ�������Ƶ
	 */
	private VideoItem videoItem;

	/**
	 * �Ƽ�����Ƶ����
	 */
	private List<VideoItem> recommendvideos = new ArrayList<VideoItem>();
	
	private SQLiteHelper helper;

	/**
	 * ��ʼ���ؼ�
	 */
	private void initViews() {
		detail_video_img = (ImageView) findViewById(R.id.detail_video_img);
		detail_video_name = (TextView) findViewById(R.id.detail_video_name);
		zhishu_rating_bar = (RatingBar) findViewById(R.id.zhishu_rating_bar);
		detail_video_type = (TextView) findViewById(R.id.detail_video_type);
		detail_video_duration = (TextView) findViewById(R.id.detail_video_duration);
		detail_video_title = (TextView) findViewById(R.id.detail_video_title);
		detail_video_gaiyao = (TextView) findViewById(R.id.detail_video_gaiyao);
		tuijian_gridview = (GridView) findViewById(R.id.tuijian_gridview);
		norecommend_text = (TextView) findViewById(R.id.norecommend_text);
		
		helper = new SQLiteHelper(this);

		detail_video_img.setOnClickListener(this);
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.about_video_info);
		initViews();
		
		videoItem = (VideoItem) getIntent().getSerializableExtra(
				"videoitemclick");
		bindData();// ������

		// �������ݾ�ȥ������ȡ��Դ
		getDataFromNet();

		tuijian_gridview.setOnItemClickListener(new GridItemOnClick());

	}

	class GridItemOnClick implements AdapterView.OnItemClickListener {

		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position,
				long id) {
			if (Utils.hasNetwork(AboutVideoActivity.this)) {
				Intent intent = new Intent(AboutVideoActivity.this,
						AboutVideoActivity.class);
				VideoItem clickRecommendVideo = recommendvideos.get(position);// �û�������Ƽ���Ƶ
				Bundle bundle = new Bundle();
				bundle.putSerializable("videoitemclick", clickRecommendVideo);// ���ݵ�ǰ�������Ƶ
				intent.putExtras(bundle);
				AboutVideoActivity.this.startActivity(intent);
				AboutVideoActivity.this.finish();

				
			} else {
				// û������
				Toast.makeText(AboutVideoActivity.this, "��,��û������Ŷ", Toast.LENGTH_LONG).show();
			}
		}

	}

	/**
	 * �Կؼ�������
	 */
	private void bindData() {
		x.image().bind(detail_video_img, videoItem.getImgUrl());// ����ͼƬ��ַ
		detail_video_name.setText(videoItem.getVideoName());// ��������
		float rating = videoItem.getRating();
		// �㷨
		zhishu_rating_bar.setRating(this.getRating(rating));
		List<String> types = videoItem.getType();
		StringBuffer stringbuffer = new StringBuffer();
		if (null != types && types.size() > 0) {
			for (String str : types) {
				stringbuffer.append(str);
				stringbuffer.append("/");
			}
			stringbuffer.deleteCharAt(stringbuffer.length() - 1);

		} else {
			stringbuffer.append("δ֪");
		}
		detail_video_type.setText(stringbuffer.toString()); // ��������
		detail_video_duration.setText(StringUtils
				.stringForTime((int) (videoItem.getDuration()) * 1000));
		detail_video_title.setText(videoItem.getDescription());// ���ñ���
		detail_video_gaiyao.setText(videoItem.getSummary());// ����������Ϣ

	}

	/**
	 * ������������
	 */
	private void getDataFromNet() {
		// ��һ��������Ƶ��URL
		RequestParams params = new RequestParams(Constant.NET_URI);
		// ���������������̣߳����ǻص���ʱ���ڲ���װ�ˣ�ת��Ϊ���̣߳������ǵĽ������ݷǳ��죬�����̼߳���
		x.http().get(params, new Callback.CommonCallback<String>() {

			@Override
			public void onCancelled(CancelledException arg0) {
				Log.e("onCancelled", "onCancelled");
			}

			@Override
			public void onError(Throwable arg0, boolean arg1) {
				Log.e("onError", "����ʧ��" + arg0.getMessage());
			}

			@Override
			public void onFinished() {
				Log.e("onFinished", "�������");
			}

			@Override
			public void onSuccess(String result) {
				// Log.e("onSuccess","�����ɹ�");
				// ���߳�
				// �ٽ�������
				processDataFromNet(result);

			}

		});
	}

	/**
	 * �����ӷ���˻������
	 * 
	 * @param result
	 */
	private void processDataFromNet(String json) {

		List<VideoItem> videoItems = parseJSON(json);
		
		recommendvideos = this.getDataRecommend(videoItems);
		
		for(VideoItem item : recommendvideos){
			if(item.getVideoName().equals(videoItem.getVideoName())){
				recommendvideos.remove(videoItem);
			}
		}
		
		if (null != recommendvideos && recommendvideos.size() > 0) {

			ItemAdapter adapter = new ItemAdapter();
			tuijian_gridview.setAdapter(adapter);
			//�ı�����
			norecommend_text.setVisibility(View.GONE);

		} else {
			// û�����磬�ı���ʾ
			norecommend_text.setVisibility(View.VISIBLE);
		}
	}

	/**
	 * ����JSON���� ��ϵͳ�Ľӿڽ���
	 * 
	 * @param json
	 * @return
	 */
	private List<VideoItem> parseJSON(String json) {
		List<VideoItem> videos = new ArrayList<VideoItem>();

		try {
			JSONObject jsonObject = new JSONObject(json);
			JSONArray array = jsonObject.optJSONArray("trailers");
			for (int i = 0; i < array.length(); i++) {
				VideoItem videoItem = new VideoItem();
				JSONObject jsonObjectItem = (JSONObject) array.opt(i);
				String videoName = jsonObjectItem.optString("movieName");// ��Ƶ������
				videoItem.setVideoName(videoName);
				String imgUrl = jsonObjectItem.optString("coverImg");// ��Ƶ��ͼƬ��ַ
				videoItem.setImgUrl(imgUrl);
				String desc = jsonObjectItem.optString("videoTitle");// ��Ƶ��������Ϣ
				videoItem.setDescription(desc);
				String summary = jsonObjectItem.optString("summary"); // ��Ƶ��Ҫ
				videoItem.setSummary(summary);
				int videoLength = jsonObjectItem.optInt("videoLength"); // ��Ƶʱ��
				videoItem.setDuration(videoLength);
				float rating = (float) jsonObjectItem.optDouble("rating");
				videoItem.setRating(rating);
				String videoUrl = jsonObjectItem.optString("hightUrl");// ������Ƶ�Ĳ��ŵ�ַ
				videoItem.setData(videoUrl);

				JSONArray arrayType = (JSONArray) jsonObjectItem.get("type");
				List<String> types = new ArrayList<String>();
				for (int j = 0; j < arrayType.length(); j++) {
					String type = (String) arrayType.opt(j);
					types.add(type);
				}

				videoItem.setType(types);

				videos.add(videoItem);
			}
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return videos;
	}

	@Override
	public void onClick(View v) {
		if (v == detail_video_img) {
			// ��Ҫ��ǰ����Ƶ��ȥ
			List<VideoItem> clickitems = new ArrayList<VideoItem>();
			clickitems.add(videoItem);
			helper.insert(videoItem.getVideoName(),videoItem.getImgUrl(),videoItem.getData());
			Intent intent = new Intent(this, MySystemDefinedVideoPlayer.class);
			Bundle bundle = new Bundle();
			bundle.putSerializable("videolist",
					(ArrayList<VideoItem>) clickitems);// ������Ƶ
			intent.putExtras(bundle);
			this.startActivity(intent);
		}
	}

	/**
	 * �Ƽ���ƵgridView��������
	 * 
	 * @author zhangcan
	 * 
	 */
	public class ItemAdapter extends BaseAdapter {

		private LayoutInflater mInflater;

		public ItemAdapter() {
			this.mInflater = LayoutInflater.from(AboutVideoActivity.this);
		}

		public int getCount() {
			return recommendvideos.size();
		}

		public Object getItem(int paramInt) {
			return recommendvideos.get(paramInt);
		}

		public long getItemId(int paramInt) {
			return paramInt;
		}

		public View getView(int paramInt, View paramView,
				ViewGroup paramViewGroup) {
			View convertView = mInflater.inflate(
					R.layout.tuijain_gridview_item, null);
			ImageView imageview = (ImageView) convertView
					.findViewById(R.id.imageView_item);
			TextView recommend_vodeo_duration = (TextView) convertView
					.findViewById(R.id.recommend_vodeo_duration);
			TextView recommend_netvideo_name = (TextView) convertView
					.findViewById(R.id.recommend_netvideo_name);
			TextView recommend_netvide_rating = (TextView) convertView
					.findViewById(R.id.recommend_netvide_rating);
			VideoItem item = recommendvideos.get(paramInt);
			x.image().bind(imageview, item.getImgUrl());// ����ͼƬ��ַ
			int duration = (int) item.getDuration();
			recommend_vodeo_duration.setText(StringUtils
					.stringForTime((int) duration * 1000));
			recommend_netvideo_name.setText(item.getVideoName());
			recommend_netvide_rating.setText(item.getRating() <= 0 ? "5.5"
					: item.getRating() + "");

			return convertView;
		}

	}

	/**
	 * �����Ƽ�ָ��ֵ��һ���㷨
	 * 
	 * @param rating
	 */
	private float getRating(float rating) {
		float realRating = 0;
		// [8.4, 7.5, 0.0, 7.3, 0.0, 6.6, 8.0, 7.2, 6.2, 8.0, 6.9, 7.0, 8.0,
		// 7.3, 8.0, 6.5, 6.5, 7.4, 6.6, 6.5, 7.3, 8.0,8.1, 5.9]
		// -5.0 �� 0.9-3.4
		// ��� rating<= 0 ��2.5���� ���� ��3�ǿ�ʼ���� �� ʣ��2�� -
		// [0.9-3.4] �������� 0.5-3.5 ���䳤��Ϊ3 (X-0.5) *��2/3��
		// X���� ʵ�ʵ� -5.0
		if (rating <= 0.0) {
			realRating = 2.5f;
		} else {
			float dis = (float) ((rating - 5.0) - 0.5);
			realRating = dis * (2.f / 3) + 3;
		}
		return realRating;
	}

	/**
	 * �㷨
	 * ��ȡ�Ƽ���Ƶ��һ���㷨 
	 * ˼·���������������ƶȽϸߵ�  ���ڵ��� 80%,���û�л������������;������ָߵ���Ƶ
	 * @param videoItems  ��������ȡ����Ƶ����
	 * @return
	 */
	private List<VideoItem> getDataRecommend(List<VideoItem> videoItems) {
		List<VideoItem> recommendItems = new ArrayList<VideoItem>();

		// ��Ϊ��ǰ����Ƶ����
		// ֵ Ϊ�������ƶ�
		Map<VideoItem, Float> xiangsimaps = new HashMap<VideoItem, Float>();

		// Ϊ��ǰ����
		// �����Ӧ������
		List<VideoItem> maprating = new ArrayList<VideoItem>();
		List<String> types = videoItem.getType();
		for (VideoItem item : videoItems) {
			List<String> itemty = item.getType();
			int count = 0;
			for (String type : types) {// ������ǰ��Ƶ�����͵ļ���
				if (itemty.contains(type)) {
					count++;
				}
			}
			// �������ƶ�
			float rate = (float) count / types.size();
			if (rate >= 0.8) { // �������ƶȴ��ڵ���80%
				xiangsimaps.put(item, rate);
			} else {
				maprating.add(item);
			}
		}

		// �����ƶȴ���80%����Ƶ�ӽ���
		for (VideoItem item : xiangsimaps.keySet()) {
			recommendItems.add(item);
		}
		// ���û�дﵽ�������Ͳ���������ߵ�
		if (recommendItems.size() <= 6) {
			while (recommendItems.size() <= 6) {
				for (int i=0;i<maprating.size();i++) {
					boolean isMax = true;
					float ratItem = maprating.get(i).getRating();
					for(int j=0;j<maprating.size();j++){
						if(ratItem < maprating.get(j).getRating()){
							isMax = false;
							break;
						}
					}
					if(isMax){
						recommendItems.add(maprating.get(i));
						maprating.remove(i);
						break;
					}
				}

			}
		}

		return recommendItems;
	}

}
