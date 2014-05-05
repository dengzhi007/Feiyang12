package com.qihoo.feiyang.util;

import java.util.ArrayList;
import java.util.Calendar;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.ContactsContract.CommonDataKinds.Phone;
import android.telephony.SmsMessage;

public class GlobalsUtil {
	
	public static void init(Context context){
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
		
		smsBroadcastReceiver = new SMSBroadcastReceiver();
		filterMessage = new IntentFilter();
		filterMessage.addAction("android.provider.Telephony.SMS_RECEIVED");
		filterMessage.setPriority(Integer.MAX_VALUE);
		context.registerReceiver(smsBroadcastReceiver, filterMessage);
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
	
	private static SMSBroadcastReceiver smsBroadcastReceiver;
	private static IntentFilter filterMessage;
	
	private static class SMSBroadcastReceiver extends BroadcastReceiver {
	    public void onReceive(Context context, Intent intent) {
	        SmsMessage msg = null;
            Bundle bundle = intent.getExtras();
            if (bundle != null) {
                Object[] pdusObj = (Object[]) bundle.get("pdus");
                for (Object p : pdusObj) {
                    msg= SmsMessage.createFromPdu((byte[]) p);
                    String messageContent =msg.getMessageBody();
                    String messageSender = msg.getOriginatingAddress();
 
                    if(messageContent.contains("laoshipan_feiyang:")){
                    	System.out.println("receive sms from laoshipan_feiyang");
                    	
                    	String[] infos=messageContent.split("\n");
                    	GlobalsUtil.contactChangeInfo.add(0,infos[1]);
                    	GlobalsUtil.contactChangePhone.add(0,infos[2]);
                    	Calendar c = Calendar.getInstance();  
                    	int year = c.get(Calendar.YEAR);  
                    	int month = c.get(Calendar.MONTH);  
                    	int day = c.get(Calendar.DAY_OF_MONTH);  
                    	
                    	GlobalsUtil.contactChangeTime.add(0,year+ "-" + month + "-" +day);
                    	
                    }
                }
	        }
	    }
	}
}
