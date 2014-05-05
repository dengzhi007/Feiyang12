package com.qihoo.feiyang.util;

import java.util.ArrayList;

import android.graphics.Bitmap;
import android.provider.ContactsContract.CommonDataKinds.Phone;

public class GlobalsUtil {
	
	public static Bitmap mainAvatar=null;
	public static String nickName;
	public static String totalSize;
	public static String usedSize;
	
	public static String[] contactChangeInfo=new String[]{"laoshiren","honestman"};
	public static String[] contactChangeTime=new String[]{"2014-4-30","2014-5-2"};
	public static String[] contactChangePhone=new String[]{"13858011543","15824125004"};
	
	
	public static String lastChangeTime="2014-4-30";
	
	public static Boolean contactGot=false;
	
	public final static int PHONES_DISPLAY_NAME_INDEX = 0;  
	public final static int PHONES_NUMBER_INDEX = 1;    
	public final static int PHONES_PHOTO_ID_INDEX = 2;  
	public final static int PHONES_CONTACT_ID_INDEX = 3; 
	
	public final static String[] contactInfo= new String[]{Phone.DISPLAY_NAME, Phone.NUMBER, Phone.PHOTO_ID,Phone.CONTACT_ID};
	public static ArrayList<String> contactNames=null;
	public static ArrayList<String> phoneNums=null;
	public static ArrayList<Bitmap> avatars=null;
	public static ArrayList<Long> contactIds=null;
	public static ArrayList<ArrayList<String>> contactDetails=null;
	
	

}
