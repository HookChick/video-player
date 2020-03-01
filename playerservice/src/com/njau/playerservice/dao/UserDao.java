package com.njau.playerservice.dao;

import com.njau.playerservice.entity.User;

public interface UserDao {

	/**
	 * ���������������ȡ�û�
	 * @param user
	 * @return
	 */
	User getUser(User user);
	
	/**
	 * �û�ע��ʱ�������ݿ�
	 * @param user
	 * @return
	 */
	int insertInto(User user);

	/**
	 * �����û���ֵ�ķ���
	 * @param isHasBalance true��ʾ��account�������м�¼������£��������һ����¼
	 * @param money
	 * @param uid ��ǰ�û���id
	 * @return
	 */
	boolean doChongzhi(boolean isHasBalance, float money, int uid);
}
