package com.njau.playerservice.dao;

import java.util.List;

import com.njau.playerservice.entity.Video;

public interface VideoDao {

	/**
	 * ��ȡ���е���Ƶ�ļ�
	 * @return
	 */
	public List<Video> getVideos();
}
