package com.njau.mobileplayer.activity;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

import javax.net.ssl.HttpsURLConnection;

import com.njau.mobileplayer.R;
import com.njau.mobileplayer.utils.Constant;
import com.njau.mobileplayer.utils.Utils;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

/**
 * ע�����
 * @author zhangcan
 *
 */
public class RegisterActivity extends Activity implements OnClickListener {

	private EditText registerUsername;
	private EditText registerPassword;
	private EditText registerConfirmPassword;
	private TextView backBtn;
	
	/**
	 * ע��ĵ�ַurl
	 */
	private String registerUrl;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_register);
		registerUsername = (EditText) findViewById(R.id.register_username);
		registerPassword = (EditText) findViewById(R.id.register_password);
		registerConfirmPassword = (EditText) findViewById(R.id.register_confirmpassword);
		backBtn = (TextView) findViewById(R.id.reg_back_btn);
		setListener();
	}

	/**
	 * ���ü����¼�
	 */
	private void setListener() {
		backBtn.setOnClickListener(this);
		
		registerConfirmPassword.setOnFocusChangeListener(new MyOnFocusChangeListener());
		
	}

	/**
	 * ����ע���¼���ť
	 * 
	 * @param view
	 */
	public void doRegister(View view) {
		String registerName = registerUsername.getText().toString().trim();
		String registerPwd = registerPassword.getText().toString().trim();
		String confirmpwd = registerConfirmPassword.getText().toString().trim();
		if("".equals(confirmpwd)){
			Toast.makeText(RegisterActivity.this, "ȷ�����벻��Ϊ��", Toast.LENGTH_SHORT).show();
			return;
		}
		if(!registerPwd.equals(confirmpwd)){
			Toast.makeText(RegisterActivity.this, "ȷ����������������ͬ", Toast.LENGTH_SHORT).show();
			return;
		}
		try {
			registerName = URLEncoder.encode(registerName, "utf-8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		registerUrl = Constant.URL_IP + "userServlet?method=doRegister&regusername="
				+ registerName + "&regpassword=" + registerPwd;
		
		registerRequest();
	}

	/**
	 * ���ӷ���������ע��ķ���
	 * 
	 */
	private void registerRequest() {
		new Thread(new Runnable() {
			
			@Override
			public void run() {
				HttpURLConnection httpsURLConnection = null;
				BufferedReader bufferedReader = null;
				try {
					URL urll = new URL(registerUrl); // ����һ��URL
					httpsURLConnection = (HttpURLConnection) urll.openConnection(); // �򿪸�URL����
					httpsURLConnection.setRequestMethod("GET");// �������󷽷���GET������������GET
					httpsURLConnection.setConnectTimeout(5000); // �������ӽ����ĳ�ʱʱ��
					httpsURLConnection.setReadTimeout(5000); // �������籨���շ���ʱʱ��
					InputStream inputStream = httpsURLConnection.getInputStream();
					bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
					String lineStr = null;
					StringBuffer buffer = new StringBuffer();
					while((lineStr = bufferedReader.readLine())!= null){
						buffer.append(lineStr);
					}
					
					Message message = new Message();
					message.what = 1;
					message.obj = buffer.toString();
					handler.sendMessage(message);
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
					if(httpsURLConnection!=null){
						httpsURLConnection.disconnect();
					}
				}
				
			}
		}).start();
	}

	/**
	 * ������Ϣ
	 */
	private Handler handler = new Handler(){
		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case 1:
				String response = msg.obj.toString();
				if(null != response && "success".equals(response)){
					//ע��ɹ�
//					Toast.makeText(RegisterActivity.this, "ע��ɹ���", Toast.LENGTH_SHORT).show();
					RegisterActivity.this.finish();
				}else if(null != response && "fail".equals(response)){
					//ע��ʧ��
					Toast.makeText(RegisterActivity.this, "ע��ʧ�ܣ�", Toast.LENGTH_SHORT).show();
				}
				break;

			}
		};
	};

	/**
	 * EditText�༭���ʧȥ���������
	 * @author zhangcan
	 *
	 */
	class MyOnFocusChangeListener implements OnFocusChangeListener {
		@Override
		public void onFocusChange(View v, boolean hasFocus) {
			if (!hasFocus) {
				// �˴�Ϊʧȥ����ʱ�Ĵ�������
				String password = registerPassword.getText().toString().trim();
				String confirmpwd = registerConfirmPassword.getText().toString().trim();
				if("".equals(confirmpwd)){
					Toast.makeText(RegisterActivity.this, "ȷ�����벻��Ϊ��", Toast.LENGTH_SHORT).show();
					return;
				}
				if(!password.equals(confirmpwd)){
					Toast.makeText(RegisterActivity.this, "ȷ����������������ͬ", Toast.LENGTH_SHORT).show();
					return;
				}
			}
		}
	}

	@Override
	public void onClick(View v) {
		if(v == backBtn){
			this.finish();
		}
	}

}
