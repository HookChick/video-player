package com.njau.playerservice.util;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Util {

	/**
	 * 获取当前系统的时间
	 * @return
	 */
	public static Timestamp getTimestamp(){
		Date date = new Date();
		String dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(date);
		Timestamp timestamp = Timestamp.valueOf(dateFormat);
		return timestamp;
	}
}
