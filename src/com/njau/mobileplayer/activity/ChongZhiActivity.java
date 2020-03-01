package com.njau.mobileplayer.activity;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import com.njau.mobileplayer.R;
import com.njau.mobileplayer.utils.Constant;
import com.njau.mobileplayer.utils.Utils;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

/**
 * ��Ա��ֵҳ��
 * @author zhangcan
 *
 */
public class ChongZhiActivity extends Activity implements OnClickListener {

	private EditText pay_money;
	private TextView chongzhi_back_btn;
	
	/**
	 * ��ֵ�ĵ�ַUrl
	 */
	private String chongzhiurl;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_chongzhi);
		pay_money = (EditText) findViewById(R.id.pay_money);
		chongzhi_back_btn = (TextView) findViewById(R.id.chongzhi_back_btn);
		chongzhi_back_btn.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		this.finish();
	}
	
	/**
	 * �����ֵ�ķ���
	 * @param view
	 */
	public void doChongzhi(View view){
		String moneyStr = pay_money.getText().toString().trim();
		if(null == moneyStr || "".equals(moneyStr)){
			Toast.makeText(this, "���벻��Ϊ�գ�", Toast.LENGTH_SHORT).show();
			return;
		}
		try {
			float money = Float.parseFloat(moneyStr);
			chongzhiurl = Constant.URL_IP + "userServlet?method=doChongzhi&money="
					+ money+"&userid="+Utils.currentUser.getUid();
			requestChongzhi();
		} catch (NumberFormatException e) {
			Toast.makeText(this, "��������", Toast.LENGTH_SHORT).show();
			return;
		}
	}

	/**
	 * ���������ֵ
	 */
	private void requestChongzhi() {
		 new Thread(new Runnable() {
			
			@Override
			public void run() {
				BufferedReader bufferedReader = null;
				HttpURLConnection httpURLConnection= null;
				try {
					URL urll = new URL(chongzhiurl); // ����һ��URL
					httpURLConnection = (HttpURLConnection) urll.openConnection(); // �򿪸�URL����
					httpURLConnection.setRequestMethod("GET");// �������󷽷���GET������������GET
					httpURLConnection.setConnectTimeout(5000); // �������ӽ����ĳ�ʱʱ��
					httpURLConnection.setReadTimeout(5000); // �������籨���շ���ʱʱ��
					InputStream inputStream = httpURLConnection.getInputStream();
					bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
					String line = null;
					StringBuffer stringBuffer = new StringBuffer();
					while((line = bufferedReader.readLine())!= null){
						stringBuffer.append(line);
					}
					
					Message message = new Message();
					message.what = 1;
					message.obj = stringBuffer.toString();
					handler.sendMessage(message);
				} catch (IOException e) {
					// TODO Auto-generated catch block
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
					if(httpURLConnection!=null){
						httpURLConnection.disconnect();
					}
				}
			}
		}).start();
	}
	
	private Handler handler = new Handler(){
		public void handleMessage(Message msg) {
			
			if(msg.what == 1){
				String response = msg.obj.toString();
				if(response != null && "success".equals(response)){
					ChongZhiActivity.this.finish();
				}else if(response != null && "fail".equals(response)){
					Toast.makeText(ChongZhiActivity.this, "��ֵʧ��", Toast.LENGTH_SHORT).show();
				}
			}
		};
	};
}
