package com.njau.mobileplayer.activity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.njau.mobileplayer.R;
import com.njau.mobileplayer.entity.VipVideoItem;
import com.njau.mobileplayer.utils.Constant;
import com.njau.mobileplayer.utils.Utils;
import com.njau.mobileplayer.zxing.QRImageUtli;

/**
 * 显示二维码图片的界面
 * 
 * @author zhangcan
 * 
 */
public class QRImageActivity extends Activity implements OnClickListener {

	private TextView qr_back_btn;
	private Button do_scan_qrimage;
	private ImageView show_qr_image;

	/**
	 * 创建二维码的类，扫描
	 */
	private QRImageUtli qrImage;

	private VipVideoItem vipVideoItem;
	
	/**
	 * 当前视频需要付的费用
	 */
	private float money;

	private void initViews() {
		qr_back_btn = (TextView) findViewById(R.id.qr_back_btn);
		do_scan_qrimage = (Button) findViewById(R.id.do_scan_qrimage);
		show_qr_image = (ImageView) findViewById(R.id.show_qr_image);
		qrImage = new QRImageUtli();

		qr_back_btn.setOnClickListener(this);
		do_scan_qrimage.setOnClickListener(this);

	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_qr_image);
		initViews();
		vipVideoItem = (VipVideoItem) getIntent().getSerializableExtra(
				"vipVideoItem");
		showQrImage();
	}

	/**
	 * 显示二维码图片,传递一个处理请求的url地址
	 */
	private void showQrImage() {
		if (null != vipVideoItem) {
			money = vipVideoItem.getNeedmoney();
			String url = Constant.URL_IP + "videoServlet?method=pay&userid="
					+ Utils.currentUser.getUid() + "&videoid="
					+ vipVideoItem.getId() + "&needmoney="
					+ money;
			Bitmap bitmap = qrImage.createQRImage(url);
			show_qr_image.setImageBitmap(bitmap);
		} else {
			Toast.makeText(this, "没有传递数据", Toast.LENGTH_SHORT).show();
		}
	}

	@Override
	public void onClick(View v) {
		if (v.getId() == R.id.qr_back_btn) {
			// 返回按钮
			this.finish();
		} else if (v.getId() == R.id.do_scan_qrimage) {
			// 扫描按钮就扫描那个二维码
			// 获取扫描的二维码信息
			String content = qrImage.scanningImage(show_qr_image);
			if(content != null && content.startsWith("http")){
				//启动确认付款页面
				Intent intent = new Intent(QRImageActivity.this,
						ConfirmedPayActivity.class);
//				Log.e("QRImageActivity.accountURL", content);
				intent.putExtra("content", content);
				intent.putExtra("paymoney", money);
				this.finish();
				this.startActivity(intent);
			}
		}
	}

}
