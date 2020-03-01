package com.njau.mobileplayer.activity;

import io.vov.vitamio.utils.Log;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.x;
import org.xutils.common.Callback;
import org.xutils.http.RequestParams;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

import com.iflytek.cloud.ErrorCode;
import com.iflytek.cloud.InitListener;
import com.iflytek.cloud.RecognizerResult;
import com.iflytek.cloud.SpeechConstant;
import com.iflytek.cloud.SpeechError;
import com.iflytek.cloud.ui.RecognizerDialog;
import com.iflytek.cloud.ui.RecognizerDialogListener;
import com.njau.mobileplayer.R;
import com.njau.mobileplayer.adapter.SearchPageAdapter;
import com.njau.mobileplayer.entity.SearchBean;
import com.njau.mobileplayer.entity.VideoItem;
import com.njau.mobileplayer.entity.SearchBean.ItemData;
import com.njau.mobileplayer.entity.SearchBean.ItemData.ItemImageEntity;
import com.njau.mobileplayer.utils.Constant;
import com.njau.mobileplayer.utils.JsonParser;

/**
 * 搜索界面的activity
 * 
 * @author zhangcan
 * 
 */
public class SearchActivity extends Activity implements OnClickListener {

	private EditText etSearch;
	private ImageView voiceSearch;
	private TextView videoSearch;
	private ListView searchListview;
	private ProgressBar progressbar;
	private TextView searchNodata;

	/**
	 * 用HashMap存储听写结果
	 */
	private HashMap<String, String> mIatResults;

	/**
	 * 网络请求的路径
	 */
	private String url;

	private List<ItemData> items;

	private SearchPageAdapter searchPageAdapter;

	/**
	 * 实例化
	 */
	private void findViews() {
		etSearch = (EditText) findViewById(R.id.et_search);
		voiceSearch = (ImageView) findViewById(R.id.voice_search);
		videoSearch = (TextView) findViewById(R.id.video_search);
		searchListview = (ListView) findViewById(R.id.search_listview);
		progressbar = (ProgressBar) findViewById(R.id.progressbar);
		searchNodata = (TextView) findViewById(R.id.search_nodata);

		mIatResults = new LinkedHashMap<String, String>();

		// 设置点击事件
		voiceSearch.setOnClickListener(this);
		videoSearch.setOnClickListener(this);
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_search);
		this.findViews();
		
//		searchListview.setOnItemClickListener(new MyOnItemClickListener());
	}
	
	/**
	 * 处理listView的点击事件
	 * 
	 * @author zhangcan
	 * 
	 */
	class MyOnItemClickListener implements OnItemClickListener {

		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position,
				long id) {

			// 需要传递视频列表过去，便于在播放界面播放上一个下一个视频
			Intent intent = new Intent(SearchActivity.this,
					MySystemDefinedVideoPlayer.class);
			Bundle bundle = new Bundle();
			bundle.putSerializable("videolist",
					(ArrayList<ItemData>) items);// 传递视频播放列表
			intent.putExtras(bundle);
			intent.putExtra("position", position);
			startActivity(intent);

		}
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.voice_search:
			// 语音输入
			// 1.创建RecognizerDialog对象
			RecognizerDialog mDialog = new RecognizerDialog(this,
					new MyInitListener());
			// 2.设置accent、 language等参数
			mDialog.setParameter(SpeechConstant.LANGUAGE, "zh_cn");// 语言中文
			mDialog.setParameter(SpeechConstant.ACCENT, "mandarin");// 普通话
			// 若要将UI控件用于语义理解，必须添加以下参数设置，设置之后onResult回调返回将是语义理解
			// 结果
			// mDialog.setParameter("asr_sch", "1");
			// mDialog.setParameter("nlp_version", "2.0");
			// 3.设置回调接口
			mDialog.setListener(new MyRecognizerDialogListener());
			// 4.显示dialog，接收语音输入
			mDialog.show();
			break;

		case R.id.video_search:
			// 点击搜索按钮执行搜索-->发送请求到服务器请求数据
			String content = etSearch.getText().toString().trim();
			if (null != content && !"".equals(content)) {
				if(items != null && items.size()>0){
					items.clear(); //第二次请求时需要把之前的数据清除掉
				}
				try {
					// 得到数据前设置可见
					progressbar.setVisibility(View.VISIBLE);

					content = URLEncoder.encode(content, "UTF-8");
					url = Constant.SEARCH_URL + content;
					// 利用xUtils从网络获取数据
					RequestParams params = new RequestParams(url);
					x.http().get(params, new MyCommonCallback());
					
					etSearch.setText("");

				} catch (UnsupportedEncodingException e) {
					e.printStackTrace();
				}
			} else {
				Toast.makeText(this, "请输入内容.", Toast.LENGTH_SHORT).show();
			}
			break;
		}
	}

	/**
	 * xUtils获取数据时的一个回调
	 * 
	 * @author zhangcan
	 * 
	 */
	class MyCommonCallback implements Callback.CommonCallback<String> {

		@Override
		public void onCancelled(CancelledException arg0) {
			progressbar.setVisibility(View.GONE);
		}

		@Override
		public void onError(Throwable arg0, boolean arg1) {
			progressbar.setVisibility(View.GONE);
		}

		@Override
		public void onFinished() {
			progressbar.setVisibility(View.GONE);
		}

		@Override
		public void onSuccess(String result) {
			processData(result);// 解析数据
		}

	}

	/**
	 * 集成科大讯飞说话的一个监听
	 * 
	 * @author zhangcan
	 * 
	 */
	class MyRecognizerDialogListener implements RecognizerDialogListener {

		/**
		 * 出错了
		 */
		@Override
		public void onError(SpeechError speechError) {
			Log.e("SearchActivity", "onError ==" + speechError.getMessage());
		}

		/**
		 * @param recognizerResult
		 * @param arg1
		 *            是否说话结束
		 */
		@Override
		public void onResult(RecognizerResult results, boolean arg1) {
			String result = results.getResultString();

			Log.e("searchActivity", "results=" + results);
			String text = JsonParser.parseIatResult(result);

			String sn = null;
			// 读取json结果中的sn字段
			try {
				JSONObject resultJson = new JSONObject(
						results.getResultString());
				sn = resultJson.optString("sn");
			} catch (JSONException e) {
				e.printStackTrace();
			}

			mIatResults.put(sn, text);

			StringBuffer resultBuffer = new StringBuffer();// 拼接字符串
			for (String key : mIatResults.keySet()) {
				resultBuffer.append(mIatResults.get(key));
			}

			etSearch.setText(resultBuffer.toString());
			etSearch.setSelection(etSearch.length());

		}

	}

	/**
	 * 初始化的一个监听
	 * 
	 * @author zhangcan
	 * 
	 */
	class MyInitListener implements InitListener {

		@Override
		public void onInit(int arg0) {
			if (arg0 != ErrorCode.SUCCESS) {
				Toast.makeText(SearchActivity.this, "初始化失败", Toast.LENGTH_SHORT)
						.show();
			}
		}

	}

	/**
	 * 解析请求的数据
	 * 
	 * @param result
	 */
	public void processData(String json) {
		SearchBean searchBean = parsedJson(json);
		if (null != searchBean) {
			items = searchBean.getItems();

			if(null != items && items.size()>0){
				searchPageAdapter = new SearchPageAdapter(this, items);
				// 设置适配器
				searchListview.setAdapter(searchPageAdapter);
				searchNodata.setVisibility(View.GONE);
			}else{
				searchNodata.setVisibility(View.VISIBLE);
				searchPageAdapter.notifyDataSetChanged();
			}
			
			progressbar.setVisibility(View.GONE);
			
		} else {
			progressbar.setVisibility(View.GONE);
			searchPageAdapter.notifyDataSetChanged();
			searchNodata.setVisibility(View.VISIBLE);
		}
	}

	/**
	 * 解析获取的json数据
	 * 
	 * @param result
	 * @return
	 */
	private SearchBean parsedJson(String json) {
		SearchBean bean = new SearchBean();
		JSONObject jsonObject;
		try {
			jsonObject = new JSONObject(json);
			String total = jsonObject.optString("total");
			bean.setTotal(total);
			String pageSize = jsonObject.optString("pageSize");
			bean.setPageSize(pageSize);
			
			JSONArray jsonArray = jsonObject.optJSONArray("items");
			List<ItemData> itemDatas = new ArrayList<ItemData>();
			for (int i = 0; i < jsonArray.length(); i++) {
				ItemData itemData = new ItemData();
				JSONObject jsonObjectItem = (JSONObject) jsonArray.get(i);
				String itemTitle = jsonObjectItem.optString("itemTitle");// 获取标题
				itemData.setItemTitle(itemTitle);
				String detailUrl = jsonObjectItem.optString("detailUrl");// 获取细节
				itemData.setDetailUrl(detailUrl);
				String pubTime = jsonObjectItem.optString("pubTime");// 获取时间
				itemData.setPubTime(pubTime);
				String keywords = jsonObjectItem.optString("keywords");// 获取关键字
				itemData.setKeywords(keywords);
				String category = jsonObjectItem.optString("category");// 获取类型
				itemData.setCategory(category);
				String source = jsonObjectItem.optString("source");// 获取来源
				itemData.setSource(source);
				String datecheck = jsonObjectItem.optString("datecheck");
				itemData.setDatecheck(datecheck);

				JSONObject jsonImage = jsonObjectItem
						.getJSONObject("itemImage");
				ItemImageEntity itemImageEntity = new ItemImageEntity();
				String imgUrl1 = jsonImage.optString("imgUrl1"); // 获取图片路劲
				itemImageEntity.setImgUrl1(imgUrl1);
				itemData.setItemImage(itemImageEntity);

				itemDatas.add(itemData);
			}

			bean.setItems(itemDatas);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return bean;
	}

}
