package com.njau.playerservice.dao.impl;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.njau.playerservice.dao.BaseDAO;
import com.njau.playerservice.dao.VideoDao;
import com.njau.playerservice.entity.Video;

public class VideoDaoImpl extends BaseDAO implements VideoDao {

	/**
	 * 获取得到所有的Video数据
	 * @return
	 */
	public List<Video> getVideos(){
		List<Video> videos = new ArrayList<Video>();
		String sql = "select * from video";
		ResultSet resultSet = this.querryForObject(sql, null);
		try {
			while (resultSet.next()) {
				Video video = new Video();
				video.setId(resultSet.getString(1));
				video.setVideoname(resultSet.getString(2));
				video.setVideosrc(resultSet.getString(3));
				video.setVideodesc(resultSet.getString(4));
				video.setDescimg(resultSet.getString(5));
				video.setNeedmoney(resultSet.getFloat(6));
				videos.add(video);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return videos;
	}
}
