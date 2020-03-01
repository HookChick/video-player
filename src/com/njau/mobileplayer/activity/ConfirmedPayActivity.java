package com.njau.mobileplayer.activity;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLDecoder;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.njau.mobileplayer.R;
import com.njau.mobileplayer.utils.Utils;

/**
 * ֧��ȷ��ҳ��
 * @author zhangcan
 *
 */
public class ConfirmedPayActivity extends Activity implements OnClickListener {

	private TextView pay_back_btn;
	private TextView pay_money;
	private Button pay_account_btn;
	
	private TextView go_chongzhi_btn;
	
	/**
	 * �Ӷ�ά����洫�����ĵ�ַ
	 */
	private String url;
	
	private void initViews() {
		pay_back_btn = (TextView) findViewById(R.id.pay_back_btn);
		pay_money = (TextView) findViewById(R.id.pay_money);
		pay_account_btn = (Button) findViewById(R.id.pay_account_btn);
		go_chongzhi_btn = (TextView) findViewById(R.id.go_chongzhi_btn);
		pay_back_btn.setOnClickListener(this);
		pay_account_btn.setOnClickListener(this);
		go_chongzhi_btn.setOnClickListener(this);
	}
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_pay_confirmed);
		initViews();
		url = getIntent().getStringExtra("content");
		float money = getIntent().getFloatExtra("paymoney",0);
		pay_money.setText("��"+money);
	}

	@Override
	public void onClick(View v) {
		if(v ==pay_back_btn){
			//���ذ�ť
			this.finish();
		}else if(v== pay_account_btn){
			//���ť
//			Toast.makeText(this, "���ť", Toast.LENGTH_SHORT).show();
//			Log.e("pay_account_btnurl", url);
			payMoney();
		}else if(v == go_chongzhi_btn){
			Intent intent = new Intent(ConfirmedPayActivity.this,ChongZhiActivity.class);
			startActivity(intent);
		}
	}

	/**
	 * ��Ϣ����
	 */
	private Handler handler = new Handler() {
		@SuppressLint("HandlerLeak") @Override
		public void handleMessage(Message msg) {
			if (msg.what == 1) {
				String responseStr = msg.obj.toString();
				if(null != responseStr){
					if ("success".equals(responseStr)) {
						//֧���ɹ�������
						ConfirmedPayActivity.this.finish();
						
					} else if ("notenough".equals(responseStr)) {
						//����
						Toast.makeText(ConfirmedPayActivity.this, "�˻�����!",
								Toast.LENGTH_SHORT).show();
						
					} else if("failed".equals(responseStr)) {
						//֧��ʧ��
						Toast.makeText(ConfirmedPayActivity.this, "��Ǹ,֧��ʧ��!",
								Toast.LENGTH_SHORT).show();
					}
				}else{
					Toast.makeText(ConfirmedPayActivity.this, "��¼��ʱ,�뿪��������!",
							Toast.LENGTH_SHORT).show();
					
				}
			}
		}
	};

	
	/**
	 * �����������
	 */
	private void payMoney() {
		//����ͨ���Ǻ�ʱ��
		new Thread(new Runnable() {
			@Override
			public void run() {
				HttpURLConnection connection = null;
				BufferedReader bufferedReader = null;
				try {
					URL urll = new URL(url); // ����һ��URL
					connection = (HttpURLConnection) urll.openConnection(); // �򿪸�URL����
					connection.setRequestMethod("GET");// �������󷽷���GET������������GET
					connection.setConnectTimeout(5000); // �������ӽ����ĳ�ʱʱ��
					connection.setReadTimeout(5000); // �������籨���շ���ʱʱ��
					InputStream in = connection.getInputStream(); // ͨ�����ӵ���������ȡ�·����ģ�Ȼ�����Java��������
					bufferedReader = new BufferedReader(
							new InputStreamReader(in));
					StringBuilder response = new StringBuilder();
					String line;
					while ((line = bufferedReader.readLine()) != null) {
						response.append(line);
					}
					// �������ݱ���
					String responseJson = URLDecoder.decode(
							response.toString(), "utf-8");

					Message msg = new Message();
					msg.what = 1;
					msg.obj = responseJson;

					handler.sendMessage(msg);
				} catch (IOException e) {
					e.printStackTrace();
				}finally{
					if(bufferedReader!=null){
						try {
							bufferedReader.close();
						} catch (IOException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
					if(connection!=null){
						connection.disconnect();
					}
				}
			}
		}).start();
	}

}
