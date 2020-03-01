package com.njau.playerservice.test;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.Formatter;
import java.util.List;
import java.util.Locale;

import com.njau.playerservice.dao.impl.PayItemDaoImpl;
import com.njau.playerservice.dao.impl.UserDaoImpl;
import com.njau.playerservice.entity.User;
import com.njau.playerservice.util.Util;

public class Test {
	
	public static void main(String[] args) {
		Date date = new Date();
		String dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(date);
		System.out.println(dateFormat);
	}
	
	public static void main7(String[] args) {
//		System.out.println(2.f/3);
		ArrayList<String> strs = new ArrayList<>();
		strs.add("123");
		System.out.println(strs.get(0));
	}
	public static void main6(String[] args) {
		String puVideoUrl = "http://vfx.mtime.cn/Video/2017/05/05/mp4/170505073213801260.mp4";
		String highVideoUrl = "http://vfx.mtime.cn/Video/2017/05/05/mp4/170505073213801260.mp4";
		String s[] = (puVideoUrl+","+highVideoUrl).split(",");
		System.out.println(s[0]);
		System.out.println(s[1]);
	}
	public static void main5(String[] args) {
		List<String> lists = (List<String>) Arrays.asList("∂Øª≠", "√∞œ’", "œ≤æÁ", "º“Õ•");
		System.out.println(lists.contains(""));
	}
	
	public static void main4(String[] args) {
		PayItemDaoImpl daoImpl = new PayItemDaoImpl();
		String sql = "select id,videoname,videosrc,videodesc,descimg,needmoney,payid,paytype,paytime from video v left join payitem p on p.videoid=v.id where p.userid=?";
		
		ResultSet resultSet = daoImpl.querryForObject(sql, 1);
		try {
			while(resultSet.next()){
				Timestamp time = resultSet.getTimestamp(9);
				SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-mm-dd hh:mm:ss");
				String arg0 = dateFormat.format(time);
				System.out.println(arg0);
				Timestamp timestamp = Timestamp.valueOf(arg0);
				System.out.println(timestamp);
			}
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		
	}
		
	public static void main3(String[] args) {
		Timestamp timestamp = Util.getTimestamp();
		System.out.println(timestamp);
		
	}
	public static void main2(String[] args) {
		Date date = new Date();
		String dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(date);
		Timestamp timestamp = Timestamp.valueOf(dateFormat);
		System.out.println(timestamp);
	}
	/**
	 * @param args
	 */
	public static void main1(String[] args) {
		UserDaoImpl daoImpl = new UserDaoImpl();
		Connection connection = daoImpl.getConnection();
		User tempUser;
		String sql = "select * from users where uname=? and password=?";
		User user = new User("zhang","123");
		ResultSet resultSet = daoImpl.querryForObject(sql, user.getUsername(),user.getPassword());
		try {
			if(resultSet.next()){
				tempUser = new User();
				tempUser.setUid(resultSet.getInt(1));
				tempUser.setUsername(resultSet.getString(2));
				tempUser.setPassword(resultSet.getString(3));
				tempUser.setRole(resultSet.getInt(4));
				System.out.println(tempUser);
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println(connection);
	}

}
