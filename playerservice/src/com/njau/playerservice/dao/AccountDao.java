package com.njau.playerservice.dao;

public interface AccountDao {

	/**
	 * 根据用户的id获取用户的余额
	 * @param uid
	 * @return
	 */
	float getBalanceByUserid(int uid);

}
