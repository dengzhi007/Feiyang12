package com.qihoo.feiyang.util;

import java.util.ArrayList;

import android.graphics.Bitmap;
import android.provider.ContactsContract.CommonDataKinds.Phone;

public class GlobalsUtil {
	
	public static Bitmap mainAvatar=null;
	public static String nickName;
	public static String totalSize;
	public static String usedSize;
	
	public static String[] cardinfo=new String[]{"手机","家庭","公司","住址"};
	public static String[] phoneinfo=new String[]{"13858011543","010-11111111","010-78787878","qihoo360"};
	
	public static String[] contactChangeInfo=new String[]{"laoshiren","honestman","大菠萝"};
	public static String[] contactChangeTime=new String[]{"2014-4-30","2014-5-2","2014-5-5"};
	public static String[] contactChangePhone=new String[]{"18611752594","18611747011","18601347404"};
	
	
	public static String lastChangeTime="2014-4-30";
	
	public static Boolean contactGot=false;
	
	public final static int PHONES_DISPLAY_NAME_INDEX = 0;  
	public final static int PHONES_NUMBER_INDEX = 1;    
	public final static int PHONES_PHOTO_ID_INDEX = 2;  
	public final static int PHONES_CONTACT_ID_INDEX = 3; 
	
	public final static String[] contactInfo= new String[]{Phone.DISPLAY_NAME, Phone.NUMBER, Phone.PHOTO_ID,Phone.CONTACT_ID};
	public static ArrayList<String> contactNames=null;
	public static ArrayList<String> contactPhones=null;
	public static ArrayList<Bitmap> contactAvatars=null;
	public static ArrayList<Long> contactIds=null;
	public static ArrayList<ArrayList<String>> contactDetails=null;
	
	

}
