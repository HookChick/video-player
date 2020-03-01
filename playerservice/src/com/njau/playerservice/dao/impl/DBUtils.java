package com.njau.playerservice.dao.impl;

import java.io.IOException;
import java.util.Properties;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;

import org.apache.tomcat.dbcp.dbcp.BasicDataSourceFactory;

public class DBUtils {
	
	private static DataSource dataSource = null;
	
	static {
		try {
			Context context = new InitialContext();
			Object obj = context.lookup("jdbc/player");
			if (null != obj) {
				dataSource = (DataSource) obj;
			}else{
				System.out.println("null == obj上下文中找不到数据源");
			}
		} catch (NamingException e) {
			// 上下文中找不到数据源
			System.out.println("上下文中找不到数据源");
			// e.printStackTrace();
			// 尝试从配置文件中去找
			Properties pro = new Properties();
			try {
				pro.load(DBUtils.class.getResourceAsStream("/jdbc.properties"));
			} catch (IOException e1) {
				// 在根路径下找不到配置的资源文件
				System.out.println("在根路径下找不到配置的资源文件");
				e1.printStackTrace();
			}
			try {
				dataSource = BasicDataSourceFactory.createDataSource(pro);
			} catch (Exception e1) {
				// 创建数据源失败
				System.out.println("创建数据源失败");
				e1.printStackTrace();
			}
		}
	}

	public static DataSource getDataSource() {
		if (null != dataSource) {
			return dataSource;
		} else {
			throw new RuntimeException("数据源创建失败");
		}

	}
}
