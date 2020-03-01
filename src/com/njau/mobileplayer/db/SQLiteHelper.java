package com.njau.mobileplayer.db;

import java.text.SimpleDateFormat;
import java.util.Date;

import com.iflytek.cloud.record.c;
import com.njau.mobileplayer.entity.VideoItem;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class SQLiteHelper extends SQLiteOpenHelper {

	/**
	 * 数据库名称  
	 */
	private static final String DATABASE_NAME="historylist.db";
	
	/**
	 * 版本号,则是升级之后的,升级方法请看onUpgrade方法里面的判断  
	 */
    private static final int MyDB_VERSION=1;
      
    public SQLiteHelper(Context context) { //构造函数,接收上下文作为参数,直接调用的父类的构造函数  
        super(context, DATABASE_NAME, null, MyDB_VERSION);  
    }  
      
    /**
     * 创建一个视频信息的表
     */
    @Override  
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table if not exists playhistory(_id INTEGER PRIMARY KEY AUTOINCREMENT,_VIDEONAME VARCHAR(20),_playedtimestring VARCHAR(20),_imgurl VARCHAR(60),_url VARCHAR(60));");
		
    }  
  
    /**
     * 维护版本
     */
    @Override  
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {  
//    	db.execSQL("DROP TABLE IF EXISTS playhistory");
//    	db.execSQL("create table if not exists playhistory(_id INTEGER PRIMARY KEY AUTOINCREMENT,_VIDEONAME VARCHAR(20),_playedtimestring VARCHAR(20),_imgurl VARCHAR(60),_url VARCHAR(60));");
    }  
  
    /**
     * 查询所有视频的方法
     * @param where
     * @param orderBy
     * @return
     */
    public Cursor getAll() {
        String sql = "SELECT _id, _VIDEONAME, _playedtimestring,_imgurl,_url FROM playhistory";  
          
        return(getReadableDatabase().rawQuery(sql, null));  
    }  
      
    /**
     * 根据视频的id获取对应视频
     * @param id
     * @return
     */
    public Cursor getById(String id) {//根据点击事件获取id,查询数据库  
        String[] args={id}; 
        String sql = "SELECT _id, _VIDEONAME, _playedtimestring, _imgurl, _url FROM playhistory WHERE _id=?";
        
        return getReadableDatabase().rawQuery(sql, args);  
    }  
    
    /**
     * 根据视频的名称获取视频
     * @param videoName
     * @return
     */
    public VideoItem getByVideoName(String videoName) { 
    	String[] args={videoName}; 
    	VideoItem item = null;
    	String sql = "SELECT _id, _VIDEONAME, _playedtimestring, _imgurl, _url FROM playhistory WHERE _VIDEONAME=?";
    	
    	Cursor cursor = getReadableDatabase().rawQuery(sql, args);  
    	if(cursor.moveToNext()){
    		item = new VideoItem();
    		item.setId(cursor.getInt(cursor.getColumnIndex("_id")));
    		item.setVideoName(cursor.getString(cursor.getColumnIndex("_VIDEONAME")));
    		item.setSummary(cursor.getString(cursor.getColumnIndex("_playedtimestring")));
    		item.setImgUrl(cursor.getString(cursor.getColumnIndex("_imgurl")));
    		item.setData(cursor.getString(cursor.getColumnIndex("_url")));
    	}
    	return item;
    }  
      
    /**
     * 插入数据
     * @param videoName  视频的名称
     */
    public void insert(String videoName,String imgurl,String url) {  
    	VideoItem videoItem = getByVideoName(videoName);
    	Date date = new Date();
		String dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(date);
		if(videoItem!=null && videoItem.getVideoName()!=null && !"".equals(videoItem.getVideoName())){
			this.update(videoItem.getId()+"");
			return;
		}
        ContentValues contentValues = new ContentValues();  
        contentValues.put("_VIDEONAME", videoName);  
        contentValues.put("_playedtimestring", dateFormat);  
        contentValues.put("_imgurl", imgurl);  
        contentValues.put("_url", url);  
          
        getWritableDatabase().insert("playhistory", null , contentValues);  
    }  
      
    /**
     * 根据id 修改观看时间
     * @param id
     * @param videoName
     */
    public void update(String id) {  
        ContentValues contentValues=new ContentValues();  
        String[] args={id};  
        Date date = new Date();
		String dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(date);
        contentValues.put("_playedtimestring", dateFormat);  
        
        getWritableDatabase().update("playhistory" , contentValues, "_id=?", args);  
    }

    /**
     * 删除所有的记录
     */
	public void deleteAll() {
		
		getWritableDatabase().delete("playhistory", null, null);
	}  
      
      
}  
