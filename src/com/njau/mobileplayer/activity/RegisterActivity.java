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
 * 注册界面
 * @author zhangcan
 *
 */
public class RegisterActivity extends Activity implements OnClickListener {

	private EditText registerUsername;
	private EditText registerPassword;
	private EditText registerConfirmPassword;
	private TextView backBtn;
	
	/**
	 * 注册的地址url
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
	 * 设置监听事件
	 */
	private void setListener() {
		backBtn.setOnClickListener(this);
		
		registerConfirmPassword.setOnFocusChangeListener(new MyOnFocusChangeListener());
		
	}

	/**
	 * 处理注册事件按钮
	 * 
	 * @param view
	 */
	public void doRegister(View view) {
		String registerName = registerUsername.getText().toString().trim();
		String registerPwd = registerPassword.getText().toString().trim();
		String confirmpwd = registerConfirmPassword.getText().toString().trim();
		if("".equals(confirmpwd)){
			Toast.makeText(RegisterActivity.this, "确定密码不能为空", Toast.LENGTH_SHORT).show();
			return;
		}
		if(!registerPwd.equals(confirmpwd)){
			Toast.makeText(RegisterActivity.this, "确认密码必须和密码相同", Toast.LENGTH_SHORT).show();
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
	 * 链接服务器进行注册的方法
	 * 
	 */
	private void registerRequest() {
		new Thread(new Runnable() {
			
			@Override
			public void run() {
				HttpURLConnection httpsURLConnection = null;
				BufferedReader bufferedReader = null;
				try {
					URL urll = new URL(registerUrl); // 声明一个URL
					httpsURLConnection = (HttpURLConnection) urll.openConnection(); // 打开该URL连接
					httpsURLConnection.setRequestMethod("GET");// 设置请求方法，GET，我们这里用GET
					httpsURLConnection.setConnectTimeout(5000); // 设置连接建立的超时时间
					httpsURLConnection.setReadTimeout(5000); // 设置网络报文收发超时时间
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
	 * 处理消息
	 */
	private Handler handler = new Handler(){
		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case 1:
				String response = msg.obj.toString();
				if(null != response && "success".equals(response)){
					//注册成功
//					Toast.makeText(RegisterActivity.this, "注册成功！", Toast.LENGTH_SHORT).show();
					RegisterActivity.this.finish();
				}else if(null != response && "fail".equals(response)){
					//注册失败
					Toast.makeText(RegisterActivity.this, "注册失败！", Toast.LENGTH_SHORT).show();
				}
				break;

			}
		};
	};

	/**
	 * EditText编辑框的失去焦点监听类
	 * @author zhangcan
	 *
	 */
	class MyOnFocusChangeListener implements OnFocusChangeListener {
		@Override
		public void onFocusChange(View v, boolean hasFocus) {
			if (!hasFocus) {
				// 此处为失去焦点时的处理内容
				String password = registerPassword.getText().toString().trim();
				String confirmpwd = registerConfirmPassword.getText().toString().trim();
				if("".equals(confirmpwd)){
					Toast.makeText(RegisterActivity.this, "确定密码不能为空", Toast.LENGTH_SHORT).show();
					return;
				}
				if(!password.equals(confirmpwd)){
					Toast.makeText(RegisterActivity.this, "确认密码必须和密码相同", Toast.LENGTH_SHORT).show();
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
