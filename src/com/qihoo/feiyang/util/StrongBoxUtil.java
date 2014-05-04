package com.qihoo.feiyang.util;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;
import android.webkit.WebChromeClient.CustomViewCallback;


public final class StrongBoxUtil {
	private static final String dbname="feiyangDB2.db";
	
	private static StrongBoxSQLiteHelper sqlHelper = null;
	private static SQLiteDatabase readDB = null;
	private static SQLiteDatabase writeDB = null;
	
	private static String KEY_ID = "_id";
	private static final String KEY_NAME = "name";
	private static final String KEY_FULLNAME = "fullname";
	private static final String KEY_NID = "nid";
	private static final String KEY_PID = "pid";
	private static final String TABLE_NAME = "strongbox";
	
	private static final String TABLE_CREATE = "CREATE TABLE " + TABLE_NAME + " ( " + 
								KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " + 
								KEY_NAME + " TEXT, " + 
								KEY_FULLNAME + " TEXT, " + 
								KEY_NID + " TEXT, " + 
								KEY_PID + " TEXT )";
	
	private static void checkNull() {
		assert sqlHelper != null;
		assert readDB != null;
		assert writeDB != null;
	}
	
	public static void init(Context context, int version) {
		if (sqlHelper ==  null) {
			sqlHelper = new StrongBoxSQLiteHelper(context, dbname, null, version);
			readDB = sqlHelper.getReadableDatabase();
			writeDB = sqlHelper.getWritableDatabase();
		}
	}
	
	public static void close() {
		readDB.close();
		writeDB.close();
		sqlHelper.close();
	}
	
	private static class StrongBoxSQLiteHelper extends SQLiteOpenHelper {

		public StrongBoxSQLiteHelper(Context context, String name,
				CursorFactory factory, int version) {
			super(context, name, factory, version);
		}

		@Override
		public void onCreate(SQLiteDatabase db) {
			db.execSQL(TABLE_CREATE);
		}

		@Override
		public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
			// TODO Auto-generated method stub
			// not needed right now
		}
	}
	
	public static boolean addPictureIntoStrongBox(String fullName, String nid, String pid) {
		checkNull();
		ContentValues values = new ContentValues();
		values.put(KEY_FULLNAME, fullName);
		values.put(KEY_NAME, FileUtil.getFileSimpleName(fullName));
		values.put(KEY_NID, nid);
		values.put(KEY_PID, pid);
		long result = writeDB.insert(TABLE_NAME, KEY_ID, values);
		return result != -1;
	}
	
	public static boolean removePictureFromStrongBox(String nid) {
		checkNull();
		writeDB.delete(TABLE_NAME, KEY_NID + "=?", new String[]{nid});
		return true;
	}
	
	public static List<StrongBoxFile> getAllStrongBoxPictures() {
		checkNull();
		Cursor cursor = readDB.query(TABLE_NAME, null, null, null, null, null, null);
		int nameIndex = cursor.getColumnIndex(KEY_NAME);
		int fullNameIndex = cursor.getColumnIndex(KEY_FULLNAME);
		int nidIndex = cursor.getColumnIndex(KEY_NID);
		int pidIndex = cursor.getColumnIndex(KEY_PID);
		List<StrongBoxFile> list = new ArrayList<StrongBoxFile>();
		for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()) {
			String name = cursor.getString(nameIndex);
			String fullName = cursor.getString(fullNameIndex);
			String pid = cursor.getString(pidIndex);
			String nid = cursor.getString(nidIndex);
			list.add(new StrongBoxFile(name, fullName, nid, pid));
		}
		cursor.close();
		return list;
	}
}
