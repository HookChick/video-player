package com.njau.mobileplayer.entity;

import java.io.Serializable;
import java.util.List;

import com.iflytek.cloud.thirdparty.v;

/**
 * ��Ƶ�ļ���Ϣ����װ���Ӧ��bean��
 * @author zhangcan
 *
 */
public class VideoItem implements Serializable{

	private static final long serialVersionUID = 5333552228544585003L;
	
	private String videoName; //��Ƶ������
	private long duration; //��Ƶ�ļ���ʱ��
	private long size; //��Ƶ�ļ���С
	private String data; //��Ƶ�ļ��ľ��Ե�ַ,���ŵ�ַ
	
	private String description;//��Ƶ��������Ϣ
	private String imgUrl;//��Ƶ��ͼƬ��ַ
	
	private String summary;//��Ƶ�ĸ�Ҫ
	private float rating; //����
	
	/**
	 * ����
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
