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
 * ���������activity
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
	 * ��HashMap�洢��д���
	 */
	private HashMap<String, String> mIatResults;

	/**
	 * ���������·��
	 */
	private String url;

	private List<ItemData> items;

	private SearchPageAdapter searchPageAdapter;

	/**
	 * ʵ����
	 */
	private void findViews() {
		etSearch = (EditText) findViewById(R.id.et_search);
		voiceSearch = (ImageView) findViewById(R.id.voice_search);
		videoSearch = (TextView) findViewById(R.id.video_search);
		searchListview = (ListView) findViewById(R.id.search_listview);
		progressbar = (ProgressBar) findViewById(R.id.progressbar);
		searchNodata = (TextView) findViewById(R.id.search_nodata);

		mIatResults = new LinkedHashMap<String, String>();

		// ���õ���¼�
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
	 * ����listView�ĵ���¼�
	 * 
	 * @author zhangcan
	 * 
	 */
	class MyOnItemClickListener implements OnItemClickListener {

		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position,
				long id) {

			// ��Ҫ������Ƶ�б��ȥ�������ڲ��Ž��沥����һ����һ����Ƶ
			Intent intent = new Intent(SearchActivity.this,
					MySystemDefinedVideoPlayer.class);
			Bundle bundle = new Bundle();
			bundle.putSerializable("videolist",
					(ArrayList<ItemData>) items);// ������Ƶ�����б�
			intent.putExtras(bundle);
			intent.putExtra("position", position);
			startActivity(intent);

		}
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.voice_search:
			// ��������
			// 1.����RecognizerDialog����
			RecognizerDialog mDialog = new RecognizerDialog(this,
					new MyInitListener());
			// 2.����accent�� language�Ȳ���
			mDialog.setParameter(SpeechConstant.LANGUAGE, "zh_cn");// ��������
			mDialog.setParameter(SpeechConstant.ACCENT, "mandarin");// ��ͨ��
			// ��Ҫ��UI�ؼ�����������⣬����������²������ã�����֮��onResult�ص����ؽ����������
			// ���
			// mDialog.setParameter("asr_sch", "1");
			// mDialog.setParameter("nlp_version", "2.0");
			// 3.���ûص��ӿ�
			mDialog.setListener(new MyRecognizerDialogListener());
			// 4.��ʾdialog��������������
			mDialog.show();
			break;

		case R.id.video_search:
			// ���������ťִ������-->�������󵽷�������������
			String content = etSearch.getText().toString().trim();
			if (null != content && !"".equals(content)) {
				if(items != null && items.size()>0){
					items.clear(); //�ڶ�������ʱ��Ҫ��֮ǰ�����������
				}
				try {
					// �õ�����ǰ���ÿɼ�
					progressbar.setVisibility(View.VISIBLE);

					content = URLEncoder.encode(content, "UTF-8");
					url = Constant.SEARCH_URL + content;
					// ����xUtils�������ȡ����
					RequestParams params = new RequestParams(url);
					x.http().get(params, new MyCommonCallback());
					
					etSearch.setText("");

				} catch (UnsupportedEncodingException e) {
					e.printStackTrace();
				}
			} else {
				Toast.makeText(this, "����������.", Toast.LENGTH_SHORT).show();
			}
			break;
		}
	}

	/**
	 * xUtils��ȡ����ʱ��һ���ص�
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
			processData(result);// ��������
		}

	}

	/**
	 * ���ɿƴ�Ѷ��˵����һ������
	 * 
	 * @author zhangcan
	 * 
	 */
	class MyRecognizerDialogListener implements RecognizerDialogListener {

		/**
		 * ������
		 */
		@Override
		public void onError(SpeechError speechError) {
			Log.e("SearchActivity", "onError ==" + speechError.getMessage());
		}

		/**
		 * @param recognizerResult
		 * @param arg1
		 *            �Ƿ�˵������
		 */
		@Override
		public void onResult(RecognizerResult results, boolean arg1) {
			String result = results.getResultString();

			Log.e("searchActivity", "results=" + results);
			String text = JsonParser.parseIatResult(result);

			String sn = null;
			// ��ȡjson����е�sn�ֶ�
			try {
				JSONObject resultJson = new JSONObject(
						results.getResultString());
				sn = resultJson.optString("sn");
			} catch (JSONException e) {
				e.printStackTrace();
			}

			mIatResults.put(sn, text);

			StringBuffer resultBuffer = new StringBuffer();// ƴ���ַ���
			for (String key : mIatResults.keySet()) {
				resultBuffer.append(mIatResults.get(key));
			}

			etSearch.setText(resultBuffer.toString());
			etSearch.setSelection(etSearch.length());

		}

	}

	/**
	 * ��ʼ����һ������
	 * 
	 * @author zhangcan
	 * 
	 */
	class MyInitListener implements InitListener {

		@Override
		public void onInit(int arg0) {
			if (arg0 != ErrorCode.SUCCESS) {
				Toast.makeText(SearchActivity.this, "��ʼ��ʧ��", Toast.LENGTH_SHORT)
						.show();
			}
		}

	}

	/**
	 * �������������
	 * 
	 * @param result
	 */
	public void processData(String json) {
		SearchBean searchBean = parsedJson(json);
		if (null != searchBean) {
			items = searchBean.getItems();

			if(null != items && items.size()>0){
				searchPageAdapter = new SearchPageAdapter(this, items);
				// ����������
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
	 * ������ȡ��json����
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
				String itemTitle = jsonObjectItem.optString("itemTitle");// ��ȡ����
				itemData.setItemTitle(itemTitle);
				String detailUrl = jsonObjectItem.optString("detailUrl");// ��ȡϸ��
				itemData.setDetailUrl(detailUrl);
				String pubTime = jsonObjectItem.optString("pubTime");// ��ȡʱ��
				itemData.setPubTime(pubTime);
				String keywords = jsonObjectItem.optString("keywords");// ��ȡ�ؼ���
				itemData.setKeywords(keywords);
				String category = jsonObjectItem.optString("category");// ��ȡ����
				itemData.setCategory(category);
				String source = jsonObjectItem.optString("source");// ��ȡ��Դ
				itemData.setSource(source);
				String datecheck = jsonObjectItem.optString("datecheck");
				itemData.setDatecheck(datecheck);

				JSONObject jsonImage = jsonObjectItem
						.getJSONObject("itemImage");
				ItemImageEntity itemImageEntity = new ItemImageEntity();
				String imgUrl1 = jsonImage.optString("imgUrl1"); // ��ȡͼƬ·��
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
