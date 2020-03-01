package com.njau.playerservice.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.njau.playerservice.dao.impl.DBUtils;

public abstract class BaseDAO {

	private ThreadLocal<Connection> localConnection = new ThreadLocal<Connection>();

	public Connection getConnection() {
		Connection con = null;
		if (localConnection.get() == null) {
			try {
				con = DBUtils.getDataSource().getConnection();
			} catch (SQLException e) {
				e.printStackTrace();
			}
			localConnection.set(con);
		} else {
			con = localConnection.get();
		}
		return con;
	}

	public ResultSet querryForObject(String sql, Object... args) {
		PreparedStatement preparedStatement = null;
		ResultSet resultSet = null;
		Connection connection = this.getConnection();
		try {
			preparedStatement = connection.prepareStatement(sql);
			if(args != null && args.length>0){
				for (int i = 0; i < args.length; i++) {
					preparedStatement.setObject(i + 1, args[i]);
				}
			}
			resultSet = preparedStatement.executeQuery();
		} catch (SQLException e) {
			// 执行查询出错
			e.printStackTrace();
		}
		
		this.closeAll(null, null, null);
		
		return resultSet;
	}

	public int updateForObject(String sql, Object... args) {
		int row = 0;
		PreparedStatement preparedStatement = null;
		ResultSet resultSet = null;
		Connection connection = this.getConnection();
		try {
			preparedStatement = connection.prepareStatement(sql);
			for (int i = 0; i < args.length; i++) {
				preparedStatement.setObject((i + 1), args[i]);
			}
			row = preparedStatement.executeUpdate();
			this.closeAll(resultSet, preparedStatement, connection);
		} catch (SQLException e) {
			// 执行更新失败
			e.printStackTrace();
		}
		return row;
	}

	public void closeAll(ResultSet resultSet,
			PreparedStatement preparedStatement, Connection connection) {
		try {
			if (null != resultSet) {
				resultSet.close();
			}
			if (null != preparedStatement) {
				preparedStatement.close();
			}
			if (null != connection) {
				connection.close();
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			localConnection.remove();
		}
	}
}
