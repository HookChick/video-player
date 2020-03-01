package com.njau.playerservice.dao;

import java.util.List;

import com.njau.playerservice.entity.PayItem;

public interface PayItemDao {

	/**
	 * 支付的方法返回是否成功结果给业务类
	 * 需要开启事物，更新用户账户余额同时更新管理员方的余额，另外购买成功了需要在payItem表中插入一条记录
	 * @param uid
	 * @param videoid
	 * @param money
	 * @return
	 */
	boolean doPay(int uid, String videoid, float money);

	/**
	 * 判断是否付款
	 * @param uid
	 * @param videoid
	 * @return
	 */
	boolean isPayed(int uid, String videoid);

	/**
	 * 获取当前用户的购买项
	 * @param uid
	 * @return
	 */
	List<PayItem> getItems(int uid);

}
