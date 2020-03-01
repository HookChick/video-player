package com.njau.mobileplayer.entity;

import java.io.Serializable;
import java.util.List;

import com.iflytek.cloud.thirdparty.v;

/**
 * 视频文件信息被封装后对应的bean类
 * @author zhangcan
 *
 */
public class VideoItem implements Serializable{

	private static final long serialVersionUID = 5333552228544585003L;
	
	private String videoName; //视频的名称
	private long duration; //视频文件总时长
	private long size; //视频文件大小
	private String data; //视频文件的绝对地址,播放地址
	
	private String description;//视频的描述信息
	private String imgUrl;//视频的图片地址
	
	private String summary;//视频的概要
	private float rating; //评分
	
	/**
	 * 类型
	 */
	private List<String> type;
	
	
	@Override
	public boolean equals(Object obj) {
		if(obj instanceof VideoItem){
			VideoItem video = (VideoItem) obj;
			if(videoName.equals(video.getVideoName()) &&  duration == video.getDuration() &&
					data.equals(video.getData()) && description.equals(video.getDescription())
							&& imgUrl.equals(video.getImgUrl()) && summary.equals(video.getSummary())
									&&rating == video.getRating() && type == video.getType()){
				return true;
			}else{
				return false;
			}
		}else{
			return false;
		}
		
	}

	public float getRating() {
		return rating;
	}
	
	private int id;
	

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public void setRating(float rating) {
		this.rating = rating;
	}

	public String getSummary() {
		return summary;
	}

	public void setSummary(String summary) {
		this.summary = summary;
	}

	public List<String> getType() {
		return type;
	}

	public void setType(List<String> type) {
		this.type = type;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getImgUrl() {
		return imgUrl;
	}

	public void setImgUrl(String imgUrl) {
		this.imgUrl = imgUrl;
	}

	public String getVideoName() {
		return videoName;
	}
	
	public void setVideoName(String videoName) {
		this.videoName = videoName;
	}
	
	public long getDuration() {
		return duration;
	}
	
	public void setDuration(long duration) {
		this.duration = duration;
	}
	
	public long getSize() {
		return size;
	}
	
	public void setSize(long size) {
		this.size = size;
	}
	
	public String getData() {
		return data;
	}
	
	public void setData(String data) {
		this.data = data;
	}
	
	
	@Override
	public String toString() {
		return "VideoItem [videoName=" + videoName + ", duration=" + duration
				+ ", size=" + size + ", data=" + data + ", description="
				+ description + ", imgUrl=" + imgUrl + "]";
	}

	public VideoItem() {
		super();
	}
	
	public VideoItem(String videoName, long duration, long size, String data) {
		super();
		this.videoName = videoName;
		this.duration = duration;
		this.size = size;
		this.data = data;
	}
	
}
