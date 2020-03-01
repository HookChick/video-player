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
				System.out.println("null == obj���������Ҳ�������Դ");
			}
		} catch (NamingException e) {
			// ���������Ҳ�������Դ
			System.out.println("���������Ҳ�������Դ");
			// e.printStackTrace();
			// ���Դ������ļ���ȥ��
			Properties pro = new Properties();
			try {
				pro.load(DBUtils.class.getResourceAsStream("/jdbc.properties"));
			} catch (IOException e1) {
				// �ڸ�·�����Ҳ������õ���Դ�ļ�
				System.out.println("�ڸ�·�����Ҳ������õ���Դ�ļ�");
				e1.printStackTrace();
			}
			try {
				dataSource = BasicDataSourceFactory.createDataSource(pro);
			} catch (Exception e1) {
				// ��������Դʧ��
				System.out.println("��������Դʧ��");
				e1.printStackTrace();
			}
		}
	}

	public static DataSource getDataSource() {
		if (null != dataSource) {
			return dataSource;
		} else {
			throw new RuntimeException("����Դ����ʧ��");
		}

	}
}
