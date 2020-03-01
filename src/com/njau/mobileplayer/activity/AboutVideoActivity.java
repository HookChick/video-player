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
 * 视频详情的界面
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
	 * 从网络视频页面传递过来的视频
	 */
	private VideoItem videoItem;

	/**
	 * 推荐的视频部分
	 */
	private List<VideoItem> recommendvideos = new ArrayList<VideoItem>();
	
	private SQLiteHelper helper;

	/**
	 * 初始化控件
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
		bindData();// 绑定数据

		// 绑定完数据就去联网获取资源
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
				VideoItem clickRecommendVideo = recommendvideos.get(position);// 用户点击的推荐视频
				Bundle bundle = new Bundle();
				bundle.putSerializable("videoitemclick", clickRecommendVideo);// 传递当前点击的视频
				intent.putExtras(bundle);
				AboutVideoActivity.this.startActivity(intent);
				AboutVideoActivity.this.finish();

				
			} else {
				// 没有网络
				Toast.makeText(AboutVideoActivity.this, "亲,您没有网络哦", Toast.LENGTH_LONG).show();
			}
		}

	}

	/**
	 * 对控件绑定数据
	 */
	private void bindData() {
		x.image().bind(detail_video_img, videoItem.getImgUrl());// 设置图片地址
		detail_video_name.setText(videoItem.getVideoName());// 设置名称
		float rating = videoItem.getRating();
		// 算法
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
			stringbuffer.append("未知");
		}
		detail_video_type.setText(stringbuffer.toString()); // 设置类型
		detail_video_duration.setText(StringUtils
				.stringForTime((int) (videoItem.getDuration()) * 1000));
		detail_video_title.setText(videoItem.getDescription());// 设置标题
		detail_video_gaiyao.setText(videoItem.getSummary());// 设置描述信息

	}

	/**
	 * 联网请求数据
	 */
	private void getDataFromNet() {
		// 传一个网络视频的URL
		RequestParams params = new RequestParams(Constant.NET_URI);
		// 联网请求是在子线程，但是回调的时候内部封装了，转化为主线程，而我们的解析数据非常快，在主线程即可
		x.http().get(params, new Callback.CommonCallback<String>() {

			@Override
			public void onCancelled(CancelledException arg0) {
				Log.e("onCancelled", "onCancelled");
			}

			@Override
			public void onError(Throwable arg0, boolean arg1) {
				Log.e("onError", "联网失败" + arg0.getMessage());
			}

			@Override
			public void onFinished() {
				Log.e("onFinished", "联网完成");
			}

			@Override
			public void onSuccess(String result) {
				// Log.e("onSuccess","联网成功");
				// 主线程
				// 再解析数据
				processDataFromNet(result);

			}

		});
	}

	/**
	 * 解析从服务端获的数据
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
			//文本隐藏
			norecommend_text.setVisibility(View.GONE);

		} else {
			// 没有网络，文本显示
			norecommend_text.setVisibility(View.VISIBLE);
		}
	}

	/**
	 * 解析JSON数据 用系统的接口解析
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
				String videoName = jsonObjectItem.optString("movieName");// 视频的名称
				videoItem.setVideoName(videoName);
				String imgUrl = jsonObjectItem.optString("coverImg");// 视频的图片地址
				videoItem.setImgUrl(imgUrl);
				String desc = jsonObjectItem.optString("videoTitle");// 视频的描述信息
				videoItem.setDescription(desc);
				String summary = jsonObjectItem.optString("summary"); // 视频概要
				videoItem.setSummary(summary);
				int videoLength = jsonObjectItem.optInt("videoLength"); // 视频时长
				videoItem.setDuration(videoLength);
				float rating = (float) jsonObjectItem.optDouble("rating");
				videoItem.setRating(rating);
				String videoUrl = jsonObjectItem.optString("hightUrl");// 高清视频的播放地址
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
			// 需要当前的视频过去
			List<VideoItem> clickitems = new ArrayList<VideoItem>();
			clickitems.add(videoItem);
			helper.insert(videoItem.getVideoName(),videoItem.getImgUrl(),videoItem.getData());
			Intent intent = new Intent(this, MySystemDefinedVideoPlayer.class);
			Bundle bundle = new Bundle();
			bundle.putSerializable("videolist",
					(ArrayList<VideoItem>) clickitems);// 传递视频
			intent.putExtras(bundle);
			this.startActivity(intent);
		}
	}

	/**
	 * 推荐视频gridView的适配器
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
			x.image().bind(imageview, item.getImgUrl());// 设置图片地址
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
	 * 计算推荐指数值的一个算法
	 * 
	 * @param rating
	 */
	private float getRating(float rating) {
		float realRating = 0;
		// [8.4, 7.5, 0.0, 7.3, 0.0, 6.6, 8.0, 7.2, 6.2, 8.0, 6.9, 7.0, 8.0,
		// 7.3, 8.0, 6.5, 6.5, 7.4, 6.6, 6.5, 7.3, 8.0,8.1, 5.9]
		// -5.0 则 0.9-3.4
		// 如果 rating<= 0 则2.5星评 否则 从3星开始计算 则 剩下2星 -
		// [0.9-3.4] 则算区间 0.5-3.5 区间长度为3 (X-0.5) *（2/3）
		// X就是 实际的 -5.0
		if (rating <= 0.0) {
			realRating = 2.5f;
		} else {
			float dis = (float) ((rating - 5.0) - 0.5);
			realRating = dis * (2.f / 3) + 3;
		}
		return realRating;
	}

	/**
	 * 算法
	 * 获取推荐视频的一个算法 
	 * 思路是优先找类型相似度较高的  大于等于 80%,如果没有或者数量不够就就找评分高的视频
	 * @param videoItems  ，联网获取的视频集合
	 * @return
	 */
	private List<VideoItem> getDataRecommend(List<VideoItem> videoItems) {
		List<VideoItem> recommendItems = new ArrayList<VideoItem>();

		// 键为当前的视频对象，
		// 值 为类型相似度
		Map<VideoItem, Float> xiangsimaps = new HashMap<VideoItem, Float>();

		// 为当前对象
		// 对象对应得评分
		List<VideoItem> maprating = new ArrayList<VideoItem>();
		List<String> types = videoItem.getType();
		for (VideoItem item : videoItems) {
			List<String> itemty = item.getType();
			int count = 0;
			for (String type : types) {// 遍历当前视频的类型的集合
				if (itemty.contains(type)) {
					count++;
				}
			}
			// 类型相似度
			float rate = (float) count / types.size();
			if (rate >= 0.8) { // 类型相似度大于等于80%
				xiangsimaps.put(item, rate);
			} else {
				maprating.add(item);
			}
		}

		// 把相似度大于80%的视频加进来
		for (VideoItem item : xiangsimaps.keySet()) {
			recommendItems.add(item);
		}
		// 如果没有达到数量，就查找评分最高的
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
