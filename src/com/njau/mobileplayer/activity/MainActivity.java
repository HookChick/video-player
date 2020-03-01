package com.njau.mobileplayer.activity;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.TextView;
import android.widget.Toast;

import com.njau.mobileplayer.R;
import com.njau.mobileplayer.base.BasePage;
import com.njau.mobileplayer.pages.NativeVideoPage;
import com.njau.mobileplayer.pages.NetVideoPage;
import com.njau.mobileplayer.pages.RecommendPage;
import com.njau.mobileplayer.pages.VipVideoPage;
import com.njau.mobileplayer.utils.Utils;

/**
 * 
 * @author zhangcan
 * 软件的主要界面 Activity
 */
public class MainActivity extends Activity implements View.OnClickListener {

	private ImageView user_clicked_login;//顶部的用户点击登陆的按钮图片
	private View tv_search;//顶部的搜索点击框
	private View tv_history_record;// 顶部的历史播放点击按钮图片
	private RadioGroup rg_bottom_tag;//主页面底部的按钮组
	
	
	/**
	 * 存放所有页面的集合
	 */
	private List<BasePage> basePages = new ArrayList<BasePage>();
	
	/**
	 * 当前页面的位置
	 */
	private int position;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_main);
		//依次获取实例
		this.rg_bottom_tag = (RadioGroup) this.findViewById(R.id.rg_bottom_tag);
		this.user_clicked_login = (ImageView) this.findViewById(R.id.user_clicked_login);
		this.tv_search = (TextView) this.findViewById(R.id.tv_search);
		this.tv_history_record = (ImageView) this.findViewById(R.id.tv_history_record);
		//依次设置点击事件
		this.user_clicked_login.setOnClickListener(this);
		this.tv_search.setOnClickListener(this);
		this.tv_history_record.setOnClickListener(this);
		
		basePages.add(new NativeVideoPage(this));//添加本地视频文件页面  位置的索引0
		basePages.add(new NetVideoPage(this));//添加网络视频文件页面   位置的索引1
		basePages.add(new RecommendPage(this));//添加推荐页面  位置的索引2
		basePages.add(new VipVideoPage(this));//添加VIP专区视频文件页面  位置的索引3
		//设置按钮RadioGroup 的点击监听事件
		rg_bottom_tag.setOnCheckedChangeListener(new MyOnCheckedChangeListener());
		
		rg_bottom_tag.check(R.id.rb_native_video);//默认选中本地视频
		
	}
	
	/**
	 * 在该类中用来实现RadioGroup监听事件的内部类
	 * @author zhangcan
	 *
	 */
	private class MyOnCheckedChangeListener implements OnCheckedChangeListener{

		@Override
		public void onCheckedChanged(RadioGroup group, int checkedId) {
			switch(checkedId){
			default:
			  position = 0;
			  break;
			case R.id.rb_native_video: //本地视频
				position = 0;
				break;
			case R.id.rb_net_video: //网络视频
				position = 1;
				break;
			case R.id.rb_recommend_video: //推荐视频
				position = 2;
				break;
			case R.id.rb_vip_video: //推荐视频
				position = 3;
				break;
			}
			
			setFragment();
		}
		
	}

	/**
	 * 把当前页面添加到Fragment中
	 */
	private void setFragment() {
		//得到FragementManger
		FragmentManager fragmentManager = this.getFragmentManager();
		//开启事物
		FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
		//替换
		fragmentTransaction.replace(R.id.fl_main_content, new Fragment(){
			@Override
			public View onCreateView(LayoutInflater inflater,
					ViewGroup container, Bundle savedInstanceState) {
				BasePage basePage = getBasePages();
				if(basePage != null)
					return basePage.baseView;
				return null;
			}
		});
		//提交事物
		fragmentTransaction.commit();
	}

	/**
	 * 根据当前位置获取页面，用以返回当前页面的视图
	 * @return
	 */
	private BasePage getBasePages() {
		BasePage basePage = this.basePages.get(position);
		
		if(basePage != null && !basePage.isInitData){
			basePage.initData();//初始化页面时绑定数据
		}
		return basePage;
	}

	@Override
	public void onClick(View v) {
		Intent intent;
		switch(v.getId()){
		case R.id.user_clicked_login:
//			Toast.makeText(this, "用户登陆按钮点击", Toast.LENGTH_SHORT).show();
			if(Utils.currentUser == null){
				intent = new Intent(this,LoginActivity.class);
				startActivity(intent);
			}else{
				//有用户登录了，则启动查看用户信息页面
				intent = new Intent(this,ShowUserInfoActivity.class);
				startActivity(intent);
			}
			break;
		case R.id.tv_search:
//			Toast.makeText(this, "搜索框被点击", Toast.LENGTH_SHORT).show();
			intent = new Intent(this,SearchActivity.class);
			startActivity(intent);
			break;
		case R.id.tv_history_record:
//			Toast.makeText(this, "历史被点击", Toast.LENGTH_SHORT).show();
			intent = new Intent(this,HistoryActivity.class);
			startActivity(intent);
			break;
		}
	}
	
	/**
	 * 在活动由不可见变为可见的时候调用,此处用于用户有登录界面登录成功返回到主页面时进行一些初始化操作
	 */
	@Override
	protected void onStart() {
		super.onStart();
		//用户登录了
		if(Utils.currentUser != null){
			user_clicked_login.setImageResource(R.drawable.user_has_login);
		}else{
			user_clicked_login.setImageResource(R.drawable.user_clicked_login);
			
		}
	}

	
}
