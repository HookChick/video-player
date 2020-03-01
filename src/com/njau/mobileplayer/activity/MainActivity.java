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
 * �������Ҫ���� Activity
 */
public class MainActivity extends Activity implements View.OnClickListener {

	private ImageView user_clicked_login;//�������û������½�İ�ťͼƬ
	private View tv_search;//���������������
	private View tv_history_record;// ��������ʷ���ŵ����ťͼƬ
	private RadioGroup rg_bottom_tag;//��ҳ��ײ��İ�ť��
	
	
	/**
	 * �������ҳ��ļ���
	 */
	private List<BasePage> basePages = new ArrayList<BasePage>();
	
	/**
	 * ��ǰҳ���λ��
	 */
	private int position;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_main);
		//���λ�ȡʵ��
		this.rg_bottom_tag = (RadioGroup) this.findViewById(R.id.rg_bottom_tag);
		this.user_clicked_login = (ImageView) this.findViewById(R.id.user_clicked_login);
		this.tv_search = (TextView) this.findViewById(R.id.tv_search);
		this.tv_history_record = (ImageView) this.findViewById(R.id.tv_history_record);
		//�������õ���¼�
		this.user_clicked_login.setOnClickListener(this);
		this.tv_search.setOnClickListener(this);
		this.tv_history_record.setOnClickListener(this);
		
		basePages.add(new NativeVideoPage(this));//��ӱ�����Ƶ�ļ�ҳ��  λ�õ�����0
		basePages.add(new NetVideoPage(this));//���������Ƶ�ļ�ҳ��   λ�õ�����1
		basePages.add(new RecommendPage(this));//����Ƽ�ҳ��  λ�õ�����2
		basePages.add(new VipVideoPage(this));//���VIPר����Ƶ�ļ�ҳ��  λ�õ�����3
		//���ð�ťRadioGroup �ĵ�������¼�
		rg_bottom_tag.setOnCheckedChangeListener(new MyOnCheckedChangeListener());
		
		rg_bottom_tag.check(R.id.rb_native_video);//Ĭ��ѡ�б�����Ƶ
		
	}
	
	/**
	 * �ڸ���������ʵ��RadioGroup�����¼����ڲ���
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
			case R.id.rb_native_video: //������Ƶ
				position = 0;
				break;
			case R.id.rb_net_video: //������Ƶ
				position = 1;
				break;
			case R.id.rb_recommend_video: //�Ƽ���Ƶ
				position = 2;
				break;
			case R.id.rb_vip_video: //�Ƽ���Ƶ
				position = 3;
				break;
			}
			
			setFragment();
		}
		
	}

	/**
	 * �ѵ�ǰҳ����ӵ�Fragment��
	 */
	private void setFragment() {
		//�õ�FragementManger
		FragmentManager fragmentManager = this.getFragmentManager();
		//��������
		FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
		//�滻
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
		//�ύ����
		fragmentTransaction.commit();
	}

	/**
	 * ���ݵ�ǰλ�û�ȡҳ�棬���Է��ص�ǰҳ�����ͼ
	 * @return
	 */
	private BasePage getBasePages() {
		BasePage basePage = this.basePages.get(position);
		
		if(basePage != null && !basePage.isInitData){
			basePage.initData();//��ʼ��ҳ��ʱ������
		}
		return basePage;
	}

	@Override
	public void onClick(View v) {
		Intent intent;
		switch(v.getId()){
		case R.id.user_clicked_login:
//			Toast.makeText(this, "�û���½��ť���", Toast.LENGTH_SHORT).show();
			if(Utils.currentUser == null){
				intent = new Intent(this,LoginActivity.class);
				startActivity(intent);
			}else{
				//���û���¼�ˣ��������鿴�û���Ϣҳ��
				intent = new Intent(this,ShowUserInfoActivity.class);
				startActivity(intent);
			}
			break;
		case R.id.tv_search:
//			Toast.makeText(this, "�����򱻵��", Toast.LENGTH_SHORT).show();
			intent = new Intent(this,SearchActivity.class);
			startActivity(intent);
			break;
		case R.id.tv_history_record:
//			Toast.makeText(this, "��ʷ�����", Toast.LENGTH_SHORT).show();
			intent = new Intent(this,HistoryActivity.class);
			startActivity(intent);
			break;
		}
	}
	
	/**
	 * �ڻ�ɲ��ɼ���Ϊ�ɼ���ʱ�����,�˴������û��е�¼�����¼�ɹ����ص���ҳ��ʱ����һЩ��ʼ������
	 */
	@Override
	protected void onStart() {
		super.onStart();
		//�û���¼��
		if(Utils.currentUser != null){
			user_clicked_login.setImageResource(R.drawable.user_has_login);
		}else{
			user_clicked_login.setImageResource(R.drawable.user_clicked_login);
			
		}
	}

	
}
