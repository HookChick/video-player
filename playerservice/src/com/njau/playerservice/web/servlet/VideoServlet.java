package com.njau.playerservice.web.servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.lang.reflect.Method;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.njau.playerservice.dao.AccountDao;
import com.njau.playerservice.dao.PayItemDao;
import com.njau.playerservice.dao.VideoDao;
import com.njau.playerservice.dao.impl.AccountDaoImpl;
import com.njau.playerservice.dao.impl.VideoDaoImpl;
import com.njau.playerservice.dao.impl.PayItemDaoImpl;
import com.njau.playerservice.entity.PayItem;
import com.njau.playerservice.entity.Video;
import com.njau.playerservice.service.PayItemService;
import com.njau.playerservice.service.impl.PayItemServiceImpl;

public class VideoServlet extends HttpServlet {

	/**
	 * 
	 */
	private static final long serialVersionUID = -7104114181802317736L;
	
	private VideoDao videoDao;
	private PayItemService payItemService;
	private AccountDao accountDao;
	
	public VideoServlet(){
		//��ʼ��
		videoDao = new VideoDaoImpl();
		payItemService = new PayItemServiceImpl();
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
	 * ���ͻ��˵��vip��Ƶר��ʱ������е���Ƶ�ļ�,ͬʱת��Ϊjson�ַ������ظ��ͻ���
	 * @param req
	 * @param resp
	 * @throws ServletException
	 * @throws IOException
	 */
	protected void getAllVideos(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		List<Video> videos = videoDao.getVideos();
		//��װ��json��������
		StringBuilder json = new StringBuilder();
        json.append("[");
        if (videos != null && videos.size() > 0) {
            for (Object video : videos) {
            	Gson gson = new Gson();
				String jsonvideo = gson.toJson(video);
                json.append(jsonvideo);
                json.append(",");
            }
            json.setCharAt(json.length() - 1, ']');
        } else {
            json.append("]");
        }
        
		resp.setContentType("application/json;charset=utf-8");
        PrintWriter out = resp.getWriter();
        out.print(json);
        
	}
	
	/**
	 * ����֧��Ǯʱ�ķ�����ͬʱ�������ݸ��ͻ�����Ӧ
	 * @param req
	 * @param resp
	 * @throws ServletException
	 * @throws IOException
	 */
	protected void pay(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		String userid = req.getParameter("userid");
		String videoid = req.getParameter("videoid");
		String needmoney = req.getParameter("needmoney");
		
		//1�������㣬2����ɹ���3����ʧ��
		int code = payItemService.doPay(userid,videoid,needmoney);
		
		resp.setCharacterEncoding("UTF-8");
		PrintWriter out = resp.getWriter();
		if(code==1){
			//����
			out.print("notenough");
		}else if(code == 2){
			//�ɹ�
			out.print("success");
		}else if(code==3){
			//ʧ��
			out.print("failed");
		}
	}
	
	/**
	 * ������������Ƶ��û�и���������˾���ͻ��˷���true
	 * @param req
	 * @param resp
	 * @throws ServletException
	 * @throws IOException
	 */
	protected void checkPayed(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		String userid = req.getParameter("userid");
		String videoid = req.getParameter("videoid");
		
		boolean isPayed = payItemService.isPayType(userid,videoid);
		
		resp.setCharacterEncoding("UTF-8");
		PrintWriter out = resp.getWriter();
		if(isPayed){
			out.print("true");
		}else{
			out.print("false");
		}
		
	}
	
	/**
	 * ��ʾ�û����ķ���
	 * @param req
	 * @param resp
	 * @throws ServletException
	 * @throws IOException
	 */
	protected void showBalance(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		
		String userid = req.getParameter("userid");
		int uid = Integer.parseInt(userid);
		
		float balance = accountDao.getBalanceByUserid(uid);
		if(balance<0){
			balance = 0;
		}
		resp.setCharacterEncoding("UTF-8");
		PrintWriter out = resp.getWriter();
		out.print(balance);
	}
	
	/**
	 * �鿴��ǰ�û������¼
	 * @param req
	 * @param resp
	 * @throws ServletException
	 * @throws IOException
	 */
	protected void showPayItem(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		String userid = req.getParameter("userid");
		
		List<PayItem> items = payItemService.getPayItemsByUid(userid);
		
        StringBuilder json = new StringBuilder();
        json.append("[");
		if(items!=null && items.size()>0){
			for (PayItem item : items) {
            	Gson gson = new Gson();
				String jsonItem = gson.toJson(item);
                json.append(jsonItem);
                json.append(",");
            }
            json.setCharAt(json.length() - 1, ']');
		}else{
			json.append("]");
		}
		
		resp.setContentType("application/json;charset=utf-8");
		PrintWriter out = resp.getWriter();
		out.print(json.toString());
		
	}
}
