package com.njau.playerservice.dao;

import java.util.List;

import com.njau.playerservice.entity.Video;

public interface VideoDao {

	/**
	 * 获取所有的视频文件
	 * @return
	 */
	public List<Video> getVideos();
}
