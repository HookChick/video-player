package com.njau.playerservice.dao.impl;

import java.sql.ResultSet;
import java.sql.SQLException;

import com.njau.playerservice.dao.BaseDAO;
import com.njau.playerservice.dao.UserDao;
import com.njau.playerservice.entity.User;

public class UserDaoImpl extends BaseDAO implements UserDao {

	@Override
	public User getUser(User user) {
		User tempUser;
		String sql = "select * from users where uname=? and password=?";
		ResultSet resultSet = this.querryForObject(sql, user.getUsername(),user.getPassword());
		try {
			if(resultSet.next()){
				tempUser = new User();
				tempUser.setUid(resultSet.getInt(1));
				tempUser.setUsername(resultSet.getString(2));
				tempUser.setPassword(resultSet.getString(3));
				tempUser.setRole(resultSet.getInt(4));
				
				return tempUser;
			}else{
				return null;
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return null;
	}

	@Override
	public int insertInto(User user) {
		int row = -1;
		String sql = "insert into users values(null,?,?,?)";
		row = this.updateForObject(sql, user.getUsername(),user.getPassword(),0);
		return row;
	}

	@Override
	public boolean doChongzhi(boolean isHasBalance, float money, int uid) {
		String sql = "";
		int row = -1;
		if(isHasBalance){
			sql = "update account set balance= balance +? where userid=?";
			row = this.updateForObject(sql, money,uid);
		}else{
			sql = "insert into account values(null,?,?)";
			row = this.updateForObject(sql, uid , money);
		}
		
		return row>0;
	}


}
