package com.qihoo.feiyang.util;

import java.util.Calendar;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.SmsMessage;

public class SMSReceiver extends BroadcastReceiver {

	@Override
	public void onReceive(Context context, Intent intent) {
		// TODO Auto-generated method stub
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
