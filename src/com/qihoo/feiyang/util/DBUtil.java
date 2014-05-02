package com.qihoo.feiyang.util;
import java.util.List;

import com.qihoo.yunpan.sdk.android.http.model.YunFile;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;

public class DBUtil {
	private static MySQLiteHelper myHelper=null;
	private static final String dbname="feiyangDB.db";
	// table fileNodes field name
	private static final String KEY_ID = "_id";
	private static final String KEY_NAME = "name";
	private static final String KEY_FULLNAME = "fullname";
	private static final String KEY_NID = "nid";
	private static final String KEY_PID = "pid";
	private static final String KEY_CREATETIME = "createtime";
	private static final String KEY_MODIFYTIME = "modifytime";
	private static final String KEY_UPLOADTIME = "uploadtime";
	private static final String KEY_COUNTSIZE = "countsize";
	private static final String KEY_FILEHASH = "filehash";
	private static final String KEY_VERSION = "version";
	private static final String KEY_NV = "nv";
	private static final String KEY_TYPE = "type";
	private static final String KEY_FILECATEGORY = "filecategory";
	private static final String KEY_UPDATEKEY = "update_key";
	private static final String DB_TABLE_FILE = "fileNodes";
	
	// table config field name
	private static final String KEY = "key";
	private static final String VALUE = "value";
	private static final String DB_TABLE_CONFIG = "config";
	
	
	
	
	private static final String DB_CREATE_FILE = "CREATE TABLE " + DB_TABLE_FILE + " ( " + 
			KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
			KEY_NAME + " TEXT, " + 
			KEY_FULLNAME + " TEXT, " + 
			KEY_NID + " TEXT, " + 
			KEY_PID + " TEXT, " +
			KEY_CREATETIME + " INTEGER, " + 
			KEY_MODIFYTIME + " INTEGER, " +
			KEY_UPLOADTIME + " TEXT, " + 
			KEY_COUNTSIZE + " TEXT, " + 
			KEY_FILEHASH + " TEXT, " + 
			KEY_VERSION + " INTEGER, " + 
			KEY_NV + " TEXT, " + 
			KEY_TYPE + " INTEGER, " + 
			KEY_FILECATEGORY + " INTEGER, " + 
			KEY_UPDATEKEY + " TEXT " + ")";
	
	private static final String DB_CREATE_CONFIG = "CREATE TABLE " + DB_TABLE_CONFIG + " ( " + 
			KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
			KEY + " TEXT, " + 
			VALUE + " TEXT " + ")";
	
	private static class MySQLiteHelper extends SQLiteOpenHelper{

		public MySQLiteHelper(Context context, String name,
				CursorFactory factory, int version) {
			super(context, name, factory, version);
			// TODO Auto-generated constructor stub
		}
		
		
		@Override
		public void onCreate(SQLiteDatabase db) {
			//建表
			System.out.println("create table: " + DB_CREATE_FILE + " ; "+ DB_CREATE_CONFIG);
			
			db.execSQL(DB_CREATE_FILE);
			db.execSQL(DB_CREATE_CONFIG);
		}

		@Override
		public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
			// TODO Auto-generated method stub
			//db.execSQL("DROP TABLE IF EXISTS " + DB_TABLE);
			//onCreate(db);
		}
		
	}
	
	public static void init(Context context,int version){
		
		System.out.println("database initial");
		
		if(myHelper==null){
			myHelper=new MySQLiteHelper(context, dbname, null, version);
		}
	}
	
	
	//read 
	public static String getKey(String key){
		SQLiteDatabase db=myHelper.getReadableDatabase();
		
		String value=null;
		//...
		Cursor cursor = db.query(DB_TABLE_CONFIG, new String[] {"value"}, "key=?", new String[]{key}, null, null, null);  
		
		while (cursor.moveToNext()) {  
            value = cursor.getString(cursor.getColumnIndex("value"));  
        } 
		cursor.close();
		db.close();
		return value;
	}
	
	public static String getQid(){
		return getKey("qid");
	}
	
	public static Cursor getCursorOfAllFile() {
		return null;
	}
	
	public static Cursor getCursorFilesInDirectory(String pid) {
		return null;
	}
	
	public static List<YunFile> getYunFileOfAllFile() {
		return null;
	}
	
	public static List<YunFile> getYunFilesInDirectory(String pid) {
		return null;
	}
	
	public static Cursor getCursorOfAllPictures() {
		return null;
	}
	
	public static Cursor getCurosrFilesInDirectory(String pid) {
		return null;
	}
	
	public static List<YunFile> getYunFileOfAllFiles() {
		return  null;
	}
	
	public static List<YunFile> getYunFilesInDirectory() {
		return null;
	}
	
	
	
	//write
	
	public static void saveKey(String key,String value){
		SQLiteDatabase db=myHelper.getWritableDatabase();
		//write key
		
		ContentValues values = new ContentValues();  
        values.put("key", key);  
        values.put("value", value);  
        
        Cursor cursor = db.query(DB_TABLE_CONFIG, new String[] {"value"}, "key=?", new String[]{key}, null, null, null); 
        
        if(cursor==null||cursor.getCount()==0){
        	db.insert(DB_TABLE_CONFIG, null, values);
        }else{
        	db.update(DB_TABLE_CONFIG, values, "key=?",  new String[]{key});
        }
        cursor.close();
		db.close();
		
	}
	
	public static void saveQid(String qid){
		saveKey("qid", qid);
	}
	
	public static long insertData(YunFile file) {
		return 0;
	}
	
	public static boolean deleteData(String nid) {
		return false;
	}
	
	
	public static boolean updateFile(String pid, String fileName) {
		return false;
	}
	
	
	
}
