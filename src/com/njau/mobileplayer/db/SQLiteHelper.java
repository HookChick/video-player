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
	 * ���ݿ�����  
	 */
	private static final String DATABASE_NAME="historylist.db";
	
	/**
	 * �汾��,��������֮���,���������뿴onUpgrade����������ж�  
	 */
    private static final int MyDB_VERSION=1;
      
    public SQLiteHelper(Context context) { //���캯��,������������Ϊ����,ֱ�ӵ��õĸ���Ĺ��캯��  
        super(context, DATABASE_NAME, null, MyDB_VERSION);  
    }  
      
    /**
     * ����һ����Ƶ��Ϣ�ı�
     */
    @Override  
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table if not exists playhistory(_id INTEGER PRIMARY KEY AUTOINCREMENT,_VIDEONAME VARCHAR(20),_playedtimestring VARCHAR(20),_imgurl VARCHAR(60),_url VARCHAR(60));");
		
    }  
  
    /**
     * ά���汾
     */
    @Override  
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {  
//    	db.execSQL("DROP TABLE IF EXISTS playhistory");
//    	db.execSQL("create table if not exists playhistory(_id INTEGER PRIMARY KEY AUTOINCREMENT,_VIDEONAME VARCHAR(20),_playedtimestring VARCHAR(20),_imgurl VARCHAR(60),_url VARCHAR(60));");
    }  
  
    /**
     * ��ѯ������Ƶ�ķ���
     * @param where
     * @param orderBy
     * @return
     */
    public Cursor getAll() {
        String sql = "SELECT _id, _VIDEONAME, _playedtimestring,_imgurl,_url FROM playhistory";  
          
        return(getReadableDatabase().rawQuery(sql, null));  
    }  
      
    /**
     * ������Ƶ��id��ȡ��Ӧ��Ƶ
     * @param id
     * @return
     */
    public Cursor getById(String id) {//���ݵ���¼���ȡid,��ѯ���ݿ�  
        String[] args={id}; 
        String sql = "SELECT _id, _VIDEONAME, _playedtimestring, _imgurl, _url FROM playhistory WHERE _id=?";
        
        return getReadableDatabase().rawQuery(sql, args);  
    }  
    
    /**
     * ������Ƶ�����ƻ�ȡ��Ƶ
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
     * ��������
     * @param videoName  ��Ƶ������
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
     * ����id �޸Ĺۿ�ʱ��
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
     * ɾ�����еļ�¼
     */
	public void deleteAll() {
		
		getWritableDatabase().delete("playhistory", null, null);
	}  
      
      
}  
