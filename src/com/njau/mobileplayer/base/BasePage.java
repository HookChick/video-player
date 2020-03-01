package com.njau.mobileplayer.base;

import android.content.Context;
import android.view.View;

/**
 * ��ҳ��������ѡ��ҳ��Ļ��࣬��������ҳ��ȥ�̳���
 * @author zhangcan
 *
 */
public abstract class BasePage {
	
	public Context context;
	/**
	 * ��ȡ��ǰҳ���ʵ��
	 */
	public View baseView;
	/**
	 * ��ֹҳ���һ�γ�ʼ�����ݺ����л�����ʱ��ҳ����һ�γ�ʼ�����ݣ������˷�������Դ
	 */
	public boolean isInitData;

	public BasePage(Context context) {
		this.context = context;
		this.baseView = this.initView();
	}

	/**
	 * �����ظ���ʱ��������Ҫ����ʵ�֣�ʵ�ֲ�ͬ����ͼ
	 * @return
	 */
	public abstract View initView();
	
	/**
	 * ����ҳ���ʼ������ʱ��д�÷���
	 */
	public void initData(){
		
	}
}
