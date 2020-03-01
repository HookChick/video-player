package com.njau.playerservice.entity;

import java.sql.Timestamp;

public class PayItem {

	private int payid;
	private int userid;
	private String videoid;
	private boolean paytype;
	private String paytime;
	
	private Video video;

	public Video getVideo() {
		return video;
	}

	public void setVideo(Video video) {
		this.video = video;
	}

	public int getPayid() {
		return payid;
	}

	public void setPayid(int payid) {
		this.payid = payid;
	}

	public int getUserid() {
		return userid;
	}

	public void setUserid(int userid) {
		this.userid = userid;
	}

	public String getVideoid() {
		return videoid;
	}

	public void setVideoid(String videoid) {
		this.videoid = videoid;
	}

	public boolean isPaytype() {
		return paytype;
	}

	public void setPaytype(boolean paytype) {
		this.paytype = paytype;
	}
	
	public String getPaytime() {
		return paytime;
	}

	public void setPaytime(String paytime) {
		this.paytime = paytime;
	}

	@Override
	public String toString() {
		return "PayItem [payid=" + payid + ", userid=" + userid + ", videoid="
				+ videoid + ", paytype=" + paytype + "]";
	}

}
