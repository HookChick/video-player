package com.njau.playerservice.service;

import java.util.List;

import com.njau.playerservice.entity.PayItem;


public interface PayItemService {

	/**
	 * 用户支付时完成操作
	 * @param userid 付款用户的id
	 * @param videoid 用户付款的视频id
	 * @param needmoney 用户付款的钱数
	 * @return 返回是否成功给服务器,1代表余额不足，2代表成功，3代表失败
	 */
	int doPay(String userid, String videoid, String needmoney);

	/**
	 * 根据用户id和视频id判断该用户试图观看的视频有没有付过费用
	 * @param userid 用户的id
	 * @param videoid 视频的id
	 * @return
	 */
	boolean isPayType(String userid, String videoid);

	/**
	 * 根据用户id获取当前用户的购买项
	 * @param userid
	 * @return
	 */
	List<PayItem> getPayItemsByUid(String userid);

}
