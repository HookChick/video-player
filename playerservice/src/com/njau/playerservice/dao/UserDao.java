package com.njau.playerservice.dao;

import com.njau.playerservice.entity.User;

public interface UserDao {

	/**
	 * 根据姓名和密码获取用户
	 * @param user
	 * @return
	 */
	User getUser(User user);
	
	/**
	 * 用户注册时插入数据库
	 * @param user
	 * @return
	 */
	int insertInto(User user);

	/**
	 * 处理用户充值的方法
	 * @param isHasBalance true表示在account表里面有记录，则更新，否则插入一条记录
	 * @param money
	 * @param uid 当前用户的id
	 * @return
	 */
	boolean doChongzhi(boolean isHasBalance, float money, int uid);
}
