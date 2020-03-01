package com.njau.playerservice.service.impl;

import java.util.List;

import com.njau.playerservice.dao.AccountDao;
import com.njau.playerservice.dao.PayItemDao;
import com.njau.playerservice.dao.impl.AccountDaoImpl;
import com.njau.playerservice.dao.impl.PayItemDaoImpl;
import com.njau.playerservice.entity.PayItem;
import com.njau.playerservice.service.PayItemService;

public class PayItemServiceImpl implements PayItemService {

	private PayItemDao payItemDao;
	private AccountDao accountDao;
	
	
	public PayItemServiceImpl() {
		payItemDao = new PayItemDaoImpl();
		accountDao = new AccountDaoImpl();
	}
	
	@Override
	public int doPay(String userid, String videoid, String needmoney) {
		int code = -1;
		int uid = Integer.parseInt(userid);
		float money = Float.parseFloat(needmoney);
		float balance = accountDao.getBalanceByUserid(uid);
		if(balance - money < 0){
			//Óà¶î²»×ã
			code = 1;
		}else{
			boolean isSuccess = payItemDao.doPay(uid,videoid,money);
			if(isSuccess){ //³É¹¦
				code = 2;
			}else{
				code = 3;
			}
		}
		
		return code;
	}

	@Override
	public boolean isPayType(String userid, String videoid) {
		int uid = Integer.parseInt(userid);
		boolean isPay = payItemDao.isPayed(uid,videoid);
		return isPay;
	}

	@Override
	public List<PayItem> getPayItemsByUid(String userid) {
		int uid = Integer.parseInt(userid);
		List<PayItem> items = payItemDao.getItems(uid);
		return items;
	}

}
