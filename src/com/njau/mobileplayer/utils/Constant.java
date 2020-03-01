package com.njau.mobileplayer.utils;

/**
 * 用于定义常量的工具类
 * @author zhangcan
 *
 */
public final class Constant {

	/**
	 * 连接电脑服务器的网段地址公共部分
	 */
	public static String URL_IP = "http://192.168.23.1:8080/playerservice/";
	
	/**
	 * 视频进度条的更新和播放时间显示更新的消息
	 */
	public static final int PROGRESS = 1;
	
	/**
	 * 隐藏视频控制面板的消息
	 */
	public static final int HIDE_MEDIA_CONTROLLER = 2;
	
	/**
	 * 显示网速
	 */
	public static final int SHOW_SPEED = 3;
	
	/**
	 * 默认屏幕
	 */
	public static final int DEFAULT_SCREEN = 1;
	
	/**
	 * 全屏
	 */
	public static final int FULL_SCREEN = 2;

	/**
	 * 网络视频的联网地址
	 */
	public static final String NET_URI = "http://api.m.mtime.cn/PageSubArea/TrailerList.api";
	
	/**
     * 搜索的路径
     */
    public static final String SEARCH_URL = "http://hot.news.cntv.cn/index.php?controller=list&action=searchList&sort=date&n=20&wd=";
	
    /**
     * 推荐的路径
     */
    public static final String RECOMMED_URL = "http://s.budejie.com/topic/list/jingxuan/1/budejie-android-6.2.8/0-20.json?market=baidu&udid=863425026599592&appname=baisibudejie&os=4.2.2&client=android&visiting=&mac=98%3A6c%3Af5%3A4b%3A72%3A6d&ver=6.2.8";
    
    private Constant(){
	};
	
}
