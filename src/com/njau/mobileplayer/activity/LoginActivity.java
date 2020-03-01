package com.njau.mobileplayer.activity;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLDecoder;
import java.net.URLEncoder;

import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.njau.mobileplayer.R;
import com.njau.mobileplayer.entity.User;
import com.njau.mobileplayer.utils.Constant;
import com.njau.mobileplayer.utils.Utils;

/**
 * 用户登录的界面 注意点： 第一个是检测网络状态，只能检测流量，无法检测wifi；
 * 第二个是在子线程中，利用获得的用户名密码调用了http通信类最后返回的info值，不能直接在子线程中更改主线程的页面值，必须handle解决
 * 第三个是有get/post两种http请求方式，两个实现类
 * 
 * @author zhangcan
 * 
 */
public class LoginActivity extends Activity {

	private EditText loginUsername;
	private EditText loginPassword;
	private CheckBox rememberPassword;
	private TextView backBtn;

	private SharedPreferences pref;
	private SharedPreferences.Editor editor;
	
	/**
	 * 用户输入的用户名和密码，要实现记住密码功能所以定义成全局
	 */
	private String username;
	private String password;

	/**
	 * 用户请求的url地址路径
	 */
	private String url;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);
		loginUsername = (EditText) this.findViewById(R.id.login_username);
		loginPassword = (EditText) this.findViewById(R.id.login_password);
		rememberPassword = (CheckBox) this.findViewById(R.id.remember_password);
		backBtn = (TextView) findViewById(R.id.back_btn);
		setListenser();
		//获取 SharedPreferences 对象，然后
		//调用它的 getBoolean()方法去获取 remember_password 这个键对应的值，一开始
		//不存在对应的值，所以会使用默认值 false，这样就什么都不会发生
		pref = PreferenceManager.getDefaultSharedPreferences(this);
		boolean isRemember = pref.getBoolean("remember_password", false);
		if (isRemember) {
			// 将账号和密码都设置到文本框中
			String account = pref.getString("account", "");
			try {
				account = URLDecoder.decode(account,"UTF-8");
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			}
			String pwd = pref.getString("password", "");
			loginUsername.setText(account);
			loginPassword.setText(pwd);
			rememberPassword.setChecked(true);
		}
	}
	

	private void setListenser() {
		backBtn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				LoginActivity.this.finish();
			}
		});
	}


	/**
	 * 点击登录按钮执行的登录事件
	 * 
	 * @param view
	 */
	public void doLogin(View view) {
		username = loginUsername.getText().toString().trim();
		password = loginPassword.getText().toString().trim();
		if (null == username || "".equals(username)) {
			Toast.makeText(this, "请输入用户名", Toast.LENGTH_SHORT).show();
			return;
		}
		if (null == password || "".equals(password)) {
			Toast.makeText(this, "请输入密码", Toast.LENGTH_SHORT).show();
			return;
		}
		try {
			username = URLEncoder.encode(username, "utf-8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		loginUsername.setText("");
		loginPassword.setText("");
		url = Constant.URL_IP + "userServlet?method=doLogin&username="
				+ username + "&password=" + password;
		requestUsingHttpURLConnection();
	}

	/**
	 * 消息处理
	 */
	private Handler handler = new Handler() {
		@SuppressLint("HandlerLeak") @Override
		public void handleMessage(Message msg) {
			if (msg.what == 1) {
				String responseStr = msg.obj.toString();
				if (null != responseStr && !"failed".equals(responseStr)) {
					// 登录成功
					editor = pref.edit(); // 设置SharedPreferences文件开始保存密码
					if (rememberPassword.isChecked()) { // 检查复选框是否被选中
						editor.putBoolean("remember_password", true);
						editor.putString("account", username);
						editor.putString("password", password);
					} else {
						editor.clear();// 将 SharedPreferences 文件中的数据全部清除掉
					}
					editor.commit();

					User user = parseJson(responseStr);
					Utils.currentUser = user; // 把登录的用户传给全局的当前user对象
					LoginActivity.this.finish();

				} else if ("failed".equals(responseStr)) {
					Toast.makeText(LoginActivity.this, "登录失败,请重新输入用户名或密码!",
							Toast.LENGTH_SHORT).show();

				} else {
					Toast.makeText(LoginActivity.this, "登录超时,请开启服务器!",
							Toast.LENGTH_SHORT).show();
				}
			}else if(msg.what == 2){
				Toast.makeText(LoginActivity.this, "登录超时,通讯异常",
						Toast.LENGTH_LONG).show();
			}
		}
	};

	/**
	 * 使用 HttpURLConnection 处理登录请求
	 */
	private void requestUsingHttpURLConnection() {
		// 网络通信属于典型的耗时操作，开启新线程进行网络请求
		new Thread(new Runnable() {
			@Override
			public void run() {
				HttpURLConnection connection = null;
				BufferedReader bufferedReader = null;
				try {
					URL urll = new URL(url); // 声明一个URL
					connection = (HttpURLConnection) urll.openConnection(); // 打开该URL连接
					connection.setRequestMethod("GET");// 设置请求方法，GET，我们这里用GET
					connection.setConnectTimeout(3000); // 设置连接建立的超时时间
					connection.setReadTimeout(3000); // 设置网络报文收发超时时间
					InputStream in = connection.getInputStream(); // 通过连接的输入流获取下发报文，然后就是Java的流处理
					bufferedReader = new BufferedReader(
							new InputStreamReader(in));
					StringBuilder response = new StringBuilder();
					String line;
					while ((line = bufferedReader.readLine()) != null) {
						response.append(line);
					}
				
					// 接受数据编码
					String responseJson = URLDecoder.decode(
							response.toString(), "utf-8");

					Message msg = new Message();
					msg.what = 1;
					msg.obj = responseJson;

//					Log.e("WangJ", responseJson);

					handler.sendMessage(msg);
				} catch (IOException e) {
					Message msg = new Message();
					msg.what = 2;
					handler.sendMessage(msg);
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

	/**
	 * 解析服务器返回的json字符串--封装为一个User对象
	 * 
	 * @param responseStr
	 */
	private User parseJson(String responseJson) {
		User user = new User();
		try {
			JSONObject jsonObject = new JSONObject(responseJson);
			int uid = jsonObject.optInt("uid");
			user.setUid(uid);
			String username = jsonObject.optString("username");
			user.setUsername(username);
			String password = jsonObject.optString("password");
			user.setPassword(password);
			int role = jsonObject.optInt("role");
			user.setRole(role);
		} catch (JSONException e) {
			e.printStackTrace();
		}

		return user;
	}

	/**
	 * 点击注册按钮进入注册界面
	 * 
	 * @param view
	 */
	public void goToRegisterActivity(View view) {
		// Toast.makeText(this, "注册按钮", Toast.LENGTH_SHORT).show();
		Intent intent = new Intent(this, RegisterActivity.class);
		startActivity(intent);
	}
}
