package com.njau.playerservice.web.servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.lang.reflect.Method;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gson.Gson;
import com.njau.playerservice.dao.AccountDao;
import com.njau.playerservice.dao.UserDao;
import com.njau.playerservice.dao.impl.AccountDaoImpl;
import com.njau.playerservice.dao.impl.UserDaoImpl;
import com.njau.playerservice.entity.User;

public class UserServlet extends HttpServlet {

	private static final long serialVersionUID = -445744296893887417L;
	
	private UserDao userDao;
	private AccountDao accountDao;
	
	public UserServlet(){
		//��ʼ��
		userDao = new UserDaoImpl();
		accountDao = new AccountDaoImpl();
	}
	
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		this.doPost(req, resp);
	}
	
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		String methodName = req.getParameter("method");
		try {
			Method method = getClass().getDeclaredMethod(methodName, HttpServletRequest.class, HttpServletResponse.class);
			method.setAccessible(true);
			method.invoke(this, req, resp);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}
	
	/**
	 * �����ֵ�ķ���
	 * @param req
	 * @param resp
	 * @throws ServletException
	 * @throws IOException
	 */
	protected void doChongzhi(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		String moneyStr = req.getParameter("money");
		String userid = req.getParameter("userid");
		int uid = Integer.parseInt(userid);
		float money = Float.parseFloat(moneyStr);
		
		float balance = accountDao.getBalanceByUserid(uid);
		boolean isHasBalance = false; //�ж��Ƿ���account�������м�¼���������,û�������һ����¼
		if(balance >= 0){
			isHasBalance = true;
		}
		
		boolean isSuccess = userDao.doChongzhi(isHasBalance,money,uid);
		
		resp.setCharacterEncoding("UTF-8");
		PrintWriter out = resp.getWriter();
		if(isSuccess){
			out.print("success");
		}else{
			out.print("fail");
		}
	}
	
	/**
	 * ����ע��ķ���
	 * @param req
	 * @param resp
	 * @throws ServletException
	 * @throws IOException
	 */
	protected void doRegister(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		String regusername = req.getParameter("regusername");
		String regpassword = req.getParameter("regpassword");
		
		regusername = new String(regusername.getBytes("ISO-8859-1"),"UTF-8");
		
		User user = new User(regusername, regpassword);
		resp.setCharacterEncoding("UTF-8");
		PrintWriter out = resp.getWriter();
		
		int code = userDao.insertInto(user);
		if(code>0){
			out.print("success");
		}else{
			out.print("fail");
		}
		
	}
	
	/**
	 * �����¼�ķ���
	 * @param req
	 * @param resp
	 * @throws ServletException
	 * @throws IOException
	 */
	protected void doLogin(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		
		String username = req.getParameter("username");
		String password = req.getParameter("password");
		
		username = new String(username.getBytes("ISO-8859-1"), "UTF-8");
		System.out.println("�û�����"+username);
		//��װ����
		User user = new User();
		user.setUsername(username);
		user.setPassword(password);
		User tempUser = userDao.getUser(user);
		resp.setCharacterEncoding("UTF-8");
//		resp.setContentType("application/json;charset=utf-8");
		PrintWriter out = resp.getWriter();
		
		if(null != tempUser){
			//��¼�ɹ�
			if(tempUser.getRole() == 1){ //����Ա�����շѷ��û�
				req.setAttribute("username", username);
				req.setAttribute("password", password);
				req.getRequestDispatcher("/WEB-INF/index.jsp").forward(req, resp);
				out.print("failed");
				
			}else{ //��ͨ�����ƶ����û�
				Gson gson = new Gson();
				String json = gson.toJson(tempUser);
				out.print(json);
			}
		}else{
			//��¼ʧ��
			out.print("failed");
		}
		out.flush();
		out.close();
		
	}

}
