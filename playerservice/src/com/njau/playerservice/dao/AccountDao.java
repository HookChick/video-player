package com.njau.playerservice.dao;

public interface AccountDao {

	/**
	 * �����û���id��ȡ�û������
	 * @param uid
	 * @return
	 */
	float getBalanceByUserid(int uid);

}
