package com.njau.mobileplayer.base;

import android.content.Context;
import android.view.View;

/**
 * 主页面中所有选项页面的基类，让所有子页面去继承他
 * @author zhangcan
 *
 */
public abstract class BasePage {
	
	public Context context;
	/**
	 * 获取当前页面的实例
	 */
	public View baseView;
	/**
	 * 防止页面第一次初始化数据后，再切换回来时该页面再一次初始化数据，以免浪费流量资源
	 */
	public boolean isInitData;

	public BasePage(Context context) {
		this.context = context;
		this.baseView = this.initView();
	}

	/**
	 * 当加载该类时就立即需要子类实现，实现不同的视图
	 * @return
	 */
	public abstract View initView();
	
	/**
	 * 让子页面初始化数据时重写该方法
	 */
	public void initData(){
		
	}
}
