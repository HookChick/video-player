package com.njau.playerservice.entity;

public class Video {

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
		return "Video [id=" + id + ", videoname=" + videoname + ", videosrc="
				+ videosrc + ", videodesc=" + videodesc + ", descimg="
				+ descimg + ", needmoney=" + needmoney + "]";
	}

}
