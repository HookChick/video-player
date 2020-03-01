package com.njau.playerservice.dao;

import java.util.List;

import com.njau.playerservice.entity.PayItem;

public interface PayItemDao {

	/**
	 * ֧���ķ��������Ƿ�ɹ������ҵ����
	 * ��Ҫ������������û��˻����ͬʱ���¹���Ա���������⹺��ɹ�����Ҫ��payItem���в���һ����¼
	 * @param uid
	 * @param videoid
	 * @param money
	 * @return
	 */
	boolean doPay(int uid, String videoid, float money);

	/**
	 * �ж��Ƿ񸶿�
	 * @param uid
	 * @param videoid
	 * @return
	 */
	boolean isPayed(int uid, String videoid);

	/**
	 * ��ȡ��ǰ�û��Ĺ�����
	 * @param uid
	 * @return
	 */
	List<PayItem> getItems(int uid);

}
