package com.njau.mobileplayer.pages;

import java.util.List;

import org.xutils.x;
import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.view.annotation.ViewInject;

import android.content.Context;
import android.view.View;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.gson.Gson;
import com.njau.mobileplayer.R;
import com.njau.mobileplayer.adapter.RecommendPageAdapter;
import com.njau.mobileplayer.base.BasePage;
import com.njau.mobileplayer.entity.RecommendData;
import com.njau.mobileplayer.entity.RecommendData.ListEntity;
import com.njau.mobileplayer.utils.Constant;
import com.njau.mobileplayer.utils.StringUtils;

/**
 * 推荐界面
 * @author zhangcan
 *
 */
public class RecommendPage extends BasePage {

	@ViewInject(R.id.recommend_listview)
	private ListView recommendListview;
	
	@ViewInject(R.id.recommend_nonet)
	private TextView recommendNonet;
	
	@ViewInject(R.id.recommend_loading)
	private ProgressBar recommendLoading;

	/**
	 * 页面数据
	 */
	private List<ListEntity> datas;
	
	private RecommendPageAdapter adapter;
	
	public RecommendPage(Context context) {
		super(context);
	}

	@Override
	public View initView() {
		View view = View.inflate(context, R.layout.recommend_page, null);
		//使这个view布局和当前的类绑定实现注入
		x.view().inject(this, view);
		
		return view;
	}
	
	@Override
	public void initData() {
		super.initData();
		//获取缓存的数据
		String catchData = StringUtils.getString(context, Constant.RECOMMED_URL);
		if(null != catchData && !"".equals(catchData)){
			//解析数据
			processData(catchData);
		}
		//联网
		getDataFromNet();
	}

	/**
	 * 解析数据同时设置适配器
	 * @param catchData
	 */
	private void processData(String json) {
		if(json !=null && !"".equals(json)){
			RecommendData recommendData = parseJson(json);
			datas = recommendData.getList();
			if(datas != null && datas.size()>0){
				//有数据
				recommendNonet.setVisibility(View.GONE);
				//设置适配器
				adapter = new RecommendPageAdapter(context, datas);
				recommendListview.setAdapter(adapter);
				
			}else{
				recommendNonet.setText("没有数据..");
				recommendNonet.setVisibility(View.VISIBLE);
			}
			recommendLoading.setVisibility(View.GONE);
		}else{
			recommendNonet.setText("没有数据..");
			recommendLoading.setVisibility(View.GONE);
			recommendNonet.setVisibility(View.VISIBLE);
		}
	}

	/**
	 * 解析json数据
	 * @param json
	 * @return
	 */
	private RecommendData parseJson(String json) {
		Gson gson = new Gson();
		return gson.fromJson(json, RecommendData.class);
	}

	/**
	 * 联网获取数据
	 */
	private void getDataFromNet() {
		RequestParams params = new RequestParams(Constant.RECOMMED_URL);
		x.http().get(params, new Callback.CommonCallback<String>(){

			@Override
			public void onCancelled(CancelledException arg0) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void onError(Throwable arg0, boolean arg1) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void onFinished() {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void onSuccess(String result) {
				//保存数据
				StringUtils.putString(context, Constant.RECOMMED_URL, result);
				processData(result);
			}
			
		});
	}

}
