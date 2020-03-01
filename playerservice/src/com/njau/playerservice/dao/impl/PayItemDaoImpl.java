package com.njau.playerservice.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import com.njau.playerservice.dao.BaseDAO;
import com.njau.playerservice.dao.PayItemDao;
import com.njau.playerservice.entity.PayItem;
import com.njau.playerservice.entity.Video;
import com.njau.playerservice.util.Util;

public class PayItemDaoImpl extends BaseDAO implements PayItemDao {

	@Override
	public boolean doPay(int uid, String videoid, float money) {
		boolean isSuccess = false;
		PreparedStatement pst = null;
		String updateOwnAccount = "update account set balance= balance - ? where userid=?";
		String updateAdminAccount = "update account set balance= balance +? where aid=2";
		String insertpayitem = "insert into payitem values(null,?,?,?,?)";
		Connection con = this.getConnection();
		try {
			con.setAutoCommit(false);
			pst = con.prepareStatement(updateOwnAccount);
			pst.setFloat(1, money);
			pst.setInt(2, uid);
			if (pst.executeUpdate() == 1) {
				pst.clearBatch();
				pst.close();
				pst = con.prepareStatement(updateAdminAccount);
				pst.setFloat(1, money);
				if (pst.executeUpdate() == 1) {
					pst.clearBatch();
					pst.close();
					pst = con.prepareStatement(insertpayitem);
					pst.setInt(1, uid);
					pst.setString(2, videoid);
					pst.setBoolean(3, true);
					pst.setTimestamp(4, Util.getTimestamp());
					if (pst.executeUpdate() == 1) {
						isSuccess = true;
					} else {
						isSuccess = false;
					}
				}
				con.commit();
				con.setAutoCommit(true);
				isSuccess = true;
			}

		} catch (Exception e) {
			try {
				con.rollback();
				isSuccess = false;
			} catch (SQLException e1) {
				e1.printStackTrace();
			}
			e.printStackTrace();
		} finally {
			this.closeAll(null, pst, con);
		}

		return isSuccess;

	}

	@Override
	public boolean isPayed(int uid, String videoid) {
//		1代表TRUE,0代表FALSE
		boolean paytype = false;
		String sql = "select paytype from payitem where userid=? and videoid=?";
		ResultSet resultSet = this.querryForObject(sql, uid,videoid);
		try {
			if(resultSet.next()){
				paytype = resultSet.getBoolean(1);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
//		System.out.println(paytype instanceof Integer);
		return paytype;
	}

	@Override
	public List<PayItem> getItems(int uid) {
		List<PayItem> payItems = new ArrayList<PayItem>();
		String sql = "select id,videoname,videosrc,videodesc,descimg,needmoney,payid,paytype,paytime from video v left join payitem p on p.videoid=v.id where p.userid=?";
		ResultSet resultSet = this.querryForObject(sql, uid);
		try {
			while(resultSet.next()){
				PayItem item = new PayItem();
				Video video = new Video();
				
				video.setId(resultSet.getString(1));
				video.setVideoname(resultSet.getString(2));
				video.setVideosrc(resultSet.getString(3));
				video.setVideodesc(resultSet.getString(4));
				video.setDescimg(resultSet.getString(5));
				video.setNeedmoney(resultSet.getFloat(6));
				
				item.setVideo(video);
				item.setPayid(resultSet.getInt(7));
				item.setUserid(uid);
				item.setPaytype(resultSet.getBoolean(8));
				Timestamp time = resultSet.getTimestamp(9);
				String timeStr = new SimpleDateFormat("yyyy-mm-dd hh:mm:ss").format(time);
				item.setPaytime(timeStr);
				
				payItems.add(item);
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return payItems;
	}

}
