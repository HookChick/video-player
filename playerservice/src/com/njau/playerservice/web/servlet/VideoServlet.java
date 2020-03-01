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
		//初始化
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
	 * 当客户端点击vip视频专区时获得所有的视频文件,同时转换为json字符串返回给客户端
	 * @param req
	 * @param resp
	 * @throws ServletException
	 * @throws IOException
	 */
	protected void getAllVideos(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		List<Video> videos = videoDao.getVideos();
		//封装成json对象数组
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
	 * 处理支付钱时的方法。同时返回数据给客户端响应
	 * @param req
	 * @param resp
	 * @throws ServletException
	 * @throws IOException
	 */
	protected void pay(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		String userid = req.getParameter("userid");
		String videoid = req.getParameter("videoid");
		String needmoney = req.getParameter("needmoney");
		
		//1代表余额不足，2代表成功，3代表失败
		int code = payItemService.doPay(userid,videoid,needmoney);
		
		resp.setCharacterEncoding("UTF-8");
		PrintWriter out = resp.getWriter();
		if(code==1){
			//余额不足
			out.print("notenough");
		}else if(code == 2){
			//成功
			out.print("success");
		}else if(code==3){
			//失败
			out.print("failed");
		}
	}
	
	/**
	 * 检查所点击的视频有没有付过款，付款了就向客户端返回true
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
	 * 显示用户余额的方法
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
	 * 查看当前用户购买记录
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
