package com.njau.mobileplayer.entity;

import java.sql.Timestamp;

/**
 * ¹ºÂò¼ÇÂ¼µÄbeanÀà
 * @author zhangcan
 *
 */
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
				+ videoid + ", paytype=" + paytype + ", paytime=" + paytime
				+ ", video=" + video + "]";
	}



	public static class Video{
		private String id;
		private String videoname;
		private String videosrc;
		private String videodesc;
		private String descimg;
		private float needmoney;

		
		public String getId() {
			return id;
		}

		public void setId(String id) {
			this.id = id;
		}

		public String getVideoname() {
			return videoname;
		}

		public void setVideoname(String videoname) {
			this.videoname = videoname;
		}

		public String getVideosrc() {
			return videosrc;
		}

		public void setVideosrc(String videosrc) {
			this.videosrc = videosrc;
		}

		public String getVideodesc() {
			return videodesc;
		}

		public void setVideodesc(String videodesc) {
			this.videodesc = videodesc;
		}

		public String getDescimg() {
			return descimg;
		}

		public void setDescimg(String descimg) {
			this.descimg = descimg;
		}

		public float getNeedmoney() {
			return needmoney;
		}

		public void setNeedmoney(float needmoney) {
			this.needmoney = needmoney;
		}

		@Override
		public String toString() {
			return "Video [id=" + id + ", videoname=" + videoname
					+ ", videosrc=" + videosrc + ", videodesc=" + videodesc
					+ ", descimg=" + descimg + ", needmoney=" + needmoney + "]";
		}
		
	}

}
