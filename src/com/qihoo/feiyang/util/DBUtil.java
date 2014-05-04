package com.qihoo.feiyang.util;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
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
	private static final String dbname="feiyangDB1.db";
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

		if(myHelper==null){
			System.out.println("database initial");
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
	
	/**
	 * 获取所有的文件
	 * @return
	 */
	public static List<YunFile> getYunFileOfAllFile() {
		return null;
	}
	
	/**
	 * 获取某个目录中所有文件
	 * @param pid
	 * @return
	 */
	public static List<YunFile> getYunFilesInDirectory(String pid) {
		return null;
	}
	
	/**
	 * 获取所有的图片
	 * @return
	 */
	public static List<YunFile> getAllPicturesOfAllFile() {
		SQLiteDatabase db = myHelper.getReadableDatabase();
		List<YunFile> picts = new ArrayList<YunFile>();
		Cursor cursor = db.query(DB_CREATE_FILE, new String[]{KEY_FULLNAME, KEY_NID, KEY_PID, KEY_FILEHASH}, 
				KEY_TYPE + "=" + "?", new String[]{"2"}, null, null, KEY_MODIFYTIME);
		int nameIndex = cursor.getColumnIndex(KEY_FULLNAME);
		int nidIndex = cursor.getColumnIndex(KEY_NID);
		int pidIndex = cursor.getColumnIndex(KEY_PID);
		int hashIndex = cursor.getColumnIndex(KEY_FILEHASH);
		
		for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()) {
			//
			YunFile f = new YunFile();
			f.name = cursor.getColumnName(nameIndex);
			f.nid = cursor.getColumnName(nidIndex);
			f.pid = cursor.getColumnName(pidIndex);
			f.file_hash = cursor.getColumnName(hashIndex);
			picts.add(f);
			
		}
		cursor.close();
		db.close();
		return picts;
	}
	
	/**
	 * 获取某个目录中的所有图片
	 * @param pid
	 * @return
	 */
	public static List<YunFile> getPicturesInDirectory(String pid) {
		SQLiteDatabase db = myHelper.getReadableDatabase();
		List<YunFile> picts = new ArrayList<YunFile>();
		Cursor cursor = db.query(DB_CREATE_FILE, new String[]{KEY_FULLNAME, KEY_NID, KEY_PID, KEY_FILEHASH}, 
				KEY_PID + "=? and " + KEY_TYPE +"=?", new String[]{pid, "2"}, null, null, KEY_MODIFYTIME);
		int nameIndex = cursor.getColumnIndex(KEY_FULLNAME);
		int nidIndex = cursor.getColumnIndex(KEY_NID);
		int pidIndex = cursor.getColumnIndex(KEY_PID);
		int hashIndex = cursor.getColumnIndex(KEY_FILEHASH);
		for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()) {
			YunFile f = new YunFile();
			f.name = cursor.getColumnName(nameIndex);
			f.nid = cursor.getColumnName(nidIndex);
			f.pid = cursor.getColumnName(pidIndex);
			f.file_hash = cursor.getColumnName(hashIndex);
			picts.add(f);
		}
		cursor.close();
		db.close();
		return picts;
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
	
	private static SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	
	public static long insertYunFile(YunFile file, boolean isPicture) {
		SQLiteDatabase db=myHelper.getWritableDatabase();
		
		if(isFileExist(db, file)) {
			return -1;
		}
		
		String fullName = file.name;
		String name = FileUtil.getFileSimpleName(fullName);
		String nid = file.nid;
		String pid = file.pid;
		long createtime = file.create_time;
		long modifytime = file.modify_time;
		String uploadtime = format.format(new Date(createtime * 1000));
		String countsize = file.count_size + "";
		String filehash = file.file_hash;
		int version = file.version;
		String nv = file.modify_time + "";
		int type = file.type;
		if (isPicture) type = 2;
		
		int filecategory = file.file_category;
		String update_key = "097c20c8481f0248e50cd056aa46b02c";
		
		ContentValues contentValues = new ContentValues();
		contentValues.put(KEY_NAME, name);
		contentValues.put(KEY_FULLNAME, fullName);
		contentValues.put(KEY_NID, nid);
		contentValues.put(KEY_PID, pid);
		contentValues.put(KEY_CREATETIME, createtime);
		contentValues.put(KEY_MODIFYTIME, modifytime);
		contentValues.put(KEY_UPLOADTIME, uploadtime);
		contentValues.put(KEY_COUNTSIZE, countsize);
		contentValues.put(KEY_FILEHASH, filehash);
		contentValues.put(KEY_VERSION, version);
		contentValues.put(KEY_NV, nv);
		contentValues.put(KEY_TYPE, type);
		contentValues.put(KEY_FILECATEGORY, filecategory);
		contentValues.put(KEY_UPDATEKEY, update_key);
		
		long result = db.insert(DB_CREATE_FILE, KEY_ID, contentValues);
		db.close();
		
		return result;
	}
	
	private static boolean isFileExist(SQLiteDatabase db, YunFile file) {
		boolean result = false;
		String nid = file.nid;
		
		Cursor cursor = db.query(DB_CREATE_FILE, new String[]{KEY_NAME, KEY_NID}, KEY_NID + "=?", new String[]{nid},
				null, null, null);
		cursor.moveToFirst();
		if (!cursor.isAfterLast()) {
			result = true;
		}
		cursor.close();
		return result;
	}
	
	public static boolean deleteYunFile(String nid) {
		SQLiteDatabase db=myHelper.getWritableDatabase();
		long result = db.delete(DB_CREATE_FILE, KEY_NID + "="+ nid, null);
		db.close();
		return result > 0;
	}
	
	public static boolean updateFile(String pid, String fileName) {
		return false;
	}
	
}
