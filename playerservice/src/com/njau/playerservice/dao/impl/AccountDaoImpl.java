package com.njau.playerservice.dao.impl;

import java.sql.ResultSet;
import java.sql.SQLException;

import com.njau.playerservice.dao.AccountDao;
import com.njau.playerservice.dao.BaseDAO;

public class AccountDaoImpl extends BaseDAO implements AccountDao {

	@Override
	public float getBalanceByUserid(int uid) {
		float balance = -1;
		String sql = "select balance from account where userid=?";
		ResultSet resultSet = this.querryForObject(sql, uid);
		try {
			if(resultSet.next()){
				balance = resultSet.getFloat(1);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return balance;
	}

}
