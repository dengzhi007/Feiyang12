package com.qihoo.feiyang.util;

import java.util.ArrayList;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.provider.ContactsContract.CommonDataKinds.Phone;

public class GlobalsUtil {
	
	public static void init( ){
		contactChangeInfo = new ArrayList<String>();
		contactChangeInfo.add("laoshiren");
		contactChangeInfo.add("honestman");
		contactChangeInfo.add("大菠萝");
		contactChangeInfo.add("敏锅锅");
		contactChangeInfo.add("龙泽王希");
		
		contactChangeTime = new ArrayList<String>();
		contactChangeTime.add("2014-4-30");
		contactChangeTime.add("2014-5-1");
		contactChangeTime.add("2014-5-2");
		contactChangeTime.add("2014-5-3");
		contactChangeTime.add("2014-5-4");
		
		contactChangePhone = new ArrayList<String>();
		contactChangePhone.add("18611752594");
		contactChangePhone.add("18611747011");
		contactChangePhone.add("18601347404");
		contactChangePhone.add("18500844425");
		contactChangePhone.add("15167150200");
		
		smsBroadcastReceiver = new SMSReceiver();
		filterMessage = new IntentFilter();
		filterMessage.addAction("android.provider.Telephony.SMS_RECEIVED");
		filterMessage.setPriority(Integer.MAX_VALUE);
		
	}
	
	public static Bitmap mainAvatar=null;
	public static String nickName;
	public static String totalSize;
	public static String usedSize;
	
	public static String[] cardinfo=new String[]{"手机","家庭","公司","住址"};
	public static String[] phoneinfo=new String[]{"13858011543","010-11111111","010-78787878","qihoo360"};
	
	public static ArrayList<String> contactChangeInfo=null;
	public static ArrayList<String> contactChangeTime=null;
	public static ArrayList<String> contactChangePhone=null;
	
	
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
	
	public static SMSReceiver smsBroadcastReceiver;
	public static IntentFilter filterMessage;
	
	
}
