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
 * �û���¼�Ľ��� ע��㣺 ��һ���Ǽ������״̬��ֻ�ܼ���������޷����wifi��
 * �ڶ����������߳��У����û�õ��û������������httpͨ������󷵻ص�infoֵ������ֱ�������߳��и������̵߳�ҳ��ֵ������handle���
 * ����������get/post����http����ʽ������ʵ����
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
	 * �û�������û��������룬Ҫʵ�ּ�ס���빦�����Զ����ȫ��
	 */
	private String username;
	private String password;

	/**
	 * �û������url��ַ·��
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
		//��ȡ SharedPreferences ����Ȼ��
		//�������� getBoolean()����ȥ��ȡ remember_password �������Ӧ��ֵ��һ��ʼ
		//�����ڶ�Ӧ��ֵ�����Ի�ʹ��Ĭ��ֵ false��������ʲô�����ᷢ��
		pref = PreferenceManager.getDefaultSharedPreferences(this);
		boolean isRemember = pref.getBoolean("remember_password", false);
		if (isRemember) {
			// ���˺ź����붼���õ��ı�����
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
	 * �����¼��ťִ�еĵ�¼�¼�
	 * 
	 * @param view
	 */
	public void doLogin(View view) {
		username = loginUsername.getText().toString().trim();
		password = loginPassword.getText().toString().trim();
		if (null == username || "".equals(username)) {
			Toast.makeText(this, "�������û���", Toast.LENGTH_SHORT).show();
			return;
		}
		if (null == password || "".equals(password)) {
			Toast.makeText(this, "����������", Toast.LENGTH_SHORT).show();
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
	 * ��Ϣ����
	 */
	private Handler handler = new Handler() {
		@SuppressLint("HandlerLeak") @Override
		public void handleMessage(Message msg) {
			if (msg.what == 1) {
				String responseStr = msg.obj.toString();
				if (null != responseStr && !"failed".equals(responseStr)) {
					// ��¼�ɹ�
					editor = pref.edit(); // ����SharedPreferences�ļ���ʼ��������
					if (rememberPassword.isChecked()) { // ��鸴ѡ���Ƿ�ѡ��
						editor.putBoolean("remember_password", true);
						editor.putString("account", username);
						editor.putString("password", password);
					} else {
						editor.clear();// �� SharedPreferences �ļ��е�����ȫ�������
					}
					editor.commit();

					User user = parseJson(responseStr);
					Utils.currentUser = user; // �ѵ�¼���û�����ȫ�ֵĵ�ǰuser����
					LoginActivity.this.finish();

				} else if ("failed".equals(responseStr)) {
					Toast.makeText(LoginActivity.this, "��¼ʧ��,�����������û���������!",
							Toast.LENGTH_SHORT).show();

				} else {
					Toast.makeText(LoginActivity.this, "��¼��ʱ,�뿪��������!",
							Toast.LENGTH_SHORT).show();
				}
			}else if(msg.what == 2){
				Toast.makeText(LoginActivity.this, "��¼��ʱ,ͨѶ�쳣",
						Toast.LENGTH_LONG).show();
			}
		}
	};

	/**
	 * ʹ�� HttpURLConnection �����¼����
	 */
	private void requestUsingHttpURLConnection() {
		// ����ͨ�����ڵ��͵ĺ�ʱ�������������߳̽�����������
		new Thread(new Runnable() {
			@Override
			public void run() {
				HttpURLConnection connection = null;
				BufferedReader bufferedReader = null;
				try {
					URL urll = new URL(url); // ����һ��URL
					connection = (HttpURLConnection) urll.openConnection(); // �򿪸�URL����
					connection.setRequestMethod("GET");// �������󷽷���GET������������GET
					connection.setConnectTimeout(3000); // �������ӽ����ĳ�ʱʱ��
					connection.setReadTimeout(3000); // �������籨���շ���ʱʱ��
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
	 * �������������ص�json�ַ���--��װΪһ��User����
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
	 * ���ע�ᰴť����ע�����
	 * 
	 * @param view
	 */
	public void goToRegisterActivity(View view) {
		// Toast.makeText(this, "ע�ᰴť", Toast.LENGTH_SHORT).show();
		Intent intent = new Intent(this, RegisterActivity.class);
		startActivity(intent);
	}
}
