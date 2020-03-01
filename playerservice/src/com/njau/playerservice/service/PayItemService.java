package com.njau.playerservice.service;

import java.util.List;

import com.njau.playerservice.entity.PayItem;


public interface PayItemService {

	/**
	 * �û�֧��ʱ��ɲ���
	 * @param userid �����û���id
	 * @param videoid �û��������Ƶid
	 * @param needmoney �û������Ǯ��
	 * @return �����Ƿ�ɹ���������,1�������㣬2����ɹ���3����ʧ��
	 */
	int doPay(String userid, String videoid, String needmoney);

	/**
	 * �����û�id����Ƶid�жϸ��û���ͼ�ۿ�����Ƶ��û�и�������
	 * @param userid �û���id
	 * @param videoid ��Ƶ��id
	 * @return
	 */
	boolean isPayType(String userid, String videoid);

	/**
	 * �����û�id��ȡ��ǰ�û��Ĺ�����
	 * @param userid
	 * @return
	 */
	List<PayItem> getPayItemsByUid(String userid);

}
